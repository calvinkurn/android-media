package com.tokopedia.flight.searchV2.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.common.view.HorizontalProgressBar;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.searchV2.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.searchV2.di.FlightSearchComponent;
import com.tokopedia.flight.searchV2.presentation.adapter.FlightSearchAdapter;
import com.tokopedia.flight.searchV2.presentation.adapter.FlightSearchAdapterTypeFactory;
import com.tokopedia.flight.searchV2.presentation.contract.FlightSearchContract;
import com.tokopedia.flight.searchV2.presentation.model.FlightAirportCombineModelList;
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchViewModel;
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel;
import com.tokopedia.flight.searchV2.presentation.presenter.FlightSearchPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.flight.searchV2.presentation.activity.FlightSearchActivity.EXTRA_PASS_DATA;

/**
 * @author by furqan on 01/10/18.
 */

public class FlightSearchFragment extends BaseDaggerFragment
        implements FlightSearchContract.View, FlightSearchAdapterTypeFactory.OnFlightSearchListener,
        ErrorNetworkModel.OnRetryListener {

    public static final int MAX_PROGRESS = 100;
    private static final int EMPTY_MARGIN = 0;
    private static final int REQUEST_CODE_SEARCH_FILTER = 1;
    private static final int REQUEST_CODE_SEE_DETAIL_FLIGHT = 2;
    private static final String SAVED_NEED_REFRESH_AIRLINE = "svd_need_refresh_airline";
    private static final String SAVED_FILTER_MODEL = "svd_filter_model";
    private static final String SAVED_SORT_OPTION = "svd_sort_option";
    private static final String SAVED_STAT_MODEL = "svd_stat_model";
    private static final String SAVED_AIRPORT_COMBINE = "svd_airport_combine";
    private static final String SAVED_PROGRESS = "svd_progress";
    private static final float DEFAULT_DIMENS_MULTIPLIER = 0.5f;
    private static final int PADDING_SEARCH_LIST = 60;

    @Inject
    public FlightSearchPresenter flightSearchPresenter;

    protected FlightSearchComponent flightSearchComponent;
    protected FlightSearchPassDataViewModel passDataViewModel;
    protected OnFlightSearchFragmentListener onFlightSearchFragmentListener;
    private FlightAirportCombineModelList flightAirportCombineModelList;
    private FlightSearchViewModel flightSearchViewModel;
    private FlightSearchAdapter adapter;

    private boolean needRefreshFromCache;
    private boolean needRefreshAirline = true;
    private boolean inFilterMode = false;
    int selectedSortOption;
    private int progress;

    private BottomActionView filterAndSortBottomAction;
    private FlightFilterModel flightFilterModel;
    private HorizontalProgressBar progressBar;
    private SwipeToRefresh swipeToRefresh;
    private RecyclerView recyclerView;

    public static FlightSearchFragment newInstance(FlightSearchPassDataViewModel passDataViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PASS_DATA, passDataViewModel);

        FlightSearchFragment fragment = new FlightSearchFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        passDataViewModel = getArguments().getParcelable(EXTRA_PASS_DATA);

        if (savedInstanceState == null) {
            flightFilterModel = new FlightFilterModel();
            selectedSortOption = FlightSortOption.CHEAPEST;
            setUpCombinationAirport();
            progress = 0;
        } else {
            needRefreshAirline = savedInstanceState.getBoolean(SAVED_NEED_REFRESH_AIRLINE);
            flightFilterModel = savedInstanceState.getParcelable(SAVED_FILTER_MODEL);
            selectedSortOption = savedInstanceState.getInt(SAVED_SORT_OPTION);
            flightAirportCombineModelList = savedInstanceState.getParcelable(SAVED_AIRPORT_COMBINE);
            progress = savedInstanceState.getInt(SAVED_PROGRESS, 0);
            setNeedRefreshFromCache(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        progressBar = (HorizontalProgressBar) view.findViewById(R.id.horizontal_progress_bar);
        recyclerView = view.findViewById(R.id.recycler_view);
        setUpProgress();
        setUpBottomAction(view);
        setUpSwipeRefresh(view);
        setUpRecyclerView();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(SAVED_NEED_REFRESH_AIRLINE, needRefreshAirline);
        outState.putParcelable(SAVED_FILTER_MODEL, flightFilterModel);
        outState.putInt(SAVED_SORT_OPTION, selectedSortOption);
        outState.putParcelable(SAVED_AIRPORT_COMBINE, flightAirportCombineModelList);
        outState.putInt(SAVED_PROGRESS, progress);
    }

    @Override
    public void onResume() {
        super.onResume();
        flightSearchPresenter.attachView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        flightSearchPresenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        flightSearchComponent = DaggerFlightSearchComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getActivity().getApplication()))
                .build();

        flightSearchComponent.inject(this);
    }

    @Override
    public FlightSearchPassDataViewModel getFlightSearchPassData() {
        return passDataViewModel;
    }

    @Override
    public boolean isReturning() {
        return false;
    }

    @Override
    public boolean isNeedRefreshFromCache() {
        return needRefreshFromCache;
    }

    @Override
    public boolean isDoneLoadData() {
        return false;
    }

    @Override
    public boolean isNeedRefreshAirline() {
        return false;
    }

    @Override
    public void loadInitialData() {
        flightSearchPresenter.initialize();
    }

    @Override
    public void fetchFlightSearchData() {

    }

    @Override
    public void reloadDataFromCache() {

    }

    @Override
    public void addToolbarElevation() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(
                getResources().getDimension(R.dimen.dp_4));
    }

    @Override
    public void addBottomPaddingForSortAndFilterActionButton() {

    }

    @Override
    public void setUIMarkFilter() {
        if (flightFilterModel.hasFilter()) {
            filterAndSortBottomAction.setMarkLeft(true);
            inFilterMode = true;
        } else {
            filterAndSortBottomAction.setMarkLeft(false);
            inFilterMode = false;
        }
    }

    @Override
    public void setNeedRefreshFromCache(boolean needRefreshFromCache) {
        this.needRefreshFromCache = needRefreshFromCache;
    }

    @Override
    public void setFlightSearchPassData(FlightSearchPassDataViewModel passDataViewModel) {
        this.passDataViewModel = passDataViewModel;
    }

    @Override
    public void setSelectedSortItem(int itemId) {

    }

    @Override
    public void setNeedRefreshAirline(boolean needRefresh) {

    }

    @Override
    public void renderFlightSearch(List<FlightJourneyViewModel> journeyViewModelList) {

    }

    @Override
    public void showDepartureDateMaxTwoYears(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showDepartureDateShouldAtLeastToday(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showReturnDateShouldGreaterOrEqual(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showFilterAndSortView() {

    }

    @Override
    public void showEmptyFlightStateView() {

    }

    @Override
    public void showNoRouteFlightEmptyState(String message) {

    }

    @Override
    public void hideHorizontalProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideFilterAndSortView() {

    }

    @Override
    public void removeToolbarElevation() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
    }

    @Override
    public void removeBottomPaddingForSortAndFilterActionButton() {

    }

    @Override
    public void clearAdapterData() {

    }

    @Override
    public void finishFragment() {
        getActivity().finish();
    }

    @Override
    public void navigateToNextPage(String selectedId) {

    }

    @Override
    public void onRetryClicked() {

    }

    @Override
    public void onDetailClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {

    }

    @Override
    public void onItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {

    }

    protected FlightAirportViewModel getDepartureAirport() {
        return passDataViewModel.getDepartureAirport();
    }

    protected FlightAirportViewModel getArrivalAirport() {
        return passDataViewModel.getArrivalAirport();
    }

    protected int getLayout() {
        return R.layout.fragment_search_flight;
    }

    protected void setUpSwipeRefresh(View view) {
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        swipeToRefresh.setSwipeDistance();
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hideLoading();
                swipeToRefresh.setEnabled(false);
//                resetDateAndReload();
            }
        });
    }

    protected void setUpBottomAction(View view) {
        filterAndSortBottomAction = (BottomActionView) view.findViewById(R.id.bottom_action_filter_sort);
        filterAndSortBottomAction.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                        .setMode(BottomSheetBuilder.MODE_LIST)
                        .addTitleItem(getString(R.string.flight_search_sort_title));

                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.CHEAPEST, getString(R.string.flight_search_sort_item_cheapest_price), null, selectedSortOption == FlightSortOption.CHEAPEST);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.MOST_EXPENSIVE, getString(R.string.flight_search_sort_item_most_expensive_price), null, selectedSortOption == FlightSortOption.MOST_EXPENSIVE);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.EARLIEST_DEPARTURE, getString(R.string.flight_search_sort_item_earliest_departure), null, selectedSortOption == FlightSortOption.EARLIEST_DEPARTURE);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.LATEST_DEPARTURE, getString(R.string.flight_search_sort_item_latest_departure), null, selectedSortOption == FlightSortOption.LATEST_DEPARTURE);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.SHORTEST_DURATION, getString(R.string.flight_search_sort_item_shortest_duration), null, selectedSortOption == FlightSortOption.SHORTEST_DURATION);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.LONGEST_DURATION, getString(R.string.flight_search_sort_item_longest_duration), null, selectedSortOption == FlightSortOption.LONGEST_DURATION);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.EARLIEST_ARRIVAL, getString(R.string.flight_search_sort_item_earliest_arrival), null, selectedSortOption == FlightSortOption.EARLIEST_ARRIVAL);
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(FlightSortOption.LATEST_ARRIVAL, getString(R.string.flight_search_sort_item_latest_arrival), null, selectedSortOption == FlightSortOption.LATEST_ARRIVAL);

                BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                        .setItemClickListener(new BottomSheetItemClickListener() {
                            @SuppressWarnings("WrongConstant")
                            @Override
                            public void onBottomSheetItemClick(MenuItem item) {
                                /*if (getAdapter().getData() != null) {
                                    flightSearchPresenter.sortFlight(getAdapter().getData(), item.getItemId());
                                }*/
                            }
                        })
                        .createDialog();
                bottomSheetDialog.show();
            }
        });

        setUIMarkSort();
        setUIMarkFilter();

        filterAndSortBottomAction.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                flightSearchPresenter.getFlightStatistic(isReturning());
