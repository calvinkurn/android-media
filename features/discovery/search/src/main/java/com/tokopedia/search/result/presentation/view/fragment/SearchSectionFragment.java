package com.tokopedia.search.result.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.common.data.Sort;
import com.tokopedia.filter.common.manager.FilterSortManager;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData;
import com.tokopedia.filter.newdynamicfilter.controller.FilterController;
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper;
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.filter.newdynamicfilter.helper.SortHelper;
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener;
import com.tokopedia.search.R;
import com.tokopedia.search.analytics.SearchTracking;
import com.tokopedia.search.result.presentation.SearchSectionContract;
import com.tokopedia.search.result.presentation.view.adapter.SearchSectionGeneralAdapter;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener;
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.domain.model.CpmData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.discovery.common.constants.SearchConstant.GCM.GCM_ID;
import static com.tokopedia.discovery.common.constants.SearchConstant.GCM.GCM_STORAGE;
import static com.tokopedia.discovery.common.constants.SearchConstant.LANDSCAPE_COLUMN_MAIN;
import static com.tokopedia.discovery.common.constants.SearchConstant.PORTRAIT_COLUMN_MAIN;
import static com.tokopedia.discovery.common.constants.SearchConstant.ViewType.BIG_GRID;
import static com.tokopedia.discovery.common.constants.SearchConstant.ViewType.LIST;
import static com.tokopedia.discovery.common.constants.SearchConstant.ViewType.SMALL_GRID;

