package com.tokopedia.search.result.presentation.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.motion.widget.MotionLayout;
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
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.discovery.common.utils.URLParser;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.abtest.AbTestPlatform;
import com.tokopedia.search.R;
import com.tokopedia.search.analytics.SearchTracking;
import com.tokopedia.search.result.presentation.view.adapter.SearchSectionPagerAdapter;
import com.tokopedia.search.result.presentation.view.listener.QuickFilterElevation;
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener;
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener;
import com.tokopedia.search.result.presentation.view.listener.SearchPerformanceMonitoringListener;
import com.tokopedia.search.result.presentation.viewmodel.SearchViewModel;
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModel;
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModelFactoryModule;
import com.tokopedia.search.utils.CountDrawable;
import com.tokopedia.search.utils.SearchLogger;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.searchbar.data.HintData;
import com.tokopedia.searchbar.navigation_component.NavToolbar;
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder;
import com.tokopedia.searchbar.navigation_component.icons.IconList;
import com.tokopedia.unifycomponents.LoaderUnify;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.Nullable;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import kotlin.Unit;

import static com.tokopedia.discovery.common.constants.SearchConstant.Cart.CACHE_TOTAL_CART;
import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_SEARCH_PARAMETER_MODEL;
import static com.tokopedia.discovery.common.constants.SearchConstant.SEARCH_RESULT_PLT_NETWORK_METRICS;
import static com.tokopedia.discovery.common.constants.SearchConstant.SEARCH_RESULT_PLT_PREPARE_METRICS;
import static com.tokopedia.discovery.common.constants.SearchConstant.SEARCH_RESULT_PLT_RENDER_METRICS;
import static com.tokopedia.discovery.common.constants.SearchConstant.SEARCH_RESULT_TRACE;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_FIRST_POSITION;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_SECOND_POSITION;
import static com.tokopedia.utils.view.DarkModeUtil.isDarkMode;

