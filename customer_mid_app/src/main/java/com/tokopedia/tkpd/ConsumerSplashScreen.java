package com.tokopedia.tkpd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;

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
import com.tokopedia.installreferral.InstallReferrerInterface;
import com.tokopedia.keys.Keys;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.logger.LogManager;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.loginregister.registerpushnotif.services.RegisterPushNotificationWorker;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
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
    public static final String GOOGLE_ANALYTICS_DEFERRED_DEEPLINK_PREFERENCE= "google.analytics.deferred.deeplink.prefs";
    public static final String GOOGLE_DDL_DEEPLINK_KEY = "deeplink";
    public static final String TYPE_KEY = "type";
    public static final String GOOGLE_DDL_KEY = "GOOGLE_DDL";
    public static final String SPLASH_SCREEN_TYPE = "splash_screen";


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
        preferences = getSharedPreferences(GOOGLE_ANALYTICS_DEFERRED_DEEPLINK_PREFERENCE, MODE_PRIVATE);
        deepLinkListener = (sharedPreferences, key) -> {
            if (GOOGLE_DDL_DEEPLINK_KEY.equals(key)) {
                String deeplink = sharedPreferences.getString(key, null);
                navigateToDeeplink(deeplink);
            } };
    }

    @Override
    protected void onStart() {
        super.onStart();
        preferences.registerOnSharedPreferenceChangeListener(deepLinkListener);
        initiateBranchAndInstallReferrerFlow();
    }

    private void initiateBranchAndInstallReferrerFlow(){
        if(LinkerManager.getInstance().isFirstAppOpen(getApplicationContext())){
            LinkerManager.getInstance().setDelayedSessionInitFlag();
        }else {
            getBranchDefferedDeeplink();
        }
        checkInstallReferrerInitialised();
    }

    @Override
    protected void onStop() {
        super.onStop();
        preferences.unregisterOnSharedPreferenceChangeListener(deepLinkListener);
        deepLinkListener = null;
    }

    private void navigateToDeeplink(String deeplink){
        if(!TextUtils.isEmpty(deeplink)) {
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
            messageMap.put(TYPE_KEY, SPLASH_SCREEN_TYPE);
            messageMap.put(GOOGLE_DDL_DEEPLINK_KEY, tokopediaDeeplink);
            ServerLogger.log(Priority.P2, GOOGLE_DDL_KEY, messageMap);
            intent.setData(Uri.parse(tokopediaDeeplink));
            startActivity(intent);
            finish();
        }
    }

    public InstallReferrerInterface getInstallReferrerInterface(){
        return new InstallReferrerInterface() {
            @Override
            public void installReferrerDataRetrived(@NonNull String installReferrerData) {
                LinkerManager.getInstance().setDataFromInstallReferrerParams(installReferrerData);
                getBranchDefferedDeeplink();
            }
        };
    }

    private void checkInstallReferrerInitialised() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(ConsumerSplashScreen.this, InstallReferralKt.KEY_INSTALL_REF_SHARED_PREF_FILE_NAME);
        Boolean installRefInitialised = localCacheHandler.getBoolean(InstallReferralKt.KEY_INSTALL_REF_INITIALISED);
        if (!installRefInitialised) {
            localCacheHandler.applyEditor();
            InstallReferral installReferral = new InstallReferral();
            if(LinkerManager.getInstance().isFirstAppOpen(getApplicationContext())) {
                installReferral.setInstallReferrerInterface(getInstallReferrerInterface());
            }
            installReferral.initilizeInstallReferral(this);
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

                syncFcmToken();
                registerPushNotif();
                return checkApkTempered();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(chkTmprApkWeave,
                RemoteConfigKey.ENABLE_SEQ4_ASYNC, ConsumerSplashScreen.this, true);
    }

    private void initializationNewRelic() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
        boolean isEnableInitNrInAct =
                remoteConfig.getBoolean(RemoteConfigKey.ENABLE_INIT_NR_IN_ACTIVITY, true);
        if (isEnableInitNrInAct) {
            NewRelic.withApplicationToken(Keys.NEW_RELIC_TOKEN_MA)
                    .start(this.getApplication());
            UserSessionInterface userSession = new UserSession(this);
            if (userSession.isLoggedIn()) {
                NewRelic.setUserId(userSession.getUserId());
            }
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
