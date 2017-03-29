package com.tokopedia.discovery.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.gson.Gson;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.entity.categoriesHades.CategoriesHadesModel;
import com.tokopedia.core.network.entity.categoriesHades.Child;
import com.tokopedia.core.network.entity.categoriesHades.SimpleCategory;
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.adapter.browseparent.BrowserSectionsPagerAdapter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.dynamicfilter.DynamicFilterActivity;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterView;
import com.tokopedia.discovery.fragment.BrowseParentFragment;
import com.tokopedia.discovery.fragment.ShopFragment;
import com.tokopedia.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.discovery.interfaces.DiscoveryListener;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.presenter.BrowseView;
import com.tokopedia.discovery.search.view.DiscoverySearchView;
import com.tokopedia.discovery.search.view.fragment.SearchMainFragment;
import com.tokopedia.discovery.view.BrowseProductParentView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.core.router.CustomerRouter.IS_DEEP_LINK_SEARCH;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.AD_SRC;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRAS_SEARCH_TERM;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRA_SOURCE;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.FRAGMENT_ID;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.VALUES_INVALID_FRAGMENT_ID;
import static com.tokopedia.discovery.activity.BrowseProductActivity.FDest.FILTER;
import static com.tokopedia.discovery.activity.BrowseProductActivity.FDest.SORT;

/**
 * Created by Erry on 6/30/2016.
 */
