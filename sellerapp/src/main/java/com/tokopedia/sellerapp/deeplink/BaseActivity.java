package com.tokopedia.sellerapp.deeplink;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.snackbar.Snackbar;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.view.DialogForceLogout;
import com.tokopedia.analyticsdebugger.debugger.TetraDebugger;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.common.ui.MaintenancePage;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.SplashScreenActivity;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by nisie on 2/7/17.
 * refer {@link com.tokopedia.abstraction.base.view.activity.BaseActivity}
 */

/**
 * Extends one of BaseActivity from tkpd abstraction eg:BaseSimpleActivity, BaseStepperActivity, BaseTabActivity, etc
 */
@Deprecated
public class BaseActivity extends AppCompatActivity implements
        ErrorNetworkReceiver.ReceiveListener, ScreenTracking.IOpenScreenAnalytics {


    public static final String FORCE_LOGOUT = "com.tokopedia.tkpd.FORCE_LOGOUT";
    public static final String SERVER_ERROR = "com.tokopedia.tkpd.SERVER_ERROR";
    public static final String TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";
    private static final long DISMISS_TIME = 10000;
    protected Boolean isAllowFetchDepartmentView = false;

    protected UserSessionInterface userSessionInterface;

    private Boolean isPause = false;
    private ErrorNetworkReceiver logoutNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MaintenancePage.isMaintenance(this)) {
            startActivity(MaintenancePage.createIntent(this));
        }

        userSessionInterface = new UserSession(getBaseContext());
        logoutNetworkReceiver = new ErrorNetworkReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterForceLogoutReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        forceRotation();
    }

    protected void forceRotation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initGTM();
        sendScreenAnalytics();

        registerForceLogoutReceiver();
        checkIfForceLogoutMustShow();
    }

    protected void sendScreenAnalytics() {
        ScreenTracking.sendScreen(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!GlobalConfig.isSellerApp() && getApplication() instanceof AbstractionRouter) {
            String screenName = getScreenName();
            if (screenName == null) {
                screenName = this.getClass().getSimpleName();
            }
        }
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
                        TrackApp.getInstance().getGTM()
                                .pushUserId(userSessionInterface.getGTMLoginID());
                        TrackingUtils.eventOnline(BaseActivity.this,
                                userSessionInterface.getGTMLoginID());
                        return true;
                    }
                })
                .subscribe(ignored -> {}, throwable -> {});
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
        DialogForceLogout.createShow(this, getScreenName(),
                new DialogForceLogout.ActionListener() {
                    @Override
                    public void onDialogClicked() {
                        onLogout();
                        Intent intent = new Intent(BaseActivity.this, SplashScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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

    public AppComponent getApplicationComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }

    public BaseAppComponent getBaseAppComponent() {
        return ((MainApplication) getApplication()).getBaseAppComponent();
    }

    public void onLogout() {
        ((AbstractionRouter) getApplication()).onForceLogout(this);
        setTetraUserId("");
    }

    private void setTetraUserId(String userId) {
        if(GlobalConfig.isAllowDebuggingTools()) {
            TetraDebugger tetraDebugger = TetraDebugger.Companion.instance(this);
            tetraDebugger.setUserId(userId);
        }
    }
}
