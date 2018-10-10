package com.tokopedia.flight.searchV2.presentation.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.view.HorizontalProgressBar;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.view.adapter.viewholder.EmptyResultViewHolder;
import com.tokopedia.flight.search.view.model.EmptyResultViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.searchV2.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.searchV2.di.FlightSearchComponent;
import com.tokopedia.flight.searchV2.presentation.adapter.FlightSearchAdapterTypeFactory;
import com.tokopedia.flight.searchV2.presentation.contract.FlightSearchContract;
import com.tokopedia.flight.searchV2.presentation.model.FlightAirportCombineModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightAirportCombineModelList;
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchMetaViewModel;
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel;
import com.tokopedia.flight.searchV2.presentation.presenter.FlightSearchPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.flight.searchV2.presentation.activity.FlightSearchActivity.EXTRA_PASS_DATA;

/**
 * @author by furqan on 01/10/18.
 */

public class FlightSearchFragment extends BaseListFragment<FlightJourneyViewModel, FlightSearchAdapterTypeFactory>
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

    private boolean needRefreshFromCache;
    private boolean needRefreshAirline = true;
    private boolean inFilterMode = false;
    int selectedSortOption;
    private int progress;

    private BottomActionView filterAndSortBottomAction;
    private FlightFilterModel flightFilterModel;
    private HorizontalProgressBar progressBar;
    private SwipeToRefresh swipeToRefresh;

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
        setUpProgress();
        setUpBottomAction(view);
        setUpSwipeRefresh(view);
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

        loadInitialData();
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
    public RecyclerView getRecyclerView(View view) {
        return (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Override
    protected FlightSearchAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightSearchAdapterTypeFactory(this);
    }

    @NonNull
    @Override
    protected BaseListAdapter<FlightJourneyViewModel, FlightSearchAdapterTypeFactory> createAdapterInstance() {
        BaseListAdapter<FlightJourneyViewModel, FlightSearchAdapterTypeFactory> adapter = super.createAdapterInstance();
        ErrorNetworkModel errorNetworkModel = adapter.getErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(R.drawable.ic_flight_empty_state);
        errorNetworkModel.setOnRetryListener(this);
        adapter.setErrorNetworkModel(errorNetworkModel);
        return adapter;
    }

    @Override
    protected boolean callInitialLoadAutomatically() {
        return false;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        onFlightSearchFragmentListener = (OnFlightSearchFragmentListener) context;
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
        return progress >= MAX_PROGRESS;
    }

    @Override
    public boolean isNeedRefreshAirline() {
        return needRefreshAirline;
    }

    @Override
    public void loadInitialData() {
        flightSearchPresenter.initialize();
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    public void fetchFlightSearchData() {
        setUpProgress();
        if (getAdapter().getItemCount() == 0) {
            showLoading();
        }

        flightSearchPresenter.fetchSearchData(passDataViewModel, flightAirportCombineModelList);
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
        float scale = getResources().getDisplayMetrics().density;
        getRecyclerView(getView()).setPadding(
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                (int) (scale * PADDING_SEARCH_LIST + DEFAULT_DIMENS_MULTIPLIER)
        );
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
        selectedSortOption = itemId;
        setUIMarkSort();
    }

    @Override
    public void setNeedRefreshAirline(boolean needRefresh) {
        this.needRefreshAirline = needRefresh;
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
        filterAndSortBottomAction.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyFlightStateView() {
        getAdapter().addElement(getEmptyDataViewModel());
    }

    @Override
    public void showNoRouteFlightEmptyState(String message) {
        getAdapter().clearAllElements();
        getAdapter().addElement(getNoFlightRouteDataViewModel(message));
    }

    @Override
    public void showGetListError(Throwable e) {
        this.addToolbarElevation();
        progressBar.setVisibility(View.GONE);
        removeBottomPaddingForSortAndFilterActionButton();
        super.showGetListError(e);
    }

    @Override
    public void hideHorizontalProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideFilterAndSortView() {
        filterAndSortBottomAction.setVisibility(View.GONE);
    }

    @Override
    public void removeToolbarElevation() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
    }

    @Override
    public void removeBottomPaddingForSortAndFilterActionButton() {
        getRecyclerView(getView()).setPadding(
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                EMPTY_MARGIN
        );
    }

    @Override
    public void clearAdapterData() {
        getAdapter().setElement(new ArrayList<>());
    }

    @Override
    public void finishFragment() {
        getActivity().finish();
    }

    @Override
    public void navigateToNextPage(String selectedId) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener.selectFlight(selectedId);
        }
    }

    @Override
    public void onGetSearchMeta(FlightSearchMetaViewModel flightSearchMetaViewModel) {
        addToolbarElevation();

        String depAirport = flightSearchMetaViewModel.getDepartureAirport();
        String arrivalAirport = flightSearchMetaViewModel.getArrivalAirport();
        FlightAirportCombineModel flightAirportCombineModel = flightAirportCombineModelList.getData(depAirport, arrivalAirport);
        List<String> localListAirlines = flightAirportCombineModel.getAirlines();
        localListAirlines.addAll(flightSearchMetaViewModel.getAirlines());
        flightAirportCombineModel.setAirlines(localListAirlines);
        int size = flightAirportCombineModelList.getData().size();
        int halfProgressAmount = divideTo(divideTo(MAX_PROGRESS, size), 2);
        if (!flightAirportCombineModel.isHasLoad()) {
            flightAirportCombineModel.setHasLoad(true);
            progress += halfProgressAmount;
        }

        if (flightAirportCombineModel.isNeedRefresh()) {
            if (flightSearchMetaViewModel.isNeedRefresh()) {
                int noRetry = flightAirportCombineModel.getNoOfRetry();
                noRetry++;
                flightAirportCombineModel.setNoOfRetry(noRetry);
                progress += divideTo(halfProgressAmount, flightSearchMetaViewModel.getMaxRetry());

                // already reach max retry limit, end retrying.
                if (noRetry >= flightSearchMetaViewModel.getMaxRetry()) {
                    flightAirportCombineModel.setNeedRefresh(false);
                } else {
                    //no retry still below the max retry, do retry
                    //retry load data
                    flightSearchPresenter.fetchSearchDataFromCloudWithDelay(passDataViewModel,
                            flightAirportCombineModel, flightSearchMetaViewModel.getRefreshTime());
                }

            } else {
                flightAirportCombineModel.setNeedRefresh(false);
                progress += (flightSearchMetaViewModel.getMaxRetry() - flightAirportCombineModel.getNoOfRetry()) *
                        divideTo(halfProgressAmount, flightSearchMetaViewModel.getMaxRetry());
            }
        }

        setUpProgress();

        flightSearchPresenter.fetchSortAndFilterLocalData(selectedSortOption, flightFilterModel);
    }

    @Override
    public void onRetryClicked() {
        getAdapter().clearAllElements();
        flightSearchPresenter.initialize();
    }

    @Override
    public void onDetailClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {
        flightSearchPresenter.onSeeDetailItemClicked(journeyViewModel, adapterPosition);
        FlightDetailViewModel flightDetailViewModel = new FlightDetailViewModel();
        flightDetailViewModel.build(journeyViewModel);
        flightDetailViewModel.build(passDataViewModel);
        startActivityForResult(FlightDetailActivity.createIntent(getActivity(),
                flightDetailViewModel, true),
                REQUEST_CODE_SEE_DETAIL_FLIGHT);
    }

    @Override
    public void onItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {
        flightSearchPresenter.onSearchItemClicked(journeyViewModel, adapterPosition);
    }

    @Override
    public void onItemClicked(FlightJourneyViewModel journeyViewModel) {
        flightSearchPresenter.onSearchItemClicked(journeyViewModel);
    }

    @Override
    public void onSwipeRefresh() {
        removeBottomPaddingForSortAndFilterActionButton();
        super.onSwipeRefresh();
    }

    public void onResetFilterClicked() {
        flightFilterModel = new FlightFilterModel();
        getAdapter().clearAllNonDataElement();
        showLoading();
        setUIMarkFilter();
        reloadDataFromCache();
    }

    @Override
    protected boolean isLoadMoreEnabledByDefault() {
        return false;
    }

    @Override
    protected void hideLoading() {
        super.hideLoading();
        hideSwipeRefreshLoad();
    }

    public void onChangeDateClicked() {
        if (!getActivity().isFinishing()) {
            final String dateInput = passDataViewModel.getDate(isReturning());
            Date date = FlightDateUtil.stringToDate(dateInput);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            flightSearchPresenter.onSuccessDateChanged(year, month, dayOfMonth);
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            DatePicker datePicker = datePickerDialog.getDatePicker();
            setMinMaxDatePicker(datePicker);

            datePickerDialog.show();
        }
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
                /*FlightSearchFragment.this.addToolbarElevation();
                startActivityForResult(FlightSearchFilterActivity.createInstance(getActivity(),
                        isReturning(),
                        flightFilterModel),
                        REQUEST_CODE_SEARCH_FILTER);*/
            }
        });
        filterAndSortBottomAction.setVisibility(View.GONE);
    }

    protected Visitable getEmptyDataViewModel() {
        EmptyResultViewModel emptyResultViewModel;
        if (inFilterMode) {
            emptyResultViewModel = new EmptyResultViewModel();
            emptyResultViewModel.setIconRes(R.drawable.ic_flight_empty_state);
            emptyResultViewModel.setContentRes(R.string.flight_there_is_zero_flight_for_the_filter);
            emptyResultViewModel.setButtonTitleRes(R.string.reset_filter);
            emptyResultViewModel.setCallback(new EmptyResultViewHolder.Callback() {
                @Override
                public void onEmptyContentItemTextClicked() {

                }

                @Override
                public void onEmptyButtonClicked() {
                    onResetFilterClicked();
                }
            });


        } else {
            emptyResultViewModel = new EmptyResultViewModel();
            emptyResultViewModel.setIconRes(R.drawable.ic_flight_empty_state);
            emptyResultViewModel.setContentRes(R.string.flight_there_is_no_flight_available);
            emptyResultViewModel.setButtonTitleRes(R.string.change_date);
            emptyResultViewModel.setCallback(new EmptyResultViewHolder.Callback() {
                @Override
                public void onEmptyContentItemTextClicked() {

                }

                @Override
                public void onEmptyButtonClicked() {
                    onChangeDateClicked();
                }
            });
        }
        return emptyResultViewModel;
    }

    protected Visitable getNoFlightRouteDataViewModel(String message) {
        EmptyResultViewModel emptyResultViewModel = new EmptyResultViewModel();
        emptyResultViewModel.setIconRes(R.drawable.ic_flight_empty_state);
        emptyResultViewModel.setTitle(message);
        emptyResultViewModel.setButtonTitleRes(R.string.flight_change_search_content_button);
        emptyResultViewModel.setCallback(new EmptyResultViewHolder.Callback() {
            @Override
            public void onEmptyContentItemTextClicked() {

            }

            @Override
            public void onEmptyButtonClicked() {
                finishFragment();
            }
        });

        return emptyResultViewModel;
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
                flightSearchPresenter.setDelayHorizontalProgress();
            } else {
                progressBar.setProgress(progress);
            }
        }
    }

    private void setMinMaxDatePicker(DatePicker datePicker) {
        Date maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 2);
        maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, -1);
        maxDate = FlightDateUtil.trimDate(maxDate);

        if (isReturning()) {
            String dateDepStr = passDataViewModel.getDate(false);
            Date dateDep = FlightDateUtil.stringToDate(dateDepStr);
            dateDep = FlightDateUtil.trimDate(dateDep);
            datePicker.setMinDate(dateDep.getTime());
            datePicker.setMaxDate(maxDate.getTime());
        } else {
            Date dateNow = FlightDateUtil.trimDate(FlightDateUtil.getCurrentDate());
            datePicker.setMinDate(dateNow.getTime());

            boolean isOneWay = passDataViewModel.isOneWay();
            if (!isOneWay) {
                String dateReturnStr = passDataViewModel.getDate(true);
                Date dateReturn = FlightDateUtil.stringToDate(dateReturnStr);
                dateReturn = FlightDateUtil.trimDate(dateReturn);
                datePicker.setMaxDate(dateReturn.getTime());
            } else {
                datePicker.setMaxDate(maxDate.getTime());
            }
        }
    }

    private void showMessageErrorInSnackbar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    private void hideSwipeRefreshLoad() {
        swipeToRefresh.setEnabled(true);
        swipeToRefresh.setRefreshing(false);
    }

    private int divideTo(int number, int pieces) {
        return (int) Math.ceil(((double) number / pieces));
    }

    public interface OnFlightSearchFragmentListener {
        void selectFlight(String selectedFlightID);

        void changeDate(FlightSearchPassDataViewModel flightSearchPassDataViewModel);
    }
}
