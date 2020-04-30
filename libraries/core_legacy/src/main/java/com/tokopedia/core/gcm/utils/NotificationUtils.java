package com.tokopedia.core.gcm.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;

import com.tokopedia.core.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 1/12/17.
 */

public class NotificationUtils {
    private static final String MOVE_TO_CART_KEY = "MOVE_TO_CART_KEY";
    private static final String WHICH_FRAGMENT_KEY = "which_fragment_key";


    public static Intent configureGeneralIntent(Intent intent) {
        intent.putExtra("from_notif", true);
        intent.putExtra("unread", false);
        return intent;
    }

    public static Intent configurePromoIntent(Intent intent, Bundle data) {
        if (data.getInt("keylogin1", -99) != -99) {
            intent.putExtra(
                    WHICH_FRAGMENT_KEY,
                    data.getInt("keylogin1")
            );
            intent.putExtra(
                    MOVE_TO_CART_KEY,
                    data.getInt("keylogin2")
            );
        }
        return intent;
    }

    /**
     * Set notification channel if device >= oreo
     * @param context
     */
    public static void setNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannels(getNotificationChannels(context));
            }
        }
    }

    /**
     * Get all channel that need to be registered on notification manager
     * @param context
     * @return List of Notification Channel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static List<NotificationChannel> getNotificationChannels(Context context) {
        List<NotificationChannel> channels = new ArrayList<>();

        channels.add(
                new NotificationChannel(
                        NotificationChannelId.GENERAL,
                        context.getString(R.string.notif_channel_general),
                        NotificationManager.IMPORTANCE_HIGH
                )
        );

        return channels;
    }
}
