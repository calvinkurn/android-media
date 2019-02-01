package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

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
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;

/**
 * Created by ricoharisin on 11/22/16.
 */

public class ConsumerSplashScreen extends SplashScreen {

    public static final String WARM_TRACE = "gl_warm_start";
    public static final String SPLASH_TRACE = "gl_splash_screen";

    private static final java.lang.String KEY_SPLASH_IMAGE_URL = "app_splash_image_url";
    private View mainLayout;

    private PerformanceMonitoring warmTrace;
    private PerformanceMonitoring splashTrace;

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

        renderDynamicImage();

        finishWarmStart();
    }

    @Override
    public void finishSplashScreen() {
        Intent homeIntent = MainParentActivity.start(this);
        startActivity(homeIntent);
        finishSplashTrace();
        finish();
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
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
        String imageUrl = remoteConfig.getString(KEY_SPLASH_IMAGE_URL);

        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        setContentView(R.layout.activity_splash);
        mainLayout = findViewById(R.id.layout_splash);
        Glide.with(this)
                .load(imageUrl)
                .asBitmap()
                .dontAnimate()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        try {
                            if (resource != null) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    mainLayout.setBackground(new BitmapDrawable(getResources(), resource));
                                }
                            }
                        } catch (Exception ex) {
                        }
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                    }
                });
    }
}
