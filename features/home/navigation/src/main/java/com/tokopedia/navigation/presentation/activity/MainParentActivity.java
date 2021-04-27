package com.tokopedia.navigation.presentation.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.RestrictTo;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.base.view.appupdate.AppUpdateDialogBuilder;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleObserver;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback;
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.DeeplinkDFMapper;
import com.tokopedia.applink.FragmentConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.cart.view.CartFragment;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.devicefingerprint.appauth.AppAuthWorker;
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker;
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker;
import com.tokopedia.dynamicfeatures.DFInstaller;
import com.tokopedia.home.HomeInternalRouter;
import com.tokopedia.home.account.presentation.fragment.AccountHomeFragment;
import com.tokopedia.inappupdate.AppUpdateManagerWrapper;
import com.tokopedia.navigation.GlobalNavAnalytics;
import com.tokopedia.navigation.GlobalNavConstant;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.analytics.performance.PerformanceData;
import com.tokopedia.navigation.appupdate.FirebaseRemoteAppUpdate;
import com.tokopedia.navigation.domain.model.Notification;
import com.tokopedia.navigation.presentation.customview.BottomMenu;
import com.tokopedia.navigation.presentation.customview.IBottomClickListener;
import com.tokopedia.navigation.presentation.customview.LottieBottomNavbar;
import com.tokopedia.navigation.presentation.di.DaggerGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavModule;
import com.tokopedia.navigation.presentation.presenter.MainParentPresenter;
import com.tokopedia.navigation.presentation.view.MainParentView;
import com.tokopedia.navigation_common.listener.AllNotificationListener;
import com.tokopedia.navigation_common.listener.CartNotifyListener;
import com.tokopedia.navigation_common.listener.FragmentListener;
import com.tokopedia.navigation_common.listener.HomePerformanceMonitoringListener;
import com.tokopedia.navigation_common.listener.MainParentStateListener;
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener;
import com.tokopedia.navigation_common.listener.OfficialStorePerformanceMonitoringListener;
import com.tokopedia.navigation_common.listener.RefreshNotificationListener;
import com.tokopedia.navigation_common.listener.ShowCaseListener;
import com.tokopedia.officialstore.category.presentation.fragment.OfficialHomeContainerFragment;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.remoteconfig.abtest.AbTestPlatform;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.track.TrackApp;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.Lazy;

import static com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_SOURCE;
import static com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP;
import static com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_PAGE;

/**
 * Created by meta on 19/06/18.
 */
