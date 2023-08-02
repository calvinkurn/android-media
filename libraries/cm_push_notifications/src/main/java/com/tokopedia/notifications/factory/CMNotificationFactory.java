package com.tokopedia.notifications.factory;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;

import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMEvents;
import com.tokopedia.notifications.common.IrisAnalyticsEvents;
import com.tokopedia.notifications.common.PersistentEvent;
import com.tokopedia.notifications.factory.custom_notifications.TokoChatBubbleChatNotification;
import com.tokopedia.notifications.factory.custom_notifications.ReplyChatNotification;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lalit.singh
 */
public class CMNotificationFactory {

    @Nullable
    public static BaseNotification getNotification(Context context, BaseNotificationModel baseNotificationModel, List<BaseNotificationModel> baseNotificationModelList) {
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

                if (baseNotificationModel.isReviewOn()) {
                    return new ReviewNotification(context.getApplicationContext(), baseNotificationModel);
                } else if (baseNotificationModel.isReplyChat()) {
                    return new ReplyChatNotification(context.getApplicationContext(), baseNotificationModel, getTopChatNotificationModelList(baseNotificationModelList));
                } else if (isEnableTokoChatBubble(context, baseNotificationModel)) {
                    return new TokoChatBubbleChatNotification(context.getApplicationContext(), baseNotificationModel, getTokoChatNotificationModelList(baseNotificationModelList), null);
                } else {
                    return new RichDefaultNotification(context.getApplicationContext(), baseNotificationModel, baseNotificationModelList);
                }
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

    public static List<BaseNotificationModel> getTopChatNotificationModelList(List<BaseNotificationModel> baseNotificationModelList) {
        List<BaseNotificationModel> topChatNotificationModelList = new ArrayList<>();
        for (BaseNotificationModel baseNotificationModel : baseNotificationModelList) {
            if (baseNotificationModel.isTopChatOn()) {
                topChatNotificationModelList.add(baseNotificationModel);
            }
        }
        return topChatNotificationModelList;
    }
    private static boolean isEnableTokoChatBubble(Context context,
                                                  BaseNotificationModel baseNotificationModel) {
        boolean isEnableBubble = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
        return isEnableBubble && getIsBubbleEnabled(context) && isTokoChatPNIdExist(baseNotificationModel);
    }

    private static boolean isTokoChatPNIdExist(BaseNotificationModel baseNotificationModel) {
        String tokoChatPNId;
        try {
            tokoChatPNId = new JSONObject(baseNotificationModel.getCustomValues() != null ? baseNotificationModel.getCustomValues() : "")
                    .optString(CMConstant.CustomValuesKeys.TOKOCHAT_PN_ID);
        } catch (JSONException e) {
            tokoChatPNId = "";
        }
        return !tokoChatPNId.isBlank();
    }

    private static boolean getIsBubbleEnabled(Context context) {
        boolean isRemoteConfigEnabled;
        try {
            isRemoteConfigEnabled = getRemoteConfig(context).getBoolean(RemoteConfigKey.IS_TOKOCHAT_BUBBLES_ENABLED, Boolean.TRUE);
        } catch (Exception exception) {
            isRemoteConfigEnabled = true;
        }
        return isRemoteConfigEnabled;
    }

    private static RemoteConfig getRemoteConfig(Context context) {
        return new FirebaseRemoteConfigImpl(context);
    }

    private static List<BaseNotificationModel> getTokoChatNotificationModelList(List<BaseNotificationModel> baseNotificationModelList) {
        List<BaseNotificationModel> tokoChatNotificationModelList = new ArrayList<>();
        for (BaseNotificationModel baseNotificationModel : baseNotificationModelList) {
            if (isTokoChatPNIdExist(baseNotificationModel)) {
                if (baseNotificationModel.getTitle() != null && !baseNotificationModel.getTitle().isBlank()) {
                    tokoChatNotificationModelList.add(baseNotificationModel);
                }
            }
        }
        return tokoChatNotificationModelList;
    }
}