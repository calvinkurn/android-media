package com.tokopedia.navigation.presentation.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieTask;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.base.view.appupdate.AppUpdateDialogBuilder;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.design.component.BottomNavigation;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.home.account.presentation.fragment.AccountHomeFragment;
import com.tokopedia.inappupdate.AppUpdateManagerWrapper;
import com.tokopedia.navigation.GlobalNavAnalytics;
import com.tokopedia.navigation.GlobalNavConstant;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.model.Notification;
import com.tokopedia.navigation.presentation.di.DaggerGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavModule;
import com.tokopedia.navigation.presentation.presenter.MainParentPresenter;
import com.tokopedia.navigation.presentation.view.MainParentView;
import com.tokopedia.navigation_common.listener.AllNotificationListener;
import com.tokopedia.navigation_common.listener.CartNotifyListener;
import com.tokopedia.navigation_common.listener.FragmentListener;
import com.tokopedia.navigation_common.listener.RefreshNotificationListener;
import com.tokopedia.navigation_common.listener.ShowCaseListener;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP;

/**
 * Created by meta on 19/06/18.
 */
public class MainParentActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, HasComponent,
        MainParentView, ShowCaseListener, CartNotifyListener, RefreshNotificationListener {

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
    @Inject
    UserSessionInterface userSession;
    @Inject
    MainParentPresenter presenter;
    @Inject
    GlobalNavAnalytics globalNavAnalytics;
    @Inject
    ApplicationUpdate appUpdate;
    private BottomNavigation bottomNavigation;
    private ShowCaseDialog showCaseDialog;
    private List<Fragment> fragmentList;
    private Notification notification;
    private Fragment currentFragment;
    private boolean isUserFirstTimeLogin = false;
    private boolean doubleTapExit = false;
    private BroadcastReceiver newFeedClickedReceiver;
    private SharedPreferences cacheManager;
    private Handler handler = new Handler();
    private CoordinatorLayout fragmentContainer;
    private boolean isFirstNavigationImpression = false;

    // animate icon OS
    private MenuItem osMenu;
    private LottieDrawable lottieOsDrawable;
    private float OS_STATE_SELECTED = 1f;
    private float OS_STATE_UNSELECTED = 0f;
    private float OS_STATE_ANIMATED = 0.7f;

    @DeepLink({ApplinkConst.HOME, ApplinkConst.HOME_CATEGORY})
    public static Intent getApplinkIntent(Context context, Bundle bundle) {
        return start(context);
    }

    @DeepLink({ApplinkConst.HOME_FEED, ApplinkConst.FEED, ApplinkConst.CONTENT_EXPLORE})
    public static Intent getApplinkFeedIntent(Context context, Bundle bundle) {
        Intent intent = start(context);
        intent.putExtra(ARGS_TAB_POSITION, FEED_MENU);
        intent.putExtras(bundle);
        return intent;
    }

    @DeepLink({ApplinkConst.HOME_ACCOUNT})
    public static Intent getApplinkAccountIntent(Context context, Bundle bundle) {
        Intent intent = start(context);
        intent.putExtra(ARGS_TAB_POSITION, ACCOUNT_MENU);
        return intent;
    }

    @DeepLink({ApplinkConst.OFFICIAL_STORES, ApplinkConst.OFFICIAL_STORE})
    public static Intent getApplinkOfficialStoreIntent(Context context, Bundle bundle) {
        Intent intent = start(context);
        intent.putExtra(ARGS_TAB_POSITION, OS_MENU);
        return intent;
    }

    @DeepLink({ApplinkConst.HOME_RECOMMENDATION})
    public static Intent getApplinkRecommendationEvent(Context context) {
        Intent intent = start(context);
        intent.putExtra(ARGS_TAB_POSITION, RECOMENDATION_LIST);
        intent.putExtra(SCROLL_RECOMMEND_LIST, true);
        return intent;
    }

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
        super.onCreate(savedInstanceState);
        initInjector();
        presenter.setView(this);
        if (savedInstanceState != null) {
            presenter.setIsRecurringApplink(savedInstanceState.getBoolean(IS_RECURRING_APPLINK, false));
        }
        cacheManager = PreferenceManager.getDefaultSharedPreferences(this);
        createView(savedInstanceState);
        ((GlobalNavRouter) getApplicationContext()).sendOpenHomeEvent();

        initCategoryConfig();
    }

    private void initCategoryConfig() {
        ((GlobalNavRouter) getApplicationContext()).setCategoryAbTestingConfig();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter.isFirstTimeUser()) {
            setDefaultShakeEnable();
            routeOnboarding();
        }
    }

    /**
     * this is temporary fix for crash MediaPlayer,
     *  because we already fix it 5times, and still appear on specific device
     */
    private void routeOnboarding() {
        if (Build.MODEL.contains("vivo Y35")
            || Build.MODEL.contains("vivo Y51L")) {
            if (Build.VERSION.RELEASE.contains("5.0.2")) {
                return;
            }
        }

        Intent intent = RouteManager.getIntent(this,
                ApplinkConstInternalMarketplace.ONBOARDING);
        startActivity(intent);
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
        return userSession.isFirstTimeUser();
    }

    private void createView(Bundle savedInstanceState) {
        isFirstNavigationImpression = true;
        GraphqlClient.init(this);
        setContentView(R.layout.activity_main_parent);

        bottomNavigation = findViewById(R.id.bottomnav);
        fragmentContainer = findViewById(R.id.container);

        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        bottomNavigation.setOnNavigationItemReselectedListener(item -> {
            Fragment fragment = fragmentList.get(getPositionFragmentByMenu(item));
            scrollToTop(fragment); // enable feature scroll to top for home & feed
        });

        fragmentList = fragments();

        if (isFirstTime()) {
            globalNavAnalytics.trackFirstTime(this);
        }

        handleAppLinkBottomNavigation(savedInstanceState);
        checkAppUpdateAndInApp();
        checkApplinkCouponCode(getIntent());

        initNewFeedClickReceiver();
    }

    private void handleAppLinkBottomNavigation(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null) {
            int tabPosition = getIntent().getExtras().getInt(ARGS_TAB_POSITION, HOME_MENU);
            switch (tabPosition) {
                case FEED_MENU:
                    bottomNavigation.getMenu().findItem(R.id.menu_feed).setChecked(true);
                    onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_feed));
                    break;
                case ACCOUNT_MENU:
                    bottomNavigation.getMenu().findItem(R.id.menu_account).setChecked(true);
                    onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_account));
                    break;
                case OS_MENU:
                    bottomNavigation.getMenu().findItem(R.id.menu_os).setChecked(true);
                    onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_os));
                    break;
                case RECOMENDATION_LIST:
                case HOME_MENU:
                default:
                    bottomNavigation.getMenu().findItem(R.id.menu_home).setChecked(true);
                    onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_home));
                    break;
            }
        } else if (savedInstanceState == null) {
            onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_home));
            this.currentFragment = fragmentList.get(HOME_MENU);
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

        presenter.setView(this);
    }

    private int getPositionFragmentByMenu(MenuItem item) {
        int i = item.getItemId();
        int position = HOME_MENU;
        if (i == R.id.menu_feed) {
            position = FEED_MENU;
        } else if (i == R.id.menu_os) {
            position = OS_MENU;
        } else if (i == R.id.menu_cart) {
            position = CART_MENU;
        } else if (i == R.id.menu_account) {
            position = ACCOUNT_MENU;
        }
        return position;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int position = getPositionFragmentByMenu(item);
        if (!isFirstNavigationImpression) {
            globalNavAnalytics.eventBottomNavigation(item.getTitle().toString()); // push analytics
        }
        isFirstNavigationImpression = false;

        if (position == FEED_MENU) {
            Intent intent = new Intent(BROADCAST_FEED);
            LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
        }

        if ((position == CART_MENU || position == ACCOUNT_MENU) && !presenter.isUserLogin()) {
            String applink = String.format("%s?source=%s", ApplinkConst.LOGIN, "account");
            RouteManager.route(this, applink);
            return false;
        }

        if (position == OS_MENU) {
            setOsIconProgress(OS_STATE_SELECTED);
        } else {
            setOsIconProgress(OS_STATE_UNSELECTED);
        }

        hideStatusBar();

        Fragment fragment = fragmentList.get(position);
        if (fragment != null) {
            this.currentFragment = fragment;
            selectFragment(fragment);
        }
        return true;
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

    private void hideStatusBar() {
        //apply inset to allow recyclerview scrolling behind status bar
        fragmentContainer.setFitsSystemWindows(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            fragmentContainer.requestApplyInsets();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = fragmentContainer.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            fragmentContainer.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void selectFragment(Fragment fragment) {
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
                for (int i = 0; i < manager.getFragments().size(); i++) {
                    Fragment frag = manager.getFragments().get(i);
                    if (frag.getClass().getName().equalsIgnoreCase(fragment.getClass().getName())) {
                        ft.show(frag); // only show fragment what you want to show
                    } else {
                        ft.hide(frag); // hide all fragment
                    }
                }
            } else {
                ft.add(R.id.container, fragment, backStateName); // add fragment if there re not registered on fragmentManager
            }
            ft.commitAllowingStateLoss();
        });
    }

    private void scrollToTop(Fragment fragment) {
        if (fragment.getUserVisibleHint() && fragment instanceof FragmentListener) {
            ((FragmentListener) fragment).onScrollToTop();
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setUserSession(UserSessionInterface userSession) {
        this.userSession = userSession;
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
        presenter.onResume();
        if (userSession.isLoggedIn() && isUserFirstTimeLogin) {
            reloadPage();
        }
        isUserFirstTimeLogin = !userSession.isLoggedIn();

        addShortcuts();

        registerNewFeedClickedReceiver();

        if (!((BaseMainApplication) getApplication()).checkAppSignature()) {
            finish();
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
            presenter.onDestroy();
    }

    private void reloadPage() {
        finish();
        startActivity(getIntent());
    }

    private List<Fragment> fragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        if (MainParentActivity.this.getApplication() instanceof GlobalNavRouter) {
            fragmentList.add(((GlobalNavRouter) MainParentActivity.this.getApplication()).getHomeFragment(getIntent().getBooleanExtra(SCROLL_RECOMMEND_LIST, false)));
            fragmentList.add(((GlobalNavRouter) MainParentActivity.this.getApplication()).getFeedPlusFragment(getIntent().getExtras()));
            fragmentList.add(((GlobalNavRouter) MainParentActivity.this.getApplication()).getOfficialStoreFragment(getIntent().getExtras()));
            Fragment cartFragment = ((GlobalNavRouter) MainParentActivity.this.getApplication()).getCartFragment(null);
            fragmentList.add(cartFragment);
            fragmentList.add(AccountHomeFragment.newInstance());
        }
        return fragmentList;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public Fragment getFragment(int index) {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    @Override
    public void renderNotification(Notification notification) {
        this.notification = notification;
        bottomNavigation.setNotification(notification.getTotalCart(), CART_MENU);
        if (notification.getHaveNewFeed()) {
            bottomNavigation.setNotification(-1, FEED_MENU);
            Intent intent = new Intent(BROADCAST_FEED);
            intent.putExtra(PARAM_BROADCAST_NEW_FEED, notification.getHaveNewFeed());
            LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
        } else {
            bottomNavigation.setNotification(0, FEED_MENU);
        }
        if (currentFragment != null)
            setBadgeNotifCounter(currentFragment);
    }

    @Override
    public void onStartLoading() { }

    @Override
    public void onError(String message) { }

    @Override
    public void onHideLoading() { }

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
        if (fragment == null)
            return;

        if (fragment instanceof AllNotificationListener && notification != null) {
            ((AllNotificationListener) fragment).onNotificationChanged(notification.getTotalNotif(), notification.getTotalInbox());
        }

        invalidateOptionsMenu();
    }

    @Override
    public void onNotifyCart() {
        if (presenter != null)
            this.presenter.getNotificationData();
    }

    private void saveInstanceState(Bundle outState) {
        if (getIntent() != null) {
            outState.putBoolean(IS_RECURRING_APPLINK, presenter.isRecurringApplink());
        }
    }

    /**
     * Show Case on boarding
     */
    private ShowCaseDialog createShowCase() {
        return new ShowCaseBuilder()
                .backgroundContentColorRes(R.color.black)
                .shadowColorRes(R.color.shadow)
                .titleTextColorRes(R.color.white)
                .textColorRes(R.color.grey_400)
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

        playAnimOsIcon(); // show animation icon

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
                                            globalNavAnalytics.eventClickAppUpdate(detail.isForceUpdate());
                                        }

                                        @Override
                                        public void onNegativeButtonClicked(DetailUpdate detail) {
                                            globalNavAnalytics.eventClickCancelAppUpdate(detail.isForceUpdate());
                                        }
                                    }
                            );
                    appUpdateDialogBuilder.getAlertDialog().show();
                    globalNavAnalytics.eventImpressionAppUpdate(detail.isForceUpdate());
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
        if (!presenter.isRecurringApplink() && !TextUtils.isEmpty(intent.getStringExtra(ApplinkRouter.EXTRA_APPLINK))) {
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

            // Note: applink/deeplink router already in DeeplinkHandlerActivity.
            // Applink should not be passed to home because the analytics at home might be triggered.
            // It is better to use TaskStackBuilder to build taskstack for home, rather than passwing to home directly.
            // Below code is still maintained to ensure no deeplink/applink uri is lost
            try {
                Intent applinkIntent = new Intent(this, MainParentActivity.class);
                applinkIntent.setData(Uri.parse(applink));
                if (getIntent() != null && getIntent().getExtras() != null) {
                    Intent newIntent = getIntent();
                    newIntent.removeExtra(DeepLink.IS_DEEP_LINK);
                    newIntent.removeExtra(DeepLink.REFERRER_URI);
                    newIntent.removeExtra(DeepLink.URI);
                    newIntent.removeExtra(ApplinkRouter.EXTRA_APPLINK);
                    if (newIntent.getExtras() != null)
                        applinkIntent.putExtras(newIntent.getExtras());
                }
                ((ApplinkRouter) getApplicationContext()).applinkDelegate().dispatchFrom(this, applinkIntent);
            } catch (ActivityNotFoundException ex) {
                ex.printStackTrace();
            }

            presenter.setIsRecurringApplink(true);
        }
    }

    private void initNewFeedClickReceiver() {
        newFeedClickedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null && intent.getAction().equals(BROADCAST_FEED)) {
                    boolean isHaveNewFeed = intent.getBooleanExtra(PARAM_BROADCAST_NEW_FEED_CLICKED, false);
                    if (isHaveNewFeed) {
                        bottomNavigation.setNotification(0, FEED_MENU);
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
        return presenter;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setPresenter(MainParentPresenter presenter) {
        this.presenter = presenter;
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

                    Intent intentHome = MainParentActivity.start(this);
                    intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentHome.setAction(Intent.ACTION_VIEW);

                    Intent productIntent = RouteManager.getIntent(this, ApplinkConstInternalDiscovery.AUTOCOMPLETE);
                    productIntent.setAction(Intent.ACTION_VIEW);
                    productIntent.putExtras(args);

                    ShortcutInfo productShortcut = new ShortcutInfo.Builder(this, SHORTCUT_BELI_ID)
                            .setShortLabel(getResources().getString(R.string.navigation_home_label_longpress_beli))
                            .setLongLabel(getResources().getString(R.string.navigation_home_label_longpress_beli))
                            .setIcon(Icon.createWithResource(this, R.drawable.ic_search_shortcut))
                            .setIntents(new Intent[]{intentHome, productIntent})
                            .setRank(0)
                            .build();
                    shortcutInfos.add(productShortcut);

                    if (userSession.isLoggedIn()) {
                        Intent wishlistIntent = ((GlobalNavRouter) getApplication()).gotoWishlistPage(this);
                        wishlistIntent.setAction(Intent.ACTION_VIEW);
                        wishlistIntent.putExtras(args);

                        ShortcutInfo wishlistShortcut = new ShortcutInfo.Builder(this, SHORTCUT_SHARE_ID)
                                .setShortLabel(getResources().getString(R.string.navigation_home_label_longpress_share))
                                .setLongLabel(getResources().getString(R.string.navigation_home_label_longpress_share))
                                .setIcon(Icon.createWithResource(this, R.drawable.ic_wishlist_shortcut))
                                .setIntents(new Intent[]{intentHome, wishlistIntent})
                                .setRank(1)
                                .build();
                        shortcutInfos.add(wishlistShortcut);
                    }

                    Intent digitalIntent = ((GlobalNavRouter) getApplication()).instanceIntentDigitalCategoryList();
                    digitalIntent.setAction(Intent.ACTION_VIEW);
                    digitalIntent.putExtras(args);

                    ShortcutInfo digitalShortcut = new ShortcutInfo.Builder(this, SHORTCUT_DIGITAL_ID)
                            .setShortLabel(getResources().getString(R.string.navigation_home_label_longpress_bayar))
                            .setLongLabel(getResources().getString(R.string.navigation_home_label_longpress_bayar))
                            .setIcon(Icon.createWithResource(this, R.drawable.ic_pay_shortcut))
                            .setIntents(new Intent[]{intentHome, digitalIntent})
                            .setRank(2)
                            .build();
                    shortcutInfos.add(digitalShortcut);

                    if (userSession.isLoggedIn()) {
                        String shopID = userSession.getShopId();

                        Intent shopIntent;
                        if (!userSession.hasShop()) {
                            shopIntent = RouteManager.getIntent(getContext(), OPEN_SHOP);
                        } else {
                            shopIntent = ((GlobalNavRouter) getApplication()).getShopPageIntent(this, shopID);
                        }

                        shopIntent.setAction(Intent.ACTION_VIEW);
                        shopIntent.putExtras(args);

                        ShortcutInfo shopShortcut = new ShortcutInfo.Builder(this, SHORTCUT_SHOP_ID)
                                .setShortLabel(getResources().getString(R.string.navigation_home_label_longpress_jual))
                                .setLongLabel(getResources().getString(R.string.navigation_home_label_longpress_jual))
                                .setIcon(Icon.createWithResource(this, R.drawable.ic_sell_shortcut))
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
    }

    @Override
    public void onRefreshNotification() {
        presenter.getNotificationData();
    }


    /**
     *
     * Load animated icon by Lottie
     * duration anim: 2s
     * 1s = 60 frames
     * + 20 frames
     *
     * 0f - 0.7f state default - animation - default
     * 1 state selected
     */
    private void initOsMenu() {
        bottomNavigation.setIconMarginTop(OS_MENU, 0);
        bottomNavigation.setIconSizeAt(OS_MENU, 55, 55);

        Menu menu = bottomNavigation.getMenu();
        osMenu = menu.findItem(R.id.menu_os);

        lottieOsDrawable = new LottieDrawable();
        LottieTask<LottieComposition> task = LottieCompositionFactory.fromRawRes(this, R.raw.icon_os);
        task.addListener(result -> lottieOsDrawable.setComposition(result));

        osMenu.setIcon(lottieOsDrawable);
    }

    private void playAnimOsIcon() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (osMenu == null) {
            initOsMenu();
        }

        lottieOsDrawable.setMaxProgress(OS_STATE_ANIMATED);
        lottieOsDrawable.setRepeatCount(1);
        lottieOsDrawable.playAnimation();
    }

    private void setOsIconProgress(float progress) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (osMenu == null) {
            initOsMenu();
        }
        if (lottieOsDrawable.isAnimating()) {
            lottieOsDrawable.setMaxProgress(OS_STATE_ANIMATED);
        } else {
            lottieOsDrawable.setMaxProgress(OS_STATE_SELECTED); // important! to reset maxProgress
        }
        lottieOsDrawable.setProgress(progress);
    }
}