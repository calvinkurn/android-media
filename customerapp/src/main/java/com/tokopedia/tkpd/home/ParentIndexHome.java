package com.tokopedia.tkpd.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
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
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.fingerprint.domain.usecase.GetFingerprintUseCase;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileData;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.gallery.ImageGalleryEntry;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.home.GetUserInfoListener;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.onboarding.OnboardingActivity;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.product.view.activity.ProductAddActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.view.FragmentFavorite;
import com.tokopedia.tkpd.home.fragment.FragmentHotListV2;
import com.tokopedia.tkpd.home.fragment.FragmentIndexCategory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.FeedPlusFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Nisie on 1/07/15.
 * modified by m.normansyah on 4/02/2016, fetch list of bank.
 * modified by alvarisi on 6/15/2016, tab selection tracking.
 * modified by Hafizh Herdi on 6/15/2016, dynamic personalization message.
 */
public class ParentIndexHome extends TkpdActivity implements NotificationReceivedListener,
        GetUserInfoListener, HasComponent {

    public static final int INIT_STATE_FRAGMENT_HOME = 0;
    public static final int INIT_STATE_FRAGMENT_FEED = 1;
    public static final int INIT_STATE_FRAGMENT_FAVORITE = 2;
    public static final int INIT_STATE_FRAGMENT_HOTLIST = 3;
    public static final String EXTRA_INIT_FRAGMENT = "EXTRA_INIT_FRAGMENT";

    public static final String TAG = ParentIndexHome.class.getSimpleName();
    public static final String messageTAG = TAG + " : ";
    public static final String VIEW_PAGE_POSITION = "VIEW_PAGE_POSITION";
    public static final String FETCH_BANK = "FETCH_BANK";
    private static final java.lang.String BUNDLE_EXTRA_REFRESH = "refresh";
    private static final String IMAGE_GALLERY = "IMAGE_GALLERY";
    public static final int ONBOARDING_REQUEST = 101;
    public static final int WISHLIST_REQUEST = 202;
    protected PagerAdapter adapter;
    protected ViewPager mViewPager;
    protected TabLayout indicator;
    protected View footerCat;
    protected LocalCacheHandler cache;
    protected Boolean needToRefresh;
    protected int viewPagerIndex;
    private GetFingerprintUseCase getFingerprintUseCase;

    private AnalyticsCacheHandler cacheHandler;
    List<String> content;
    TkpdProgressDialog progressDialog;
    CompositeSubscription subscription = new CompositeSubscription();
    private int initStateFragment = INIT_STATE_FRAGMENT_HOME;

    @DeepLink(Constants.Applinks.HOME)
    public static Intent getApplinkCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ParentIndexHome.class)
                .setData(uri.build())
                .putExtras(extras);
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

        sendNotifLocalyticsCallback();

    }

    private void sendNotifLocalyticsCallback() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(AppEventTracking.LOCA.NOTIFICATION_BUNDLE)) {
                TrackingUtils.eventLocaNotificationCallback(getIntent());
            }
        }
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
        content = new ArrayList<>();
        setView();

        if (isFirstTime()) {
            TrackFirstTime();
        }
        indicator.removeAllTabs();
        content.clear();
