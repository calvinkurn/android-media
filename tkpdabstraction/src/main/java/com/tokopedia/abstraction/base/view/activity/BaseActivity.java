package com.tokopedia.abstraction.base.view.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.splitcompat.SplitCompat;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.listener.DebugVolumeListener;
import com.tokopedia.abstraction.common.utils.receiver.ErrorNetworkReceiver;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.DialogForceLogout;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.inappupdate.AppUpdateManagerWrapper;
import com.tokopedia.track.TrackApp;

import java.util.ArrayList;


/**
 * Created by nisie on 2/7/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements
        ErrorNetworkReceiver.ReceiveListener {

    public static final String FORCE_LOGOUT = "com.tokopedia.tkpd.FORCE_LOGOUT";
    public static final String SERVER_ERROR = "com.tokopedia.tkpd.SERVER_ERROR";
    public static final String TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";
    public static final String INAPP_UPDATE = "inappupdate";
    private static final long DISMISS_TIME = 10000;

    private ErrorNetworkReceiver logoutNetworkReceiver;
    private BroadcastReceiver inappReceiver;
    private boolean pauseFlag;

    private final ArrayList<DebugVolumeListener> debugVolumeListeners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logoutNetworkReceiver = new ErrorNetworkReceiver();
        inappReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                AppUpdateManagerWrapper.showSnackBarComplete(BaseActivity.this);
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseFlag = true;
        unregisterForceLogoutReceiver();
        unregisterInAppReceiver();
    }

    public void addListener(DebugVolumeListener debugVolumeListener) {
        debugVolumeListeners.add(debugVolumeListener);
    }

    public void removeDebugVolumeListener() {
        debugVolumeListeners.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // this is to make sure the context of dynamic feature is updated when activity is onBackpressed
        // hacky way of dynamic feature module, when activity is resumed after pausing.
        // SplitCompat.install initiates on onAttachBaseContext by default.
        if (pauseFlag) {
            SplitCompat.installActivity(this);
            pauseFlag = false;
        }

        sendScreenAnalytics();
        setLogCrash();

        registerForceLogoutReceiver();
        registerInAppReceiver();
        checkIfForceLogoutMustShow();
    }

    protected void sendScreenAnalytics() {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(getScreenName());
    }

    private void registerForceLogoutReceiver() {
        logoutNetworkReceiver.setReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(FORCE_LOGOUT);
        filter.addAction(SERVER_ERROR);
        filter.addAction(TIMEZONE_ERROR);
        LocalBroadcastManager.getInstance(this).registerReceiver(logoutNetworkReceiver, filter);
    }

    private void registerInAppReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(INAPP_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(inappReceiver, filter);
    }

    private void unregisterForceLogoutReceiver() {
        logoutNetworkReceiver.setReceiver(null);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(logoutNetworkReceiver);
    }

    private void unregisterInAppReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(inappReceiver);
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
        DialogForceLogout.createShow(this, getScreenName(),
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        SplitCompat.installActivity(this);
    }

    public void setLogCrash() {
        if (!GlobalConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().log(this.getClass().getCanonicalName());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getApplication() instanceof AbstractionRouter) {
            ((AbstractionRouter) getApplication()).onNewIntent(this, intent);
        }
    }

    public boolean isAllowShake() {
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            for (int i = 0, size = debugVolumeListeners.size(); i < size; i++) {
                debugVolumeListeners.get(i).onKeyUp(keyCode, event);
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}