package com.tokopedia.notifications.factory;

import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.Nullable;

import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMEvents;
import com.tokopedia.notifications.common.IrisAnalyticsEvents;
import com.tokopedia.notifications.common.PersistentEvent;
import com.tokopedia.notifications.model.BaseNotificationModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lalit.singh
 */
public class CMNotificationFactory {

    @Nullable
    public static BaseNotification getNotification(Context context, BaseNotificationModel baseNotificationModel) {
        if (context == null) {
            return null;
        }

        if (baseNotificationModel.getType() == null) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "validation");
            messageMap.put("reason", "type_missing");
            messageMap.put("data", baseNotificationModel.toString().substring(0, (Math.min(baseNotificationModel.toString().length(),
                    CMConstant.TimberTags.MAX_LIMIT))));
            ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap);
            return null;
        }

        switch (baseNotificationModel.getType()) {
            case CMConstant.NotificationType.GENERAL:
            case CMConstant.NotificationType.BIG_IMAGE:
            case CMConstant.NotificationType.ACTION_BUTTONS: {
                return new RichDefaultNotification(context.getApplicationContext(), baseNotificationModel);
            }

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

            case CMConstant.NotificationType.DELETE_NOTIFICATION:
                cancelNotification(context, baseNotificationModel.getNotificationId());
                IrisAnalyticsEvents.INSTANCE.sendPushEvent(context, IrisAnalyticsEvents.PUSH_DELETED, baseNotificationModel);
                return null;

            case CMConstant.NotificationType.DROP_NOTIFICATION:
                cancelNotification(context, baseNotificationModel.getNotificationId());
                IrisAnalyticsEvents.INSTANCE.sendPushEvent(context, IrisAnalyticsEvents.PUSH_CANCELLED, baseNotificationModel);
                return null;
            case CMConstant.NotificationType.SILENT_PUSH:
                return null;

        }

        return null;
    }

    private static void cancelNotification(Context context, int notificationId) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }


}