/*                FlightSearchFragment.this.addToolbarElevation();
                startActivityForResult(FlightSearchFilterActivity.createInstance(getActivity(),
                        isReturning(),
                        flightFilterModel),
                        REQUEST_CODE_SEARCH_FILTER);*/
            }
        });
        filterAndSortBottomAction.setVisibility(View.GONE);
    }

    private void setUpRecyclerView() {
        FlightSearchAdapterTypeFactory adapterTypeFactory = new FlightSearchAdapterTypeFactory(this);
        adapter = new FlightSearchAdapter(adapterTypeFactory, new ArrayList<>());

        ErrorNetworkModel errorNetworkModel = adapter.getErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(R.drawable.ic_flight_empty_state);
        errorNetworkModel.setOnRetryListener(this);
        adapter.setErrorNetworkModel(errorNetworkModel);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        showLoading();
    }

    private void setUpCombinationAirport() {
        List<String> departureAirportList;
        String departureAirportCode = getDepartureAirport().getAirportCode();
        if (departureAirportCode == null || departureAirportCode.equals("")) {
            departureAirportList = Arrays.asList(getDepartureAirport().getCityAirports());
        } else {
            departureAirportList = new ArrayList<>();
            departureAirportList.add(departureAirportCode);
        }

        List<String> arrivalAirportList;
        String arrivalAirportCode = getArrivalAirport().getAirportCode();
        if (arrivalAirportCode == null || arrivalAirportCode.equals("")) {
            arrivalAirportList = Arrays.asList(getArrivalAirport().getCityAirports());
        } else {
            arrivalAirportList = new ArrayList<>();
            arrivalAirportList.add(arrivalAirportCode);
        }

        flightAirportCombineModelList = new FlightAirportCombineModelList(departureAirportList, arrivalAirportList);
    }

    private void setUIMarkSort() {
        if (selectedSortOption == FlightSortOption.NO_PREFERENCE) {
            filterAndSortBottomAction.setMarkRight(false);
        } else {
            filterAndSortBottomAction.setMarkRight(true);
        }
    }

    private void setUpProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            if (progress >= MAX_PROGRESS) {
                progress = MAX_PROGRESS;
                progressBar.setProgress(MAX_PROGRESS);
//                flightSearchPresenter.setDelayHorizontalProgress();
            } else {
                progressBar.setProgress(progress);
            }
        }
    }

    private void showLoading() {
        adapter.showLoading();
    }

    private void hideLoading() {
        swipeToRefresh.setEnabled(true);
        swipeToRefresh.setRefreshing(false);

        adapter.hideLoading();
    }

    private void showMessageErrorInSnackbar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    public interface OnFlightSearchFragmentListener {
        void selectFlight(String selectedFlightID);

        void changeDate(FlightSearchPassDataViewModel flightSearchPassDataViewModel);
    }
}
