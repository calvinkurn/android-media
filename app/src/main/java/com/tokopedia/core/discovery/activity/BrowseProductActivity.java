package com.tokopedia.core.discovery.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.gson.Gson;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.deeplink.presenter.DeepLinkPresenterImpl;
import com.tokopedia.core.discovery.fragment.browseparent.BrowseParentFragment;
import com.tokopedia.core.discovery.fragment.browseparent.ProductFragment;
import com.tokopedia.core.discovery.fragment.browseparent.ShopFragment;
import com.tokopedia.core.discovery.fragment.history.SearchHistoryFragment;
import com.tokopedia.core.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.core.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.core.discovery.interactor.SearchInteractor;
import com.tokopedia.core.discovery.interactor.SearchInteractorImpl;
import com.tokopedia.core.discovery.interfaces.DiscoveryListener;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.discovery.model.BrowseProductActivityModel;
import com.tokopedia.core.discovery.model.BrowseProductModel;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.discovery.model.NetworkParam;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.discovery.presenter.DiscoveryActivityPresenter;
import com.tokopedia.core.dynamicfilter.DynamicFilterActivity;
import com.tokopedia.core.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.core.dynamicfilter.presenter.DynamicFilterPresenter;
import com.tokopedia.core.dynamicfilter.presenter.DynamicFilterView;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.myproduct.presenter.ImageGalleryImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Erry on 6/30/2016.
 */
