package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.tkpd.timber.TimberWrapper;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;
import com.tokopedia.weaver.WeaverFirebaseConditionCheck;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricoharisin on 11/22/16.
 */

public class ConsumerSplashScreen extends SplashScreen {

    public static final String WARM_TRACE = "gl_warm_start";
    public static final String SPLASH_TRACE = "gl_splash_screen";
    public static final String IRIS_ANALYTICS_APP_SITE_OPEN = "appSiteOpen";
    private static final String IRIS_ANALYTICS_EVENT_KEY = "event";

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
        startWarmStart();
        startSplashTrace();

        super.onCreate(savedInstanceState);
        createAndCallChkApk();

        finishWarmStart();

        CMPushNotificationManager.getInstance()
                .refreshFCMTokenFromForeground(FCMCacheManager.getRegistrationId(this.getApplicationContext()), false);


        trackIrisEventForAppOpen();

    }

    private void createAndCallChkApk(){
        WeaveInterface chkTmprApkWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Boolean execute() {
                return checkApkTempered();
            }
        };
        Weaver.Companion.executeWeaveCoRoutine(chkTmprApkWeave,
                new WeaverFirebaseConditionCheck(RemoteConfigKey.ENABLE_SEQ4_ASYNC, remoteConfig));
    }

    private void trackIrisEventForAppOpen() {
        Iris instance = IrisAnalytics.Companion.getInstance(this);
        Map<String, Object> map = new HashMap<>();
        map.put(IRIS_ANALYTICS_EVENT_KEY, IRIS_ANALYTICS_APP_SITE_OPEN);
        instance.saveEvent(map);
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

    private void checkExecTemperedFlow(){
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

        Intent homeIntent = new Intent(this, MainParentActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finishSplashTrace();
        finishAffinity();
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
                TimberWrapper.initByConfig(getApplication(), remoteConfig);
            }

            @Override
            public void onError(Exception e) {

            }
        };
    }
}
