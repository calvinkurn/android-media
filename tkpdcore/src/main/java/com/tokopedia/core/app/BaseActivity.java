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

import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.ForceUpdate;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.DialogForceLogout;
import com.tokopedia.core.network.retrofit.utils.DialogHockeyApp;
import com.tokopedia.core.router.CustomerRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.service.ErrorNetworkReceiver;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
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

@Deprecated
public class BaseActivity extends AppCompatActivity implements SessionHandler.onLogoutListener,
        ErrorNetworkReceiver.ReceiveListener, ScreenTracking.IOpenScreenAnalytics {

    public static final String FORCE_LOGOUT = "com.tokopedia.tkpd.FORCE_LOGOUT";
    public static final String SERVER_ERROR = "com.tokopedia.tkpd.SERVER_ERROR";
    public static final String TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";
    public static final String FORCE_HOCKEYAPP = "com.tokopedia.tkpd.FORCE_HOCKEYAPP";
    private static final String TAG = "BaseActivity";
    private static final long DISMISS_TIME = 10000;
    protected Boolean isAllowFetchDepartmentView = false;

    protected SessionHandler sessionHandler;

    protected GCMHandler gcmHandler;

    private Boolean isPause = false;
    private boolean isDialogNotConnectionShown = false;
    private ErrorNetworkReceiver logoutNetworkReceiver;
    protected GlobalCacheManager globalCacheManager;
    private LocalCacheHandler cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);


        if (MaintenancePage.isMaintenance(this)) {
            startActivity(MaintenancePage.createIntent(this));
        }
        sessionHandler = new SessionHandler(getBaseContext());
        gcmHandler = new GCMHandler(this);
        logoutNetworkReceiver = new ErrorNetworkReceiver();
        globalCacheManager = new GlobalCacheManager();

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
        unregisterForceLogoutReceiver();
        MainApplication.setActivityState(0);
        MainApplication.setActivityname(null);
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


        registerForceLogoutReceiver();
        checkIfForceLogoutMustShow();
    }

    private void sendScreenAnalytics() {
        ScreenTracking.sendScreen(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        HockeyAppHelper.unregisterManager();

        sessionHandler = null;
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
                invalidateCategoryCache();
                intent = HomeRouter.getHomeActivity(this);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            AppWidgetUtil.sendBroadcastToAppWidget(this);
        }
    }

    private void registerForceLogoutReceiver() {
        logoutNetworkReceiver.setReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(FORCE_LOGOUT);
        filter.addAction(SERVER_ERROR);
        filter.addAction(TIMEZONE_ERROR);
        if (!GlobalConfig.isAllowDebuggingTools()) filter.addAction(FORCE_HOCKEYAPP);
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

    @SuppressWarnings("Range")
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

    @SuppressWarnings("Range")
    @Override
    public void onTimezoneError() {

        final Snackbar snackBar = SnackbarManager.make(this, getString(R.string.check_timezone),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_check, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                    }
                });
        snackBar.show();
    }

    private void showForceLogoutDialog() {
        DialogForceLogout.createShow(this,
                new DialogForceLogout.ActionListener() {
                    @Override
                    public void onDialogClicked() {
                        sessionHandler.forceLogout();
                        if (GlobalConfig.isSellerApp()) {
                            Intent intent = SellerRouter.getActivitySplashScreenActivity(getBaseContext());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            invalidateCategoryCache();
                            Intent intent = CustomerRouter.getSplashScreenIntent(getBaseContext());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void invalidateCategoryCache() {
        ((TkpdCoreRouter) getApplication()).invalidateCategoryMenuData();
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

    public AppComponent getApplicationComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }

    protected void setGoldMerchant(ShopModel shopModel) {
        sessionHandler.setGoldMerchant(shopModel.info.shopIsGold);
    }
}
