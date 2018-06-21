package com.tokopedia.tkpd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.home.HomeRouter;

/**
 * Created by ricoharisin on 11/22/16.
 */

public class ConsumerSplashScreen extends SplashScreen {

    private static final java.lang.String KEY_SPLASH_IMAGE_URL = "app_splash_image_url";
    private View mainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainLayout = findViewById(R.id.layout_splash);
        renderDynamicImage();
    }

    @Override
    public void finishSplashScreen() {
        Intent homeIntent = HomeRouter.getHomeActivity(this);
        startActivity(homeIntent);
        finish();
    }

    private void renderDynamicImage() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
        String imageUrl = remoteConfig.getString(KEY_SPLASH_IMAGE_URL);

        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }

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
