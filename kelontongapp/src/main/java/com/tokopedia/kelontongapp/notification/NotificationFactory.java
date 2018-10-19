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
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.tokopedia.kelontongapp.KelontongConstant;
import com.tokopedia.kelontongapp.KelontongMainActivity;
import com.tokopedia.kelontongapp.R;

/**
 * Created by meta on 19/10/18.
 */
public class NotificationFactory {

    public static void showNotification(Context context, Bundle data) {

        NotificationModel notificationModel = convertBundleToModel(data);
        if (allowToShow(notificationModel)) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            int notificationId = KelontongConstant.NotificationConstant.NOTIFICATION_ID_GENERAL;
            Notification notifChat = createNotification(context, notificationModel, notificationId);

            notificationManagerCompat.notify(notificationId, notifChat);
        }
    }

    public static Notification createNotification(Context context, NotificationModel notificationModel, int notificationId) {
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
        return builder.build();
    }

    private static Integer setIcon() {
        return R.mipmap.ic_launcher;
    }

    private static Integer setBigIcon() {
        return R.mipmap.ic_launcher;
    }

    private static boolean isAllowBell() {
        return true;
    }

    private static Uri setSound() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    private static long[] setVibrate() {
        return new long[]{500, 500};
    }

    protected static PendingIntent createPendingIntent(Context context, int notificationId) {
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

    public static Boolean allowToShow(NotificationModel notificationModel) {
        return KelontongConstant.NotificationConstant.LOWER_CODE <= notificationModel.getTkpCode() && notificationModel.getTkpCode() <= KelontongConstant.NotificationConstant.UPPER_CODE;
    }

    public static NotificationModel convertBundleToModel(Bundle data) {
        NotificationModel model = new NotificationModel();
        model.setApplinks(data.getString("applinks", ""));
        model.setCounter(data.getString("counter", ""));
        model.setCreateTime(data.getString("create_time", ""));
        model.setDesc(data.getString("desc", ""));
        model.setFullName(data.getString("full_name", ""));
        model.setGId(data.getString("g_id", ""));
        model.setLoginRequired(data.getString("login_required", "false").equals("true"));
        model.setSenderId(data.getString("sender_id", ""));
        model.setSummary(data.getString("summary", ""));
        model.setThumbnail(data.getString("thumbnail", ""));
        model.setTkpCode(Integer.parseInt(data.getString("tkp_code", "0")));
        model.setToUserId(data.getString("to_user_id", ""));
        model.setTitle(data.getString("title", ""));
        return model;
    }


    public static Boolean allowGroup() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT;
    }
}
