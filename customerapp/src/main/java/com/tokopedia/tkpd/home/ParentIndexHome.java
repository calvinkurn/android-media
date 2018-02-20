package com.tokopedia.tkpd.home;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.ui.widget.TouchViewPager;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.anals.UserAttribute;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.appupdate.AppUpdateDialogBuilder;
import com.tokopedia.core.appupdate.ApplicationUpdate;
import com.tokopedia.core.appupdate.model.DetailUpdate;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileData;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.gallery.ImageGalleryEntry;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.home.GetUserInfoListener;
import com.tokopedia.core.network.retrofit.utils.DialogHockeyApp;
import com.tokopedia.core.onboarding.NewOnboardingActivity;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.design.bottomnavigation.BottomNavigation;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.view.ShakeDetectManager;
import com.tokopedia.tkpd.campaign.view.activity.CapturedAudioCampaignActivity;
import com.tokopedia.tkpd.qrscanner.QrScannerActivity;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment;
import com.tokopedia.tkpd.deeplink.DeepLinkDelegate;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.fcm.appupdate.FirebaseRemoteAppUpdate;
import com.tokopedia.tkpd.home.favorite.view.FragmentFavorite;
import com.tokopedia.tkpd.home.fragment.FragmentHotListV2;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.FeedPlusFragment;

import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;


/**
 * Created by Nisie on 1/07/15.
 * modified by m.normansyah on 4/02/2016, fetch list of bank.
 * modified by alvarisi on 6/15/2016, tab selection tracking.
 * modified by Hafizh Herdi on 6/15/2016, dynamic personalization message.
 * modified by meta on 24/01/2018, implement bottom navigation menu
 */
