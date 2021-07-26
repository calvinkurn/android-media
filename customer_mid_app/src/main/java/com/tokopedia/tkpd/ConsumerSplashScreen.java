package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.analytics.performance.util.SplashScreenPerformanceTracker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.customer_mid_app.R;
import com.tokopedia.fcmcommon.service.SyncFcmTokenService;
import com.tokopedia.installreferral.InstallReferral;
import com.tokopedia.installreferral.InstallReferralKt;
import com.tokopedia.logger.LogManager;
import com.tokopedia.loginregister.login.service.RegisterPushNotifService;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

/**
 * Created by ricoharisin on 11/22/16.
 */

public class ConsumerSplashScreen extends SplashScreen {

    public static final String WARM_TRACE = "gl_warm_start";
    public static final String SPLASH_TRACE = "gl_splash_screen";

    private PerformanceMonitoring warmTrace;
    private PerformanceMonitoring splashTrace;
    private boolean isApkTempered;

    @DeepLink(ApplinkConst.CONSUMER_SPLASH_SCREEN)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent destination;
        destination = new Intent(context, ConsumerSplashScreen.class)
                .setData(uri.build())
                .putExtras(extras);
        return destination;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SplashScreenPerformanceTracker.startMonitoring();
        super.onCreate(savedInstanceState);
        executeInBackground();
    }

    private void checkInstallReferrerInitialised() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(ConsumerSplashScreen.this, InstallReferralKt.KEY_INSTALL_REF_SHARED_PREF_FILE_NAME);
        Boolean installRefInitialised = localCacheHandler.getBoolean(InstallReferralKt.KEY_INSTALL_REF_INITIALISED);
        if (!installRefInitialised) {
            localCacheHandler.applyEditor();
            new InstallReferral().initilizeInstallReferral(this);
        }
    }

    private void executeInBackground() {
        WeaveInterface chkTmprApkWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Boolean execute() {
                CMPushNotificationManager.getInstance()
                        .refreshFCMTokenFromForeground(FCMCacheManager.getRegistrationId(ConsumerSplashScreen.this.getApplicationContext()), false);

                checkInstallReferrerInitialised();
                syncFcmToken();
                registerPushNotif();
                return checkApkTempered();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(chkTmprApkWeave,
                RemoteConfigKey.ENABLE_SEQ4_ASYNC, ConsumerSplashScreen.this);
    }

    private void syncFcmToken() {
        SyncFcmTokenService.Companion.startService(this);
    }

    private void registerPushNotif() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RegisterPushNotifService.Companion.startService(getApplicationContext());
        }
    }

    @NotNull
    private Boolean checkApkTempered() {
        isApkTempered = false;
        try {
            getResources().getDrawable(R.drawable.launch_screen);
        } catch (Exception e) {
            isApkTempered = true;
            runOnUiThread(() -> setTheme(R.style.Theme_Tokopedia3_PlainGreen));
        }
        checkExecTemperedFlow();
        return true;
    }

    private void checkExecTemperedFlow() {
        if (isApkTempered) {
            startActivity(new Intent(this, FallbackActivity.class));
            finish();
        }
    }

    @Override
    public void finishSplashScreen() {
        if (isApkTempered) {
            return;
        }

        Intent homeIntent = new Intent(this, MainParentActivity.class);
        boolean needClearTask = getIntent() == null || !getIntent().hasExtra("branch");
        if (needClearTask) {
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(homeIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if (needClearTask) {
            finishAffinity();
        } else {
            finish();
        }
        SplashScreenPerformanceTracker.stopMonitoring();
    }

    private void startWarmStart() {
        warmTrace = PerformanceMonitoring.start(WARM_TRACE);
    }

    private void finishWarmStart() {
        warmTrace.stopTrace();
    }

    private void startSplashTrace() {
        splashTrace = PerformanceMonitoring.start(SPLASH_TRACE);
    }

    private void finishSplashTrace() {
        splashTrace.stopTrace();
    }

    @Override
    protected RemoteConfig.Listener getRemoteConfigListener() {
        return new RemoteConfig.Listener() {
            @Override
            public void onComplete(RemoteConfig remoteConfig) {
                LogManager logManager = LogManager.instance;
                if (logManager!= null) {
                    logManager.refreshConfig();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        };
    }
}