public abstract class SearchSectionFragment
        extends BaseDaggerFragment
        implements
        SearchSectionContract.View,
        BannerAdsListener {

    private static final String SHOP = "shop";
    public static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 4;
    public static final int REQUEST_CODE_LOGIN = 561;

    protected static final int START_ROW_FIRST_TIME_LOAD = 0;

    private static final String EXTRA_SPAN_COUNT = "EXTRA_SPAN_COUNT";
    private static final String EXTRA_SHOW_BOTTOM_BAR = "EXTRA_SHOW_BOTTOM_BAR";
    protected static final String EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER";

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
    private boolean hasLoadData;
    private FilterTrackingData filterTrackingData;

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
        restoreInstanceState(savedInstanceState);
        onViewCreatedBeforeLoadData(view, savedInstanceState);

        startToLoadDataForFirstActiveTab();
    }

    protected abstract void onViewCreatedBeforeLoadData(@NonNull View view, @Nullable Bundle savedInstanceState);

    private void initSwipeToRefresh(View view) {
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(this::onSwipeToRefresh);
    }

    private void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    private void startToLoadDataForFirstActiveTab() {
        if (isFirstActiveTab()) {
            hasLoadData = true;
            onFirstTimeLaunch();
        }
    }

    protected abstract boolean isFirstActiveTab();

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
            startToLoadData();
        }
    }

    protected void screenTrack() {
        if (getUserVisibleHint()
                && getActivity() != null
                && getActivity().getApplicationContext() != null) {
            SearchTracking.screenTrackSearchSectionFragment(getScreenName());
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

    private void startToLoadData() {
        if (canStartToLoadData()) {
            hasLoadData = true;
            onFirstTimeLaunch();
        }
    }

    private boolean canStartToLoadData() {
        return !hasLoadData
                && isAdded()
                && getPresenter() != null;
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
            case LIST:
                switchLayoutTypeTo(BIG_GRID);
                SearchTracking.eventSearchResultChangeGrid(getActivity(), "grid 1", getScreenName());
                break;
            case SMALL_GRID:
                switchLayoutTypeTo(LIST);
                SearchTracking.eventSearchResultChangeGrid(getActivity(),"list", getScreenName());
                break;
            case BIG_GRID:
                switchLayoutTypeTo(SMALL_GRID);
                SearchTracking.eventSearchResultChangeGrid(getActivity(),"grid 2", getScreenName());
                break;
        }
    }

    protected void switchLayoutTypeTo(SearchConstant.ViewType layoutType) {
        if (!getUserVisibleHint() || getAdapter() == null) {
            return;
        }

        switch (layoutType) {
            case LIST:
                recyclerViewLayoutManagerChangeSpanCount(1);
                getAdapter().changeListView();
                break;
            case SMALL_GRID:
                recyclerViewLayoutManagerChangeSpanCount(2);
                getAdapter().changeDoubleGridView();
                break;
            case BIG_GRID:
                recyclerViewLayoutManagerChangeSpanCount(1);
                getAdapter().changeSingleGridView();
                break;
        }

        refreshMenuItemGridIcon();
    }

    private void recyclerViewLayoutManagerChangeSpanCount(int spanCount) {
        setSpanCount(spanCount);
        gridLayoutManager.setSpanCount(spanCount);
        staggeredGridLayoutManager.setSpanCount(spanCount);
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
        FilterSortManager.handleOnActivityResult(requestCode, resultCode, data, new FilterSortManager.Callback() {
            @Override
            public void onFilterResult(Map<String, String> queryParams, Map<String, String> selectedFilters,
                                       List<Option> selectedOptions) {
                handleFilterResult(queryParams, selectedFilters);
            }

            @Override
            public void onSortResult(Map<String, String> selectedSort, String selectedSortName, String autoApplyFilter) {
                handleSortResult(selectedSort, selectedSortName, autoApplyFilter);
            }
        });
    }

    private void handleFilterResult(Map<String, String> queryParams, Map<String, String> selectedFilters) {
        FilterTracking.eventApplyFilter(getFilterTrackingData(),
                getScreenName(), selectedFilters);

        refreshSearchParameter(queryParams);
        refreshFilterController(new HashMap<>(queryParams));
        clearDataFilterSort();
        reloadData();
    }

    private void handleSortResult(Map<String, String> selectedSort, String selectedSortName, String autoApplyFilter) {
        setSelectedSort(new HashMap<>(selectedSort));
        searchTracking.eventSearchResultSort(getScreenName(), selectedSortName);
        if(searchParameter != null) {
            searchParameter.getSearchParameterHashMap().put(SearchApiConst.ORIGIN_FILTER,
                    SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE);

            searchParameter.getSearchParameterHashMap().putAll(UrlParamUtils.getParamMap(autoApplyFilter));

            searchParameter.getSearchParameterHashMap().putAll(getSelectedSort());
        }
        clearDataFilterSort();
        reloadData();
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
    public void refreshFilterController(HashMap<String, String> queryParams) {
        if(filterController == null || getFilters() == null) return;

        HashMap<String, String> params = new HashMap<>(queryParams);
        params.put(SearchApiConst.ORIGIN_FILTER,
                SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE);

        List<Filter> initializedFilterList = FilterHelper.initializeFilterList(getFilters());
        filterController.initFilterController(params, initializedFilterList);
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
            FilterSortManager.openFilterPage(getFilterTrackingData(), this, getScreenName(), searchParameter.getSearchParameterHashMap());
        }
    }

    protected void openBottomSheetFilter() {
        if(searchParameter == null || getFilters() == null) return;

        FilterTracking.eventOpenFilterPage(getFilterTrackingData());

        bottomSheetListener.loadFilterItems(getFilters(), searchParameter.getSearchParameterHashMap());
        bottomSheetListener.launchFilterBottomSheet();
    }

    protected boolean isFilterDataAvailable() {
        return filters != null && !filters.isEmpty();
    }

    protected void openSortActivity() {
        if(getActivity() == null) return;

        if (!FilterSortManager.openSortActivity(this, sort, getSelectedSort())) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_sort_data_not_ready));
        }
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
        redirectionListener.startActivityWithApplink(ApplinkConstInternalDiscovery.SEARCH_RESULT + "?" + query);
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

    protected void onSwipeToRefresh() {

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        setSpanCount(savedInstanceState.getInt(EXTRA_SPAN_COUNT));
        copySearchParameter(savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER));
        showBottomBar = savedInstanceState.getBoolean(EXTRA_SHOW_BOTTOM_BAR);
    }

    @Override
    public void setTotalSearchResultCount(String formattedResultCount) {
        if (bottomSheetListener != null) {
            bottomSheetListener.setFilterResultCount(formattedResultCount);
        }
    }

    public void onBottomSheetHide() {
        FilterTracking.eventApplyFilter(getFilterTrackingData(), getScreenName(), getSelectedFilter());
    }

    protected void removeSelectedFilter(String uniqueId) {
        if(filterController == null) return;

        Option option = OptionHelper.generateOptionFromUniqueId(uniqueId);

        removeFilterFromFilterController(option);
        refreshSearchParameter(filterController.getParameter());
        refreshFilterController(new HashMap<>(filterController.getParameter()));
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

    public void refreshSearchParameter(Map<String, String> queryParams) {
        if(searchParameter == null) return;

        this.searchParameter.getSearchParameterHashMap().clear();
        this.searchParameter.getSearchParameterHashMap().putAll(queryParams);
        this.searchParameter.getSearchParameterHashMap().put(SearchApiConst.ORIGIN_FILTER,
                SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE);
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

    @Override
    public void onBannerAdsClicked(int position, String applink, CpmData cpmData) {
        if(getActivity() == null || redirectionListener == null) return;

        redirectionListener.startActivityWithApplink(applink);

        trackBannerAdsClicked(position, applink, cpmData);
    }

    private void trackBannerAdsClicked(int position, String applink, CpmData data) {
        if (applink.contains(SHOP)) {
            TopAdsGtmTracker.eventSearchResultPromoShopClick(getActivity(), data, position);
        } else {
            TopAdsGtmTracker.eventSearchResultPromoProductClick(getActivity(), data, position);
        }
    }

    @Override
    public void onBannerAdsImpressionListener(int position, CpmData data) {
        TopAdsGtmTracker.eventSearchResultPromoView(getActivity(), data, position);
    }

    protected String getActiveTab() {
        return searchParameter.get(SearchApiConst.ACTIVE_TAB);
    }

    protected void removeSearchPageLoading() {
        if (isFirstActiveTab() && searchNavigationListener != null) {
            searchNavigationListener.removeSearchPageLoading();
        }
    }

    private FilterTrackingData getFilterTrackingData() {
        if (filterTrackingData == null) {
            filterTrackingData = new FilterTrackingData(FilterEventTracking.Event.CLICK_SEARCH_RESULT,
                    getFilterTrackingCategory(),
                    "",
                    FilterEventTracking.Category.PREFIX_SEARCH_RESULT_PAGE
                    );
        }
        return filterTrackingData;
    }

    protected abstract String getFilterTrackingCategory();
}