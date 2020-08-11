package com.tokopedia.search.result.presentation.view.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback;
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface;
import com.tokopedia.analytics.performance.util.PltPerformanceData;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.discovery.common.utils.URLParser;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData;
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener;
import com.tokopedia.filter.widget.BottomSheetFilterView;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.search.R;
import com.tokopedia.search.analytics.SearchTracking;
import com.tokopedia.search.result.presentation.view.adapter.SearchSectionPagerAdapter;
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment;
import com.tokopedia.search.result.presentation.view.fragment.ProfileListFragment;
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener;
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener;
import com.tokopedia.search.result.presentation.view.listener.SearchPerformanceMonitoringListener;
import com.tokopedia.search.result.presentation.viewmodel.SearchViewModel;
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModel;
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModelFactoryModule;
import com.tokopedia.search.utils.CountDrawable;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import static com.tokopedia.discovery.common.constants.SearchConstant.Cart.CACHE_TOTAL_CART;
import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_SEARCH_PARAMETER_MODEL;
import static com.tokopedia.discovery.common.constants.SearchConstant.SEARCH_RESULT_PLT_NETWORK_METRICS;
import static com.tokopedia.discovery.common.constants.SearchConstant.SEARCH_RESULT_PLT_PREPARE_METRICS;
import static com.tokopedia.discovery.common.constants.SearchConstant.SEARCH_RESULT_PLT_RENDER_METRICS;
import static com.tokopedia.discovery.common.constants.SearchConstant.SEARCH_RESULT_TRACE;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_FIRST_POSITION;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_SECOND_POSITION;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_THIRD_POSITION;

