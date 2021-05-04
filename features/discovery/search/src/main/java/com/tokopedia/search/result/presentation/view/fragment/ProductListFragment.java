package com.tokopedia.search.result.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConsInternalHome;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.coachmark.CoachMark2;
import com.tokopedia.coachmark.CoachMark2Item;
import com.tokopedia.coachmark.CoachMarkBuilder;
import com.tokopedia.coachmark.CoachMarkItem;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.manager.AdultManager;
import com.tokopedia.discovery.common.manager.ProductCardOptionsManager;
import com.tokopedia.discovery.common.model.ProductCardOptionsModel;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.discovery.common.model.WishlistTrackingModel;
import com.tokopedia.discovery.common.utils.URLParser;
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet;
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel;
import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData;
import com.tokopedia.filter.newdynamicfilter.controller.FilterController;
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.iris.util.IrisSession;
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel;
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant;
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils;
import com.tokopedia.productcard.IProductCardView;
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.search.R;
import com.tokopedia.search.analytics.GeneralSearchTrackingModel;
import com.tokopedia.search.analytics.RecommendationTracking;
import com.tokopedia.search.analytics.SearchEventTracking;
import com.tokopedia.search.analytics.SearchTracking;
import com.tokopedia.search.di.module.SearchContextModule;
import com.tokopedia.search.result.presentation.ProductListSectionContract;
import com.tokopedia.search.result.presentation.model.BannerDataView;
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView;
import com.tokopedia.search.result.presentation.model.BroadMatchDataView;
import com.tokopedia.search.result.presentation.model.EmptySearchProductDataView;
import com.tokopedia.search.result.presentation.model.GlobalNavDataView;
import com.tokopedia.search.result.presentation.model.InspirationCardOptionDataView;
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView;
import com.tokopedia.search.result.presentation.model.ProductItemDataView;
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView;
import com.tokopedia.search.result.presentation.model.SuggestionDataView;
import com.tokopedia.search.result.presentation.model.TickerDataView;
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ProductItemDecoration;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.BannerListener;
import com.tokopedia.search.result.presentation.view.listener.BroadMatchListener;
import com.tokopedia.search.result.presentation.view.listener.ChooseAddressListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.search.result.presentation.view.listener.GlobalNavListener;
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener;
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener;
import com.tokopedia.search.result.presentation.view.listener.ProductListener;
import com.tokopedia.search.result.presentation.view.listener.QuickFilterElevation;
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener;
import com.tokopedia.search.result.presentation.view.listener.SearchInTokopediaListener;
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationClickListener;
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener;
import com.tokopedia.search.result.presentation.view.listener.SearchPerformanceMonitoringListener;
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener;
import com.tokopedia.search.result.presentation.view.listener.TickerListener;
import com.tokopedia.search.result.presentation.view.listener.TopAdsImageViewListener;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactoryImpl;
import com.tokopedia.search.utils.SearchFilterUtilsKt;
import com.tokopedia.search.utils.SearchKotlinExtKt;
import com.tokopedia.search.utils.SearchLogger;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.sortfilter.SortFilter;
import com.tokopedia.sortfilter.SortFilterItem;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Category;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.FreeOngkir;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.unifycomponents.Toaster;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import static com.tokopedia.discovery.common.constants.SearchApiConst.PREVIOUS_KEYWORD;
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
        GlobalNavListener,
        BannerAdsListener,
        EmptyStateListener,
        RecommendationListener,
        InspirationCarouselListener,
        BroadMatchListener,
        InspirationCardListener,
        QuickFilterElevation,
        SortFilterBottomSheet.Callback,
        SearchInTokopediaListener,
        SearchNavigationClickListener,
        TopAdsImageViewListener,
        ChooseAddressListener,
        BannerListener {

    private static final String SCREEN_SEARCH_PAGE_PRODUCT_TAB = "Search result - Product tab";
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123;
    private static final String SEARCH_RESULT_ENHANCE_ANALYTIC = "SEARCH_RESULT_ENHANCE_ANALYTIC";
    private static final String LAST_POSITION_ENHANCE_PRODUCT = "LAST_POSITION_ENHANCE_PRODUCT";
    private static final String SEARCH_PRODUCT_TRACE = "search_product_trace";
    private static final String EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER";
    private static final String SEARCH_RESULT_PRODUCT_ONBOARDING_TAG = "SEARCH_RESULT_PRODUCT_ONBOARDING_TAG";
    private static final int REQUEST_CODE_LOGIN = 561;
    private static final String SHOP = "shop";
    private static final int DEFAULT_SPAN_COUNT = 2;

    @Inject
    ProductListSectionContract.Presenter presenter;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private SwipeRefreshLayout refreshLayout;
    private EndlessRecyclerViewScrollListener staggeredGridLayoutLoadMoreTriggerListener;
    @Nullable private SearchNavigationListener searchNavigationListener;
    private RedirectionListener redirectionListener;
    private SearchPerformanceMonitoringListener searchPerformanceMonitoringListener;
    private RecyclerView recyclerView;
    private ProductListAdapter adapter;
    private TrackingQueue trackingQueue;
    private PerformanceMonitoring performanceMonitoring;
    private Config topAdsConfig;
    private SearchParameter searchParameter;
    private FilterController filterController = new FilterController();
    private IrisSession irisSession;
    private SortFilter searchSortFilter;
    private LinearLayout shimmeringView;
    @Nullable private SortFilterBottomSheet sortFilterBottomSheet = null;
    private FilterTrackingData filterTrackingData;

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
        initTrackingQueue();
    }

    private void loadDataFromArguments() {
        if (getArguments() != null) {
            copySearchParameter(getArguments().getParcelable(EXTRA_SEARCH_PARAMETER));
        }
    }

    private void copySearchParameter(@Nullable SearchParameter searchParameterToCopy) {
        if (searchParameterToCopy != null) {
            this.searchParameter = new SearchParameter(searchParameterToCopy);
        }
    }

    private void initTrackingQueue() {
        if (getContext() == null) return;
        trackingQueue = new TrackingQueue(getContext());
    }

    @Override
    protected void initInjector() {
        if (getActivity() == null) return;

        ProductListViewComponent component = DaggerProductListViewComponent.builder()
                .baseAppComponent(getComponent(BaseAppComponent.class))
                .searchContextModule(new SearchContextModule(getActivity()))
                .build();

        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);

        if(irisSession == null && container != null)
            irisSession = new IrisSession(container.getContext());

        return inflater.inflate(R.layout.search_result_product_fragment_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restoreInstanceState(savedInstanceState);
        initViews(view);
        addDefaultSelectedSort();

        if (presenter != null) {
            presenter.onViewCreated();
        }
    }

    private void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            copySearchParameter(savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER));
        }
    }

    private void initViews(@NonNull View view) {
        initRecyclerView(view);
        initLayoutManager();
        initSwipeToRefresh(view);
        initTopAdsConfig();
        initTopAdsParams();
        initAdapter();
        initLoadMoreListener();
        initSearchQuickSortFilter(view);
        initShimmeringView(view);

        setupRecyclerView();

        if (getUserVisibleHint()) {
            setupSearchNavigation();
        }
    }

    private void initRecyclerView(View rootView) {
        recyclerView = rootView.findViewById(R.id.recyclerview);
    }

    private void initLayoutManager() {
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
    }

    private void initSwipeToRefresh(View view) {
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(this::onSwipeToRefresh);
    }

    private void initTopAdsConfig() {
        if (getActivity() == null || getActivity().getApplicationContext() == null || presenter == null)
            return;

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

        if (getSearchParameter() != null) {
            getSearchParameter().cleanUpNullValuesInMap();
            adsParams.getParam().putAll(getSearchParameter().getSearchParameterHashMap());
        }

        topAdsConfig.setTopAdsParams(adsParams);
    }

    private void initAdapter() {
        ProductListTypeFactory productListTypeFactory = new ProductListTypeFactoryImpl(
                this, this,
                this, this, this,
                this, this,
                this, this, this,
                this, this, this,
                this, this,
                topAdsConfig);

        adapter = new ProductListAdapter(this, productListTypeFactory);
    }

    private void initLoadMoreListener() {
        staggeredGridLayoutLoadMoreTriggerListener = getEndlessRecyclerViewListener(getStaggeredGridLayoutManager());
    }

    private EndlessRecyclerViewScrollListener getEndlessRecyclerViewListener(RecyclerView.LayoutManager recyclerViewLayoutManager) {
        return new EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isAllowLoadMore()) {
                    presenter.loadMoreData(searchParameter.getSearchParameterMap());
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

    private void initSearchQuickSortFilter(View rootView) {
        searchSortFilter = rootView.findViewById(R.id.search_product_quick_sort_filter);
    }

    private void initShimmeringView(View view) {
        shimmeringView = view.findViewById(R.id.shimmeringView);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(createProductItemDecoration());
        recyclerView.addOnScrollListener(staggeredGridLayoutLoadMoreTriggerListener);
    }

    @NonNull
    private ProductItemDecoration createProductItemDecoration() {
        return new ProductItemDecoration(getContext().getResources().getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16));
    }

    @Override
    public boolean isFirstActiveTab() {
        return getActiveTab().equals(SearchConstant.ActiveTab.PRODUCT);
    }

    private String getActiveTab() {
        return searchParameter.get(SearchApiConst.ACTIVE_TAB);
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
        redirectionListener = castContextToRedirectionListener(context);
        searchPerformanceMonitoringListener = castContextToSearchPerformanceMonitoring(context);
    }

    private SearchNavigationListener castContextToSearchNavigationListener(Context context) {
        if (context instanceof SearchNavigationListener) {
            return (SearchNavigationListener) context;
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
        if (context instanceof SearchPerformanceMonitoringListener) {
            return (SearchPerformanceMonitoringListener) context;
        }

        return null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (presenter != null) presenter.onViewResumed();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (presenter != null) {
            presenter.onViewVisibilityChanged(isVisibleToUser, isAdded());
        }
    }

    @Override
    public void setupSearchNavigation() {
        if (searchNavigationListener == null) return;

        searchNavigationListener.setupSearchNavigation(this::switchLayoutType, true);
        refreshMenuItemGridIcon();
    }

    private void switchLayoutType() {
        if (!getUserVisibleHint() || adapter == null) {
            return;
        }

        switch (adapter.getCurrentLayoutType()) {
            case LIST:
                switchLayoutTypeTo(BIG_GRID);
                SearchTracking.eventSearchResultChangeGrid(getActivity(), "grid 1", getScreenName());
                break;
            case SMALL_GRID:
                switchLayoutTypeTo(LIST);
                SearchTracking.eventSearchResultChangeGrid(getActivity(), "list", getScreenName());
                break;
            case BIG_GRID:
                switchLayoutTypeTo(SMALL_GRID);
                SearchTracking.eventSearchResultChangeGrid(getActivity(), "grid 2", getScreenName());
                break;
        }
    }

    private void switchLayoutTypeTo(SearchConstant.ViewType layoutType) {
        if (!getUserVisibleHint() || adapter == null) {
            return;
        }

        switch (layoutType) {
            case LIST:
                staggeredGridLayoutManager.setSpanCount(1);
                adapter.changeListView();
                break;
            case SMALL_GRID:
                staggeredGridLayoutManager.setSpanCount(2);
                adapter.changeDoubleGridView();
                break;
            case BIG_GRID:
                staggeredGridLayoutManager.setSpanCount(1);
                adapter.changeSingleGridView();
                break;
        }

        refreshMenuItemGridIcon();
    }

    private void refreshMenuItemGridIcon() {
        if (searchNavigationListener == null || adapter == null) return;

        searchNavigationListener.refreshMenuItemGridIcon(adapter.getTitleTypeRecyclerView(), adapter.getIconTypeRecyclerView());
    }

    private FilterTrackingData getFilterTrackingData() {
        if (filterTrackingData == null) {
            filterTrackingData = new FilterTrackingData(
                    FilterEventTracking.Event.CLICK_SEARCH_RESULT,
                    FilterEventTracking.Category.FILTER_PRODUCT,
                    "",
                    FilterEventTracking.Category.PREFIX_SEARCH_RESULT_PAGE
            );
        }
        return filterTrackingData;
    }

    @Override
    public void trackScreenAuthenticated() {
        if (getUserVisibleHint()
                && getActivity() != null
                && getActivity().getApplicationContext() != null) {
            SearchTracking.screenTrackSearchSectionFragment(getScreenName());
        }
    }

    private StaggeredGridLayoutManager getStaggeredGridLayoutManager() {
        return staggeredGridLayoutManager;
    }

    @Override
    public void addProductList(List<Visitable> list) {
        adapter.appendItems(list);
    }

    public void setProductList(List<Visitable> list) {
        adapter.clearData();

        stopSearchResultPagePerformanceMonitoring();
        addProductList(list);
    }

    public void addRecommendationList(List<Visitable> list) {
        adapter.appendItems(list);
    }

    private void stopSearchResultPagePerformanceMonitoring() {
        recyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (searchPerformanceMonitoringListener != null) {
                            searchPerformanceMonitoringListener.stopRenderPerformanceMonitoring();
                            searchPerformanceMonitoringListener.stopPerformanceMonitoring();
                        }

                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    @Override
    public void sendProductImpressionTrackingEvent(ProductItemDataView item, String suggestedRelatedKeyword, String dimension90) {
        String userId = getUserId();
        String eventLabel = getSearchProductTrackingEventLabel(item, suggestedRelatedKeyword);
        List<Object> dataLayerList = new ArrayList<>();
        List<ProductItemDataView> productItemDataViews = new ArrayList<>();

        String filterSortParams = searchParameter == null ? "" :
                SearchFilterUtilsKt.getSortFilterParamsString(searchParameter.getSearchParameterMap());
        dataLayerList.add(item.getProductAsObjectDataLayer(userId, filterSortParams, dimension90));
        productItemDataViews.add(item);

        if(irisSession != null){
            SearchTracking.eventImpressionSearchResultProduct(trackingQueue, dataLayerList, eventLabel,
                    irisSession.getSessionId());
        }else {
            SearchTracking.eventImpressionSearchResultProduct(trackingQueue, dataLayerList, eventLabel, "");
        }
    }

    private String getSearchRef() {
        return searchParameter.get(SearchApiConst.SEARCH_REF);
    }

    private String getSearchProductTrackingEventLabel(ProductItemDataView item, String suggestedRelatedKeyword) {
        String keyword = suggestedRelatedKeyword.isEmpty() ? getQueryKey() : suggestedRelatedKeyword;
        return TextUtils.isEmpty(item.getPageTitle()) ? keyword : item.getPageTitle();
    }

    @Override
    public void showNetworkError(final int startRow) {
        if (adapter.isListEmpty()) {
            showNetworkErrorOnEmptyList();
        } else {
            showNetworkErrorOnLoadMore(startRow);
        }
    }

    private void showNetworkErrorOnEmptyList() {
        hideViewOnError();

        NetworkErrorHelper.showEmptyState(getActivity(), getView(), () -> {
            refreshLayout.setVisibility(View.VISIBLE);
            reloadData();
        });
    }

    private void hideViewOnError() {
        searchSortFilter.setVisibility(View.GONE);
        shimmeringView.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
    }

    private void showNetworkErrorOnLoadMore(int startRow) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), () -> {
            addLoading();
            presenter.loadMoreData(searchParameter.getSearchParameterMap());
        }).showRetrySnackbar();
    }

    @Override
    public String getScreenNameId() {
        return SCREEN_SEARCH_PAGE_PRODUCT_TAB;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
            ProductCardOptionsManager.handleProductCardOptionsActivityResult(
                    requestCode, resultCode, data,
                    this::handleWishlistAction, this::handleAddToCartAction, this::handleVisitShopAction, this::handleShareProductAction
            );
        }
    }

    private void updateWishlistFromPDP(int position, boolean isWishlist) {
        if (adapter != null && (adapter.isProductItem(position) || adapter.isRecommendationItem(position))) {
            adapter.updateWishlistStatus(position, isWishlist);
        }
    }

    private void handleWishlistAction(ProductCardOptionsModel productCardOptionsModel) {
        presenter.handleWishlistAction(productCardOptionsModel);
    }

    private void handleAddToCartAction(ProductCardOptionsModel productCardOptionsModel) {
        presenter.handleAddToCartAction(productCardOptionsModel);
    }

    @Override
    public void trackSuccessAddToCartEvent(boolean isAds, Object addToCartDataLayer) {
        SearchTracking.trackEventAddToCart(getQueryKey(), isAds, addToCartDataLayer);
    }

    @Override
    public void showAddToCartSuccessMessage() {
        if (getView() == null) return;
        Toaster.make(getView(), getString(R.string.search_add_to_cart_success), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL);
    }

    @Override
    public void showAddToCartFailedMessage(String errorMessage) {
        if (getView() == null) return;
        Toaster.make(getView(), errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR);
    }

    private void handleVisitShopAction(ProductCardOptionsModel productCardOptionsModel) {
        if (presenter != null) presenter.handleVisitShopAction();
    }

    @Override
    public void routeToShopPage(String shopId) {
        if (getContext() != null) RouteManager.route(getContext(), ApplinkConst.SHOP, shopId);
    }

    @Override
    public void trackEventGoToShopPage(Object dataLayer) {
        SearchTracking.trackEventGoToShopPage(getQueryKey(), dataLayer);
    }

    private void handleShareProductAction(ProductCardOptionsModel productCardOptionsModel) {
        SearchTracking.trackEventShareProduct(getQueryKey(), productCardOptionsModel.getProductId());
    }

    @Override
    public void onPause() {
        super.onPause();
        if(irisSession == null) {
            TopAdsGtmTracker.getInstance().eventSearchResultProductView(trackingQueue, getQueryKey(), SCREEN_SEARCH_PAGE_PRODUCT_TAB, "");
        }else{
            TopAdsGtmTracker.getInstance().eventSearchResultProductView(trackingQueue, getQueryKey(), SCREEN_SEARCH_PAGE_PRODUCT_TAB, irisSession.getSessionId());
        }
        trackingQueue.sendAll();
    }

    @Override
    public void onProductImpressed(ProductItemDataView item, int adapterPosition) {
        if (presenter == null) return;

        presenter.onProductImpressed(item, adapterPosition);
    }

    @Override
    public void sendTopAdsGTMTrackingProductImpression(ProductItemDataView item) {
        Product product = createTopAdsProductForTracking(item);

        TopAdsGtmTracker.getInstance().addSearchResultProductViewImpressions(product, item.getPosition());
    }

    private Product createTopAdsProductForTracking(ProductItemDataView item) {
        Product product = new Product();
        product.setId(item.getProductID());
        product.setName(item.getProductName());
        product.setPriceFormat(item.getPrice());
        product.setCategory(new Category(item.getCategoryID()));
        product.setFreeOngkir(createTopAdsProductFreeOngkirForTracking(item));
        product.setCategoryBreadcrumb(item.getCategoryBreadcrumb());

        return product;
    }

    private FreeOngkir createTopAdsProductFreeOngkirForTracking(ProductItemDataView item) {
        if (item != null && item.getFreeOngkirDataView() != null) {
            return new FreeOngkir(
                    item.getFreeOngkirDataView().isActive(),
                    item.getFreeOngkirDataView().getImageUrl()
            );
        }

        return null;
    }

    @Override
    public void onGlobalNavWidgetClicked(GlobalNavDataView.Item item, String keyword) {
        redirectionStartActivity(item.getApplink(), item.getUrl());

        SearchTracking.trackEventClickGlobalNavWidgetItem(item.getGlobalNavItemAsObjectDataLayer(item.getName()),
                keyword, item.getName(), item.getApplink());
    }

    @Override
    public void onGlobalNavWidgetClickSeeAll(GlobalNavDataView model) {
        redirectionStartActivity(model.getSeeAllApplink(), model.getSeeAllUrl());

        SearchTracking.eventUserClickSeeAllGlobalNavWidget(model.getKeyword(),
                model.getTitle(), model.getSeeAllApplink());
    }

    @Override
    public void redirectionStartActivity(String applink, String url) {
        if (redirectionListener == null) return;

        if (!TextUtils.isEmpty(applink)) {
            redirectionListener.startActivityWithApplink(SearchKotlinExtKt.decodeQueryParameter(applink));
        } else {
            redirectionListener.startActivityWithUrl(url);
        }
    }

    @Override
    public void onItemClicked(ProductItemDataView item, int adapterPosition) {
        if (presenter == null) return;

        presenter.onProductClick(item, adapterPosition);
    }

    @Override
    public void sendTopAdsGTMTrackingProductClick(ProductItemDataView item) {
        Product product = createTopAdsProductForTracking(item);

        TopAdsGtmTracker.eventSearchResultProductClick(getContext(), getQueryKey(), product, item.getPosition(), SCREEN_SEARCH_PAGE_PRODUCT_TAB);
    }

    @Override
    public void sendGTMTrackingProductClick(ProductItemDataView item, String userId, String suggestedRelatedKeyword, String dimension90) {
        String eventLabel = getSearchProductTrackingEventLabel(item, suggestedRelatedKeyword);
        String filterSortParams = searchParameter == null ? "" :
                SearchFilterUtilsKt.getSortFilterParamsString(searchParameter.getSearchParameterMap());

        SearchTracking.trackEventClickSearchResultProduct(
                item.getProductAsObjectDataLayer(userId, filterSortParams, dimension90),
                item.isOrganicAds(),
                eventLabel,
                filterSortParams
        );
    }

    @Override
    public void routeToProductDetail(ProductItemDataView item, int adapterPosition) {
        Intent intent = getProductIntent(item.getProductID(), item.getWarehouseID());

        if (intent != null) {
            intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition);
            startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
        }
    }

    private Intent getProductIntent(String productId, String warehouseId) {
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
    public void onProductClick(@NotNull RecommendationItem item, @org.jetbrains.annotations.Nullable String layoutType, @NotNull int... position) {
        Intent intent = getProductIntent(String.valueOf(item.getProductId()), "0");

        if (intent != null) {
            intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, item.getPosition());
            if (presenter.isUserLoggedIn()) {
                RecommendationTracking.Companion.eventClickProductRecommendationLogin(item, String.valueOf(item.getPosition()));
            } else {
                RecommendationTracking.Companion.eventClickProductRecommendationNonLogin(item, String.valueOf(item.getPosition()));
            }
            startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
        }
    }

    @Override
    public void onProductImpression(@NotNull RecommendationItem item) {
        if (presenter.isUserLoggedIn()) {
            RecommendationTracking.Companion.eventImpressionProductRecommendationLogin(trackingQueue, item, String.valueOf(item.getPosition()));
        } else {
            RecommendationTracking.Companion.eventImpressionProductRecommendationNonLogin(trackingQueue, item, String.valueOf(item.getPosition()));
        }
    }

    public void onWishlistClick(@NotNull RecommendationItem item, boolean isAddWishlist, @NotNull Function2<? super Boolean, ? super Throwable, Unit> callback) {
        // Unused
    }

    @Override
    public void onThreeDotsClick(@NotNull RecommendationItem item, @NotNull int... position) {
        if (getSearchParameter() == null || getActivity() == null) return;

        ProductCardOptionsManager.showProductCardOptions(this, createProductCardOptionsModel(item));
    }

    private ProductCardOptionsModel createProductCardOptionsModel(RecommendationItem item) {
        ProductCardOptionsModel productCardOptionsModel = new ProductCardOptionsModel();

        productCardOptionsModel.setHasWishlist(true);
        productCardOptionsModel.setWishlisted(item.isWishlist());
        productCardOptionsModel.setKeyword(getSearchParameter().getSearchQuery());
        productCardOptionsModel.setProductId(String.valueOf(item.getProductId()));
        productCardOptionsModel.setTopAds(item.isTopAds());
        productCardOptionsModel.setTopAdsWishlistUrl(item.getWishlistUrl());
        productCardOptionsModel.setRecommendation(true);

        return productCardOptionsModel;
    }

    @Override
    public void onThreeDotsClick(ProductItemDataView item, int adapterPosition) {
        if (getSearchParameter() == null || getActivity() == null || presenter == null) return;

        presenter.onThreeDotsClick(item, adapterPosition);
    }

    @Override
    public void trackEventLongPress(String productID) {
        SearchTracking.trackEventProductLongPress(getSearchParameter().getSearchQuery(), productID);
    }

    @Override
    public void showProductCardOptions(ProductCardOptionsModel productCardOptionsModel) {
        ProductCardOptionsManager.showProductCardOptions(this, productCardOptionsModel);
    }

    @Override
    public void onTickerClicked(TickerDataView tickerDataView) {
        SearchTracking.trackEventClickTicker(getQueryKey(), tickerDataView.getTypeId());
        applyParamsFromTicker(UrlParamUtils.getParamMap(tickerDataView.getQuery()));
    }

    private void applyParamsFromTicker(HashMap<String, String> tickerParams) {
        HashMap<String, String> params = new HashMap<>(filterController.getParameter());
        params.putAll(tickerParams);
        refreshSearchParameter(params);
        refreshFilterController(params);
        reloadData();
    }

    public void refreshSearchParameter(Map<String, String> queryParams) {
        if (searchParameter == null) return;

        this.searchParameter.getSearchParameterHashMap().clear();
        this.searchParameter.getSearchParameterHashMap().putAll(queryParams);
        this.searchParameter.getSearchParameterHashMap().put(SearchApiConst.ORIGIN_FILTER,
                SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE);
    }

    private void refreshFilterController(HashMap<String, String> queryParams) {
        if (filterController == null) return;

        HashMap<String, String> params = new HashMap<>(queryParams);
        params.put(SearchApiConst.ORIGIN_FILTER,
                SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE);

        filterController.refreshMapParameter(params);
    }

    @Override
    public void onTickerDismissed() {
        if (presenter != null && adapter != null) {
            presenter.onPriceFilterTickerDismissed();
            adapter.removePriceFilterTicker();
        }
    }

    @Override
    public boolean isTickerHasDismissed() {
        return presenter != null && presenter.getIsTickerHasDismissed();
    }

    @Override
    public void onSuggestionClicked(SuggestionDataView suggestionDataView) {
        SearchTracking.eventClickSuggestedSearch(getQueryKey(), suggestionDataView.getSuggestion());
        performNewProductSearch(suggestionDataView.getSuggestedQuery());
    }

    private void performNewProductSearch(String queryParams) {
        String applinkToSearchResult = ApplinkConstInternalDiscovery.SEARCH_RESULT + "?" + queryParams;
        String modifiedApplinkToSearchResult = modifyApplinkToSearchResult(applinkToSearchResult);

        redirectionListener.startActivityWithApplink(modifiedApplinkToSearchResult);
    }

    private String modifyApplinkToSearchResult(String applink) {
        URLParser urlParser = new URLParser(applink);

        Map<String, String> params = urlParser.getParamKeyValueMap();
        params.put(PREVIOUS_KEYWORD, getQueryKey());

        return ApplinkConstInternalDiscovery.SEARCH_RESULT + "?" + UrlParamUtils.generateUrlParamString(params);
    }

    @Override
    public boolean isQuickFilterSelected(Option option) {
        if (filterController == null) return false;

        return filterController.getFilterViewState(option.getUniqueId());
    }

    @Override
    public void onQuickFilterSelected(Option option) {
        if (filterController == null) return;

        boolean isQuickFilterSelectedReversed = !isQuickFilterSelected(option);

        setFilterToQuickFilterController(option, isQuickFilterSelectedReversed);

        Map<String, String> queryParams = filterController.getParameter();
        refreshSearchParameter(queryParams);
        refreshFilterController(new HashMap<>(queryParams));

        reloadData();

        trackEventSearchResultQuickFilter(option.getKey(), option.getValue(), isQuickFilterSelectedReversed);
    }

    private void setFilterToQuickFilterController(Option option, boolean isQuickFilterSelected) {
        if (filterController == null) return;

        if (option.isCategoryOption()) {
            filterController.setFilter(option, isQuickFilterSelected, true);
        } else {
            filterController.setFilter(option, isQuickFilterSelected);
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
        if (filterController == null) return;

        Option option = OptionHelper.generateOptionFromUniqueId(uniqueId);

        removeFilterFromFilterController(option);
        refreshSearchParameter(filterController.getParameter());
        refreshFilterController(new HashMap<>(filterController.getParameter()));
        reloadData();
    }

    private void removeFilterFromFilterController(Option option) {
        if (filterController == null) return;

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
        SearchTracking.eventUserClickNewSearchOnEmptySearchProduct(getQueryKey());
        showSearchInputView();
    }

    private void showSearchInputView() {
        redirectionListener.showSearchInputView();
    }

    @Override
    public List<Option> getSelectedFilterAsOptionList() {
        return getOptionListFromFilterController();
    }

    @Override
    public void onEmptySearchToGlobalSearchClicked(String applink) {
        if (redirectionListener == null) return;

        redirectionListener.startActivityWithApplink(applink);
    }

    private List<Option> getOptionListFromFilterController() {
        if (filterController == null) return new ArrayList<>();

        return OptionHelper.combinePriceFilterIfExists(filterController.getActiveFilterOptionList(),
                getResources().getString(R.string.empty_state_selected_filter_price_name));
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
    public String getQueryKey() {
        return searchParameter == null ? "" : searchParameter.getSearchQuery();
    }

    @Override
    public String getPreviousKeyword() {
        return searchParameter == null ? "" : searchParameter.get(PREVIOUS_KEYWORD);
    }

    @Override
    public void setEmptyProduct(GlobalNavDataView globalNavDataView, EmptySearchProductDataView emptySearchProductDataView) {
        adapter.showEmptyState(globalNavDataView, emptySearchProductDataView);
    }

    private void setSortFilterIndicatorCounter() {
        searchSortFilter.setIndicatorCounter(SearchFilterUtilsKt.getSortFilterCount(searchParameter.getSearchParameterMap()));
    }

    private void addDefaultSelectedSort() {
        if (searchParameter != null && searchParameter.get(SearchApiConst.OB).isEmpty()) {
            searchParameter.set(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT);
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
        } else {
            SearchTracking.trackEventImpressionBannedProductsWithResult(getQueryKey());
        }
    }

    @Override
    public void trackEventImpressionTicker(int typeId) {
        SearchTracking.trackEventImpressionTicker(getQueryKey(), typeId);
    }

    @Override
    public void reloadData() {
        if (adapter == null || getSearchParameter() == null) {
            return;
        }

        showRefreshLayout();
        presenter.clearData();
        adapter.clearData();
        hideSearchSortFilter();
        initTopAdsParams();
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_PRODUCT_TRACE);
        presenter.loadData(getSearchParameter().getSearchParameterMap());
        TopAdsGtmTracker.getInstance().clearDataLayerList();
        setSortFilterIndicatorCounter();
    }

    private void hideSearchSortFilter() {
        if (searchSortFilter != null) searchSortFilter.setVisibility(View.GONE);
        if (shimmeringView != null) shimmeringView.setVisibility(View.VISIBLE);
    }

    private void onSwipeToRefresh() {
        reloadData();
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

    @Override
    public void backToTop() {
        smoothScrollRecyclerView();
    }

    private void smoothScrollRecyclerView() {
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
    public void sendImpressionGlobalNav(GlobalNavDataView globalNavDataView) {
        List<Object> dataLayerList = new ArrayList<>();
        for (GlobalNavDataView.Item item : globalNavDataView.getItemList()) {
            dataLayerList.add(item.getGlobalNavItemAsObjectDataLayer(item.getApplink()));
        }
        SearchTracking.trackEventImpressionGlobalNavWidgetItem(trackingQueue, dataLayerList, globalNavDataView.getKeyword());
    }

    @Override
    public boolean isAnyFilterActive() {
        if (filterController == null) return false;

        return filterController.isFilterActive();
    }

    @Override
    public boolean isAnySortActive() {
        if (searchParameter == null) return false;

        return !SearchFilterUtilsKt.isSortHasDefaultValue(searchParameter.getSearchParameterMap());
    }

    @Override
    public void clearLastProductItemPositionFromCache() {
        if (getActivity() == null || getActivity().getApplicationContext() == null) return;
        LocalCacheHandler.clearCache(getActivity().getApplicationContext(), SEARCH_RESULT_ENHANCE_ANALYTIC);
    }

    @Override
    public void saveLastProductItemPositionToCache(int lastProductItemPositionToCache) {
        if (getActivity() == null || getActivity().getApplicationContext() == null) return;

        LocalCacheHandler cache = new LocalCacheHandler(getActivity().getApplicationContext(), SEARCH_RESULT_ENHANCE_ANALYTIC);
        cache.putInt(LAST_POSITION_ENHANCE_PRODUCT, lastProductItemPositionToCache);
        cache.applyEditor();
    }

    @Override
    public int getLastProductItemPositionFromCache() {
        if (getActivity() == null || getActivity().getApplicationContext() == null) return 0;

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
    public void redirectSearchToAnotherPage(String applink) {
        redirectionListener.startActivityWithApplink(applink);
        finishActivity();
    }

    @Override
    public void sendTrackingForNoResult(String resultCode, String alternativeKeyword, String keywordProcess) {
        Map<String, String> mapParameter = searchParameter == null ? new HashMap<>() :
                SearchFilterUtilsKt.getFilterParams(searchParameter.getSearchParameterHashMap());

        SearchTracking.eventSearchNoResult(getQueryKey(), getScreenName(), mapParameter, alternativeKeyword, resultCode, keywordProcess);
    }

    private void finishActivity() {
        if (getActivity() != null) {
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
    public void onBannerAdsClicked(int position, String applink, CpmData cpmData) {
        if (getActivity() == null || redirectionListener == null) return;

        redirectionListener.startActivityWithApplink(applink);

        trackBannerAdsClicked(position, applink, cpmData);
    }

    private void trackBannerAdsClicked(int position, String applink, CpmData data) {
        if (applink.contains(SHOP)) {
            TopAdsGtmTracker.eventTopAdsHeadlineShopClick(position, getQueryKey(), data, getUserId());
            TopAdsGtmTracker.eventSearchResultPromoShopClick(getActivity(), data, position);
        } else {
            TopAdsGtmTracker.eventTopAdsHeadlineProductClick(position, getQueryKey(), data, getUserId());
            TopAdsGtmTracker.eventSearchResultPromoProductClick(getActivity(), data, position);
        }
    }

    @Override
    public void onBannerAdsImpressionListener(int position, CpmData data) {
        TopAdsGtmTracker.eventTopAdsHeadlineShopView(position, data, getQueryKey(), getUserId());
        TopAdsGtmTracker.eventSearchResultPromoView(getActivity(), data, position);
    }

    public String getRegistrationId() {
        return presenter != null ? presenter.getDeviceId() : "";
    }

    @Override
    public void onInspirationCarouselListProductClicked(@NotNull InspirationCarouselDataView.Option.Product product) {
        redirectionStartActivity(product.getApplink(), product.getUrl());

        List<Object> products = new ArrayList<>();
        products.add(product.getInspirationCarouselListProductAsObjectDataLayer());
        SearchTracking.trackEventClickInspirationCarouselListProduct(product.getInspirationCarouselType(), getQueryKey(), products);
    }
    
    @Override
    public void onInspirationCarouselInfoProductClicked(@NotNull InspirationCarouselDataView.Option.Product product) {
        redirectionStartActivity(product.getApplink(), product.getUrl());

        List<Object> products = new ArrayList<>();
        products.add(product.getInspirationCarouselInfoProductAsObjectDataLayer());
        SearchTracking.trackEventClickInspirationCarouselInfoProduct(product.getInspirationCarouselType(), getQueryKey(), products);
    }

    @Override
    public void onInspirationCarouselSeeAllClicked(@NotNull InspirationCarouselDataView.Option option) {
        redirectionStartActivity(option.getApplink(), option.getUrl());

        String keywordBefore = getQueryKey();
        Uri applink = Uri.parse(option.getApplink());
        String keywordAfter = applink.getQueryParameter(SearchApiConst.Q);
        SearchTracking.trackEventClickInspirationCarouselOptionSeeAll(option.getInspirationCarouselType(), keywordBefore, keywordAfter);
    }

    @Override
    public void onInspirationCarouselGridProductClicked(@NotNull InspirationCarouselDataView.Option.Product product) {
        redirectionStartActivity(product.getApplink(), product.getUrl());

        List<Object> products = new ArrayList<>();
        products.add(product.getInspirationCarouselListProductAsObjectDataLayer());
        SearchTracking.trackEventClickInspirationCarouselListProduct(product.getInspirationCarouselType(), getQueryKey(), products);
    }

    @Override
    public void onInspirationCarouselGridBannerClicked(@NotNull InspirationCarouselDataView.Option option) {
        redirectionStartActivity(option.getBannerApplinkUrl(), option.getBannerLinkUrl());

        SearchTracking.trackEventClickInspirationCarouselGridBanner(
                option.getInspirationCarouselType(), getQueryKey(), option.getBannerDataLayer(getQueryKey()), getUserId()
        );
    }

    @Override
    public void onImpressedInspirationCarouselInfoProduct(InspirationCarouselDataView.Option.Product product) {
        if (product == null) return;

        List<Object> products = new ArrayList<>();
        products.add(product.getInspirationCarouselInfoProductAsObjectDataLayer());

        SearchTracking.trackImpressionInspirationCarouselInfo(trackingQueue, product.getInspirationCarouselType(), getQueryKey(), products);
    }

    @Override
    public void onImpressedInspirationCarouselListProduct(InspirationCarouselDataView.Option.Product product) {
        if (product == null) return;

        List<Object> products = new ArrayList<>();
        products.add(product.getInspirationCarouselListProductImpressionAsObjectDataLayer());

        SearchTracking.trackImpressionInspirationCarouselList(trackingQueue, product.getInspirationCarouselType(), getQueryKey(), products);
    }

    @Override
    public void onImpressedInspirationCarouselGridProduct(@NotNull InspirationCarouselDataView.Option.Product product) {
        List<Object> products = new ArrayList<>();
        products.add(product.getInspirationCarouselListProductImpressionAsObjectDataLayer());

        SearchTracking.trackImpressionInspirationCarouselList(trackingQueue, product.getInspirationCarouselType(), getQueryKey(), products);
    }

    @Override
    public void onInspirationCarouselChipsProductClicked(@NotNull InspirationCarouselDataView.Option.Product product) {
        redirectionStartActivity(product.getApplink(), product.getUrl());

        String filterSortParams = searchParameter == null ? "" :
                SearchFilterUtilsKt.getSortFilterParamsString(searchParameter.getSearchParameterMap());

        List<Object> products = new ArrayList<>();
        products.add(product.getInspirationCarouselChipsProductAsObjectDataLayer(filterSortParams));
        SearchTracking.trackEventClickInspirationCarouselChipsProduct(
                product.getInspirationCarouselType(),
                getQueryKey(),
                product.getOptionTitle(),
                getUserId(),
                products
        );
    }

    @Override
    public void onImpressedInspirationCarouselChipsProduct(@NotNull InspirationCarouselDataView.Option.Product product) {
        String filterSortParams = searchParameter == null ? "" :
                SearchFilterUtilsKt.getSortFilterParamsString(searchParameter.getSearchParameterMap());

        List<Object> products = new ArrayList<>();
        products.add(product.getInspirationCarouselChipsProductAsObjectDataLayer(filterSortParams));

        SearchTracking.trackImpressionInspirationCarouselChips(
                trackingQueue,
                product.getInspirationCarouselType(),
                getQueryKey(),
                product.getOptionTitle(),
                getUserId(),
                products
        );
    }

    @Override
    public void onInspirationCarouselChipsSeeAllClicked(@NotNull InspirationCarouselDataView.Option option) {
        redirectionStartActivity(option.getApplink(), option.getUrl());

        SearchTracking.trackEventClickInspirationCarouselChipsSeeAll(
                option.getInspirationCarouselType(),
                getQueryKey(),
                option.getTitle(),
                getUserId()
        );
    }

    @Override
    public void onInspirationCarouselChipsClicked(
            int inspirationCarouselAdapterPosition,
            @NotNull InspirationCarouselDataView inspirationCarouselViewModel,
            @NotNull InspirationCarouselDataView.Option inspirationCarouselOption
    ) {
        if (presenter == null) return;

        presenter.onInspirationCarouselChipsClick(
                inspirationCarouselAdapterPosition,
                inspirationCarouselViewModel,
                inspirationCarouselOption,
                getSearchParameter().getSearchParameterMap()
        );
    }

    @Override
    public void trackInspirationCarouselChipsClicked(@NotNull InspirationCarouselDataView.Option option) {
        SearchTracking.trackEventClickInspirationCarouselChipsVariant(
                option.getInspirationCarouselType(),
                getQueryKey(),
                option.getTitle(),
                getUserId()
        );
    }

    @Override
    public RemoteConfig getABTestRemoteConfig() {
        return RemoteConfigInstance.getInstance().getABTestPlatform();
    }

    @Override
    public void trackWishlistRecommendationProductLoginUser(boolean isAddWishlist) {
        RecommendationTracking.Companion.eventUserClickProductToWishlistForUserLogin(isAddWishlist);
    }

    @Override
    public void trackWishlistRecommendationProductNonLoginUser() {
        RecommendationTracking.Companion.eventUserClickProductToWishlistForNonLogin();
    }

    @Override
    public void trackWishlistProduct(WishlistTrackingModel wishlistTrackingModel) {
        SearchTracking.eventSuccessWishlistSearchResultProduct(wishlistTrackingModel);
    }

    @Override
    public void updateWishlistStatus(String productId, boolean isWishlisted) {
        adapter.updateWishlistStatus(productId, isWishlisted);
    }

    @Override
    public void showMessageSuccessWishlistAction(boolean isWishlisted) {
        if (getView() == null) return;

        if (isWishlisted) showToastSuccess(
                getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist),
                getString(com.tokopedia.wishlist.common.R.string.lihat_label),
                (view) -> goToWishlist());
        else Toaster.build(getView(), getString(R.string.msg_remove_wishlist), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show();
    }

    private void showToastSuccess(String message, String ctaText, View.OnClickListener ctaAction) {
        if (getView() != null) {
            Toaster.build(getView(),
                    message,
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    ctaText,
                    ctaAction).show();
        }
    }

    private void goToWishlist() {
        if (getContext() != null) {
            RouteManager.route(getContext(), ApplinkConsInternalHome.HOME_WISHLIST);
        }
    }

    @Override
    public void showMessageFailedWishlistAction(boolean isWishlisted) {
        if (getView() == null) return;

        if (isWishlisted)
            Toaster.build(getView(), getString(R.string.msg_add_wishlist_failed), Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show();
        else Toaster.build(getView(), getString(R.string.msg_remove_wishlist_failed), Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show();
    }

    @Override
    public boolean isLandingPage() {
        return searchParameter.getBoolean(SearchApiConst.LANDING_PAGE);
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle savedInstanceState) {
        savedInstanceState.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter);
    }

    @Override
    public void logWarning(String message, @Nullable Throwable throwable) {
        new SearchLogger().logWarning(message, throwable);
    }

    @Override
    public void stopPreparePagePerformanceMonitoring() {
        if (searchPerformanceMonitoringListener != null) {
            searchPerformanceMonitoringListener.stopPreparePagePerformanceMonitoring();
        }
    }

    @Override
    public void startNetworkRequestPerformanceMonitoring() {
        if (searchPerformanceMonitoringListener != null) {
            searchPerformanceMonitoringListener.startNetworkRequestPerformanceMonitoring();
        }
    }

    @Override
    public void stopNetworkRequestPerformanceMonitoring() {
        if (searchPerformanceMonitoringListener != null) {
            searchPerformanceMonitoringListener.stopNetworkRequestPerformanceMonitoring();
        }
    }

    @Override
    public void startRenderPerformanceMonitoring() {
        if (searchPerformanceMonitoringListener != null) {
            searchPerformanceMonitoringListener.startRenderPerformanceMonitoring();
        }
    }

    @Override
    public void onBroadMatchItemImpressed(@NotNull BroadMatchItemDataView broadMatchItemDataView) {
        if (presenter == null) return;

        presenter.onBroadMatchItemImpressed(broadMatchItemDataView);
    }

    @Override
    public void onBroadMatchItemClicked(@NotNull BroadMatchItemDataView broadMatchItemDataView) {
        if (presenter == null) return;

        presenter.onBroadMatchItemClick(broadMatchItemDataView);
    }

    @Override
    public void trackEventClickBroadMatchItem(@NotNull BroadMatchItemDataView broadMatchItemDataView) {
        List<Object> broadMatchItem = new ArrayList<>();
        broadMatchItem.add(broadMatchItemDataView.asClickObjectDataLayer());

        SearchTracking.trackEventClickBroadMatchItem(
                getQueryKey(), broadMatchItemDataView.getAlternativeKeyword(), getUserId(), broadMatchItem
        );
    }

    @Override
    public void onBroadMatchSeeMoreClicked(@NotNull BroadMatchDataView broadMatchDataView) {
        SearchTracking.trackEventClickBroadMatchSeeMore(getQueryKey(), broadMatchDataView.getKeyword());

        String applink = (broadMatchDataView.getApplink().startsWith(ApplinkConst.DISCOVERY_SEARCH)) ?
            modifyApplinkToSearchResult(broadMatchDataView.getApplink()) : broadMatchDataView.getApplink();

        redirectionStartActivity(applink, broadMatchDataView.getUrl());
    }

    @Override
    public void onBroadMatchThreeDotsClicked(@NotNull BroadMatchItemDataView broadMatchItemDataView) {
        ProductCardOptionsManager.showProductCardOptions(this, createProductCardOptionsModel(broadMatchItemDataView));
    }

    private ProductCardOptionsModel createProductCardOptionsModel(BroadMatchItemDataView item) {
        ProductCardOptionsModel productCardOptionsModel = new ProductCardOptionsModel();

        productCardOptionsModel.setHasWishlist(true);
        productCardOptionsModel.setHasSimilarSearch(true);
        productCardOptionsModel.setWishlisted(item.isWishlisted());
        productCardOptionsModel.setKeyword(getSearchParameter().getSearchQuery());
        productCardOptionsModel.setProductId(item.getId());
        productCardOptionsModel.setScreenName(SearchEventTracking.Category.SEARCH_RESULT);
        productCardOptionsModel.setSeeSimilarProductEvent(SearchTracking.EVENT_CLICK_SEARCH_RESULT);
        productCardOptionsModel.setTopAds(item.isOrganicAds());
        productCardOptionsModel.setTopAdsWishlistUrl(item.getTopAdsWishlistUrl());

        return productCardOptionsModel;
    }

    @Override
    public void trackBroadMatchImpression(BroadMatchItemDataView broadMatchItemDataView) {
        List<Object> broadMatchItemAsObjectDataLayer = new ArrayList<>();
        broadMatchItemAsObjectDataLayer.add(broadMatchItemDataView.asImpressionObjectDataLayer());

        SearchTracking.trackEventImpressionBroadMatch(trackingQueue, getQueryKey(), broadMatchItemDataView.getAlternativeKeyword(), getUserId(), broadMatchItemAsObjectDataLayer);
    }

    @Override
    public void onInspirationCardOptionClicked(@NotNull InspirationCardOptionDataView option) {
        trackEventClickInspirationCardOption(option);

        redirectionStartActivity(option.getApplink(), option.getUrl());
    }

    private void trackEventClickInspirationCardOption(InspirationCardOptionDataView option) {
        String label = option.getInspirationCardType() + " - " + getQueryKey() + " - " + option.getText();
        SearchTracking.trackEventClickInspirationCardOption(label);
    }

    @Override
    public void initFilterControllerForQuickFilter(List<Filter> quickFilterList) {
        filterController.initFilterController(searchParameter.getSearchParameterHashMap(), quickFilterList);
    }

    @Override
    public void hideQuickFilterShimmering() {
        shimmeringView.setVisibility(View.GONE);
    }

    @Override
    public void setQuickFilter(List<SortFilterItem> items) {
        searchSortFilter.getSortFilterItems().removeAllViews();
        searchSortFilter.setVisibility(View.VISIBLE);
        searchSortFilter.getSortFilterHorizontalScrollView().setScrollX(0);
        searchSortFilter.addItem((ArrayList<SortFilterItem>) items);
        if (searchSortFilter.getTextView() != null) searchSortFilter.getTextView().setText(getString(R.string.search_filter));
        searchSortFilter.setParentListener(this::openBottomSheetFilterRevamp);
        setSortFilterNewNotification(items);
    }

    private void setSortFilterNewNotification(List<SortFilterItem> items) {
        if (presenter == null) return;

        List<Option> quickFilterOptionList = presenter.getQuickFilterOptionList();

        for(int i = 0; i < items.size(); i++) {
            if (i >= quickFilterOptionList.size()) break;

            SortFilterItem item = items.get(i);
            Option quickFilterOption = quickFilterOptionList.get(i);

            sortFilterItemShowNew(item, quickFilterOption.isNew());
        }
    }

    private void sortFilterItemShowNew(SortFilterItem item, boolean isNew) {
        if (item.refChipUnify != null) item.refChipUnify.setShowNewNotification(isNew);
    }

    @Override
    public void showOnBoarding(int firstProductPositionWithBOELabel) {
        if (recyclerView == null || getContext() == null) return;

        View productWithBOELabel = getFirstProductWithBOELabel(firstProductPositionWithBOELabel);

        recyclerView.postDelayed(() -> {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                buildCoachMark(productWithBOELabel);
            }
            else {
                buildCoachMark2(productWithBOELabel);
            }
        }, 200);
    }

    private View getFirstProductWithBOELabel(int firstProductPositionWithBOELabel) {
        if (recyclerView == null) return null;

        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(firstProductPositionWithBOELabel);
        if (viewHolder == null) return null;

        if (viewHolder.itemView instanceof IProductCardView)
            return viewHolder.itemView;
        else
            return null;
    }

    private void buildCoachMark(View view) {
        ArrayList<CoachMarkItem> coachMarkItemList = createCoachMarkItemList(view);

        if (coachMarkItemList.size() <= 0) return;

        CoachMarkBuilder builder = new CoachMarkBuilder();
        builder.allowPreviousButton(false);
        builder.build().show(getActivity(), SEARCH_RESULT_PRODUCT_ONBOARDING_TAG, coachMarkItemList);
    }

    private ArrayList<CoachMarkItem> createCoachMarkItemList(@Nullable View boeLabelProductCard) {
        ArrayList<CoachMarkItem> coachMarkItemList = new ArrayList<>();

        if (boeLabelProductCard != null) coachMarkItemList.add(createBOELabelCoachMarkItem(boeLabelProductCard));

        return coachMarkItemList;
    }

    private CoachMarkItem createBOELabelCoachMarkItem(View boeLabelProductCard) {
        return new CoachMarkItem(
                boeLabelProductCard,
                getString(R.string.search_product_boe_label_onboarding_title),
                getString(R.string.search_product_boe_label_onboarding_description)
        );
    }

    private void buildCoachMark2(View view) {
        ArrayList<CoachMark2Item> coachMark2ItemList = createCoachMark2ItemList(view);

        if (coachMark2ItemList.size() <= 0) return;

        CoachMark2 coachMark = new CoachMark2(getContext());
        coachMark.showCoachMark(coachMark2ItemList, null, 0);
    }

    private ArrayList<CoachMark2Item> createCoachMark2ItemList(@Nullable View boeLabelProductCard) {
        ArrayList<CoachMark2Item> coachMarkItemList = new ArrayList<>();

        if (boeLabelProductCard != null) coachMarkItemList.add(createBOELabelCoachMark2Item(boeLabelProductCard));

        return coachMarkItemList;
    }

    private CoachMark2Item createBOELabelCoachMark2Item(View boeLabelProductCard) {
        return new CoachMark2Item(
                boeLabelProductCard,
                getString(R.string.search_product_boe_label_onboarding_title),
                getString(R.string.search_product_boe_label_onboarding_description),
                CoachMark2.POSITION_TOP
        );
    }

    private Unit openBottomSheetFilterRevamp() {
        if (presenter == null) return Unit.INSTANCE;

        presenter.openFilterPage(getSearchParameter().getSearchParameterMap());

        return Unit.INSTANCE;
    }

    @Override
    public void sendTrackingOpenFilterPage() {
        FilterTracking.eventOpenFilterPage(getFilterTrackingData());
    }

    @Override
    public void openBottomSheetFilter(@Nullable DynamicFilterModel dynamicFilterModel) {
        if (getFragmentManager() == null || searchParameter == null) return;

        sortFilterBottomSheet = new SortFilterBottomSheet();
        sortFilterBottomSheet.show(
                requireFragmentManager(),
                searchParameter.getSearchParameterHashMap(),
                dynamicFilterModel,
                this
        );

        sortFilterBottomSheet.setOnDismissListener(() -> {
            sortFilterBottomSheet = null;
            presenter.onBottomSheetFilterDismissed();
            return Unit.INSTANCE;
        });
    }

    @Override
    public void setDynamicFilter(@NotNull DynamicFilterModel dynamicFilterModel) {
        if (searchParameter != null) {
            filterController.appendFilterList(searchParameter.getSearchParameterHashMap(), dynamicFilterModel.getData().getFilter());
        }

        if (sortFilterBottomSheet != null) {
            sortFilterBottomSheet.setDynamicFilterModel(dynamicFilterModel);
        }
    }

    @Override
    public void onApplySortFilter(@NotNull ApplySortFilterModel applySortFilterModel) {
        sortFilterBottomSheet = null;

        applySort(applySortFilterModel);
        applyFilter(applySortFilterModel);

        filterController.refreshMapParameter(applySortFilterModel.getMapParameter());
        searchParameter.getSearchParameterHashMap().clear();
        searchParameter.getSearchParameterHashMap().putAll(applySortFilterModel.getMapParameter());

        reloadData();
    }

    private void applySort(@NotNull ApplySortFilterModel applySortFilterModel) {
        if (applySortFilterModel.getSelectedSortName().isEmpty()
                || applySortFilterModel.getSelectedSortMapParameter().isEmpty()) return;

        SearchTracking.eventSearchResultSort(getScreenName(), applySortFilterModel.getSelectedSortName(), presenter.getUserId());
    }

    private void applyFilter(@NotNull ApplySortFilterModel applySortFilterModel) {
        FilterTracking.eventApplyFilter(getFilterTrackingData(), getScreenName(), applySortFilterModel.getSelectedFilterMapParameter());
    }

    @Override
    public void getResultCount(@NotNull Map<String, String> mapParameter) {
        if (presenter == null) {
            setProductCount("0");
            return;
        }

        presenter.getProductCount(mapParameter);
    }

    @Override
    public void setProductCount(String productCountText) {
        if (sortFilterBottomSheet == null) return;

        sortFilterBottomSheet.setResultCountText(String.format(getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_template_text), productCountText));
    }

    @Override
    public String getClassName() {
        if (getActivity() == null) return "";

        return getActivity().getClass().getName();
    }

    @Override
    public void configure(boolean shouldRemove) {
        if (shouldRemove) SearchFilterUtilsKt.removeQuickFilterElevation(searchSortFilter);
        else SearchFilterUtilsKt.applyQuickFilterElevation(getContext(), searchSortFilter);
    }

    @Override
    public void onSearchInTokopediaClick(@NotNull String applink) {
        if (getActivity() == null) return;

        RouteManager.route(getActivity(), applink);
    }

    @Override
    public void addLocalSearchRecommendation(List<Visitable> visitableList) {
        adapter.appendItems(visitableList);
    }

    @Override
    public void onChangeViewClicked(int position) {
        if (presenter == null) return;

        presenter.handleChangeView(position, adapter.getCurrentLayoutType());
    }

    @Override
    public void trackEventSearchResultChangeView(String viewType) {
        SearchTracking.eventSearchResultChangeGrid(getActivity(), viewType, getScreenName());
    }

    @Override
    public void switchSearchNavigationLayoutTypeToListView(int position) {
        if (!getUserVisibleHint() || adapter == null) return;

        staggeredGridLayoutManager.setSpanCount(1);
        adapter.changeSearchNavigationListView(position);
    }

    @Override
    public void switchSearchNavigationLayoutTypeToBigGridView(int position) {
        if (!getUserVisibleHint() || adapter == null) return;

        staggeredGridLayoutManager.setSpanCount(1);
        adapter.changeSearchNavigationSingleGridView(position);
    }

    @Override
    public void switchSearchNavigationLayoutTypeToSmallGridView(int position) {
        if (!getUserVisibleHint() || adapter == null) return;

        staggeredGridLayoutManager.setSpanCount(2);
        adapter.changeSearchNavigationDoubleGridView(position);
    }

    @Override
    public void onTopAdsImageViewImpressed(
            String className,
            @NotNull SearchProductTopAdsImageDataView searchTopAdsImageViewModel
    ) {
        if (className == null || getContext() == null) return;

        new TopAdsUrlHitter(getContext()).hitImpressionUrl(
                className,
                searchTopAdsImageViewModel.getTopAdsImageViewModel().getAdViewUrl(),
                "",
                "",
                searchTopAdsImageViewModel.getTopAdsImageViewModel().getImageUrl()
        );
    }

    @Override
    public void onTopAdsImageViewClick(@NotNull SearchProductTopAdsImageDataView searchTopAdsImageViewModel) {
        if (getContext() == null) return;

        RouteManager.route(getContext(), searchTopAdsImageViewModel.getTopAdsImageViewModel().getApplink());
    }

    @Override
    public void onLocalizingAddressSelected() {
        if (presenter != null)
            presenter.onLocalizingAddressSelected();
    }

    @NotNull
    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public boolean isChooseAddressWidgetEnabled() {
        if (getContext() == null) return false;

        try {
            return ChooseAddressUtils.INSTANCE.isRollOutUser(getContext());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public LocalCacheModel getChooseAddressData() {
        if (getContext() == null) return ChooseAddressConstant.Companion.getEmptyAddress();

        try {
            return ChooseAddressUtils.INSTANCE.getLocalizingAddressData(getContext());
        } catch (Exception e) {
            return ChooseAddressConstant.Companion.getEmptyAddress();
        }
    }

    @Override
    public boolean getIsLocalizingAddressHasUpdated(LocalCacheModel currentChooseAddressData) {
        if (getContext() == null) return false;

        try {
            return ChooseAddressUtils.INSTANCE.isLocalizingAddressHasUpdated(getContext(), currentChooseAddressData);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void refreshItemAtIndex(int index) {
        if (adapter == null) return;

        adapter.refreshItemAtIndex(index);
    }

    @Override
    public void onBannerClicked(@NotNull BannerDataView bannerDataView) {
        redirectionStartActivity(bannerDataView.getApplink(), "");
    }
}
