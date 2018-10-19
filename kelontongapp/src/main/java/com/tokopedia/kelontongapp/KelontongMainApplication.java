package com.tokopedia.kelontongapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by meta on 02/10/18.
 */
public class KelontongMainApplication extends Application {

    public final String NOTIFICATION_CHANNEL_NAME = KelontongMainApplication.class.getSimpleName();
    public final static String NOTIFICATION_CHANNEL_ID = KelontongMainApplication.class.getPackage().getName();
    public final String NOTIFICATION_CHANNEL_DESC = KelontongMainApplication.class.getName();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initCrashlytics();
        createNotificationChannel();
    }

    public void initCrashlytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
            Crashlytics.setUserIdentifier(getString(R.string.app_name));
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription(NOTIFICATION_CHANNEL_DESC);
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(mChannel);
        }
    }
}
