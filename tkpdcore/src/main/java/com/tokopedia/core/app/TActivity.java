package com.tokopedia.core.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.localytics.android.Localytics;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.ForceUpdate;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.R;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.core.database.manager.CategoryDatabaseManager;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.retrofit.utils.DialogForceLogout;
import com.tokopedia.core.network.retrofit.utils.DialogNoConnection;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.ErrorNetworkReceiver;
import com.tokopedia.core.service.HUDIntent;
import com.tokopedia.core.service.HadesBroadcastReceiver;
import com.tokopedia.core.service.HadesService;
import com.tokopedia.core.service.constant.HadesConstant;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.core.util.PhoneVerificationUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.var.ToolbarVariable;
import com.tokopedia.core.welcome.WelcomeActivity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Nisie on 31/08/15.
 */
public abstract class TActivity extends AppCompatActivity implements SessionHandler.onLogoutListener,
        HadesBroadcastReceiver.ReceiveListener,
        ErrorNetworkReceiver.ReceiveListener, ScreenTracking.IOpenScreenAnalytics {

    public static final int DEFAULT_NOT_FETCH_DEPARTMENT = -1;
    private Boolean isPause = false;
    private HUDIntent hudIntent;
    private ServiceConnection hudConnection;
    private boolean isBind = false;
    private boolean isDialogNotConnectionShown = false;
    protected FrameLayout parentView;
    protected ToolbarVariable toolbar;
    private DrawerLayout drawerLayout;
    protected Boolean isAllowFetchDepartmentView = false;
    HadesBroadcastReceiver mReceiverHades;
    ErrorNetworkReceiver mReceiverLogout;
    FragmentManager supportFragmentManager;
    protected DrawerVariable drawer;

    private Activity currentActivity;

    //[START] This is for downloading departmend id using IntentService
    private TkpdProgressDialog mProgressDialog;
    int std = DEFAULT_NOT_FETCH_DEPARTMENT;


    //[START] This is for downloading departmend id using IntentService

    public PhoneVerificationUtil phoneVerificationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Tokopedia3);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
        }

        if (MaintenancePage.isMaintenance(this)) {
            startActivity(MaintenancePage.createIntent(this));
        }
        if (!isBind && HUDIntent.isRunning)
            bindLogService();

        setContentView(R.layout.drawer_activity);
        parentView = (FrameLayout) findViewById(R.id.parent_view);
        toolbar = new ToolbarVariable(this);
        toolbar.createToolbarWithoutDrawer();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_nav);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Localytics.registerPush(new GCMHandler(this).getSenderID());

        /*  Support Fragment */
        supportFragmentManager = getFragmentManager();

        mReceiverHades = new HadesBroadcastReceiver();

        phoneVerificationUtil = new PhoneVerificationUtil(this);

        mReceiverLogout = new ErrorNetworkReceiver();

        /* clear cache if not login */
        if (!SessionHandler.isV4Login(this)) {
//            CacheHomeInteractorImpl.deleteAllCache();
            new GlobalCacheManager().deleteAll();
        }

        if (GlobalConfig.isSellerApp()) {
            drawer = ((TkpdCoreRouter)getApplication()).getDrawer(this);
        } else {
            drawer = new DrawerVariable(this);
        }

        drawer.setToolbar(toolbar);
        drawer.createDrawer();
        drawer.setEnabled(false);

        HockeyAppHelper.handleLogin(this);
        HockeyAppHelper.checkForUpdate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isPause = false;
        MainApplication.setActivityState(TkpdState.Application.ACTIVITY);
        MainApplication.setActivityname(this.getClass().getSimpleName());
        if (!MainApplication.isTablet()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        MainApplication.setActivityState(0);
        MainApplication.setActivityname(null);

        if (!GlobalConfig.isSellerApp()) {
            if (phoneVerificationUtil != null) {
                phoneVerificationUtil.setHasShown(false);
                phoneVerificationUtil.dismissDialog();

            }
        }
        drawer.setHasUpdated(false);
        HockeyAppHelper.unregisterManager();
    }

    //TODO Lanjutkan tes email kalo server devel udah waras lagi
    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
        MainApplication.setActivityState(TkpdState.Application.ACTIVITY);
        MainApplication.setActivityname(this.getClass().getSimpleName());
        LocalCacheHandler cache = new LocalCacheHandler(this, TkpdCache.STATUS_UPDATE);
        if (cache.getInt(TkpdCache.Key.STATUS) == TkpdState.UpdateState.MUST_UPDATE) {
            Intent intent = new Intent(this, ForceUpdate.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(new Intent(this, ForceUpdate.class));
            finish();
        }

        if (!isBind && HUDIntent.isRunning)
            bindLogService();

        initGTM();
        sendScreenAnalytics();
        verifyFetchDepartment();
        if (!GlobalConfig.isSellerApp()) {
            if (phoneVerificationUtil != null) {
                if (!phoneVerificationUtil.hasShown())
                    phoneVerificationUtil.checkIsMSISDNVerified();

            }
        }
        if (!drawer.hasUpdated()) {
            drawer.updateData();
        }


        registerForceLogoutReceiver();
        checkIfForceLogoutMustShow();
    }


    ///////////////////////

    private void bindLogService() {
//        HUDIntent.bindService(this, onBindServiceListener());
        hudIntent = MainApplication.hudIntent;
        isBind = true;
    }

    private void sendScreenAnalytics() {
        ScreenTracking.sendScreen(this, this);
    }

    private HUDIntent.HUDInterface onBindServiceListener() {
        return new HUDIntent.HUDInterface() {
            @Override
            public void onServiceConnected(HUDIntent service, ServiceConnection connection) {
                hudIntent = service;
                hudConnection = connection;
                hudIntent.printMessage("CONNECTED");
            }

            @Override
            public void onServiceDisconnected() {
                hudIntent = null;
                hudConnection = null;
            }
        };
    }

    private void unbindLogService() {
        HUDIntent.unbindService(this, hudConnection);
    }

    ///////////////////////

    /**
     * verify and if the {@link TActivity#std} is certain values that already registered,
     * then fetch the department and also if not expired.
     */
    public Boolean verifyFetchDepartment() {
        if (new CategoryDatabaseManager().isExpired(System.currentTimeMillis())) {
            if (!HadesService.getIsHadesRunning()) {
                fetchDepartment();
            } else {
                registerHadesReceiver();
            }
            return true;
        }
        return false;
    }

    /**
     * download department using intentservice, so that it will not affected UI
     * {@link DownloadService#startDownload(Context, DownloadResultReceiver, Bundle, int)}
     */
    private void fetchDepartment() {
        if (!HadesService.getIsHadesRunning()) {
            Log.i("HADES TAG", "START DOWNLOAAAD");
            HadesService.startDownload(this, HadesConstant.STATE_DEPARTMENT);
        }

        registerHadesReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPause = true;
        unbindLogService();
        unregisterHadesReceiver();
        if (!GlobalConfig.isSellerApp()) {
            if (phoneVerificationUtil != null)
                phoneVerificationUtil.unSubscribe();
        }
        unregisterForceLogoutReceiver();

        HockeyAppHelper.unregisterManager();
    }

    public Boolean isPausing() {
        return isPause;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return onHomeOptionSelected();
        } else if (item.getItemId() == R.id.action_settings) {
            return onSettingsOptionSelected();
        } else if (item.getItemId() == R.id.action_search) {
            return onSearchOptionSelected();
        } else if (item.getItemId() == R.id.action_cart) {
            return onCartOptionSelected();
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean onSearchOptionSelected() {
        Intent intent = BrowseProductRouter
                .getBrowseProductIntent(this, "0", TopAdsApi.SRC_BROWSE_PRODUCT);

        startActivity(intent);
        return true;
    }

    public static boolean onCartOptionSelected(Context context) {
        if (!SessionHandler.isV4Login(context)) {
            Intent intent = SessionRouter.getLoginActivityIntent(context);
            intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
            context.startActivity(intent);
        } else {
            context.startActivity(TransactionCartRouter.createInstanceCartActivity(context));
        }
        return true;
    }

    private Boolean onCartOptionSelected() {

        if (!SessionHandler.isV4Login(getBaseContext())) {
            Intent intent = SessionRouter.getLoginActivityIntent(getBaseContext());
            intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
            startActivity(intent);
        } else {
            startActivity(TransactionCartRouter.createInstanceCartActivity(this));
        }
        return true;
    }

    private boolean onLogoutOptionSelected() {
        SessionHandler session = new SessionHandler(TActivity.this);
        Context context = TActivity.this;
        session.Logout(context);
        return true;
    }

    private boolean onAboutUsOptionSelected() {
        VersionInfo.createVersionDialog(this);
        return true;
    }

    private boolean onSettingsOptionSelected() {
        startActivity(new Intent(getBaseContext(), ManageGeneral.class));
        return true;
    }

    private boolean onHomeOptionSelected() {
        KeyboardHandler.DropKeyboard(this, parentView);
        onBackPressed();
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Localytics.onNewIntent(this, intent);
    }

    public void inflateView(int layoutId) {
        getLayoutInflater().inflate(layoutId, parentView);
    }

    public void initGTM() {
        Observable.just(this)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Context, Boolean>() {
                    @Override
                    public Boolean call(Context context) {
                        TrackingUtils.eventPushUserID();
                        TrackingUtils.eventOnline();
                        return true;
                    }
                })
                .subscribe();
    }

    public void hideToolbar() {
        getSupportActionBar().hide();
    }

    @Override
    public void onLogout(Boolean success) {
        if (success) {
            finish();
            Intent intent;
            if (GlobalConfig.isSellerApp()) {
                intent = new Intent(this, WelcomeActivity.class);
            } else {
                intent = HomeRouter.getHomeActivity(this);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private boolean isSellerApp() {
        return getApplication().getClass().getSimpleName().equals("SellerMainApplication");
    }


    protected void onFinishFetechedDepartment() {

    }

    protected void showProgressService() {
        if (isFinishing())
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed())
            return;

        if (mProgressDialog != null && mProgressDialog.isProgress()) return;

        mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        mProgressDialog.setCancelable(false);
        mProgressDialog.showDialog();
    }

    protected void dissmisProgressService() {
        if (mProgressDialog != null && mProgressDialog.isProgress()) {
            mProgressDialog.dismiss();
        }
    }

    private void showSnackbar(String data) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), data.toString(), Snackbar.LENGTH_INDEFINITE);
        snack.setAction(getString(R.string.title_try_again), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDepartment();
            }
        });
        snack.show();
    }


    @Override
    public void onHadesRunning() {
        Log.i("HADES TAG", "LAGI JALAN NEEEEH");
    }

    @Override
    public void onHadesComplete() {
        Log.i("HADES TAG", "UDAH KELAR NEEEEH");
        onFinishFetechedDepartment();
    }

    @Override
    public void onHadesNoConenction() {
        Log.i("TAG HADES", this.getClass().getSimpleName() + " " + getIsAllowFetchDepartmentView());
        if (getIsAllowFetchDepartmentView() && !isDialogNotConnectionShown) {
            DialogNoConnection.create(this, new DialogNoConnection.ActionListener() {
                @Override
                public void onRetryClicked() {
                    fetchDepartment();
                    isDialogNotConnectionShown = false;
                }
            }).show();
            isDialogNotConnectionShown = true;
        }
    }

    @Override
    public void onHadesTimeout() {
        Log.i("TAG HADES", this.getClass().getSimpleName() + " " + getIsAllowFetchDepartmentView());
        if (getIsAllowFetchDepartmentView() && !isDialogNotConnectionShown) {
            DialogNoConnection.create(this, new DialogNoConnection.ActionListener() {
                @Override
                public void onRetryClicked() {
                    fetchDepartment();
                    isDialogNotConnectionShown = false;
                }
            }).show();
            isDialogNotConnectionShown = true;
        }
    }

    private void registerHadesReceiver() {
        mReceiverHades.setReceiver(this);
        IntentFilter mStatusIntentFilter = new IntentFilter(HadesService.ACTION_FETCH_DEPARTMENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverHades, mStatusIntentFilter);
    }

    private void unregisterHadesReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiverHades);
    }

    private void registerForceLogoutReceiver() {
        mReceiverLogout.setReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.tokopedia.tkpd.FORCE_LOGOUT");
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverLogout, filter);
    }

    private void unregisterForceLogoutReceiver() {
        mReceiverLogout.setReceiver(null);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiverLogout);
    }

    public Boolean getIsAllowFetchDepartmentView() {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onForceLogout() {
        if (!DialogForceLogout.isDialogShown(this)) showForceLogoutDialog();
    }

    @Override
    public void onServerError() {
        final Snackbar snackBar = SnackbarManager.make(this, getString(R.string.msg_server_error_2), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_report, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEmailComplain();
                    }
                });
        snackBar.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                snackBar.dismiss();
            }
        }, 10000);
    }

    public void showForceLogoutDialog() {
        DialogForceLogout.createShow(this,
                new DialogForceLogout.ActionListener() {
                    @Override
                    public void onDialogClicked() {
                        SessionHandler sessionHandler = new SessionHandler(getBaseContext());
                        sessionHandler.forceLogout();
                        if(GlobalConfig.isSellerApp()){
                            Intent intent = SellerRouter.getAcitivitySplashScreenActivity(getBaseContext());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(getBaseContext(), SplashScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
    }

    public void checkIfForceLogoutMustShow() {
        if (DialogForceLogout.isDialogShown(this)) showForceLogoutDialog();
    }

    public void sendEmailComplain() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + "android.feedback@tokopedia.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Masalah Server Error");
        intent.putExtra(Intent.EXTRA_TEXT, "Versi Aplikasi: " + GlobalConfig.VERSION_CODE);
        startActivity(Intent.createChooser(intent, "Kirim Email"));
    }

    protected AppComponent getApplicationComponent() {
        return ((MainApplication) getApplication())
                .getApplicationComponent(getActivityModule());
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
