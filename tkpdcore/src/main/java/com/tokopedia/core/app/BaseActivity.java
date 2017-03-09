package com.tokopedia.core.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.localytics.android.Localytics;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.ForceUpdate;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.R;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.core.category.data.utils.CategoryVersioningHelper;
import com.tokopedia.core.category.data.utils.CategoryVersioningHelperListener;
import com.tokopedia.core.database.manager.CategoryDatabaseManager;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.DialogForceLogout;
import com.tokopedia.core.network.retrofit.utils.DialogNoConnection;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.ErrorNetworkReceiver;
import com.tokopedia.core.service.HadesBroadcastReceiver;
import com.tokopedia.core.service.HadesService;
import com.tokopedia.core.service.constant.HadesConstant;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.core.util.PhoneVerificationUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.welcome.WelcomeActivity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by nisie on 2/7/17.
 */

public class BaseActivity extends AppCompatActivity implements SessionHandler.onLogoutListener,
        HadesBroadcastReceiver.ReceiveListener,
        ErrorNetworkReceiver.ReceiveListener, ScreenTracking.IOpenScreenAnalytics {

    private static final String FORCE_LOGOUT = "com.tokopedia.tkpd.FORCE_LOGOUT";
    private static final long DISMISS_TIME = 10000;
    private static final String HADES = "TAG HADES";
    protected Boolean isAllowFetchDepartmentView = false;
    private Boolean isPause = false;
    private boolean isDialogNotConnectionShown = false;
    private HadesBroadcastReceiver hadesBroadcastReceiver;
    private ErrorNetworkReceiver logoutNetworkReceiver;
    private SessionHandler sessionHandler;
    private CategoryDatabaseManager categoryDatabaseManager;
    private GCMHandler gcmHandler;
    private GlobalCacheManager globalCacheManager;
    private LocalCacheHandler cache;
    private PhoneVerificationUtil phoneVerificationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MaintenancePage.isMaintenance(this)) {
            startActivity(MaintenancePage.createIntent(this));
        }
        sessionHandler = new SessionHandler(getBaseContext());
        categoryDatabaseManager = new CategoryDatabaseManager();
        gcmHandler = new GCMHandler(this);
        hadesBroadcastReceiver = new HadesBroadcastReceiver();
        phoneVerificationUtil = new PhoneVerificationUtil(this);
        logoutNetworkReceiver = new ErrorNetworkReceiver();
        globalCacheManager = new GlobalCacheManager();
        Localytics.registerPush(gcmHandler.getSenderID());


        /* clear cache if not login */
        if (!SessionHandler.isV4Login(this)) {
            globalCacheManager.deleteAll();
        }

        HockeyAppHelper.handleLogin(this);
        HockeyAppHelper.checkForUpdate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        MainApplication.setActivityState(0);
        MainApplication.setActivityname(null);

        if (!GlobalConfig.isSellerApp()) {
            if (phoneVerificationUtil != null) {
                phoneVerificationUtil.setHasShown(false);
                phoneVerificationUtil.dismissDialog();

            }
        }
        HockeyAppHelper.unregisterManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainApplication.setActivityState(TkpdState.Application.ACTIVITY);
        MainApplication.setActivityname(this.getClass().getSimpleName());
        cache = new LocalCacheHandler(this, TkpdCache.STATUS_UPDATE);
        if (cache.getInt(TkpdCache.Key.STATUS) == TkpdState.UpdateState.MUST_UPDATE) {
            Intent intent = new Intent(this, ForceUpdate.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(new Intent(this, ForceUpdate.class));
            finish();
        }

        initGTM();
        sendScreenAnalytics();
        verifyFetchDepartment();
        if (!GlobalConfig.isSellerApp()) {
            if (phoneVerificationUtil != null) {
                if (!phoneVerificationUtil.hasShown())
                    phoneVerificationUtil.checkIsMSISDNVerified();

            }
        }

        registerForceLogoutReceiver();
        checkIfForceLogoutMustShow();
    }

    private void sendScreenAnalytics() {
        ScreenTracking.sendScreen(this, this);
    }

    public boolean verifyFetchDepartment() {
        CategoryVersioningHelper.checkVersionCategory(this, new CategoryVersioningHelperListener() {
            @Override
            public void doAfterChecking() {
                if (categoryDatabaseManager.isExpired(System.currentTimeMillis())) {
                    if (!HadesService.getIsHadesRunning()) {
                        fetchDepartment();
                    } else {
                        registerHadesReceiver();
                    }
                }
            }
        });
        return false;
    }

    /**
     * download department using intentservice, so that it will not affected UI
     * {@link DownloadService#startDownload(Context, DownloadResultReceiver, Bundle, int)}
     */
    private void fetchDepartment() {
        if (!HadesService.getIsHadesRunning()) {
            Log.i(HADES, "START DOWNLOAAAD");
            HadesService.startDownload(this, HadesConstant.STATE_DEPARTMENT);
        }

        registerHadesReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterHadesReceiver();
        if (!GlobalConfig.isSellerApp()
                && phoneVerificationUtil != null) {
            phoneVerificationUtil.unSubscribe();
        }
        unregisterForceLogoutReceiver();
        HockeyAppHelper.unregisterManager();

        sessionHandler = null;
        phoneVerificationUtil = null;
        categoryDatabaseManager = null;
        gcmHandler = null;
        globalCacheManager = null;
        cache = null;
    }

    public Boolean isPausing() {
        return isPause;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Localytics.onNewIntent(this, intent);
    }

    public void initGTM() {
        Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean b) {
                        TrackingUtils.eventPushUserID();
                        TrackingUtils.eventOnline();
                        return true;
                    }
                })
                .subscribe();
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

    @Override
    public void onHadesRunning() {
        Log.i(HADES, "LAGI JALAN NEEEEH");
    }

    @Override
    public void onHadesComplete() {
        Log.i(HADES, "UDAH KELAR NEEEEH");
    }

    @Override
    public void onHadesNoConenction() {
        Log.i(HADES, this.getClass().getSimpleName() + " " + getIsAllowFetchDepartmentView());
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
        Log.i(HADES, this.getClass().getSimpleName() + " " + getIsAllowFetchDepartmentView());
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
        hadesBroadcastReceiver.setReceiver(this);
        IntentFilter mStatusIntentFilter = new IntentFilter(HadesService.ACTION_FETCH_DEPARTMENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(hadesBroadcastReceiver, mStatusIntentFilter);
    }

    private void unregisterHadesReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(hadesBroadcastReceiver);
    }

    private void registerForceLogoutReceiver() {
        logoutNetworkReceiver.setReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(FORCE_LOGOUT);
        LocalBroadcastManager.getInstance(this).registerReceiver(logoutNetworkReceiver, filter);
    }

    private void unregisterForceLogoutReceiver() {
        logoutNetworkReceiver.setReceiver(null);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(logoutNetworkReceiver);
    }

    public Boolean getIsAllowFetchDepartmentView() {
        return isAllowFetchDepartmentView;
    }


    @Override
    public void onForceLogout() {
        if (!DialogForceLogout.isDialogShown(this)) showForceLogoutDialog();
    }

    @Override
    public void onServerError() {
        final Snackbar snackBar = SnackbarManager.make(this,
                getString(R.string.msg_server_error_2), Snackbar.LENGTH_INDEFINITE)
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
        }, DISMISS_TIME);
    }

    public void showForceLogoutDialog() {
        DialogForceLogout.createShow(this,
                new DialogForceLogout.ActionListener() {
                    @Override
                    public void onDialogClicked() {
                        sessionHandler.forceLogout();
                        if (GlobalConfig.isSellerApp()) {
                            Intent intent = SellerRouter.getAcitivitySplashScreenActivity(getBaseContext());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
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
        intent.setData(Uri.parse(getString(R.string.mail_to_feedback)));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.server_error_problem));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.application_version_text) + GlobalConfig.VERSION_CODE);
        startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
    }

    @Override
    public String getScreenName() {
        return null;
    }

    public PhoneVerificationUtil getPhoneVerificationUtil() {
        return phoneVerificationUtil;
    }

    protected AppComponent getApplicationComponent() {
        return ((MainApplication) getApplication())
                .getApplicationComponent(getActivityModule());
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
