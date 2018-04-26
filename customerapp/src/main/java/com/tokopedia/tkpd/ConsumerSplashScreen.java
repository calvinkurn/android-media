package com.tokopedia.tkpd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.ride.bookingride.view.activity.RideHomeActivity;
import com.tokopedia.ride.common.configuration.RideConfiguration;

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
        RideConfiguration rideConfiguration = new RideConfiguration(getApplicationContext());

        Intent homeIntent = HomeRouter.getHomeActivity(this);
        if ((getIntent() != null &&
                getIntent().getStringExtra(RideHomeActivity.EXTRA_LAUNCH_SHORTCUT) != null &&
                getIntent().getStringExtra(RideHomeActivity.EXTRA_LAUNCH_SHORTCUT).equalsIgnoreCase("true")) ||
                !TextUtils.isEmpty(rideConfiguration.getActiveRequestId())) {

            Intent rideHomeIntent = RideHomeActivity.getCallingIntent(this);
            if (getIntent().getExtras() != null) rideHomeIntent.putExtras(getIntent().getExtras());

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
            taskStackBuilder.addNextIntent(homeIntent);
            taskStackBuilder.addNextIntent(rideHomeIntent);
            taskStackBuilder.startActivities();

        } else {
            startActivity(homeIntent);
        }
        finish();
    }

    private void renderDynamicImage() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
        String imageUrl = remoteConfig.getString(KEY_SPLASH_IMAGE_URL);

        CommonUtils.dumper("Vishal, Splash Image :: " + imageUrl);

        //imageUrl = "https://lh3.googleusercontent.com/_EqErMcqOR4UiS84FzuXd31AX8cmFEA93LhM0KxeiS_FRC8GoTepMpOADrKzfba9Br-UbzcmgYXCL9R-zQaBVHW4UiiUOAH4fsAuSX-qlx4GIrHD0_z8Ji5Ft7Z7vMHcQ5hwZ7OQah7QZALawCfRjYBzewhU6hqGFM_zb5MrrS5nCY9eodZd74AiaT0XO0-17ndoXx-9D8Va2ockac1ggfG4TgCn8ui7wP1FHVlOuy2QsyZvn5g2p01YB9bObLz6Ah7Q_RJGG-yOgGjnZP87EQF_mMgaP2tW8LVmxjaQ7dKoQVw75Qk0kwSjB6nvuQ9B0iS-yQ6qXHxYm06cwXTsrRfa-e5mBSdpoQxy7sqmom36VT9O-IiGiY9_ezxnhFqzhoncWXKNNhbpnb6bqCBtxAc3Z462eCVMoK-Nwqa6Rq459kTcGiOYdH6p2giiF75eWQDV7a0qDY3aAQQAAwmoS7kqXI-AXhC1kqxfwOnSkitE1T3pYfolYngt2W1BPivIPId0nkxjxCZWNosfQ9Lzn3MRpcAPlCs_0m5-U_Gi13BgZEnrRaH3UQQNYQMALhhydd3p5E7buuVpiHCv2gtXaOYUA7hp3xujNDDWmvwu=w452-h803-no";
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
                        e.printStackTrace();
                        super.onLoadFailed(e, errorDrawable);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        CommonUtils.dumper("Vishal onResourceReady :: ");
                        if (resource != null) {
                            CommonUtils.dumper("Vishal onResourceReady 2:: ");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                mainLayout.setBackground(new BitmapDrawable(getResources(), resource));
                            }
                        }
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        CommonUtils.dumper("Vishal onLoadStarted :: ");
                        super.onLoadStarted(placeholder);
                    }
                });
    }
}
