package com.tokopedia.tkpd.fcm.applink;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.core.R;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.utils.NotificationChannelId;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.fcm.applink.model.ApplinkNotificationModel;
import com.tokopedia.tkpd.fcm.applink.model.HistoryNotificationModel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author ricoharisin .
 */

public class ApplinkNotificationHelper {

    private Context context;
    private static final String GROUP_KEY_TALK = "com.tokopedia.tkpd.TALK";
    private static final String GROUP_KEY_TOPCHAT = "com.tokopedia.tkpd.TOPCHAT";
    private static final String GROUP_KEY_TRANSACTION = "com.tokopedia.tkpd.TRANSACTION";
    private static final String GROUP_KEY_NEW_ORDER = "com.tokopedia.tkpd.NEWORDER";
    private static final String GROUP_KEY_RESOLUTION = "com.tokopedia.tkpd.RESOLUTION";
    private static final String GROUP_KEY_GENERAL = "com.tokopedia.tkpd.GENERAL";
    private static final int NOTIFICATION_ID = 100;
    private static final int NOTIFICATION_ID_TALK = 200;
    private static final int NOTIFICATION_ID_CHAT = 300;
    private static final int NOTIFICATION_ID_TRANSACTION = 400;
    private static final int NOTIFICATION_ID_SELLER = 500;
    private static final int NOTIFICATION_ID_RESOLUTION = 600;


    public ApplinkNotificationHelper(Context context) {
        this.context = context;
    }

