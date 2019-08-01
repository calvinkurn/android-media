package com.tokopedia.search.result.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.data.Filter;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.base.BottomSheetListener;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.search.adapter.SearchSectionPagerAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.NetworkParamHelper;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.newdiscovery.search.model.SearchSectionItem;
import com.tokopedia.discovery.newdiscovery.widget.BottomSheetFilterView;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.SearchContract;
import com.tokopedia.search.result.presentation.view.fragment.CatalogListFragment;
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment;
import com.tokopedia.search.result.presentation.view.fragment.ProfileListFragment;
import com.tokopedia.search.result.presentation.view.fragment.SearchSectionFragment;
import com.tokopedia.search.result.presentation.view.fragment.ShopListFragment;
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener;
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.discovery.common.constants.SearchConstant.AUTO_COMPLETE_ACTIVITY_REQUEST_CODE;
import static com.tokopedia.discovery.common.constants.SearchConstant.AUTO_COMPLETE_ACTIVITY_RESULT_CODE_START_ACTIVITY;
import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_ACTIVE_TAB_POSITION;
import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_FORCE_SWIPE_TO_SHOP;
import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_SEARCH_PARAMETER_MODEL;
import static com.tokopedia.discovery.common.constants.SearchConstant.GCM_ID;
import static com.tokopedia.discovery.common.constants.SearchConstant.GCM_STORAGE;
import static com.tokopedia.discovery.common.constants.SearchConstant.SEARCH_RESULT_TRACE;

