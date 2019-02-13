package com.tokopedia.flight.search.presentation.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.common.travel.constant.TravelSortOption;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.view.HorizontalProgressBar;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.search.di.FlightSearchComponent;
import com.tokopedia.flight.search.presentation.activity.FlightSearchFilterActivity;
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory;
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder;
import com.tokopedia.flight.search.presentation.contract.FlightSearchContract;
import com.tokopedia.flight.search.presentation.model.EmptyResultViewModel;
import com.tokopedia.flight.search.presentation.model.FlightAirportCombineModel;
import com.tokopedia.flight.search.presentation.model.FlightAirportCombineModelList;
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchMetaViewModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.presentation.presenter.FlightSearchPresenter;
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.flight.search.presentation.activity.FlightSearchActivity.EXTRA_PASS_DATA;

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
    private static final int REQUEST_CODE_CHANGE_DATE = 3;
    private static final String SAVED_NEED_REFRESH_AIRLINE = "svd_need_refresh_airline";
    private static final String SAVED_FILTER_MODEL = "svd_filter_model";
    private static final String SAVED_SORT_OPTION = "svd_sort_option";
    private static final String SAVED_STAT_MODEL = "svd_stat_model";
    private static final String SAVED_AIRPORT_COMBINE = "svd_airport_combine";
    private static final String SAVED_PROGRESS = "svd_progress";
    private static final String FLIGHT_SEARCH_TRACE = "tr_flight_search";
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
    private PerformanceMonitoring performanceMonitoring;
    private boolean traceStop = false;

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
            flightFilterModel = buildFilterModel(new FlightFilterModel());
            selectedSortOption = TravelSortOption.CHEAPEST;
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

        performanceMonitoring = PerformanceMonitoring.start(FLIGHT_SEARCH_TRACE);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flightSearchPresenter.attachView(this);
        searchFlightData();
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
        flightSearchPresenter.fetchSortAndFilterLocalData(selectedSortOption, flightFilterModel, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        flightSearchPresenter.unsubscribeAll();
    }

    @Override
    public void onDestroy() {
        flightSearchPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SEARCH_FILTER:
                    if (data != null && data.hasExtra(FlightSearchFilterActivity.EXTRA_FILTER_MODEL)) {
                        flightFilterModel = (FlightFilterModel) data.getExtras().get(FlightSearchFilterActivity.EXTRA_FILTER_MODEL);
                        flightFilterModel = buildFilterModel(flightFilterModel);

                        flightSearchPresenter.fetchSortAndFilterLocalData(selectedSortOption, flightFilterModel, false);
                        setNeedRefreshFromCache(true);
                    }
                    break;
                case REQUEST_CODE_SEE_DETAIL_FLIGHT:
                    if (data != null && data.hasExtra(FlightDetailActivity.EXTRA_FLIGHT_SELECTED)) {
                        String selectedId = data.getStringExtra(FlightDetailActivity.EXTRA_FLIGHT_SELECTED);
                        if (!TextUtils.isEmpty(selectedId)) {
                            onSelectedFromDetail(selectedId);
                        }
                    }
                    break;
            }
        }
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

        flightSearchPresenter.attachView(this);
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
        return true;
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
    public void renderSearchList(List<FlightJourneyViewModel> list, boolean needRefresh) {
        if (!needRefresh || list.size() > 0) {
            renderList(list);
        }

        if (list.size() > 0) {
            showFilterAndSortView();
        }

    }

    @Override
    public void renderList(@NonNull List<FlightJourneyViewModel> list) {
        hideLoading();
        // remove all unneeded element (empty/retry/loading/etc)
        if (isLoadingInitialData) {
            clearAllData();
        } else {
            getAdapter().clearAllNonDataElement();
        }
        getAdapter().addElement(list);
        // update the load more state (paging/can loadmore)
        updateScrollListenerState(false);

        if (isListEmpty() && flightSearchPresenter.isDoneLoadData()) {
            // Note: add element should be the last in line.
            getAdapter().addElement(getEmptyDataViewModel());
        } else {
            //set flag to false, indicate that the initial data has been set.
            isLoadingInitialData = false;
        }
    }

    @Override
    public boolean isListEmpty() {
        return !getAdapter().isContainData();
    }

    @Override
    public void addToolbarElevation() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(
                getResources().getDimension(R.dimen.dp_4));
    }

    @Override
    public void addProgress(int numberToAdd) {
        progress += numberToAdd;
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
        hideLoading();
        // Note: add element should be the last in line.
        if (!getAdapter().isContainData()) {
            onGetListErrorWithEmptyData(e);
        }
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
        getAdapter().setElements(new ArrayList<>());
    }

    @Override
    public void finishFragment() {
        getActivity().finish();
    }

    @Override
    public void navigateToNextPage(String selectedId, FlightPriceViewModel fareViewModel, boolean isBestPairing) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener.selectFlight(selectedId, fareViewModel, isBestPairing);
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

        flightSearchPresenter.fetchSortAndFilterLocalData(selectedSortOption, flightFilterModel,
                flightAirportCombineModel.isNeedRefresh());
    }

    @Override
    public void onSuccessGetDetailFlightDeparture(FlightJourneyViewModel flightJourneyViewModel) {
        // do nothing
    }

    @Override
    public void onErrorDeleteFlightCache(Throwable throwable) {
        resetDateAndReload();
    }

    @Override
    public void onSuccessDeleteFlightCache() {
        resetDateAndReload();
    }

    @Override
    public FlightFilterModel getFilterModel() {
        return flightFilterModel;
    }

    @Override
    public void traceStop() {
        if (!traceStop) {
            performanceMonitoring.stopTrace();
            traceStop = true;
        }
    }

    @Override
    public void onRetryClicked() {
        getAdapter().clearAllElements();
        flightSearchPresenter.resetCounterCall();
        fetchFlightSearchData();
    }

    @Override
    public void onDetailClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {
        flightSearchPresenter.onSeeDetailItemClicked(journeyViewModel, adapterPosition);
        FlightDetailViewModel flightDetailViewModel = new FlightDetailViewModel();
        flightDetailViewModel.build(journeyViewModel);
        flightDetailViewModel.build(passDataViewModel);

        if (journeyViewModel.getFare().getAdultNumericCombo() != 0) {
            flightDetailViewModel.setTotal(journeyViewModel.getComboPrice());
            flightDetailViewModel.setTotalNumeric(journeyViewModel.getComboPriceNumeric());
            flightDetailViewModel.setAdultNumericPrice(journeyViewModel.getFare().getAdultNumericCombo());
            flightDetailViewModel.setChildNumericPrice(journeyViewModel.getFare().getChildNumericCombo());
            flightDetailViewModel.setInfantNumericPrice(journeyViewModel.getFare().getInfantNumericCombo());
        }

        startActivityForResult(FlightDetailActivity.createIntent(getActivity(),
                flightDetailViewModel, true),
                REQUEST_CODE_SEE_DETAIL_FLIGHT);
    }

    @Override
    public void onItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {
        flightSearchPresenter.onSearchItemClicked(journeyViewModel, adapterPosition);
    }

    @Override
    public void onShowAllClicked() {
        // need in return search
    }

    @Override
    public void onShowBestPairingClicked() {
        // need in return search
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
        flightFilterModel = buildFilterModel(new FlightFilterModel());
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
            Date maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 2);
            maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, -1);
            maxDate = FlightDateUtil.trimDate(maxDate);
            String title = getString(R.string.travel_calendar_label_choose_departure_trip_date);
            Date minDate;

            if (isReturning()) {
                String dateDepStr = passDataViewModel.getDate(false);
                Date dateDep = FlightDateUtil.stringToDate(dateDepStr);
                minDate = FlightDateUtil.trimDate(dateDep);
                title = getString(R.string.travel_calendar_label_choose_return_trip_date);
            } else {
                minDate = FlightDateUtil.trimDate(FlightDateUtil.getCurrentDate());

                boolean isOneWay = passDataViewModel.isOneWay();
                if (!isOneWay) {
                    String dateReturnStr = passDataViewModel.getDate(true);
                    Date dateReturn = FlightDateUtil.stringToDate(dateReturnStr);
                    maxDate = FlightDateUtil.trimDate(dateReturn);
                }
            }

            final String dateInput = passDataViewModel.getDate(isReturning());
            Date date = FlightDateUtil.stringToDate(dateInput);
            TravelCalendarBottomSheet travelCalendarBottomSheet = new TravelCalendarBottomSheet.Builder()
                    .setShowHoliday(true)
                    .setMinDate(minDate)
                    .setMaxDate(maxDate)
                    .setTitle(title)
                    .setSelectedDate(date)
                    .setBottomSheetState(BottomSheets.BottomSheetsState.NORMAL)
                    .build();
            travelCalendarBottomSheet.setListener(new TravelCalendarBottomSheet.ActionListener() {
                @Override
                public void onClickDate(@NotNull Date dateSelected) {
                    Calendar calendar = FlightDateUtil.getCurrentCalendar();
                    calendar.setTime(dateSelected);
                    flightSearchPresenter.resetCounterCall();
                    flightSearchPresenter.onSuccessDateChanged(calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                }
            });
            travelCalendarBottomSheet.show(getActivity().getSupportFragmentManager(), "travel calendar");
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
        swipeToRefresh.setOnRefreshListener(() -> {
            hideLoading();
            swipeToRefresh.setEnabled(false);
            resetDateAndReload();
        });
    }

    protected void setUpBottomAction(View view) {
        filterAndSortBottomAction = view.findViewById(R.id.bottom_action_filter_sort);
        filterAndSortBottomAction.setButton2OnClickListener(v -> {
            BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                    .setMode(BottomSheetBuilder.MODE_LIST)
                    .addTitleItem(getString(R.string.flight_search_sort_title));

            ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TravelSortOption.CHEAPEST, getString(R.string.flight_search_sort_item_cheapest_price), null, selectedSortOption == TravelSortOption.CHEAPEST);
            ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TravelSortOption.MOST_EXPENSIVE, getString(R.string.flight_search_sort_item_most_expensive_price), null, selectedSortOption == TravelSortOption.MOST_EXPENSIVE);
            ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TravelSortOption.EARLIEST_DEPARTURE, getString(R.string.flight_search_sort_item_earliest_departure), null, selectedSortOption == TravelSortOption.EARLIEST_DEPARTURE);
            ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TravelSortOption.LATEST_DEPARTURE, getString(R.string.flight_search_sort_item_latest_departure), null, selectedSortOption == TravelSortOption.LATEST_DEPARTURE);
            ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TravelSortOption.SHORTEST_DURATION, getString(R.string.flight_search_sort_item_shortest_duration), null, selectedSortOption == TravelSortOption.SHORTEST_DURATION);
            ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TravelSortOption.LONGEST_DURATION, getString(R.string.flight_search_sort_item_longest_duration), null, selectedSortOption == TravelSortOption.LONGEST_DURATION);
            ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TravelSortOption.EARLIEST_ARRIVAL, getString(R.string.flight_search_sort_item_earliest_arrival), null, selectedSortOption == TravelSortOption.EARLIEST_ARRIVAL);
            ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TravelSortOption.LATEST_ARRIVAL, getString(R.string.flight_search_sort_item_latest_arrival), null, selectedSortOption == TravelSortOption.LATEST_ARRIVAL);

            BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                    .setItemClickListener(item -> {
                        if (getAdapter().getData() != null) {
                            selectedSortOption = item.getItemId();
                            flightSearchPresenter.fetchSortAndFilterLocalData(selectedSortOption, flightFilterModel, false);
                        }
                    })
                    .createDialog();
            bottomSheetDialog.show();
        });

        setUIMarkSort();
        setUIMarkFilter();

        filterAndSortBottomAction.setButton1OnClickListener(v -> {
            FlightSearchFragment.this.addToolbarElevation();
            startActivityForResult(FlightSearchFilterActivity.createInstance(getActivity(),
                    isReturning(),
                    flightFilterModel),
                    REQUEST_CODE_SEARCH_FILTER);
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

    protected void onSelectedFromDetail(String selectedId) {
        flightSearchPresenter.onSearchItemClicked(selectedId);
    }

    protected FlightFilterModel buildFilterModel(FlightFilterModel flightFilterModel) {
        return flightFilterModel;
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
        if (selectedSortOption == TravelSortOption.NO_PREFERENCE) {
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

    private void resetDateAndReload() {
        flightSearchPresenter.detachView();

        onFlightSearchFragmentListener.changeDate(passDataViewModel);

        setUpCombinationAirport();
        progressBar.setVisibility(View.VISIBLE);
        progress = 0;
        filterAndSortBottomAction.setVisibility(View.GONE);

        flightSearchPresenter.attachView(this);
        clearAllData();
        showLoading();

        flightSearchPresenter.resetCounterCall();

        searchFlightData();
    }

    protected void searchFlightData() {
        if (!isReturning()) {
            flightSearchPresenter.fetchCombineData(passDataViewModel);
        } else {
            fetchFlightSearchData();
        }
    }

    public interface OnFlightSearchFragmentListener {
        void selectFlight(String selectedFlightID, FlightPriceViewModel flightPriceViewModel, boolean isBestPairing);

        void changeDate(FlightSearchPassDataViewModel flightSearchPassDataViewModel);
    }
}