    public void notifyApplinkNotification(Bundle data) {
        ApplinkNotificationModel applinkNotificationModel = convertToApplinkModel(data);

        if (allowToShow(applinkNotificationModel.getToUserId())) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            int notificationId = generateNotifictionId(applinkNotificationModel.getApplinks());

            Notification notif = buildNotification(applinkNotificationModel, notificationId).build();

            notificationManagerCompat.notify(notificationId, notif);
        }

    }

    private NotificationCompat.Builder buildNotification(ApplinkNotificationModel applinkNotificationModel, int notificationId) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationChannelId.GENERAL);
        builder.setContentTitle(applinkNotificationModel.getDesc());
        builder.setContentText(applinkNotificationModel.getSummary());
        builder.setSmallIcon(getDrawableIcon());

        if (notificationId == NOTIFICATION_ID_TALK || notificationId == NOTIFICATION_ID_CHAT) {
            String key = notificationId == NOTIFICATION_ID_TALK ? HistoryNotification.KEY_TALK : HistoryNotification.KEY_CHAT;
            HistoryNotification historyNotification = new HistoryNotification(context, key);
            if (historyNotification.getListHistoryNotificationModel().size() == 0) {
                builder.setContentIntent(createPendingIntent(applinkNotificationModel.getApplinks(), notificationId));
                builder.setLargeIcon(getBitmap(applinkNotificationModel.getThumbnail()));
            } else {
                if (notificationId == NOTIFICATION_ID_TALK) {
                    builder.setContentIntent(createPendingIntent(Constants.Applinks.TALK, notificationId));
                } else {
                    builder.setContentIntent(createPendingIntent(Constants.Applinks.TOPCHAT_IDLESS, notificationId));
                }
            }

            NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("ME");

            for (HistoryNotificationModel historyNotificationModel : historyNotification.getListHistoryNotificationModel()) {
                messagingStyle.addMessage(historyNotificationModel.getMessage(),
                        historyNotificationModel.getTimeStamp(),
                        historyNotificationModel.getSenderName());
            }

            messagingStyle.addMessage(applinkNotificationModel.getSummary(), System.currentTimeMillis(), applinkNotificationModel.getFullName());
            messagingStyle.setConversationTitle(historyNotification.getSummary());

            historyNotification.storeNotification(applinkNotificationModel.getSummary(), applinkNotificationModel.getFullName());

            builder.setStyle(messagingStyle);

        } else {
            builder.setLargeIcon(getBitmap(applinkNotificationModel.getThumbnail()));
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(applinkNotificationModel.getSummary()));
            builder.setContentIntent(createPendingIntent(applinkNotificationModel.getApplinks(),NOTIFICATION_ID));
        }

        builder.setGroup(generateGroupKey(applinkNotificationModel.getApplinks()));

        return builder;

    }

    private NotificationCompat.Style generateStyle(ApplinkNotificationModel applinkNotificationModel, int notificationId) {
        if (notificationId == NOTIFICATION_ID_TALK) {
            HistoryNotification historyNotification = new HistoryNotification(context, HistoryNotification.KEY_TALK);
            NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("ME");

            for (HistoryNotificationModel historyNotificationModel : historyNotification.getListHistoryNotificationModel()) {
                messagingStyle.addMessage(historyNotificationModel.getMessage(),
                        historyNotificationModel.getTimeStamp(),
                        historyNotificationModel.getSenderName());
            }

            messagingStyle.addMessage(applinkNotificationModel.getSummary(), System.currentTimeMillis(), applinkNotificationModel.getFullName());
            messagingStyle.setConversationTitle(historyNotification.getSummary());

            historyNotification.storeNotification(applinkNotificationModel.getSummary(), applinkNotificationModel.getFullName());
            return messagingStyle;
        } else {
            return new NotificationCompat.BigTextStyle().bigText(applinkNotificationModel.getSummary());
        }
    }


    private ApplinkNotificationModel convertToApplinkModel(Bundle data) {
        ApplinkNotificationModel model = new ApplinkNotificationModel();
        model.setApplinks(data.getString("applinks", "tokopedia://home"));
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

        return model;
    }

    private Boolean allowToShow(String toUserId) {
        return toUserId.equals(SessionHandler.getLoginID(context));
    }

    private int generateNotifictionId(String appLink) {
        if (appLink.contains("talk")) {
            return NOTIFICATION_ID_TALK;
        } else if (appLink.contains("message")) {
            return NOTIFICATION_ID;
        } else if (appLink.contains("chat")) {
            return NOTIFICATION_ID_CHAT;
        } else if (appLink.contains("buyer")) {
            return NOTIFICATION_ID_TRANSACTION;
        } else if (appLink.contains("seller")) {
            return NOTIFICATION_ID_SELLER;
        } else if (appLink.contains("resolution")) {
            return NOTIFICATION_ID_RESOLUTION;
        } else {
            return NOTIFICATION_ID;
        }
    }

    private String generateGroupKey(String appLink) {
        if (appLink.contains("talk")) {
            return GROUP_KEY_TALK;
        } else if (appLink.contains("chat")) {
            return GROUP_KEY_TOPCHAT;
        } else if (appLink.contains("buyer")) {
            return GROUP_KEY_TRANSACTION;
        } else if (appLink.contains("seller")) {
            return GROUP_KEY_NEW_ORDER;
        } else if (appLink.contains("resolution")) {
            return GROUP_KEY_RESOLUTION;
        } else {
            return GROUP_KEY_GENERAL;
        }
    }

    private int getDrawableIcon() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.ic_status_bar_toped_topseller;
        else
            return R.drawable.ic_stat_notify_white;
    }

    private int getDrawableLargeIcon() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.qc_launcher2;
        else
            return R.drawable.qc_launcher;
    }

    private Bitmap getBitmap(String url) {
        try {
            return Glide.with(context).load(url)
                    .asBitmap()
                    .into(getImageWidth(), getImageHeight())
                    .get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e ) {
            return BitmapFactory.decodeResource(context.getResources(), getDrawableLargeIcon());
        }
    }

    private int getImageWidth() {
        return context.getResources().getDimensionPixelSize(R.dimen.notif_width);
    }

    private int getImageHeight() {
        return context.getResources().getDimensionPixelSize(R.dimen.notif_height);
    }

    private PendingIntent createPendingIntent(String appLinks, int notificationId) {
        PendingIntent resultPendingIntent;
        Intent intent = new Intent(context, DeeplinkHandlerActivity.class);
        intent.setData(Uri.parse(appLinks));
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, true);
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