public class MainParentActivity extends BaseActivity implements
        HasComponent,
        MainParentView,
        ShowCaseListener,
        CartNotifyListener,
        RefreshNotificationListener,
        MainParentStatusBarListener,
        HomePerformanceMonitoringListener,
        OfficialStorePerformanceMonitoringListener,
        IBottomClickListener,
        MainParentStateListener
{

    public static final String MO_ENGAGE_COUPON_CODE = "coupon_code";
    public static final String ARGS_TAB_POSITION = "TAB_POSITION";
    public static final int HOME_MENU = 0;
    public static final int FEED_MENU = 1;
    public static final int OS_MENU = 2;
    public static final int CART_MENU = 3;
    public static final int ACCOUNT_MENU = 4;
    public static final int RECOMENDATION_LIST = 5;
    public static final String DEFAULT_NO_SHOP = "0";
    public static final String BROADCAST_FEED = "BROADCAST_FEED";
    public static final String PARAM_BROADCAST_NEW_FEED = "PARAM_BROADCAST_NEW_FEED";
    public static final String PARAM_BROADCAST_NEW_FEED_CLICKED = "PARAM_BROADCAST_NEW_FEED_CLICKED";
    public static final String SCROLL_RECOMMEND_LIST = "recommend_list";
    private static final String OFFICIAL_STORE = "Official Store";
    private static final int EXIT_DELAY_MILLIS = 2000;
    private static final String IS_RECURRING_APPLINK = "IS_RECURRING_APPLINK";
    private static final String SHORTCUT_BELI_ID = "Beli";
    private static final String SHORTCUT_DIGITAL_ID = "Bayar";
    private static final String SHORTCUT_SHARE_ID = "Share";
    private static final String SHORTCUT_SHOP_ID = "Jual";
    private static final String ANDROID_CUSTOMER_NEW_OS_HOME_ENABLED = "android_customer_new_os_home_enabled";
    private static final String SOURCE_ACCOUNT = "account";
    private static final String HOME_PERFORMANCE_MONITORING_KEY = "mp_home";
    private static final String HOME_PERFORMANCE_MONITORING_PREPARE_METRICS = "home_plt_start_page_metrics";
    private static final String HOME_PERFORMANCE_MONITORING_NETWORK_METRICS = "home_plt_network_request_page_metrics";
    private static final String HOME_PERFORMANCE_MONITORING_RENDER_METRICS = "home_plt_render_page_metrics";

    private static final String HOME_PERFORMANCE_MONITORING_CACHE_ATTRIBUTION = "dataSource";
    private static final String HOME_PERFORMANCE_MONITORING_CACHE_VALUE = "Cache";
    private static final String HOME_PERFORMANCE_MONITORING_NETWORK_VALUE = "Network";

    private static final String OFFICIAL_STORE_PERFORMANCE_MONITORING_KEY = "mp_official_store";
    private static final String OFFICIAL_STORE_PERFORMANCE_MONITORING_PREPARE_METRICS = "official_store_plt_start_page_metrics";
    private static final String OFFICIAL_STORE_PERFORMANCE_MONITORING_NETWORK_METRICS = "official_store_plt_network_request_page_metrics";
    private static final String OFFICIAL_STORE_PERFORMANCE_MONITORING_RENDER_METRICS = "official_store_plt_render_page_metrics";

    private static final String MAIN_PARENT_PERFORMANCE_MONITORING_KEY = "mp_slow_rendering_perf";

    private static final String ROLLANCE_EXP_NAME = AbTestPlatform.NAVIGATION_EXP_TOP_NAV;
    private static final String ROLLANCE_VARIANT_OLD = AbTestPlatform.NAVIGATION_VARIANT_OLD;
    private static final String ROLLANCE_VARIANT_REVAMP = AbTestPlatform.NAVIGATION_VARIANT_REVAMP;

    ArrayList<BottomMenu> menu = new ArrayList<>();

    @Inject
    Lazy<UserSessionInterface> userSession;
    @Inject
    Lazy<MainParentPresenter> presenter;
    @Inject
    Lazy<GlobalNavAnalytics> globalNavAnalytics;
    @Inject
    Lazy<RemoteConfig> remoteConfig;

    private ApplicationUpdate appUpdate;
    private LottieBottomNavbar bottomNavigation;
    private ShowCaseDialog showCaseDialog;
    List<Fragment> fragmentList;
    private Notification notification;
    Fragment currentFragment;
    private int currentSelectedFragmentPosition = HOME_MENU;
    private SparseArray<PerformanceData> fragmentPerformanceDatas = new SparseArray<>();
    private boolean isUserFirstTimeLogin = false;
    private boolean doubleTapExit = false;
    private BroadcastReceiver newFeedClickedReceiver;
    private SharedPreferences cacheManager;
    private Handler handler = new Handler();
    private FrameLayout fragmentContainer;
    private boolean isFirstNavigationImpression = false;
    private boolean useNewInbox = false;

    private PerformanceMonitoring officialStorePerformanceMonitoring;

    private PerformanceMonitoring mainParentPerformanceMonitoring;

    private PageLoadTimePerformanceCallback pageLoadTimePerformanceCallback;
    private PageLoadTimePerformanceCallback officialStorePageLoadTimePerformanceCallback;

    private boolean isNewNavigation;

    public static Intent start(Context context) {
        return new Intent(context, MainParentActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        validateNavigationRollence();

        //changes for triggering unittest checker
        startSelectedPagePerformanceMonitoring();
        startMainParentPerformanceMonitoring();

        super.onCreate(savedInstanceState);
        initInjector();
        initInboxAbTest();
        presenter.get().setView(this);
        if (savedInstanceState != null) {
            presenter.get().setIsRecurringApplink(savedInstanceState.getBoolean(IS_RECURRING_APPLINK, false));
        }
        cacheManager = PreferenceManager.getDefaultSharedPreferences(this);
        appUpdate = new FirebaseRemoteAppUpdate(this);
        createView(savedInstanceState);
        WeaveInterface executeEventsWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return sendOpenHomeEvent();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(executeEventsWeave, RemoteConfigKey.ENABLE_ASYNC_OPENHOME_EVENT, getContext());
        installDFonBackground();
        runRiskWorker();
    }

    private void runRiskWorker() {
        // Most of workers do nothing if it has already succeed previously.
        SubmitDeviceWorker.Companion.scheduleWorker(getApplicationContext(), false);
        DataVisorWorker.Companion.scheduleWorker(getApplicationContext(), false);
        AppAuthWorker.Companion.scheduleWorker(getApplicationContext(), false);
    }

    private void initInboxAbTest() {
        useNewInbox = RemoteConfigInstance.getInstance().getABTestPlatform().getString(
                AbTestPlatform.KEY_AB_INBOX_REVAMP, AbTestPlatform.VARIANT_OLD_INBOX
        ).equals(AbTestPlatform.VARIANT_NEW_INBOX) && isNewNavigation;
    }

    private void installDFonBackground() {
        List<String> moduleNameList = new ArrayList<>();
        if (userSession.get().isLoggedIn()) {
            moduleNameList.add(DeeplinkDFMapper.DF_PROMO_TOKOPOINTS);
            moduleNameList.add(DeeplinkDFMapper.DF_USER_SETTINGS);
            moduleNameList.add(DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US);
            moduleNameList.add(DeeplinkDFMapper.DF_PROMO_GAMIFICATION);
            moduleNameList.add(DeeplinkDFMapper.DF_MERCHANT_LOGIN);
        }
        if (userSession.get().hasShop()) {
            moduleNameList.add(DeeplinkDFMapper.DF_MERCHANT_SELLER);
        }
        moduleNameList.add(DeeplinkDFMapper.DF_TRAVEL);
        moduleNameList.add(DeeplinkDFMapper.DF_SALAM_UMRAH);
        moduleNameList.add(DeeplinkDFMapper.DF_ENTERTAINMENT);
        DFInstaller.installOnBackground(this.getApplication(), moduleNameList, "Home");
    }

    @NotNull
    private boolean sendOpenHomeEvent() {
        UserSessionInterface userSession = new UserSession(this);
        Map<String, Object> value = DataLayer.mapOf(
                AppEventTracking.MOENGAGE.LOGIN_STATUS, userSession.isLoggedIn()
        );
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, AppEventTracking.EventMoEngage.OPEN_BERANDA);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isFirstTimeUser()) {
            setDefaultShakeEnable();
            routeOnboarding();
        }
    }

    private void routeOnboarding() {
        RouteManager.route(this, ApplinkConstInternalMarketplace.ONBOARDING);
        finish();
    }

    private void setDefaultShakeEnable() {
        cacheManager.edit()
                .putBoolean(getString(R.string.pref_receive_shake), true)
                .apply();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        saveInstanceState(outState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        } catch (Exception e) {
            reloadPage();
        }
    }

    public boolean isFirstTimeUser() {
        return userSession.get().isFirstTimeUser();
    }

    private void createView(Bundle savedInstanceState) {
        isFirstNavigationImpression = true;
        setContentView(R.layout.activity_main_parent);

        fragmentContainer = findViewById(R.id.container);
        fragmentList = fragments();

        WeaveInterface firstTimeWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return executeFirstTimeEvent();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(firstTimeWeave, RemoteConfigKey.ENABLE_ASYNC_FIRSTTIME_EVENT, getContext());
        showSelectedPage();
        checkAppUpdateAndInApp();
        checkApplinkCouponCode(getIntent());
        showSelectedPage();
        bottomNavigation = findViewById(R.id.bottom_navbar);

        populateBottomNavigationView();
        bottomNavigation.setMenuClickListener(this);

        initNewFeedClickReceiver();
        registerNewFeedClickedReceiver();
    }

    @NotNull
    private boolean executeFirstTimeEvent() {
        if (isFirstTime()) {
            globalNavAnalytics.get().trackFirstTime(MainParentActivity.this);
        }
        return true;
    }

    private void showSelectedPage() {
        int tabPosition = HOME_MENU;
        if (getIntent().getExtras() != null) {
            tabPosition = getTabPositionFromIntent();
        }
        if (tabPosition > fragmentList.size() - 1) {
            tabPosition = HOME_MENU;
        }
        Fragment fragment = fragmentList.get(tabPosition);
        if (fragment != null) {
            this.currentFragment = fragment;
            selectFragment(fragment);
        }
    }

    private int getTabPositionFromIntent() {
        int position = getIntent().getExtras().getInt(ARGS_TAB_POSITION, -1);
        if (position != -1) return position;

        try {
            String posString = getIntent().getExtras().getString(ARGS_TAB_POSITION);
            return Integer.parseInt(posString);
        } catch (Exception e) {
            return HOME_MENU;
        }
    }

    private void startSelectedPagePerformanceMonitoring() {
        int tabPosition = HOME_MENU;
        if (getIntent().getExtras() != null) {
            tabPosition = getTabPositionFromIntent();
        }
        switch (tabPosition) {
            case HOME_MENU:
                startHomePerformanceMonitoring();
                break;
            case OS_MENU:
                startOfficialStorePerformanceMonitoring();
        }
    }

    private void handleAppLinkBottomNavigation() {

        if (bottomNavigation == null) return;

        if (getIntent().getExtras() != null) {
            int tabPosition = getTabPositionFromIntent();
            switch (tabPosition) {
                case FEED_MENU:
                    bottomNavigation.setSelected(FEED_MENU);
                    break;
                case OS_MENU:
                    bottomNavigation.setSelected(OS_MENU);
                    break;
                case ACCOUNT_MENU:
                    bottomNavigation.setSelected(ACCOUNT_MENU);
                    break;
                case RECOMENDATION_LIST:
                case HOME_MENU:
                default:
                    bottomNavigation.setSelected(HOME_MENU);
                    break;
            }
        } else {
            bottomNavigation.setSelected(HOME_MENU);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterNewFeedClickedReceiver();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkIsNeedUpdateIfComeFromUnsupportedApplink(intent);
        checkApplinkCouponCode(intent);
        checkAgeVerificationExtra(intent);

        setIntent(intent);
        showSelectedPage();
        handleAppLinkBottomNavigation();
    }

    private void initInjector() {
        DaggerGlobalNavComponent.builder()
                .baseAppComponent(getApplicationComponent())
                .globalNavModule(new GlobalNavModule())
                .build()
                .inject(this);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void reInitInjector(GlobalNavComponent globalNavComponent) {
        globalNavComponent.inject(this);

        presenter.get().setView(this);
    }

    private int getPositionFragmentByMenu(int i) {
        int position = HOME_MENU;
        if (i == FEED_MENU) {
            position = FEED_MENU;
        } else if (i == OS_MENU) {
            position = OS_MENU;
        } else if (i == CART_MENU) {
            position = CART_MENU;
        } else if (i == ACCOUNT_MENU) {
            position = ACCOUNT_MENU;
        }
        return position;
    }

    private void checkAgeVerificationExtra(Intent intent) {
        if (intent.hasExtra(ApplinkConstInternalCategory.PARAM_EXTRA_SUCCESS)) {
            Toaster.INSTANCE.showErrorWithAction(this.findViewById(android.R.id.content),
                    intent.getStringExtra(ApplinkConstInternalCategory.PARAM_EXTRA_SUCCESS),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.general_label_ok), (v) -> {
                    });
        }
    }

    private void setupStatusBar() {
        //apply inset to allow recyclerview scrolling behind status bar
        fragmentContainer.setFitsSystemWindows(false);
        fragmentContainer.requestApplyInsets();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = fragmentContainer.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            fragmentContainer.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0));
        }

        //make full transparent statusBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void selectFragment(Fragment fragment) {
        configureStatusBarBasedOnFragment(fragment);
        openFragment(fragment);
        setBadgeNotifCounter(fragment);
    }

    private void openFragment(Fragment fragment) {
        handler.post(() -> {
            String backStateName = fragment.getClass().getName();

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();

            Fragment currentFrag = manager.findFragmentByTag(backStateName);
            if (currentFrag != null && manager.getFragments().size() > 0) {
                showSelectedFragment(fragment, manager, ft);
            } else {
                ft.add(R.id.container, fragment, backStateName); // add fragment if there re not registered on fragmentManager
                showSelectedFragment(fragment, manager, ft);
                FragmentLifecycleObserver.INSTANCE.onFragmentSelected(fragment);
            }
            ft.commitNowAllowingStateLoss();
        });
    }

    private void showSelectedFragment(Fragment fragment, FragmentManager manager, FragmentTransaction ft) {
        for (int i = 0; i < manager.getFragments().size(); i++) {
            Fragment frag = manager.getFragments().get(i);
            if (frag.getClass().getName().equalsIgnoreCase(fragment.getClass().getName())) {
                ft.show(frag); // only show fragment what you want to show
                FragmentLifecycleObserver.INSTANCE.onFragmentSelected(frag);
            } else {
                ft.hide(frag); // hide all fragment
                FragmentLifecycleObserver.INSTANCE.onFragmentUnSelected(frag);
            }
        }
    }

    private void configureStatusBarBasedOnFragment(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setupStatusBarInMarshmallowAbove(fragment);
        } else {
            setupStatusBar();
        }
    }

    private void scrollToTop(Fragment fragment) {
        if (fragment.getUserVisibleHint() && fragment instanceof FragmentListener) {
            ((FragmentListener) fragment).onScrollToTop();
        }
    }

    private void setupStatusBarInMarshmallowAbove(Fragment fragment) {
        if (getIsFragmentLightStatusBar(fragment)) {
            requestStatusBarLight();
        } else {
            requestStatusBarDark();
        }
    }

    private boolean getIsFragmentLightStatusBar(Fragment fragment) {
        if (fragment instanceof FragmentListener) {
            return ((FragmentListener) fragment).isLightThemeStatusBar();
        }
        return false;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setUserSession(UserSessionInterface userSession) {
        this.userSession = (Lazy<UserSessionInterface>) userSession;
    }


    @Override
    public BaseAppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // if user is downloading the update (in app update feature),
        // check if the download is finished or is in progress
        checkForInAppUpdateInProgressOrCompleted();
        presenter.get().onResume();

        if (userSession.get().isLoggedIn() && isUserFirstTimeLogin) {
            reloadPage();
        }
        isUserFirstTimeLogin = !userSession.get().isLoggedIn();

        addShortcuts();

        if (currentFragment != null) {
            configureStatusBarBasedOnFragment(currentFragment);
            FragmentLifecycleObserver.INSTANCE.onFragmentSelected(currentFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppUpdateManagerWrapper.onActivityResult(this, requestCode, resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * While refreshing the app update info, we also check whether we have updates in progress to
     * complete.
     *
     * <p>This is important, so the app doesn't forget about downloaded updates even if it gets killed
     * during the download or misses some notifications.
     */
    private void checkForInAppUpdateInProgressOrCompleted() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppUpdateManagerWrapper.checkUpdateInProgressOrCompleted(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.get().onDestroy();
    }

    private void reloadPage() {
        finish();
        startActivity(getIntent());
    }

    private List<Fragment> fragments() {
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(HomeInternalRouter.getHomeFragment(getIntent().getBooleanExtra(SCROLL_RECOMMEND_LIST, false)));
        fragmentList.add(RouteManager.instantiateFragment(this, FragmentConst.FEED_PLUS_CONTAINER_FRAGMENT, getIntent().getExtras()));
        fragmentList.add(OfficialHomeContainerFragment.newInstance(getIntent().getExtras()));
        fragmentList.add(CartFragment.newInstance(getIntent().getExtras(), MainParentActivity.class.getSimpleName()));
        fragmentList.add(AccountHomeFragment.newInstance(getIntent().getExtras()));

        return fragmentList;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public Fragment getFragment(int index) {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    @Override
    public void renderNotification(Notification notification) {
        this.notification = notification;
        if (bottomNavigation != null) {
            if (notification.getTotalCart() != 0) {
                bottomNavigation.setBadge(notification.getTotalCart(), CART_MENU, View.VISIBLE);
            } else {
                bottomNavigation.setBadge(notification.getTotalCart(), CART_MENU, View.INVISIBLE);
            }
            if (notification.getHaveNewFeed()) {
                bottomNavigation.setBadge(0, FEED_MENU, View.VISIBLE);
                Intent intent = new Intent(BROADCAST_FEED);
                intent.putExtra(PARAM_BROADCAST_NEW_FEED, notification.getHaveNewFeed());
                LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
            } else {
                bottomNavigation.setBadge(0, FEED_MENU, View.GONE);
            }
        }
        if (currentFragment != null)
            setBadgeNotifCounter(currentFragment);
    }

    @Override
    public void onStartLoading() {
    }

    @Override
    public void onError(String message) {
    }

    @Override
    public void onHideLoading() {
    }

    @Override
    public Context getContext() {
        return this;
    }

    public BaseAppComponent getApplicationComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }

    @Override
    public void onBackPressed() {
        doubleTapExit();
    }

    private void doubleTapExit() {
        if (doubleTapExit) {
            this.finish();
        } else {
            doubleTapExit = true;
            Toast.makeText(this, R.string.exit_message, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleTapExit = false, EXIT_DELAY_MILLIS);
        }
    }

    /**
     * Notification
     */
    private void setBadgeNotifCounter(Fragment fragment) {
        handler.post(() -> {
            if (fragment == null)
                return;

            if (fragment instanceof AllNotificationListener && notification != null) {
                int totalInbox = notification.getTotalInbox();
                if (useNewInbox) {
                    totalInbox = notification.totalNewInbox;
                }
                ((AllNotificationListener) fragment).onNotificationChanged(
                        notification.getTotalNotif(),
                        totalInbox,
                        notification.getTotalCart());
            }

            invalidateOptionsMenu();
        });
    }

    @Override
    public void onNotifyCart() {
        if (presenter != null)
            this.presenter.get().getNotificationData();
    }

    private void saveInstanceState(Bundle outState) {
        if (getIntent() != null) {
            outState.putBoolean(IS_RECURRING_APPLINK, presenter.get().isRecurringApplink());
        }
    }

    /**
     * Show Case on boarding
     */
    private ShowCaseDialog createShowCase() {
        return new ShowCaseBuilder()
                .backgroundContentColorRes(com.tokopedia.unifyprinciples.R.color.Unify_N700)
                .shadowColorRes(R.color.Unify_N700_68)
                .titleTextColorRes(com.tokopedia.unifyprinciples.R.color.Unify_N0)
                .textColorRes(com.tokopedia.unifyprinciples.R.color.Unify_N150)
                .textSizeRes(R.dimen.sp_12)
                .titleTextSizeRes(R.dimen.sp_16)
                .nextStringRes(R.string.next)
                .prevStringRes(R.string.previous)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }

    @Override
    public void onReadytoShowBoarding(ArrayList<ShowCaseObject> showCaseObjects) {

        if (bottomNavigation != null) {
            final String showCaseTag = MainParentActivity.class.getName() + ".bottomNavigation";
            if (ShowCasePreference.hasShown(this, showCaseTag) || showCaseDialog != null
                    || showCaseObjects == null) {
                return;
            }

            showCaseDialog = createShowCase();

            int bottomNavTopPos = bottomNavigation.getTop();
            int bottomNavBottomPos = bottomNavigation.getBottom();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                bottomNavBottomPos =
                        bottomNavBottomPos - DisplayMetricUtils.getStatusBarHeight(this);
                bottomNavTopPos =
                        bottomNavTopPos - DisplayMetricUtils.getStatusBarHeight(this);
            }
            ArrayList<ShowCaseObject> showcases = new ArrayList<>();
            showcases.add(new ShowCaseObject(
                    bottomNavigation,
                    getString(R.string.title_showcase),
                    getString(R.string.desc_showcase))
                    .withCustomTarget(new int[]{
                            bottomNavigation.getLeft(),
                            bottomNavTopPos,
                            bottomNavigation.getRight(),
                            bottomNavBottomPos}));
            showcases.addAll(showCaseObjects);

            showCaseDialog.show(this, showCaseTag, showcases);
        }
    }

    private Boolean isFirstTime() {
        LocalCacheHandler cache = new LocalCacheHandler(this, GlobalNavConstant.Cache.KEY_FIRST_TIME);
        return cache.getBoolean(GlobalNavConstant.Cache.KEY_IS_FIRST_TIME, false);
    }

    private void checkAppUpdateAndInApp() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // if download finished or flexible update is in progress, we do not need to show the update dialog
            AppUpdateManagerWrapper.checkUpdateInFlexibleProgressOrCompleted(this, isOnProgress -> {
                if (!isOnProgress) {
                    checkAppUpdateRemoteConfig();
                }
                return null;
            });
        } else {
            checkAppUpdateRemoteConfig();
        }
    }

    private void checkAppUpdateRemoteConfig() {
        appUpdate.checkApplicationUpdate(new ApplicationUpdate.OnUpdateListener() {
            @Override
            public void onNeedUpdate(DetailUpdate detail) {
                if (!isFinishing()) {
                    AppUpdateDialogBuilder appUpdateDialogBuilder =
                            new AppUpdateDialogBuilder(
                                    MainParentActivity.this,
                                    detail,
                                    new AppUpdateDialogBuilder.Listener() {
                                        @Override
                                        public void onPositiveButtonClicked(DetailUpdate detail) {
                                            globalNavAnalytics.get().eventClickAppUpdate(detail.isForceUpdate());
                                        }

                                        @Override
                                        public void onNegativeButtonClicked(DetailUpdate detail) {
                                            globalNavAnalytics.get().eventClickCancelAppUpdate(detail.isForceUpdate());
                                        }
                                    }
                            );
                    appUpdateDialogBuilder.getAlertDialog().show();
                    globalNavAnalytics.get().eventImpressionAppUpdate(detail.isForceUpdate());
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onNotNeedUpdate() {
                if (!isFinishing()) {
                    checkIsNeedUpdateIfComeFromUnsupportedApplink(MainParentActivity.this.getIntent());
                }
            }
        });
    }

    private void checkIsNeedUpdateIfComeFromUnsupportedApplink(Intent intent) {
        if (intent.getBooleanExtra(ApplinkRouter.EXTRA_APPLINK_UNSUPPORTED, false)) {
            if (getApplication() instanceof ApplinkRouter) {
                ((ApplinkRouter) getApplication()).getApplinkUnsupported(this).showAndCheckApplinkUnsupported();
            }
        }
    }

    private void checkApplinkCouponCode(Intent intent) {
        if (!presenter.get().isRecurringApplink() && !TextUtils.isEmpty(intent.getStringExtra(ApplinkRouter.EXTRA_APPLINK))) {
            String applink = intent.getStringExtra(ApplinkRouter.EXTRA_APPLINK);

            if (intent.getStringExtra(MO_ENGAGE_COUPON_CODE) != null &&
                    !TextUtils.isEmpty(intent.getStringExtra(MO_ENGAGE_COUPON_CODE))) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getResources().getString(R.string.coupon_copy_text), intent.getStringExtra(MO_ENGAGE_COUPON_CODE));
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                }

                Toast.makeText(this, getResources().getString(R.string.coupon_copy_text), Toast.LENGTH_LONG).show();
            }

            RouteManager.route(this, applink);

            presenter.get().setIsRecurringApplink(true);
        }
    }

    private void initNewFeedClickReceiver() {
        newFeedClickedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null && intent.getAction().equals(BROADCAST_FEED)) {
                    boolean isHaveNewFeed = intent.getBooleanExtra(PARAM_BROADCAST_NEW_FEED_CLICKED, false);
                    if (isHaveNewFeed) {
                        bottomNavigation.setBadge(0, FEED_MENU, View.VISIBLE);
                    } else {
                        bottomNavigation.setBadge(0, FEED_MENU, View.GONE);
                    }
                }
            }
        };
    }

    private void registerNewFeedClickedReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_FEED);
        LocalBroadcastManager.getInstance(getContext().getApplicationContext()).registerReceiver(newFeedClickedReceiver, intentFilter);
    }

    private void unRegisterNewFeedClickedReceiver() {
        LocalBroadcastManager.getInstance(getContext().getApplicationContext()).unregisterReceiver(newFeedClickedReceiver);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public MainParentPresenter getPresenter() {
        return (MainParentPresenter) presenter;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setPresenter(MainParentPresenter presenter) {
        this.presenter = (Lazy<MainParentPresenter>) presenter;
    }

    @NotNull
    private boolean addShortcuts() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            try {
                ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
                if (shortcutManager != null) {
                    shortcutManager.removeAllDynamicShortcuts();

                    List<ShortcutInfo> shortcutInfos = new ArrayList<>();
                    Bundle args = new Bundle();
                    args.putBoolean(GlobalNavConstant.EXTRA_APPLINK_FROM_PUSH, true);
                    args.putBoolean(GlobalNavConstant.FROM_APP_SHORTCUTS, true);

                    Intent intentHome = MainParentActivity.start(MainParentActivity.this);
                    intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentHome.setAction(RouteManager.INTERNAL_VIEW);

                    Intent productIntent = RouteManager.getIntent(MainParentActivity.this, ApplinkConstInternalDiscovery.AUTOCOMPLETE);
                    productIntent.setAction(RouteManager.INTERNAL_VIEW);
                    productIntent.putExtras(args);

                    ShortcutInfo productShortcut = new ShortcutInfo.Builder(MainParentActivity.this, SHORTCUT_BELI_ID)
                            .setShortLabel(getResources().getString(R.string.navigation_home_label_longpress_beli))
                            .setLongLabel(getResources().getString(R.string.navigation_home_label_longpress_beli))
                            .setIcon(Icon.createWithResource(MainParentActivity.this, R.drawable.main_parent_navigation_ic_search_shortcut))
                            .setIntents(new Intent[]{intentHome, productIntent})
                            .setRank(0)
                            .build();
                    shortcutInfos.add(productShortcut);

                    if (userSession.get().isLoggedIn()) {
                        Intent wishlistIntent = RouteManager.getIntent(MainParentActivity.this, ApplinkConst.NEW_WISHLIST);
                        wishlistIntent.setAction(Intent.ACTION_VIEW);
                        wishlistIntent.putExtras(args);

                        ShortcutInfo wishlistShortcut = new ShortcutInfo.Builder(MainParentActivity.this, SHORTCUT_SHARE_ID)
                                .setShortLabel(getResources().getString(R.string.navigation_home_label_longpress_share))
                                .setLongLabel(getResources().getString(R.string.navigation_home_label_longpress_share))
                                .setIcon(Icon.createWithResource(MainParentActivity.this, R.drawable.ic_wishlist_shortcut))
                                .setIntents(new Intent[]{intentHome, wishlistIntent})
                                .setRank(1)
                                .build();
                        shortcutInfos.add(wishlistShortcut);
                    }

                    Intent digitalIntent = RouteManager.getIntent(MainParentActivity.this, ApplinkConst.DIGITAL_SUBHOMEPAGE_HOME);
                    digitalIntent.setAction(Intent.ACTION_VIEW);
                    digitalIntent.putExtras(args);

                    ShortcutInfo digitalShortcut = new ShortcutInfo.Builder(MainParentActivity.this, SHORTCUT_DIGITAL_ID)
                            .setShortLabel(getResources().getString(R.string.navigation_home_label_longpress_bayar))
                            .setLongLabel(getResources().getString(R.string.navigation_home_label_longpress_bayar))
                            .setIcon(Icon.createWithResource(MainParentActivity.this, R.drawable.ic_pay_shortcut))
                            .setIntents(new Intent[]{intentHome, digitalIntent})
                            .setRank(2)
                            .build();
                    shortcutInfos.add(digitalShortcut);

                    if (userSession.get().isLoggedIn()) {
                        String shopID = userSession.get().getShopId();

                        Intent shopIntent;
                        if (!userSession.get().hasShop()) {
                            shopIntent = RouteManager.getIntent(getContext(), OPEN_SHOP);
                        } else {
                            shopIntent = RouteManager.getIntent(getContext(), SHOP_PAGE, shopID);
                        }

                        shopIntent.setAction(Intent.ACTION_VIEW);
                        shopIntent.putExtras(args);

                        ShortcutInfo shopShortcut = new ShortcutInfo.Builder(MainParentActivity.this, SHORTCUT_SHOP_ID)
                                .setShortLabel(getResources().getString(R.string.navigation_home_label_longpress_jual))
                                .setLongLabel(getResources().getString(R.string.navigation_home_label_longpress_jual))
                                .setIcon(Icon.createWithResource(MainParentActivity.this, R.drawable.ic_sell_shortcut))
                                .setIntents(new Intent[]{intentHome, shopIntent})
                                .setRank(3)
                                .build();
                        shortcutInfos.add(shopShortcut);
                    }

                    shortcutManager.addDynamicShortcuts(shortcutInfos);

                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void onRefreshNotification() {
        presenter.get().getNotificationData();
    }

    private void startMainParentPerformanceMonitoring() {
        mainParentPerformanceMonitoring = PerformanceMonitoring.start(MAIN_PARENT_PERFORMANCE_MONITORING_KEY);
    }

    @Override
    public void startHomePerformanceMonitoring() {
        pageLoadTimePerformanceCallback = new PageLoadTimePerformanceCallback(
                HOME_PERFORMANCE_MONITORING_PREPARE_METRICS,
                HOME_PERFORMANCE_MONITORING_NETWORK_METRICS,
                HOME_PERFORMANCE_MONITORING_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        );

        getPageLoadTimePerformanceInterface().startMonitoring(HOME_PERFORMANCE_MONITORING_KEY);
        getPageLoadTimePerformanceInterface().startPreparePagePerformanceMonitoring();
    }

    @Override
    public void stopHomePerformanceMonitoring(boolean isCache) {
        if (getPageLoadTimePerformanceInterface() != null) {
            if (isCache) {
                getPageLoadTimePerformanceInterface().addAttribution(
                        HOME_PERFORMANCE_MONITORING_CACHE_ATTRIBUTION,
                        HOME_PERFORMANCE_MONITORING_CACHE_VALUE);
            } else {
                getPageLoadTimePerformanceInterface().addAttribution(
                        HOME_PERFORMANCE_MONITORING_CACHE_ATTRIBUTION,
                        HOME_PERFORMANCE_MONITORING_NETWORK_VALUE);
            }
            getPageLoadTimePerformanceInterface().stopRenderPerformanceMonitoring();
            getPageLoadTimePerformanceInterface().stopMonitoring();
        }
    }

    @Override
    public PageLoadTimePerformanceInterface getPageLoadTimePerformanceInterface() {
        return pageLoadTimePerformanceCallback;
    }

    @Override
    public void requestStatusBarDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void requestStatusBarLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void stopOfficialStorePerformanceMonitoring() {
        if (officialStorePageLoadTimePerformanceCallback != null) {
            officialStorePageLoadTimePerformanceCallback.stopRenderPerformanceMonitoring();
            officialStorePageLoadTimePerformanceCallback.stopMonitoring();
            officialStorePageLoadTimePerformanceCallback = null;
        }
    }

    @Override
    public void startOfficialStorePerformanceMonitoring() {
        if (officialStorePageLoadTimePerformanceCallback == null) {
            officialStorePageLoadTimePerformanceCallback = new PageLoadTimePerformanceCallback(
                    OFFICIAL_STORE_PERFORMANCE_MONITORING_PREPARE_METRICS,
                    OFFICIAL_STORE_PERFORMANCE_MONITORING_NETWORK_METRICS,
                    OFFICIAL_STORE_PERFORMANCE_MONITORING_RENDER_METRICS,
                    0,
                    0,
                    0,
                    0,
                    null
            );
            officialStorePageLoadTimePerformanceCallback.startMonitoring(OFFICIAL_STORE_PERFORMANCE_MONITORING_KEY);
            officialStorePageLoadTimePerformanceCallback.startPreparePagePerformanceMonitoring();
        }
    }

    @Override
    public PageLoadTimePerformanceInterface getOfficialStorePageLoadTimePerformanceInterface() {
        return officialStorePageLoadTimePerformanceCallback;
    }

    @Override
    public boolean menuClicked(int index, int id) {
        int position = getPositionFragmentByMenu(index);
        this.currentSelectedFragmentPosition = position;
        if (!isFirstNavigationImpression) {
            if (isNewNavigation) {
                String pageName = "";
                if (menu.get(index).getTitle().equals(getResources().getString(R.string.home))) {
                    pageName = "/";
                } else if (menu.get(index).getTitle().equals(getResources().getString(R.string.official))) {
                    pageName = "OS Homepage";
                } else if (menu.get(index).getTitle().equals(getResources().getString(R.string.feed))) {
                    pageName = "Feed";
                }
                globalNavAnalytics.get().eventBottomNavigationDrawer(pageName, menu.get(index).getTitle(), userSession.get().getUserId());
            } else {
                globalNavAnalytics.get().eventBottomNavigation(menu.get(index).getTitle()); // push analytics
            }
        }
        isFirstNavigationImpression = false;

        if (position == FEED_MENU) {
            presenter.get().getNotificationData();
            Intent intent = new Intent(BROADCAST_FEED);
            LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
        }

        if ((position == CART_MENU || position == ACCOUNT_MENU) && !presenter.get().isUserLogin()) {
            Intent intent = RouteManager.getIntent(this, ApplinkConst.LOGIN);
            intent.putExtra(PARAM_SOURCE, SOURCE_ACCOUNT);
            startActivity(intent);
            return false;
        }

        Fragment fragment = fragmentList.get(position);
        if (fragment != null) {
            this.currentFragment = fragment;
            selectFragment(fragment);
        }
        return true;
    }

    @Override
    public void menuReselected(int position, int id) {
        Fragment fragment = fragmentList.get(getPositionFragmentByMenu(position));
        scrollToTop(fragment); // enable feature scroll to top for home & feed
    }

    @Override
    public boolean isNavigationRevamp() {
        return isNewNavigation;
    }

    public void populateBottomNavigationView() {
        menu.add(new BottomMenu(R.id.menu_home, getResources().getString(R.string.home), R.raw.bottom_nav_home, R.raw.bottom_nav_home_to_enabled, R.drawable.ic_bottom_nav_home_active, R.drawable.ic_bottom_nav_home_enabled, com.tokopedia.unifyprinciples.R.color.Unify_G500, true, 1f, 3f));
        menu.add(new BottomMenu(R.id.menu_feed, getResources().getString(R.string.feed), R.raw.bottom_nav_feed, R.raw.bottom_nav_feed_to_enabled, R.drawable.ic_bottom_nav_feed_active, R.drawable.ic_bottom_nav_feed_enabled, com.tokopedia.unifyprinciples.R.color.Unify_G500, true, 1f, 3f));
        menu.add(new BottomMenu(R.id.menu_os, getResources().getString(R.string.official), R.raw.bottom_nav_official, R.raw.bottom_nav_os_to_enabled, R.drawable.ic_bottom_nav_os_active, R.drawable.ic_bottom_nav_os_enabled, com.tokopedia.unifyprinciples.R.color.Unify_P500, true, 1f, 3f));
        if (!isNewNavigation) {
            menu.add(new BottomMenu(R.id.menu_cart, getResources().getString(R.string.keranjang), R.raw.bottom_nav_cart, R.raw.bottom_nav_cart_to_enabled, R.drawable.ic_bottom_nav_cart_active, R.drawable.ic_bottom_nav_cart_enabled, com.tokopedia.unifyprinciples.R.color.Unify_G500, true, 1f, 3f));
            if (userSession.get().isLoggedIn()) {
                menu.add(new BottomMenu(R.id.menu_account, getResources().getString(R.string.akun), R.raw.bottom_nav_account, R.raw.bottom_nav_account_to_enabled, R.drawable.ic_bottom_nav_account_active, R.drawable.ic_bottom_nav_account_enabled, com.tokopedia.unifyprinciples.R.color.Unify_G500, true, 1f, 3f));
            } else {
                menu.add(new BottomMenu(R.id.menu_account, getResources().getString(R.string.akun_non_login), null, null, R.drawable.ic_bottom_nav_nonlogin_enabled, null, com.tokopedia.unifyprinciples.R.color.Unify_G500, true, 1f, 3f));
            }
        }
        bottomNavigation.setMenu(menu, isNewNavigation);
        handleAppLinkBottomNavigation();
    }

    private void validateNavigationRollence() {
        try {
            String rollanceNavType = RemoteConfigInstance.getInstance().getABTestPlatform().getString(ROLLANCE_EXP_NAME, ROLLANCE_VARIANT_OLD);
            this.isNewNavigation = rollanceNavType.equalsIgnoreCase(ROLLANCE_VARIANT_REVAMP);
        } catch (Exception e) {
            this.isNewNavigation = false;
        }
    }
}
