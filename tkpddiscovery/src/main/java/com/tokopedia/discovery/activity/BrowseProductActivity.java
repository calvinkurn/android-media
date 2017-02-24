package com.tokopedia.discovery.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.tokopedia.core.R;
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
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.dynamicfilter.DynamicFilterActivity;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterView;
import com.tokopedia.discovery.fragment.browseparent.BrowseParentFragment;
import com.tokopedia.discovery.fragment.browseparent.ShopFragment;
import com.tokopedia.discovery.fragment.history.SearchHistoryFragment;
import com.tokopedia.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.discovery.interactor.SearchInteractor;
import com.tokopedia.discovery.interactor.SearchInteractorImpl;
import com.tokopedia.discovery.interfaces.DiscoveryListener;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.presenter.DiscoveryActivityPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.core.router.CustomerRouter.IS_DEEP_LINK_SEARCH;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.AD_SRC;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRAS_DISCOVERY_ALIAS;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRAS_SEARCH_TERM;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRA_SOURCE;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.FRAGMENT_ID;
import static com.tokopedia.core.router.discovery.BrowseProductRouter.VALUES_INVALID_FRAGMENT_ID;

/**
 * Created by Erry on 6/30/2016.
 */
public class BrowseProductActivity extends TActivity implements SearchView.OnQueryTextListener,
        DiscoveryActivityPresenter, MenuItemCompat.OnActionExpandListener {

    private static final String TAG = BrowseProductActivity.class.getSimpleName();
    private static final String KEY_GTM = "GTMFilterData";
    private static final String EXTRA_BROWSE_ATRIBUT = "EXTRA_BROWSE_ATRIBUT";
    @BindView(R2.id.progressBar)
    ProgressBar progressBar;
    private SearchView searchView;
    private String searchQuery;
    private FragmentManager fragmentManager;
    private SearchInteractor searchInteractor;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private BrowseProductAtribut mBrowseProductAtribut;
    private FilterMapAtribut mFilterMapAtribut;
    private SharedPreferences preferences;
    private List<Breadcrumb> breadcrumbs;
    private boolean afterRestoreSavedInstance;
    private Subscription querySubscription;
    private QueryListener queryListener;

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
        fetchCategoriesHeader(departementId);
        browseProductActivityModel.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY);
        browseProductActivityModel.setDepartmentId(departementId);
    }


    public enum FDest {
        SORT, FILTER;
    }


    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_BROWSE_MODEL = "EXTRA_BROWSE_MODEL";
    public static final String EXTRA_FIRST_TIME = "EXTRA_FIRST_TIME";
    public static final String EXTRA_FILTER_MAP = "EXTRA_FILTER_MAP";
    public static final String EXTRA_FILTER_MAP_ATTR = "EXTRA_FILTER_MAP_ATTR";
    public static String browseType;
    private int gridIcon = R.drawable.ic_grid_default;
    private BrowseProductRouter.GridType gridType = BrowseProductRouter.GridType.GRID_2;
    private Fragment mLastFragment;
    private int keepActivitySettings;
    private boolean firstTime = true;
    @BindView(R2.id.root)
    CoordinatorLayout coordinatorLayout;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;
    @BindView(R2.id.container)
    FrameLayout container;
    BrowseProductActivityModel browseProductActivityModel;
    DiscoveryInteractor discoveryInteractor;
    LocalCacheHandler cacheGTM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        discoveryInteractor = new DiscoveryInteractorImpl();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        keepActivitySettings = Settings.System.getInt(getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0);
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
        setContentView(R.layout.activity_browse_category_new);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        switch (browseProductActivityModel.getSource()) {
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT:
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG:
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP:
                toolbar.setTitle(getString(R.string.search_product));
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);

        compositeSubscription.add(querySubscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                queryListener = new QueryListener() {
                    @Override
                    public void onQueryChanged(String query) {
                        subscriber.onNext(query);
                    }
                };
            }
        })
                .debounce(150, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        if (s != null) {
                            Log.d(TAG, "Sending the text " + s);
                            sendBroadCast(s);
                        }
                    }
                }));

        if (browseProductActivityModel.alias != null && browseProductActivityModel.getHotListBannerModel() == null)
            fetchHotListHeader(browseProductActivityModel.alias);

        if (browseProductActivityModel.isSearchDeeplink()) {
            sendQuery(browseProductActivityModel.getQ());
        } else {
            switch (browseProductActivityModel.getFragmentId()) {
                case BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID:
                    if (!isFragmentCreated(BrowseParentFragment.FRAGMENT_TAG) || keepActivitySettings == 1) {
                        setFragment(BrowseParentFragment.newInstance(browseProductActivityModel), BrowseParentFragment.FRAGMENT_TAG);
                    }
                    break;
                case BrowseProductRouter.VALUES_HISTORY_FRAGMENT_ID:
                    if (!isFragmentCreated(SearchHistoryFragment.FRAGMENT_TAG)) {
                        setFragment(SearchHistoryFragment.newInstance(), SearchHistoryFragment.FRAGMENT_TAG);
                    }
                    break;
            }
        }
        if (searchView != null) {
            searchView.clearFocus();
        }
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
        afterRestoreSavedInstance = true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    @Override
    protected void onDestroy() {
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

        Log.d(TAG, "setFragment TAG " + TAG);
        fragmentManager.beginTransaction().replace(R.id.container, fragment, TAG).addToBackStack(null).commit();
        if (fragment instanceof BrowseParentFragment) {
            bottomNavigation.setBehaviorTranslationEnabled(true);
            bottomNavigation.restoreBottomNavigation();
            browseProductActivityModel.setFragmentId(BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
        } else {
            bottomNavigation.hideBottomNavigation();
            bottomNavigation.setBehaviorTranslationEnabled(false);
            browseProductActivityModel.setFragmentId(BrowseProductRouter.VALUES_HISTORY_FRAGMENT_ID);
            showLoading(false);
        }
        Runtime.getRuntime().gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browse_category_v2, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.action_search));
        SearchView.SearchAutoComplete mSearchSrcTextView =
                (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        mSearchSrcTextView.setTextColor(getResources().getColor(R.color.white));
        mSearchSrcTextView.setHintTextColor(getResources().getColor(R.color.white));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        MenuItemCompat.setActionView(searchItem, searchView);
        switch (browseProductActivityModel.getSource()) {
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG:
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT:
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP:
                searchItem.expandActionView();
                break;
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT:
                searchItem.setVisible(false);
                break;
        }
        if (browseProductActivityModel.isSearchDeeplink()) {
            searchView.setQuery(browseProductActivityModel.getQ(), false);
            CommonUtils.hideKeyboard(this, getCurrentFocus());
            browseProductActivityModel.setSearchDeeplink(false);
        }
        if (CommonUtils.isFinishActivitiesOptionEnabled(this)) {
            searchView.clearFocus();
        }
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        if (fragmentManager.findFragmentById(R.id.container) instanceof BrowseParentFragment && !browseProductActivityModel.isSearchDeeplink() && !afterRestoreSavedInstance) {
            setFragment(SearchHistoryFragment.newInstance(), SearchHistoryFragment.FRAGMENT_TAG);
        }
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        } else if (item.getItemId() == R.id.action_search) {
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        sendQuery(query);
        sendSearchGTM(query);
        return true;
    }

    private void saveQueryCache(final String query) {
        this.searchQuery = query;
        SearchInteractor.GetSearchCacheListener listener = new SearchInteractor.GetSearchCacheListener() {
            @Override
            public void onSuccess(List<String> cacheListener) {
                if (cacheListener.contains(query)) {
                    cacheListener.remove(query);
                }
                cacheListener.add(query);
                searchInteractor.storeSearchCache(cacheListener);
            }

            @Override
            public void onError(Throwable e) {

            }
        };
        searchInteractor = new SearchInteractorImpl();
        searchInteractor.setListener(listener);
        searchInteractor.setCompositeSubscription(compositeSubscription);
        searchInteractor.getSearchCache();
    }

    private void sendBroadCast(String query) {
        Intent intent = new Intent(SEARCH_ACTION_INTENT);
        intent.putExtra(EXTRAS_SEARCH_TERM, query);
        sendBroadcast(intent);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.searchQuery = newText;
        if (!newText.isEmpty() && fragmentManager.findFragmentById(R.id.container)
                instanceof BrowseParentFragment && !browseProductActivityModel.isSearchDeeplink()) {

            setFragment(SearchHistoryFragment.newInstance(newText), SearchHistoryFragment.FRAGMENT_TAG);
        } else {
            queryListener.onQueryChanged(newText);
        }
        return false;
    }

    public void setFilterAttribute(DataValue filterAttribute, int activeTab) {
        if (checkHasFilterAttrIsNull(activeTab))
//            filterAttributMap.put(activeTab, filterAttribute);
            mBrowseProductAtribut.getFilterAttributMap().put(activeTab, filterAttribute);
    }

    @Override
    public boolean checkHasFilterAttrIsNull(int activeTab) {
//        return filterAttributMap.get(activeTab) == null;
        return mBrowseProductAtribut.getFilterAttributMap().get(activeTab) == null;
    }


    public void sendQuery(String query) {
        breadcrumbs = null;
        saveQueryCache(query);
        resetBrowseProductActivityModel();
        browseProductActivityModel.setQ(query);
        if (firstTime || browseProductActivityModel.getSource().equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT)
                || browseProductActivityModel.getSource().equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY)) {
            browseProductActivityModel.setSource(BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT);
            browseProductActivityModel.setOb("23");
        }
        setFragment(BrowseParentFragment.newInstance(browseProductActivityModel), BrowseParentFragment.FRAGMENT_TAG);
        deleteFilterCache();
        sendBroadCast(query);
        if (searchView != null) {
            searchView.setQuery(query, false);
            searchView.setFocusable(false);
        }
        CommonUtils.hideKeyboard(this, getCurrentFocus());
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

    public void deleteFilterAndSortCache() {
        deleteFilterCache();
        browseProductActivityModel.setOb("23");
//        filterAttributMap.clear();
        if (mBrowseProductAtribut != null && mBrowseProductAtribut.getFilterAttributMap() != null) {
            mBrowseProductAtribut.getFilterAttributMap().clear();
        }
    }

    private void resetBrowseProductActivityModel() {
        deleteFilterAndSortCache();
        browseProductActivityModel.setAdSrc(TopAdsApi.SRC_BROWSE_PRODUCT);
        browseProductActivityModel.alias = null;
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
    @Override
    public boolean isFragmentCreated(String TAG) {
        return fragmentManager.findFragmentByTag(TAG) != null;
    }

    @Override
    public void fetchIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            //[START] check hot list param
            browseProductActivityModel.alias
                    = intent.getStringExtra(BrowseProductRouter.EXTRAS_DISCOVERY_ALIAS);

            //[END] check hot list param
            String source = getIntent().getStringExtra(BrowseProductRouter.EXTRA_SOURCE);
            if (source != null) {
                browseProductActivityModel.setSource(source);
                browseType = source;
            } else {
                browseType = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;
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
                browseProductActivityModel.setDepartmentId(departmentId);
                browseProductActivityModel.setParentDepartement(departmentId);
                browseProductActivityModel.setFragmentId(fragmentId);
            }
            browseProductActivityModel.setSearchDeeplink(intent.getBooleanExtra(IS_DEEP_LINK_SEARCH, false));
        }
    }

    @Override
    public BrowseProductModel getDataForBrowseProduct(boolean firstTimeOnly) {
        Fragment fragment = fragmentManager.findFragmentByTag(BrowseParentFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof BrowseParentFragment) {
            return ((BrowseParentFragment) fragment).discoveryActivityPresenter.getDataForBrowseProduct(firstTimeOnly);
        } else {
            return null;
        }
    }

    @Override
    public NetworkParam.Product getProductParam() {
        Fragment fragment = fragmentManager.findFragmentByTag(BrowseParentFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof BrowseParentFragment) {
            return ((BrowseParentFragment) fragment).discoveryActivityPresenter.getProductParam();
        } else {
            return null;
        }
    }

    @Override
    public List<Breadcrumb> getProductBreadCrumb() {
        Fragment fragment = fragmentManager.findFragmentByTag(BrowseParentFragment.FRAGMENT_TAG);
        if (fragment != null && fragment instanceof BrowseParentFragment) {
            return ((BrowseParentFragment) fragment).discoveryActivityPresenter.getProductBreadCrumb();
        } else {
            return null;
        }
    }


    public void clearQuery() {
        searchView.setQuery("", false);
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

    public AHBottomNavigation getBottomNavigation() {
        return bottomNavigation;
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
                            openFilter(filterAttribute, source, parentFragment.getActiveTab(), FDest.FILTER);
                        } else {
                            openSort(filterAttribute, source, parentFragment.getActiveTab(), FDest.SORT);
                        }
                        break;
                    case 1:
                        openFilter(filterAttribute, source, parentFragment.getActiveTab(), FDest.FILTER);
                        break;
                    case 2:
                        intent = new Intent(CHANGE_GRID_ACTION_INTENT);
                        switch (gridType) {
                            case GRID_1:
                                gridType = BrowseProductRouter.GridType.GRID_2;
                                gridIcon = R.drawable.ic_grid_default;
                                bottomNavigation.getItem(2).setTitle(getString(R.string.grid));
                                break;
                            case GRID_2:
                                gridType = BrowseProductRouter.GridType.GRID_3;
                                gridIcon = R.drawable.ic_grid_box;
                                bottomNavigation.getItem(2).setTitle(getString(R.string.grid));
                                break;
                            case GRID_3:
                                gridType = BrowseProductRouter.GridType.GRID_1;
                                gridIcon = R.drawable.ic_list;
                                bottomNavigation.getItem(2).setTitle(getString(R.string.list));
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

    private void openSort(DataValue filterAttribute, String source, int activeTab, FDest dest) {
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
            fetchDynamicAttribute(activeTab, source, dest);
        }
    }

    private void openFilter(DataValue filterAttribute, String source, int activeTab, FDest dest) {
        Log.d(TAG, "openFilter source " + source);
        breadcrumbs = getProductBreadCrumb();
        if (filterAttribute != null && breadcrumbs != null) {
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
            fetchDynamicAttribute(activeTab, source, dest);
        }
    }

    private void fetchDynamicAttribute(final int activeTab, final String source, final FDest dest) {
        Log.d(TAG, "Source " + source);
        discoveryInteractor.setDiscoveryListener(new DiscoveryListener() {
            @Override
            public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {
                Log.d(TAG, "onComplete type " + type);
            }

            @Override
            public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
                Log.e(TAG, "onFailed type " + type);
                Toast.makeText(BrowseProductActivity.this, getString(R.string.try_again), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
                Log.d(TAG, "onSuccess type " + type);
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
                                openFilter(filterAtrribute, source, activeTab, dest);
                                break;
                            case SORT:
                                openSort(filterAtrribute, source, activeTab, dest);
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
            Log.d(TAG,"get dynamic filter product");
            discoveryInteractor.getDynamicAttribute(this, BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY, browseProductActivityModel.getDepartmentId());
        } else if (source.contains("directory") && activeTab == 1) {
            Log.d(TAG,"get dynamic filter catalog");
            discoveryInteractor.getDynamicAttribute(this, BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG, browseProductActivityModel.getDepartmentId());
        } else {
            discoveryInteractor.getDynamicAttribute(this, BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT, browseProductActivityModel.getDepartmentId());
        }
    }

    private List<AHBottomNavigationItem> getBottomItemsShop() {
        List<AHBottomNavigationItem> items = new ArrayList<>();
        items.add(new AHBottomNavigationItem(getString(R.string.filter), R.drawable.ic_filter_list_black_24dp));
        return items;
    }

    private List<AHBottomNavigationItem> getBottomItemsAll() {
        List<AHBottomNavigationItem> items = new ArrayList<>();
        items.add(new AHBottomNavigationItem(getString(R.string.sort), R.drawable.ic_sort_black_24dp));
        items.add(new AHBottomNavigationItem(getString(R.string.filter), R.drawable.ic_filter_list_black_24dp));
        items.add(new AHBottomNavigationItem(getString(R.string.grid), gridIcon));
        items.add(new AHBottomNavigationItem(getString(R.string.share), R.drawable.ic_share_black_24dp));
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
                    String source = data.getStringExtra(BrowseParentFragment.SOURCE_EXTRA);
//                    filterAttributMap.put(browseProductActivityModel.getActiveTab(), sortData);
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
                    Log.d(TAG, "filter option " + filterMapValue.getValue());
                    sendFilterGTM(filterMapValue.getValue());
                    break;
            }
            setFragment(BrowseParentFragment.newInstance(browseProductActivityModel, parentFragment.getActiveTab()), BrowseParentFragment.FRAGMENT_TAG);
        }
    }

    public static Intent getDefaultMoveToIntent(Context context) {
        return getDefaultMoveToIntent(context, TopAdsApi.SRC_BROWSE_PRODUCT);
    }

    @NonNull
    public static Intent getDefaultMoveToIntent(Context context, String ad_src) {
        Intent intent = new Intent(context, BrowseProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, "0");
        bundle.putInt(FRAGMENT_ID, BrowseProductRouter.VALUES_HISTORY_FRAGMENT_ID);
        bundle.putString(AD_SRC, ad_src);
        intent.putExtras(bundle);
        return intent;
    }


    public static void moveTo(Context context, String alias) {
        if (context == null)
            return;

        Intent intent = new Intent(context, BrowseProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_DISCOVERY_ALIAS, alias);
        intent.putExtras(bundle);
        context.startActivity(intent);
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
        Log.d(TAG, "fetchHotListHeader alias " + alias);
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
                        Log.d(TAG, "fetch " + data.getModel1());
                        ObjContainer model2 = data.getModel2();
                        HotListBannerModel.HotListBannerContainer hotListBannerContainer = (HotListBannerModel.HotListBannerContainer) model2;
                        HotListBannerModel body = hotListBannerContainer.body();
                        if (browseProductActivityModel.getOb() != null) {
                            body.query.ob = browseProductActivityModel.getOb();
                        }
                        Map<String, String> filters;

                        if ( browseProductActivityModel != null ) {
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
                        Log.d(TAG, "Hotlist query " + body.query.toString());

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
                Log.d(TAG, "onSuccess: ");
                switch (type) {
                    case DiscoveryListener.CATEGY_HEADER:
                        Log.d(TAG, "onSuccess: ");
                      /*  Log.d(TAG, "fetch " + data.getModel1());
                        ObjContainer model2 = data.getModel2();
                        HotListBannerModel.HotListBannerContainer hotListBannerContainer = (HotListBannerModel.HotListBannerContainer) model2;
                        HotListBannerModel body = hotListBannerContainer.body();
                        if (browseProductActivityModel.getOb() != null) {
                            body.query.ob = browseProductActivityModel.getOb();
                        }
                        Map<String, String> filters;

                        if ( browseProductActivityModel != null ) {
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
                        Log.d(TAG, "Hotlist query " + body.query.toString());

                        FilterMapAtribut.FilterMapValue filterMapValue
                                = new FilterMapAtribut.FilterMapValue();
                        filterMapValue.setValue((HashMap<String, String>) filters);
                        mFilterMapAtribut.getFiltersMap()
                                .put(browseProductActivityModel.getActiveTab(), filterMapValue);


                        browseProductActivityModel.setFilterOptions(filters);
                        browseProductActivityModel.setOb(body.query.ob);
                        browseProductActivityModel.setHotListBannerModel(body);
                        Fragment fragment = BrowseParentFragment.newInstance(browseProductActivityModel);

                        setFragment(fragment, BrowseParentFragment.FRAGMENT_TAG);*/
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


    public BrowseProductRouter.GridType getGridType() {
        return gridType;
    }

    private interface QueryListener {
        void onQueryChanged(String query);
    }


}
