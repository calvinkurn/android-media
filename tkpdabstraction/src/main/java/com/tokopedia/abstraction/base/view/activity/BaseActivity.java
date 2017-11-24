package com.tokopedia.abstraction.base.view.activity;


import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.abstraction.constant.TkpdState;
import com.tokopedia.abstraction.utils.DialogForceLogout;
import com.tokopedia.abstraction.utils.GlobalConfig;
import com.tokopedia.abstraction.utils.LocalCacheHandler;
import com.tokopedia.abstraction.utils.receiver.ErrorNetworkReceiver;
import com.tokopedia.abstraction.utils.snackbar.SnackbarManager;

/**
 * Created by nisie on 2/7/17.
 */

public class BaseActivity extends AppCompatActivity implements
        ErrorNetworkReceiver.ReceiveListener {

    public static final String FORCE_LOGOUT = "com.tokopedia.tkpd.FORCE_LOGOUT";
    public static final String SERVER_ERROR = "com.tokopedia.tkpd.SERVER_ERROR";
    public static final String TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";

    private static final long DISMISS_TIME = 10000;

//    protected SessionHandler sessionHandler;

    private ErrorNetworkReceiver logoutNetworkReceiver;
    private LocalCacheHandler cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (MaintenancePage.isMaintenance(this)) {
//            startActivity(MaintenancePage.createIntent(this));
//        }

        logoutNetworkReceiver = new ErrorNetworkReceiver();
//        Localytics.registerPush(Constants.FIREBASE_PROJECT_ID);

//        HockeyAppHelper.handleLogin(this);
//        HockeyAppHelper.checkForUpdate(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterForceLogoutReceiver();
//        HockeyAppHelper.unregisterManager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        cache = new LocalCacheHandler(this, TkpdCache.STATUS_UPDATE);
        if (cache.getInt(TkpdCache.Key.STATUS) == TkpdState.UpdateState.MUST_UPDATE) {
            if (getApplication() instanceof AbstractionRouter) {
                ((AbstractionRouter) getApplication()).goToForceUpdate(this);
            }
//            Intent intent = new Intent(this, ForceUpdate.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(new Intent(this, ForceUpdate.class));
            finish();
        }

        initGTM();
        sendScreenAnalytics();

        registerForceLogoutReceiver();
        checkIfForceLogoutMustShow();
    }

    private void sendScreenAnalytics() {
//        ScreenTracking.sendScreen(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        HockeyAppHelper.unregisterManager();
//        sessionHandler = null;
        cache = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        Localytics.onNewIntent(this, intent);
    }

    public void initGTM() {
//        Observable.just(true)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(new Func1<Boolean, Boolean>() {
//                    @Override
//                    public Boolean call(Boolean b) {
//                        TrackingUtils.eventPushUserID();
//                        TrackingUtils.eventOnline();
//                        return true;
//                    }
//                })
//                .subscribe();
    }

    private void registerForceLogoutReceiver() {
        logoutNetworkReceiver.setReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(FORCE_LOGOUT);
        filter.addAction(SERVER_ERROR);
        filter.addAction(TIMEZONE_ERROR);
        LocalBroadcastManager.getInstance(this).registerReceiver(logoutNetworkReceiver, filter);
    }

    private void unregisterForceLogoutReceiver() {
        logoutNetworkReceiver.setReceiver(null);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(logoutNetworkReceiver);
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

    public void showForceLogoutDialog() {
        DialogForceLogout.createShow(this,
                new DialogForceLogout.ActionListener() {
                    @Override
                    public void onDialogClicked() {
                        if (getApplication() instanceof AbstractionRouter) {
                            ((AbstractionRouter) getApplication()).onForceLogout(BaseActivity.this);
                        }
//                        LoginManager.getInstance().logOut();
//                        setIsDialogShown(context, false);
//                        NotificationModHandler.clearCacheAllNotification(context);
//                        sessionHandler.forceLogout();
//                        if (GlobalConfig.isSellerApp()) {
//                            Intent intent = SellerRouter.getAcitivitySplashScreenActivity(getBaseContext());
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        } else {
//                            Intent intent = CustomerRouter.getSplashScreenIntent(getBaseContext());
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
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

//    @Override
//    public String getScreenName() {
//        return null;
//    }

}
