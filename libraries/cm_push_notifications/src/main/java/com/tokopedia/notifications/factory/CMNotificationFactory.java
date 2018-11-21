package com.tokopedia.notifications.factory;

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

    public static BaseNotification getNotification(Context context, Bundle bundle){
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
        }
        return null;
    }

    private static BaseNotificationModel convertToBaseModel(Bundle data) {
        BaseNotificationModel model = new BaseNotificationModel();
        model.setAppLink(data.getString("appLink", ApplinkConst.HOME));
        model.setNotificationId(Integer.parseInt(data.getString("notiId", "500")));
        model.setBigImageURL(data.getString("bigImage", ""));
        model.setTitle(data.getString("title", ""));
        model.setIcon(data.getString("icon", ""));
        model.setDetailMessage(data.getString("desc", ""));
        model.setMessage(data.getString("message", ""));
        model.setType(data.getString("notiType", ""));
        model.setSoundFileName(data.getString("sound", ""));
        model.setChannelName(data.getString("channel", ""));
        model.setCustomValues(getCustomValues(data));
        model.setActionButton(getActionButtons(data));
        model.setPersistentButtonList(getPersistentNotificationData(data));
        return model;
    }

    private static JSONObject getCustomValues(Bundle extras) {
        String values = extras.getString(CMConstant.NOTIFICATION_CUSTOM_VALUES);
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
        String actions = extras.getString(CMConstant.NOTIFICATION_ACTION_BUTTONS);
        if (TextUtils.isEmpty(actions)) {
            return null;
        }
        try {
            Type listType = new TypeToken<ArrayList<ActionButton>>() {}.getType();
            return new Gson().fromJson(actions, listType);
        } catch (Exception e) {
            Log.e("getActions", e.getMessage());
        }
        return null;
    }

    private static List<PersistentButton> getPersistentNotificationData(Bundle bundle){
        String persistentData = bundle.getString(CMConstant.NOTIFICATION_PERSISTENT);
        if (TextUtils.isEmpty(persistentData)) {
            return null;
        }
        try {
            Type listType = new TypeToken<ArrayList<PersistentButton>>() {}.getType();
            return new Gson().fromJson(persistentData, listType);
        } catch (Exception e) {
            Log.e("getActions", e.getMessage());
        }
        return null;
    }
}