public class BrowseProductActivity extends TActivity implements DiscoverySearchView.SearchViewListener,
        BrowseView, MenuItemCompat.OnActionExpandListener, DiscoverySearchView.OnQueryTextListener {

    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String CHANGE_GRID_ACTION_INTENT = BuildConfig.APPLICATION_ID+".LAYOUT";
    public static final String GRID_TYPE_EXTRA = "GRID_TYPE_EXTRA";

    private static final String EXTRA_BROWSE_MODEL = "EXTRA_BROWSE_MODEL";
    private static final String EXTRA_FIRST_TIME = "EXTRA_FIRST_TIME";
    private static final String EXTRA_FILTER_MAP = "EXTRA_FILTER_MAP";
    private static final String LAYOUT_GRID_DEFAULT = "1";
    private static final String LAYOUT_GRID_BOX = "2";
    private static final String LAYOUT_LIST = "3";
    private static final String SEARCH_ACTION_INTENT = BuildConfig.APPLICATION_ID+".SEARCH";
    private static final String KEY_GTM = "GTMFilterData";
    private static final String EXTRA_BROWSE_ATRIBUT = "EXTRA_BROWSE_ATRIBUT";
    private static final int REQUEST_SORT = 121;

    private int gridIcon = R.drawable.ic_grid_default;
    private BrowseProductRouter.GridType gridType = BrowseProductRouter.GridType.GRID_2;
    private boolean firstTime = true;
    private String searchQuery;
    private FragmentManager fragmentManager;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private BrowseProductAtribut mBrowseProductAtribut;
    private FilterMapAtribut mFilterMapAtribut;
    private SharedPreferences preferences;
    private List<Breadcrumb> breadcrumbs;
    private BrowseProductActivityModel browseProductActivityModel;
    private DiscoveryInteractor discoveryInteractor;
    private LocalCacheHandler cacheGTM;
    private MenuItem searchItem;
    private Stack<SimpleCategory> categoryLevel = new Stack<>();

    enum FDest {
        SORT, FILTER
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_category_new);
        ButterKnife.bind(this);

        discoveryInteractor = new DiscoveryInteractorImpl();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (savedInstanceState == null) {
            browseProductActivityModel = new BrowseProductActivityModel();
            mBrowseProductAtribut = new BrowseProductAtribut();
            mFilterMapAtribut = new FilterMapAtribut();
            fetchIntent();
            deleteFilterAndSortCache();
        } else {
            firstTime = savedInstanceState.getBoolean(EXTRA_FIRST_TIME);
            browseProductActivityModel = savedInstanceState.getParcelable(EXTRA_BROWSE_MODEL);
            mBrowseProductAtribut = savedInstanceState.getParcelable(EXTRA_BROWSE_ATRIBUT);
            if (mBrowseProductAtribut == null) mBrowseProductAtribut = new BrowseProductAtribut();

            mFilterMapAtribut = savedInstanceState.getParcelable(EXTRA_FILTER_MAP);

            if (mFilterMapAtribut != null && mFilterMapAtribut.getFiltersMap() != null) {
                FilterMapAtribut.FilterMapValue filterMapAtribut
                        = mFilterMapAtribut.getFiltersMap().get(browseProductActivityModel.getActiveTab());

                if (filterMapAtribut != null) {
                    browseProductActivityModel.setFilterOptions(filterMapAtribut.getValue());
                } else {
                    browseProductActivityModel.setFilterOptions(new HashMap<String, String>());
                }
            }
        }
        if (SessionHandler.isV4Login(this)) {
            String userId = SessionHandler.getLoginID(this);
            browseProductActivityModel.setUnique_id(AuthUtil.md5(userId));
        } else {
            browseProductActivityModel.setUnique_id(AuthUtil.md5(GCMHandler.getRegistrationId(this)));
        }
        fragmentManager = getSupportFragmentManager();
        switch (browseProductActivityModel.getSource()) {
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT:
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG:
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP:
                toolbar.setTitle("");
                toolbar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        discoverySearchView.showSearch();
                        discoverySearchView.setFinishOnClose(false);
                    }
                });
                break;
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY:
                toolbar.setTitle(getString(R.string.title_activity_browse_category));
                break;
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT:
                toolbar.setTitle(getString(R.string.title_activity_browse_hot_detail));
                break;
        }
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title != null) {
            toolbar.setTitle(title);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        cacheGTM = new LocalCacheHandler(this, KEY_GTM);

        discoverySearchView.setActivity(this);
        discoverySearchView.setOnQueryTextListener(this);
        discoverySearchView.setOnSearchViewListener(this);

        if (browseProductActivityModel.alias != null && browseProductActivityModel.getHotListBannerModel() == null) {
            fetchHotListHeader(browseProductActivityModel.alias);
        } else if (browseProductActivityModel.isSearchDeeplink()) {
            sendQuery(browseProductActivityModel.getQ());
        } else {
            switch (browseProductActivityModel.getFragmentId()) {
                case BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID:
                    int keepActivitySettings = Settings.System.getInt(getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0);
                    if (!isFragmentCreated(BrowseParentFragment.FRAGMENT_TAG) || keepActivitySettings == 1) {
                        setFragment(BrowseParentFragment.newInstance(browseProductActivityModel), BrowseParentFragment.FRAGMENT_TAG);
                    }
                    break;
                case BrowseProductRouter.VALUES_HISTORY_FRAGMENT_ID:
                    discoverySearchView.showSearch(true, false);
                    break;
            }
        }
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_BROWSE_PRODUCT_FROM_SEARCH;
    }

    public void sendHotlist(String selected, String keyword) {
        fetchHotListHeader(selected);
        browseProductActivityModel.setQ(keyword);
        browseProductActivityModel.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT);
        browseProductActivityModel.alias = selected;
    }

    public void sendCategory(String departementId) {
        browseProductActivityModel.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY);
        fetchCategoriesHeader(departementId);
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
        sendQuery(query);
        sendSearchGTM(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        return false;
    }

    @Override
    public void onSearchViewShown() {
        bottomNavigation.hideBottomNavigation();
        bottomNavigation.setBehaviorTranslationEnabled(false);
        discoverySearchView.setQuery(searchQuery, false);
    }

    @Override
    public void onSearchViewClosed() {
        bottomNavigation.restoreBottomNavigation();
        bottomNavigation.setBehaviorTranslationEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_BROWSE_MODEL, browseProductActivityModel);
        outState.putBoolean(EXTRA_FIRST_TIME, firstTime);
        outState.putParcelable(EXTRA_BROWSE_ATRIBUT, mBrowseProductAtribut);
        outState.putParcelable(EXTRA_FILTER_MAP, mFilterMapAtribut);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        browseProductActivityModel = savedInstanceState.getParcelable(EXTRA_BROWSE_MODEL);
        mBrowseProductAtribut = savedInstanceState.getParcelable(EXTRA_BROWSE_ATRIBUT);
        mFilterMapAtribut = savedInstanceState.getParcelable(EXTRA_FILTER_MAP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
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
            browseProductActivityModel.setFragmentId(BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
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

    private void sendBroadCast(String query) {
        Intent intent = new Intent(SEARCH_ACTION_INTENT);
        intent.putExtra(EXTRAS_SEARCH_TERM, query);
        sendBroadcast(intent);
    }

    public void setFilterAttribute(DataValue filterAttribute, int activeTab) {
        if (checkHasFilterAttrIsNull(activeTab))
            mBrowseProductAtribut.getFilterAttributMap().put(activeTab, filterAttribute);
    }

    @Override
    public boolean checkHasFilterAttrIsNull(int activeTab) {
        return mBrowseProductAtribut.getFilterAttributMap().get(activeTab) == null;
    }

    public void sendQuery(String query) {
        sendQuery(query, "");
    }

    public void sendQuery(String query, String depId) {
        breadcrumbs = null;
        searchQuery = query;
        resetBrowseProductActivityModel();
        browseProductActivityModel.setQ(query);
        browseProductActivityModel.setDepartmentId(depId);
        if (firstTime || browseProductActivityModel.getSource().equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT)
                || browseProductActivityModel.getSource().equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY)) {
            browseProductActivityModel.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT);
            browseProductActivityModel.setOb("23");
        }
        setFragment(BrowseParentFragment.newInstance(browseProductActivityModel), BrowseParentFragment.FRAGMENT_TAG);
        deleteFilterCache();
        sendBroadCast(query);
        toolbar.setTitle(query);

        int currentSuggestionTab = discoverySearchView.getSuggestionFragment().getCurrentTab();

        if (currentSuggestionTab == SearchMainFragment.PAGER_POSITION_PRODUCT) {
            browseProductActivityModel.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT);
        } else if (currentSuggestionTab == SearchMainFragment.PAGER_POSITION_SHOP) {
            browseProductActivityModel.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP);
        }
        discoverySearchView.setLastQuery(query);
        discoverySearchView.closeSearch();
    }

    private void deleteFilterCache() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(DynamicFilterActivity.FILTER_SELECTED_PREF);
        editor.remove(DynamicFilterActivity.FILTER_TEXT_PREF);
        editor.remove(DynamicFilterActivity.FILTER_SELECTED_POS_PREF);
        editor.apply();
        if (browseProductActivityModel != null) {
            browseProductActivityModel.setFilterOptions(new HashMap<String, String>());
        }

    }

    private void deleteFilterAndSortCache() {
        deleteFilterCache();
        browseProductActivityModel.setOb("23");
        if (mBrowseProductAtribut != null && mBrowseProductAtribut.getFilterAttributMap() != null) {
            mBrowseProductAtribut.getFilterAttributMap().clear();
        }
    }

    public void resetBrowseProductActivityModel() {
        deleteFilterAndSortCache();
        browseProductActivityModel.setAdSrc(TopAdsApi.SRC_BROWSE_PRODUCT);
        browseProductActivityModel.alias = null;
        browseProductActivityModel.setHotListBannerModel(null);
        browseProductActivityModel.removeBannerModel();
        browseProductActivityModel.setDepartmentId("");
    }

    @Override
    public BrowseProductActivityModel getBrowseProductActivityModel() {
        return browseProductActivityModel;
    }

    /**
     * @param TAG
     * @return true means fragment was already created
     */
    private boolean isFragmentCreated(String TAG) {
        return fragmentManager.findFragmentByTag(TAG) != null;
    }

    private void fetchIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            //[START] check hot list param
            browseProductActivityModel.alias
                    = intent.getStringExtra(BrowseProductRouter.EXTRAS_DISCOVERY_ALIAS);

            //[END] check hot list param
            String source = getIntent().getStringExtra(BrowseProductRouter.EXTRA_SOURCE);
            if (source != null) {
                browseProductActivityModel.setSource(source);
            }
            if (browseProductActivityModel.alias == null) {
                // get department and fragment id that would be shown.
                String departmentId = intent.getStringExtra(BrowseProductRouter.DEPARTMENT_ID);
                int fragmentId = intent.getIntExtra(FRAGMENT_ID, VALUES_INVALID_FRAGMENT_ID);
                String adSrc = intent.getStringExtra(AD_SRC);

                this.searchQuery = intent.getStringExtra(EXTRAS_SEARCH_TERM);
                browseProductActivityModel.setQ(this.searchQuery);
                // set the value get from intent
                if (adSrc != null)
                    browseProductActivityModel.setAdSrc(adSrc);
                if (departmentId != null) {
                    browseProductActivityModel.setDepartmentId(departmentId);
                    browseProductActivityModel.setParentDepartement(departmentId);
                }
                browseProductActivityModel.setFragmentId(fragmentId);
            }
            browseProductActivityModel.setSearchDeeplink(intent.getBooleanExtra(IS_DEEP_LINK_SEARCH, false));
        }
    }

    @Override
    public BrowseProductModel getDataForBrowseProduct(boolean firstTimeOnly) {
        Fragment fragment = fragmentManager.findFragmentByTag(BrowseParentFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof BrowseProductParentView) {
            return ((BrowseProductParentView) fragment).getDataForBrowseProduct(firstTimeOnly);
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

    private List<Breadcrumb> getProductBreadCrumb() {
        Fragment fragment = fragmentManager.findFragmentByTag(BrowseParentFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof BrowseProductParentView) {
            return ((BrowseProductParentView) fragment).getProductBreadCrumb();
        } else {
            return null;
        }
    }

    public void changeBottomBar(String source) {
        browseProductActivityModel.setSource(source);
        switch (source) {
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP:
                setupBottomBar(getBottomItemsShop(), source);
                break;
            default:
                setupBottomBar(getBottomItemsAll(), source);
        }
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
                Intent intent;
                DataValue filterAttribute
                        = mBrowseProductAtribut.getFilterAttributMap().get(parentFragment.getActiveTab());
                switch (position) {
                    case 0:
                        if (parentFragment.getActiveFragment() instanceof ShopFragment) {
                            openFilter(filterAttribute, source, parentFragment.getActiveTab());
                        } else {
                            openSort(filterAttribute, source, parentFragment.getActiveTab());
                        }
                        break;
                    case 1:
                        openFilter(filterAttribute, source, parentFragment.getActiveTab());
                        break;
                    case 2:
                        intent = new Intent(CHANGE_GRID_ACTION_INTENT);
                        switch (gridType) {
                            case GRID_1:
                                gridType = BrowseProductRouter.GridType.GRID_2;
                                gridIcon = R.drawable.ic_grid_default;
                                bottomNavigation.getItem(2).setTitle(getString(R.string.grid));
                                UnifyTracking.eventDisplayCategory(LAYOUT_GRID_DEFAULT);
                                break;
                            case GRID_2:
                                gridType = BrowseProductRouter.GridType.GRID_3;
                                gridIcon = R.drawable.ic_grid_box;
                                bottomNavigation.getItem(2).setTitle(getString(R.string.grid));
                                UnifyTracking.eventDisplayCategory(LAYOUT_GRID_BOX);
                                break;
                            case GRID_3:
                                gridType = BrowseProductRouter.GridType.GRID_1;
                                gridIcon = R.drawable.ic_list;
                                bottomNavigation.getItem(2).setTitle(getString(R.string.list));
                                UnifyTracking.eventDisplayCategory(LAYOUT_LIST);
                                break;
                            default:
                                gridIcon = R.drawable.ic_grid_default;
                                bottomNavigation.getItem(2).setTitle(getString(R.string.grid));
                        }
                        intent.putExtra(GRID_TYPE_EXTRA, gridType);
                        sendBroadcast(intent);
                        bottomNavigation.getItem(position).setDrawable(gridIcon);
                        bottomNavigation.refresh();
                        break;
                    case 3:
                        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
                        if (fragment != null && fragment instanceof BrowseParentFragment) {
                            String shareUrl = ((BrowseParentFragment) fragment).getProductShareUrl();
                            if (!shareUrl.isEmpty()) {
                                Intent sintent = new Intent(BrowseProductActivity.this, ShareActivity.class);
                                ShareData shareData = ShareData.Builder.aShareData()
                                        .setType(ShareData.DISCOVERY_TYPE)
                                        .setName(getString(R.string.message_share_catalog))
                                        .setTextContent(getString(R.string.message_share_category))
                                        .setUri(shareUrl)
                                        .build();
                                if (browseProductActivityModel.getSource().equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY)) {
                                    shareData.setType(ShareData.CATEGORY_TYPE);
                                    shareData.setDescription(browseProductActivityModel.getDepartmentId());
                                }
                                sintent.putExtra(ShareData.TAG, shareData);
                                startActivity(sintent);
                            }
                        }
                        break;
                }
                return true;
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
        if (firstTime) {
            if (!source.contains("shop")) {
                bottomNavigation.setCurrentItem(0, false);
            }
            firstTime = false;
        }
    }

    private void openSort(DataValue filterAttribute, String source, int activeTab) {
        if (filterAttribute != null) {
            if (browseProductActivityModel.getOb() != null) {
                filterAttribute.setSelectedOb(browseProductActivityModel.getOb());
            }
            Intent intent = new Intent(BrowseProductActivity.this, SortProductActivity.class);
            intent.putExtra(EXTRA_DATA, (Parcelable) filterAttribute);
            intent.putExtra(EXTRA_SOURCE, source);
            startActivityForResult(intent, REQUEST_SORT);
            overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
        } else {
            fetchDynamicAttribute(activeTab, source, SORT);
        }
    }

    private void openFilter(DataValue filterAttribute, String source, int activeTab) {
        breadcrumbs = getProductBreadCrumb();
        if (filterAttribute != null ) {
            Map<String, String> filters;
            if (mFilterMapAtribut != null && mFilterMapAtribut.getFiltersMap() != null) {
                if (mFilterMapAtribut.getFiltersMap().get(source) != null) {
                    filters = mFilterMapAtribut.getFiltersMap().get(source).getValue();
                } else {
                    filters = new HashMap<>();
                }
            } else {
                filters = new HashMap<>();
            }
            DynamicFilterActivity.moveTo(BrowseProductActivity.this,
                    filters, breadcrumbs,
                    filterAttribute.getFilter(),
                    browseProductActivityModel.getParentDepartement(), source);

        } else {
            fetchDynamicAttribute(activeTab, source, FILTER);
        }
    }

    private void fetchDynamicAttribute(final int activeTab, final String source, final FDest dest) {
        discoveryInteractor.setDiscoveryListener(new DiscoveryListener() {
            @Override
            public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {
            }

            @Override
            public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
                Toast.makeText(BrowseProductActivity.this, getString(R.string.try_again), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
                switch (type) {
                    case DYNAMIC_ATTRIBUTE:
                        DynamicFilterModel.DynamicFilterContainer dynamicFilterContainer
                                = (DynamicFilterModel.DynamicFilterContainer) data.getModel2();

                        DataValue filterAtrribute = dynamicFilterContainer.body().getData();
                        if (filterAtrribute.getSort() != null) {
                            filterAtrribute.setSelected(filterAtrribute.getSort().get(0).getName());
                        }
                        setFilterAttribute(filterAtrribute, activeTab);
                        switch (dest) {
                            case FILTER:
                                openFilter(filterAtrribute, source, activeTab);
                                break;
                            case SORT:
                                openSort(filterAtrribute, source, activeTab);
                                break;
                        }
                        break;
                }
            }
        });
        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);
        if (source.contains("catalog")) {
            discoveryInteractor.getDynamicAttribute(this, BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG, browseProductActivityModel.getDepartmentId());
        } else if (source.contains("shop")) {
            discoveryInteractor.getDynamicAttribute(this, BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP, browseProductActivityModel.getDepartmentId());
        } else if (source.contains("directory") && activeTab == 0) {
            discoveryInteractor.getDynamicAttribute(this, BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY, browseProductActivityModel.getDepartmentId());
        } else if (source.contains("directory") && activeTab == 1) {
            discoveryInteractor.getDynamicAttribute(this, BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG, browseProductActivityModel.getDepartmentId());
        } else {
            discoveryInteractor.getDynamicAttribute(this, BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT, browseProductActivityModel.getDepartmentId());
        }
    }

    private List<AHBottomNavigationItem> getBottomItemsShop() {
        List<AHBottomNavigationItem> items = new ArrayList<>();
        items.add(new AHBottomNavigationItem(getString(R.string.filter), R.drawable.ic_filter_list_black));
        return items;
    }

    private List<AHBottomNavigationItem> getBottomItemsAll() {
        List<AHBottomNavigationItem> items = new ArrayList<>();
        items.add(new AHBottomNavigationItem(getString(R.string.sort), R.drawable.ic_sort_black));
        items.add(new AHBottomNavigationItem(getString(R.string.filter), R.drawable.ic_filter_list_black));
        items.add(new AHBottomNavigationItem(getString(R.string.grid), gridIcon));
        items.add(new AHBottomNavigationItem(getString(R.string.share), R.drawable.ic_share_black));
        return items;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            BrowseParentFragment parentFragment = (BrowseParentFragment) fragmentManager.findFragmentByTag(BrowseParentFragment.FRAGMENT_TAG);
            switch (requestCode) {
                case REQUEST_SORT:
                    DataValue sortData = data.getParcelableExtra(BrowseParentFragment.SORT_EXTRA);
                    mBrowseProductAtribut.getFilterAttributMap().put(browseProductActivityModel.getActiveTab(), sortData);
                    String newOb = sortData.getSelectedOb();
                    if (browseProductActivityModel.getActiveTab() == 1) {
                        browseProductActivityModel.setObCatalog(newOb);
                    } else {
                        browseProductActivityModel.setOb(newOb);
                    }

                    if (browseProductActivityModel.getHotListBannerModel() != null) {
                        browseProductActivityModel.getHotListBannerModel().query.ob = browseProductActivityModel.getOb();
                    }
                    sendSortGTM(browseProductActivityModel.getOb());
                    break;
                case DynamicFilterView.REQUEST_CODE:
                    FilterMapAtribut.FilterMapValue filterMapValue =
                            data.getParcelableExtra(DynamicFilterView.EXTRA_FILTERS);
                    mFilterMapAtribut.getFiltersMap()
                            .put(browseProductActivityModel.getActiveTab(), filterMapValue);
                    browseProductActivityModel.setFilterOptions(filterMapValue.getValue());
                    sendFilterGTM(filterMapValue.getValue());
                    break;
            }
            setFragment(BrowseParentFragment.newInstance(browseProductActivityModel, parentFragment.getActiveTab()), BrowseParentFragment.FRAGMENT_TAG);
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

    public void showLoading(boolean isLoading) {
        progressBar.setIndeterminate(isLoading);
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void showEmptyState(NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        NetworkErrorHelper.showEmptyState(BrowseProductActivity.this, container, retryClickedListener);
        if (bottomNavigation != null) {
            bottomNavigation.hideBottomNavigation();
        }
    }

    public void removeEmptyState() {
        NetworkErrorHelper.removeEmptyState(coordinatorLayout);
        NetworkErrorHelper.removeEmptyState(container);
        if (bottomNavigation != null && bottomNavigation.isHidden()) {
            bottomNavigation.restoreBottomNavigation();
        }
    }

    private void fetchHotListHeader(final String alias) {
        HashMap<String, String> query = new HashMap<>();
        query.put("key", alias);
        showLoading(true);
        discoveryInteractor.setDiscoveryListener(new DiscoveryListener() {
            @Override
            public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {
                showLoading(false);
            }

            @Override
            public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
                showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        fetchHotListHeader(alias);
                    }
                });
            }

            @Override
            public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
                switch (type) {
                    case DiscoveryListener.HOTLIST_BANNER:
                        ObjContainer model2 = data.getModel2();
                        HotListBannerModel.HotListBannerContainer hotListBannerContainer = (HotListBannerModel.HotListBannerContainer) model2;
                        HotListBannerModel body = hotListBannerContainer.body();
                        if (browseProductActivityModel.getOb() != null) {
                            body.query.ob = browseProductActivityModel.getOb();
                        }
                        Map<String, String> filters;

                        if (browseProductActivityModel != null) {
                            filters = browseProductActivityModel.getFilterOptions();
                            for (Map.Entry<String, String> set : filters.entrySet()) {
                                if (set.getKey().equals("ob")) {
                                    body.query.ob = set.getValue();
                                }
                            }
                        } else {
                            filters = new HashMap<String, String>();
                            filters.put("sc", body.query.sc);
                            ArrayMap<String, Boolean> selectedPositions = new ArrayMap<>();
                            List<String> scList = new ArrayList<String>();
                            if (body.query.sc.contains(",")) {
                                for (String s : body.query.sc.split(",")) {
                                    scList.add(s);
                                }
                            } else {
                                scList.add(body.query.sc);
                            }
                            for (String s : scList) {
                                selectedPositions.put(s, true);
                            }
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(DynamicFilterActivity.FILTER_SELECTED_POS_PREF, new Gson().toJson(selectedPositions));
                            editor.apply();
                            editor.putString(DynamicFilterActivity.FILTER_SELECTED_PREF, new Gson().toJson(filters));
                            editor.apply();
                        }

                        FilterMapAtribut.FilterMapValue filterMapValue
                                = new FilterMapAtribut.FilterMapValue();
                        filterMapValue.setValue((HashMap<String, String>) filters);
                        mFilterMapAtribut.getFiltersMap()
                                .put(browseProductActivityModel.getActiveTab(), filterMapValue);


                        browseProductActivityModel.setFilterOptions(filters);
                        browseProductActivityModel.setOb(body.query.ob);
                        browseProductActivityModel.setHotListBannerModel(body);
                        Fragment fragment = BrowseParentFragment.newInstance(browseProductActivityModel);

                        setFragment(fragment, BrowseParentFragment.FRAGMENT_TAG);
                        break;
                }
            }
        });
        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);
        discoveryInteractor.getHotListBanner(query);
    }

    private void fetchCategoriesHeader(final String departementId) {
        showLoading(true);
        discoveryInteractor.setDiscoveryListener(new DiscoveryListener() {
            @Override
            public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {
                showLoading(false);
            }

            @Override
            public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
                showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        fetchCategoriesHeader(departementId);
                    }
                });
            }

            @Override
            public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
                switch (type) {
                    case DiscoveryListener.CATEGORY_HEADER:
                        BrowseParentFragment parentFragment = (BrowseParentFragment)
                                fragmentManager.findFragmentById(R.id.container);
                        if (parentFragment!=null) {
                            ObjContainer objContainer = data.getModel2();
                            CategoriesHadesModel.CategoriesHadesContainer categoriesHadesContainer = (CategoriesHadesModel.CategoriesHadesContainer) objContainer;
                            CategoriesHadesModel body = categoriesHadesContainer.body();
                            if (browseProductActivityModel !=null && body !=null && body.getData() !=null) {
                                browseProductActivityModel.categotyHeader = body.getData();
                                parentFragment.renderCategories(browseProductActivityModel.categotyHeader);
                            }
                        }
                        break;
                }
            }
        });
        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);
        discoveryInteractor.getCategoryHeader(departementId);
    }


    private void sendFilterGTM(Map<String, String> maps) {
        String sortFilterData = "";
        if (TextUtils.isEmpty(cacheGTM.getString(KEY_GTM)) || cacheGTM.isExpired()) {
            sortFilterData = TrackingUtils.getGtmString(AppEventTracking.GTM.FILTER_SORT);
            if (TextUtils.isEmpty(sortFilterData))
                return;
            cacheGTM.putString(KEY_GTM, sortFilterData);
            cacheGTM.setExpire(86400);
            cacheGTM.applyEditor();
        } else {
            sortFilterData = cacheGTM.getString(KEY_GTM);
        }

        try {
            JSONObject jsonObject = new JSONObject(sortFilterData);
            JSONArray dynamicFilter = jsonObject.getJSONArray("dynamic_filter");
            String filteredKey = jsonObject.getString("dynamic_filter_key");
            for (Map.Entry<String, String> map : maps.entrySet()) {
                if (filteredKey.contains(map.getKey())) {
                    for (int i = 0; i < dynamicFilter.length(); i++) {
                        JSONObject item = (JSONObject) dynamicFilter.get(i);
                        if (item.getString("key").equalsIgnoreCase(map.getKey())) {
                            if (TextUtils.isEmpty(item.getString("value")) ||
                                    item.getString("value").equalsIgnoreCase(map.getValue())) {
                                UnifyTracking.eventDiscoveryFilter(item.getString("label"));
                                if (browseProductActivityModel.getSource().equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY)) {
                                    UnifyTracking.eventFilterCategory(item.getString("label"));
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendSortGTM(String valueSort) {
        String sortFilterData = "";
        if (TextUtils.isEmpty(cacheGTM.getString(KEY_GTM)) || cacheGTM.isExpired()) {
            sortFilterData = TrackingUtils.getGtmString(AppEventTracking.GTM.FILTER_SORT);
            if (TextUtils.isEmpty(sortFilterData))
                return;
            cacheGTM.putString(KEY_GTM, sortFilterData);
            cacheGTM.setExpire(86400);
            cacheGTM.applyEditor();
        } else {
            sortFilterData = cacheGTM.getString(KEY_GTM);
        }

        try {
            JSONObject jsonObject = new JSONObject(sortFilterData);
            JSONArray dynamicSort = jsonObject.getJSONArray("dynamic_sort");
            for (int i = 0; i < dynamicSort.length(); i++) {
                JSONObject item = (JSONObject) dynamicSort.get(i);
                if (item.getString("value").equalsIgnoreCase(valueSort)) {
                    UnifyTracking.eventDiscoverySort(item.getString("label"));
                    if (browseProductActivityModel.getSource().equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY)) {
                        UnifyTracking.eventSortCategory(item.getString("label"));
                    }
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendSearchGTM(String keyword) {
        if (keyword != null &&
                !TextUtils.isEmpty(keyword)) {
            UnifyTracking.eventDiscoverySearch(keyword);
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
        return gridType;
    }

    private void renderNewCategoryLevel(String departementId, String name) {
        if (departementId!=null && name!=null) {
            browseProductActivityModel.setDepartmentId(departementId);
            toolbar.setTitle(name);
            setFragment(BrowseParentFragment.newInstance(browseProductActivityModel), BrowseParentFragment.FRAGMENT_TAG);
            BrowseParentFragment parentFragment = (BrowseParentFragment)
                    fragmentManager.findFragmentById(R.id.container);
            ArrayMap<String, String> visibleTab = new ArrayMap<>();
            visibleTab.put(BrowserSectionsPagerAdapter.PRODUK, BrowseProductParentView.VISIBLE_ON);
            parentFragment.initSectionAdapter(visibleTab);
            parentFragment.setupWithTabViewPager();
        }
    }

    public void renderLowerCategoryLevel(Child child) {
        categoryLevel.push(new SimpleCategory(browseProductActivityModel.getDepartmentId(),getIntent().getStringExtra(EXTRA_TITLE)));
        getIntent().putExtra(EXTRA_TITLE,child.getName());
        renderNewCategoryLevel(child.getId(),child.getName());

    }

    @Override
    public void onBackPressed() {
        if (discoverySearchView.isSearchOpen()) {
            if(discoverySearchView.isFinishOnClose()){
                finish();
            } else {
                discoverySearchView.closeSearch();
            }
        } else if (categoryLevel.size()>0) {
            SimpleCategory simpleCategory = categoryLevel.pop();
            getIntent().putExtra(EXTRA_TITLE,simpleCategory.getName());
            renderNewCategoryLevel(simpleCategory.getId(),simpleCategory.getName());
        } else {
            finish();
        }

    }


}
