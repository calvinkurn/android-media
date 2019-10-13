package com.tokopedia.search.result.presentation.view.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.design.drawable.CountDrawable;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData;
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener;
import com.tokopedia.filter.widget.BottomSheetFilterView;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.search.R;
import com.tokopedia.search.analytics.SearchTracking;
import com.tokopedia.search.result.presentation.SearchContract;
import com.tokopedia.search.result.presentation.view.adapter.SearchSectionPagerAdapter;
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment;
import com.tokopedia.search.result.presentation.view.fragment.SearchSectionFragment;
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener;
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener;
import com.tokopedia.search.result.presentation.view.listener.SearchPerformanceMonitoringListener;
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModel;
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModelFactoryModule;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import static com.tokopedia.discovery.common.constants.SearchConstant.Cart.CACHE_TOTAL_CART;
import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_SEARCH_PARAMETER_MODEL;
import static com.tokopedia.discovery.common.constants.SearchConstant.GCM.GCM_ID;
import static com.tokopedia.discovery.common.constants.SearchConstant.GCM.GCM_STORAGE;
import static com.tokopedia.discovery.common.constants.SearchConstant.SEARCH_RESULT_TRACE;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_FIRST_POSITION;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_FORTH_POSITION;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_SECOND_POSITION;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_THIRD_POSITION;