public class BrowseProductActivity extends TActivity implements SearchView.OnQueryTextListener,
        DiscoveryActivityPresenter, MenuItemCompat.OnActionExpandListener {

    private static final String TAG = BrowseProductActivity.class.getSimpleName();
    private static final String KEY_GTM = "GTMFilterData";
    @Bind(R2.id.container)
    FrameLayout container;
    @Bind(R2.id.progressBar)
    ProgressBar progressBar;
    private SearchView searchView;
    private String searchQuery;
    //    private Map<String, String> filters;
    private FragmentManager fragmentManager;
    private SearchInteractor searchInteractor;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    //    private DynamicFilterModel.Data filterAttribute;
    private HashMap<String, DynamicFilterModel.Data> filterAttributMap = new HashMap<>();
    private HashMap<String, Map<String, String>> filtersMap = new HashMap<>();
    private HashMap<String, Fragment> fragmentHashMap = new HashMap<>();
    private SharedPreferences preferences;
    private List<Breadcrumb> breadcrumbs;
    private boolean afterRestoreSavedInstance;
    private Subscription querySubscription;
    private QueryListener queryListener;

    public void sendHotlist(String selected) {
        fetchHotListHeader(selected);
        browseProductActivityModel.setSource(DynamicFilterPresenter.HOT_PRODUCT);
        browseProductActivityModel.alias = selected;
    }

    public enum GridType {
        GRID_1, GRID_2, GRID_3
    }

    public static final String EXTRA_SOURCE = "EXTRA_SOURCE";
    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_BROWSE_MODEL = "EXTRA_BROWSE_MODEL";
    public static final String EXTRA_FIRST_TIME = "EXTRA_FIRST_TIME";
    public static final String EXTRA_FILTER_MAP = "EXTRA_FILTER_MAP";
    public static final String EXTRA_FILTER_MAP_ATTR = "EXTRA_FILTER_MAP_ATTR";
    public static String browseType;
    private int gridIcon = R.drawable.ic_grid_default;
    private GridType gridType = GridType.GRID_2;
    private Fragment mLastFragment;
    private int keepActivitySettings;
    private boolean firstTime = true;
    @Bind(R2.id.root)
    CoordinatorLayout coordinatorLayout;
    @Bind(R2.id.toolbar)
    Toolbar toolbar;
    @Bind(R2.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;
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
            fetchIntent();
            deleteFilterAndSortCache();
        } else {
            firstTime = savedInstanceState.getBoolean(EXTRA_FIRST_TIME);
            browseProductActivityModel = Parcels.unwrap(savedInstanceState.getParcelable(EXTRA_BROWSE_MODEL));
            filterAttributMap = Parcels.unwrap(savedInstanceState.getParcelable(EXTRA_FILTER_MAP_ATTR));
            filtersMap = Parcels.unwrap(savedInstanceState.getParcelable(EXTRA_FILTER_MAP));
            browseProductActivityModel.setFilterOptions(filtersMap.get(browseProductActivityModel.getSource()));
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
            case DynamicFilterPresenter.SEARCH_PRODUCT:
            case DynamicFilterPresenter.SEARCH_CATALOG:
            case DynamicFilterPresenter.SEARCH_SHOP:
                toolbar.setTitle(getString(R.string.search_product));
                break;
            case DynamicFilterPresenter.DIRECTORY:
                toolbar.setTitle(getString(R.string.title_activity_browse_category));
                break;
            case DynamicFilterPresenter.HOT_PRODUCT:
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
        .debounce(400, TimeUnit.MILLISECONDS)
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
                if(s != null) {
                    Log.d(TAG, "Sending the text " + s);
                    sendBroadCast(s);
                }
            }
        }));

        if (browseProductActivityModel.alias != null && browseProductActivityModel.getHotListBannerModel() == null)
            fetchHotListHeader(browseProductActivityModel.alias);

        if(browseProductActivityModel.isSearchDeeplink()){
            sendQuery(browseProductActivityModel.getQ());
        } else {
            switch (browseProductActivityModel.getFragmentId()) {
                case ProductFragment.FRAGMENT_ID:
                    if (!isFragmentCreated(BrowseParentFragment.FRAGMENT_TAG) || keepActivitySettings == 1) {
                        setFragment(BrowseParentFragment.newInstance(browseProductActivityModel), BrowseParentFragment.FRAGMENT_TAG);
                    }
                    break;
                case SearchHistoryFragment.FRAGMENT_ID:
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
        outState.putParcelable(EXTRA_BROWSE_MODEL, Parcels.wrap(browseProductActivityModel));
        outState.putBoolean(EXTRA_FIRST_TIME, firstTime);
        outState.putParcelable(EXTRA_FILTER_MAP, Parcels.wrap(filtersMap));
        outState.putParcelable(EXTRA_FILTER_MAP_ATTR, Parcels.wrap(filterAttributMap));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        browseProductActivityModel = Parcels.unwrap(savedInstanceState.getParcelable(EXTRA_BROWSE_MODEL));
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
        if(isFinishing()) return;
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
            browseProductActivityModel.setFragmentId(ProductFragment.FRAGMENT_ID);
        } else {
            bottomNavigation.hideBottomNavigation();
            bottomNavigation.setBehaviorTranslationEnabled(false);
            browseProductActivityModel.setFragmentId(SearchHistoryFragment.FRAGMENT_ID);
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
            case DynamicFilterPresenter.SEARCH_CATALOG:
            case DynamicFilterPresenter.SEARCH_PRODUCT:
            case DynamicFilterPresenter.SEARCH_SHOP:
                searchItem.expandActionView();
                break;
            case DynamicFilterPresenter.HOT_PRODUCT:
                searchItem.setVisible(false);
                break;
        }
        if(browseProductActivityModel.isSearchDeeplink()) {
            searchView.setQuery(browseProductActivityModel.getQ(), false);
            browseProductActivityModel.setSearchDeeplink(false);
        }
        searchView.clearFocus();
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R2.id.action_search:
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
        intent.putExtra(SEARCH_TERM, query);
        sendBroadcast(intent);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.searchQuery = newText;
        if (!newText.isEmpty() && fragmentManager.findFragmentById(R.id.container) instanceof BrowseParentFragment && !browseProductActivityModel.isSearchDeeplink()) {
            setFragment(SearchHistoryFragment.newInstance(newText), SearchHistoryFragment.FRAGMENT_TAG);
        } else {
            queryListener.onQueryChanged(newText);
        }
        return false;
    }

    public void setFilterAttribute(DynamicFilterModel.Data filterAttribute, String source) {
        if (checkHasFilterAttrIsNull(source))
            filterAttributMap.put(source, filterAttribute);
    }

    public boolean checkHasFilterAttrIsNull(String source) {
        return filterAttributMap.get(source) == null;
    }


    public void sendQuery(String query) {
        saveQueryCache(query);
        resetBrowseProductActivityModel();
        browseProductActivityModel.setQ(query);
        if (firstTime || browseProductActivityModel.getSource().equals(DynamicFilterPresenter.HOT_PRODUCT) || browseProductActivityModel.getSource().equals(DynamicFilterPresenter.DIRECTORY)) {
            browseProductActivityModel.setSource(DynamicFilterPresenter.SEARCH_PRODUCT);
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
        browseProductActivityModel.setFilterOptions(null);

    }

    public void deleteFilterAndSortCache() {
        deleteFilterCache();
        browseProductActivityModel.setOb("23");
        filterAttributMap.clear();
    }

    private void resetBrowseProductActivityModel() {
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
            browseProductActivityModel.alias = intent.getStringExtra(ALIAS);
            //[END] check hot list param
            String source = getIntent().getStringExtra(EXTRA_SOURCE);
            if (source != null) {
                browseProductActivityModel.setSource(source);
                browseType = source;
            } else {
                browseType = DynamicFilterPresenter.SEARCH_PRODUCT;
            }
            if (browseProductActivityModel.alias == null) {
                // get department and fragment id that would be shown.
                String departmentId = intent.getStringExtra(DEPARTMENT_ID);
                int fragmentId = intent.getIntExtra(FRAGMENT_ID, INVALID_FRAGMENT_ID);
                String adSrc = intent.getStringExtra(AD_SRC);

                this.searchQuery = intent.getStringExtra(SEARCH_TERM);
                browseProductActivityModel.setQ(this.searchQuery);
                // set the value get from intent
                if (adSrc != null)
                    browseProductActivityModel.setAdSrc(adSrc);
                browseProductActivityModel.setDepartmentId(departmentId);
                browseProductActivityModel.setParentDepartement(departmentId);
                browseProductActivityModel.setFragmentId(fragmentId);
            }
            browseProductActivityModel.setSearchDeeplink(intent.getBooleanExtra(DeepLinkPresenterImpl.IS_DEEP_LINK_SEARCH, false));
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
            case DynamicFilterPresenter.SEARCH_SHOP:
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
                Intent intent;
                DynamicFilterModel.Data filterAttribute = filterAttributMap.get(source);
                switch (position) {
                    case 0:
                        BrowseParentFragment parentFragment = (BrowseParentFragment) fragmentManager.findFragmentById(R.id.container);
                        if (parentFragment.getActiveFragment() instanceof ShopFragment) {
                            openFilter(filterAttribute, source);
                        } else {
                            openSort(filterAttribute, source);
                        }
                        break;
                    case 1:
                        openFilter(filterAttribute, source);
                        break;
                    case 2:
                        intent = new Intent(CHANGE_GRID_ACTION_INTENT);
                        switch (gridType) {
                            case GRID_1:
                                gridType = GridType.GRID_2;
                                gridIcon = R.drawable.ic_grid_default;
                                bottomNavigation.getItem(2).setTitle(getString(R.string.grid));
                                break;
                            case GRID_2:
                                gridType = GridType.GRID_3;
                                gridIcon = R.drawable.ic_grid_box;
                                bottomNavigation.getItem(2).setTitle(getString(R.string.grid));
                                break;
                            case GRID_3:
                                gridType = GridType.GRID_1;
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
                                        .setType(getString(R.string.share_product_key))
                                        .setName(getString(R.string.message_share_catalog))
                                        .setTextContent(getString(R.string.message_share_category) + shareUrl)
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
        if (firstTime) {
            bottomNavigation.setCurrentItem(0, false);
            firstTime = false;
        }
    }

    private void openSort(DynamicFilterModel.Data filterAttribute, String source) {
        Intent intent;
        if (filterAttribute != null) {
            if (browseProductActivityModel.getOb() != null) {
                filterAttribute.setSelectedOb(browseProductActivityModel.getOb());
            }
            intent = new Intent(BrowseProductActivity.this, SortProductActivity.class);
            intent.putExtra(EXTRA_DATA, Parcels.wrap(filterAttribute));
            intent.putExtra(EXTRA_SOURCE, source);
            startActivityForResult(intent, REQUEST_SORT);
            overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
        }
    }

    private void openFilter(DynamicFilterModel.Data filterAttribute, String source) {
        List<Breadcrumb> crumb = getProductBreadCrumb();
        if (crumb != null) {
            breadcrumbs = crumb;
        }
        if (filterAttribute != null && breadcrumbs != null) {
            Map<String, String> filters = filtersMap.get(source);
            DynamicFilterActivity.moveTo(BrowseProductActivity.this,
                    filters, breadcrumbs,
                    filterAttribute.getFilter(),
                    browseProductActivityModel.getParentDepartement(), source);

        }
        return;
    }


    private List<AHBottomNavigationItem> getBottomItemsShop() {
        List<AHBottomNavigationItem> items = new ArrayList<>();
        items.add(new AHBottomNavigationItem("Filter", R.drawable.ic_filter_list_black_24dp));
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
                    DynamicFilterModel.Data sortData = Parcels.unwrap(data.getParcelableExtra(BrowseParentFragment.SORT_EXTRA));
                    String source = data.getStringExtra(BrowseParentFragment.SOURCE_EXTRA);
                    filterAttributMap.put(browseProductActivityModel.getSource(), sortData);
                    String newOb = sortData.getSelectedOb();
                    if(source.equals(DynamicFilterPresenter.SEARCH_CATALOG)) {
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
                    Map<String, String> filters = Parcels.unwrap(data.getParcelableExtra(DynamicFilterView.EXTRA_RESULT));
                    filtersMap.put(browseProductActivityModel.getSource(), filters);
                    browseProductActivityModel.setFilterOptions(filters);
                    Log.d(TAG, "filter option " + filters);
                    sendFilterGTM(filters);
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
        bundle.putString(DEPARTMENT_ID, "0");
        bundle.putInt(FRAGMENT_ID, SearchHistoryFragment.FRAGMENT_ID);
        bundle.putString(AD_SRC, ad_src);
        intent.putExtras(bundle);
        return intent;
    }


    public static void moveTo(Context context, String alias) {
        if (context == null)
            return;

        Intent intent = new Intent(context, BrowseProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ALIAS, alias);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param depId
     * @param ad_src  {@value TopAdsApi#SRC_BROWSE_PRODUCT} for example
     */
    public static void moveTo(Context context, String depId, String ad_src) {
        moveTo(context, depId, ad_src, DynamicFilterPresenter.SEARCH_PRODUCT, null);
    }

    public static void moveTo(Context context, String depId, String ad_src, String source, String title) {
        if (context == null)
            return;

        Intent intent = new Intent(context, BrowseProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(DEPARTMENT_ID, depId);
        bundle.putInt(FRAGMENT_ID, ProductFragment.FRAGMENT_ID);
        bundle.putString(AD_SRC, ad_src);
        bundle.putString(EXTRA_SOURCE, source);
        if (title != null) {
            bundle.putString(EXTRA_TITLE, title);
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void showLoading(boolean isLoading){
        progressBar.setIndeterminate(isLoading);
        if(isLoading){
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void showEmptyState(NetworkErrorHelper.RetryClickedListener retryClickedListener){
        NetworkErrorHelper.showEmptyState(BrowseProductActivity.this,container, retryClickedListener);
        if (bottomNavigation!=null) {
            bottomNavigation.hideBottomNavigation();
        }
    }

    public void removeEmptyState(){
        NetworkErrorHelper.removeEmptyState(coordinatorLayout);
        NetworkErrorHelper.removeEmptyState(container);
            if (bottomNavigation!=null && bottomNavigation.isHidden() ) {
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
            public void onComplete(int type, ImageGalleryImpl.Pair<String, ? extends ObjContainer> data) {
                showLoading(false);
            }

            @Override
            public void onFailed(int type, ImageGalleryImpl.Pair<String, ? extends ObjContainer> data) {
                showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        fetchHotListHeader(alias);
                    }
                });
            }

            @Override
            public void onSuccess(int type, ImageGalleryImpl.Pair<String, ? extends ObjContainer> data) {
                switch (type) {
                    case DiscoveryListener.HOTLIST_BANNER:
                        Log.d(TAG, "fetch " + data.getModel1());
                        ObjContainer model2 = data.getModel2();
                        HotListBannerModel.HotListBannerContainer hotListBannerContainer = (HotListBannerModel.HotListBannerContainer) model2;
                        HotListBannerModel body = hotListBannerContainer.body();
                        if (browseProductActivityModel.getOb() != null) {
                            body.query.ob = browseProductActivityModel.getOb();
                        }
                        Map<String, String> filters = filtersMap.get(browseProductActivityModel.getSource());
                        if (filters != null) {
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
                        filtersMap.put(browseProductActivityModel.getSource(), filters);
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

    public GridType getGridType() {
        return gridType;
    }

    private interface QueryListener {
        void onQueryChanged(String query);
    }
}
