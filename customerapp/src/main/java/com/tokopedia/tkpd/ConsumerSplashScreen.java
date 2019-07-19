package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.perf.metrics.Trace;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.tkpd.splash.DynamicSplash;
import com.tokopedia.tkpd.splash.DynamicSplashWrapper;
import com.tokopedia.tkpd.timber.TimberWrapper;

import java.util.Random;

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

        setContentView(R.layout.activity_splash);
        renderDynamicImage();

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

        new Handler().postDelayed(() -> {
            Intent homeIntent = MainParentActivity.start(this);
            startActivity(homeIntent);
            finishSplashTrace();
            finish();
        }, 2000);
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

    private void renderDynamicImage() {
        String dataSplash = remoteConfig.getString(RemoteConfigKey.ANDROID_SPLASH_IMAGE);

        DynamicSplash data = null;
        if (dataSplash != null && !dataSplash.isEmpty()) {
            data = DynamicSplashWrapper.transform(dataSplash);
        }

        if (data == null) {
            data = DynamicSplashWrapper.getDefaultVal();
        }

        ImageView imageViewMain = findViewById(R.id.main);
        ImageView imageViewBg = findViewById(R.id.background);

        int random = new Random().nextInt(5);

        if (data.getMainLogo() != null
                && !data.getMainLogo().isEmpty()) {

            Glide.with(this)
                    .load(data.getMainLogo())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .dontAnimate()
                    .into(imageViewMain);
        }

        if (data.getBackground() != null
                && !data.getBackground().isEmpty()
                && data.getBackground().size() > random) {

            String imageBgUrl = data.getBackground().get(random).getImageUrl();
            Glide.with(this)
                    .load(imageBgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .dontAnimate()
                    .into(imageViewBg);
        }
    }

//    private void renderDynamicImage() {
//        if (TextUtils.isEmpty(imageUrl)) {
//            return;
//        }
//        setContentView(R.layout.activity_splash);
//        mainLayout = findViewById(R.id.layout_splash);
//        Glide.with(this)
//                .load(imageUrl)
//                .asBitmap()
//                .dontAnimate()
//                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                .priority(Priority.HIGH)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                        super.onLoadFailed(e, errorDrawable);
//                    }
//
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        try {
//                            if (resource != null) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                    mainLayout.setBackground(new BitmapDrawable(getResources(), resource));
//                                }
//                            }
//                        } catch (Exception ignored) { }
//                    }
//
//                    @Override
//                    public void onLoadStarted(Drawable placeholder) {
//                        super.onLoadStarted(placeholder);
//                    }
//                });
//    }

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
