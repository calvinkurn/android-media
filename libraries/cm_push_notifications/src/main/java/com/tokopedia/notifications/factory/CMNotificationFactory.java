package com.tokopedia.notifications.factory;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMEvents;
import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.notifications.common.CmEventPost;
import com.tokopedia.notifications.model.ActionButton;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.notifications.model.Carousal;
import com.tokopedia.notifications.model.Grid;
import com.tokopedia.notifications.model.Media;
import com.tokopedia.notifications.model.PersistentButton;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lalit.singh
 */
public class CMNotificationFactory {

    private static final String TAG = CMNotificationFactory.class.getSimpleName();

    public static BaseNotification getNotification(Context context, Bundle bundle) {
        BaseNotificationModel baseNotificationModel = convertToBaseModel(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isChannelBlocked(context, baseNotificationModel)) {
            //todo notify to server for Blocked Channel By User.
        } else {
            switch (baseNotificationModel.getType()) {
                case CMConstant.NotificationType.GENERAL:
                    if (CMNotificationUtils.hasActionButton(baseNotificationModel)) {
                        return (new ActionNotification(context.getApplicationContext(), baseNotificationModel));
                    }
                    return (new GeneralNotification(context.getApplicationContext(), baseNotificationModel));
                case CMConstant.NotificationType.GRID_NOTIFICATION:
                    return (new GridNotification(context.getApplicationContext(), baseNotificationModel));
                case CMConstant.NotificationType.ACTION_BUTTONS:
                    return (new ActionNotification(context.getApplicationContext(), baseNotificationModel));
                case CMConstant.NotificationType.BIG_IMAGE:
                    if (CMNotificationUtils.hasActionButton(baseNotificationModel)) {
                        return (new ActionNotification(context.getApplicationContext(), baseNotificationModel));
                    }
                    return (new ImageNotification(context.getApplicationContext(), baseNotificationModel));
                case CMConstant.NotificationType.PERSISTENT:
                    CmEventPost.postEvent(context, CMEvents.PersistentEvent.EVENT_VIEW_NOTIFICATION, CMEvents.PersistentEvent.EVENT_CATEGORY,
                            CMEvents.PersistentEvent.EVENT_ACTION_PUSH_RECEIVED, CMEvents.PersistentEvent.EVENT_LABEL);
                    return (new PersistentNotification(context.getApplicationContext(), baseNotificationModel));
                case CMConstant.NotificationType.DELETE_NOTIFICATION:
                    cancelNotification(context, baseNotificationModel.getNotificationId());
                    return null;
                case CMConstant.NotificationType.CAROUSEL_NOTIFICATION:
                    return (new CarouselNotification(context.getApplicationContext(), baseNotificationModel));
                case CMConstant.NotificationType.VISUAL_NOTIIFICATION:
                    return new VisualNotification(context.getApplicationContext(), baseNotificationModel);
            }
        }
        return null;
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

    private static BaseNotificationModel convertToBaseModel(Bundle data) {
        BaseNotificationModel model = new BaseNotificationModel();
        model.setIcon(data.getString(CMConstant.PayloadKeys.ICON, ""));
        model.setSoundFileName(data.getString(CMConstant.PayloadKeys.SOUND, ""));
        model.setNotificationId(Integer.parseInt(data.getString(CMConstant.PayloadKeys.NOTIFICATION_ID, "500")));
        model.setTribeKey(data.getString(CMConstant.PayloadKeys.TRIBE_KEY, ""));
        model.setType(data.getString(CMConstant.PayloadKeys.NOTIFICATION_TYPE, ""));
        model.setChannelName(data.getString(CMConstant.PayloadKeys.CHANNEL, ""));
        model.setTitle(data.getString(CMConstant.PayloadKeys.TITLE, ""));
        model.setDetailMessage(data.getString(CMConstant.PayloadKeys.DESCRIPTION, ""));
        model.setMessage(data.getString(CMConstant.PayloadKeys.MESSAGE, ""));
        model.setMedia(getMedia(data));
        model.setAppLink(data.getString(CMConstant.PayloadKeys.APP_LINK, ApplinkConst.HOME));
        model.setActionButton(getActionButtons(data));
        model.setPersistentButtonList(getPersistentNotificationData(data));
        model.setVideoPushModel(getVideoNotificationData(data));
        model.setCustomValues(getCustomValues(data));
        model.setCarousalList(getCarousalList(data));
        model.setCarousalIndex(data.getInt(CMConstant.PayloadKeys.CAROUSEL_INDEX, 0));
        model.setVibration(data.getBoolean(CMConstant.PayloadKeys.VIBRATE, true));
        model.setUpdateExisting(data.getBoolean(CMConstant.PayloadKeys.UPDATE, false));
        model.setGridList(getGridList(data));
        model.setSubText(data.getString(CMConstant.PayloadKeys.SUB_TEXT));
        model.setVisualCollapsedImageUrl(data.getString(CMConstant.PayloadKeys.VISUAL_COLLAPSED_IMAGE));
        model.setVisualExpandedImageUrl(data.getString(CMConstant.PayloadKeys.VISUAL_EXPANDED_IMAGE));
        return model;
    }

    private static Media getMedia(Bundle extras) {
        String actions = extras.getString(CMConstant.PayloadKeys.MEDIA);
        if (TextUtils.isEmpty(actions)) {
            return null;
        }
        try {
            return new Gson().fromJson(actions, Media.class);
        } catch (Exception e) {
            Log.e(TAG, "CM-getMedia", e);
        }
        return null;
    }

    private static JSONObject getCustomValues(Bundle extras) {
        String values = extras.getString(CMConstant.PayloadKeys.CUSTOM_VALUE);
        if (TextUtils.isEmpty(values)) {
            return null;
        }
        try {
            return new JSONObject(values);
        } catch (Exception e) {
            Log.e(TAG, "CM-getCustomValues", e);
        }
        return null;
    }

    private static List<ActionButton> getActionButtons(Bundle extras) {
        String actions = extras.getString(CMConstant.PayloadKeys.ACTION_BUTTON);
        if (TextUtils.isEmpty(actions)) {
            return null;
        }
        try {
            Type listType = new TypeToken<ArrayList<ActionButton>>() {
            }.getType();
            return new Gson().fromJson(actions, listType);
        } catch (Exception e) {
            Log.e(TAG, "CM-getActionButtons", e);
        }
        return null;
    }

    private static List<PersistentButton> getPersistentNotificationData(Bundle bundle) {
        String persistentData = bundle.getString(CMConstant.PayloadKeys.PERSISTENT_DATA);
        if (TextUtils.isEmpty(persistentData)) {
            return null;
        }
        try {
            Type listType = new TypeToken<ArrayList<PersistentButton>>() {
            }.getType();
            return new Gson().fromJson(persistentData, listType);
        } catch (Exception e) {

            Log.e(TAG, "CM-getPersistentNotificationData", e);
        }
        return null;
    }

    private static List<Grid> getGridList(Bundle bundle) {
        String persistentData = bundle.getString(CMConstant.PayloadKeys.GRID_DATA);
        if (TextUtils.isEmpty(persistentData)) {
            return null;
        }
        try {
            Type listType = new TypeToken<ArrayList<Grid>>() {
            }.getType();
            return new Gson().fromJson(persistentData, listType);
        } catch (Exception e) {
            Log.e(TAG, "CM-getGridList", e);
        }
        return null;
    }

    private static JSONObject getVideoNotificationData(Bundle bundle) {

        String values = bundle.getString(CMConstant.PayloadKeys.VIDEO_DATA);
        if (TextUtils.isEmpty(values)) {
            return null;
        }
        try {
            return new JSONObject(values);
        } catch (Exception e) {
            Log.e(TAG, "CM-getVideoNotificationData", e);
        }
        return null;
    }

    private static List<Carousal> getCarousalList(Bundle extras) {
        String carousalData = extras.getString(CMConstant.PayloadKeys.CAROUSEL_DATA);
        if (TextUtils.isEmpty(carousalData)) {
            List<Carousal> carousalList = extras.getParcelableArrayList(CMConstant.ReceiverExtraData.CAROUSAL_DATA);
            if (carousalList != null)
                return carousalList;
            return null;
        }
        try {
            Type listType = new TypeToken<ArrayList<Carousal>>() {
            }.getType();
            return new Gson().fromJson(carousalData, listType);
        } catch (Exception e) {

            Log.e(TAG, "CM-getCarousalList", e);
        }
        return null;
    }

}
