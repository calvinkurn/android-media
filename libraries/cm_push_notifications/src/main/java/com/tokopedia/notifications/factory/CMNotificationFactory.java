package com.tokopedia.notifications.factory;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.model.ActionButton;
import com.tokopedia.notifications.model.BaseNotificationModel;
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
        switch (baseNotificationModel.getType()) {
            case CMConstant.NotificationType.GENERAL:
                return (new GeneralNotification(context.getApplicationContext(), baseNotificationModel));
            case CMConstant.NotificationType.ACTION_BUTTONS:
                return (new ActionNotification(context.getApplicationContext(), baseNotificationModel));
            case CMConstant.NotificationType.BIG_IMAGE:
                return (new ImageNotification(context.getApplicationContext(), baseNotificationModel));
            case CMConstant.NotificationType.PERSISTENT:
                return (new PersistentNotification(context.getApplicationContext(), baseNotificationModel));
            case CMConstant.NotificationType.DELETE_NOTIFICATION:
                cancelNotification(context, baseNotificationModel.getNotificationId());
                return null;
        }
        return null;
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
        model.setCustomValues(getCustomValues(data));
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
            Log.e("getCustomValues", e.getMessage());
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
            Log.e("getActions", e.getMessage());
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
            Log.e("getActions", e.getMessage());
        }
        return null;
    }
}
