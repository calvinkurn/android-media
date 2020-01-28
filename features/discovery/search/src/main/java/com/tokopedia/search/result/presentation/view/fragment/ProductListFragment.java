package com.tokopedia.search.result.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.manager.AdultManager;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.discovery.common.model.WishlistTrackingModel;
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
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.search.R;
import com.tokopedia.search.analytics.GeneralSearchTrackingModel;
import com.tokopedia.search.analytics.RecommendationTracking;
import com.tokopedia.search.analytics.SearchTracking;
import com.tokopedia.search.result.presentation.ProductListSectionContract;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ProductItemDecoration;
import com.tokopedia.search.result.presentation.view.listener.BannedProductsRedirectToBrowserListener;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.search.result.presentation.view.listener.GlobalNavListener;
import com.tokopedia.search.result.presentation.view.listener.ProductListener;
import com.tokopedia.search.result.presentation.view.listener.QuickFilterListener;
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener;
import com.tokopedia.search.result.presentation.view.listener.RelatedSearchListener;
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener;
import com.tokopedia.search.result.presentation.view.listener.SearchPerformanceMonitoringListener;
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener;
import com.tokopedia.search.result.presentation.view.listener.TickerListener;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactoryImpl;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Category;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.FreeOngkir;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import static com.tokopedia.discovery.common.constants.SearchConstant.GCM.GCM_ID;
import static com.tokopedia.discovery.common.constants.SearchConstant.GCM.GCM_STORAGE;
import static com.tokopedia.discovery.common.constants.SearchConstant.ViewType.BIG_GRID;
import static com.tokopedia.discovery.common.constants.SearchConstant.ViewType.LIST;
import static com.tokopedia.discovery.common.constants.SearchConstant.ViewType.SMALL_GRID;

