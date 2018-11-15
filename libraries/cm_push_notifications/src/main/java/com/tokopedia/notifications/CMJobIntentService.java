package com.tokopedia.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.factory.ActionNotificationFactory;
import com.tokopedia.notifications.factory.GeneralNotificationFactory;
import com.tokopedia.notifications.factory.ImageNotificationFactory;
import com.tokopedia.notifications.model.ActionButton;
import com.tokopedia.notifications.model.BaseNotificationModel;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lalit.singh
 */
public class CMJobIntentService extends JobIntentService {

    private static int JOB_ID = 131;

    static String TAG = CMJobIntentService.class.getSimpleName();

    public static void enqueueWork(Context context, Bundle bundle) {
        Intent work = new Intent();
        work.putExtra(CMConstant.EXTRA_NOTIFICATION_BUNDLE, bundle);
        enqueueWork(context, CMJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Bundle bundle = intent.getBundleExtra(CMConstant.EXTRA_NOTIFICATION_BUNDLE);
        if (null != bundle) {
            String notificationType = getNotificationType(bundle);
            BaseNotificationModel baseNotificationModel = convertToBaseModel(bundle);
            Notification notification;
            switch (notificationType) {
                case CMConstant.NotificationType.GENERAL:
                    notification = (new GeneralNotificationFactory(this.getApplicationContext()))
                            .createNotification(baseNotificationModel, CMConstant.NotificationId.GENERAL);
                    postNotification(notification, CMConstant.NotificationId.GENERAL);
                    break;

                case CMConstant.NotificationType.ACTION_BUTTONS:
                    notification = (new ActionNotificationFactory(this.getApplicationContext()))
                            .createNotification(baseNotificationModel, CMConstant.NotificationId.ACTION_BUTTONS);
                    postNotification(notification, CMConstant.NotificationId.ACTION_BUTTONS);
                    break;

                case CMConstant.NotificationType.BIG_IMAGE:
                    notification = (new ImageNotificationFactory(this.getApplicationContext()))
                            .createNotification(baseNotificationModel, CMConstant.NotificationId.BIG_IMAGE);
                    postNotification(notification, CMConstant.NotificationId.BIG_IMAGE);
                    break;
                case CMConstant.NotificationType.PERSISTENT:
                    break;
            }
        }
    }

    private void postNotification(Notification notification, int notificationId) {
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (null != notificationManager)
            notificationManager.notify(notificationId, notification);
    }

    private String getNotificationType(Bundle extras) {
        try {
            if (null == extras) {
                Log.e(TAG, "CMPushNotificationManager: No Intent extra available");
            } else if (extras.containsKey(CMConstant.EXTRA_NOTIFICATION_TYPE)) {
                return extras.getString(CMConstant.EXTRA_NOTIFICATION_TYPE);
            }
        } catch (Exception e) {
            Log.e(TAG, "CMPushNotificationManager: ", e);
        }
        return CMConstant.EXTRA_NOTIFICATION_TYPE;
    }

    private BaseNotificationModel convertToBaseModel(Bundle data) {
        BaseNotificationModel model = new BaseNotificationModel();
        model.setApplink(data.getString("applinks", ApplinkConst.HOME));
        model.setTitle(data.getString("title", ""));
        model.setDesc(data.getString("desc", ""));
        model.setMessage(data.getString("message", ""));
        model.setType(data.getString("type", ""));
        model.setCustomValues(getCustomValues(data));
        model.setActionButton(getActionButtons(data));
        return model;
    }

    private JSONObject getCustomValues(Bundle extras) {
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

    private List<ActionButton> getActionButtons(Bundle extras) {
        String actions = extras.getString(CMConstant.NOTIFICATION_ACTION_BUTTONS);
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

}
