package com.tokopedia.notifications.factory;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMEvents;
import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.notifications.common.IrisAnalyticsEvents;
import com.tokopedia.notifications.common.PayloadConverter;
import com.tokopedia.notifications.common.PersistentEvent;
import com.tokopedia.notifications.model.ActionButton;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.notifications.model.Carousel;
import com.tokopedia.notifications.model.Grid;
import com.tokopedia.notifications.model.Media;
import com.tokopedia.notifications.model.PersistentButton;
import com.tokopedia.notifications.model.ProductInfo;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lalit.singh
 */
public class CMNotificationFactory {

    @Nullable
    public static BaseNotification getNotification(Context context, BaseNotificationModel baseNotificationModel) {
        if (context == null) {
            return null;
        }
        IrisAnalyticsEvents.INSTANCE.sendPushReceiveEvent(context, baseNotificationModel);
        if (CMConstant.NotificationType.SILENT_PUSH.equals(baseNotificationModel.getType())) {
            handleSilentPush(context, baseNotificationModel);
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isChannelBlocked(context, baseNotificationModel)) {
            //todo notify to server for Blocked Channel By User.
        } else {
            if (baseNotificationModel.getType() == null)
                return null;
            switch (baseNotificationModel.getType()) {

                case CMConstant.NotificationType.GENERAL:
                    if (CMNotificationUtils.INSTANCE.hasActionButton(baseNotificationModel)) {
                        return new ActionNotification(context.getApplicationContext(), baseNotificationModel);
                    }
                    return (new GeneralNotification(context.getApplicationContext(), baseNotificationModel));

                case CMConstant.NotificationType.ACTION_BUTTONS:
                    return new ActionNotification(context.getApplicationContext(), baseNotificationModel);

                case CMConstant.NotificationType.BIG_IMAGE:
                    if (CMNotificationUtils.INSTANCE.hasActionButton(baseNotificationModel)) {
                        return new ActionNotification(context.getApplicationContext(), baseNotificationModel);
                    }
                    return (new ImageNotification(context.getApplicationContext(), baseNotificationModel));

                case CMConstant.NotificationType.PERSISTENT:
                    CMEvents.postGAEvent(PersistentEvent.EVENT_VIEW_NOTIFICATION, PersistentEvent.EVENT_CATEGORY,
                            PersistentEvent.EVENT_ACTION_PUSH_RECEIVED, PersistentEvent.EVENT_LABEL);
                    return (new PersistentNotification(context.getApplicationContext(), baseNotificationModel));

                case CMConstant.NotificationType.GRID_NOTIFICATION:
                    return (new GridNotification(context.getApplicationContext(), baseNotificationModel));

                case CMConstant.NotificationType.CAROUSEL_NOTIFICATION:
                    return (new CarouselNotification(context.getApplicationContext(), baseNotificationModel));

                case CMConstant.NotificationType.VISUAL_NOTIIFICATION:
                    return new VisualNotification(context.getApplicationContext(), baseNotificationModel);

                case CMConstant.NotificationType.PRODUCT_NOTIIFICATION:
                    return new ProductNotification(context.getApplicationContext(), baseNotificationModel);

                case CMConstant.NotificationType.BIG_IMAGE_BANNER:
                    return new BannerNotification(context.getApplicationContext(), baseNotificationModel);

                case CMConstant.NotificationType.DELETE_NOTIFICATION:
                    cancelNotification(context, baseNotificationModel.getNotificationId());
                    return null;

            }
        }
        return null;
    }

    private static void handleSilentPush(Context context, BaseNotificationModel baseNotificationModel) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean isChannelBlocked(Context context, BaseNotificationModel baseNotificationModel) {
        try {
            String channelId = baseNotificationModel.getChannelName();
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = manager.getNotificationChannel(channelId);
            return null != channel && channel.getImportance() == NotificationManager.IMPORTANCE_NONE;
        } catch (NullPointerException e) {
        }
        return false;
    }

    private static void cancelNotification(Context context, int notificationId) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }



}
