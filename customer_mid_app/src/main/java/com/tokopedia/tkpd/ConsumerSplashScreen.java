package com.tokopedia.tkpd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.newrelic.agent.android.NewRelic;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.customer_mid_app.R;
import com.tokopedia.fcmcommon.service.SyncFcmTokenService;
import com.tokopedia.installreferral.InstallReferral;
import com.tokopedia.installreferral.InstallReferralKt;
import com.tokopedia.keys.Keys;
import com.tokopedia.logger.LogManager;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.loginregister.registerpushnotif.services.RegisterPushNotificationWorker;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricoharisin on 11/22/16.
 */

public class ConsumerSplashScreen extends SplashScreen {

    public static final String WARM_TRACE = "gl_warm_start";
    public static final String SPLASH_TRACE = "gl_splash_screen";

    private PerformanceMonitoring warmTrace;
    private PerformanceMonitoring splashTrace;
    private boolean isApkTempered;

    private SharedPreferences preferences;
    private SharedPreferences.OnSharedPreferenceChangeListener deepLinkListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executeInBackground();
        setUpGoogleDeeplinkListener();
    }

    private void setUpGoogleDeeplinkListener(){
        preferences = getSharedPreferences("google.analytics.deferred.deeplink.prefs", MODE_PRIVATE);
        deepLinkListener = (sharedPreferences, key) -> {
            Log.d("DEEPLINK_LISTENER", "Deep link changed");
            if ("deeplink".equals(key)) {
                String deeplink = sharedPreferences.getString(key, null);
                Double cTime = Double.longBitsToDouble(sharedPreferences.getLong("timestamp", 0));
                Log.d("DEEPLINK_LISTENER", "Deep link retrieved: " + deeplink);
                showDeepLinkResult(deeplink);
            } };
    }

    @Override
    protected void onStart() {
        super.onStart();
        preferences.registerOnSharedPreferenceChangeListener(deepLinkListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        preferences.unregisterOnSharedPreferenceChangeListener(deepLinkListener);
        deepLinkListener = null;
    }

    public void showDeepLinkResult(String result) {
        String toastText = result;
        if (toastText == null) {
            toastText = "The deep link retrieval failed";
        } else if (toastText.isEmpty()) {
            toastText = "Deep link empty";
        }else{
            navigateToDeeplink(result);
        }
        Toast.makeText(ConsumerSplashScreen.this, toastText, Toast.LENGTH_LONG).show();
        Log.d("DEEPLINK", toastText);
    }

    private void navigateToDeeplink(String deeplink){
        Intent intent = new Intent();
        String tokopediaDeeplink = deeplink;
        if (URLUtil.isNetworkUrl(deeplink)) {
            intent.setClassName(ConsumerSplashScreen.this.getPackageName(),
                    com.tokopedia.config.GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME);
        } else {
            if (deeplink.startsWith(ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://")) {
                tokopediaDeeplink = deeplink;
            } else {
                tokopediaDeeplink = ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://" + deeplink;
            }
            intent.setClassName(ConsumerSplashScreen.this.getPackageName(),
                    com.tokopedia.config.GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME);
        }
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "splash_screen");
        messageMap.put("deeplink", tokopediaDeeplink);
        ServerLogger.log(Priority.P2, "GOOGLE_DDL", messageMap);
        intent.setData(Uri.parse(tokopediaDeeplink));
        startActivity(intent);
        finish();
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
                initializationNewRelic();
                CMPushNotificationManager.getInstance()
                        .refreshFCMTokenFromForeground(FCMCacheManager.getRegistrationId(ConsumerSplashScreen.this.getApplicationContext()), false);

                checkInstallReferrerInitialised();
                syncFcmToken();
                registerPushNotif();
                return checkApkTempered();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(chkTmprApkWeave,
                RemoteConfigKey.ENABLE_SEQ4_ASYNC, ConsumerSplashScreen.this, true);
    }

    private void initializationNewRelic() {
        NewRelic.withApplicationToken(Keys.NEW_RELIC_TOKEN_MA)
                .start(this.getApplication());
        UserSessionInterface userSession = new UserSession(this);
        if (userSession.isLoggedIn()) {
            NewRelic.setUserId(userSession.getUserId());
        }
    }

    private void syncFcmToken() {
        SyncFcmTokenService.Companion.startService(this);
    }

    private void registerPushNotif() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RegisterPushNotificationWorker.Companion.scheduleWorker(ConsumerSplashScreen.this.getApplicationContext());
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
