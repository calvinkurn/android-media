package com.tokopedia.kelontongapp.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.tokopedia.kelontongapp.KelontongConstant;
import com.tokopedia.kelontongapp.KelontongMainActivity;
import com.tokopedia.kelontongapp.R;

/**
 * Created by meta on 19/10/18.
 */
public class NotificationBuilder {

    private NotificationCompat.Builder builder;

    public NotificationBuilder(Context context, NotificationModel notificationModel, int notificationId) {
        this.builder = createNotification(context, notificationModel, notificationId);
    }

    private NotificationCompat.Builder createNotification(Context context, NotificationModel notificationModel, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, KelontongConstant.NotificationConstant.CHANNEL_GENERAL);
        builder.setContentTitle(TextUtils.isEmpty(notificationModel.getTitle()) ? context.getResources().getString(R.string.title_general_push_notification) : notificationModel.getTitle());
        builder.setContentText(notificationModel.getDesc());
        builder.setSmallIcon(setIcon());
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), setBigIcon()));
        if (allowGroup())
            builder.setGroup(KelontongConstant.NotificationConstant.GROUP_GENERAL);
        builder.setContentIntent(createPendingIntent(context, notificationId));
        builder.setAutoCancel(true);

        if (isAllowBell()) {
            builder.setSound(setSound());
            builder.setVibrate(setVibrate());
        }
        return builder;
    }

    public Notification build() {
        return builder.build();
    }

    private Integer setIcon() {
        return R.mipmap.ic_launcher;
    }

    private Integer setBigIcon() {
        return R.mipmap.ic_launcher;
    }

    private boolean isAllowBell() {
        return true;
    }

    private Uri setSound() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    private long[] setVibrate() {
        return new long[]{500, 500};
    }

    private Boolean allowGroup() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT;
    }

    private PendingIntent createPendingIntent(Context context, int notificationId) {
        PendingIntent resultPendingIntent;
        Intent intent = KelontongMainActivity.start(context);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        } else {
            resultPendingIntent = PendingIntent.getActivity(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }

        return resultPendingIntent;
    }
}