public class ParentIndexHome extends TkpdActivity implements NotificationReceivedListener,
        GetUserInfoListener, HasComponent  {

    public static final int INIT_STATE_FRAGMENT_HOME = 0;
    public static final int INIT_STATE_FRAGMENT_FEED = 1;
    public static final int INIT_STATE_FRAGMENT_FAVORITE = 2;
    public static final int INIT_STATE_FRAGMENT_HOTLIST = 3;
    public static final int ONBOARDING_REQUEST = 101;
    public static final int WISHLIST_REQUEST = 202;

    public static final int SHAKE_SHAKE_REQUEST = 301;

    public static final String EXTRA_INIT_FRAGMENT = "EXTRA_INIT_FRAGMENT";
    public static final String TAG = ParentIndexHome.class.getSimpleName();
    public static final String messageTAG = TAG + " : ";
    public static final String VIEW_PAGE_POSITION = "VIEW_PAGE_POSITION";

    protected TkpdProgressDialog progressDialog;
    protected PagerAdapter adapter;
    protected TouchViewPager mViewPager;
    protected BottomNavigation bottomNavigation;

    protected LocalCacheHandler cache;
    private AnalyticsCacheHandler cacheHandler;
    private CompositeSubscription subscription = new CompositeSubscription();

    protected Boolean needToRefresh;
    protected int viewPagerIndex;

    private int initStateFragment = INIT_STATE_FRAGMENT_HOME;

    private BroadcastReceiver hockeyBroadcastReceiver;

    @DeepLink(Constants.Applinks.HOME)
    public static Intent getApplinkCallingIntent(Context context, Bundle extras) {
        return new Intent(context, ParentIndexHome.class);
    }

    @DeepLink({Constants.Applinks.HOME_FEED, Constants.Applinks.FEED})
    public static Intent getFeedApplinkCallingIntent(Context context, Bundle extras) {
        return new Intent(context, ParentIndexHome.class)
                .putExtra(HomeRouter.EXTRA_INIT_FRAGMENT, HomeRouter.INIT_STATE_FRAGMENT_FEED)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @DeepLink({Constants.Applinks.FAVORITE})
    public static Intent getFavoriteApplinkCallingIntent(Context context, Bundle extras) {
        return new Intent(context, ParentIndexHome.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(HomeRouter.EXTRA_INIT_FRAGMENT, HomeRouter.INIT_STATE_FRAGMENT_FAVORITE);
    }

    @DeepLink(Constants.Applinks.HOME_CATEGORY)
    public static Intent getCategoryApplinkCallingIntent(Context context, Bundle extras) {
        return new Intent(context, ParentIndexHome.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(HomeRouter.EXTRA_INIT_FRAGMENT, HomeRouter.INIT_STATE_FRAGMENT_HOME);
    }

    @DeepLink(Constants.Applinks.HOME_HOTLIST)
    public static Intent getHotlistApplinkCallingIntent(Context context, Bundle extras) {
        return new Intent(context, ParentIndexHome.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(HomeRouter.EXTRA_INIT_FRAGMENT, HomeRouter.INIT_STATE_FRAGMENT_HOTLIST);
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initStateFragment = intent.getIntExtra(EXTRA_INIT_FRAGMENT, -1);
        if (mViewPager != null) {
            if (initStateFragment != -1) {
                mViewPager.setCurrentItem(initStateFragment);
            }
        }
        checkIsNeedUpdateIfComeFromUnsupportedApplink(intent);

        checkIsHaveApplinkComeFromDeeplink(intent);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_INDEX_HOME;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        initStateFragment = getDefaultTabPosition();
        Log.d(TAG, messageTAG + "onCreate");
        super.onCreate(arg0);

        ShakeDetectManager.getShakeDetectManager(this).init();



        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        if (arg0 != null) {
            //be16268	commit id untuk memperjelas yang bawah
            //yang bikin nama var pake entahlah..... semoga lu segera tobat -rico-

            //beware banyak sampah

            int entahlah = arg0.getInt(VIEW_PAGE_POSITION, (getIntent().getIntExtra(EXTRA_INIT_FRAGMENT, -1)));
            if (entahlah != (-1)) {
                viewPagerIndex = entahlah;
            }

        } else {
            viewPagerIndex = getIntent().getIntExtra(EXTRA_INIT_FRAGMENT,
                    (getIntent().getIntExtra("fragment", initStateFragment)));
            initStateFragment = viewPagerIndex;
        }
        setView();

        if (isFirstTime()) {
            trackFirstTime();
        }

        initCreate();

        NotificationModHandler.clearCacheIfFromNotification(this, getIntent());

        cacheHandler = new AnalyticsCacheHandler();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (SessionHandler.isFirstTimeUser(ParentIndexHome.this)) {

                    //  Launch app intro
                    Intent i = new Intent(ParentIndexHome.this, NewOnboardingActivity.class);
                    startActivityForResult(i, ONBOARDING_REQUEST);

                }
            }
        });

        t.start();

        checkAppUpdate();
        checkIsHaveApplinkComeFromDeeplink(getIntent());

        initHockeyBroadcastReceiver();
    }

    @Override
    public void onGetProfile(DrawerProfile profile) {
        super.onGetProfile(profile);

        setMoengageUserAttributes();
    }

    @Override
    protected void setupToolbar() {
        toolbar = findViewById(R.id.app_bar);
        toolbar.removeAllViews();
        View view = getLayoutInflater().inflate(R.layout.custom_action_bar_searchview, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        View searchView = view.findViewById(R.id.search_container);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchOptionSelected();
            }
        });
        View notif = view.findViewById(R.id.burger_menu);
        ImageView drawerToggle = notif.findViewById(R.id.toggle_but_ab);
        drawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerHelper.isOpened()) {
                    drawerHelper.closeDrawer();
                } else {
                    drawerHelper.openDrawer();
                }
            }
        });
        toolbar.addView(view);
        setSupportActionBar(toolbar);
    }

    private void setMoengageUserAttributes() {

        AnalyticsCacheHandler.GetUserDataListener listener
                = new AnalyticsCacheHandler.GetUserDataListener() {
            @Override
            public void onSuccessGetUserData(ProfileData result) {
                TrackingUtils.setMoEUserAttributes(result);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccessGetUserAttr(UserAttribute.Data data) {
                if (data != null)
                    TrackingUtils.setMoEUserAttributes(data);
            }
        };

        cacheHandler.getUserDataCache(listener);
        cacheHandler.getUserAttrGraphQLCache(listener);

    }

    public void initCreate() {

        adapter = new PagerAdapter(getSupportFragmentManager(), getFragments());
        mViewPager.setAdapter(adapter);

        mViewPager.setSmoothScroll(false);
        mViewPager.SetAllowPageSwitching(false);
        mViewPager.setOffscreenPageLimit(4);

        bottomNavigation.setupWithViewPager(mViewPager, false);

        bottomNavigation.setViewPagerPageChangeListener(new BottomNavigation.ViewPagerPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                initStateFragment = position;
                sendGTMButtonEvent(position);
            }
        });

        bottomNavigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_feed || menuItem.getItemId() == R.id.menu_beranda) {
                    Fragment fragment = adapter.getFragments().get(initStateFragment); // scroll to top
                    if (fragment != null) {
                        if (fragment instanceof FeedPlusFragment)
                            ((FeedPlusFragment) fragment).scrollToTop();
                        else if (fragment instanceof HomeFragment)
                            ((HomeFragment) fragment).scrollToTop();
                    }
                }
            }
        });

        mViewPager.setCurrentItem(initStateFragment, false);
        adapter.notifyDataSetChanged();// DON'T DELETE THIS BECAUSE IT WILL NOTIFY ADAPTER TO CHANGE FROM GUEST TO LOGIN
    }

    @Override
    public void onBackPressed() {
        if (drawerHelper.isOpened()) {
            drawerHelper.closeDrawer();
        } else {
            if (bottomNavigation.getBackStackEntryCount() > 1) {
                bottomNavigation.handleBackPressed();
            } else {
                this.finish();
            }
        }
    }

    private void setView() {
        inflateView(R.layout.activity_index_home_4);
        mViewPager = findViewById(R.id.index_page);
        bottomNavigation = findViewById(R.id.bottomnav);

        bottomNavigation.initBackStack();
    }

    public ChangeTabListener changeTabListener() {
        return new ChangeTabListener() {
            @Override
            public void onChangeTab(int i) {
                mViewPager.setCurrentItem(i);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static Intent getHomeHotlistIntent(Context context) {
        Intent intent = new Intent(context, ParentIndexHome.class);
        intent.putExtra(EXTRA_INIT_FRAGMENT, INIT_STATE_FRAGMENT_HOTLIST);
        return intent;
    }




    private SparseArray<Fragment> getFragments() {
        SparseArray<Fragment> fragments = new SparseArray<>();
        fragments.put(INIT_STATE_FRAGMENT_HOME, HomeFragment.newInstance());
        fragments.put(INIT_STATE_FRAGMENT_FEED, new FeedPlusFragment());
        fragments.put(INIT_STATE_FRAGMENT_FAVORITE, new FragmentFavorite());
        fragments.put(INIT_STATE_FRAGMENT_HOTLIST, new FragmentHotListV2());
        return fragments;
    }

    protected class PagerAdapter extends FragmentStatePagerAdapter {

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public PagerAdapter(FragmentManager fm, SparseArray<Fragment> fragmentSparseArray) {
            super(fm);
            this.registeredFragments = fragmentSparseArray;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return registeredFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return registeredFragments.get(position);
        }

        public SparseArray<Fragment> getFragments() {
            return registeredFragments;
        }

    }

    @Override
    public int getDrawerPosition() {
        return TkpdState.DrawerPosition.INDEX_HOME;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (SessionHandler.isV4Login(this)) {
            getMenuInflater().inflate(R.menu.main, menu);
            LocalCacheHandler Cache = new LocalCacheHandler(getBaseContext(), DrawerHelper.DRAWER_CACHE);
            int CartCache = Cache.getInt(DrawerNotification.IS_HAS_CART);
            if (CartCache > 0) {
                menu.findItem(R.id.action_cart).setIcon(R.drawable.ic_new_action_cart_active);
            } else {
                menu.findItem(R.id.action_cart).setIcon(R.drawable.ic_new_action_cart);
            }
        } else {
            getMenuInflater().inflate(R.menu.menu_guest, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_search:
//                return onSearchOptionSelected();
            case R.id.action_cart:
                if (!SessionHandler.isV4Login(getBaseContext())) {
                    UnifyTracking.eventClickCart();
                    Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext())
                            .getLoginIntent(this);
                    startActivity(intent);
                } else {
                    startActivity(TransactionCartRouter.createInstanceCartActivity(this));
                }
                return true;
            case R.id.action_barcode_scan:
                startActivity(QrScannerActivity.newInstance(this));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onGetNotif() {
        CommonUtils.dumper("nyampeee nich status: " + MainApplication.getNotificationStatus());
    }

    @Override
    protected void onPause() {
        RxUtils.unsubscribeIfNotNull(subscription);
        MainApplication.setCurrentActivity(null);
        Log.d(TAG, messageTAG + "onPause");
        super.onPause();
        viewPagerIndex = mViewPager.getCurrentItem();

        unregisterBroadcastHockeyApp();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(VIEW_PAGE_POSITION, mViewPager.getCurrentItem());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(VIEW_PAGE_POSITION, mViewPager.getCurrentItem());
    }

    boolean isUserFirstTimeLogin = false;

    @Override
    protected void onResume() {
        HockeyAppHelper.checkForUpdate(this);
        RxUtils.getNewCompositeSubIfUnsubscribed(subscription);
        FCMCacheManager.checkAndSyncFcmId(getApplicationContext());
        if (SessionHandler.isV4Login(this) && isUserFirstTimeLogin) {

            initStateFragment = INIT_STATE_FRAGMENT_HOME;
            adapter = new PagerAdapter(getSupportFragmentManager(), getFragments());
            mViewPager.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            bottomNavigation.initBackStack();
            bottomNavigation.setCurrentItem(INIT_STATE_FRAGMENT_HOME);
        }

        isUserFirstTimeLogin = !SessionHandler.isV4Login(this);

        invalidateOptionsMenu();
        MainApplication.setCurrentActivity(this);
        super.onResume();

        NotificationModHandler.showDialogNotificationIfNotShowing(this);

        registerBroadcastHockeyApp();



    }

    @Override
    protected void onStop() {
        Log.d(TAG, messageTAG + "onStop");
        super.onStop();
        needToRefresh = true;
       // sd.unregisterListener(this);
        //sd.stop();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, messageTAG + "onResume");
        ImageGalleryEntry.onActivityForResult(new ImageGalleryEntry.GalleryListener() {
            @Override
            public void onSuccess(ArrayList<String> imageUrls) {
                ProductAddActivity.start(ParentIndexHome.this, imageUrls);
            }

            @Override
            public void onSuccess(String path) {
                ArrayList<String> imageUrls = new ArrayList<>();
                imageUrls.add(path);
                ProductAddActivity.start(ParentIndexHome.this, imageUrls);
            }

            @Override
            public void onFailed(String message) {
                Snackbar.make(parentView, message, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return ParentIndexHome.this;
            }
        }, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WISHLIST_REQUEST && resultCode == RESULT_OK) {
            mViewPager.setCurrentItem(INIT_STATE_FRAGMENT_HOTLIST);
        }
    }

    @Override
    public void onRefreshCart(int status) {
        LocalCacheHandler Cache = new LocalCacheHandler(this, DrawerHelper.DRAWER_CACHE);
        Cache.putInt(DrawerNotification.IS_HAS_CART, status);
        Cache.applyEditor();
        invalidateOptionsMenu();
        MainApplication.resetCartStatus(false);
    }

    private Boolean isFirstTime() {
        LocalCacheHandler cache = new LocalCacheHandler(this, TkpdCache.FIRST_TIME);
        return cache.getBoolean(TkpdCache.Key.IS_FIRST_TIME, false);
    }

    private void trackFirstTime() {
        TrackingUtils.activityBasedAFEvent(HomeRouter.IDENTIFIER_HOME_ACTIVITY);

        LocalCacheHandler cache = new LocalCacheHandler(this, TkpdCache.FIRST_TIME);
        cache.putBoolean(TkpdCache.Key.IS_FIRST_TIME, true);
        cache.applyEditor();
    }

    private int getDefaultTabPosition() {
        return 0;
    }

    private void sendGTMButtonEvent(int position) {
        String action = "";
        String label = "";

        switch (position) {
            case INIT_STATE_FRAGMENT_HOME:
                label = AppEventTracking.EventLabel.HOME;
                break;
            case INIT_STATE_FRAGMENT_FEED:
                label = AppEventTracking.EventLabel.PRODUCT_FEED;
                break;
            case INIT_STATE_FRAGMENT_FAVORITE:
                label = AppEventTracking.EventLabel.FAVORITE;
                break;
            case INIT_STATE_FRAGMENT_HOTLIST:
                label = AppEventTracking.EventLabel.HOTLIST;
                break;
        }

        if (SessionHandler.isV4Login(ParentIndexHome.this)) {
            action = String.format("%s %s %s", action, AppEventTracking.Category.HOME_BOTTOM_NAV, AppEventTracking.EventLabel.AFTER_LOGIN);
        } else {
            action = String.format("%s %s %s", action, AppEventTracking.Category.HOME_BOTTOM_NAV, AppEventTracking.EventLabel.BEFORE_LOGIN);
        }

        UnifyTracking.eventHomeTab(action, label);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onGetUserInfo() {
        setMoengageUserAttributes();
    }


    public interface ChangeTabListener {
        void onChangeTab(int i);
    }

    private void checkAppUpdate() {
        ApplicationUpdate appUpdate = new FirebaseRemoteAppUpdate(this);
        appUpdate.checkApplicationUpdate(new ApplicationUpdate.OnUpdateListener() {
            @Override
            public void onNeedUpdate(DetailUpdate detail) {
                if (!isPausing()) {
                    new AppUpdateDialogBuilder(ParentIndexHome.this, detail)
                            .getAlertDialog().show();
                    UnifyTracking.eventImpressionAppUpdate(detail.isForceUpdate());
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onNotNeedUpdate() {
                checkIsNeedUpdateIfComeFromUnsupportedApplink(ParentIndexHome.this.getIntent());
            }
        });
    }

    private void checkIsNeedUpdateIfComeFromUnsupportedApplink(Intent intent) {
        if (intent.getBooleanExtra(HomeRouter.EXTRA_APPLINK_UNSUPPORTED, false)) {
            if (getApplication() instanceof TkpdCoreRouter && !isPausing()) {
                ((TkpdCoreRouter) getApplication()).getApplinkUnsupported(ParentIndexHome.this).showAndCheckApplinkUnsupported();
            }
        }
    }

    private void checkIsHaveApplinkComeFromDeeplink(Intent intent) {
        if (!TextUtils.isEmpty(intent.getStringExtra(HomeRouter.EXTRA_APPLINK))) {
            String applink = intent.getStringExtra(HomeRouter.EXTRA_APPLINK);
            if (!isPausing()) {
                try {
                    DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
                    Intent applinkIntent = new Intent(this, ParentIndexHome.class);
                    applinkIntent.setData(Uri.parse(applink));
                    deepLinkDelegate.dispatchFrom(this, applinkIntent);
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void initHockeyBroadcastReceiver() {
        hockeyBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null) {
                    if (intent.getAction().equals(FORCE_HOCKEYAPP)) {
                        if (!DialogHockeyApp.isDialogShown(ParentIndexHome.this))
                            showHockeyAppDialog();
                    }
                }
            }
        };
    }

    private void registerBroadcastHockeyApp() {
        if (!GlobalConfig.isAllowDebuggingTools()) {
            IntentFilter intentFilter = new IntentFilter(FORCE_HOCKEYAPP);
            LocalBroadcastManager.getInstance(this).registerReceiver(hockeyBroadcastReceiver,
                    new IntentFilter(intentFilter));
        }
    }

    private void unregisterBroadcastHockeyApp() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(hockeyBroadcastReceiver);
    }

    private void showHockeyAppDialog() {
        DialogHockeyApp.createShow(this,
                new DialogHockeyApp.ActionListener() {
                    @Override
                    public void onDialogClicked() {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(HockeyAppHelper.getHockeyappDownloadUrl()));
                        startActivity(intent);
                    }
                });
    }
}
