package com.tokopedia.search.result.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.common.data.Filter;
import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.discovery.common.data.Sort;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.base.BottomSheetListener;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.newdynamicfilter.controller.FilterController;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterHelper;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.helper.SortHelper;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.SearchSectionContract;
import com.tokopedia.search.result.presentation.view.adapter.SearchSectionGeneralAdapter;
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener;
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.discovery.common.constants.SearchConstant.GCM_ID;
import static com.tokopedia.discovery.common.constants.SearchConstant.GCM_STORAGE;
import static com.tokopedia.discovery.common.constants.SearchConstant.LANDSCAPE_COLUMN_MAIN;
import static com.tokopedia.discovery.common.constants.SearchConstant.PORTRAIT_COLUMN_MAIN;

public abstract class SearchSectionFragment
        extends BaseDaggerFragment
        implements
        SearchSectionContract.View {

    public static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 4;
    public static final int REQUEST_CODE_LOGIN = 561;

    protected static final int START_ROW_FIRST_TIME_LOAD = 0;

    private static final String EXTRA_SPAN_COUNT = "EXTRA_SPAN_COUNT";
    private static final String EXTRA_FILTER = "EXTRA_FILTER";
    private static final String EXTRA_SORT = "EXTRA_SORT";
    private static final String EXTRA_SELECTED_FILTER = "EXTRA_SELECTED_FILTER";
    private static final String EXTRA_SELECTED_SORT = "EXTRA_SELECTED_SORT";
    private static final String EXTRA_SHOW_BOTTOM_BAR = "EXTRA_SHOW_BOTTOM_BAR";
    private static final String EXTRA_IS_GETTING_DYNNAMIC_FILTER = "EXTRA_IS_GETTING_DYNNAMIC_FILTER";
    protected static final String EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER";
    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_SELECTED_NAME = "EXTRA_SELECTED_NAME";
    public static final String EXTRA_FILTER_LIST = "EXTRA_FILTER_LIST";
    public static final String EXTRA_FILTER_PARAMETER = "EXTRA_FILTER_PARAMETER";
    public static final String EXTRA_SELECTED_FILTERS = "EXTRA_SELECTED_FILTERS";

    private SearchNavigationListener searchNavigationListener;
    private BottomSheetListener bottomSheetListener;
    protected RedirectionListener redirectionListener;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private SwipeRefreshLayout refreshLayout;
    private boolean showBottomBar;
    public int spanCount;

    private ArrayList<Sort> sort;
    private ArrayList<Filter> filters;
    private HashMap<String, String> selectedSort;
    protected boolean isUsingBottomSheetFilter;
    protected boolean isListEmpty = false;

    protected SearchParameter searchParameter;
    protected FilterController filterController = new FilterController();

    @Inject
    SearchTracking searchTracking;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSpan();
        initLayoutManager();
        initSwipeToRefresh(view);

        if (savedInstanceState == null) {
            refreshLayout.post(this::onFirstTimeLaunch);
        } else {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    private void initSwipeToRefresh(View view) {
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(this::onSwipeToRefresh);
    }

    @Override
    public void showRefreshLayout() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshLayout() {
        refreshLayout.setRefreshing(false);
    }

    private void initSpan() {
        setSpanCount(calcColumnSize(getResources().getConfiguration().orientation));
    }

    private int calcColumnSize(int orientation) {
        int defaultColumnNumber = 1;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                defaultColumnNumber = PORTRAIT_COLUMN_MAIN;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                defaultColumnNumber = LANDSCAPE_COLUMN_MAIN;
                break;
        }
        return defaultColumnNumber;
    }

    private void initLayoutManager() {
        linearLayoutManager = new LinearLayoutManager(getActivity());

        gridLayoutManager = new GridLayoutManager(getActivity(), getSpanCount());
        gridLayoutManager.setSpanSizeLookup(onSpanSizeLookup());

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchNavigationListener) {
            this.searchNavigationListener = (SearchNavigationListener) context;
        }
        if (context instanceof BottomSheetListener) {
            this.bottomSheetListener = (BottomSheetListener) context;
        }
        if (context instanceof RedirectionListener) {
            this.redirectionListener = (RedirectionListener) context;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getView() != null) {
            setupSearchNavigation();
            screenTrack();
        }
    }

    protected void screenTrack() {
        if (getUserVisibleHint()
                && getActivity() != null
                && getActivity().getApplicationContext() != null) {
            searchTracking.screenTrackSearchSectionFragment(getScreenName());
        }
    }

    protected void setupSearchNavigation() {
        searchNavigationListener
                .setupSearchNavigation(new SearchNavigationListener.ClickListener() {
                    @Override
                    public void onFilterClick() {
                        openFilterActivity();
                    }

                    @Override
                    public void onSortClick() {
                        openSortActivity();
                    }

                    @Override
                    public void onChangeGridClick() {
                        switchLayoutType();
                    }
                }, isSortEnabled());
        refreshMenuItemGridIcon();
    }

    protected GridLayoutManager getGridLayoutManager() {
        return gridLayoutManager;
    }

    protected LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    protected StaggeredGridLayoutManager getStaggeredGridLayoutManager() {
        return staggeredGridLayoutManager;
    }

    protected void switchLayoutType() {
        if (!getUserVisibleHint() || getAdapter() == null) {
            return;
        }

        switch (getAdapter().getCurrentLayoutType()) {
            case GRID_1:
                setSpanCount(1);
                gridLayoutManager.setSpanCount(spanCount);
                staggeredGridLayoutManager.setSpanCount(spanCount);
                getAdapter().changeSingleGridView();
                SearchTracking.eventSearchResultChangeGrid(getActivity(), "grid 1", getScreenName());
                break;
            case GRID_2:
                setSpanCount(1);
                gridLayoutManager.setSpanCount(spanCount);
                staggeredGridLayoutManager.setSpanCount(spanCount);
                getAdapter().changeListView();
                SearchTracking.eventSearchResultChangeGrid(getActivity(),"list", getScreenName());
                break;
            case GRID_3:
                setSpanCount(2);
                gridLayoutManager.setSpanCount(spanCount);
                staggeredGridLayoutManager.setSpanCount(spanCount);
                getAdapter().changeDoubleGridView();
                SearchTracking.eventSearchResultChangeGrid(getActivity(),"grid 2", getScreenName());
                break;
        }
        refreshMenuItemGridIcon();
    }

    public void refreshMenuItemGridIcon() {
        if(searchNavigationListener == null || getAdapter() == null) return;

        searchNavigationListener.refreshMenuItemGridIcon(getAdapter().getTitleTypeRecyclerView(), getAdapter().getIconTypeRecyclerView());
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    private int getSpanCount() {
        return spanCount;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == getSortRequestCode()) {
                setSelectedSort(new HashMap<>(getMapFromIntent(data, EXTRA_SELECTED_SORT)));
                String selectedSortName = data.getStringExtra(EXTRA_SELECTED_NAME);
                searchTracking.eventSearchResultSort(getScreenName(), selectedSortName);

                if(searchParameter != null) {
                    searchParameter.getSearchParameterHashMap().putAll(getSelectedSort());
                }

                clearDataFilterSort();
                reloadData();
            } else if (requestCode == getFilterRequestCode()) {
                Map<String, String> filterParameter = getMapFromIntent(data, EXTRA_FILTER_PARAMETER);
                Map<String, String> activeFilterParameter = getMapFromIntent(data, EXTRA_SELECTED_FILTERS);

                SearchTracking.eventSearchResultFilter(getActivity(), getScreenName(), activeFilterParameter);

                applyFilterToSearchParameter(filterParameter);
                setSelectedFilter(new HashMap<>(filterParameter));
                clearDataFilterSort();
                reloadData();
            }
        }
    }

    private Map<String, String> getMapFromIntent(Intent data, String extraName) {
        Serializable serializableExtra = data.getSerializableExtra(extraName);

        if(serializableExtra == null) return new HashMap<>();

        Map<?, ?> filterParameterMapIntent = (Map<?, ?>)data.getSerializableExtra(extraName);
        Map<String, String> filterParameter = new HashMap<>(filterParameterMapIntent.size());

        for(Map.Entry<?, ?> entry: filterParameterMapIntent.entrySet()) {
            filterParameter.put(entry.getKey().toString(), entry.getValue().toString());
        }

        return filterParameter;
    }

    private void setFilterData(List<Filter> filters) {
        this.filters = new ArrayList<>();
        if (filters == null) {
            return;
        }

        this.filters.addAll(filters);
    }

    protected ArrayList<Filter> getFilters() {
        return filters;
    }

    protected void setSortData(List<Sort> sorts) {
        this.sort = new ArrayList<>();
        if (sorts == null) {
            return;
        }

        this.sort.addAll(sorts);
    }

    private ArrayList<Sort> getSort() {
        return sort;
    }

    @Override
    public HashMap<String, String> getSelectedSort() {
        return selectedSort;
    }

    @Override
    public void setSelectedSort(HashMap<String, String> selectedSort) {
        this.selectedSort = selectedSort;
    }

    @Override
    public HashMap<String, String> getSelectedFilter() {
        if(filterController == null) return new HashMap<>();

        return new HashMap<>(filterController.getActiveFilterMap());
    }

    protected boolean isFilterActive() {
        if(filterController == null) return false;

        return filterController.isFilterActive();
    }

    @Override
    public void setSelectedFilter(HashMap<String, String> selectedFilter) {
        if(filterController == null || getFilters() == null) return;

        List<Filter> initializedFilterList = FilterHelper.initializeFilterList(getFilters());
        filterController.initFilterController(selectedFilter, initializedFilterList);
    }

    public void clearDataFilterSort() {
        if (filters != null) {
            this.filters.clear();
        }
        if (sort != null) {
            this.sort.clear();
        }
    }

    protected void openFilterActivity() {
        if (!isFilterDataAvailable() && getActivity() != null) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_filter_data_not_ready));
            return;
        }

        if (bottomSheetListener != null && isUsingBottomSheetFilter) {
            openBottomSheetFilter();
        } else {
            openFilterPage();
        }
    }

    protected void openBottomSheetFilter() {
        if(searchParameter == null || getFilters() == null) return;

        bottomSheetListener.loadFilterItems(getFilters(), searchParameter.getSearchParameterHashMap());
        bottomSheetListener.launchFilterBottomSheet();
    }

    protected void openFilterPage() {
        if (searchParameter == null) return;

        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalDiscovery.FILTER);
        intent.putExtra(EXTRA_FILTER_LIST, getScreenName());
        intent.putExtra(EXTRA_FILTER_PARAMETER, searchParameter.getSearchParameterHashMap());

        startActivityForResult(intent, getFilterRequestCode());

        if (getActivity() != null) {
            getActivity().overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
        }
    }

    protected boolean isFilterDataAvailable() {
        return filters != null && !filters.isEmpty();
    }

    protected void openSortActivity() {
        if(getActivity() == null) return;

        if (isSortDataAvailable()) {
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalDiscovery.SORT);
            intent.putParcelableArrayListExtra(EXTRA_DATA, sort);
            if (getSelectedSort() != null) {
                intent.putExtra(EXTRA_SELECTED_SORT, getSelectedSort());
            }

            startActivityForResult(intent, getSortRequestCode());

            if(getActivity() != null) {
                getActivity().overridePendingTransition(R.anim.pull_up, R.anim.fade_out);
            }
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_sort_data_not_ready));
        }
    }

    private boolean isSortDataAvailable() {
        return sort != null && !sort.isEmpty();
    }

    @Override
    public void renderDynamicFilter(DynamicFilterModel pojo) {
        setFilterData(pojo.getData().getFilter());
        setSortData(pojo.getData().getSort());

        if(filterController == null || searchParameter == null
            || getFilters() == null || getSort() == null) return;

        List<Filter> initializedFilterList = FilterHelper.initializeFilterList(getFilters());
        filterController.initFilterController(searchParameter.getSearchParameterHashMap(), initializedFilterList);
        initSelectedSort();

        if(isListEmpty) {
            refreshAdapterForEmptySearch();
        }
    }

    private void initSelectedSort() {
        if(getSort() == null) return;

        HashMap<String, String> selectedSort = new HashMap<>(
                SortHelper.Companion.getSelectedSortFromSearchParameter(searchParameter.getSearchParameterHashMap(), getSort())
        );
        addDefaultSelectedSort(selectedSort);
        setSelectedSort(selectedSort);
    }

    private void addDefaultSelectedSort(HashMap<String, String> selectedSort) {
        if (selectedSort.isEmpty()) {
            selectedSort.put(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT);
        }
    }

    protected abstract void refreshAdapterForEmptySearch();

    @Override
    public void renderFailRequestDynamicFilter() {
        if(getActivity() == null) return;

        NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_get_dynamic_filter));
    }

    public void performNewProductSearch(String query) {
        redirectionListener.performNewProductSearch(query);
    }

    public void showSearchInputView() {
        redirectionListener.showSearchInputView();
    }

    protected boolean isRefreshing() {
        return refreshLayout.isRefreshing();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_SPAN_COUNT, getSpanCount());
        outState.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter);
        outState.putParcelableArrayList(EXTRA_FILTER, getFilters());
        outState.putParcelableArrayList(EXTRA_SORT, getSort());
        outState.putSerializable(EXTRA_SELECTED_FILTER, getSelectedFilter());
        outState.putSerializable(EXTRA_SELECTED_SORT, getSelectedSort());
        outState.putBoolean(EXTRA_SHOW_BOTTOM_BAR, showBottomBar);
    }

    public abstract void reloadData();

    protected abstract int getFilterRequestCode();

    protected abstract int getSortRequestCode();

    protected abstract SearchSectionGeneralAdapter getAdapter();

    protected abstract SearchSectionContract.Presenter getPresenter();

    protected abstract GridLayoutManager.SpanSizeLookup onSpanSizeLookup();

    protected abstract boolean isSortEnabled();

    protected void onFirstTimeLaunch() {

    }

    protected void disableSwipeRefresh() {
        refreshLayout.setEnabled(false);
        refreshLayout.setRefreshing(false);
    }

    protected void onSwipeToRefresh() {

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        setSpanCount(savedInstanceState.getInt(EXTRA_SPAN_COUNT));
        copySearchParameter(savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER));
        setFilterData(savedInstanceState.getParcelableArrayList(EXTRA_FILTER));
        setSortData(savedInstanceState.getParcelableArrayList(EXTRA_SORT));
        setSelectedFilter((HashMap<String, String>) savedInstanceState.getSerializable(EXTRA_SELECTED_FILTER));
        setSelectedSort((HashMap<String, String>) savedInstanceState.getSerializable(EXTRA_SELECTED_SORT));
        showBottomBar = savedInstanceState.getBoolean(EXTRA_SHOW_BOTTOM_BAR);
    }

    @Override
    public void setTotalSearchResultCount(String formattedResultCount) {
        if (bottomSheetListener != null) {
            bottomSheetListener.setFilterResultCount(formattedResultCount);
        }
    }

    protected RecyclerView.OnScrollListener getRecyclerViewBottomSheetScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && bottomSheetListener != null) {
                    bottomSheetListener.closeFilterBottomSheet();
                }
            }
        };
    }

    public void onBottomSheetHide() {
        SearchTracking.eventSearchResultCloseBottomSheetFilter(getActivity(), getScreenName(), getSelectedFilter());
    }

    protected void removeSelectedFilter(String uniqueId) {
        if(filterController == null) return;

        Option option = OptionHelper.generateOptionFromUniqueId(uniqueId);

        removeFilterFromFilterController(option);
        applyFilterToSearchParameter(filterController.getParameter());
        clearDataFilterSort();
        reloadData();
    }

    public void removeFilterFromFilterController(Option option) {
        if(filterController == null) return;

        String optionKey = option.getKey();

        if (Option.KEY_CATEGORY.equals(optionKey)) {
            filterController.setFilter(option, false, true);
        } else if (Option.KEY_PRICE_MIN.equals(optionKey) ||
                Option.KEY_PRICE_MAX.equals(optionKey)) {
            filterController.setFilter(generatePriceOption(Option.KEY_PRICE_MIN), false, true);
            filterController.setFilter(generatePriceOption(Option.KEY_PRICE_MAX), false, true);
        } else {
            filterController.setFilter(option, false);
        }
    }

    private Option generatePriceOption(String priceOptionKey) {
        Option option = new Option();
        option.setKey(priceOptionKey);
        return option;
    }

    protected void copySearchParameter(@Nullable SearchParameter searchParameterToCopy) {
        if (searchParameterToCopy != null) {
            this.searchParameter = new SearchParameter(searchParameterToCopy);
        }
    }

    public void applyFilterToSearchParameter(Map<String, String> filterParameter) {
        if(searchParameter == null) return;

        this.searchParameter.getSearchParameterHashMap().clear();
        this.searchParameter.getSearchParameterHashMap().putAll(filterParameter);
    }

    protected List<Option> getOptionListFromFilterController() {
        if(filterController == null) return new ArrayList<>();

        return OptionHelper.combinePriceFilterIfExists(filterController.getActiveFilterOptionList(),
                getResources().getString(R.string.empty_state_selected_filter_price_name));
    }

    public String getRegistrationId() {
        if(getActivity() == null || getActivity().getApplicationContext() == null) return "";

        LocalCacheHandler cache = new LocalCacheHandler(getActivity().getApplicationContext(), GCM_STORAGE);
        return cache.getString(GCM_ID, "");
    }

    @Nullable
    public BaseAppComponent getBaseAppComponent() {
        if(getActivity() == null || getActivity().getApplication() == null) return null;

        return ((BaseMainApplication)getActivity().getApplication()).getBaseAppComponent();
    }

    @Override
    public void logDebug(String tag, String message) {
        Log.d(tag, message);
    }
}