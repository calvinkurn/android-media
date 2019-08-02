package com.tokopedia.search.result.presentation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.CatalogListSectionContract;
import com.tokopedia.search.result.presentation.SearchSectionContract;
import com.tokopedia.search.result.presentation.view.adapter.CatalogListAdapter;
import com.tokopedia.search.result.presentation.view.adapter.SearchSectionGeneralAdapter;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.CatalogListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.search.result.presentation.view.typefactory.CatalogListTypeFactory;
import com.tokopedia.search.result.presentation.view.typefactory.CatalogListTypeFactoryImpl;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_CATALOG_ID;

public class CatalogListFragment extends SearchSectionFragment implements
        CatalogListSectionContract.View,
        CatalogListener,
        TopAdsItemClickListener,
        TopAdsListener,
        EmptyStateListener,
        BannerAdsListener,
        SearchSectionGeneralAdapter.OnItemChangeView {

    public static final String SCREEN_SEARCH_PAGE_CATALOG_TAB = "Search result - Catalog tab";
    public static final String SOURCE = SearchApiConst.DEFAULT_VALUE_SOURCE_CATALOG;

    private static final String EXTRA_DEPARTMENT_ID = "EXTRA_DEPARTMENT_ID";
    private static final String EXTRA_QUERY = "EXTRA_QUERY";
    private static final String EXTRA_SHARE_URL = "EXTRA_SHARE_URL";
    private static final int REQUEST_CODE_GOTO_CATALOG_DETAIL = 124;

    private static final int REQUEST_ACTIVITY_SORT_CATALOG = 1234;
    private static final int REQUEST_ACTIVITY_FILTER_CATALOG = 4321;
    private static final String SEARCH_CATALOG_TRACE = "search_catalog_trace";

    protected RecyclerView recyclerView;
    protected ProgressBar loadingView;

    protected CatalogListAdapter catalogAdapter;
    protected TopAdsRecyclerAdapter topAdsRecyclerAdapter;

    private PerformanceMonitoring performanceMonitoring;
    private Config topAdsConfig;
    private String shareUrl = "";

    @Inject
    CatalogListSectionContract.Presenter presenter;

    @Inject
    UserSessionInterface userSession;

    public static CatalogListFragment newInstance(SearchParameter searchParameter) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter);

        return createFragmentWithArguments(bundle);
    }

    public static CatalogListFragment createInstanceByQuery(String query) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_QUERY, query);

        return createFragmentWithArguments(bundle);
    }

    public static CatalogListFragment createInstanceByCategoryID(String departmentId) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DEPARTMENT_ID, departmentId);

        return createFragmentWithArguments(bundle);
    }

    private static CatalogListFragment createFragmentWithArguments(Bundle bundle) {
        CatalogListFragment fragment = new CatalogListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String getDepartmentId() {
        if(getSearchParameter() == null) return "";

        return getSearchParameter().get(SearchApiConst.SC);
    }

    @Override
    public void setDepartmentId(String departmentId) {
        if(getSearchParameter() == null) return;

        getSearchParameter().set(SearchApiConst.SC, departmentId);
    }

    @Override
    public String getQueryKey() {
        if(getSearchParameter() == null) return "";

        return getSearchParameter().getSearchQuery();
    }

    @Override
    public void setQueryKey(String queryKey) {
        if(getSearchParameter() == null) return;

        getSearchParameter().setSearchQuery(queryKey);
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(@Nullable String shareUrl) {
        this.shareUrl = TextUtils.isEmpty(shareUrl) ? "" : shareUrl;
    }

    @Override
    public String getScreenNameId() {
        return SCREEN_SEARCH_PAGE_CATALOG_TAB;
    }

    @Override
    protected void initInjector() {
        CatalogListViewComponent component = DaggerCatalogListViewComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .build();

        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        loadDataFromBundle(getArguments());
    }

    private void loadDataFromBundle(@Nullable Bundle bundle) {
        if (bundle != null) {
            copySearchParameter(bundle.getParcelable(EXTRA_SEARCH_PARAMETER));
            setShareUrl(bundle.getString(EXTRA_SHARE_URL));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);
        presenter.initInjector(this);
        return inflater.inflate(R.layout.search_fragment_base_discovery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        prepareView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        catalogAdapter.onSaveInstanceState(outState);
        saveDataToBundle(outState);
    }

    private void saveDataToBundle(Bundle outState) {
        outState.putString(EXTRA_SHARE_URL, getShareUrl());
    }

    @Override
    protected int getFilterRequestCode() {
        return REQUEST_ACTIVITY_FILTER_CATALOG;
    }

    @Override
    protected int getSortRequestCode() {
        return REQUEST_ACTIVITY_SORT_CATALOG;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        loadingView = view.findViewById(R.id.loading);
    }

    protected void prepareView() {
        setupAdapter();
        setupListener();
        if (getUserVisibleHint()) {
            setupSearchNavigation();
        }
    }

    private void setupListener() {
        topAdsRecyclerAdapter.setOnLoadListener((page, totalCount) -> {
            if (isAllowLoadMore()) {
                onLoadMoreCatalog();
            }
        });
    }

    private boolean isAllowLoadMore() {
        return getUserVisibleHint()
                && catalogAdapter.hasNextPage()
                && !isRefreshing();
    }

    protected void onLoadMoreCatalog() {
        presenter.requestCatalogLoadMore();
    }

    @Override
    protected void onSwipeToRefresh() {
        reloadData();
    }

    @Override
    public void setOnCatalogClicked(String catalogID, String catalogName) {
        SearchTracking.eventSearchResultCatalogClick(getActivity(), getQueryKey(), catalogName);
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalDiscovery.CATALOG);
        intent.putExtra(EXTRA_CATALOG_ID, catalogID);
        startActivityForResult(intent, REQUEST_CODE_GOTO_CATALOG_DETAIL);
    }

    @Override
    public String getUserId() {
        return userSession.getUserId();
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

    protected void setupAdapter() {
        if(getActivity() == null) return;

        topAdsConfig = new Config.Builder()
                .setSessionId(getRegistrationId())
                .setUserId(userSession.getUserId())
                .setEndpoint(Endpoint.PRODUCT)
                .build();

        CatalogListTypeFactory typeFactory = new CatalogListTypeFactoryImpl(
                this, this, this,
                topAdsConfig);
        catalogAdapter = new CatalogListAdapter(this, typeFactory);

        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), catalogAdapter);
        topAdsRecyclerAdapter.setConfig(topAdsConfig);
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
        topAdsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());
        topAdsRecyclerAdapter.setTopAdsListener(this);
        topAdsRecyclerAdapter.setHasHeader(true);
        recyclerView.setAdapter(topAdsRecyclerAdapter);
        topAdsRecyclerAdapter.setLayoutManager(getGridLayoutManager());
    }

    @Override
    public void onChangeList() {
        topAdsRecyclerAdapter.setLayoutManager(getLinearLayoutManager());
    }

    @Override
    public void onChangeDoubleGrid() {
        topAdsRecyclerAdapter.setLayoutManager(getGridLayoutManager());
    }

    @Override
    public void onChangeSingleGrid() {
        topAdsRecyclerAdapter.setLayoutManager(getGridLayoutManager());
    }

    @Override
    protected GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (catalogAdapter.isEmptyItem(position) ||
                        catalogAdapter.isCatalogHeader(position) ||
                        topAdsRecyclerAdapter.isLoading(position) ||
                        topAdsRecyclerAdapter.isTopAdsViewHolder(position)) {
                    return spanCount;
                } else {
                    return 1;
                }
            }
        };
    }

    @Override
    protected boolean isSortEnabled() {
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void onFirstTimeLaunch() {
        super.onFirstTimeLaunch();
        requestCatalogList();
    }

    private void requestCatalogList() {
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_CATALOG_TRACE);
        presenter.requestCatalogList();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setShareUrl(savedInstanceState.getString(EXTRA_SHARE_URL));

        if(catalogAdapter != null) {
            catalogAdapter.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void renderListView(List<Visitable> catalogViewModels) {
        if (performanceMonitoring != null) {
            performanceMonitoring.stopTrace();
        }
        topAdsRecyclerAdapter.hideLoading();
        catalogAdapter.incrementStart();
        topAdsRecyclerAdapter.reset();
        catalogAdapter.clearData();
        catalogAdapter.addElements(catalogViewModels);
        isListEmpty = catalogViewModels.isEmpty();
    }

    @Override
    public void renderNextListView(List<Visitable> catalogViewModels) {
        topAdsRecyclerAdapter.hideLoading();
        catalogAdapter.incrementStart();
        catalogAdapter.addElements(catalogViewModels);
    }

    @Override
    public void successRefreshCatalog(List<Visitable> visitables) {
        topAdsRecyclerAdapter.hideLoading();
        if (!visitables.isEmpty()) {
            isListEmpty = false;
            catalogAdapter.setElement(visitables);
        } else {
            isListEmpty = true;
            catalogAdapter.showEmptyState(getActivity(), getQueryKey(), isFilterActive(), getString(R.string.catalog_tab_title).toLowerCase());
            topAdsRecyclerAdapter.shouldLoadAds(false);
            SearchTracking.eventSearchNoResult(getActivity(), getQueryKey(), getScreenName(), getSelectedFilter());
        }
    }

    @Override
    protected void refreshAdapterForEmptySearch() {
        if (catalogAdapter != null) {
            catalogAdapter.showEmptyState(getActivity(), getQueryKey(), isFilterActive(), getString(R.string.catalog_tab_title).toLowerCase());
        }
    }

    @Override
    public void renderErrorView(String message) {
        if (catalogAdapter.isEmpty()) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), null);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        }
    }

    @Override
    public void renderRetryInit() {
        topAdsRecyclerAdapter.hideLoading();
        if (catalogAdapter.isEmpty()) {
            NetworkErrorHelper.showEmptyState(
                    getActivity(),
                    getView(),
                    this::requestCatalogList);
        } else {
            NetworkErrorHelper.createSnackbarWithAction(
                    getActivity(), this::onLoadMoreCatalog).showRetrySnackbar();
        }
    }

    @Override
    public void renderRetryRefresh() {
        if (catalogAdapter.isEmpty()) {
            NetworkErrorHelper.showEmptyState(
                    getActivity(),
                    getView(),
                    () -> presenter.refreshSort());
        } else {
            NetworkErrorHelper.createSnackbarWithAction(
                    getActivity(), () -> presenter.refreshSort());
        }
    }

    @Override
    public void renderUnknown() {

    }

    @Override
    public void renderShareURL(String shareURL) {
        setShareUrl(shareURL);
    }

    @Override
    public void setHasNextPage(boolean hasNextPage) {
        catalogAdapter.setNextPage(hasNextPage);
    }

    @Override
    public int getStartFrom() {
        return catalogAdapter.getStartFrom();
    }

    @Override
    public void initTopAdsParams(RequestParams requestParams) {
        TopAdsParams adsParams = new TopAdsParams();
        if(!TextUtils.isEmpty(getDepartmentId())) {
            adsParams.getParam().put(TopAdsParams.KEY_SRC, SearchApiConst.DEFAULT_VALUE_SOURCE_DIRECTORY);
            adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, getDepartmentId());
        } else {
            adsParams.getParam().put(TopAdsParams.KEY_SRC, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
            adsParams.getParam().put(TopAdsParams.KEY_QUERY, getQueryKey());
        }
        adsParams.getParam().put(TopAdsParams.KEY_USER_ID, userSession.getUserId());
        adsParams.getParam().putAll(requestParams.getParamsAllValueInString());
        topAdsConfig.setTopAdsParams(adsParams);
        topAdsRecyclerAdapter.setConfig(topAdsConfig);
    }

    @Override
    public void onTopAdsLoaded(List<Item> list) {

    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        topAdsRecyclerAdapter.hideLoading();
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        Intent intent = getProductIntent(product.getId());
        startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
    }

    private Intent getProductIntent(String productId){
        if (getContext() != null) {
            return RouteManager.getIntent(getContext(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        if(getActivity() == null) return;

        Intent intent = ((DiscoveryRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shop.getId());
        startActivity(intent);
    }

    @Override
    public void onAddFavorite(int position, Data data) {

    }

    @Override
    protected SearchSectionGeneralAdapter getAdapter() {
        return catalogAdapter;
    }

    @Override
    protected SearchSectionContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public String getSource() {
        return SOURCE;
    }

    @Override
    public void reloadData() {
        catalogAdapter.resetStartFrom();
        catalogAdapter.clearData();
        topAdsRecyclerAdapter.reset();
        presenter.refreshSort();
    }

    @Override
    public void setTopAdsEndlessListener() {
        topAdsRecyclerAdapter.setEndlessScrollListener();
    }

    @Override
    public void unSetTopAdsEndlessListener() {
        topAdsRecyclerAdapter.unsetEndlessScrollListener();
        topAdsRecyclerAdapter.hideLoading();
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

    public SearchParameter getSearchParameter() {
        return this.searchParameter;
    }

    @Override
    public Map<String, Object> getSearchParameterMap() {
        return searchParameter.getSearchParameterMap();
    }
}
