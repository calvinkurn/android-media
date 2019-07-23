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
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.tkpd.timber.TimberWrapper;

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
        checkApkTempered();

        startWarmStart();
        startSplashTrace();

        super.onCreate(savedInstanceState);

        if (isApkTempered) {
            startActivity(new Intent(this, FallbackActivity.class));
            finish();
        }

        finishWarmStart();

        CMPushNotificationManager.getInstance()
                .refreshFCMTokenFromForeground(FCMCacheManager.getRegistrationId(this.getApplicationContext()), false);


    }

    private void checkApkTempered() {
        isApkTempered = false;
        try {
            getResources().getDrawable(R.drawable.launch_screen);
        } catch (Exception e) {
            isApkTempered = true;
            setTheme(R.style.Theme_Tokopedia3_PlainGreen);
        }
    }

    @Override
    public void finishSplashScreen() {
        if (isApkTempered) {
            return;
        }

        Intent homeIntent = MainParentActivity.start(this);
        startActivity(homeIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finishSplashTrace();
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
                TimberWrapper.initByConfig(remoteConfig);
            }

            @Override
            public void onError(Exception e) {

            }
        };
    }
}