public class SearchActivity extends BaseActivity
        implements SearchContract.View,
        RedirectionListener,
        BottomSheetListener,
        SearchNavigationListener,
        HasComponent<BaseAppComponent> {

    private static final int TAB_FIRST_POSITION = 0;
    private static final int TAB_THIRD_POSITION = 2;
    private static final int TAB_SECOND_POSITION = 1;

    @DeepLink(ApplinkConst.DISCOVERY_SEARCH)
    public static Intent getCallingApplinkSearchIntent(Context context, Bundle bundle) {
        Uri uri = Uri.parse(bundle.getString(DeepLink.URI));

        if(uri == null) return new Intent();

        return RouteManager.getIntent(context, ApplinkConstInternalDiscovery.SEARCH_RESULT + uri.getEncodedQuery());
    }

    private ProductListFragment productListFragment;
    private CatalogListFragment catalogListFragment;
    private ShopListFragment shopListFragment;
    private ProfileListFragment profileListFragment;

    private Toolbar toolbar;
    private FrameLayout container;
    private ProgressBar loadingView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchSectionPagerAdapter searchSectionPagerAdapter;
    private TextView buttonFilter;
    private TextView buttonSort;
    private View iconFilter;
    private View iconSort;
    private View searchNavDivider;
    private View searchNavContainer;
    private BottomSheetFilterView bottomSheetFilterView;
    private SearchNavigationListener.ClickListener searchNavigationClickListener;

    private String productTabTitle;
    private String catalogTabTitle;
    private String shopTabTitle;
    private String profileTabTitle;
    private boolean isForceSwipeToShop;
    private boolean isHasCatalog;
    private int activeTabPosition;

    @Inject SearchContract.Presenter searchPresenter;
    @Inject SearchTracking searchTracking;
    @Inject UserSessionInterface userSession;

    private SearchViewComponent searchComponent;
    private MenuItem menuChangeGrid;
    private PerformanceMonitoring performanceMonitoring;
    private SearchParameter searchParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_search);
        initActivityOnCreate();
        proceed();
        handleIntent(getIntent());
    }

    private void initActivityOnCreate() {
        GraphqlClient.init(this);
        initInjector();
    }

    private void initInjector() {
        searchComponent =
                DaggerSearchViewComponent.builder()
                        .baseAppComponent(getBaseAppComponent())
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
        iconFilter = findViewById(R.id.icon_filter);
        buttonSort = findViewById(R.id.button_sort);
        iconSort = findViewById(R.id.icon_sort);
        searchNavDivider = findViewById(R.id.search_nav_divider);
        searchNavContainer = findViewById(R.id.search_nav_container);
    }

    protected void prepareView() {
        initToolbar();
        initViewPager();
        initBottomSheetListener();
        initSearchNavigationListener();
        bottomSheetFilterView.initFilterBottomSheet();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        toolbar.setOnClickListener(v -> moveToAutoCompleteActivity());
    }

    private void moveToAutoCompleteActivity() {
        ActivityCompat.startActivityForResult(
                this,
                getAutoCompleteIntent(),
                AUTO_COMPLETE_ACTIVITY_REQUEST_CODE,
                getOptionsForTransitionAnimation().toBundle());
    }

    private Intent getAutoCompleteIntent() {
        SearchParameter autoCompleteSearchParameter = new SearchParameter();
        autoCompleteSearchParameter.setSearchQuery(searchParameter.getSearchQuery());

        Intent intent = RouteManager.getIntent(this, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE);
        intent.putExtra(EXTRA_SEARCH_PARAMETER_MODEL, autoCompleteSearchParameter);

        return intent;
    }

    private ActivityOptionsCompat getOptionsForTransitionAnimation() {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(this, toolbar, SearchConstant.TRANSITION);
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                bottomSheetFilterView.closeView();
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
        this.isForceSwipeToShop = false;
        this.activeTabPosition = position;
    }

    private void initBottomSheetListener() {
        bottomSheetFilterView.setCallback(new BottomSheetFilterView.Callback() {
            @Override
            public void onApplyFilter(Map<String, String> filterParameter) {
                applyFilter(filterParameter);
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
            public boolean isSearchShown() {
                return false;
            }

            @Override
            public void hideKeyboard() {
                KeyboardHandler.hideSoftKeyboard(SearchActivity.this);
            }

            @Override
            public AppCompatActivity getActivity() {
                return SearchActivity.this;
            }
        });
    }

    private void applyFilter(Map<String, String> filterParameter) {
        SearchSectionFragment selectedFragment
                = (SearchSectionFragment) searchSectionPagerAdapter.getItem(viewPager.getCurrentItem());

        selectedFragment.applyFilterToSearchParameter(filterParameter);
        selectedFragment.setSelectedFilter(new HashMap<>(filterParameter));
        selectedFragment.clearDataFilterSort();
        selectedFragment.reloadData();
    }

    @Override
    public void hideBottomNavigation() {
        searchNavContainer.setVisibility(View.GONE);
    }

    public void showBottomNavigation() {
        searchNavContainer.setVisibility(View.VISIBLE);
    }

    private void sendBottomSheetHideEventForProductList() {
        SearchSectionFragment selectedFragment
                = (SearchSectionFragment) searchSectionPagerAdapter.getItem(viewPager.getCurrentItem());

        if (selectedFragment instanceof ProductListFragment) {
            selectedFragment.onBottomSheetHide();
        }
    }

    private void initSearchNavigationListener() {
        buttonFilter.setOnClickListener(view -> {
            if (searchNavigationClickListener != null) {
                searchNavigationClickListener.onFilterClick();
            }
        });
        iconFilter.setOnClickListener(view -> {
            if (searchNavigationClickListener != null) {
                searchNavigationClickListener.onFilterClick();
            }
        });
        buttonSort.setOnClickListener(view -> {
            if (searchNavigationClickListener != null) {
                searchNavigationClickListener.onSortClick();
            }
        });
        iconSort.setOnClickListener(view -> {
            if (searchNavigationClickListener != null) {
                searchNavigationClickListener.onSortClick();
            }
        });
    }

    private void handleIntent(Intent intent) {
        initPresenter();
        initResources();
        getExtrasFromIntent(intent);
        performProductSearch("");
        setToolbarTitle(searchParameter.getSearchQuery());
    }

    private void initPresenter() {
        searchPresenter.attachView(this);
        searchPresenter.initInjector(this);
    }

    private void initResources() {
        productTabTitle = getString(R.string.product_tab_title);
        catalogTabTitle = getString(R.string.catalog_tab_title);
        shopTabTitle = getString(R.string.shop_tab_title);
        profileTabTitle = getString(R.string.title_profile);
    }

    private void getExtrasFromIntent(Intent intent) {
        searchParameter = getSearchParameterFromIntentUri(intent);
        isForceSwipeToShop = intent.getBooleanExtra(EXTRA_FORCE_SWIPE_TO_SHOP, false);
    }

    private SearchParameter getSearchParameterFromIntentUri(Intent intent) {
        Uri uri = intent.getData();

        return (uri == null) ? new SearchParameter() : new SearchParameter(uri.toString());
    }

    private void performProductSearch(String queryParams) {
        updateSearchParameterBeforeSearch(queryParams);
        onSearchingStart();
        performanceMonitoring = PerformanceMonitoring.start(SEARCH_RESULT_TRACE);

        searchPresenter.initiateSearch(searchParameter.getSearchParameterMap());
    }

    private void updateSearchParameterBeforeSearch(String queryParams) {
        searchParameter.getSearchParameterHashMap().putAll(NetworkParamHelper.getParamMap(queryParams));
        setSearchParameterUniqueId();
        setSearchParameterUserIdIfLoggedIn();
    }

    private void setSearchParameterUniqueId() {
        String uniqueId = userSession.isLoggedIn() ?
                AuthUtil.md5(userSession.getUserId()) :
                AuthUtil.md5(getRegistrationId(this));

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(query);
        }
    }

    @Override
    public void initiateSearchHandleResponseSearch(boolean isHasCatalog) {
        stopPerformanceMonitoring();
        setHasCatalog(isHasCatalog);
        loadSection();
    }

    protected void stopPerformanceMonitoring() {
        if (performanceMonitoring != null) {
            performanceMonitoring.stopTrace();
        }
    }

    private void setHasCatalog(boolean isHasCatalog) {
        this.isHasCatalog = isHasCatalog;
    }

    private void loadSection() {
        List<SearchSectionItem> searchSectionItemList = new ArrayList<>();
        populateTab(searchSectionItemList);

        searchSectionPagerAdapter = new SearchSectionPagerAdapter(getSupportFragmentManager());
        searchSectionPagerAdapter.setData(searchSectionItemList);
        viewPager.setAdapter(searchSectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setActiveTab();
    }

    private void populateTab(List<SearchSectionItem> searchSectionItemList) {
        initFragments();
        addFragmentsToList(searchSectionItemList);
        initTabLayout();
    }

    private void initFragments() {
        productListFragment = getProductFragment();
        if(isHasCatalog) catalogListFragment = getCatalogFragment();
        shopListFragment = getShopFragment();
        profileListFragment = getProfileListFragment();
    }

    private ProductListFragment getProductFragment() {
        return ProductListFragment.newInstance(searchParameter);
    }

    private ShopListFragment getShopFragment() {
        return ShopListFragment.newInstance(searchParameter);
    }

    private CatalogListFragment getCatalogFragment() {
        return CatalogListFragment.newInstance(searchParameter);
    }

    private ProfileListFragment getProfileListFragment() {
        return ProfileListFragment.Companion.newInstance(searchParameter.getSearchQuery(), this, this);
    }

    private void addFragmentsToList(List<SearchSectionItem> searchSectionItemList) {
        searchSectionItemList.add(new SearchSectionItem(productTabTitle, productListFragment));
        if(isHasCatalog) searchSectionItemList.add(new SearchSectionItem(catalogTabTitle, catalogListFragment));
        searchSectionItemList.add(new SearchSectionItem(shopTabTitle, shopListFragment));
        searchSectionItemList.add(new SearchSectionItem(profileTabTitle, profileListFragment));
    }

    private void initTabLayout() {
        tabLayout.clearOnTabSelectedListeners();
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                SearchActivity.this.onTabReselected(tab.getPosition());
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                SearchActivity.this.onTabSelected(tab.getPosition());
            }
        });
    }

    private void onTabReselected(int tabPosition) {
        switch (tabPosition) {
            case TAB_FIRST_POSITION:
                productListFragment.backToTop();
                break;
            case TAB_SECOND_POSITION:
                if(isHasCatalog) catalogListFragment.backToTop();
                else shopListFragment.backToTop();
                break;
            case TAB_THIRD_POSITION:
                if(isHasCatalog) shopListFragment.backToTop();
                break;
        }
    }

    private void onTabSelected(int tabPosition) {
        switch (tabPosition) {
            case TAB_FIRST_POSITION:
                SearchTracking.eventSearchResultTabClick(this, productTabTitle);
                break;
            case TAB_SECOND_POSITION:
                if(isHasCatalog) SearchTracking.eventSearchResultTabClick(this, catalogTabTitle);
                else SearchTracking.eventSearchResultTabClick(this, shopTabTitle);
                break;
            case TAB_THIRD_POSITION:
                if(isHasCatalog) SearchTracking.eventSearchResultTabClick(this, shopTabTitle);
                break;
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
        if(isForceSwipeToShop) return getShopTabPosition();
        else return activeTabPosition;
    }

    private int getShopTabPosition() {
        return isHasCatalog ? 2 : 1;
    }

    @Override
    public void initiateSearchHandleApplink(@NonNull String applink) {
        moveWithApplink(applink);
    }
    public void moveWithApplink(String applink) {
        startActivityWithApplink(applink);
        finish();
    }

    @Override
    public void startActivityWithApplink(String applink, String... parameter) {
        finishCurrentActivityIfRedirectedToSearch(applink);

        Intent intent = RouteManager.getIntent(this, applink, parameter);
        int startActivityForResultRequestCode = getStartActivityForResultRequestCode(applink);

        startActivityForResult(intent, startActivityForResultRequestCode);
    }

    private void finishCurrentActivityIfRedirectedToSearch(String applink) {
        if(isApplinkToSearchActivity(applink)) {
            setIntent(new Intent());
            finish();
        }
    }

    private boolean isApplinkToSearchActivity(String applink) {
        return !TextUtils.isEmpty(applink)
                && applink.startsWith(ApplinkConst.DISCOVERY_SEARCH + "?");
    }

    private int getStartActivityForResultRequestCode(String applink) {
        if(isApplinkToAutoCompleteActivity(applink)) {
            return AUTO_COMPLETE_ACTIVITY_REQUEST_CODE;
        }

        return -1;
    }

    private boolean isApplinkToAutoCompleteActivity(String applink) {
        return !TextUtils.isEmpty(applink)
                && applink.startsWith(ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE + "?");
    }

    @Override
    public void initiateSearchHandleResponseError() {
        NetworkErrorHelper.showEmptyState(SearchActivity.this, container, () -> {
            if(searchParameter == null) return;
            SearchActivity.this.performProductSearch("");
        });
    }

    @Override
    public void initiateSearchHandleResponseUnknown() {
        throw new RuntimeException("Not yet handle unknown response");
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchPresenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchPresenter.onResume();
        unregisterShake();
    }

    @Override
    protected void onDestroy() {
        searchPresenter.onDestroy();
        searchPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        restartActivityWithoutAnimation(intent);
    }

    private void restartActivityWithoutAnimation(Intent intent) {
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case AUTO_COMPLETE_ACTIVITY_REQUEST_CODE:
                handleResultFromAutoCompleteActivity(resultCode);
                break;
            default:
                handleDefaultActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void handleResultFromAutoCompleteActivity(int resultCode) {
        switch(resultCode) {
            case AUTO_COMPLETE_ACTIVITY_RESULT_CODE_START_ACTIVITY:
                finish();
                overridePendingTransition(0, 0);
                break;
        }
    }

    private void handleDefaultActivityResult(int requestCode, int resultCode, Intent data) {
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
        outState.putBoolean(EXTRA_FORCE_SWIPE_TO_SHOP, isForceSwipeToShop);
        outState.putInt(EXTRA_ACTIVE_TAB_POSITION, activeTabPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        searchParameter = savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER_MODEL);
        isForceSwipeToShop = savedInstanceState.getBoolean(EXTRA_FORCE_SWIPE_TO_SHOP);
        activeTabPosition = savedInstanceState.getInt(EXTRA_ACTIVE_TAB_POSITION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change_grid, menu);
        menuChangeGrid = menu.findItem(R.id.action_change_grid);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_grid) {
            changeGrid();
            return true;
        }
        else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (item.getItemId() == R.id.action_search) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeGrid() {
        if (searchNavigationClickListener != null) {
            searchNavigationClickListener.onChangeGridClick();
        }
    }

    @Override
    public void performNewProductSearch(String queryParams) {
        this.searchParameter = new SearchParameter();
        this.isForceSwipeToShop = false;
        performProductSearch(queryParams);
    }

    @Override
    public void onProductLoadingFinished() {
        showLoadingView(false);
        showContainer(true);
        showBottomNavigation();
    }

    @Override
    public void showSearchInputView() {
        moveToAutoCompleteActivity();
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
    public void closeFilterBottomSheet() {
        bottomSheetFilterView.closeView();
    }

    @Override
    public boolean isBottomSheetShown() {
        return bottomSheetFilterView.isBottomSheetShown();
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
            iconSort.setVisibility(View.VISIBLE);
            searchNavDivider.setVisibility(View.VISIBLE);
        } else {
            buttonSort.setVisibility(View.GONE);
            iconSort.setVisibility(View.GONE);
            searchNavDivider.setVisibility(View.GONE);
        }

        this.searchNavigationClickListener = clickListener;
    }

    @Override
    public void refreshMenuItemGridIcon(int titleResId, int iconResId) {
        if (menuChangeGrid != null) {
            menuChangeGrid.setIcon(iconResId);
            menuChangeGrid.setTitle(titleResId);
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
}