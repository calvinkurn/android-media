package com.tokopedia.pushnotif;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.pushnotif.factory.SummaryNotificationFactory;
import com.tokopedia.pushnotif.factory.TalkNotificationFactory;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;
import com.tokopedia.pushnotif.model.HistoryNotificationModel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author ricoharisin .
 */

public class ApplinkNotificationHelper {

    private Context context;

    public ApplinkNotificationHelper(Context context) {
        this.context = context;
    }

    public void notifyApplinkNotification(Bundle data) {
        ApplinkNotificationModel applinkNotificationModel = convertToApplinkModel(data);

        if (allowToShow(context, applinkNotificationModel.getToUserId())) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            int notificationId = generateNotifictionId(applinkNotificationModel.getApplinks());

            if (notificationId == Constant.NotificationId.TALK) {
                Notification notifTalk = new TalkNotificationFactory(context)
                        .createTalkNotification(applinkNotificationModel, notificationId);

                Notification notifSummary = new SummaryNotificationFactory(context)
                        .createSummaryNotification(applinkNotificationModel, notificationId);
            }

            Notification notif = buildNotification(applinkNotificationModel, notificationId).build();

            notificationManagerCompat.notify(notificationId, notif);
        }

    }



    private NotificationCompat.Builder buildNotification(ApplinkNotificationModel applinkNotificationModel, int notificationId) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL);
        builder.setContentTitle(applinkNotificationModel.getDesc());
        builder.setContentText(applinkNotificationModel.getSummary());
        builder.setSmallIcon(getDrawableIcon());

        if (notificationId == Constant.NotificationId.TALK || notificationId == Constant.NotificationId.CHAT) {
            /*String key = notificationId == Constant.NotificationId.TALK ? HistoryNotification.KEY_TALK : HistoryNotification.KEY_CHAT;
            HistoryNotification historyNotification = new HistoryNotification(context, key);
            if (historyNotification.getListHistoryNotificationModel().size() == 0) {
                builder.setContentIntent(createPendingIntent(applinkNotificationModel.getApplinks(), notificationId));
                builder.setLargeIcon(getBitmap(applinkNotificationModel.getThumbnail()));
            } else {
                if (notificationId == Constant.NotificationId.TALK) {
                    builder.setContentIntent(createPendingIntent(ApplinkConst.TALK, notificationId));
                } else {
                    builder.setContentIntent(createPendingIntent(ApplinkConst.TOPCHAT_IDLESS, notificationId));
                }
            }*/

            NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("ME");

            /*for (HistoryNotificationModel historyNotificationModel : historyNotification.getListHistoryNotificationModel()) {
                messagingStyle.addMessage(historyNotificationModel.getMessage(),
                        historyNotificationModel.getTimeStamp(),
                        historyNotificationModel.getSenderName());
            }*/

            messagingStyle.addMessage(applinkNotificationModel.getSummary(), System.currentTimeMillis(), applinkNotificationModel.getFullName());
            //messagingStyle.setConversationTitle(historyNotification.getSummary());

            //historyNotification.storeNotification(applinkNotificationModel.getSummary(), applinkNotificationModel.getFullName());

            builder.setStyle(messagingStyle);

        } else {
            builder.setLargeIcon(getBitmap(applinkNotificationModel.getThumbnail()));
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(applinkNotificationModel.getSummary()));
            builder.setContentIntent(createPendingIntent(applinkNotificationModel.getApplinks(), Constant.NotificationId.GENERAL));
        }

        builder.setGroup(generateGroupKey(applinkNotificationModel.getApplinks()));

        return builder;

    }

    private NotificationCompat.Style generateStyle(ApplinkNotificationModel applinkNotificationModel, int notificationId) {
       /* if (notificationId == Constant.NotificationId.TALK) {
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
        }*/
       return null;
    }


    public static ApplinkNotificationModel convertToApplinkModel(Bundle data) {
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

    public static Boolean allowToShow(Context context, String toUserId) {
        String loginId = ((AbstractionRouter) context.getApplicationContext()).getSession().getUserId();
        return toUserId.equals(loginId);
    }

    public static int getNotificationId(String appLinks) {
        Uri uri = Uri.parse(appLinks);
        if (appLinks.contains("talk") || appLinks.contains("chat")) {
            return Integer.parseInt(uri.getLastPathSegment());
        }

        return 0;
    }

    public static int generateNotifictionId(String appLink) {
        if (appLink.contains("talk")) {
            return Constant.NotificationId.TALK;
        } else if (appLink.contains("message")) {
            return Constant.NotificationId.GENERAL;
        } else if (appLink.contains("chat")) {
            return Constant.NotificationId.CHAT;
        } else if (appLink.contains("buyer")) {
            return Constant.NotificationId.TRANSACTION;
        } else if (appLink.contains("seller")) {
            return Constant.NotificationId.SELLER;
        } else if (appLink.contains("resolution")) {
            return Constant.NotificationId.RESOLUTION;
        } else {
            return Constant.NotificationId.GENERAL;
        }
    }

    private String generateGroupKey(String appLink) {
        if (appLink.contains("talk")) {
            return Constant.NotificationGroup.TALK;
        } else if (appLink.contains("chat")) {
            return Constant.NotificationGroup.TOPCHAT;
        } else if (appLink.contains("buyer")) {
            return Constant.NotificationGroup.TRANSACTION;
        } else if (appLink.contains("seller")) {
            return Constant.NotificationGroup.NEW_ORDER;
        } else if (appLink.contains("resolution")) {
            return Constant.NotificationGroup.RESOLUTION;
        } else {
            return Constant.NotificationGroup.GENERAL;
        }
    }

    private int getDrawableIcon() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.ic_status_bar_notif_sellerapp;
        else
            return R.drawable.ic_status_bar_notif_customerapp;
    }

    private int getDrawableLargeIcon() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.ic_big_notif_sellerapp;
        else
            return R.drawable.ic_big_notif_customerapp;
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
        Intent intent = RouteManager.getIntent(context, appLinks);
        intent.setData(Uri.parse(appLinks));
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.EXTRA_APPLINK_FROM_PUSH, true);
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