public class ProductListFragment
        extends BaseDaggerFragment
        implements ProductListAdapter.OnItemChangeView,
        ProductListSectionContract.View,
        ProductListener,
        TickerListener,
        SuggestionListener,
        RelatedSearchListener,
        QuickFilterListener,
        GlobalNavListener,
        BannerAdsListener,
        EmptyStateListener,
        RecommendationListener,
        BannedProductsRedirectToBrowserListener {

    private static final String SCREEN_SEARCH_PAGE_PRODUCT_TAB = "Search result - Product tab";
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123;
    private static final String SEARCH_RESULT_ENHANCE_ANALYTIC = "SEARCH_RESULT_ENHANCE_ANALYTIC";
    private static final String LAST_POSITION_ENHANCE_PRODUCT = "LAST_POSITION_ENHANCE_PRODUCT";
    private static final String SEARCH_PRODUCT_TRACE = "search_product_trace";
    private static final String EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER";
    private static final int REQUEST_CODE_LOGIN = 561;
    private static final String SHOP = "shop";
    private static final int DEFAULT_SPAN_COUNT = 2;

    @Inject
    ProductListSectionContract.Presenter presenter;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private SwipeRefreshLayout refreshLayout;
    private EndlessRecyclerViewScrollListener staggeredGridLayoutLoadMoreTriggerListener;
    private SearchNavigationListener searchNavigationListener;
    private BottomSheetListener bottomSheetListener;
    private RedirectionListener redirectionListener;
    private SearchPerformanceMonitoringListener searchPerformanceMonitoringListener;
    private RecyclerView recyclerView;
    private ProductListAdapter adapter;
    private TrackingQueue trackingQueue;
    private PerformanceMonitoring performanceMonitoring;
    private Config topAdsConfig;
    private SearchParameter searchParameter;
    private FilterController quickFilterController = new FilterController();
    private FilterController filterController = new FilterController();

    private boolean hasLoadData;
    private ArrayList<Sort> sort;
    private ArrayList<Filter> filters;
    private HashMap<String, String> selectedSort;
    private FilterTrackingData filterTrackingData;
    private boolean isListEmpty = false;

    public static ProductListFragment newInstance(SearchParameter searchParameter) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter);

        ProductListFragment productListFragment = new ProductListFragment();
        productListFragment.setArguments(args);

        return productListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDataFromArguments();

        if(getContext() == null) return;

        trackingQueue = new TrackingQueue(getContext());
    }

    private void loadDataFromArguments() {
        if(getArguments() != null) {
            copySearchParameter(getArguments().getParcelable(EXTRA_SEARCH_PARAMETER));
        }
    }

    private void copySearchParameter(@Nullable SearchParameter searchParameterToCopy) {
        if (searchParameterToCopy != null) {
            this.searchParameter = new SearchParameter(searchParameterToCopy);
        }
    }

    @Override
    protected void initInjector() {
        ProductListViewComponent component = DaggerProductListViewComponent.builder()
                .baseAppComponent(getComponent(BaseAppComponent.class))
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);
        presenter.initInjector(this);

        return inflater.inflate(R.layout.search_result_product_fragment_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initLayoutManager();
        initSwipeToRefresh(view);
        onViewCreatedBeforeLoadData(view);

        startToLoadDataForFirstActiveTab();
    }

    private void initLayoutManager() {
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
    }

    private void initSwipeToRefresh(View view) {
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(this::onSwipeToRefresh);
    }

    private void switchLayoutType() {
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

    private void switchLayoutTypeTo(SearchConstant.ViewType layoutType) {
        if (!getUserVisibleHint() || getAdapter() == null) {
            return;
        }

        switch (layoutType) {
            case LIST:
                staggeredGridLayoutManager.setSpanCount(1);
                getAdapter().changeListView();
                break;
            case SMALL_GRID:
                staggeredGridLayoutManager.setSpanCount(2);
                getAdapter().changeDoubleGridView();
                break;
            case BIG_GRID:
                staggeredGridLayoutManager.setSpanCount(1);
                getAdapter().changeSingleGridView();
                break;
        }

        refreshMenuItemGridIcon();
    }

    private void refreshMenuItemGridIcon() {
        if(searchNavigationListener == null || getAdapter() == null) return;

        searchNavigationListener.refreshMenuItemGridIcon(getAdapter().getTitleTypeRecyclerView(), getAdapter().getIconTypeRecyclerView());
    }

    private void onViewCreatedBeforeLoadData(@NonNull View view) {
        bindView(view);
        initTopAdsConfig();
        initTopAdsParams();
        setupAdapter();
        setupRecyclerViewListener();
        if (getUserVisibleHint()) {
            setupSearchNavigation();
        }
    }

    private void bindView(View rootView) {
        recyclerView = rootView.findViewById(R.id.recyclerview);
    }

    private void initTopAdsConfig() {
        if(getActivity() == null || getActivity().getApplicationContext() == null || presenter == null) return;

        topAdsConfig = new Config.Builder()
                .setSessionId(getRegistrationId())
                .setUserId(getUserId())
                .setEndpoint(Endpoint.PRODUCT)
                .build();
    }

    private void initTopAdsParams() {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        adsParams.getParam().put(TopAdsParams.KEY_QUERY, getQueryKey());
        adsParams.getParam().put(TopAdsParams.KEY_USER_ID, getUserId());

        if(getSearchParameter() != null) {
            getSearchParameter().cleanUpNullValuesInMap();
            adsParams.getParam().putAll(getSearchParameter().getSearchParameterHashMap());
        }

        topAdsConfig.setTopAdsParams(adsParams);
    }

    private void setupAdapter() {
        ProductListTypeFactory productListTypeFactory = new ProductListTypeFactoryImpl(
                this,
                this,
                this, this,
                this, this,
                this, this, this,
                this,
                topAdsConfig);
        adapter = new ProductListAdapter(this, productListTypeFactory);
        recyclerView.setLayoutManager(getStaggeredGridLayoutManager());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(createProductItemDecoration());
    }

    @NonNull
    private ProductItemDecoration createProductItemDecoration() {
        return new ProductItemDecoration(getContext().getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16));
    }

    private void setupRecyclerViewListener() {
        staggeredGridLayoutLoadMoreTriggerListener = getEndlessRecyclerViewListener(getStaggeredGridLayoutManager());
        recyclerView.addOnScrollListener(staggeredGridLayoutLoadMoreTriggerListener);
    }

    private EndlessRecyclerViewScrollListener getEndlessRecyclerViewListener(RecyclerView.LayoutManager recyclerViewLayoutManager) {
        return new EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isAllowLoadMore()) {
                    loadMoreProduct(presenter.getStartFrom());
                } else {
                    adapter.removeLoading();
                }
            }
        };
    }

    private boolean isAllowLoadMore() {
        return getUserVisibleHint()
                && presenter != null
                && presenter.hasNextPage();
    }

    private void startToLoadDataForFirstActiveTab() {
        if (isFirstActiveTab()) {
            hasLoadData = true;
            onFirstTimeLaunch();
        }
    }

    private boolean isFirstActiveTab() {
        return getActiveTab().equals(SearchConstant.ActiveTab.PRODUCT);
    }

    private String getActiveTab() {
        return searchParameter.get(SearchApiConst.ACTIVE_TAB);
    }

    private void onFirstTimeLaunch() {
        if (presenter != null) {
            presenter.setIsFirstTimeLoad(true);
        }

        reloadData();
    }

    @Override
    public void showRefreshLayout() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshLayout() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        searchNavigationListener = castContextToSearchNavigationListener(context);
        bottomSheetListener = castContextToBottomSheetListener(context);
        redirectionListener = castContextToRedirectionListener(context);
        searchPerformanceMonitoringListener = castContextToSearchPerformanceMonitoring(context);
    }

    private SearchNavigationListener castContextToSearchNavigationListener(Context context) {
        if (context instanceof SearchNavigationListener) {
            return (SearchNavigationListener) context;
        }

        return null;
    }

    private BottomSheetListener castContextToBottomSheetListener(Context context) {
        if (context instanceof BottomSheetListener) {
            return (BottomSheetListener) context;
        }

        return null;
    }

    private RedirectionListener castContextToRedirectionListener(Context context) {
        if (context instanceof RedirectionListener) {
            return (RedirectionListener) context;
        }

        return null;
    }

    private SearchPerformanceMonitoringListener castContextToSearchPerformanceMonitoring(Context context) {
        if(context instanceof SearchPerformanceMonitoringListener) {
            return (SearchPerformanceMonitoringListener) context;
        }

        return null;
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

    private void setupSearchNavigation() {
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

    private void openFilterActivity() {
        if (!isFilterDataAvailable() && getActivity() != null) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_filter_data_not_ready));
            return;
        }

        if (bottomSheetListener != null && isUsingBottomSheetFilter()) {
            openBottomSheetFilter();
        } else {
            FilterSortManager.openFilterPage(getFilterTrackingData(), this, getScreenName(), searchParameter.getSearchParameterHashMap());
        }
    }

    private boolean isFilterDataAvailable() {
        return filters != null && !filters.isEmpty();
    }

    private void openBottomSheetFilter() {
        if(searchParameter == null || getFilters() == null) return;

        FilterTracking.eventOpenFilterPage(getFilterTrackingData());

        bottomSheetListener.loadFilterItems(getFilters(), searchParameter.getSearchParameterHashMap());
        bottomSheetListener.launchFilterBottomSheet();
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

    private ArrayList<Filter> getFilters() {
        return filters;
    }

    private void openSortActivity() {
        if(getActivity() == null) return;

        if (!FilterSortManager.openSortActivity(this, sort, getSelectedSort())) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_sort_data_not_ready));
        }
    }

    private void screenTrack() {
        if (getUserVisibleHint()
                && getActivity() != null
                && getActivity().getApplicationContext() != null) {
            SearchTracking.screenTrackSearchSectionFragment(getScreenName());
        }
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

    private StaggeredGridLayoutManager getStaggeredGridLayoutManager() {
        return staggeredGridLayoutManager;
    }

    @Override
    public void addProductList(List<Visitable> list) {
        isListEmpty = false;

        sendProductImpressionTrackingEvent(list);

        adapter.appendItems(list);
    }

    public void setProductList(List<Visitable> list) {
        adapter.clearData();

        stopSearchResultPagePerformanceMonitoring();
        addProductList(list);
    }

    public void addRecommendationList(List<Visitable> list){
        isListEmpty = false;

        adapter.appendItems(list);
    }

    private void stopSearchResultPagePerformanceMonitoring() {
        recyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if(searchPerformanceMonitoringListener != null) {
                            searchPerformanceMonitoringListener.stopPerformanceMonitoring();
                        }

                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    private void sendProductImpressionTrackingEvent(List<Visitable> list) {
        String userId = getUserId();
        String searchRef = getSearchRef();
        List<Object> dataLayerList = new ArrayList<>();
        List<ProductItemViewModel> productItemViewModels = new ArrayList<>();
        for (Visitable object : list) {
            if (object instanceof ProductItemViewModel) {
                ProductItemViewModel item = (ProductItemViewModel) object;
                if (!item.isTopAds()) {
                    String filterSortParams
                            = SearchTracking.generateFilterAndSortEventLabel(getSelectedFilter(), getSelectedSort());
                    dataLayerList.add(item.getProductAsObjectDataLayer(userId, filterSortParams, searchRef));
                    productItemViewModels.add(item);
                }
            }
        }
        SearchTracking.eventImpressionSearchResultProduct(trackingQueue, dataLayerList, productItemViewModels, getQueryKey());
    }

    private String getSearchRef() {
        return searchParameter.get(SearchApiConst.SEARCH_REF);
    }

    private void loadMoreProduct(final int startRow) {
        generateLoadMoreParameter(startRow);

        presenter.loadMoreData(searchParameter.getSearchParameterMap());
    }

    @Override
    public void showNetworkError(final int startRow) {
        if (adapter.isListEmpty()) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), this::reloadData);
        } else {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), () -> {
                presenter.setStartFrom(startRow);
                loadMoreProduct(startRow);
            }).showRetrySnackbar();
        }
    }

    private void generateLoadMoreParameter(int startRow) {
        if(getSearchParameter() == null) setSearchParameter(new SearchParameter());

        getSearchParameter().set(SearchApiConst.UNIQUE_ID, generateUniqueId());
        getSearchParameter().set(SearchApiConst.USER_ID, getUserId());
        getSearchParameter().set(SearchApiConst.START, String.valueOf(startRow));
    }

    private String generateUniqueId() {
        return presenter.isUserLoggedIn() ?
                AuthHelper.getMD5Hash(getUserId()) :
                AuthHelper.getMD5Hash(getRegistrationId());
    }

    @Override
    public String getScreenNameId() {
        return SCREEN_SEARCH_PAGE_PRODUCT_TAB;
    }

    private boolean isSortEnabled() {
        return true;
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

        if (requestCode == REQUEST_CODE_GOTO_PRODUCT_DETAIL
                && data != null
                && data.getExtras() != null
                && data.getExtras().getInt(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, -1) != -1) {
            int position = data.getExtras().getInt(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, -1);
            boolean isWishlist = data.getExtras().getBoolean(SearchConstant.Wishlist.WISHLIST_STATUS_IS_WISHLIST, false);

            updateWishlistFromPDP(position, isWishlist);

        }
        if (getActivity() != null) {
            AdultManager.handleActivityResult(getActivity(), requestCode, resultCode, data);
        }
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
        String userId = presenter.getUserId();
        SearchTracking.eventSearchResultSort(getScreenName(), selectedSortName, userId);
        if(searchParameter != null) {
            searchParameter.getSearchParameterHashMap().put(SearchApiConst.ORIGIN_FILTER,
                    SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE);

            searchParameter.getSearchParameterHashMap().putAll(UrlParamUtils.getParamMap(autoApplyFilter));

            searchParameter.getSearchParameterHashMap().putAll(getSelectedSort());
        }
        clearDataFilterSort();
        reloadData();
    }

    private void updateWishlistFromPDP(int position, boolean isWishlist) {
        if (adapter != null && (adapter.isProductItem(position) || adapter.isRecommendationItem(position))) {
            adapter.updateWishlistStatus(position, isWishlist);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        TopAdsGtmTracker.getInstance().eventSearchResultProductView(trackingQueue, getQueryKey(), SCREEN_SEARCH_PAGE_PRODUCT_TAB);
        trackingQueue.sendAll();
    }

    @Override
    public void onProductImpressed(ProductItemViewModel item, int adapterPosition) {
        if (item.isTopAds()) {
            new ImpresionTask().execute(item.getTopadsImpressionUrl());
            Product product = new Product();
            product.setId(item.getProductID());
            product.setName(item.getProductName());
            product.setPriceFormat(item.getPrice());
            product.setCategory(new Category(item.getCategoryID()));
            product.setFreeOngkir(createTopAdsProductFreeOngkirForTracking(item));
            TopAdsGtmTracker.getInstance().addSearchResultProductViewImpressions(product, adapterPosition);
        }
    }

    @Override
    public void onGlobalNavWidgetClicked(GlobalNavViewModel.Item item, String keyword) {
        redirectionStartActivity(item.getApplink(), item.getUrl());

        SearchTracking.trackEventClickGlobalNavWidgetItem(item.getGlobalNavItemAsObjectDataLayer(),
                keyword, item.getName(), item.getApplink());
    }

    @Override
    public void onGlobalNavWidgetClickSeeAll(GlobalNavViewModel model) {
        redirectionStartActivity(model.getSeeAllApplink(), model.getSeeAllUrl());

        SearchTracking.eventUserClickSeeAllGlobalNavWidget(model.getKeyword(),
                model.getTitle(), model.getSeeAllApplink());
    }

    private void redirectionStartActivity(String applink, String url) {
        if(redirectionListener == null) return;

        if (!TextUtils.isEmpty(applink)) {
            redirectionListener.startActivityWithApplink(applink);
        } else {
            redirectionListener.startActivityWithUrl(url);
        }
    }

    @Override
    public void onItemClicked(ProductItemViewModel item, int adapterPosition) {
        Intent intent = getProductIntent(item.getProductID(), item.getWarehouseID());

        if(intent != null) {
            intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition);
            sendItemClickTrackingEvent(item, adapterPosition);
            startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
        }
    }

    @Override
    public void onProductClick(@NotNull RecommendationItem item, @org.jetbrains.annotations.Nullable String layoutType, @NotNull int... position) {
        Intent intent = getProductIntent(String.valueOf(item.getProductId()), "0");

        if(intent != null) {
            intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, item.getPosition());
            if(presenter.isUserLoggedIn()){
                RecommendationTracking.Companion.eventClickProductRecommendationLogin(item, String.valueOf(item.getPosition()));
            }else {
                RecommendationTracking.Companion.eventClickProductRecommendationNonLogin(item, String.valueOf(item.getPosition()));
            }
            startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
        }
    }

    @Override
    public void onProductImpression(@NotNull RecommendationItem item) {
        if(presenter.isUserLoggedIn()){
            RecommendationTracking.Companion.eventImpressionProductRecommendationLogin(trackingQueue, item, String.valueOf(item.getPosition()));
        } else {
            RecommendationTracking.Companion.eventImpressionProductRecommendationNonLogin(trackingQueue, item, String.valueOf(item.getPosition()));
        }
    }

    @Override
    public void onWishlistClick(@NotNull RecommendationItem item, boolean isAddWishlist, @NotNull Function2<? super Boolean, ? super Throwable, Unit> callback) {
        presenter.handleWishlistButtonClicked(item);
        if(presenter.isUserLoggedIn()){
            RecommendationTracking.Companion.eventUserClickProductToWishlistForUserLogin(!isAddWishlist);
        } else {
            RecommendationTracking.Companion.eventUserClickProductToWishlistForNonLogin();
        }
    }

    private Intent getProductIntent(String productId, String warehouseId){
        if (getContext() == null) {
            return null;
        }
        
        if (!TextUtils.isEmpty(warehouseId)) {
            return RouteManager.getIntent(getContext(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_WAREHOUSE_ID, productId, warehouseId);
        } else {
            return RouteManager.getIntent(getContext(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        }
    }

    @Override
    public void onLongClick(ProductItemViewModel item, int adapterPosition) {
        if(getSearchParameter() == null) return;
        SearchTracking.trackEventProductLongPress(getSearchParameter().getSearchQuery(), item.getProductID());
        startSimilarSearch(item.getProductID());
    }

    private void startSimilarSearch(String productId) {
        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT, productId);
        intent.putExtra(SearchConstant.SimilarSearch.QUERY, getQueryKey());

        startActivity(intent);
    }

    private void sendItemClickTrackingEvent(ProductItemViewModel item, int pos) {
        String userId = getUserId();
        if (item.isTopAds()) {
            sendItemClickTrackingEventForTopAdsItem(item, pos);
        } else {
            String filterSortParams
                    = SearchTracking.generateFilterAndSortEventLabel(getSelectedFilter(), getSelectedSort());
            String searchRef = getSearchRef();
            SearchTracking.trackEventClickSearchResultProduct(
                    item,
                    item.getProductAsObjectDataLayer(userId, filterSortParams, searchRef),
                    item.getPageNumber(),
                    getQueryKey(),
                    filterSortParams
            );
        }
    }

    private void sendItemClickTrackingEventForTopAdsItem(ProductItemViewModel item, int pos) {
        new ImpresionTask().execute(item.getTopadsClickUrl());

        Product product = createTopAdsProductForTracking(item);

        TopAdsGtmTracker.eventSearchResultProductClick(getContext(), getQueryKey(), product, pos, SCREEN_SEARCH_PAGE_PRODUCT_TAB);
    }

    private Product createTopAdsProductForTracking(ProductItemViewModel item) {
        Product product  = new Product();
        product.setId(item.getProductID());
        product.setName(item.getProductName());
        product.setPriceFormat(item.getPrice());
        product.setCategory(new Category(item.getCategoryID()));
        product.setFreeOngkir(createTopAdsProductFreeOngkirForTracking(item));

        return product;
    }

    private FreeOngkir createTopAdsProductFreeOngkirForTracking(ProductItemViewModel item) {
        if (item != null && item.getFreeOngkirViewModel() != null) {
            return new FreeOngkir(
                    item.getFreeOngkirViewModel().isActive(),
                    item.getFreeOngkirViewModel().getImageUrl()
            );
        }

        return null;
    }

    @Override
    public void onWishlistButtonClicked(final ProductItemViewModel productItem) {
        presenter.handleWishlistButtonClicked(productItem);
    }

    @Override
    public void onTickerClicked(String queryParams) {
        SearchTracking.trackEventClickSortPriceMinTicker(getQueryKey());
        applyParamsFromTicker(UrlParamUtils.getParamMap(queryParams));
    }

    private void applyParamsFromTicker(HashMap<String, String> tickerParams) {
        HashMap<String, String> params = new HashMap<>(quickFilterController.getParameter());
        params.putAll(tickerParams);
        refreshSearchParameter(params);
        refreshFilterController(params);
        clearDataFilterSort();
        reloadData();
    }

    public void refreshSearchParameter(Map<String, String> queryParams) {
        if(searchParameter == null) return;

        this.searchParameter.getSearchParameterHashMap().clear();
        this.searchParameter.getSearchParameterHashMap().putAll(queryParams);
        this.searchParameter.getSearchParameterHashMap().put(SearchApiConst.ORIGIN_FILTER,
                SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE);
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

    @Override
    public void onTickerDismissed() {
        if (presenter != null) {
            presenter.setIsTickerHasDismissed(true);
        }
    }

    @Override
    public boolean isTickerHasDismissed() {
        return presenter != null && presenter.getIsTickerHasDismissed();
    }

    @Override
    public void onSuggestionClicked(String queryParams) {
        performNewProductSearch(queryParams);
    }

    @Override
    public void onRelatedSearchClicked(String queryParams, String keyword) {
        SearchTracking.eventClickRelatedSearch(getContext(), getQueryKey(), keyword);
        performNewProductSearch(queryParams);
    }

    private void performNewProductSearch(String queryParams) {
        redirectionListener.startActivityWithApplink(ApplinkConstInternalDiscovery.SEARCH_RESULT + "?" + queryParams);
    }

    @Override
    public boolean isQuickFilterSelected(Option option) {
        if(quickFilterController == null) return false;

        return quickFilterController.getFilterViewState(option.getUniqueId());
    }

    @Override
    public void onQuickFilterSelected(Option option) {
        if(quickFilterController == null) return;

        boolean isQuickFilterSelectedReversed = !isQuickFilterSelected(option);

        setFilterToQuickFilterController(option, isQuickFilterSelectedReversed);

        Map<String, String> queryParams = quickFilterController.getParameter();
        refreshSearchParameter(queryParams);
        refreshFilterController(new HashMap<>(queryParams));

        clearDataFilterSort();
        reloadData();

        trackEventSearchResultQuickFilter(option.getKey(), option.getValue(), isQuickFilterSelectedReversed);
    }

    private void setFilterToQuickFilterController(Option option, boolean isQuickFilterSelected) {
        if(quickFilterController == null) return;

        if (option.isCategoryOption()) {
            quickFilterController.setFilter(option, isQuickFilterSelected, true);
        } else {
            quickFilterController.setFilter(option, isQuickFilterSelected);
        }
    }

    private void trackEventSearchResultQuickFilter(String filterName, String filterValue, boolean isSelected) {
        SearchTracking.trackEventClickQuickFilter(filterName, filterValue, isSelected, getUserId());
    }

    @Override
    public void onSelectedFilterRemoved(String uniqueId) {
        removeSelectedFilter(uniqueId);
    }

    private void removeSelectedFilter(String uniqueId) {
        if(filterController == null) return;

        Option option = OptionHelper.generateOptionFromUniqueId(uniqueId);

        removeFilterFromFilterController(option);
        refreshSearchParameter(filterController.getParameter());
        refreshFilterController(new HashMap<>(filterController.getParameter()));
        clearDataFilterSort();
        reloadData();
    }

    private void removeFilterFromFilterController(Option option) {
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

    @Override
    public void onEmptyButtonClicked() {
        SearchTracking.eventUserClickNewSearchOnEmptySearch(getContext(), getScreenName());
        showSearchInputView();
    }

    private void showSearchInputView() {
        redirectionListener.showSearchInputView();
    }

    @Override
    public List<Option> getSelectedFilterAsOptionList() {
        return getOptionListFromFilterController();
    }

    private List<Option> getOptionListFromFilterController() {
        if(filterController == null) return new ArrayList<>();

        return OptionHelper.combinePriceFilterIfExists(filterController.getActiveFilterOptionList(),
                getResources().getString(R.string.empty_state_selected_filter_price_name));
    }

    @Override
    public void errorAddWishList(String errorMessage, String productId) {
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void successAddWishlist(ProductItemViewModel productItemViewModel) {
        trackSuccessAddWishlist(productItemViewModel);

        adapter.updateWishlistStatus(productItemViewModel.getProductID(), true);
        enableWishlistButton(productItemViewModel.getProductID());
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
    }

    private void trackSuccessAddWishlist(ProductItemViewModel productItemViewModel) {
        WishlistTrackingModel wishlistTrackingModel = createWishlistTrackingModel(productItemViewModel);
        wishlistTrackingModel.setAddWishlist(true);

        SearchTracking.eventSuccessWishlistSearchResultProduct(wishlistTrackingModel);
    }

    private WishlistTrackingModel createWishlistTrackingModel(ProductItemViewModel productItemViewModel) {
        WishlistTrackingModel wishlistTrackingModel = new WishlistTrackingModel();

        wishlistTrackingModel.setProductId(productItemViewModel.getProductID());
        wishlistTrackingModel.setTopAds(productItemViewModel.isTopAds());
        wishlistTrackingModel.setKeyword(getQueryKey());
        wishlistTrackingModel.setUserLoggedIn(presenter.isUserLoggedIn());

        return wishlistTrackingModel;
    }

    @Override
    public void errorRemoveWishlist(String errorMessage, String productId) {
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void successRemoveWishlist(ProductItemViewModel productItemViewModel) {
        trackSuccessRemoveWishlist(productItemViewModel);

        adapter.updateWishlistStatus(productItemViewModel.getProductID(), false);
        enableWishlistButton(productItemViewModel.getProductID());
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist));
    }

    private void trackSuccessRemoveWishlist(ProductItemViewModel productItemViewModel) {
        WishlistTrackingModel wishlistTrackingModel = createWishlistTrackingModel(productItemViewModel);
        wishlistTrackingModel.setAddWishlist(false);

        SearchTracking.eventSuccessWishlistSearchResultProduct(wishlistTrackingModel);
    }

    @Override
    public void successRemoveRecommendationWishlist(String productId) {
        adapter.updateWishlistStatus(productId, false);
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist));
    }

    @Override
    public void successAddRecommendationWishlist(String productId) {
        adapter.updateWishlistStatus(productId, true);
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
    }

    @Override
    public void errorRecommendationWishlist(String errorMessage, String productId) {
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void stopTracePerformanceMonitoring() {
        if (performanceMonitoring != null) {
            performanceMonitoring.stopTrace();
        }
    }

    @Override
    public String getUserId() {
        if (presenter == null) return "0";

        return presenter.getUserId();
    }

    @Override
    public void disableWishlistButton(String productId) {
        adapter.setWishlistButtonEnabled(productId, false);
    }

    @Override
    public void enableWishlistButton(String productId) {
        adapter.setWishlistButtonEnabled(productId, true);
    }

    @Override
    public String getQueryKey() {
        return searchParameter == null ? "" : searchParameter.getSearchQuery();
    }

    @Override
    public void setEmptyProduct(GlobalNavViewModel globalNavViewModel) {
        isListEmpty = true;
        adapter.setGlobalNavViewModel(globalNavViewModel);
        presenter.clearData();
        adapter.showEmptyState(getActivity(), isFilterActive());
    }

    private boolean isFilterActive() {
        if(filterController == null) return false;

        return filterController.isFilterActive();
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

    private void setFilterData(List<Filter> filters) {
        this.filters = new ArrayList<>();
        if (filters == null) {
            return;
        }

        this.filters.addAll(filters);
    }

    private void setSortData(List<Sort> sorts) {
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

    private void refreshAdapterForEmptySearch() {
        if (adapter != null) {
            presenter.clearData();
            adapter.showEmptyState(getActivity(), isFilterActive());
        }
    }

    @Override
    public void setBannedProductsErrorMessage(List<Visitable> bannedProductsErrorMessageAsList) {
        adapter.appendItems(bannedProductsErrorMessageAsList);
    }

    @Override
    public void trackEventImpressionBannedProducts(boolean isEmptySearch) {
        if (isEmptySearch) {
            SearchTracking.trackEventImpressionBannedProductsEmptySearch(getQueryKey());
        }
        else {
            SearchTracking.trackEventImpressionBannedProductsWithResult(getQueryKey());
        }
    }

    @Override
    public void trackEventImpressionSortPriceMinTicker() {
        SearchTracking.trackEventImpressionSortPriceMinTicker(getQueryKey());
    }

    public void reloadData() {
        if (adapter == null || getSearchParameter() == null) {
            return;
        }

        showRefreshLayout();
        presenter.clearData();
        adapter.clearData();
        initTopAdsParams();
        generateLoadMoreParameter(0);
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_PRODUCT_TRACE);
        presenter.loadData(getSearchParameter().getSearchParameterMap());
        TopAdsGtmTracker.getInstance().clearDataLayerList();
    }

    private void onSwipeToRefresh() {
        reloadData();
    }

    private ProductListAdapter getAdapter() {
        return adapter;
    }

    private ProductListSectionContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onChangeList() {
        recyclerView.requestLayout();
    }

    @Override
    public void onChangeDoubleGrid() {
        recyclerView.requestLayout();
    }

    @Override
    public void onChangeSingleGrid() {
        recyclerView.requestLayout();
    }

    private SearchParameter getSearchParameter() {
        return searchParameter;
    }

    private void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
    }

    @Override
    public void backToTop() {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return getScreenNameId();
    }

    @Override
    public void addLoading() {
        adapter.addLoading();
    }

    @Override
    public void removeLoading() {
        removeSearchPageLoading();
        adapter.removeLoading();
    }

    private void removeSearchPageLoading() {
        if (isFirstActiveTab() && searchNavigationListener != null) {
            searchNavigationListener.removeSearchPageLoading();
        }
    }

    @Override
    public void initQuickFilter(List<Filter> quickFilterList) {
        if(quickFilterController == null) return;

        quickFilterController.initFilterController(getSearchParameter().getSearchParameterHashMap(), quickFilterList);
    }

    @Override
    public void setAutocompleteApplink(String autocompleteApplink) {
        if (redirectionListener != null) {
            redirectionListener.setAutocompleteApplink(autocompleteApplink);
        }
    }

    @Override
    public void sendTrackingEventAppsFlyerViewListingSearch(JSONArray afProdIds, String query, ArrayList<String> prodIdArray) {
        SearchTracking.eventAppsFlyerViewListingSearch(afProdIds, query, prodIdArray);
    }

    @Override
    public void sendTrackingEventMoEngageSearchAttempt(String query, boolean hasProductList, HashMap<String, String> category) {
        SearchTracking.trackMoEngageSearchAttempt(query, hasProductList, category);
    }

    @Override
    public void sendTrackingGTMEventSearchAttempt(GeneralSearchTrackingModel generalSearchTrackingModel) {
        SearchTracking.trackGTMEventSearchAttempt(generalSearchTrackingModel);
    }

    @Override
    public void sendImpressionGlobalNav(GlobalNavViewModel globalNavViewModel) {
        List<Object> dataLayerList = new ArrayList<>();
        for (GlobalNavViewModel.Item item : globalNavViewModel.getItemList()) {
            dataLayerList.add(item.getGlobalNavItemAsObjectDataLayer());
        }
        SearchTracking.trackEventImpressionGlobalNavWidgetItem(trackingQueue, dataLayerList, globalNavViewModel.getKeyword());
    }

    @Override
    public boolean isAnyFilterActive() {
        return isFilterActive();
    }

    @Override
    public void clearLastProductItemPositionFromCache() {
        if(getActivity() == null || getActivity().getApplicationContext() == null) return;
        LocalCacheHandler.clearCache(getActivity().getApplicationContext(), SEARCH_RESULT_ENHANCE_ANALYTIC);
    }

    @Override
    public void saveLastProductItemPositionToCache(int lastProductItemPositionToCache) {
        if(getActivity() == null || getActivity().getApplicationContext() == null) return;

        LocalCacheHandler cache = new LocalCacheHandler(getActivity().getApplicationContext(), SEARCH_RESULT_ENHANCE_ANALYTIC);
        cache.putInt(LAST_POSITION_ENHANCE_PRODUCT, lastProductItemPositionToCache);
        cache.applyEditor();
    }

    @Override
    public int getLastProductItemPositionFromCache() {
        if(getActivity() == null || getActivity().getApplicationContext() == null) return 0;

        LocalCacheHandler cache = new LocalCacheHandler(getActivity().getApplicationContext(), SEARCH_RESULT_ENHANCE_ANALYTIC);
        return cache.getInt(LAST_POSITION_ENHANCE_PRODUCT, 0);
    }

    @Override
    public void updateScrollListener() {
        staggeredGridLayoutLoadMoreTriggerListener.updateStateAfterGetData();
    }

    @Override
    public void launchLoginActivity(String productId) {
        Bundle extras = new Bundle();
        extras.putString("product_id", productId);

        if (getActivity() == null) return;

        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.LOGIN);
        intent.putExtras(extras);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    public void showAdultRestriction() {
        AdultManager.showAdultPopUp(this, AdultManager.ORIGIN_SEARCH_PAGE, getQueryKey());
    }

    @Override
    public void sendTrackingWishlistNonLogin(ProductItemViewModel productItemViewModel) {
        WishlistTrackingModel wishlistTrackingModel = createWishlistTrackingModel(productItemViewModel);
        wishlistTrackingModel.setAddWishlist(!productItemViewModel.isWishlisted());

        SearchTracking.eventSuccessWishlistSearchResultProduct(wishlistTrackingModel);
    }

    @Override
    public void redirectSearchToAnotherPage(String applink) {
        redirectionListener.startActivityWithApplink(applink);
        finishActivity();
    }

    @Override
    public void sendTrackingForNoResult(String resultCode, String alternativeKeyword, String keywordProcess) {
        SearchTracking.eventSearchNoResult(getQueryKey(), getScreenName(), getSelectedFilter(), alternativeKeyword, resultCode, keywordProcess);
    }

    private void finishActivity() {
        if(getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void setDefaultLayoutType(int defaultView) {
        switch (defaultView) {
            case SearchConstant.DefaultViewType.SMALL_GRID:
                switchLayoutTypeTo(SMALL_GRID);
                break;
            case SearchConstant.DefaultViewType.LIST:
                switchLayoutTypeTo(LIST);
                break;
            default:
                switchLayoutTypeTo(SMALL_GRID);
                break;
        }
    }

    @Override
    public void showFreeOngkirShowCase(boolean hasFreeOngkirBadge) {
        if (getActivity() != null) {
            FreeOngkirShowCaseDialog.show(getActivity(), hasFreeOngkirBadge);
        }
    }

    private String getFilterTrackingCategory() {
        return FilterEventTracking.Category.FILTER_PRODUCT;
    }

    @Override
    public void onGoToBrowserClicked(boolean isEmptySearch, @NotNull String liteUrl) {
        trackEventClickGoToBrowserBannedProducts(isEmptySearch);

        if (presenter != null) {
            presenter.onBannedProductsGoToBrowserClick(liteUrl);
        }
    }

    @Override
    public void redirectToBrowser(String url) {
        if (TextUtils.isEmpty(url)) return;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void trackEventClickGoToBrowserBannedProducts(boolean isEmptySearch) {
        if (isEmptySearch) {
            SearchTracking.trackEventClickGoToBrowserBannedProductsEmptySearch(getQueryKey());
        }
        else {
            SearchTracking.trackEventClickGoToBrowserBannedProductsWithResult(getQueryKey());
        }
    }

    private boolean isUsingBottomSheetFilter() {
        return presenter != null && presenter.isUsingBottomSheetFilter();
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

    @Override
    public void setTotalSearchResultCount(String formattedResultCount) {
        if (bottomSheetListener != null) {
            bottomSheetListener.setFilterResultCount(formattedResultCount);
        }
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
    public void renderFailRequestDynamicFilter() {
        if(getActivity() == null) return;

        NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_get_dynamic_filter));
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

    public String getRegistrationId() {
        if(getActivity() == null || getActivity().getApplicationContext() == null) return "";

        LocalCacheHandler cache = new LocalCacheHandler(getActivity().getApplicationContext(), GCM_STORAGE);
        return cache.getString(GCM_ID, "");
    }

    public void onBottomSheetHide() {
        FilterTracking.eventApplyFilter(getFilterTrackingData(), getScreenName(), getSelectedFilter());
    }
}