//        adapter.notifyDataSetChanged();
        if (SessionHandler.isV4Login(getBaseContext())) {
            String[] CONTENT = new String[]{
                    getString(R.string.title_categories),
                    getString(R.string.title_index_prod_shop),
                    getString(R.string.title_index_favorite),
                    getString(R.string.title_index_hot_list)
            };
            content = new ArrayList<>();
            for (String content_ : CONTENT) {
                indicator.addTab(indicator.newTab().setText(content_));
                content.add(content_);
            }
        } else {
            String[] CONTENT = new String[]{getString(R.string.title_categories), getString(R.string.title_index_hot_list)};
            content = new ArrayList<>();
            for (String content_ : CONTENT) {
                indicator.addTab(indicator.newTab().setText(content_));
                content.add(content_);
            }
        }

        initCreate();
        adapter.notifyDataSetChanged();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (SessionHandler.isFirstTimeUser(ParentIndexHome.this)) {

                    //  Launch app intro
                    Intent i = new Intent(ParentIndexHome.this, OnboardingActivity.class);
                    startActivityForResult(i, ONBOARDING_REQUEST);

                }
            }
        });

        t.start();

        NotificationModHandler.clearCacheIfFromNotification(this, getIntent());

        cacheHandler = new AnalyticsCacheHandler();
    }

    @Override
    public void onGetProfile(DrawerProfile profile) {
        super.onGetProfile(profile);

        setMoengageUserAttributes();
    }

    @Override
    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
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
        ImageView drawerToggle = (ImageView) notif.findViewById(R.id.toggle_but_ab);
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
        cacheHandler.getUserDataCache(new AnalyticsCacheHandler.GetUserDataListener() {
            @Override
            public void onSuccessGetUserData(ProfileData result) {
                TrackingUtils.setMoEUserAttributes(result);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public void initCreate() {

        if (SessionHandler.isV4Login(getBaseContext())) {
            adapter = new PagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(adapter);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));

            // indicator.setupWithViewPager(mViewPager);
            // int fragment = getIntent().getIntExtra("fragment", 1);
            switch (initStateFragment) {
                case INIT_STATE_FRAGMENT_HOME:
                    mViewPager.setCurrentItem(0, true);
                    break;
                case INIT_STATE_FRAGMENT_FAVORITE:
                    mViewPager.setCurrentItem(2, true);
                    break;
                case INIT_STATE_FRAGMENT_HOTLIST:
                    mViewPager.setCurrentItem(3, true);
                    break;
                case INIT_STATE_FRAGMENT_FEED:
                    mViewPager.setCurrentItem(1, true);
                    break;
            }


            /**
             * send Localytics user attributes
             * by : Hafizh Herdi
             */
            getUserCache();
        } else {
            adapter = new PagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(adapter);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
            indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(mViewPager));
            mViewPager.setCurrentItem(0);
        }

        mViewPager.setOffscreenPageLimit(3);
        adapter.notifyDataSetChanged();// DON'T DELETE THIS BECAUSE IT WILL NOTIFY ADAPTER TO CHANGE FROM GUEST TO LOGIN
    }

    /**
     * send Localytics user attributes
     * by : Hafizh Herdi
     */
    private void getUserCache() {
        try {
            LocalCacheHandler cacheUser = new LocalCacheHandler(this, TkpdState.CacheName.CACHE_USER);
            TrackingUtils.eventLocaUserAttributes(SessionHandler.getLoginID(this), cacheUser.getString("user_name"), "");
        } catch (Exception e) {
            CommonUtils.dumper(TAG + " error connecting to GCM Service");
            TrackingUtils.eventLogAnalytics(ParentIndexHome.class.getSimpleName(), e.getMessage());
        }
    }

    private void setView() {
        inflateView(R.layout.activity_index_home_4);
        footerCat = View.inflate(ParentIndexHome.this, R.layout.fragment_category, null);
        mViewPager = (ViewPager) findViewById(R.id.index_page);
        indicator = (TabLayout) findViewById(R.id.indicator);
    }

    public ChangeTabListener GetHotListListener() {
        return new ChangeTabListener() {
            @Override
            public void onChangeTab(int i) {
                mViewPager.setCurrentItem(3);
            }
        };
    }

    public ChangeTabListener GetFavoriteListener() {
        return new ChangeTabListener() {
            @Override
            public void onChangeTab(int i) {
                mViewPager.setCurrentItem(2);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected class PagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public PagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (SessionHandler.isV4Login(ParentIndexHome.this)) {

                if (getPageTitle(position).equals(content.get(0))) {
                    Fragment fragment = FragmentIndexCategory.newInstance();
                    registeredFragments.put(position, fragment);
                    return fragment;
                }

                if (getPageTitle(position).equals(content.get(1))) {
                    Fragment fragment = new FeedPlusFragment();
                    registeredFragments.put(position, fragment);
                    return fragment;
                }

                if (getPageTitle(position).equals(content.get(2))) {
                    Fragment fragment = new FragmentFavorite();
                    registeredFragments.put(position, fragment);
                    return fragment;
                }

                if (getPageTitle(position).equals(content.get(3))) {
                    Fragment fragment = new FragmentHotListV2();
                    registeredFragments.put(position, fragment);
                    return fragment;
                }
            } else {
                switch (position) {
                    case 0:
                        Fragment fragment = FragmentIndexCategory.newInstance();
                        registeredFragments.put(position, fragment);
                        return fragment;
                    case 1:
                        fragment = new FragmentHotListV2();
                        registeredFragments.put(position, fragment);
                        return fragment;
                }
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ParentIndexHome.this.content.get(position);
        }

        @Override
        public int getCount() {
            return ParentIndexHome.this.content.size();
        }

        public
        @Nullable
        Fragment getFragmentForPosition(int position) {
            return registeredFragments.get(position);
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
                    Intent intent = SessionRouter.getLoginActivityIntent(getApplicationContext());
                    intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.MOVE_TO_CART_TYPE);
                    intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                    startActivity(intent);
                } else {
                    startActivity(TransactionCartRouter.createInstanceCartActivity(this));
                }
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

    @Override
    protected void onResume() {
        RxUtils.getNewCompositeSubIfUnsubscribed(subscription);
        if (SessionHandler.isV4Login(this) && indicator.getTabCount() < 4) {
            indicator.removeAllTabs();
            content.clear();
            String[] CONTENT = new String[]{
                    getString(R.string.title_categories),
                    getString(R.string.title_index_prod_shop),
                    getString(R.string.title_index_favorite),
                    getString(R.string.title_index_hot_list)
            };
            content = new ArrayList<>();
            for (String content_ : CONTENT) {
                indicator.addTab(indicator.newTab().setText(content_));
                content.add(content_);
            }
            adapter = new PagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        setScrollFeedListener();
        invalidateOptionsMenu();
        MainApplication.setCurrentActivity(this);
        super.onResume();

        sendNotifLocalyticsCallback();

        NotificationModHandler.showDialogNotificationIfNotShowing(this);
    }

    private void setScrollFeedListener() {
        if (sessionHandler != null
                && sessionHandler.isV4Login()
                && indicator != null) {
            indicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());
                    sendGTMButtonEvent(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    Fragment fragment = adapter.getFragmentForPosition(tab.getPosition());
                    if (fragment != null && fragment instanceof FeedPlusFragment) {
                        ((FeedPlusFragment) fragment).scrollToTop();
                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, messageTAG + "onStop");
        super.onStop();
        needToRefresh = true;
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
            public void onSuccess(String path, int position) {
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
        if (requestCode == ONBOARDING_REQUEST && resultCode == RESULT_OK
                && data != null && data.getExtras() != null) {
            if (data.getExtras().getInt(com.tokopedia.core.session.presenter.Session
                    .WHICH_FRAGMENT_KEY) == TkpdState.DrawerPosition.LOGIN) {
                Intent intent = ((TkpdCoreRouter) getApplication()).getLoginIntent(this);
                intent.putExtras(data.getExtras());
                Intent intentHome = ((TkpdCoreRouter) getApplication()).getHomeIntent
                        (this);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivities(new Intent[]
                        {
                                intentHome,
                                intent
                        });
                finish();
            } else if (data.getExtras().getInt(com.tokopedia.core.session.presenter.Session
                    .WHICH_FRAGMENT_KEY) == TkpdState.DrawerPosition.REGISTER) {
                Intent intent = ((TkpdCoreRouter) getApplication()).getRegisterIntent
                        (this);
                Intent intentHome = ((TkpdCoreRouter) getApplication()).getHomeIntent
                        (this);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivities(new Intent[]
                        {
                                intentHome,
                                intent
                        });
                finish();
            }
        }
        if (requestCode == WISHLIST_REQUEST && resultCode == RESULT_OK) {
            mViewPager.setCurrentItem(3);
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

    private void TrackFirstTime() {
        TrackingUtils.activityBasedAFEvent(HomeRouter.IDENTIFIER_HOME_ACTIVITY);

        LocalCacheHandler cache = new LocalCacheHandler(this, TkpdCache.FIRST_TIME);
        cache.putBoolean(TkpdCache.Key.IS_FIRST_TIME, true);
        cache.applyEditor();
    }

    private int getDefaultTabPosition() {
        if (SessionHandler.isV2Login(getApplicationContext()) || SessionHandler.isV4Login(getApplicationContext())) {
            return 1;
        }

        return 0;
    }

    private void sendGTMButtonEvent(int position) {
        if (SessionHandler.isV4Login(ParentIndexHome.this)) {
            String label = "";
            switch (position) {
                case 0:
                    label = AppEventTracking.EventLabel.HOME;
                    break;
                case 1:
                    label = AppEventTracking.EventLabel.PRODUCT_FEED;
                    break;
                case 2:
                    label = AppEventTracking.EventLabel.FAVORITE;
                    break;
                case 3:
                    label = AppEventTracking.EventLabel.HOTLIST;
                    break;
            }

            UnifyTracking.eventHomeTab(label);
        }
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

}
