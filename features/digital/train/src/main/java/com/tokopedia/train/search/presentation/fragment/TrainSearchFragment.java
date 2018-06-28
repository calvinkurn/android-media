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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.train.search.constant.TrainSortOption;
import com.tokopedia.train.search.di.DaggerTrainSearchComponent;
import com.tokopedia.train.search.di.TrainSearchComponent;
import com.tokopedia.train.search.domain.GetScheduleUseCase;
import com.tokopedia.train.search.presentation.activity.TrainFilterSearchActivity;
import com.tokopedia.train.search.presentation.adapter.TrainSearchAdapterTypeFactory;
import com.tokopedia.train.search.presentation.contract.TrainSearchContract;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.train.search.presentation.presenter.TrainSearchPresenter;
import com.tokopedia.train.seat.presentation.activity.TrainSeatActivity;
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

    private static final String TAG = TrainSearchFragment.class.getSimpleName();
    private static final String SELECTED_SORT = "selected_sort";
    private static final int EMPTY_MARGIN = 0;
    private static final float DEFAULT_DIMENS_MULTIPLIER = 0.5f;
    private static final int PADDING_SEARCH_LIST = 60;

    protected TrainSearchPassDataViewModel trainSearchPassDataViewModel;
    protected String dateDeparture;
    protected int adultPassanger;
    protected int infantPassanger;
    protected String originCode;
    protected String originCity;
    protected String destinationCode;
    protected String destinationCity;
    private TextView originCodeTv;
    private TextView originCityTv;
    private TextView destinationCodeTv;
    private TextView destinationCityTv;
    private LinearLayout tripInfoLinearLayout;
    protected TrainSearchComponent trainSearchComponent;
    private int selectedSortOption;
    private FilterSearchData filterSearchData;

    private BottomActionView filterAndSortBottomAction;

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
        originCodeTv = view.findViewById(R.id.origin_code);
        originCityTv = view.findViewById(R.id.origin_city);
        destinationCodeTv = view.findViewById(R.id.destination_code);
        destinationCityTv = view.findViewById(R.id.destination_city);
        tripInfoLinearLayout = view.findViewById(R.id.layout_trip_info);
        return view;
    }

    protected int getLayoutView() {
        return R.layout.fragment_train_search;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromFragment();

        if (savedInstanceState == null) {
            filterSearchData = new FilterSearchData();
            selectedSortOption = TrainSortOption.CHEAPEST;
        } else {
            selectedSortOption = savedInstanceState.getInt(SELECTED_SORT);
        }

        showLoading();
        presenter.getTrainSchedules(getScheduleVariant());

        setUpButtonActionView(view);

        originCodeTv.setText(originCode);
        originCityTv.setText(originCity);
        destinationCodeTv.setText(destinationCode);
        destinationCityTv.setText(destinationCity);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_SORT, selectedSortOption);
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

    private void setUpButtonActionView(View view) {
        filterAndSortBottomAction = view.findViewById(R.id.bottom_action_filter_sort);
        filterAndSortBottomAction.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(TrainFilterSearchActivity.getCallingIntent(getActivity(),
                        getRequestParam().getParameters(), getScheduleVariant(), filterSearchData), 133);
            }
        });
        filterAndSortBottomAction.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        if (filterSearchData.isHasFilter()) {
            filterAndSortBottomAction.setMarkLeft(true);
        } else {
            filterAndSortBottomAction.setMarkLeft(false);
        }
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        clearAdapterData();
        EmptyResultViewModel emptyResultViewModel;
        if (filterSearchData.isHasFilter()) {
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
                    presenter.getFilteredAndSortedSchedules(filterSearchData.getMinPrice(),
                            filterSearchData.getMaxPrice(), filterSearchData.getTrainClass(),
                            filterSearchData.getTrains(), filterSearchData.getDepartureTimeList(),
                            selectedSortOption);
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
                    //TODO : ask sonny about empty button ganti pencarian in empty page
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

        //TODO : not yet implemented in query gql staging
        //requestParams.putInt(GetScheduleUseCase.TOTAL_ADULT, adultPassanger);
        //requestParams.putInt(GetScheduleUseCase.TOTAL_INFANT, infantPassanger);
        return requestParams;
    }

    @Override
    public void hideLayoutTripInfo() {
        tripInfoLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLayoutTripInfo() {
        tripInfoLinearLayout.setVisibility(View.VISIBLE);
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
    public void setSortOptionId(int sortOptionId) {
        selectedSortOption = sortOptionId;
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
        return new TrainSearchAdapterTypeFactory(this);
    }

    @Override
    public void onRetryClicked() {
        clearAdapterData();
        presenter.getTrainSchedules(getScheduleVariant());
    }

    @Override
    public void onDetailClicked(TrainScheduleViewModel trainScheduleViewModel, int adapterPosition) {
        //TODO : detail clicked go to detail trip
        Toast.makeText(getActivity(), "detail " + trainScheduleViewModel.getTrainName(), Toast.LENGTH_SHORT).show();

        startActivity(TrainSeatActivity.getCallingIntent(getActivity()));
    }

    private void removePaddingSortAndFilterSearch() {
        RecyclerView recyclerView = getRecyclerView(getView());
        recyclerView.setPadding(
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
                        List<String> trains = new ArrayList<>();
                        getAdapter().showLoading();
                        presenter.getFilteredAndSortedSchedules(0, 200000,
                                new ArrayList<String>(), trains, new ArrayList<>(), item.getItemId());
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
        if (resultCode == Activity.RESULT_OK && requestCode == 133) {
            filterSearchData = data.getExtras().getParcelable(TrainFilterSearchActivity.MODEL_SEARCH_FILTER);
            long minPrice = filterSearchData.getSelectedMinPrice() == 0 ? filterSearchData.getMinPrice() : filterSearchData.getSelectedMinPrice();
            long maxPrice = filterSearchData.getSelectedMaxPrice() == 0 ? filterSearchData.getMaxPrice() : filterSearchData.getSelectedMaxPrice();
            List<String> trains = filterSearchData.getSelectedTrains() != null && !filterSearchData.getSelectedTrains().isEmpty() ?
                    filterSearchData.getSelectedTrains() : filterSearchData.getTrains();
            List<String> trainsClass = filterSearchData.getSelectedTrainClass() != null && !filterSearchData.getSelectedTrainClass().isEmpty() ?
                    filterSearchData.getSelectedTrainClass() : filterSearchData.getTrainClass();
            List<String> departureHours = filterSearchData.getSelectedDepartureTimeList() != null && !filterSearchData.getSelectedDepartureTimeList().isEmpty() ?
                    filterSearchData.getSelectedDepartureTimeList() : filterSearchData.getDepartureTimeList();

            presenter.getFilteredAndSortedSchedules(minPrice, maxPrice, trainsClass, trains,
                    departureHours, selectedSortOption);
        }
    }
}