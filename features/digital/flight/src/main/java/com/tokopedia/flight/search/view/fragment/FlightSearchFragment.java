package com.tokopedia.flight.search.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common.travel.constant.TravelSortOption;
import com.tokopedia.common.travel.presentation.dialog.TravelSearchSortBottomSheet;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.common.util.FlightRequestUtil;
import com.tokopedia.flight.common.view.HorizontalProgressBar;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.search.di.FlightSearchComponent;
import com.tokopedia.flight.search.presenter.FlightSearchPresenter;
import com.tokopedia.flight.search.view.FlightSearchView;
import com.tokopedia.flight.search.view.activity.FlightSearchFilterActivity;
import com.tokopedia.flight.search.view.adapter.FilterSearchAdapterTypeFactory;
import com.tokopedia.flight.search.view.adapter.viewholder.EmptyResultViewHolder;
import com.tokopedia.flight.search.view.model.AirportCombineModelList;
import com.tokopedia.flight.search.view.model.EmptyResultViewModel;
import com.tokopedia.flight.search.view.model.FlightAirportCombineModel;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight_dbflow.FlightMetaDataDB;
import com.tokopedia.travelcalendar.view.TravelCalendarActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by hendry on 10/26/2017.
 */

public class FlightSearchFragment extends BaseListFragment<FlightSearchViewModel, FilterSearchAdapterTypeFactory> implements FlightSearchView,
        FilterSearchAdapterTypeFactory.OnFlightSearchListener {

    public static final String TAG = FlightSearchFragment.class.getSimpleName();
    public static final int MAX_PROGRESS = 100;
    protected static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
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
    private static final int REQUEST_CODE_CHANGE_DATE = 1001;

    @Inject
    public FlightSearchPresenter flightSearchPresenter;
    protected FlightSearchPassDataViewModel flightSearchPassDataViewModel;
    protected OnFlightSearchFragmentListener onFlightSearchFragmentListener;
    protected FlightSearchComponent flightSearchComponent;
    int selectedSortOption;
    private BottomActionView filterAndSortBottomAction;
    private FlightFilterModel flightFilterModel;
    private HorizontalProgressBar progressBar;
    private int progress;
    private AirportCombineModelList airportCombineModelList;
    private SwipeToRefresh swipeToRefresh;
    private boolean needRefreshFromCache;
    private boolean needRefreshAirline = true;
    private boolean inFilterMode = false;

    public static FlightSearchFragment newInstance(FlightSearchPassDataViewModel passDataViewModel) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PASS_DATA, passDataViewModel);
        FlightSearchFragment fragment = new FlightSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flightSearchPassDataViewModel = getArguments().getParcelable(EXTRA_PASS_DATA);

        if (savedInstanceState == null) {
            flightFilterModel = new FlightFilterModel();
            selectedSortOption = TravelSortOption.CHEAPEST;
            setUpCombinationAirport();
            progress = 0;
        } else {
            needRefreshAirline = savedInstanceState.getBoolean(SAVED_NEED_REFRESH_AIRLINE);
            flightFilterModel = savedInstanceState.getParcelable(SAVED_FILTER_MODEL);
            selectedSortOption = savedInstanceState.getInt(SAVED_SORT_OPTION);
            airportCombineModelList = savedInstanceState.getParcelable(SAVED_AIRPORT_COMBINE);
            progress = savedInstanceState.getInt(SAVED_PROGRESS, 0);
            setNeedRefreshFromCache(true);
        }
    }

    private void setUpCombinationAirport() {
        List<String> departureAirportList;
        String depAirportID = getDepartureAirport().getAirportCode();
        if (TextUtils.isEmpty(depAirportID)) {
            String[] depAirportIDs = getDepartureAirport().getCityAirports();
            departureAirportList = Arrays.asList(depAirportIDs);
        } else {
            departureAirportList = new ArrayList<>();
            departureAirportList.add(depAirportID);
        }

        List<String> arrivalAirportList;
        String arrAirportID = getArrivalAirport().getAirportCode();
        if (TextUtils.isEmpty(arrAirportID)) {
            String[] arrAirportIDs = getArrivalAirport().getCityAirports();
            arrivalAirportList = Arrays.asList(arrAirportIDs);
        } else {
            arrivalAirportList = new ArrayList<>();
            arrivalAirportList.add(arrAirportID);
        }

        airportCombineModelList = new AirportCombineModelList(departureAirportList, arrivalAirportList);
    }

    protected FlightAirportViewModel getDepartureAirport() {
        return flightSearchPassDataViewModel.getDepartureAirport();
    }

    protected FlightAirportViewModel getArrivalAirport() {
        return flightSearchPassDataViewModel.getArrivalAirport();
    }

    @Override
    protected void initInjector() {
        flightSearchComponent = DaggerFlightSearchComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getActivity().getApplication()))
                .build();

        flightSearchComponent
                .inject(this);
        flightSearchPresenter.attachView(this);
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        return (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Override
    protected FilterSearchAdapterTypeFactory getAdapterTypeFactory() {
        return new FilterSearchAdapterTypeFactory(this);
    }

    @NonNull
    @Override
    protected BaseListAdapter<FlightSearchViewModel, FilterSearchAdapterTypeFactory> createAdapterInstance() {
        BaseListAdapter<FlightSearchViewModel, FilterSearchAdapterTypeFactory> adapter = super.createAdapterInstance();
        ErrorNetworkModel errorNetworkModel = adapter.getErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(R.drawable.ic_flight_empty_state);
        errorNetworkModel.setOnRetryListener(this);
        adapter.setErrorNetworkModel(errorNetworkModel);
        return adapter;
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

    protected void setUpSwipeRefresh(View view) {
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        swipeToRefresh.setSwipeDistance();
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hideLoading();
                swipeToRefresh.setEnabled(false);
                resetDateAndReload();
            }
        });
    }

    protected int getLayout() {
        return R.layout.fragment_search_flight;
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

    @Override
    public boolean isReturning() {
        return false;
    }

    @Override
    public FlightSearchPassDataViewModel getFlightSearchPassData() {
        return flightSearchPassDataViewModel;
    }

    @Override
    public void setFlightSearchPassData(FlightSearchPassDataViewModel flightSearchPassData) {
        this.flightSearchPassDataViewModel = flightSearchPassData;
    }

    @Override
    public void showDepartureDateMaxTwoYears(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showDepartureDateShouldAtLeastToday(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showReturnDateShouldGreaterOrEqual(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void finishFragment() {
        getActivity().finish();
    }

    @Override
    public boolean isNeedRefreshFromCache() {
        return needRefreshFromCache;
    }

    @Override
    public void setNeedRefreshFromCache(boolean needRefreshFromCache) {
        this.needRefreshFromCache = needRefreshFromCache;
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        flightSearchPresenter.attachView(this);

        flightSearchPresenter.checkCacheExpired();
    }

    @Override
    public void loadInitialData() {
        flightSearchPresenter.initialize();
    }

    @Override
    public void loadData(int page) {
        // no op, load data handled manually in onResume
    }

    @Override
    protected boolean callInitialLoadAutomatically() {
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        flightSearchPresenter.detachView();
    }

    @Override
    public void hideHorizontalProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void removeToolbarElevation() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
    }

    @Override
    public void addToolbarElevation() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(getResources().getDimension(R.dimen.dp_4));
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onItemClicked(FlightSearchViewModel flightSearchViewModel) {
        flightSearchPresenter.onSearchItemClicked(flightSearchViewModel);
    }

    @Override
    public void onItemClicked(FlightSearchViewModel flightSearchViewModel, int adapterPosition) {
        flightSearchPresenter.onSearchItemClicked(flightSearchViewModel, adapterPosition);

    }

    public void actionFetchFlightSearchData() {
        setUpProgress();
        if (getAdapter().getItemCount() == 0) {
            showLoading();
        }
        String date = flightSearchPassDataViewModel.getDate(isReturning());
        FlightPassengerViewModel flightPassengerViewModel = flightSearchPassDataViewModel.getFlightPassengerViewModel();
        int adult = flightPassengerViewModel.getAdult();
        int child = flightPassengerViewModel.getChildren();
        int infant = flightPassengerViewModel.getInfant();
        int classID = flightSearchPassDataViewModel.getFlightClass().getId();
        boolean anyLoadToCloud = false;
        for (int i = 0, sizei = airportCombineModelList.getData().size(); i < sizei; i++) {
            FlightAirportCombineModel flightAirportCombineModel = airportCombineModelList.getData().get(i);
            boolean needLoadFromCloud = true;
            if (flightAirportCombineModel.isHasLoad() && !flightAirportCombineModel.isNeedRefresh()) {
                needLoadFromCloud = false;
            }
            if (needLoadFromCloud) {
                anyLoadToCloud = true;
                FlightSearchApiRequestModel flightSearchApiRequestModel = new FlightSearchApiRequestModel(
                        flightAirportCombineModel.getDepAirport(), flightAirportCombineModel.getArrAirport(),
                        date, adult, child, infant, classID, flightAirportCombineModel.getAirlines(),
                        FlightRequestUtil.getLocalIpAddress());
                flightSearchPresenter.searchAndSortFlight(flightSearchApiRequestModel,
                        isReturning(), false, flightFilterModel, selectedSortOption);
            }
        }
        if (!anyLoadToCloud) {
            reloadDataFromCache();
        }
    }

    @Override
    public void setNeedRefreshAirline(boolean needRefresh) {
        needRefreshAirline = needRefresh;
    }

    @Override
    public boolean isNeedRefreshAirline() {
        return needRefreshAirline;
    }

    @Override
    public void navigateToNextPage(String selectedId) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener.selectFlight(selectedId);
        }
    }

    /**
     * load all data from cache
     */
    @Override
    public void reloadDataFromCache() {
        flightSearchPresenter.searchAndSortFlight(null,
                isReturning(), true, flightFilterModel, selectedSortOption);
    }

    protected void setUpBottomAction(View view) {
        filterAndSortBottomAction = (BottomActionView) view.findViewById(R.id.bottom_action_filter_sort);
        filterAndSortBottomAction.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelSearchSortBottomSheet bottomSheet = new TravelSearchSortBottomSheet();
                bottomSheet.setIdSortSelected(selectedSortOption);
                bottomSheet.setListener(new TravelSearchSortBottomSheet.ActionListener() {
                    @Override
                    public void onClickSortLabel(int idSort) {
                        if (getAdapter().getData() != null) {
                            flightSearchPresenter.sortFlight(getAdapter().getData(), idSort);
                        }
                    }
                });
                bottomSheet.show(getChildFragmentManager(), getString(R.string.flight_bottom_sheet_tag));
            }
        });

        setUIMarkSort();
        setUIMarkFilter();

        filterAndSortBottomAction.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        filterAndSortBottomAction.setVisibility(View.GONE);
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

    private void setUIMarkSort() {
        if (selectedSortOption == TravelSortOption.NO_PREFERENCE) {
            filterAndSortBottomAction.setMarkRight(false);
        } else {
            filterAndSortBottomAction.setMarkRight(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SEARCH_FILTER:
                    if (data != null && data.hasExtra(FlightSearchFilterActivity.EXTRA_FILTER_MODEL)) {
                        flightFilterModel = (FlightFilterModel) data.getExtras().get(FlightSearchFilterActivity.EXTRA_FILTER_MODEL);
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
                case REQUEST_CODE_CHANGE_DATE:
                    flightSearchPresenter.attachView(this);
                    Date dateString = (Date) data.getSerializableExtra(TravelCalendarActivity.DATE_SELECTED);
                    Calendar calendarSelected = Calendar.getInstance();
                    calendarSelected.setTime(dateString);
                    flightSearchPresenter.onSuccessDateChanged(calendarSelected.get(Calendar.YEAR),
                            calendarSelected.get(Calendar.MONTH), calendarSelected.get(Calendar.DATE));
                    break;
            }
        }
    }

    protected void onSelectedFromDetail(String selectedId) {
        flightSearchPresenter.onSearchItemClicked(selectedId);
    }

    @Override
    public void onErrorGetDetailFlightDeparture(Throwable e) {
        // do nothing
    }

    @Override
    public void onSwipeRefresh() {
        removeBottomPaddingForSortAndFilterActionButton();
        super.onSwipeRefresh();
    }

    @Override
    public void onSuccessGetDetailFlightDeparture(FlightSearchViewModel flightSearchViewModel) {
        // do nothing
    }

    @Override
    public void showFilterAndSortView() {
        filterAndSortBottomAction.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFilterAndSortView() {
        filterAndSortBottomAction.setVisibility(View.GONE);
    }

    @Override
    public void clearAdapterData() {
        getAdapter().setElements(new ArrayList<>());
    }

    @Override
    public void renderFlightSearchFromCache(List<FlightSearchViewModel> flightSearchViewModels) {
        getAdapter().addElement(flightSearchViewModels);
    }

    @Override
    public void addBottomPaddingForSortAndFilterActionButton() {
        float scale = getResources().getDisplayMetrics().density;
        RecyclerView recyclerView = getRecyclerView(getView());
        recyclerView.setPadding(
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                (int) (scale * PADDING_SEARCH_LIST + DEFAULT_DIMENS_MULTIPLIER)
        );
    }

    @Override
    public boolean isAlreadyFullLoadData() {
        return progress >= MAX_PROGRESS;
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
    public void removeBottomPaddingForSortAndFilterActionButton() {
        RecyclerView recyclerView = getRecyclerView(getView());
        recyclerView.setPadding(
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                EMPTY_MARGIN
        );
    }

    @Override
    protected void hideLoading() {
        super.hideLoading();
        hideSwipeRefreshLoad();
    }

    private void hideSwipeRefreshLoad() {
        swipeToRefresh.setEnabled(true);
        swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void onSuccessGetDataFromCloud(boolean isDataEmpty, FlightMetaDataDB flightMetaDataDB, List<String> airlines) {
        this.addToolbarElevation();
        String depAirport = flightMetaDataDB.getDepartureAirport();
        String arrivalAirport = flightMetaDataDB.getArrivalAirport();
        FlightAirportCombineModel flightAirportCombineModel = airportCombineModelList.getData(depAirport, arrivalAirport);
        List<String> localListAirlines = flightAirportCombineModel.getAirlines();
        localListAirlines.addAll(airlines);
        flightAirportCombineModel.setAirlines(localListAirlines);
        int size = airportCombineModelList.getData().size();
        int halfProgressAmount = divideTo(divideTo(MAX_PROGRESS, size), 2);
        if (!flightAirportCombineModel.isHasLoad()) {
            flightAirportCombineModel.setHasLoad(true);
            progress += halfProgressAmount;
        }

        if (flightAirportCombineModel.isNeedRefresh()) {
            if (flightMetaDataDB.isNeedRefresh()) {
                int noRetry = flightAirportCombineModel.getNoOfRetry();
                noRetry++;
                flightAirportCombineModel.setNoOfRetry(noRetry);
                progress += divideTo(halfProgressAmount, flightMetaDataDB.getMaxRetry());
                // already reach max retry limit, end retrying.
                if (noRetry >= flightMetaDataDB.getMaxRetry()) {
                    flightAirportCombineModel.setNeedRefresh(false);
                } else { //no retry still below the max retry, do retry
                    //retry load data
                    String date = flightSearchPassDataViewModel.getDate(isReturning());
                    FlightPassengerViewModel flightPassengerViewModel = flightSearchPassDataViewModel.getFlightPassengerViewModel();
                    int adult = flightPassengerViewModel.getAdult();
                    int child = flightPassengerViewModel.getChildren();
                    int infant = flightPassengerViewModel.getInfant();
                    int classID = flightSearchPassDataViewModel.getFlightClass().getId();
                    FlightSearchApiRequestModel flightSearchApiRequestModel = new FlightSearchApiRequestModel(
                            flightAirportCombineModel.getDepAirport(), flightAirportCombineModel.getArrAirport(),
                            date, adult, child, infant, classID, flightAirportCombineModel.getAirlines(),
                            FlightRequestUtil.getLocalIpAddress());
                    flightSearchPresenter.searchAndSortFlightWithDelay(flightSearchApiRequestModel, isReturning(), flightMetaDataDB.getRefreshTime());
                }

            } else {
                flightAirportCombineModel.setNeedRefresh(false);
                progress += (flightMetaDataDB.getMaxRetry() - flightAirportCombineModel.getNoOfRetry()) *
                        divideTo(halfProgressAmount, flightMetaDataDB.getMaxRetry());
            }
        }
        setUpProgress();

        // if the data is empty, but there is data need to fetch, then keep the loading state
        if (getAdapter().getItemCount() == 0 && isDataEmpty &&
                airportCombineModelList.isRetrievingData()) {
            return;
        }

        // will update the data
        // if there is already data loaded, reload the data from cache
        // because the data might have filter/sort in it, so cannot be added directly

        // we retrieve from cache, because there is possibility the filter/sort will be different
        reloadDataFromCache();
    }

    @Override
    public void onSuccessDeleteFlightCache() {
        resetDateAndReload();
    }

    @Override
    public void onErrorDeleteFlightCache(Throwable throwable) {
        resetDateAndReload();
    }

    private void resetDateAndReload() {
        // preserve the filter and sort option

        // cancel all cloud progress, setupCombination airport, reset progress, hide filter, clear current data
        flightSearchPresenter.detachView();

        onFlightSearchFragmentListener.changeDate(flightSearchPassDataViewModel);

        setUpCombinationAirport();
        progressBar.setVisibility(View.VISIBLE);
        progress = 0;
        filterAndSortBottomAction.setVisibility(View.GONE);

        getAdapter().clearAllElements();
        getAdapter().notifyDataSetChanged();

        flightSearchPresenter.attachView(this);
        loadInitialData();
    }

    @Override
    public void showGetListError(Throwable t) {
        this.addToolbarElevation();
        progressBar.setVisibility(View.GONE);
        removeBottomPaddingForSortAndFilterActionButton();
        super.showGetListError(t);
    }

    private int divideTo(int number, int pieces) {
        return (int) Math.ceil(((double) number / pieces));
    }

    @Override
    public void setSelectedSortItem(int itemId) {
        selectedSortOption = itemId;
        setUIMarkSort();
    }

    @Override
    protected String getMessageFromThrowable(Context context, Throwable t) {
        return FlightErrorUtil.getMessageFromException(context, t);
    }

    @Override
    public void onDetailClicked(FlightSearchViewModel flightSearchViewModel, int adapterPosition) {
        flightSearchPresenter.onSeeDetailItemClicked(flightSearchViewModel, adapterPosition);
        FlightDetailViewModel flightDetailViewModel = new FlightDetailViewModel();
        flightDetailViewModel.build(flightSearchViewModel);
        flightDetailViewModel.build(flightSearchPassDataViewModel);
        this.startActivityForResult(FlightDetailActivity.createIntent(getActivity(), flightDetailViewModel, true),
                REQUEST_CODE_SEE_DETAIL_FLIGHT);
    }

    @Override
    public void onRetryClicked() {
        getAdapter().clearAllElements();
        flightSearchPresenter.initialize();
    }

    public void onResetFilterClicked() {
        flightFilterModel = new FlightFilterModel();
        getAdapter().clearAllNonDataElement();
        showLoading();
        setUIMarkFilter();
        reloadDataFromCache();
    }

    public void onChangeDateClickedOld() {
        if (!getActivity().isFinishing()) {
            final String dateInput = flightSearchPassDataViewModel.getDate(isReturning());
            Date date = FlightDateUtil.stringToDate(dateInput);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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

    public void onChangeDateClicked() {
        if (!getActivity().isFinishing()) {
            final String dateInput = flightSearchPassDataViewModel.getDate(isReturning());
            Date date = FlightDateUtil.stringToDate(dateInput);

            Date maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 2);
            maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, -1);
            maxDate = FlightDateUtil.trimDate(maxDate);
            Date minDate;

            if (isReturning()) {
                String dateDepStr = flightSearchPassDataViewModel.getDate(false);
                Date dateDep = FlightDateUtil.stringToDate(dateDepStr);
                dateDep = FlightDateUtil.trimDate(dateDep);
                minDate = dateDep;
            } else {
                minDate = FlightDateUtil.trimDate(FlightDateUtil.getCurrentDate());

                boolean isOneWay = flightSearchPassDataViewModel.isOneWay();
                if (!isOneWay) {
                    String dateReturnStr = flightSearchPassDataViewModel.getDate(true);
                    Date dateReturn = FlightDateUtil.stringToDate(dateReturnStr);
                    dateReturn = FlightDateUtil.trimDate(dateReturn);
                    maxDate = dateReturn;
                }
            }

            int calendarTitleType = isReturning() ? TravelCalendarActivity.RETURN_TYPE : TravelCalendarActivity.DEPARTURE_TYPE;

            startActivityForResult(TravelCalendarActivity
                            .newInstance(getActivity(), date, minDate, maxDate,
                                    calendarTitleType),
                    REQUEST_CODE_CHANGE_DATE);
        }
    }

    private void setMinMaxDatePicker(DatePicker datePicker) {
        Date maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 2);
        maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, -1);
        maxDate = FlightDateUtil.trimDate(maxDate);

        if (isReturning()) {
            String dateDepStr = flightSearchPassDataViewModel.getDate(false);
            Date dateDep = FlightDateUtil.stringToDate(dateDepStr);
            dateDep = FlightDateUtil.trimDate(dateDep);
            datePicker.setMinDate(dateDep.getTime());
            datePicker.setMaxDate(maxDate.getTime());
        } else {
            Date dateNow = FlightDateUtil.trimDate(FlightDateUtil.getCurrentDate());
            datePicker.setMinDate(dateNow.getTime());

            boolean isOneWay = flightSearchPassDataViewModel.isOneWay();
            if (!isOneWay) {
                String dateReturnStr = flightSearchPassDataViewModel.getDate(true);
                Date dateReturn = FlightDateUtil.stringToDate(dateReturnStr);
                dateReturn = FlightDateUtil.trimDate(dateReturn);
                datePicker.setMaxDate(dateReturn.getTime());
            } else {
                datePicker.setMaxDate(maxDate.getTime());
            }
        }
    }

    @Override
    public void onErrorGetFlightStatistic(Throwable throwable) {
        String message = FlightErrorUtil.getMessageFromException(getActivity(), throwable);
        if (!TextUtils.isEmpty(message)) {
            NetworkErrorHelper.showCloseSnackbar(getActivity(), message);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_NEED_REFRESH_AIRLINE, needRefreshAirline);
        outState.putParcelable(SAVED_FILTER_MODEL, flightFilterModel);
        outState.putInt(SAVED_SORT_OPTION, selectedSortOption);
        outState.putParcelable(SAVED_AIRPORT_COMBINE, airportCombineModelList);
        outState.putInt(SAVED_PROGRESS, progress);
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        onFlightSearchFragmentListener = (OnFlightSearchFragmentListener) context;
    }

    @Override
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
        EmptyResultViewModel emptyResultViewModel;
        emptyResultViewModel = new EmptyResultViewModel();
        emptyResultViewModel.setIconRes(R.drawable.ic_flight_empty_state);
        emptyResultViewModel.setTitle(message);
        emptyResultViewModel.setButtonTitleRes(R.string.flight_change_search_content_button);
        emptyResultViewModel.setCallback(new EmptyResultViewHolder.Callback() {
            @Override
            public void onEmptyContentItemTextClicked() {

            }

            @Override
            public void onEmptyButtonClicked() {
                getActivity().finish();
            }
        });

        return emptyResultViewModel;
    }

    @SuppressWarnings("Range")
    private void showMessageErrorInSnackBar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    protected boolean isLoadMoreEnabledByDefault() {
        return false;
    }


    public interface OnFlightSearchFragmentListener {
        void selectFlight(String selectedFlightID);

        void changeDate(FlightSearchPassDataViewModel flightSearchPassDataViewModel);
    }
}
