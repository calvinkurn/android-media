package com.tokopedia.search.result.presentation.view.fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

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
import com.tokopedia.iris.util.IrisSession;
import com.tokopedia.kotlin.extensions.view.IntExtKt;
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
import com.tokopedia.search.result.presentation.model.BroadMatchItemViewModel;
import com.tokopedia.search.result.presentation.model.BroadMatchViewModel;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCardOptionViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.SuggestionViewModel;
import com.tokopedia.search.result.presentation.model.TickerViewModel;
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ProductItemDecoration;
import com.tokopedia.search.result.presentation.view.listener.BannedProductsRedirectToBrowserListener;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.BroadMatchListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.search.result.presentation.view.listener.GlobalNavListener;
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener;
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener;
import com.tokopedia.search.result.presentation.view.listener.ProductListener;
import com.tokopedia.search.result.presentation.view.listener.QuickFilterListener;
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener;
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener;
import com.tokopedia.search.result.presentation.view.listener.SearchPerformanceMonitoringListener;
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener;
import com.tokopedia.search.result.presentation.view.listener.TickerListener;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactoryImpl;
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
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.unifycomponents.ChipsUnify;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.net.URLEncoder;
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
        QuickFilterListener,
        GlobalNavListener,
        BannerAdsListener,
        EmptyStateListener,
        RecommendationListener,
        InspirationCarouselListener,
        BannedProductsRedirectToBrowserListener,
        BroadMatchListener,
        InspirationCardListener,
        SortFilterBottomSheet.Callback {

    private static final String SCREEN_SEARCH_PAGE_PRODUCT_TAB = "Search result - Product tab";
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123;
    private static final String SEARCH_RESULT_ENHANCE_ANALYTIC = "SEARCH_RESULT_ENHANCE_ANALYTIC";
    private static final String LAST_POSITION_ENHANCE_PRODUCT = "LAST_POSITION_ENHANCE_PRODUCT";
    private static final String SEARCH_PRODUCT_TRACE = "search_product_trace";
    private static final String EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER";
    private static final String SEARCH_RESULT_PRODUCT_FILTER_ONBOARDING_TAG = "SEARCH_RESULT_PRODUCT_FILTER_ONBOARDING_TAG";
    private static final int REQUEST_CODE_LOGIN = 561;
    private static final String SHOP = "shop";
    private static final int DEFAULT_SPAN_COUNT = 2;
    private static final int SCROLL_THRESHOLD_TO_ANIMATE_TAB = 50;

    @Inject
    ProductListSectionContract.Presenter presenter;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private SwipeRefreshLayout refreshLayout;
    private EndlessRecyclerViewScrollListener staggeredGridLayoutLoadMoreTriggerListener;
    @Nullable private SearchNavigationListener searchNavigationListener;
    private BottomSheetListener bottomSheetListener;
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
                this, this, this,
                this, this, this,
                this, this, this,
                this, this, this, topAdsConfig);

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
        recyclerView.addOnScrollListener(createHideTabOnScrollListener());
    }

    private RecyclerView.OnScrollListener createHideTabOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (searchNavigationListener == null) return;

                if (dy > SCROLL_THRESHOLD_TO_ANIMATE_TAB || dy < -SCROLL_THRESHOLD_TO_ANIMATE_TAB || recyclerView.computeVerticalScrollOffset() == 0) {
                    boolean isVisible = dy <= 0;
                    searchNavigationListener.configureTabLayout(isVisible);
                }

                if (recyclerView.computeVerticalScrollOffset() > 0) applyQuickFilterLayoutOnTop();
                else applyQuickFilterLayoutOnScroll();
            }
        };
    }

    private void applyQuickFilterLayoutOnTop() {
        if(getContext() == null) return;

        applyQuickFilterElevation(getContext());

        int paddingTop = IntExtKt.dpToPx(8, getContext().getResources().getDisplayMetrics());
        animateQuickFilterPaddingTopChanges(paddingTop);
    }

    private void applyQuickFilterElevation(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && searchSortFilter != null) {
            if (searchSortFilter.getElevation() == 0f) {
                int elevation = IntExtKt.dpToPx(5, context.getResources().getDisplayMetrics());
                searchSortFilter.setElevation(elevation);
            }
        }
    }

    private void animateQuickFilterPaddingTopChanges(int paddingTop) {
        ValueAnimator anim = ValueAnimator.ofInt(searchSortFilter.getPaddingTop(), paddingTop);
        anim.addUpdateListener(animator -> setQuickFilterPaddingTop((Integer)animator.getAnimatedValue()));
        anim.setDuration(200);
        anim.start();
    }

    private void setQuickFilterPaddingTop(int paddingTop) {
        if (searchSortFilter == null || searchSortFilter.getPaddingTop() == paddingTop) return;

        searchSortFilter.setPadding(searchSortFilter.getPaddingLeft(), paddingTop, searchSortFilter.getPaddingRight(), searchSortFilter.getPaddingBottom());
    }

    private void applyQuickFilterLayoutOnScroll() {
        if(getContext() == null) return;

        removeQuickFilterElevation();

        int paddingTop = IntExtKt.dpToPx(16, getContext().getResources().getDisplayMetrics());
        animateQuickFilterPaddingTopChanges(paddingTop);
    }

    private void removeQuickFilterElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && searchSortFilter != null) {
            if (searchSortFilter.getElevation() > 0f) {
                searchSortFilter.setElevation(0);
            }
        }
    }

    @NonNull
    private ProductItemDecoration createProductItemDecoration() {
        return new ProductItemDecoration(getContext().getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16));
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
        if (context instanceof SearchPerformanceMonitoringListener) {
            return (SearchPerformanceMonitoringListener) context;
        }

        return null;
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

        searchNavigationListener.setupSearchNavigation(createSearchNavigationClickListener(), true);
        refreshMenuItemGridIcon();
    }

    private SearchNavigationListener.ClickListener createSearchNavigationClickListener() {
        return new SearchNavigationListener.ClickListener() {
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
        };
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

    private void openFilterActivity() {
        if (!isFilterDataAvailable() && getActivity() != null) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_filter_data_not_ready));
            return;
        }

        if (bottomSheetListener != null) {
            openBottomSheetFilter();
        }
    }

    private boolean isFilterDataAvailable() {
        return filters != null && !filters.isEmpty();
    }

    private void openBottomSheetFilter() {
        if (searchParameter == null || getFilters() == null) return;

        FilterTracking.eventOpenFilterPage(getFilterTrackingData());

        bottomSheetListener.loadFilterItems(getFilters(), searchParameter.getSearchParameterHashMap());
        bottomSheetListener.launchFilterBottomSheet();
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

    private ArrayList<Filter> getFilters() {
        return filters;
    }

    private void openSortActivity() {
        if (getActivity() == null) return;

        if (!FilterSortManager.openSortActivity(this, sort, getSelectedSort())) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_sort_data_not_ready));
        }
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
        isListEmpty = false;

        adapter.appendItems(list);
    }

    public void setProductList(List<Visitable> list) {
        adapter.clearData();

        stopSearchResultPagePerformanceMonitoring();
        addProductList(list);
    }

    public void addRecommendationList(List<Visitable> list) {
        isListEmpty = false;

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
    public void sendProductImpressionTrackingEvent(ProductItemViewModel item) {
        String userId = getUserId();
        String searchRef = getSearchRef();
        List<Object> dataLayerList = new ArrayList<>();
        List<ProductItemViewModel> productItemViewModels = new ArrayList<>();

        String filterSortParams
                = SearchTracking.generateFilterAndSortEventLabel(getSelectedFilter(), getSelectedSort());
        dataLayerList.add(item.getProductAsObjectDataLayer(userId, filterSortParams, searchRef));
        productItemViewModels.add(item);

        if(irisSession != null){
            SearchTracking.eventImpressionSearchResultProduct(trackingQueue, dataLayerList, getQueryKey(),
                    irisSession.getSessionId());
        }else {
            SearchTracking.eventImpressionSearchResultProduct(trackingQueue, dataLayerList, getQueryKey(), "");
        }
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
        if (getSearchParameter() == null) setSearchParameter(new SearchParameter());

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
            ProductCardOptionsManager.handleProductCardOptionsActivityResult(requestCode, resultCode, data, this::handleWishlistAction);
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
        if (searchParameter != null) {
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

    private void handleWishlistAction(ProductCardOptionsModel productCardOptionsModel) {
        presenter.handleWishlistAction(productCardOptionsModel);
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
    public void onProductImpressed(ProductItemViewModel item) {
        if (presenter == null) return;

        presenter.onProductImpressed(item);
    }

    @Override
    public void sendTopAdsClickUrl(String topAdsClickUrl, String productId, String productName, String imageUrl) {
        if (getActivity() == null) return;

        String className = getActivity().getClass().getName();

        new TopAdsUrlHitter(className).hitClickUrl(
                getActivity(), topAdsClickUrl, productId, productName, imageUrl
        );
    }

    @Override
    public void sendTopAdsImpressionUrl(String topAdsImpressionUrl, String productId, String productName, String imageUrl) {
        if (getActivity() == null) return;

        String className = getActivity().getClass().getName();

        new TopAdsUrlHitter(className).hitImpressionUrl(
                getActivity(), topAdsImpressionUrl, productId, productName, imageUrl
        );
    }

    @Override
    public void sendTopAdsGTMTrackingProductImpression(ProductItemViewModel item) {
        Product product = createTopAdsProductForTracking(item);

        TopAdsGtmTracker.getInstance().addSearchResultProductViewImpressions(product, item.getPosition());
    }

    private Product createTopAdsProductForTracking(ProductItemViewModel item) {
        Product product = new Product();
        product.setId(item.getProductID());
        product.setName(item.getProductName());
        product.setPriceFormat(item.getPrice());
        product.setCategory(new Category(item.getCategoryID()));
        product.setFreeOngkir(createTopAdsProductFreeOngkirForTracking(item));
        product.setCategoryBreadcrumb(item.getCategoryBreadcrumb());

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
        if (redirectionListener == null) return;

        if (!TextUtils.isEmpty(applink)) {
            redirectionListener.startActivityWithApplink(SearchKotlinExtKt.decodeQueryParameter(applink));
        } else {
            redirectionListener.startActivityWithUrl(url);
        }
    }

    @Override
    public void onItemClicked(ProductItemViewModel item, int adapterPosition) {
        if (presenter == null) return;

        presenter.onProductClick(item, adapterPosition);
    }

    @Override
    public void sendTopAdsGTMTrackingProductClick(ProductItemViewModel item) {
        Product product = createTopAdsProductForTracking(item);

        TopAdsGtmTracker.eventSearchResultProductClick(getContext(), getQueryKey(), product, item.getPosition(), SCREEN_SEARCH_PAGE_PRODUCT_TAB);
    }

    @Override
    public void sendGTMTrackingProductClick(ProductItemViewModel item, String userId) {
        String filterSortParams
                = SearchTracking.generateFilterAndSortEventLabel(getSelectedFilter(), getSelectedSort());
        String searchRef = getSearchRef();
        SearchTracking.trackEventClickSearchResultProduct(
                item.getProductAsObjectDataLayer(userId, filterSortParams, searchRef),
                item.isOrganicAds(),
                getQueryKey(),
                filterSortParams
        );
    }

    @Override
    public void routeToProductDetail(ProductItemViewModel item, int adapterPosition) {
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
    public void onThreeDotsClick(ProductItemViewModel item, int adapterPosition) {
        if (getSearchParameter() == null || getActivity() == null) return;

        SearchTracking.trackEventProductLongPress(getSearchParameter().getSearchQuery(), item.getProductID());
        ProductCardOptionsManager.showProductCardOptions(this, createProductCardOptionsModel(item));
    }

    private ProductCardOptionsModel createProductCardOptionsModel(ProductItemViewModel item) {
        ProductCardOptionsModel productCardOptionsModel = new ProductCardOptionsModel();

        productCardOptionsModel.setHasWishlist(item.isWishlistButtonEnabled());
        productCardOptionsModel.setHasSimilarSearch(true);
        productCardOptionsModel.setWishlisted(item.isWishlisted());
        productCardOptionsModel.setKeyword(getSearchParameter().getSearchQuery());
        productCardOptionsModel.setProductId(item.getProductID() == null ? "" : item.getProductID());
        productCardOptionsModel.setTopAds(item.isTopAds() || item.isOrganicAds());
        productCardOptionsModel.setTopAdsWishlistUrl(item.getTopadsWishlistUrl() == null ? "" : item.getTopadsWishlistUrl());
        productCardOptionsModel.setRecommendation(false);
        productCardOptionsModel.setScreenName(SearchEventTracking.Category.SEARCH_RESULT);
        productCardOptionsModel.setSeeSimilarProductEvent(SearchTracking.EVENT_CLICK_SEARCH_RESULT);

        return productCardOptionsModel;
    }

    @Override
    public void onTickerClicked(TickerViewModel tickerViewModel) {
        SearchTracking.trackEventClickTicker(getQueryKey(), tickerViewModel.getTypeId());
        applyParamsFromTicker(UrlParamUtils.getParamMap(tickerViewModel.getQuery()));
    }

    private void applyParamsFromTicker(HashMap<String, String> tickerParams) {
        HashMap<String, String> params = new HashMap<>(filterController.getParameter());
        params.putAll(tickerParams);
        refreshSearchParameter(params);
        refreshFilterController(params);
        clearDataFilterSort();
        reloadData();
    }

    public void refreshSearchParameter(Map<String, String> queryParams) {
        if (searchParameter == null) return;

        this.searchParameter.getSearchParameterHashMap().clear();
        this.searchParameter.getSearchParameterHashMap().putAll(queryParams);
        this.searchParameter.getSearchParameterHashMap().put(SearchApiConst.ORIGIN_FILTER,
                SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE);
    }

    @Override
    public void refreshFilterController(HashMap<String, String> queryParams) {
        if (filterController == null || getFilters() == null) return;

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
    public void onSuggestionClicked(SuggestionViewModel suggestionViewModel) {
        SearchTracking.eventClickSuggestedSearch(getQueryKey(), suggestionViewModel.getSuggestion());
        performNewProductSearch(suggestionViewModel.getSuggestedQuery());
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

        clearDataFilterSort();
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
        clearDataFilterSort();
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
    public void setEmptyProduct(GlobalNavViewModel globalNavViewModel) {
        isListEmpty = true;
        adapter.setGlobalNavViewModel(globalNavViewModel);
        presenter.clearData();
        adapter.showEmptyState(getActivity(), isFilterActive());
    }

    private boolean isFilterActive() {
        if (filterController == null) return false;

        return filterController.isFilterActive();
    }

    @Override
    public void renderDynamicFilter(DynamicFilterModel pojo) {
        setFilterData(pojo.getData().getFilter());
        setSortData(pojo.getData().getSort());

        if (filterController == null || searchParameter == null
                || getFilters() == null || getSort() == null) return;

        initFilters();
        initSelectedSort();

        refreshAdapterForQuickFilter();
        refreshQuickFilter();

        if (isListEmpty) {
            refreshAdapterForEmptySearch();
        }
    }

    private void initFilters() {
        List<Filter> initializedFilterList = FilterHelper.initializeFilterList(getFilters());
        filterController.appendFilterList(searchParameter.getSearchParameterHashMap(), initializedFilterList);
    }

    private void refreshAdapterForQuickFilter() {
        if (adapter == null) return;

        adapter.refreshQuickFilter();
    }

    private void refreshQuickFilter() {
        List<Option> options = presenter.getQuickFilterOptionList();

        if (searchSortFilter == null || searchSortFilter.chipItems == null || options.size() != searchSortFilter.chipItems.size()) return;

        setSortFilterIndicatorCounter();
        setSortFilterItemState(options);
    }

    private void setSortFilterIndicatorCounter() {
        searchSortFilter.setIndicatorCounter(getSortFilterIndicatorCounter());
    }

    private int getSortFilterIndicatorCounter() {
        int filterCount = filterController.getFilterCount();
        int sortCount = getSortCount();

        return filterCount + sortCount;
    }

    private int getSortCount() {
        if (getSelectedSort() == null) return 0;

        String selectedSortValue = getSelectedSort().get(SearchApiConst.OB);

        if (selectedSortValue != null && !selectedSortValue.equals(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT))
            return 1;
        else return 0;
    }

    private void setSortFilterItemState(List<Option> options) {
        for (int i = 0; i < options.size(); i++) {
            if (isQuickFilterSelected(options.get(i)))
                setQuickFilterChipsSelected(i);
            else
                setQuickFilterChipsNormal(i);
        }
    }

    private void setQuickFilterChipsSelected(int position) {
        searchSortFilter.chipItems.get(position).setType(ChipsUnify.TYPE_SELECTED);
        searchSortFilter.chipItems.get(position).refChipUnify.setChipType(ChipsUnify.TYPE_SELECTED);
    }

    private void setQuickFilterChipsNormal(int position) {
        searchSortFilter.chipItems.get(position).setType(ChipsUnify.TYPE_NORMAL);
        searchSortFilter.chipItems.get(position).refChipUnify.setChipType(ChipsUnify.TYPE_NORMAL);
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
        if (getSort() == null) return;

        addDefaultSelectedSort();

        HashMap<String, String> selectedSort = new HashMap<>(
                SortHelper.Companion.getSelectedSortFromSearchParameter(searchParameter.getSearchParameterHashMap(), getSort())
        );

        setSelectedSort(selectedSort);
    }

    private void addDefaultSelectedSort() {
        if (searchParameter != null && searchParameter.get(SearchApiConst.OB).isEmpty()) {
            searchParameter.set(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT);
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
        generateLoadMoreParameter(0);
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_PRODUCT_TRACE);
        presenter.loadData(getSearchParameter().getSearchParameterMap());
        TopAdsGtmTracker.getInstance().clearDataLayerList();
    }

    private void hideSearchSortFilter() {
        if (isEnableBottomSheetFilterRevamp()) {
            searchSortFilter.setVisibility(View.GONE);
            shimmeringView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isEnableBottomSheetFilterRevamp() {
        return presenter != null && presenter.isBottomSheetFilterRevampEnabled();
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

    private void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
    }

    @Override
    public void backToTop() {
        new Handler().postDelayed(() -> {
            smoothScrollRecyclerView();
            showTabInFull();
        }, 200);
    }

    private void smoothScrollRecyclerView() {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    private void showTabInFull() {
        if (searchNavigationListener != null) {
            searchNavigationListener.configureTabLayout(true);
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
        if (filterController == null) return;

        filterController.initFilterController(getSearchParameter().getSearchParameterHashMap(), quickFilterList);
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
        SearchTracking.eventSearchNoResult(getQueryKey(), getScreenName(), getSelectedFilter(), alternativeKeyword, resultCode, keywordProcess);
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
    public void showFreeOngkirShowCase(boolean hasFreeOngkirBadge) {
        if (getActivity() != null) {
            FreeOngkirShowCaseDialog.show(getActivity(), hasFreeOngkirBadge, this::onFreeOngkirOnBoardingShown);
        }
    }

    private void onFreeOngkirOnBoardingShown() {
        if (presenter != null) {
            presenter.onFreeOngkirOnBoardingShown();
        }
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
        } else {
            SearchTracking.trackEventClickGoToBrowserBannedProductsWithResult(getQueryKey());
        }
    }

    @Override
    public void setSelectedSort(HashMap<String, String> selectedSort) {
        this.selectedSort = selectedSort;
    }

    @Override
    public HashMap<String, String> getSelectedFilter() {
        if (filterController == null) return new HashMap<>();

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
        if (getActivity() == null || getActivity().getApplication() == null) return null;

        return ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent();
    }

    @Override
    public void renderFailRequestDynamicFilter() {
        if (getActivity() == null) return;

        NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.error_get_dynamic_filter));
    }

    @Override
    public void onBannerAdsClicked(int position, String applink, CpmData cpmData) {
        if (getActivity() == null || redirectionListener == null) return;

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
        return presenter != null ? presenter.getDeviceId() : "";
    }

    public void onBottomSheetHide() {
        FilterTracking.eventApplyFilter(getFilterTrackingData(), getScreenName(), getSelectedFilter());
    }

    @Override
    public void showBottomNavigation() {
        if (searchNavigationListener != null) {
            searchNavigationListener.showBottomNavigation();
        }
    }

    @Override
    public void hideBottomNavigation() {
        if (searchNavigationListener != null) {
            searchNavigationListener.hideBottomNavigation();
        }
    }

    @Override
    public void onInspirationCarouselProductClicked(@NotNull InspirationCarouselViewModel.Option.Product product) {
        redirectionStartActivity(product.getApplink(), product.getUrl());

        List<Object> products = new ArrayList<>();
        products.add(product.getProductAsObjectDataLayer());
        SearchTracking.trackEventClickInspirationCarouselOptionProduct(product.getInspirationCarouselType(), getQueryKey(), products);
    }

    @Override
    public void onInspirationCarouselSeeAllClicked(@NotNull InspirationCarouselViewModel.Option option) {
        redirectionStartActivity(option.getApplink(), option.getUrl());

        String keywordBefore = getQueryKey();
        Uri applink = Uri.parse(option.getApplink());
        String keywordAfter = applink.getQueryParameter(SearchApiConst.Q);
        SearchTracking.trackEventClickInspirationCarouselOptionSeeAll(option.getInspirationCarouselType(), keywordBefore, keywordAfter);
    }

    @Override
    public void sendImpressionInspirationCarousel(InspirationCarouselViewModel inspirationCarouselViewModel) {
        List<Object> products = new ArrayList<>();

        for (InspirationCarouselViewModel.Option option : inspirationCarouselViewModel.getOptions()) {
            for (InspirationCarouselViewModel.Option.Product object : option.getProduct()) {
                products.add(object.getProductImpressionAsObjectDataLayer());
            }
        }
        SearchTracking.trackImpressionInspirationCarousel(inspirationCarouselViewModel.getType(), getQueryKey(), products);
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
        if (isWishlisted) {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist));
        }
    }

    @Override
    public void showMessageFailedWishlistAction(boolean isWishlisited) {
        if (isWishlisited) {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist_failed));
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist_failed));
        }
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
    public void onBroadMatchItemClicked(@NotNull BroadMatchItemViewModel broadMatchItemViewModel) {
        trackEventClickBroadMatchItem(broadMatchItemViewModel);

        redirectionStartActivity(broadMatchItemViewModel.getApplink(), broadMatchItemViewModel.getUrl());
    }

    private void trackEventClickBroadMatchItem(@NotNull BroadMatchItemViewModel broadMatchItemViewModel) {
        List<Object> broadMatchItem = new ArrayList<>();
        broadMatchItem.add(broadMatchItemViewModel.asClickObjectDataLayer());

        SearchTracking.trackEventClickBroadMatchItem(getQueryKey(), broadMatchItemViewModel.getAlternativeKeyword(), broadMatchItem);
    }

    @Override
    public void onBroadMatchSeeMoreClicked(@NotNull BroadMatchViewModel broadMatchViewModel) {
        SearchTracking.trackEventClickBroadMatchSeeMore(getQueryKey(), broadMatchViewModel.getKeyword());

        String applink = (broadMatchViewModel.getApplink().startsWith(ApplinkConst.DISCOVERY_SEARCH)) ?
            modifyApplinkToSearchResult(broadMatchViewModel.getApplink()) : broadMatchViewModel.getApplink();

        redirectionStartActivity(applink, broadMatchViewModel.getUrl());
    }

    @Override
    public void trackBroadMatchImpression(String alternativeKeyword, List<Object> impressionObjectDataLayer) {
        SearchTracking.trackEventImpressionBroadMatch(getQueryKey(), alternativeKeyword, impressionObjectDataLayer);
    }

    @Override
    public void onInspirationCardOptionClicked(@NotNull InspirationCardOptionViewModel option) {
        trackEventClickInspirationCardOption(option);

        redirectionStartActivity(option.getApplink(), option.getUrl());
    }

    private void trackEventClickInspirationCardOption(InspirationCardOptionViewModel option) {
        String label = option.getInspirationCardType() + " - " + getQueryKey() + " - " + option.getText();
        SearchTracking.trackEventClickInspirationCardOption(label);
    }

    @Override
    public void hideQuickFilterShimmering() {
        shimmeringView.setVisibility(View.GONE);
    }

    @Override
    public void setNewQuickFilter(List<SortFilterItem> items) {
        searchSortFilter.getSortFilterItems().removeAllViews();
        searchSortFilter.setVisibility(View.VISIBLE);
        searchSortFilter.getSortFilterHorizontalScrollView().setScrollX(0);
        searchSortFilter.addItem((ArrayList<SortFilterItem>) items);
        searchSortFilter.getTextView().setText(getString(R.string.search_filter));
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
    public void showOnBoarding() {
        if (searchSortFilter != null && searchSortFilter.getSortFilterPrefix() != null) {
            ArrayList<CoachMarkItem> coachMarkItemList = new ArrayList<>();

            CoachMarkItem item = createFilterOnBoardingCoachMarkItem();
            coachMarkItemList.add(item);

            CoachMarkBuilder builder = new CoachMarkBuilder();
            builder.allowPreviousButton(false);
            builder.build().show(getActivity(), SEARCH_RESULT_PRODUCT_FILTER_ONBOARDING_TAG, coachMarkItemList);
        }
    }

    private CoachMarkItem createFilterOnBoardingCoachMarkItem() {
        return new CoachMarkItem(
                searchSortFilter.getSortFilterPrefix(),
                getFilterOnBoardingTitle(),
                getFilterOnBoardingDescription()
        );
    }

    private String getFilterOnBoardingTitle() {
        return getString(R.string.search_product_filter_onboarding_title);
    }

    private String getFilterOnBoardingDescription() {
        return getString(R.string.search_product_filter_onboarding_description);
    }

    private Unit openBottomSheetFilterRevamp() {
        if (getFragmentManager() == null
                || presenter == null
                || searchParameter == null
        ) return Unit.INSTANCE;

        FilterTracking.eventOpenFilterPage(getFilterTrackingData());

        sortFilterBottomSheet = new SortFilterBottomSheet();
        sortFilterBottomSheet.show(
                requireFragmentManager(),
                searchParameter.getSearchParameterHashMap(),
                presenter.getDynamicFilterModel(),
                getSortFilterIndicatorCounter() > 0,
                this
        );

        sortFilterBottomSheet.setOnDismissListener(() -> {
            sortFilterBottomSheet = null;
            return Unit.INSTANCE;
        });

        return Unit.INSTANCE;
    }

    @Override
    public void onApplySortFilter(@NotNull ApplySortFilterModel applySortFilterModel) {
        sortFilterBottomSheet = null;

        applySort(applySortFilterModel);
        applyFilter(applySortFilterModel);

        List<Filter> initializedFilterList = FilterHelper.initializeFilterList(getFilters());
        filterController.initFilterController(applySortFilterModel.getMapParameter(), initializedFilterList);
        searchParameter.getSearchParameterHashMap().clear();
        searchParameter.getSearchParameterHashMap().putAll(applySortFilterModel.getMapParameter());

        clearDataFilterSort();
        reloadData();
    }

    private void applySort(@NotNull ApplySortFilterModel applySortFilterModel) {
        if (applySortFilterModel.getSelectedSortName().isEmpty()
                || applySortFilterModel.getSelectedSortMapParameter().isEmpty()) return;

        SearchTracking.eventSearchResultSort(getScreenName(), applySortFilterModel.getSelectedSortName(), presenter.getUserId());

        setSelectedSort(new HashMap<>(applySortFilterModel.getSelectedSortMapParameter()));
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
}