public class SearchActivity extends BaseActivity
        implements
        RedirectionListener,
        SearchNavigationListener,
        SearchPerformanceMonitoringListener,
        HasComponent<BaseAppComponent> {

    private NavToolbar searchNavigationToolbar;
    private Toolbar toolbar;
    private MotionLayout container;
    private LoaderUnify loadingView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View tabShadow;
    private View quickFilterTopPadding;
    private SearchSectionPagerAdapter searchSectionPagerAdapter;
    private View backButton;
    private TextView searchTextView;
    private ImageView buttonChangeGrid;
    private ImageView buttonCart;
    private ImageView buttonHome;
    private SearchNavigationListener.ClickListener searchNavigationClickListener;

    private String productTabTitle;
    private String shopTabTitle;
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
    private boolean isABTestNavigationRevamp = false;
    private boolean isEnableChooseAddress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startPerformanceMonitoring();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_search);

        isABTestNavigationRevamp = isABTestNavigationRevamp();
        isEnableChooseAddress = getIsEnableChooseAddress();

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

    private boolean isABTestNavigationRevamp() {
        try {
            return RemoteConfigInstance.getInstance().getABTestPlatform()
                    .getString(AbTestPlatform.NAVIGATION_EXP_TOP_NAV, AbTestPlatform.NAVIGATION_VARIANT_OLD)
                    .equals(AbTestPlatform.NAVIGATION_VARIANT_REVAMP);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean getIsEnableChooseAddress() {
        try {
            return ChooseAddressUtils.INSTANCE.isRollOutUser(this);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setStatusBarColor() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!isDarkMode(this)) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0));
        }
    }

    private void getExtrasFromIntent(Intent intent) {
        searchParameter = getSearchParameterFromIntentUri(intent);

        if (TextUtils.isEmpty(searchParameter.getSearchQuery())) {
            new SearchLogger().logAnomalyNoKeyword(UrlParamUtils.generateUrlParamString(searchParameter.getSearchParameterMap()));
        }
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
        searchNavigationToolbar = findViewById(R.id.searchNavigationToolbar);
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.container);
        loadingView = findViewById(R.id.progressBar);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pager);
        backButton = findViewById(R.id.action_up_btn);
        searchTextView = findViewById(R.id.searchTextView);
        buttonChangeGrid = findViewById(R.id.search_change_grid_button);
        buttonCart = findViewById(R.id.search_cart_button);
        buttonHome = findViewById(R.id.search_home_button);
        tabShadow = findViewById(R.id.search_top_bar_shadow);
        quickFilterTopPadding = findViewById(R.id.search_quick_filter_top_padding);
    }

    protected void prepareView() {
        initToolbar();
        initViewPager();
        configureTabLayout();
    }

    private void configureTabLayout() {
        if (container == null || isLandingPage()) return;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) return;

        container.loadLayoutDescription(R.xml.search_tab_layout_scene);
        container.setTransition(R.id.searchMotionTabStart, R.id.searchMotionTabEnd);
        container.setTransitionListener(getContainerTransitionListener());
    }

    private MotionLayout.TransitionListener getContainerTransitionListener() {
        return new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) { }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) { }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) { }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) { onContainerTransitionCompleted(i); }
        };
    }

    private void onContainerTransitionCompleted(int id) {
        Fragment fragmentItem = searchSectionPagerAdapter.getRegisteredFragmentAtPosition(viewPager.getCurrentItem());
        if (!(fragmentItem instanceof QuickFilterElevation)) return;

        if (id == R.id.searchMotionTabStart) {
            ((QuickFilterElevation) fragmentItem).configure(true);
        } else if (id == R.id.searchMotionTabEnd){
            ((QuickFilterElevation) fragmentItem).configure(false);
        }
    }

    private void initToolbar() {
        if (isABTestNavigationRevamp) {
            configureSearchNavigationToolbar();
        }
        else {
            configureSupportActionBar();
            configureToolbarOnClickListener();
        }
        configureToolbarVisibility();
    }

    private void configureSearchNavigationToolbar() {
        hideToolbar();
        setSearchNavigationToolbar();
    }

    private void hideToolbar() {
        if (toolbar == null) return;

        toolbar.setVisibility(View.GONE);
    }

    private void setSearchNavigationToolbar(){
        if (searchNavigationToolbar == null) return;

        searchNavigationToolbar.bringToFront();
        searchNavigationToolbar.setToolbarPageName(SearchConstant.SEARCH_RESULT_PAGE);
        searchNavigationToolbar.setIcon(
                new IconBuilder()
                        .addIcon(IconList.ID_CART, false, false, () -> Unit.INSTANCE)
                        .addIcon(IconList.ID_NAV_GLOBAL, false, false, () -> Unit.INSTANCE)
        );
    }

    private void configureSupportActionBar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }

        if (isEnableChooseAddress) {
            buttonChangeGrid.setVisibility(View.GONE);
        } else {
            buttonChangeGrid.setVisibility(View.VISIBLE);
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
            tabShadow.setVisibility(View.GONE);
            quickFilterTopPadding.setVisibility(View.GONE);
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
        switch (position) {
            case TAB_FIRST_POSITION:
                SearchTracking.eventSearchResultTabClick(this, productTabTitle);
                break;
            case TAB_SECOND_POSITION:
                SearchTracking.eventSearchResultTabClick(this, shopTabTitle);
                break;
        }
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
    }

    private void initViewModel() {
        searchViewModel = ViewModelProviders.of(this, searchViewModelFactory).get(SearchViewModel.class);
        searchShopViewModel = ViewModelProviders.of(this, searchShopViewModelFactory).get(SearchShopViewModel.class);
    }

    private void observeViewModel() {
        observeAutoCompleteEvent();
        observeHideLoadingEvent();
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
        setSearchParameterDefaultActiveTab();
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

        return !availableSearchTabs.contains(activeTab);
    }

    protected void onSearchingStart() {
        showLoadingView(true);
        showContainer(false);
    }

    private void showLoadingView(boolean visible) {
        loadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void showContainer(boolean visible) {
        container.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setToolbarTitle(String query) {
        if (isABTestNavigationRevamp) {
            configureSearchNavigationSearchBar();
        }
        else {
            String toolbarTitle = getToolbarTitle(query);
            searchTextView.setText(toolbarTitle);
        }
    }

    private void configureSearchNavigationSearchBar(){
        String query = searchParameter.getSearchQuery();

        List<HintData> hintData = new ArrayList();
        hintData.add(new HintData(query, query));

        searchNavigationToolbar.setupSearchbar(
                hintData,
                "",
                this::onSearchNavigationSearchBarClicked,
                null,
                0,
                true,
                false
        );
    }

    private Unit onSearchNavigationSearchBarClicked(String keyword) {
        moveToAutoCompleteActivity();
        return Unit.INSTANCE;
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

        if (isABTestNavigationRevamp) setSearchNavigationCartButton();
        else showButtonCart();
    }

    @Override
    public boolean isAllowShake() {
        return false;
    }

    private void setSearchNavigationCartButton() {
        if (userSession.isLoggedIn()) {
            setSearchNavigationCartButtonCount();
        }
    }

    private void setSearchNavigationCartButtonCount() {
        if (searchNavigationToolbar == null) return;

        int cartCount = localCacheHandler.getInt(CACHE_TOTAL_CART, 0);
        searchNavigationToolbar.setBadgeCounter(IconList.ID_CART, cartCount);
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
    public void setupSearchNavigation(ClickListener clickListener, boolean isSortEnabled) {
        this.searchNavigationClickListener = clickListener;
    }

    @Override
    public void refreshMenuItemGridIcon(int titleResId, int iconResId) {
        if (isABTestNavigationRevamp) return;

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
}
