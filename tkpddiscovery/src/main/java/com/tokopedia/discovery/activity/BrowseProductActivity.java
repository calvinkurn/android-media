package com.tokopedia.discovery.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.entity.intermediary.Child;
import com.tokopedia.core.network.entity.intermediary.SimpleCategory;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.discovery.BuildConfig;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.adapter.browseparent.BrowserSectionsPagerAdapter;
import com.tokopedia.discovery.categorynav.view.CategoryNavigationActivity;
import com.tokopedia.discovery.fragment.BrowseParentFragment;
import com.tokopedia.discovery.fragment.ProductFragment;
import com.tokopedia.discovery.fragment.ShopFragment;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.newdynamicfilter.RevampedDynamicFilterActivity;
import com.tokopedia.discovery.presenter.BrowsePresenter;
import com.tokopedia.discovery.presenter.BrowsePresenterImpl;
import com.tokopedia.discovery.presenter.BrowseView;
import com.tokopedia.discovery.search.view.DiscoverySearchView;
import com.tokopedia.discovery.view.BrowseProductParentView;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.core.router.discovery.BrowseProductRouter.AD_SRC;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRAS_SEARCH_TERM;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRA_SOURCE;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRA_TITLE;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.FRAGMENT_ID;

/**
 * Created by Erry on 6/30/2016.
 */
