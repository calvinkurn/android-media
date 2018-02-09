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
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.utils.view.DialogForceLogout;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.HockeyAppHelper;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.receiver.ErrorNetworkReceiver;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;

/**
 * Created by nisie on 2/7/17.
 */

abstract class BaseActivity extends AppCompatActivity implements
        ErrorNetworkReceiver.ReceiveListener {

    public static final String FORCE_LOGOUT = "com.tokopedia.tkpd.FORCE_LOGOUT";
    public static final String SERVER_ERROR = "com.tokopedia.tkpd.SERVER_ERROR";
    public static final String TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";

    private static final long DISMISS_TIME = 10000;

    private ErrorNetworkReceiver logoutNetworkReceiver;
    private LocalCacheHandler cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logoutNetworkReceiver = new ErrorNetworkReceiver();
        HockeyAppHelper.handleLogin(this);
        HockeyAppHelper.checkForUpdate(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterForceLogoutReceiver();
        HockeyAppHelper.unregisterManager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        sendScreenAnalytics();

        registerForceLogoutReceiver();
        checkIfForceLogoutMustShow();
    }

    private void sendScreenAnalytics() {
        if (getApplication() instanceof AbstractionRouter) {
            AnalyticTracker analyticTracker = ((AbstractionRouter) getApplication()).getAnalyticTracker();
            analyticTracker.sendScreen(this, getScreenName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HockeyAppHelper.unregisterManager();
        cache = null;
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

    public String getScreenName() {
        return null;
    }
}