package com.tokopedia.search.result.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.SearchSectionContract;
import com.tokopedia.search.result.presentation.ShopListSectionContract;
import com.tokopedia.search.result.presentation.model.ShopViewModel;
import com.tokopedia.search.result.presentation.view.adapter.SearchSectionGeneralAdapter;
import com.tokopedia.search.result.presentation.view.adapter.ShopListAdapter;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ShopListItemDecoration;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.search.result.presentation.view.listener.ShopListener;
import com.tokopedia.search.result.presentation.view.typefactory.ShopListTypeFactoryImpl;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.discovery.common.constants.SearchConstant.SearchShop.SHOP_PRODUCT_PREVIEW_ITEM_MAX_COUNT;

public class ShopListFragment
        extends SearchSectionFragment
        implements
        ShopListSectionContract.View,
        SearchSectionGeneralAdapter.OnItemChangeView,
        ShopListener,
        EmptyStateListener,
        BannerAdsListener {

    public static final String SCREEN_SEARCH_PAGE_SHOP_TAB = "Search result - Store tab";
    private static final int REQUEST_ACTIVITY_SORT_SHOP = 1235;
    private static final int REQUEST_ACTIVITY_FILTER_SHOP = 4322;
    private static final String SEARCH_SHOP_TRACE = "search_shop_trace";

    private RecyclerView recyclerView;
    private ShopListAdapter adapter;

    @Inject
    ShopListSectionContract.Presenter presenter;
    @Inject
    UserSessionInterface userSession;

    private int loadShopRow = START_ROW_FIRST_TIME_LOAD;
    private boolean isLoadingData;
    private boolean isNextPageAvailable = true;

    private EndlessRecyclerViewScrollListener gridLayoutLoadMoreTriggerListener;
    private PerformanceMonitoring performanceMonitoring;

    public static ShopListFragment newInstance(SearchParameter searchParameter) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter);

        ShopListFragment shopListFragment = new ShopListFragment();
        shopListFragment.setArguments(args);
        return shopListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            loadDataFromBundle(savedInstanceState);
        } else {
            loadDataFromBundle(getArguments());
        }
    }

    private void loadDataFromBundle(Bundle bundle) {
        if (bundle != null) {
            copySearchParameter(bundle.getParcelable(EXTRA_SEARCH_PARAMETER));
        }
    }

    @Override
    protected void initInjector() {
        ShopListViewComponent component = DaggerShopListViewComponent.builder()
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

        return inflater.inflate(R.layout.fragment_shop_list_search, null);
    }

    @Override
    public void onViewCreatedBeforeLoadData(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initListener();
        bindView(view);
        if (getUserVisibleHint()) {
            setupSearchNavigation();
        }
    }

    private void bindView(View rootView) {
        if(getContext() == null || getContext().getResources() == null) return;
        setSpanCountToOne();

        adapter = new ShopListAdapter(this,
                new ShopListTypeFactoryImpl(this, this, this));

        recyclerView = rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(getGridLayoutManager());
        recyclerView.addItemDecoration(
                new ShopListItemDecoration(
                        getContext().getResources().getDimensionPixelSize(R.dimen.dp_16),
                        getContext().getResources().getDimensionPixelSize(R.dimen.dp_16),
                        getContext().getResources().getDimensionPixelSize(R.dimen.dp_16),
                        getContext().getResources().getDimensionPixelSize(R.dimen.dp_16)
                )
        );

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(gridLayoutLoadMoreTriggerListener);

        adapter.addLoading();
    }

    private void setSpanCountToOne() {
        setSpanCount(1);
        getGridLayoutManager().setSpanCount(1);
    }

    private void initListener() {
        gridLayoutLoadMoreTriggerListener = new EndlessRecyclerViewScrollListener(getGridLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isAllowLoadMore()) {
                    loadShopRow = totalItemsCount - 1;
                    loadShop();
                }
            }
        };
    }

    private boolean isAllowLoadMore() {
        return getUserVisibleHint()
                && !isLoadingData
                && !isRefreshing()
                && isNextPageAvailable;
    }

    @Override
    protected boolean isFirstActiveTab() {
        return getActiveTab().equals(SearchConstant.ActiveTab.SHOP);
    }

    private void loadShopFirstTime() {
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_SHOP_TRACE);

        loadShopRow = START_ROW_FIRST_TIME_LOAD;
        loadShop();
    }

    private void loadShop() {
        if(getSearchParameter() == null) return;

        generateLoadShopParameter(loadShopRow);

        isLoadingData = true;

        loadDataFromPresenter();
    }

    private void generateLoadShopParameter(int startRow) {
        if(getSearchParameter() == null) return;

        getSearchParameter().set(SearchApiConst.UNIQUE_ID, generateUniqueId());
        getSearchParameter().set(SearchApiConst.USER_ID, generateUserId());
        getSearchParameter().set(SearchApiConst.START, String.valueOf(startRow));
    }

    private String generateUserId() {
        if(userSession == null) return "0";

        return userSession.isLoggedIn() ? userSession.getUserId() : "0";
    }

    private String generateUniqueId() {
        if(userSession == null) return "";

        return userSession.isLoggedIn() ?
                AuthUtil.md5(userSession.getUserId()) :
                AuthUtil.md5(getRegistrationId());
    }

    private void loadDataFromPresenter() {
        if(loadShopRow == START_ROW_FIRST_TIME_LOAD) {
            presenter.loadData(getSearchParameter().getSearchParameterMap(), loadShopRow);
        }
        else {
            presenter.loadMoreData(getSearchParameter().getSearchParameterMap(), loadShopRow);
        }
    }

    @Override
    public void onSearchShopSuccessWithData(List<Visitable> shopItemList, boolean isHasNextPage) {
        if(adapter == null) return;

        if (performanceMonitoring != null) {
            performanceMonitoring.stopTrace();
            performanceMonitoring = null;
        }

        sendShopImpressionTrackingEvent(shopItemList);
        handleSearchResult(shopItemList, isHasNextPage);
        stopLoadingAndHideRefreshLayout();
    }

    private void sendShopImpressionTrackingEvent(List<Visitable> list) {
        List<Object> dataLayerShopItemList = new ArrayList<>();
        List<Object> dataLayerShopItemProductList = new ArrayList<>();

        for (Visitable object : list) {
            if (object instanceof ShopViewModel.ShopItem) {
                ShopViewModel.ShopItem item = (ShopViewModel.ShopItem) object;

                dataLayerShopItemList.add(item.getShopAsObjectDataLayer());
                dataLayerShopItemProductList.addAll(createShopProductPreviewDataLayerObjectList(item));
            }
        }

        searchTracking.trackImpressionSearchResultShop(dataLayerShopItemList, getQueryKey());
        searchTracking.trackImpressionSearchResultShopProductPreview(dataLayerShopItemProductList, getQueryKey());
    }

    private List<Object> createShopProductPreviewDataLayerObjectList(ShopViewModel.ShopItem shopItem) {
        List<Object> dataLayerShopItemProductList = new ArrayList<>();

        int maxProductCount = getProductPreviewItemMaxCount(shopItem.getProductList());

        for(int i = 0; i < maxProductCount; i++) {
            ShopViewModel.ShopItem.ShopItemProduct product = shopItem.getProductList().get(i);
            dataLayerShopItemProductList.add(product.getShopProductPreviewAsObjectDataLayerList());
        }

        return dataLayerShopItemProductList;
    }

    private int getProductPreviewItemMaxCount(List<ShopViewModel.ShopItem.ShopItemProduct> shopItemProductList) {
        return shopItemProductList.size() > SHOP_PRODUCT_PREVIEW_ITEM_MAX_COUNT
                ? SHOP_PRODUCT_PREVIEW_ITEM_MAX_COUNT : shopItemProductList.size();
    }

    private void handleSearchResult(List<Visitable> shopItemList, boolean isHasNextPage) {
        isListEmpty = false;
        isNextPageAvailable = isHasNextPage;
        adapter.removeLoading();
        adapter.appendItems(shopItemList);

        updateScrollListenerState(isHasNextPage);

        if (isHasNextPage) {
            adapter.addLoading();
        }
    }

    public void updateScrollListenerState(boolean hasNextPage){
        if(getAdapter() == null) return;

        gridLayoutLoadMoreTriggerListener.updateStateAfterGetData();
        gridLayoutLoadMoreTriggerListener.setHasNextPage(hasNextPage);
    }

    @Override
    public void onSearchShopSuccessEmptyResult() {
        if(adapter == null) return;

        handleEmptySearchResult();

        stopLoadingAndHideRefreshLayout();
    }

    private void handleEmptySearchResult() {
        isNextPageAvailable = false;
        adapter.removeLoading();
        if (adapter.isListEmpty()) {
            isListEmpty = true;
            adapter.showEmptyState(getActivity(), getQueryKey(), isFilterActive(), getString(R.string.shop_tab_title).toLowerCase());
            SearchTracking.eventSearchNoResult(getActivity(), getQueryKey(), getScreenName(), getSelectedFilter());
        }
    }

    @Override
    public void onSearchShopFailed() {
        if(adapter == null) return;

        adapter.removeLoading();

        NetworkErrorHelper.RetryClickedListener retryClickedListener = this::loadShop;

        if (adapter.isListEmpty()) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), retryClickedListener);
        } else {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), retryClickedListener).showRetrySnackbar();
        }

        stopLoadingAndHideRefreshLayout();
    }

    private void stopLoadingAndHideRefreshLayout() {
        isLoadingData = false;
        hideRefreshLayout();
    }

    @Override
    protected void refreshAdapterForEmptySearch() {
        if (adapter != null && getSearchParameter() != null) {
            adapter.showEmptyState(getActivity(), getSearchParameter().getSearchQuery(), isFilterActive(), getString(R.string.shop_tab_title).toLowerCase());
        }
    }

    @Override
    public String getScreenNameId() {
        return SCREEN_SEARCH_PAGE_SHOP_TAB;
    }

    @Override
    protected void onFirstTimeLaunch() {
        super.onFirstTimeLaunch();
        loadShopFirstTime();
    }

    @Override
    public void onItemClicked(@NonNull ShopViewModel.ShopItem shopItem) {
        if (redirectionListener == null) return;

        trackShopItemClick(shopItem);

        redirectIfApplinkNotEmpty(shopItem.getApplink());
    }

    private void trackShopItemClick(@NonNull ShopViewModel.ShopItem shopItem) {
        searchTracking.trackSearchResultShopItemClick(shopItem.getShopAsObjectDataLayer(), getQueryKey());

        if(isShopNotActive(shopItem)) {
            searchTracking.trackSearchResultShopItemClosedClick(shopItem.getShopAsObjectDataLayer(), getQueryKey());
        }
    }

    private boolean isShopNotActive(@NonNull ShopViewModel.ShopItem shopItem) {
        return shopItem.isClosed()
                || shopItem.isModerated()
                || shopItem.isInactive();
    }

    private void redirectIfApplinkNotEmpty(@Nullable String applink) {
        if (!TextUtils.isEmpty(applink)) {
            redirectionListener.startActivityWithApplink(applink);
        }
    }

    @Override
    public void onProductItemClicked(@NonNull ShopViewModel.ShopItem.ShopItemProduct shopItemProduct) {
        if (redirectionListener == null) return;

        searchTracking.trackSearchResultShopProductPreviewClick(shopItemProduct.getShopProductPreviewAsObjectDataLayer(), getQueryKey());

        redirectIfApplinkNotEmpty(shopItemProduct.getApplink());
    }

    @Override
    public void onSelectedFilterRemoved(String uniqueId) {
        removeSelectedFilter(uniqueId);
    }

    @Override
    public void onEmptyButtonClicked() {
        SearchTracking.eventUserClickNewSearchOnEmptySearch(getContext(), getScreenName());
        showSearchInputView();
    }

    @Override
    public List<Option> getSelectedFilterAsOptionList() {
        return getOptionListFromFilterController();
    }

    @Override
    protected SearchSectionGeneralAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected SearchSectionContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.isLoading(position) || adapter.isEmptyItem(position)) {
                    return spanCount;
                } else {
                    return 1;
                }
            }
        };
    }

    @Override
    protected boolean isSortEnabled() {
        return false;
    }
    
    @Override
    public String getUserId() {
        if(userSession == null) return "0";

        return userSession.getUserId();
    }

    private String getQueryKey() {
        if(getSearchParameter() == null) return "";

        return getSearchParameter().getSearchQuery();
    }

    @Override
    protected void switchLayoutType() {

    }

    @Override
    public void reloadData() {
        adapter.clearData();
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_SHOP_TRACE);
        loadShopRow = START_ROW_FIRST_TIME_LOAD;
        loadShop();
    }

    @Override
    protected void onSwipeToRefresh() {
        showRefreshLayout();
        reloadData();
    }

    @Override
    protected int getFilterRequestCode() {
        return REQUEST_ACTIVITY_FILTER_SHOP;
    }

    @Override
    protected int getSortRequestCode() {
        return REQUEST_ACTIVITY_SORT_SHOP;
    }

    @Override
    public void onChangeList() {
    }

    @Override
    public void onChangeDoubleGrid() {
    }

    @Override
    public void onChangeSingleGrid() {
    }

    public void backToTop() {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    public SearchParameter getSearchParameter() {
        return this.searchParameter;
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
    public void removeLoading() {
        removeSearchPageLoading();
    }
}