public class BrowseProductActivity extends TActivity implements DiscoverySearchView.SearchViewListener,
        BrowseView, MenuItemCompat.OnActionExpandListener, DiscoverySearchView.OnQueryTextListener, ProductFragment.ProductFragmentListener {

    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String CHANGE_GRID_ACTION_INTENT = BuildConfig.APPLICATION_ID + ".LAYOUT";
    public static final String GRID_TYPE_EXTRA = "GRID_TYPE_EXTRA";
    public static final String TAG_SHOWCASE_BOTTOM_NAV = "-SHOWCASE_BOTTOM_NAVIGATION";
    public static final int REQUEST_SORT = 121;
    private static final String SEARCH_ACTION_INTENT = BuildConfig.APPLICATION_ID + ".SEARCH";
    private static final int BOTTOM_BAR_GRID_TYPE_ITEM_POSITION = 2;

    private int gridIcon = R.drawable.ic_grid_default;
    private int gridTitleRes = R.string.grid;
    private FragmentManager fragmentManager;
    private BrowsePresenter browsePresenter;
    private MenuItem searchItem;

    private ShowCaseDialog showCaseDialog;

    @BindView(R2.id.progressBar)
    ProgressBar progressBar;
    @BindView(R2.id.root)
    CoordinatorLayout coordinatorLayout;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.bottom_navigation_container)
    AHBottomNavigation bottomNavigation;
    @BindView(R2.id.container)
    FrameLayout container;
    @BindView(R2.id.search)
    DiscoverySearchView discoverySearchView;

    private static HashMap<String, String> convertBundleToMaps(Bundle bundle) {
        HashMap<String, String> maps = new HashMap<>();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            maps.put(key, String.valueOf(bundle.get(key)));
        }
        return maps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_category_new);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        browsePresenter = new BrowsePresenterImpl(
                this,
                new DiscoveryInteractorImpl(),
                PreferenceManager.getDefaultSharedPreferences(this)
        );
        browsePresenter.initPresenterData(savedInstanceState, getIntent());
    }

    @Override
    public void initDiscoverySearchView(String lastQuery) {
        discoverySearchView.setActivity(this);
        discoverySearchView.setOnQueryTextListener(this);
        discoverySearchView.setOnSearchViewListener(this);
        discoverySearchView.setLastQuery(lastQuery);
    }

    @Override
    public void initToolbar(String title, boolean isClickable) {
        toolbar.setTitle(title);

        if (isClickable) {
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    discoverySearchView.showSearch();
                    discoverySearchView.setFinishOnClose(false);
                }
            });
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_BROWSE_PRODUCT_FROM_SEARCH;
    }

    public void sendHotlist(String selected, String keyword) {
        browsePresenter.sendHotlist(selected, keyword);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        boolean redirectToOtherPage = sendQuery(query);
        sendSearchGTM(query);
        return redirectToOtherPage;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        return false;
    }

    @Override
    public void onSearchViewShown() {
        bottomNavigation.hideBottomNavigation();
        bottomNavigation.setBehaviorTranslationEnabled(false);
        discoverySearchView.setQuery(browsePresenter.getSearchQuery(), false);
    }

    @Override
    public void onSearchViewClosed() {
        bottomNavigation.restoreBottomNavigation();
        bottomNavigation.setBehaviorTranslationEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        browsePresenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        browsePresenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        browsePresenter.disposePresenterData();
        super.onDestroy();
    }

    public void setFragment(Fragment fragment, String TAG) {
        if (isFinishing()) return;
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        if (fragment instanceof BrowseParentFragment) {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
            CommonUtils.hideKeyboard(this, getCurrentFocus());
        } else {
            params.setScrollFlags(0);
        }
        toolbar.setLayoutParams(params);
        toolbar.requestLayout();
        String backStateName = fragment.getClass().getName();

        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.container, fragment, TAG);
            ft.addToBackStack(backStateName);
            ft.commit();
            browsePresenter.onSetFragment(BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
        }
        Runtime.getRuntime().gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchItem = menu.findItem(R.id.action_search);
        discoverySearchView.setMenuItem(searchItem);
        return true;
    }

    public void setSearchQuery(String query) {
        discoverySearchView.setQuery(query, false, true);
        CommonUtils.hideKeyboard(this, getCurrentFocus());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setFilterAttribute(DataValue filterAttribute, int activeTab) {
        browsePresenter.setFilterAttribute(filterAttribute, activeTab);
    }

    @Override
    public void showFailedFetchAttribute() {
        CommonUtils.UniversalToast(BrowseProductActivity.this, getString(R.string.toast_try_again));
    }

    @Override
    public boolean checkHasFilterAttrIsNull(int activeTab) {
        return browsePresenter.checkHasFilterAttributeIsNull(activeTab);
    }

    public boolean sendQuery(String query) {
        boolean redirectToOtherPage = sendQuery(query, "");
        return redirectToOtherPage;
    }

    public boolean sendQuery(String query, String depId) {
        removeEmptyState();
        boolean redirectToOtherPage = browsePresenter.sendQuery(query, depId);
        if (!redirectToOtherPage) {
            toolbar.setTitle(query);
            discoverySearchView.setLastQuery(query);
            discoverySearchView.closeSearch();
        }
        return redirectToOtherPage;
    }

    public void resetBrowseProductActivityModel() {
        browsePresenter.resetBrowseProductActivityModel();
    }

    @Override
    public BrowseProductActivityModel getBrowseProductActivityModel() {
        return browsePresenter.getBrowseProductActivityModel();
    }

    /**
     * @param TAG
     * @return true means fragment was already created
     */
    @Override
    public boolean isFragmentCreated(String TAG) {
        return fragmentManager.findFragmentByTag(TAG) != null;
    }

    @Override
    public BrowseProductModel getDataForBrowseProduct() {
        Fragment fragment = fragmentManager.findFragmentByTag(BrowseParentFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof BrowseProductParentView) {
            return ((BrowseProductParentView) fragment).getDataForBrowseProduct();
        } else {
            return null;
        }
    }

    @Override
    public NetworkParam.Product getProductParam() {
        Fragment fragment = fragmentManager.findFragmentByTag(BrowseParentFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof BrowseProductParentView) {
            return ((BrowseProductParentView) fragment).getProductParam();
        } else {
            return null;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    private List<Breadcrumb> getProductBreadCrumb() {
        Fragment fragment = fragmentManager.findFragmentByTag(BrowseParentFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof BrowseProductParentView) {
            return ((BrowseProductParentView) fragment).getProductBreadCrumb();
        } else {
            return null;
        }
    }

    public void changeBottomBar(String source) {
        browsePresenter.onBottomBarChanged(source);
    }

    private void setupBottomBar(List<AHBottomNavigationItem> items, final String source) {
        // Create items
        bottomNavigation.setBackgroundResource(R.drawable.bottomtab_background);
        bottomNavigation.removeAllItems();
        bottomNavigation.addItems(items);
        bottomNavigation.setForceTitlesDisplay(true);
        bottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.tkpd_dark_green));
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(final int position, boolean wasSelected) {
                BrowseParentFragment parentFragment = (BrowseParentFragment)
                        fragmentManager.findFragmentById(R.id.container);
                boolean isShopFragment = parentFragment.getActiveFragment() instanceof ShopFragment;

                return browsePresenter.onBottomBarTabSelected(source,
                        position, parentFragment.getActiveTab(), isShopFragment);
            }
        });
        bottomNavigation.setUseElevation(true, getResources().getDimension(R.dimen.bottom_navigation_elevation));
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) container.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, y);
                container.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public void openSort(DataValue filterAttribute, String source) {
        Intent intent = new Intent(BrowseProductActivity.this, SortProductActivity.class);
        intent.putExtra(EXTRA_DATA, (Parcelable) filterAttribute);
        intent.putExtra(EXTRA_SOURCE, source);
        startActivityForResult(intent, REQUEST_SORT);
        overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
    }

    @Override
    public void openFilter(DataValue filterAttribute,
                           String source,
                           String parentDepartment,
                           String departmentId,
                           Map<String, String> filters) {
        RevampedDynamicFilterActivity.moveTo(BrowseProductActivity.this,
                filterAttribute.getFilter());
    }

    @Override
    public void openCategoryNavigation(
            String departmentId) {
        CategoryNavigationActivity.moveTo(BrowseProductActivity.this, departmentId);

    }

    private List<AHBottomNavigationItem> getBottomItemsShop() {
        List<AHBottomNavigationItem> items = new ArrayList<>();
        items.add(new AHBottomNavigationItem(getString(R.string.filter), R.drawable.ic_filter_list_black));
        items.add(new AHBottomNavigationItem(getString(gridTitleRes), gridIcon));
        return items;
    }

    private List<AHBottomNavigationItem> getBottomItemsAll() {
        List<AHBottomNavigationItem> items = new ArrayList<>();
        items.add(new AHBottomNavigationItem(getString(R.string.sort), R.drawable.ic_sort_black));
        items.add(new AHBottomNavigationItem(getString(R.string.filter), R.drawable.ic_filter_list_black));
        items.add(new AHBottomNavigationItem(getString(gridTitleRes), gridIcon));
        if (!browsePresenter.isFromCategory()) {
            items.add(new AHBottomNavigationItem(getString(R.string.share), R.drawable.ic_share_black));
        } else {
            items.add(new AHBottomNavigationItem(getString(R.string.title_category), R.drawable.ic_category_black));
        }
        return items;
    }

    private void startShowCase() {
        final String showCaseTag = BrowseProductActivity.class.getName()+TAG_SHOWCASE_BOTTOM_NAV;
        if (ShowCasePreference.hasShown(BrowseProductActivity.this, showCaseTag) || showCaseDialog != null) {
            return;
        }
        showCaseDialog = createShowCase();
        showCaseDialog.setShowCaseStepListener(new ShowCaseDialog.OnShowCaseStepListener() {
            @Override
            public boolean onShowCaseGoTo(int previousStep, int nextStep, ShowCaseObject showCaseObject) {
                return false;
            }
        });

        Rect rectToShowCase = new Rect();
        bottomNavigation.getGlobalVisibleRect(rectToShowCase);

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();
        showCaseObjectList.add(new ShowCaseObject(
                bottomNavigation,
                getResources().getString(R.string.choose_category),
                getResources().getString(R.string.choose_category_desc),
                ShowCaseContentPosition.UNDEFINED,
                R.color.tkpd_main_green).withCustomTarget(new int[]{ rectToShowCase.right-(bottomNavigation.getWidth()/bottomNavigation.getItemsCount()),
                rectToShowCase.top-(int)getResources().getDimension(R.dimen.bottom_navigation_height_elevation), rectToShowCase.right, rectToShowCase.bottom}));
        showCaseDialog.show(BrowseProductActivity.this, showCaseTag, showCaseObjectList);
    }

    private ShowCaseDialog createShowCase() {
        return new ShowCaseBuilder()
                .customView(R.layout.view_onboarding_category_nav)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .textSizeRes(R.dimen.fontvs)
                .finishStringRes(R.string.title_done)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .arrowWidth(R.dimen.category_nav_showcase_arrow)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SORT:
                case RevampedDynamicFilterActivity.REQUEST_CODE:
                    browsePresenter.handleResultData(requestCode, data);
                    BrowseParentFragment parentFragment = (BrowseParentFragment)
                            fragmentManager.findFragmentByTag(BrowseParentFragment.FRAGMENT_TAG);
                    setFragment(BrowseParentFragment.newInstance(browsePresenter
                                    .getBrowseProductActivityModel(), parentFragment.getActiveTab()),
                            BrowseParentFragment.FRAGMENT_TAG);
                    break;
                case DiscoverySearchView.REQUEST_VOICE:
                    List<String> results = data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);
                    if (results != null && results.size() > 0) {
                        discoverySearchView.setQuery(results.get(0), false);
                        sendVoiceSearchGTM(results.get(0));
                    }
                    break;
            }
        } else if (resultCode == CategoryNavigationActivity.DESTROY_BROWSE_PARENT) {
            setResult(CategoryNavigationActivity.DESTROY_INTERMEDIARY);
            finish();
        }
    }

    /**
     * @param context
     * @param depId
     * @param ad_src  {@value TopAdsApi#SRC_BROWSE_PRODUCT} for example
     */
    public static void moveTo(Context context, String depId, String ad_src) {
        moveTo(context, depId, ad_src, BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT, null);
    }

    public static void moveTo(Context context, String depId, String ad_src, String source, String title) {
        if (context == null)
            return;

        Intent intent = new Intent(context, BrowseProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, depId);
        bundle.putInt(FRAGMENT_ID, BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
        bundle.putString(AD_SRC, ad_src);
        bundle.putString(EXTRA_SOURCE, source);
        if (title != null) {
            bundle.putString(EXTRA_TITLE, title);
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void moveToFromIntermediary(FragmentActivity activity, String depId, String ad_src, String source, String title) {
        if (activity == null)
            return;

        Intent intent = new Intent(activity, BrowseProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, depId);
        bundle.putInt(FRAGMENT_ID, BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
        bundle.putString(AD_SRC, ad_src);
        bundle.putString(EXTRA_SOURCE, source);
        if (title != null) {
            bundle.putString(EXTRA_TITLE, title);
        }
        intent.putExtras(bundle);
        activity.startActivityForResult(intent,CategoryNavigationActivity.DESTROY_INTERMEDIARY);
    }

    public static void moveToWithoutAnimation(Context context, String depId, String ad_src, String source, String title) {
        if (context == null) return;

        Intent intent = new Intent(context, BrowseProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, depId);
        bundle.putInt(FRAGMENT_ID, BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
        bundle.putString(AD_SRC, ad_src);
        bundle.putString(EXTRA_SOURCE, source);
        if (title != null) {
            bundle.putString(EXTRA_TITLE, title);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    public void showLoading(boolean isLoading) {
        progressBar.setIndeterminate(isLoading);
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyState(NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        NetworkErrorHelper.showEmptyState(BrowseProductActivity.this, container, retryClickedListener);
        if (bottomNavigation != null) {
            bottomNavigation.hideBottomNavigation();
        }
    }

    @Override
    public void setupShopItemsBottomBar(String source) {
        setupBottomBar(getBottomItemsShop(), source);
    }

    @Override
    public void setupAllItemsBottomBar(String source) {
        setupBottomBar(getBottomItemsAll(), source);

        if (browsePresenter.isFromCategory()) {
            startShowCase();
        }
    }

    @Override
    public void setFocusOnBottomBarFirstItem() {
        bottomNavigation.setCurrentItem(0, false);
    }

    @Override
    public void close() {
        if (isTaskRoot() && GlobalConfig.isSellerApp()) {
            startActivity(SellerAppRouter.getSellerHomeActivity(this));
            finish();
        } else if (isTaskRoot()) {
            startActivity(HomeRouter.getHomeActivity(this));
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void showBrowseParentFragment(BrowseProductActivityModel browseModel) {
        setFragment(BrowseParentFragment.newInstance(browseModel), BrowseParentFragment.FRAGMENT_TAG);
    }

    @Override
    public void sendQueryBroadcast(String query) {
        Intent intent = new Intent(SEARCH_ACTION_INTENT);
        intent.putExtra(EXTRAS_SEARCH_TERM, query);
        sendBroadcast(intent);
    }

    @Override
    public void sendChangeGridBroadcast(BrowseProductRouter.GridType gridType) {
        Intent intent = new Intent(CHANGE_GRID_ACTION_INTENT);
        intent.putExtra(GRID_TYPE_EXTRA, gridType);
        sendBroadcast(intent);
    }

    @Override
    public void renderUpperCategoryLevel(SimpleCategory simpleCategory) {
        browsePresenter.onRenderUpperCategoryLevel(simpleCategory.getId());
        getIntent().putExtra(EXTRA_TITLE, simpleCategory.getName());
        renderNewCategoryLevel(simpleCategory.getId(), simpleCategory.getName(), true);
    }

    @Override
    public int getCurrentSuggestionTab() {
        return discoverySearchView.getSuggestionFragment().getCurrentTab();
    }

    @Override
    public void changeBottomBarGridIcon(int gridIconResId, int gridTitleResId) {
        gridIcon = gridIconResId;
        gridTitleRes = gridTitleResId;

        BrowseParentFragment parentFragment = (BrowseParentFragment)
                fragmentManager.findFragmentById(R.id.container);
        boolean isShopFragment = parentFragment.getActiveFragment() instanceof ShopFragment;

        int viewTypeItemPosition;
        if (isShopFragment) {
            viewTypeItemPosition = 1;
        } else {
            viewTypeItemPosition = BOTTOM_BAR_GRID_TYPE_ITEM_POSITION;
        }

        if (isBottomBarItemReady(viewTypeItemPosition)) {
            bottomNavigation.getItem(viewTypeItemPosition).setTitle(getString(gridTitleResId));
            bottomNavigation.getItem(viewTypeItemPosition).setDrawable(gridIconResId);
            bottomNavigation.refresh();
        }
    }

    private boolean isBottomBarItemReady(int itemIndex) {
        return itemIndex < bottomNavigation.getItemsCount() &&
                bottomNavigation.getItem(itemIndex) != null;
    }

    @Override
    public void showSearchPage() {
        discoverySearchView.showSearch(true, false);
    }

    @Override
    public void startShareActivity(ShareData shareData) {
       startActivity(ShareActivity.createIntent(BrowseProductActivity.this,shareData));
    }

    @Override
    public String getShareUrl() {
        String shareUrl;
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment != null && fragment instanceof BrowseParentFragment) {
            shareUrl = ((BrowseParentFragment) fragment).getProductShareUrl();
        } else {
            shareUrl = "";
        }
        return shareUrl;
    }

    @Override
    public String getSource() {
        if (browsePresenter != null && browsePresenter.getBrowseProductActivityModel() != null) {
            return browsePresenter.getBrowseProductActivityModel().getSource();
        } else {
            return "";
        }
    }

    @Override
    public void setDefaultGridTypeFromNetwork(Integer viewType) {
        browsePresenter.setDefaultGridTypeFromNetwork(viewType);
    }

    @Override
    public void launchOfficialStorePage() {
        startActivity(BrandsWebViewActivity.newInstance(this, TkpdBaseURL.OfficialStore.URL_WEBVIEW));
        finish();
    }

    public void removeEmptyState() {
        NetworkErrorHelper.removeEmptyState(coordinatorLayout);
        NetworkErrorHelper.removeEmptyState(container);
        if (bottomNavigation != null && bottomNavigation.isHidden()) {
            bottomNavigation.restoreBottomNavigation();
        }
    }

    private void sendSearchGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventDiscoverySearch(keyword);
        }
    }

    private void sendVoiceSearchGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventDiscoveryVoiceSearch(keyword);
        }
    }

    public void showBottomBar() {
        bottomNavigation.restoreBottomNavigation(true);
    }

    public void deleteRecentSearch(String keyword) {
        discoverySearchView.getSuggestionFragment().deleteRecentSearch(keyword);
    }

    public void deleteAllRecentSearch() {
        discoverySearchView.getSuggestionFragment().deleteAllRecentSearch();
    }

    public BrowseProductRouter.GridType getGridType() {
        return browsePresenter.getGridType();
    }

    private void renderNewCategoryLevel(String departementId, String name, boolean isBack) {
        if (departementId != null) {
            getBrowseProductActivityModel().setQ("");
            String toolbarTitle;
            if (name != null) {
                toolbarTitle = name;
            } else {
                toolbarTitle = getString(R.string.title_activity_browse_category);
            }
            toolbar.setTitle(toolbarTitle);
            setFragment(
                    BrowseParentFragment.newInstance(browsePresenter.getBrowseProductActivityModel()),
                    BrowseParentFragment.FRAGMENT_TAG
            );
            BrowseParentFragment parentFragment = (BrowseParentFragment)
                    fragmentManager.findFragmentById(R.id.container);
            ArrayMap<String, String> visibleTab = new ArrayMap<>();
            visibleTab.put(BrowserSectionsPagerAdapter.PRODUK, BrowseProductParentView.VISIBLE_ON);
            parentFragment.initSectionAdapter(visibleTab);
            browsePresenter.retrieveLastGridConfig(departementId);
        }
    }

    public void renderLowerCategoryLevel(Child child) {
        browsePresenter.onRenderLowerCategoryLevel(
                child.getId(), child.getName(), getIntent().getStringExtra(EXTRA_TITLE));
        getIntent().putExtra(EXTRA_TITLE, child.getName());
        renderNewCategoryLevel(child.getId(), child.getName(), false);
    }


    @Override
    public void onBackPressed() {
        if (discoverySearchView.isSearchOpen()) {
            if (discoverySearchView.isFinishOnClose()) {
               // finish();
                close();
            } else {
                discoverySearchView.closeSearch();
            }
        } else {
            browsePresenter.onBackPressed();
        }
    }

    @Override
    public String getDepartmentId() {
        if (browsePresenter != null) {
            return browsePresenter.getBrowseProductActivityModel().getDepartmentId();
        }

        return null;
    }
}
