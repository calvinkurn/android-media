package com.tokopedia.navigation.presentation.activity;

import static com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_SOURCE;
import static com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP;
import static com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_PAGE;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwnerKt;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleObserver;
import com.tokopedia.abstraction.base.view.model.InAppCallback;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.analytics.performance.perf.BlocksPerformanceTrace;
import com.tokopedia.analytics.performance.perf.BlocksSummaryModel;
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback;
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.DeeplinkDFMapper;
import com.tokopedia.applink.FragmentConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;
import com.tokopedia.applink.internal.ApplinkConstInternalContent;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker;
import com.tokopedia.dynamicfeatures.DFInstaller;
import com.tokopedia.home.HomeInternalRouter;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment;
import com.tokopedia.inappupdate.AppUpdateManagerWrapper;
import com.tokopedia.kotlin.extensions.view.StringExtKt;
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
import com.tokopedia.navigation.util.FeedCoachMark;
import com.tokopedia.navigation.util.MainParentServerLogger;
import com.tokopedia.navigation_common.listener.AllNotificationListener;
import com.tokopedia.navigation_common.listener.CartNotifyListener;
import com.tokopedia.navigation_common.listener.FragmentListener;
import com.tokopedia.navigation_common.listener.HomeCoachmarkListener;
import com.tokopedia.navigation_common.listener.HomePerformanceMonitoringListener;
import com.tokopedia.navigation_common.listener.MainParentStateListener;
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener;
import com.tokopedia.navigation_common.listener.RefreshNotificationListener;
import com.tokopedia.notifications.utils.NotificationUserSettingsTracker;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.telemetry.ITelemetryActivity;
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
import java.util.Set;

import javax.inject.Inject;

import dagger.Lazy;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import rx.android.BuildConfig;

/**
 * Created by meta on 19/06/18.
 */
