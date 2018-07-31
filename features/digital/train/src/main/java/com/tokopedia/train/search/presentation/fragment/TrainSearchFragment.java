package com.tokopedia.train.search.presentation.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.util.TrainAnalytics;
import com.tokopedia.train.common.util.TrainFlowUtil;
import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.train.passenger.presentation.activity.TrainBookingPassengerActivity;
import com.tokopedia.train.scheduledetail.presentation.activity.TrainScheduleDetailActivity;
import com.tokopedia.train.search.constant.TrainSortOption;
import com.tokopedia.train.search.di.DaggerTrainSearchComponent;
import com.tokopedia.train.search.di.TrainSearchComponent;
import com.tokopedia.train.search.domain.FilterParam;
import com.tokopedia.train.search.domain.GetScheduleUseCase;
import com.tokopedia.train.search.presentation.activity.TrainFilterSearchActivity;
import com.tokopedia.train.search.presentation.activity.TrainSearchReturnActivity;
import com.tokopedia.train.search.presentation.adapter.TrainSearchAdapterTypeFactory;
import com.tokopedia.train.search.presentation.contract.TrainSearchContract;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.train.search.presentation.presenter.TrainSearchPresenter;
import com.tokopedia.usecase.RequestParams;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TrainSearchFragment extends BaseListFragment<TrainScheduleViewModel,
        TrainSearchAdapterTypeFactory> implements TrainSearchContract.View,
        TrainSearchAdapterTypeFactory.OnTrainSearchListener {

    private static final int RETURN_SEARCH_FRAGMENT_REQUEST_CODE = 1010;
    private static final int FILTER_SEARCH_REQUEST_CODE = 1011;
    protected static final int NEXT_STEP_REQUEST_CODE = 1010;

    private static final String TAG = TrainSearchFragment.class.getSimpleName();

    private static final String SELECTED_SORT = "selected_sort";
    private static final int EMPTY_MARGIN = 0;
    private static final float DEFAULT_DIMENS_MULTIPLIER = 0.5f;
    private static final int PADDING_SEARCH_LIST = 60;
    private static final int MIN_VALUE = Integer.MIN_VALUE;
    private static final int MAX_VALUE = Integer.MAX_VALUE;

    private static final int REQUEST_CODE_OPEN_TRAIN_SCHEDULE_DETAIL = 1012;

    protected TrainSearchPassDataViewModel trainSearchPassDataViewModel;
    protected String dateDeparture;
    protected int adultPassenger;
    protected int infantPassenger;
    protected String originCode;
    protected String originCity;
    protected String destinationCode;
    protected String destinationCity;
    protected TrainSearchComponent trainSearchComponent;
    private int selectedSortOption;
    private FilterSearchData filterSearchData;
    private TrainScheduleBookingPassData trainScheduleBookingPassData;
    private ActionListener listener;
    private long minPrice;
    private long maxPrice;
    private List<String> trains;
    private List<String> trainClass;
    private List<String> departureTrains;

    //this is for save departure trip - arrival timestamp schedule
    protected String arrivalScheduleSelected;

    private BottomActionView filterAndSortBottomAction;

    @Inject
    TrainAnalytics trainAnalytics;

    @Inject
    TrainFlowUtil trainFlowUtil;

    @Inject
    TrainSearchPresenter presenter;

    public TrainSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter.attachView(this);
        View view = inflater.inflate(getLayoutView(), container, false);
        return view;
    }

    protected int getLayoutView() {
        return R.layout.fragment_train_search;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromFragment();
        arrivalScheduleSelected = getArguments().getString(TrainSearchReturnActivity.EXTRA_ARRIVAL_TIME_SCHEDULE_SELECTED);

        if (savedInstanceState == null) {
            filterSearchData = new FilterSearchData();
            selectedSortOption = TrainSortOption.CHEAPEST;
            resetFilterParam();
        } else {
            selectedSortOption = savedInstanceState.getInt(SELECTED_SORT);
            if (filterSearchData == null) {
                resetFilterParam();
            }
        }

        trainScheduleBookingPassData = new TrainScheduleBookingPassData();
        trainScheduleBookingPassData.setOriginCity(originCity);
        trainScheduleBookingPassData.setDestinationCity(destinationCity);
        trainScheduleBookingPassData.setAdultPassenger(adultPassenger);
        trainScheduleBookingPassData.setInfantPassenger(infantPassenger);

        presenter.getTrainSchedules();
        setUpButtonActionView(view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_SORT, selectedSortOption);
    }

    @Override
    protected boolean isLoadMoreEnabledByDefault() {
        return false;
    }

    @Override
    protected boolean callInitialLoadAutomatically() {
        return false;
    }

    @NonNull
    @Override
    protected BaseListAdapter<TrainScheduleViewModel, TrainSearchAdapterTypeFactory> createAdapterInstance() {
        BaseListAdapter<TrainScheduleViewModel, TrainSearchAdapterTypeFactory> adapter = super.createAdapterInstance();
        ErrorNetworkModel errorNetworkModel = adapter.getErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(R.drawable.ic_train_error_network);
        errorNetworkModel.setErrorMessage(getString(R.string.train_search_no_connection_title));
        errorNetworkModel.setSubErrorMessage(getString(R.string.train_search_no_connection));
        errorNetworkModel.setOnRetryListener(this);
        adapter.setErrorNetworkModel(errorNetworkModel);
        return adapter;
    }

    protected abstract int getScheduleVariant();

    protected abstract void getDataFromFragment();

    private void setCenterTextView(RelativeLayout.LayoutParams layoutParams, TextView textView) {
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(layoutParams);
    }

    private void removeCenterTextView(RelativeLayout.LayoutParams layoutParams, TextView textView) {
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
        textView.setLayoutParams(layoutParams);
    }

    private void setUpButtonActionView(View view) {
        filterAndSortBottomAction = view.findViewById(R.id.bottom_action_filter_sort);
        filterAndSortBottomAction.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trainAnalytics.eventClickFilterOnBottomBar();
                startActivityForResult(TrainFilterSearchActivity.getCallingIntent(getActivity(),
                        arrivalScheduleSelected, getScheduleVariant(), filterSearchData), FILTER_SEARCH_REQUEST_CODE);
            }
        });
        filterAndSortBottomAction.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trainAnalytics.eventClickSortOnBottomBar();
                showSortBottomSheets();
            }
        });

        markUiSortAndFilterOption();
    }

    private void markUiSortAndFilterOption() {
        if (selectedSortOption == TrainSortOption.NO_PREFERENCE) {
            filterAndSortBottomAction.setMarkRight(false);
        } else {
            filterAndSortBottomAction.setMarkRight(true);
        }

        if (filterSearchData != null && filterSearchData.isHasFilter()) {
            filterAndSortBottomAction.setMarkLeft(true);
        } else {
            filterAndSortBottomAction.setMarkLeft(false);
        }
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        trainAnalytics.eventViewRouteNotAvailablePage(
                trainSearchPassDataViewModel.getOriginStationName(),
                trainSearchPassDataViewModel.getDestinationStationName(),
                trainSearchPassDataViewModel.getDepartureDate());

        clearAdapterData();
        EmptyResultViewModel emptyResultViewModel;
        if (filterSearchData != null && filterSearchData.isHasFilter()) {
            emptyResultViewModel = new EmptyResultViewModel();
            emptyResultViewModel.setIconRes(R.drawable.ic_train_no_result);
            emptyResultViewModel.setContentRes(R.string.train_search_no_result_filter);
            emptyResultViewModel.setButtonTitleRes(R.string.train_search_reset_filter_button);
            emptyResultViewModel.setCallback(new EmptyResultViewHolder.Callback() {
                @Override
                public void onEmptyContentItemTextClicked() {

                }

                @Override
                public void onEmptyButtonClicked() {
                    filterSearchData = filterSearchData.resetSelectedValue();
                    getAdapter().clearAllNonDataElement();
                    showLoading();
                    markUiSortAndFilterOption();
                    minPrice = filterSearchData.getMinPrice();
                    maxPrice = filterSearchData.getMaxPrice();
                    trains = filterSearchData.getTrainClass();
                    trainClass = filterSearchData.getTrains();
                    departureTrains = filterSearchData.getDepartureTimeList();
                    presenter.getFilteredAndSortedSchedules();
                }
            });
            return emptyResultViewModel;
        } else {
            emptyResultViewModel = new EmptyResultViewModel();
            emptyResultViewModel.setIconRes(R.drawable.ic_train_no_result);
            emptyResultViewModel.setContentRes(R.string.train_search_no_result_default);
            emptyResultViewModel.setButtonTitleRes(R.string.train_search_reset_button);
            emptyResultViewModel.setCallback(new EmptyResultViewHolder.Callback() {
                @Override
                public void onEmptyContentItemTextClicked() {

                }

                @Override
                public void onEmptyButtonClicked() {
                    listener.navigateToHomepage();
                }
            });
            return emptyResultViewModel;
        }
    }

    @Override
    public RequestParams getRequestParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetScheduleUseCase.DATE_SCHEDULE, dateDeparture);
        if (originCode != null) {
            requestParams.putString(GetScheduleUseCase.ORIGIN_CODE, originCode);
        } else {
            requestParams.putString(GetScheduleUseCase.ORIGIN_CODE, "");
        }
        requestParams.putString(GetScheduleUseCase.ORIGIN_CITY, originCity);
        if (destinationCode != null) {
            requestParams.putString(GetScheduleUseCase.DEST_CODE, destinationCode);
        } else {
            requestParams.putString(GetScheduleUseCase.DEST_CODE, "");
        }
        requestParams.putString(GetScheduleUseCase.DEST_CITY, destinationCity);
        return requestParams;
    }

    @Override
    public void showEmptyResult() {
        removePaddingSortAndFilterSearch();
        hideFilterAndSortButtonAction();
        getAdapter().addElement(getEmptyDataViewModel());
    }

    @Override
    public void clearAdapterData() {
        getAdapter().clearAllElements();
    }

    @Override
    public void showGetListError(Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            clearAdapterData();
        }
        hideLoading();
        removePaddingSortAndFilterSearch();
        hideFilterAndSortButtonAction();
        super.showGetListError(throwable);
    }

    @Override
    public void showDataScheduleFromCache(List<TrainScheduleViewModel> trainScheduleViewModels) {
        getAdapter().hideLoading();
        clearAdapterData();
        addPaddingSortAndFilterSearch();
        showFilterAndSortButtonAction();
        if (trainScheduleViewModels.size() > 0) {
            getAdapter().addElement(trainScheduleViewModels);
        } else {
            getAdapter().addElement(getEmptyDataViewModel());
        }
    }

    @Override
    public void markSortOption() {
        markUiSortAndFilterOption();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
    }

    @Override
    protected void initInjector() {
        if (trainSearchComponent == null) {
            trainSearchComponent = DaggerTrainSearchComponent.builder()
                    .trainComponent(TrainComponentUtils.getTrainComponent(getActivity().getApplication())).build();
        }
        trainSearchComponent
                .inject(this);
    }

    @Override
    protected TrainSearchAdapterTypeFactory getAdapterTypeFactory() {
        return new TrainSearchAdapterTypeFactory(this, trainAnalytics);
    }

    @Override
    public void onRetryClicked() {
        clearAdapterData();
        presenter.getTrainSchedules();
    }

    @Override
    public void onDetailClicked(TrainScheduleViewModel trainScheduleViewModel, int adapterPosition) {
        String specialTagging = "";
        if (trainScheduleViewModel.isCheapestFlag()) {
            specialTagging = "Termurah";
        } else if (trainScheduleViewModel.isFastestFlag()) {
            specialTagging = "Tercepat";
        }

        trainAnalytics.eventProductClick(
                trainScheduleViewModel.getIdSchedule(),
                trainScheduleViewModel.getOrigin(),
                trainScheduleViewModel.getDestination(),
                trainScheduleViewModel.getClassTrain(),
                trainScheduleViewModel.getTrainName(),
                specialTagging
        );

        Intent intent = TrainScheduleDetailActivity.createIntent(getActivity(),
                trainScheduleViewModel.getIdSchedule(),
                trainSearchPassDataViewModel.getAdult(),
                trainSearchPassDataViewModel.getInfant(),
                trainScheduleViewModel.getAvailableSeat() > 0);
        startActivityForResult(intent, REQUEST_CODE_OPEN_TRAIN_SCHEDULE_DETAIL);
    }

    @Override
    public void onItemClicked(TrainScheduleViewModel trainScheduleViewModel) {
        if (trainScheduleViewModel.getAvailableSeat() > 0) {
            selectSchedule(trainScheduleViewModel);
        }
    }

    @Override
    public void selectSchedule(TrainScheduleViewModel trainScheduleViewModel) {
        if (!trainSearchPassDataViewModel.isOneWay()) {
            trainScheduleBookingPassData.setDepartureScheduleId(trainScheduleViewModel.getIdSchedule());
            startActivityForResult(TrainSearchReturnActivity.getCallingIntent(getActivity(),
                    trainSearchPassDataViewModel, trainScheduleBookingPassData,
                    trainScheduleViewModel.getIdSchedule(), trainScheduleViewModel.getArrivalTimestamp()),
                    RETURN_SEARCH_FRAGMENT_REQUEST_CODE);
        } else {
            trainScheduleBookingPassData.setDepartureScheduleId(trainScheduleViewModel.getIdSchedule());
            startActivityForResult(TrainBookingPassengerActivity.callingIntent(getActivity(), trainScheduleBookingPassData), NEXT_STEP_REQUEST_CODE);
        }
    }

    @Override
    public int getSortOptionSelected() {
        return selectedSortOption;
    }

    @Override
    public int getScheduleVariantSelected() {
        return getScheduleVariant();
    }

    @Override
    public FilterParam getFilterParam() {
        FilterParam filterParam = new FilterParam.Builder()
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .trains(trains)
                .trainClass(trainClass)
                .departureTimeList(departureTrains)
                .scheduleVariant(getScheduleVariant())
                .arrivalTimestampSelected(arrivalScheduleSelected)
                .build();
        return filterParam;
    }

    @Override
    public void resetFilterParam() {
        minPrice = MIN_VALUE;
        maxPrice = MAX_VALUE;
        trains = new ArrayList<>();
        trainClass = new ArrayList<>();
        departureTrains = new ArrayList<>();
    }

    @Override
    public void resetFilterAndSortParam() {
        filterSearchData = new FilterSearchData();
        selectedSortOption = TrainSortOption.CHEAPEST;
    }

    @Override
    public void showLoadingPage() {
        showLoading();
    }

    private void removePaddingSortAndFilterSearch() {
        getRecyclerView(getView()).setPadding(
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                EMPTY_MARGIN,
                EMPTY_MARGIN
        );
    }

    @Override
    public void addPaddingSortAndFilterSearch() {
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
    public void showFilterAndSortButtonAction() {
        filterAndSortBottomAction.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFilterAndSortButtonAction() {
        filterAndSortBottomAction.setVisibility(View.GONE);
    }

    @Override
    public void loadData(int page) {
    }

    public void showSortBottomSheets() {
        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(getString(R.string.train_search_sort));

        ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TrainSortOption.EARLIEST_DEPARTURE, "Waktu Keberangkatan Terpagi", null, isSortSelected(TrainSortOption.EARLIEST_DEPARTURE));
        ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TrainSortOption.LATEST_DEPARTURE, "Waktu Keberangkatan Termalam", null, isSortSelected(TrainSortOption.LATEST_DEPARTURE));
        ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TrainSortOption.SHORTEST_DURATION, "Durasi Terpendek", null, isSortSelected(TrainSortOption.SHORTEST_DURATION));
        ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TrainSortOption.LONGEST_DURATION, "Durasi Terlama", null, isSortSelected(TrainSortOption.LONGEST_DURATION));
        ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TrainSortOption.EARLIEST_ARRIVAL, "Waktu Tiba Terpagi", null, isSortSelected(TrainSortOption.EARLIEST_ARRIVAL));
        ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TrainSortOption.LATEST_ARRIVAL, "Waktu Tiba Termalam", null, isSortSelected(TrainSortOption.LATEST_ARRIVAL));
        ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TrainSortOption.CHEAPEST, "Harga Termurah", null, isSortSelected(TrainSortOption.CHEAPEST));
        ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(TrainSortOption.MOST_EXPENSIVE, "Harga Termahal", null, isSortSelected(TrainSortOption.MOST_EXPENSIVE));

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @SuppressWarnings("WrongConstant")
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        getAdapter().showLoading();
                        selectedSortOption = item.getItemId();
                        presenter.getFilteredAndSortedSchedules();
                    }
                })
                .createDialog();
        bottomSheetDialog.show();
    }

    private boolean isSortSelected(int sortOption) {
        return selectedSortOption == sortOption;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case FILTER_SEARCH_REQUEST_CODE:
                    filterSearchData = data.getExtras().getParcelable(TrainFilterSearchActivity.MODEL_SEARCH_FILTER);
                    minPrice = filterSearchData.getSelectedMinPrice() == 0 ? filterSearchData.getMinPrice() : filterSearchData.getSelectedMinPrice();
                    maxPrice = filterSearchData.getSelectedMaxPrice() == 0 ? filterSearchData.getMaxPrice() : filterSearchData.getSelectedMaxPrice();
                    trains = filterSearchData.getSelectedTrains() != null && !filterSearchData.getSelectedTrains().isEmpty() ?
                            filterSearchData.getSelectedTrains() : filterSearchData.getTrains();
                    trainClass = filterSearchData.getSelectedTrainClass() != null && !filterSearchData.getSelectedTrainClass().isEmpty() ?
                            filterSearchData.getSelectedTrainClass() : filterSearchData.getTrainClass();
                    departureTrains = filterSearchData.getSelectedDepartureTimeList() != null && !filterSearchData.getSelectedDepartureTimeList().isEmpty() ?
                            filterSearchData.getSelectedDepartureTimeList() : filterSearchData.getDepartureTimeList();

                    presenter.getFilteredAndSortedSchedules();
                    break;
                case REQUEST_CODE_OPEN_TRAIN_SCHEDULE_DETAIL:
                    if (data != null && data.hasExtra(TrainScheduleDetailActivity.EXTRA_TRAIN_SELECTED)) {
                        TrainScheduleViewModel trainScheduleViewModel = data.getParcelableExtra(TrainScheduleDetailActivity.EXTRA_TRAIN_SELECTED);
                        if (trainScheduleViewModel != null) {
                            selectSchedule(trainScheduleViewModel);
                        }
                    }
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ActionListener) activity;
    }

    public interface ActionListener {

        void navigateToHomepage();

    }

}