public class SearchActivity extends BaseActivity
        implements
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
    private View topBarShadow;
    private BottomSheetFilterView bottomSheetFilterView;
    private SearchNavigationListener.ClickListener searchNavigationClickListener;

    private String productTabTitle;
    private String shopTabTitle;
    private String profileTabTitle;
    private String autocompleteApplink;

    @Inject UserSessionInterface userSession;
    @Inject @Named(SearchConstant.Cart.CART_LOCAL_CACHE) LocalCacheHandler localCacheHandler;
    @Inject @Named(SearchConstant.SearchShop.SEARCH_SHOP_VIEW_MODEL_FACTORY)
    ViewModelProvider.Factory searchShopViewModelFactory;
    @Inject @Named(SearchConstant.SEARCH_VIEW_MODEL_FACTORY)
    ViewModelProvider.Factory searchViewModelFactory;

    @Nullable
    SearchViewModel searchViewModel;
    @Nullable
    SearchShopViewModel searchShopViewModel;

    private PageLoadTimePerformanceInterface pageLoadTimePerformanceMonitoring;
    private SearchParameter searchParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startPerformanceMonitoring();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_search);

        setStatusBarColor();
        getExtrasFromIntent(getIntent());
        initActivityOnCreate();
        proceed();
        handleIntent();
    }

    @Override
    public void startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = new PageLoadTimePerformanceCallback(
                SEARCH_RESULT_PLT_PREPARE_METRICS,
                SEARCH_RESULT_PLT_NETWORK_METRICS,
                SEARCH_RESULT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        );

        pageLoadTimePerformanceMonitoring.startMonitoring(SEARCH_RESULT_TRACE);
        pageLoadTimePerformanceMonitoring.startPreparePagePerformanceMonitoring();
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(getResources().getColor(R.color.white));
        }
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
        topBarShadow = findViewById(R.id.search_top_bar_shadow);
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
        configureToolbarOnClickListener();
        configureToolbarVisibility();
    }

    private void configureSupportActionBar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }
    }

    private void configureToolbarOnClickListener() {
        searchTextView.setOnClickListener(v -> onSearchBarClicked());
        backButton.setOnClickListener(v -> onBackPressed());
        buttonChangeGrid.setOnClickListener(v -> changeGrid());
        buttonCart.setOnClickListener(v -> onCartButtonClicked());
        buttonHome.setOnClickListener(v -> moveToHomeActivity());
    }

    private void onSearchBarClicked() {
        String keyword = "";
        if (searchParameter != null) keyword = searchParameter.getSearchQuery();

        SearchTracking.trackEventClickSearchBar(keyword);
        moveToAutoCompleteActivity();
    }

    private void moveToAutoCompleteActivity() {
        String query = URLEncoder.encode(searchParameter.getSearchQuery()).replace("+", " ");

        String currentAutoCompleteApplink = !TextUtils.isEmpty(autocompleteApplink) ?
                autocompleteApplink : ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?q=" + query;

        Map<String, String> autoCompleteParams = new URLParser(currentAutoCompleteApplink).getParamKeyValueMap();
        autoCompleteParams.put(SearchApiConst.PREVIOUS_KEYWORD, query);

        startActivityWithApplink(
                ApplinkConstInternalDiscovery.AUTOCOMPLETE
                        + "?"
                        + UrlParamUtils.generateUrlParamString(autoCompleteParams)
        );
    }

    private void onCartButtonClicked() {
        SearchTracking.eventActionClickCartButton(searchParameter.getSearchQuery());

        if (userSession.isLoggedIn()) {
            RouteManager.route(this, ApplinkConstInternalMarketplace.CART);
        }
        else {
            RouteManager.route(this, ApplinkConst.LOGIN);
        }
    }

    private void moveToHomeActivity() {
        SearchTracking.eventActionClickHomeButton(searchParameter.getSearchQuery());
        RouteManager.route(this, ApplinkConst.HOME);
    }

    private void configureToolbarVisibility() {
        if (isLandingPage()) {
            tabLayout.setVisibility(View.GONE);
        }
    }

    private boolean isLandingPage() {
        return searchParameter.getBoolean(SearchApiConst.LANDING_PAGE);
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(2);
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
        new Handler().postDelayed(() -> animateTab(true), 300);

        switch (position) {
            case TAB_FIRST_POSITION:
                SearchTracking.eventSearchResultTabClick(this, productTabTitle);
                break;
            case TAB_SECOND_POSITION:
                SearchTracking.eventSearchResultTabClick(this, shopTabTitle);
                break;
        }
    }

    private void initBottomSheetListener() {
        bottomSheetFilterView.setCallback(new BottomSheetFilterView.Callback() {
            @Override
            public void onApplyFilter(Map<String, String> queryParams) {
                applyBottomSheetFilter(queryParams);
            }

            @Override
            public void onShow() { }

            @Override
            public void onHide() {
                sendBottomSheetHideEventForProductList();
            }

            @NotNull
            @Override
            public AppCompatActivity getActivity() {
                return SearchActivity.this;
            }
        });
    }

    private void applyBottomSheetFilter(Map<String, String> queryParams) {
        if (isViewPagerCurrentItemPositionIsInvalid()) return;

        Fragment fragmentItem = searchSectionPagerAdapter.getRegisteredFragmentAtPosition(viewPager.getCurrentItem());
        if (fragmentItem instanceof ProductListFragment) {
            ProductListFragment selectedFragment = (ProductListFragment) fragmentItem;

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

    @Override
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
        initViewModel();
        observeViewModel();
        performProductSearch();
        setToolbarTitle(searchParameter.getSearchQuery());
    }

    private void initResources() {
        productTabTitle = getString(R.string.product_tab_title);
        shopTabTitle = getString(R.string.shop_tab_title);
        profileTabTitle = getString(R.string.title_profile);
    }

    private void initViewModel() {
        searchViewModel = ViewModelProviders.of(this, searchViewModelFactory).get(SearchViewModel.class);
        searchShopViewModel = ViewModelProviders.of(this, searchShopViewModelFactory).get(SearchShopViewModel.class);
    }

    private void observeViewModel() {
        observeAutoCompleteEvent();
        observeHideLoadingEvent();
        observeBottomNavigationVisibility();
    }

    private void observeAutoCompleteEvent() {
        if (searchViewModel == null) return;

        searchViewModel.getShowAutoCompleteViewEventLiveData().observe(this, booleanEvent -> {
            if (booleanEvent != null) {
                Boolean content = booleanEvent.getContentIfNotHandled();

                if (content != null && content) {
                    showSearchInputView();
                }
            }
        });
    }

    private void observeHideLoadingEvent() {
        if (searchViewModel == null) return;

        searchViewModel.getHideLoadingEventLiveData().observe(this, booleanEvent -> {
            if (booleanEvent != null) {
                Boolean content = booleanEvent.getContentIfNotHandled();

                if (content != null && content) {
                    removeSearchPageLoading();
                }
            }
        });
    }

    private void observeBottomNavigationVisibility() {
        if (searchViewModel == null) return;

        searchViewModel.getBottomNavigationVisibilityLiveData().observe(this, isVisible -> {
            if (isVisible) {
                showBottomNavigation();
            }
            else {
                hideBottomNavigation();
            }
        });
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
                AuthHelper.getMD5Hash(userSession.getDeviceId());

        searchParameter.set(SearchApiConst.UNIQUE_ID, uniqueId);
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
        String toolbarTitle = getToolbarTitle(query);
        searchTextView.setText(toolbarTitle);
    }

    private String getToolbarTitle(String query) {
        if (getResources() == null) return query;

        return query == null || query.isEmpty() ?
                getResources().getString(R.string.search_toolbar_title_default)
                : query;
    }

    private void loadSection() {
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

        if (!isLandingPage()) {
            searchSectionItemList.add(shopTabTitle);
            searchSectionItemList.add(profileTabTitle);
        }
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
                    shopListFragmentExecuteBackToTop();
                    break;
                case TAB_THIRD_POSITION:
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
            case SearchConstant.ActiveTab.SHOP:
                return TAB_SECOND_POSITION;
            case SearchConstant.ActiveTab.PROFILE:
                return TAB_THIRD_POSITION;
            default:
                return TAB_FIRST_POSITION;
        }
    }

    @Override
    public void startActivityWithApplink(String applink, String... parameter) {
        RouteManager.route(this, applink, parameter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showButtonCart();
    }

    @Override
    public boolean isAllowShake() {
        return false;
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

    private void changeGrid() {
        if (searchNavigationClickListener != null) {
            searchNavigationClickListener.onChangeGridClick();
        }
    }

    @Override
    public void removeSearchPageLoading() {
        showLoadingView(false);
        showContainer(true);
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
        searchNavContainer.post(() -> {
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
        });
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
    public void startPreparePagePerformanceMonitoring() {
        if (pageLoadTimePerformanceMonitoring != null) {
            pageLoadTimePerformanceMonitoring.startPreparePagePerformanceMonitoring();
        }
    }

    @Override
    public void stopPreparePagePerformanceMonitoring() {
        if (pageLoadTimePerformanceMonitoring != null) {
            pageLoadTimePerformanceMonitoring.stopPreparePagePerformanceMonitoring();
        }
    }

    @Override
    public void startNetworkRequestPerformanceMonitoring() {
        if (pageLoadTimePerformanceMonitoring != null) {
            pageLoadTimePerformanceMonitoring.startNetworkRequestPerformanceMonitoring();
        }
    }

    @Override
    public void stopNetworkRequestPerformanceMonitoring() {
        if (pageLoadTimePerformanceMonitoring != null) {
            pageLoadTimePerformanceMonitoring.stopNetworkRequestPerformanceMonitoring();
        }
    }

    @Override
    public void startRenderPerformanceMonitoring() {
        if (pageLoadTimePerformanceMonitoring != null) {
            pageLoadTimePerformanceMonitoring.startRenderPerformanceMonitoring();
        }
    }

    @Override
    public void stopRenderPerformanceMonitoring() {
        if (pageLoadTimePerformanceMonitoring != null) {
            pageLoadTimePerformanceMonitoring.stopRenderPerformanceMonitoring();
        }
    }

    @Override
    public void stopPerformanceMonitoring() {
        if (pageLoadTimePerformanceMonitoring != null) {
            pageLoadTimePerformanceMonitoring.stopMonitoring();
        }
    }

    @Nullable
    public PltPerformanceData getPltPerformanceResultData() {
        if (pageLoadTimePerformanceMonitoring != null) {
            return pageLoadTimePerformanceMonitoring.getPltPerformanceData();
        }
        return null;
    }

    @Override
    public void configureTabLayout(boolean isVisible) {
        Fragment fragmentItem = searchSectionPagerAdapter.getRegisteredFragmentAtPosition(viewPager.getCurrentItem());
        if (fragmentItem instanceof ProfileListFragment) return;

        animateTab(isVisible);
    }

    private void animateTab(boolean isVisible) {
        int targetHeight = isVisible ? getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_40) : 0;

        if (tabLayout == null || tabLayout.getLayoutParams().height == targetHeight) return;

        ValueAnimator anim = ValueAnimator.ofInt(tabLayout.getMeasuredHeight(), targetHeight);
        anim.addUpdateListener(this::changeTabHeightByAnimator);
        anim.addListener(createTabAnimatorListener(isVisible));
        anim.setDuration(200);
        anim.start();
    }

    private void changeTabHeightByAnimator(ValueAnimator valueAnimator) {
        int height = (Integer) valueAnimator.getAnimatedValue();

        changeTabHeight(height);
    }

    private void changeTabHeight(int height) {
        ViewGroup.LayoutParams layoutParams = tabLayout.getLayoutParams();
        layoutParams.height = height;
        tabLayout.setLayoutParams(layoutParams);
    }

    private Animator.AnimatorListener createTabAnimatorListener(boolean isVisible) {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onTabAnimationEnd(isVisible);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    private void onTabAnimationEnd(boolean isVisible) {
        if (topBarShadow == null) return;

        if (isVisible) topBarShadow.setVisibility(View.VISIBLE);
        else topBarShadow.setVisibility(View.GONE);
    }
}