public class MainParentActivity extends BaseActivity implements
        HasComponent,
        MainParentView,
        CartNotifyListener,
        RefreshNotificationListener,
        MainParentStatusBarListener,
        HomePerformanceMonitoringListener,
        IBottomClickListener,
        MainParentStateListener,
        ITelemetryActivity,
        InAppCallback,
        HomeCoachmarkListener
{

    public static final String MO_ENGAGE_COUPON_CODE = "coupon_code";
    public static final String ARGS_TAB_POSITION = "TAB_POSITION";
    public static final int HOME_MENU = 0;
    public static final int FEED_MENU = 1;
    public static final int OS_MENU = 2;
    public static final int CART_MENU = 3;
    public static final int ACCOUNT_MENU = 4;
    public static final int RECOMENDATION_LIST = 5;
    public static final int REQUEST_CODE_LOGIN = 12137;
    public static final String FEED_PAGE = "FeedIntermediaryFragment";
    public static final int UOH_MENU = 4;
    public static final int WISHLIST_MENU = 3;
    public static final String DEFAULT_NO_SHOP = "0";
    public static final String BROADCAST_FEED = "BROADCAST_FEED";
    public static final String FEED_IS_VISIBLE = "FEED_IS_VISIBLE";
    public static final String BROADCAST_VISIBLITY = "BROADCAST_VISIBILITY";
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
    private static final String SOURCE_ACCOUNT = "account";
    private static final String HOME_PERFORMANCE_MONITORING_KEY = "mp_home";
    private static final String HOME_PERFORMANCE_MONITORING_PREPARE_METRICS = "home_plt_start_page_metrics";
    private static final String HOME_PERFORMANCE_MONITORING_NETWORK_METRICS = "home_plt_network_request_page_metrics";
    private static final String HOME_PERFORMANCE_MONITORING_RENDER_METRICS = "home_plt_render_page_metrics";
    private static final String MAIN_PARENT_ON_CREATE_METRICS = "mp_main_parent_on_create_metrics";
    private static final String MAIN_PARENT_ON_START_METRICS = "mp_main_parent_on_start_metrics";
    private static final String MAIN_PARENT_ON_RESUME_METRICS = "mp_main_parent_on_resume_metrics";

    private static final String PERFORMANCE_MONITORING_CACHE_ATTRIBUTION = "dataSource";
    private static final String PERFORMANCE_MONITORING_CACHE_VALUE = "Cache";
    private static final String PERFORMANCE_MONITORING_NETWORK_VALUE = "Network";

    private static final String MAIN_PARENT_PERFORMANCE_MONITORING_KEY = "mp_slow_rendering_perf";

    private static final String MAIN_PARENT_LOAD_ON_RESUME = "main_parent_load_on_resume";

    private static final String OS_KEY_MOBILE = "mobile";
    public static final String PAGE_OS_HOMEPAGE = "OS Homepage";
    public static final String PAGE_FEED = "Feed";
    public static final String PAGE_DAFTAR_TRANSAKSI = "transaction page";
    public static final String PAGE_WISHLIST = "wishlist page";
    public static final int RANK_SHOP_SHORTCUT = 3;
    public static final int RANK_DIGITAL_SHORTCUT = 2;
    public static final int RANK_WISHLIST_SHORTCUT = 1;
    public static final String UOH_SOURCE_FILTER_KEY = "source_filter";
    public static final String PARAM_ACTIVITY_ORDER_HISTORY = "activity_order_history";
    public static final String PARAM_HOME = "home";
    public static final String PARAM_ACTIVITY_WISHLIST_COLLECTION = "activity_wishlist_collection";
    private static final String SUFFIX_ALPHA = "-alpha";

    public static final String UOH_PAGE = "UohListFragment";

    public static final String WISHLIST_COLLECTION_PAGE = "WishlistCollectionFragment";
    private static final String DISCOVERY_APPLINK = "discovery_applink";
    private static final String DISCOVERY_APPLINK_TARGET = "tokopedia://discovery/sos";
    private static final String DISCOVERY_PAGE_SOURCE = "discovery_page_source";
    private static final String DISCOVERY_END_POINT = "end_point";
    private static final String SOS_END_POINT = "sos";

    private static final String PERFORMANCE_TRACE_HOME = "home";

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

    private View lineBottomNav;
    List<Fragment> fragmentList;
    private Notification notification;
    Fragment currentFragment;
    private int currentSelectedFragmentPosition = HOME_MENU;
    private SparseArray<PerformanceData> fragmentPerformanceDatas = new SparseArray<>();
    private boolean isUserFirstTimeLogin = false;
    private boolean doubleTapExit = false;
    private SharedPreferences cacheManager;
    private Handler handler = new Handler();
    private FrameLayout fragmentContainer;
    private boolean isFirstNavigationImpression = false;
    private boolean isFeedClickedFortheFirstTime = true;
    private boolean useNewNotificationOnNewInbox = false;
    private RemoteConfigInstance remoteConfigInstance;

    private PerformanceMonitoring officialStorePerformanceMonitoring;

    private PerformanceMonitoring mainParentPerformanceMonitoring;

    private BlocksPerformanceTrace performanceTrace;

    private PageLoadTimePerformanceCallback pageLoadTimePerformanceCallback;
    private PageLoadTimePerformanceCallback mainParentPageLoadTimePerformanceCallback;

    private String embracePageName = "";

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
        //changes for triggering unittest checker
        startSelectedPagePerformanceMonitoring();
        startMainParentPerformanceMonitoring();
        try {
            performanceTrace = new BlocksPerformanceTrace(
                    this.getContext().getApplicationContext(),
                    PERFORMANCE_TRACE_HOME,
                    LifecycleOwnerKt.getLifecycleScope(this),
                    this,
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pageLoadTimePerformanceCallback != null) {
            pageLoadTimePerformanceCallback.startCustomMetric(MAIN_PARENT_ON_CREATE_METRICS);
        }

        super.onCreate(savedInstanceState);
        initInjector();
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
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(executeEventsWeave, RemoteConfigKey.ENABLE_ASYNC_OPENHOME_EVENT, getContext(), true);
        installDFonBackground();
        runRiskWorker();

        if (pageLoadTimePerformanceCallback != null && pageLoadTimePerformanceCallback.getCustomMetric().containsKey(MAIN_PARENT_ON_CREATE_METRICS)) {
            pageLoadTimePerformanceCallback.stopCustomMetric(MAIN_PARENT_ON_CREATE_METRICS);
        }
        sendNotificationUserSetting();
    }

    private void sendNotificationUserSetting() {
        if (userSession.get().isLoggedIn()) {
            new NotificationUserSettingsTracker(getApplicationContext()).sendNotificationUserSettings();
        }
    }

    private void runRiskWorker() {
        // Most of workers do nothing if it has already succeed previously.
        SubmitDeviceWorker.Companion.scheduleWorker(getApplicationContext(), false);
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
        moduleNameList.add(DeeplinkDFMapper.DF_DIGITAL);
        moduleNameList.add(DeeplinkDFMapper.DF_TRAVEL);
        moduleNameList.add(DeeplinkDFMapper.DF_ENTERTAINMENT);
        moduleNameList.add(DeeplinkDFMapper.DF_TOKOPEDIA_NOW);
        moduleNameList.add(DeeplinkDFMapper.DF_TOKOFOOD);
        moduleNameList.add(DeeplinkDFMapper.DF_MERCHANT_NONLOGIN);
        moduleNameList.add(DeeplinkDFMapper.DF_DILAYANI_TOKOPEDIA);
        if (BuildConfig.VERSION_NAME.endsWith(SUFFIX_ALPHA) && remoteConfig.get().getBoolean(RemoteConfigKey.ENABLE_APLHA_OBSERVER, true)) {
            moduleNameList.add(DeeplinkDFMapper.DF_ALPHA_TESTING);
        }
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
        if (pageLoadTimePerformanceCallback != null) {
            pageLoadTimePerformanceCallback.startCustomMetric(MAIN_PARENT_ON_START_METRICS);
        }
        if (!GlobalConfig.ENABLE_MACROBENCHMARK_UTIL) {
            if (isFirstTimeUser()) {
                setDefaultShakeEnable();
                routeOnboarding();
            }
        }
        if (pageLoadTimePerformanceCallback != null && pageLoadTimePerformanceCallback.getCustomMetric().containsKey(MAIN_PARENT_ON_START_METRICS)) {
            pageLoadTimePerformanceCallback.stopCustomMetric(MAIN_PARENT_ON_START_METRICS);
        }
    }

    private void routeOnboarding() {
        RouteManager.route(this, ApplinkConstInternalMarketplace.ONBOARDING);
        finish();
    }

    private void setDefaultShakeEnable() {
        cacheManager.edit()
                .putBoolean(getString(R.string.pref_receive_shake_nav), true)
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
            reloadPage(HOME_MENU, false);
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

        bottomNavigation = findViewById(R.id.bottom_navbar);
        lineBottomNav = findViewById(R.id.line_bottom_nav);

        WeaveInterface firstTimeWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return executeFirstTimeEvent();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(firstTimeWeave, RemoteConfigKey.ENABLE_ASYNC_FIRSTTIME_EVENT, getContext(), true);
        checkApplinkCouponCode(getIntent());
        showSelectedPage();

        populateBottomNavigationView();
        bottomNavigation.setMenuClickListener(this);
    }

    @NotNull
    private boolean executeFirstTimeEvent() {
        if (isFirstTime()) {
            globalNavAnalytics.get().trackFirstTime(MainParentActivity.this);
        }
        return true;
    }

    private void showSelectedPage() {
        int tabPosition = getTabPositionFromIntent();
        if (tabPosition > fragmentList.size() - 1) {
            tabPosition = HOME_MENU;
        }
        Fragment fragment = fragmentList.get(tabPosition);
        if (fragment != null) {
            this.currentFragment = fragment;
            if (fragment.getClass().getName().equalsIgnoreCase(FragmentConst.FEED_PLUS_CONTAINER_FRAGMENT)) {
                try {
                    Bundle oldArgs = fragment.getArguments();
                    if (oldArgs == null) {
                        oldArgs = new Bundle();
                    }
                    if (getIntent().getExtras() != null) {
                        oldArgs.putAll(getIntent().getExtras());
                    }
                    fragment.setArguments(oldArgs);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            selectFragment(fragment);
        }
    }

    private int getTabPositionFromIntent() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            int position = getIntent().getExtras().getInt(ARGS_TAB_POSITION, -1);
            if (position != -1) return position;

            if (getIntent().getExtras().getString(ARGS_TAB_POSITION) != null) {
                try {
                    String posString = getIntent().getExtras().getString(ARGS_TAB_POSITION);
                    return StringExtKt.toIntOrZero(posString);
                } catch (Exception e) {
                    return HOME_MENU;
                }
            } else {
                return HOME_MENU;
            }
        } else if (
                getIntent() != null &&
                        getIntent().getData() != null &&
                        getIntent().getData().getQueryParameter(ARGS_TAB_POSITION) != null) {
            try {
                String posString = getIntent().getData().getQueryParameter(ARGS_TAB_POSITION);
                return StringExtKt.toIntOrZero(posString);
            } catch (Exception e) {
                return HOME_MENU;
            }
        } else {
            return HOME_MENU;
        }
    }

    private void startSelectedPagePerformanceMonitoring() {
        int tabPosition = HOME_MENU;
        tabPosition = getTabPositionFromIntent();
        switch (tabPosition) {
            case HOME_MENU:
                startHomePerformanceMonitoring();
                break;
        }
    }

    private void handleAppLinkBottomNavigation() {

        if (bottomNavigation == null) return;

        int tabPosition = getTabPositionFromIntent();
        switch (tabPosition) {
            case FEED_MENU:
                bottomNavigation.setSelected(FEED_MENU);
                break;
            case OS_MENU:
                bottomNavigation.setSelected(OS_MENU);
                break;
            case WISHLIST_MENU:
                bottomNavigation.setSelected(WISHLIST_MENU);
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
        } else if (i == UOH_MENU) {
            position = UOH_MENU;
        } else if (i == WISHLIST_MENU) {
            position = WISHLIST_MENU;
        }
        return position;
    }

    private void checkAgeVerificationExtra(Intent intent) {
        if (intent.hasExtra(ApplinkConstInternalCategory.PARAM_EXTRA_SUCCESS) && !isFinishing()) {
            Toaster.INSTANCE.showErrorWithAction(this.findViewById(android.R.id.content),
                    intent.getStringExtra(ApplinkConstInternalCategory.PARAM_EXTRA_SUCCESS),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(com.tokopedia.resources.common.R.string.general_label_ok), (v) -> {
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
            getWindow().setStatusBarColor(androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN0));
        }

        //make full transparent statusBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void selectFragment(Fragment fragment) {
        configureStatusBarBasedOnFragment(fragment);
        configureNavigationBarBasedOnFragment(fragment);
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
                if (!(frag instanceof HomeRevampFragment)) {
                    frag.setUserVisibleHint(true);
                }
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

    private void configureNavigationBarBasedOnFragment(Fragment fragment) {
        boolean isForceDarkMode = getIsFragmentForceDarkModeNavigationBar(fragment);
        bottomNavigation.forceDarkMode(isForceDarkMode);

        int lineColorRes = isForceDarkMode ? R.color.navigation_dms_line_bottom_nav_darkmode : com.tokopedia.unifyprinciples.R.color.Unify_NN50;
        lineBottomNav.setBackgroundResource(lineColorRes);
    }

    private void scrollToTop(Fragment fragment) {
        if (fragment != null && fragment.getUserVisibleHint() && fragment instanceof FragmentListener) {
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

    private boolean getIsFragmentForceDarkModeNavigationBar(Fragment fragment) {
        if (fragment instanceof FragmentListener) {
            return ((FragmentListener) fragment).isForceDarkModeNavigationBar();
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
        if (pageLoadTimePerformanceCallback != null && !getIntent().getBooleanExtra(MAIN_PARENT_LOAD_ON_RESUME, false)) {
            pageLoadTimePerformanceCallback.startCustomMetric(MAIN_PARENT_ON_RESUME_METRICS);
        }
        // if user is downloading the update (in app update feature),
        // check if the download is finished or is in progress
        checkForInAppUpdateInProgressOrCompleted();
        presenter.get().onResume();

        if (userSession.get().isLoggedIn() && isUserFirstTimeLogin) {
            int position = HOME_MENU;
            if (currentFragment.getClass().getSimpleName().equalsIgnoreCase(FEED_PAGE)) {
                for (int i = 0; i < fragmentList.size(); i++) {
                    Fragment frag = fragmentList.get(i);
                    if (frag.getClass().getName().equalsIgnoreCase(currentFragment.getClass().getName())) {
                        position = i;
                        break;
                    }
                }
            }
            reloadPage(position, true);
        }
        isUserFirstTimeLogin = !userSession.get().isLoggedIn();

        addShortcutsAsync();

        if (currentFragment != null) {
            configureStatusBarBasedOnFragment(currentFragment);
            FragmentLifecycleObserver.INSTANCE.onFragmentSelected(currentFragment);
        }

        if (pageLoadTimePerformanceCallback != null && pageLoadTimePerformanceCallback.getCustomMetric().containsKey(MAIN_PARENT_ON_RESUME_METRICS) && !getIntent().getBooleanExtra(MAIN_PARENT_LOAD_ON_RESUME, false)) {
            pageLoadTimePerformanceCallback.stopCustomMetric(MAIN_PARENT_ON_RESUME_METRICS);
            getIntent().putExtra(MAIN_PARENT_LOAD_ON_RESUME, true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppUpdateManagerWrapper.onActivityResult(this, requestCode, resultCode);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    boolean isSuccessRegister = data.getBooleanExtra(ApplinkConstInternalGlobal.PARAM_IS_SUCCESS_REGISTER, false);
                    if (isSuccessRegister) {
                        gotoNewUserZonePage();
                    }
                }
                break;
        }
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

    private void reloadPage(int position, boolean isJustLoggedIn) {
        getIntent().putExtra(ARGS_TAB_POSITION, position);

        boolean isPositionFeed = position == FEED_MENU;
        getIntent().putExtra(
                ApplinkConstInternalContent.UF_EXTRA_FEED_IS_JUST_LOGGED_IN,
                isPositionFeed && isJustLoggedIn
        );
        recreate();
    }

    private List<Fragment> fragments() {
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(HomeInternalRouter.getHomeFragment(getIntent().getBooleanExtra(SCROLL_RECOMMEND_LIST, false)));

        if (getSupportFragmentManager().findFragmentByTag(FragmentConst.FEED_PLUS_CONTAINER_FRAGMENT) == null) {
            fragmentList.add(
                    RouteManager.instantiateFragment(
                            this,
                            FragmentConst.FEED_PLUS_CONTAINER_FRAGMENT,
                            getIntent().getExtras()
                    )
            );
        } else {
            fragmentList.add(
                    getSupportFragmentManager().findFragmentByTag(
                            FragmentConst.FEED_PLUS_CONTAINER_FRAGMENT
                    )
            );
        }

        Bundle bundleSosDisco = getIntent().getExtras();
        if (bundleSosDisco == null) {
            bundleSosDisco = new Bundle();
        }
        bundleSosDisco.putString(DISCOVERY_APPLINK, DISCOVERY_APPLINK_TARGET);
        bundleSosDisco.putString(DISCOVERY_PAGE_SOURCE, PARAM_HOME);
        bundleSosDisco.putString(DISCOVERY_END_POINT, SOS_END_POINT);
        fragmentList.add(RouteManager.instantiateFragment(this, FragmentConst.DISCOVERY_FRAGMENT, bundleSosDisco));

        Bundle bundleWishlistCollection = getIntent().getExtras();
        if (bundleWishlistCollection == null) {
            bundleWishlistCollection = new Bundle();
        }
        bundleWishlistCollection.putString(PARAM_ACTIVITY_WISHLIST_COLLECTION, PARAM_HOME);
        bundleWishlistCollection.putString(WISHLIST_COLLECTION_PAGE, MainParentActivity.class.getSimpleName());
        fragmentList.add(RouteManager.instantiateFragment(this, FragmentConst.WISHLIST_COLLECTION_FRAGMENT, bundleWishlistCollection));

        Bundle bundleUoh = getIntent().getExtras();
        if (bundleUoh == null) {
            bundleUoh = new Bundle();
        }
        bundleUoh.putString(UOH_SOURCE_FILTER_KEY, "");
        bundleUoh.putString(PARAM_ACTIVITY_ORDER_HISTORY, PARAM_HOME);
        bundleUoh.putString(UOH_PAGE, MainParentActivity.class.getSimpleName());
        fragmentList.add(RouteManager.instantiateFragment(this, FragmentConst.UOH_LIST_FRAGMENT, bundleUoh));

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
            if (notification.getHaveNewFeed()) {
                Intent intent = new Intent(BROADCAST_FEED);
                intent.putExtra(PARAM_BROADCAST_NEW_FEED, notification.getHaveNewFeed());
                LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
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
            try {
                if (!isFinishing()) {
                    Toast.makeText(this, R.string.exit_message, Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(() -> doubleTapExit = false, EXIT_DELAY_MILLIS);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                int totalNotification = notification.getTotalNotif();
                if (useNewNotificationOnNewInbox) {
                    totalNotification = notification.totalNotificationOnNewInbox;
                }
                ((AllNotificationListener) fragment).onNotificationChanged(
                        totalNotification,
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

    private Boolean isFirstTime() {
        LocalCacheHandler cache = new LocalCacheHandler(this, GlobalNavConstant.Cache.KEY_FIRST_TIME);
        return cache.getBoolean(GlobalNavConstant.Cache.KEY_IS_FIRST_TIME, false);
    }

    protected InAppCallback getInAppCallback() {
        return new InAppCallback() {
            @Override
            public void onPositiveButtonInAppClicked(DetailUpdate detailUpdate) {
                globalNavAnalytics.get().eventClickAppUpdate(detailUpdate.isForceUpdate());
            }

            @Override
            public void onNegativeButtonInAppClicked(DetailUpdate detailUpdate) {
                globalNavAnalytics.get().eventClickCancelAppUpdate(detailUpdate.isForceUpdate());
            }

            @Override
            public void onNotNeedUpdateInApp() {
                if (!isFinishing()) {
                    checkIsNeedUpdateIfComeFromUnsupportedApplink(MainParentActivity.this.getIntent());
                }
            }

            @Override
            public void onNeedUpdateInApp(DetailUpdate detailUpdate) {
                globalNavAnalytics.get().eventImpressionAppUpdate(detailUpdate.isForceUpdate());
            }
        };
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
                try {
                    if (!isFinishing()) {
                        Toast.makeText(this, getResources().getString(R.string.coupon_copy_text), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            RouteManager.route(this, applink);

            presenter.get().setIsRecurringApplink(true);
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public MainParentPresenter getPresenter() {
        return (MainParentPresenter) presenter;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setPresenter(MainParentPresenter presenter) {
        this.presenter = (Lazy<MainParentPresenter>) presenter;
    }

    private void addShortcutsAsync() {
        WeaveInterface mainDaggerWeave = () -> {
            addShortcuts();
            return true;
        };
        Weaver.Companion.executeWeaveCoRoutineNow(mainDaggerWeave);
    }

    private void addShortcuts() {
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
                                .setRank(RANK_WISHLIST_SHORTCUT)
                                .build();
                        shortcutInfos.add(wishlistShortcut);
                    }

                    Intent digitalIntent = RouteManager.getIntent(MainParentActivity.this, ApplinkConst.RECHARGE_SUBHOMEPAGE_HOME_NEW);
                    digitalIntent.setAction(Intent.ACTION_VIEW);
                    digitalIntent.putExtras(args);

                    ShortcutInfo digitalShortcut = new ShortcutInfo.Builder(MainParentActivity.this, SHORTCUT_DIGITAL_ID)
                            .setShortLabel(getResources().getString(R.string.navigation_home_label_longpress_bayar))
                            .setLongLabel(getResources().getString(R.string.navigation_home_label_longpress_bayar))
                            .setIcon(Icon.createWithResource(MainParentActivity.this, R.drawable.ic_pay_shortcut))
                            .setIntents(new Intent[]{intentHome, digitalIntent})
                            .setRank(RANK_DIGITAL_SHORTCUT)
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
                                .setRank(RANK_SHOP_SHORTCUT)
                                .build();
                        shortcutInfos.add(shopShortcut);
                    }

                    shortcutManager.addDynamicShortcuts(shortcutInfos);

                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
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
                        PERFORMANCE_MONITORING_CACHE_ATTRIBUTION,
                        PERFORMANCE_MONITORING_CACHE_VALUE);
            } else {
                getPageLoadTimePerformanceInterface().addAttribution(
                        PERFORMANCE_MONITORING_CACHE_ATTRIBUTION,
                        PERFORMANCE_MONITORING_NETWORK_VALUE);
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
    public BlocksPerformanceTrace getBlocksPerformanceMonitoring() {
        return performanceTrace;
    }

    @Override
    public void requestStatusBarDark() {
        //for tokopedia lightmode, triggered when in top page
        //for tokopedia darkmode, triggered when not in top page
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            //to trigger white text when tokopedia darkmode not on top page
            requestStatusBarLight();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
                this.getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public void requestStatusBarLight() {
        //for tokopedia lightmode, triggered when not in top page
        //for tokopedia darkmode, triggered when in top page
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean menuClicked(int index, int id) {
        int position = getPositionFragmentByMenu(index);
        if (index != currentSelectedFragmentPosition) {
            currentFragment.setUserVisibleHint(false);
        }
        this.currentSelectedFragmentPosition = position;
        String pageName = "";
        String pageTitle = "";
        if (menu.size() > index) {
            pageTitle = menu.get(index).getTitle();
        }

        if (pageTitle.equals(getResources().getString(R.string.home))) {
            pageName = "/";
        } else if (pageTitle.equals(getResources().getString(R.string.official))) {
            pageName = PAGE_OS_HOMEPAGE;
        } else if (pageTitle.equals(getResources().getString(R.string.feed))) {
            getIntent().putExtra(ApplinkConstInternalContent.UF_EXTRA_FEED_ENTRY_POINT, ApplinkConstInternalContent.NAV_BUTTON_ENTRY_POINT);

            if (isFeedClickedFortheFirstTime) {
                isFeedClickedFortheFirstTime = false;
                globalNavAnalytics.get().userVisitsFeed(Boolean.toString(userSession.get().isLoggedIn()), userSession.get().getUserId());
            }
            pageName = PAGE_FEED;
        } else if (pageTitle.equals(getResources().getString(R.string.uoh))) {
            pageName = PAGE_DAFTAR_TRANSAKSI;
        } else if (pageTitle.equals(getResources().getString(R.string.wishlist))) {
            pageName = PAGE_WISHLIST;
        }

        if (!isFirstNavigationImpression) {
            globalNavAnalytics.get().eventBottomNavigationDrawer(pageName, menu.get(index).getTitle(), userSession.get().getUserId());
        }
        isFirstNavigationImpression = false;

        if (!menu.get(index).getTitle().equals(getResources().getString(R.string.feed))) {
            isFeedClickedFortheFirstTime = true;
            Intent intent = new Intent(BROADCAST_VISIBLITY);
            LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
        } else {
            presenter.get().getNotificationData();
            Intent intent = new Intent(BROADCAST_FEED);
            intent.putExtra(FEED_IS_VISIBLE, true);
            LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
        }

        if ((position == CART_MENU || position == UOH_MENU || position == WISHLIST_MENU) && !presenter.get().isUserLogin()) {
            Intent intent = RouteManager.getIntent(this, ApplinkConst.LOGIN);
            intent.putExtra(PARAM_SOURCE, SOURCE_ACCOUNT);
            startActivity(intent);
            return false;
        } else if (position == ACCOUNT_MENU && !presenter.get().isUserLogin()) {
            Intent intent = RouteManager.getIntent(this, ApplinkConst.LOGIN);
            intent.putExtra(PARAM_SOURCE, SOURCE_ACCOUNT);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);
            return false;
        }

        Fragment fragment = fragmentList.get(position);
        if (fragment != null) {
            this.currentFragment = fragment;
            selectFragment(fragment);
        }
        this.embracePageName = pageTitle;
        MainParentServerLogger.Companion.sendEmbraceBreadCrumb(embracePageName);
        return true;
    }

    @Override
    public void menuReselected(int position, int id) {
        Fragment fragment = fragmentList.get(getPositionFragmentByMenu(position));
        scrollToTop(fragment); // enable feature scroll to top for home & feed
    }

    @Override
    public String currentVisibleFragment() {
        return embracePageName;
    }

    public void populateBottomNavigationView() {
        menu.add(new BottomMenu(R.id.menu_home, getResources().getString(R.string.home), R.raw.bottom_nav_home, R.raw.bottom_nav_home_to_enabled, R.raw.bottom_nav_home_dark, R.raw.bottom_nav_home_to_enabled_dark, R.drawable.ic_bottom_nav_home_active, R.drawable.ic_bottom_nav_home_enabled, com.tokopedia.unifyprinciples.R.color.Unify_GN500, true, 1f, 1f));
        menu.add(new BottomMenu(R.id.menu_feed, getResources().getString(R.string.feed), R.raw.bottom_nav_feed, R.raw.bottom_nav_feed_to_enabled, R.raw.bottom_nav_feed_dark, R.raw.bottom_nav_feed_to_enabled_dark, R.drawable.ic_bottom_nav_feed_active, R.drawable.ic_bottom_nav_feed_enabled, com.tokopedia.unifyprinciples.R.color.Unify_GN500, true, 1f, 1f));
        menu.add(new BottomMenu(R.id.menu_os, getResources().getString(R.string.official), R.raw.bottom_nav_official, R.raw.bottom_nav_os_to_enabled, R.raw.bottom_nav_official_dark, R.raw.bottom_nav_os_to_enabled_dark, R.drawable.ic_bottom_nav_os_active, R.drawable.ic_bottom_nav_os_enabled, com.tokopedia.unifyprinciples.R.color.Unify_GN500, true, 1f, 1f));
        menu.add(new BottomMenu(R.id.menu_wishlist, getResources().getString(R.string.wishlist), R.raw.bottom_nav_wishlist, R.raw.bottom_nav_wishlist_to_enabled, R.raw.bottom_nav_wishlist_dark, R.raw.bottom_nav_wishlist_to_enabled_dark, R.drawable.ic_bottom_nav_wishlist_active, R.drawable.ic_bottom_nav_wishlist_enabled, com.tokopedia.unifyprinciples.R.color.Unify_GN500, true, 1f, 1f));
        menu.add(new BottomMenu(R.id.menu_uoh, getResources().getString(R.string.uoh), R.raw.bottom_nav_transaction, R.raw.bottom_nav_transaction_to_enabled, R.raw.bottom_nav_transaction_dark, R.raw.bottom_nav_transaction_to_enabled_dark, R.drawable.ic_bottom_nav_uoh_active, R.drawable.ic_bottom_nav_uoh_enabled, com.tokopedia.unifyprinciples.R.color.Unify_GN500, true, 1f, 1f));
        bottomNavigation.setMenu(menu);
        handleAppLinkBottomNavigation();
    }

    private void gotoNewUserZonePage() {
        Intent intentNewUser = RouteManager.getIntent(this, ApplinkConst.DISCOVERY_NEW_USER);
        Intent intentHome = RouteManager.getIntent(this, ApplinkConst.HOME);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivities(new Intent[]{intentHome, intentNewUser});
        finish();
    }

    @NonNull
    @Override
    public String getTelemetrySectionName() {
        return "home";
    }

    @Override
    public void onPositiveButtonInAppClicked(DetailUpdate detailUpdate) {
        globalNavAnalytics.get().eventClickAppUpdate(detailUpdate.isForceUpdate());
    }

    @Override
    public void onNegativeButtonInAppClicked(DetailUpdate detailUpdate) {
        globalNavAnalytics.get().eventClickCancelAppUpdate(detailUpdate.isForceUpdate());
    }

    @Override
    public void onNotNeedUpdateInApp() {
        if (!isFinishing()) {
            checkIsNeedUpdateIfComeFromUnsupportedApplink(MainParentActivity.this.getIntent());
        }
    }

    @Override
    public void onNeedUpdateInApp(DetailUpdate detailUpdate) {
        globalNavAnalytics.get().eventImpressionAppUpdate(detailUpdate.isForceUpdate());
    }

    @Override
    public void onHomeCoachMarkFinished() {
        View feedIconView = bottomNavigation.findViewById(R.id.menu_feed);
        new FeedCoachMark(this).show(feedIconView);
    }
}