public class SearchActivity extends BaseActivity
        implements SearchContract.View,
        RedirectionListener,
        BottomSheetListener,
        SearchNavigationListener,
        SearchPerformanceMonitoringListener,
        HasComponent<BaseAppComponent> {

    private Toolbar toolbar;
    private FrameLayout container;
    private ProgressBar loadingView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchSectionPagerAdapter searchSectionPagerAdapter;
    private View buttonFilter;
    private View buttonSort;
    private View searchNavDivider;
    private View searchNavContainer;
    private View backButton;
    private TextView searchTextView;
    private ImageView buttonChangeGrid;
    private ImageView buttonCart;
    private ImageView buttonHome;
    private BottomSheetFilterView bottomSheetFilterView;
    private SearchNavigationListener.ClickListener searchNavigationClickListener;

    private String productTabTitle;
    private String catalogTabTitle;
    private String shopTabTitle;
    private String profileTabTitle;
    private String autocompleteApplink;

    @Inject SearchTracking searchTracking;
    @Inject UserSessionInterface userSession;
    @Inject RemoteConfig remoteConfig;
    @Inject @Named(SearchConstant.Cart.CART_LOCAL_CACHE) LocalCacheHandler localCacheHandler;
    @Inject @Named(SearchConstant.SearchShop.SEARCH_SHOP_VIEW_MODEL_FACTORY)
    ViewModelProvider.Factory searchShopViewModelFactory;

    private PerformanceMonitoring performanceMonitoring;
    private SearchParameter searchParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startPerformanceMonitoring();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_search);

        getExtrasFromIntent(getIntent());
        initActivityOnCreate();
        proceed();
        handleIntent();
    }

    @Override
    public void startPerformanceMonitoring() {
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_RESULT_TRACE);
    }

    private void getExtrasFromIntent(Intent intent) {
        searchParameter = getSearchParameterFromIntentUri(intent);
    }

    private void initActivityOnCreate() {
        GraphqlClient.init(this);
        initInjector();
    }

    private void initInjector() {
        SearchViewComponent searchComponent = DaggerSearchViewComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .searchShopViewModelFactoryModule(new SearchShopViewModelFactoryModule(searchParameter.getSearchParameterMap()))
                .build();

        searchComponent.inject(this);
    }

    public BaseAppComponent getBaseAppComponent() {
        return ((BaseMainApplication)getApplication()).getBaseAppComponent();
    }

    private void proceed() {
        findViews();
        prepareView();
    }

    protected void findViews() {
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.container);
        loadingView = findViewById(R.id.progressBar);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pager);
        bottomSheetFilterView = findViewById(R.id.bottomSheetFilter);
        buttonFilter = findViewById(R.id.button_filter);
        buttonSort = findViewById(R.id.button_sort);
        searchNavDivider = findViewById(R.id.search_nav_divider);
        searchNavContainer = findViewById(R.id.search_nav_container);
        backButton = findViewById(R.id.action_up_btn);
        searchTextView = findViewById(R.id.searchTextView);
        buttonChangeGrid = findViewById(R.id.search_change_grid_button);
        buttonCart = findViewById(R.id.search_cart_button);
        buttonHome = findViewById(R.id.search_home_button);
    }

    protected void prepareView() {
        initToolbar();
        initViewPager();
        initBottomSheetListener();
        initSearchNavigationListener();
        bottomSheetFilterView.initFilterBottomSheet(new FilterTrackingData(
                FilterEventTracking.Event.CLICK_SEARCH_RESULT,
                FilterEventTracking.Category.FILTER_PRODUCT,
                "",
                FilterEventTracking.Category.PREFIX_SEARCH_RESULT_PAGE));
    }

    private void initToolbar() {
        configureSupportActionBar();
        setSearchTextViewDrawableLeft();
        configureToolbarOnClickListener();
    }

    private void configureSupportActionBar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }
    }

    private void setSearchTextViewDrawableLeft() {
        Drawable iconSearch = AppCompatResources.getDrawable(this, R.drawable.search_ic_search);
        searchTextView.setCompoundDrawablesWithIntrinsicBounds(iconSearch, null, null, null);
    }

    private void configureToolbarOnClickListener() {
        searchTextView.setOnClickListener(v -> onSearchBarClicked());
        backButton.setOnClickListener(v -> onBackPressed());
        buttonChangeGrid.setOnClickListener(v -> changeGrid());
        buttonCart.setOnClickListener(v -> onCartButtonClicked());
        buttonHome.setOnClickListener(v -> moveToHomeActivity());
    }

    private void onSearchBarClicked() {
        SearchTracking.trackEventClickSearchBar();
        moveToAutoCompleteActivity();
    }

    private void moveToAutoCompleteActivity() {
        String query = searchParameter.getSearchQuery();

        if (!TextUtils.isEmpty(autocompleteApplink)) {
            startActivityWithApplink(autocompleteApplink);
        } else {
            startActivityWithApplink(ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?q=" + query);
        }
    }

    private void onCartButtonClicked() {
        searchTracking.eventActionClickCartButton(searchParameter.getSearchQuery());

        if (userSession.isLoggedIn()) {
            RouteManager.route(this, ApplinkConstInternalMarketplace.CART);
        }
        else {
            RouteManager.route(this, ApplinkConst.LOGIN);
        }
    }

    private void moveToHomeActivity() {
        searchTracking.eventActionClickHomeButton(searchParameter.getSearchQuery());
        RouteManager.route(this, ApplinkConst.HOME);
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SearchActivity.this.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void onPageSelected(int position) {
        switch (position) {
            case TAB_FIRST_POSITION:
                SearchTracking.eventSearchResultTabClick(this, productTabTitle);
                break;
            case TAB_SECOND_POSITION:
                SearchTracking.eventSearchResultTabClick(this, catalogTabTitle);
                break;
            case TAB_THIRD_POSITION:
                SearchTracking.eventSearchResultTabClick(this, shopTabTitle);
                break;
        }
    }

    private void initBottomSheetListener() {
        bottomSheetFilterView.setCallback(new BottomSheetFilterView.Callback() {
            @Override
            public void onApplyFilter(Map<String, String> queryParams) {
                applyFilter(queryParams);
            }

            @Override
            public void onShow() {
                hideBottomNavigation();
            }

            @Override
            public void onHide() {
                showBottomNavigation();
                sendBottomSheetHideEventForProductList();
            }

            @Override
            public AppCompatActivity getActivity() {
                return SearchActivity.this;
            }
        });
    }

    private void applyFilter(Map<String, String> queryParams) {
        if (isViewPagerCurrentItemPositionIsInvalid()) return;

        Fragment fragmentItem = searchSectionPagerAdapter.getRegisteredFragmentAtPosition(viewPager.getCurrentItem());
        if (fragmentItem instanceof SearchSectionFragment) {
            SearchSectionFragment selectedFragment = (SearchSectionFragment) fragmentItem;

            selectedFragment.refreshSearchParameter(queryParams);
            selectedFragment.refreshFilterController(new HashMap<>(queryParams));
            selectedFragment.clearDataFilterSort();
            selectedFragment.reloadData();
        }
    }

    private boolean isViewPagerCurrentItemPositionIsInvalid() {
        return viewPager.getCurrentItem() < 0
                || viewPager.getCurrentItem() >= searchSectionPagerAdapter.getCount();
    }

    @Override
    public void hideBottomNavigation() {
        searchNavContainer.setVisibility(View.GONE);
    }

    public void showBottomNavigation() {
        searchNavContainer.setVisibility(View.VISIBLE);
    }

    private void sendBottomSheetHideEventForProductList() {
        if (isViewPagerCurrentItemPositionIsInvalid()) return;

        Fragment fragmentItem = searchSectionPagerAdapter.getRegisteredFragmentAtPosition(viewPager.getCurrentItem());

        if (fragmentItem instanceof ProductListFragment) {
            ProductListFragment selectedFragment = (ProductListFragment) fragmentItem;
            selectedFragment.onBottomSheetHide();
        }
    }

    private void initSearchNavigationListener() {
        buttonFilter.setOnClickListener(view -> {
            if (searchNavigationClickListener != null) {
                searchNavigationClickListener.onFilterClick();
            }
        });
        buttonSort.setOnClickListener(view -> {
            if (searchNavigationClickListener != null) {
                searchNavigationClickListener.onSortClick();
            }
        });
    }

    private void handleIntent() {
        initResources();
        performProductSearch();
        setToolbarTitle(searchParameter.getSearchQuery());
    }

    private void initResources() {
        productTabTitle = getString(R.string.product_tab_title);
        catalogTabTitle = getString(R.string.catalog_tab_title);
        shopTabTitle = getString(R.string.shop_tab_title);
        profileTabTitle = getString(R.string.title_profile);
    }

    private SearchParameter getSearchParameterFromIntentUri(Intent intent) {
        Uri uri = intent.getData();

        SearchParameter searchParameter = (uri == null) ? new SearchParameter() : new SearchParameter(uri.toString());
        searchParameter.cleanUpNullValuesInMap();

        return searchParameter;
    }

    private void performProductSearch() {
        updateSearchParameterBeforeSearch();
        onSearchingStart();
        loadSection();
    }

    private void updateSearchParameterBeforeSearch() {
        setSearchParameterUniqueId();
        setSearchParameterUserIdIfLoggedIn();
        setSearchParameterDefaultActiveTab();
    }

    private void setSearchParameterUniqueId() {
        String uniqueId = userSession.isLoggedIn() ?
                AuthHelper.getMD5Hash(userSession.getUserId()) :
                AuthHelper.getMD5Hash(getRegistrationId(this));

        searchParameter.set(SearchApiConst.UNIQUE_ID, uniqueId);
    }

    private String getRegistrationId(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, GCM_STORAGE);
        return cache.getString(GCM_ID, "");
    }

    private void setSearchParameterUserIdIfLoggedIn() {
        if(userSession.isLoggedIn()) {
            searchParameter.set(SearchApiConst.USER_ID, userSession.getUserId());
        }
    }

    private void setSearchParameterDefaultActiveTab() {
        String activeTab = searchParameter.get(SearchApiConst.ACTIVE_TAB);

        if (shouldSetActiveTabToDefault(activeTab)) {
            searchParameter.set(SearchApiConst.ACTIVE_TAB, SearchConstant.ActiveTab.PRODUCT);
        }
    }

    private boolean shouldSetActiveTabToDefault(String activeTab) {
        List<String> availableSearchTabs = new ArrayList<>();
        availableSearchTabs.add(SearchConstant.ActiveTab.PRODUCT);
        availableSearchTabs.add(SearchConstant.ActiveTab.CATALOG);
        availableSearchTabs.add(SearchConstant.ActiveTab.SHOP);
        availableSearchTabs.add(SearchConstant.ActiveTab.PROFILE);

        return !availableSearchTabs.contains(activeTab);
    }

    protected void onSearchingStart() {
        showLoadingView(true);
        showContainer(false);
        hideBottomNavigation();
    }

    private void showLoadingView(boolean visible) {
        loadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void showContainer(boolean visible) {
        container.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setToolbarTitle(String query) {
        searchTextView.setText(query);
    }

    private void loadSection() {
        ViewModelProviders.of(this, searchShopViewModelFactory).get(SearchShopViewModel.class);

        List<String> searchFragmentTitles = new ArrayList<>();
        addFragmentTitlesToList(searchFragmentTitles);
        initTabLayout();

        searchSectionPagerAdapter = new SearchSectionPagerAdapter(getSupportFragmentManager(), searchParameter);
        searchSectionPagerAdapter.updateData(searchFragmentTitles);
        viewPager.setAdapter(searchSectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setActiveTab();
    }

    private void addFragmentTitlesToList(List<String> searchSectionItemList) {
        searchSectionItemList.add(productTabTitle);
        searchSectionItemList.add(catalogTabTitle);
        searchSectionItemList.add(shopTabTitle);
        searchSectionItemList.add(profileTabTitle);
    }

    private void initTabLayout() {
        tabLayout.clearOnTabSelectedListeners();
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                SearchActivity.this.onTabReselected(tab.getPosition());
            }
        });
    }

    private void onTabReselected(int tabPosition) {
        if (searchSectionPagerAdapter != null) {
            switch (tabPosition) {
                case TAB_FIRST_POSITION:
                    productListFragmentExecuteBackToTop();
                    break;
                case TAB_SECOND_POSITION:
                    catalogListFragmentExecuteBackToTop();
                    break;
                case TAB_THIRD_POSITION:
                    shopListFragmentExecuteBackToTop();
                    break;
                case TAB_FORTH_POSITION:
                    profileListFragmentExecuteBackToTop();
                    break;
            }
        }
    }

    private void productListFragmentExecuteBackToTop() {
        if (searchSectionPagerAdapter.getProductListFragment() != null) {
            searchSectionPagerAdapter.getProductListFragment().backToTop();
        }
    }

    private void catalogListFragmentExecuteBackToTop() {
        if (searchSectionPagerAdapter.getCatalogListFragment() != null) {
            searchSectionPagerAdapter.getCatalogListFragment().backToTop();
        }
    }

    private void shopListFragmentExecuteBackToTop() {
        if (searchSectionPagerAdapter.getShopListFragment() != null) {
            searchSectionPagerAdapter.getShopListFragment().backToTop();
        }
    }

    private void profileListFragmentExecuteBackToTop() {
        if (searchSectionPagerAdapter.getProfileListFragment() != null) {
            searchSectionPagerAdapter.getProfileListFragment().backToTop();
        }
    }

    private void setActiveTab() {
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                viewPager.setCurrentItem(getViewPagerCurrentItem());
            }
        });
    }

    private int getViewPagerCurrentItem() {
        String activeTab = searchParameter.get(SearchApiConst.ACTIVE_TAB);

        switch (activeTab) {
            case SearchConstant.ActiveTab.CATALOG:
                return TAB_SECOND_POSITION;
            case SearchConstant.ActiveTab.SHOP:
                return TAB_THIRD_POSITION;
            case SearchConstant.ActiveTab.PROFILE:
                return TAB_FORTH_POSITION;
            default:
                return TAB_FIRST_POSITION;
        }
    }

    @Override
    public void startActivityWithApplink(String applink, String... parameter) {
        Intent intent = RouteManager.getIntent(this, applink, parameter);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        configureButtonCart();
        unregisterShake();
    }

    private void configureButtonCart() {
        if (isCartIconInSearchEnabled()) {
            showButtonCart();
        } else {
            hideButtonCart();
        }
    }

    private boolean isCartIconInSearchEnabled() {
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_CART_ICON_IN_SEARCH, true);
    }

    private void showButtonCart() {
        if (userSession.isLoggedIn()) {
            setButtonCartCount();
        }

        buttonCart.setVisibility(View.VISIBLE);
    }

    private void setButtonCartCount() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.search_ic_cart);

        if (drawable instanceof LayerDrawable) {
            int cartCount = localCacheHandler.getInt(CACHE_TOTAL_CART, 0);

            CountDrawable countDrawable = new CountDrawable(this);
            countDrawable.setCount(String.valueOf(cartCount));

            drawable.mutate();

            ((LayerDrawable) drawable).setDrawableByLayerId(R.id.ic_cart_count, countDrawable);

            buttonCart.setImageDrawable(drawable);
        }
    }

    private void hideButtonCart() {
        buttonCart.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onActivityResultBottomSheet(requestCode, resultCode, data);
    }

    private void onActivityResultBottomSheet(int requestCode, int resultCode, Intent data) {
        bottomSheetFilterView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (!bottomSheetFilterView.onBackPressed()) {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXTRA_SEARCH_PARAMETER_MODEL, searchParameter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        searchParameter = savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER_MODEL);
    }

    private void changeGrid() {
        if (searchNavigationClickListener != null) {
            searchNavigationClickListener.onChangeGridClick();
        }
    }

    @Override
    public void removeSearchPageLoading() {
        showLoadingView(false);
        showContainer(true);
        showBottomNavigationForActiveTab();
    }

    private void showBottomNavigationForActiveTab() {
        if (isCurrentActiveTabIsNotProfile()) {
            showBottomNavigation();
        }
    }

    private boolean isCurrentActiveTabIsNotProfile() {
        return !getCurrentActivePageTitle().equals(profileTabTitle);
    }

    private String getCurrentActivePageTitle() {
        if (searchSectionPagerAdapter != null) {
            return searchSectionPagerAdapter.getPageTitle(viewPager.getCurrentItem()).toString();
        }

        return "";
    }

    @Override
    public void showSearchInputView() {
        moveToAutoCompleteActivity();
    }

    @Override
    public void setAutocompleteApplink(String autocompleteApplink) {
        this.autocompleteApplink = autocompleteApplink;
    }

    @Override
    public void loadFilterItems(ArrayList<Filter> filters, Map<String, String> searchParameter) {
        bottomSheetFilterView.loadFilterItems(filters, searchParameter);
    }

    @Override
    public void setFilterResultCount(String formattedResultCount) {
        bottomSheetFilterView.setFilterResultCount(formattedResultCount);
    }

    @Override
    public void launchFilterBottomSheet() {
        bottomSheetFilterView.launchFilterBottomSheet();
    }

    @Override
    public void setupSearchNavigation(ClickListener clickListener, boolean isSortEnabled) {
        if (loadingView.getVisibility() != View.VISIBLE) {
            showBottomNavigation();
        }

        if (isSortEnabled) {
            buttonSort.setVisibility(View.VISIBLE);
            searchNavDivider.setVisibility(View.VISIBLE);
        } else {
            buttonSort.setVisibility(View.GONE);
            searchNavDivider.setVisibility(View.GONE);
        }

        this.searchNavigationClickListener = clickListener;
    }

    @Override
    public void refreshMenuItemGridIcon(int titleResId, int iconResId) {
        if(buttonChangeGrid != null) {
            buttonChangeGrid.setImageResource(iconResId);
        }
    }

    @Override
    public BaseAppComponent getComponent() {
        return getBaseAppComponent();
    }

    @Override
    public void startActivityWithUrl(String url, String... parameter) {
        RouteManager.route(this, url, parameter);
    }

    @Override
    public void stopPerformanceMonitoring() {
        if (performanceMonitoring != null) {
            performanceMonitoring.stopTrace();
            performanceMonitoring = null;
        }
    }
}
