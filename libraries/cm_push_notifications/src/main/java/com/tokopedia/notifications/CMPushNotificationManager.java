package com.tokopedia.notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.notifications.common.CMConstant;

import java.util.Map;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public class CMPushNotificationManager {

    private final String TAG = CMPushNotificationManager.class.getCanonicalName();
    private static final CMPushNotificationManager sInstance;
    private Context mContext;

    public static CMPushNotificationManager getInstance() {
        return sInstance;
    }

    static {
        sInstance = new CMPushNotificationManager();
    }

    /**
     * initialization of push notification library
     *
     * @param context
     */
    public void init(@NonNull Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context can not be null");
        }
        this.mContext = context.getApplicationContext();
    }

    public Context getApplicationContext() {
        return mContext;
    }

    /**
     * Send FCM token to server
     *
     * @param token
     */
    public void setFcmTokenCMNotif(String token) {
        Log.i(TAG, "token: " + token);
        if (mContext == null) {
            throw new IllegalArgumentException("Kindly invoke init before calling notification library");
        }
        if (TextUtils.isEmpty(token)) {
            return;
        }
        (new CMUserHandler(mContext)).sendFcmTokenToServer(token);
    }


    /**
     * To check weather the incoming notification belong to campaign management
     *
     * @param extras
     * @return
     */
    public boolean isFromCMNotificationPlatform(Map<String, String> extras) {
        if (mContext == null) {
            throw new IllegalArgumentException("Kindly invoke init before calling notification library");
        }
        try {
            if (null == extras) {
                Log.e(TAG, "CMPushNotificationManager: No Intent extra available");
            } else if (extras.containsKey(CMConstant.FCM_EXTRA_CONFIRMATION_KEY)) {
                String confirmationValue =
                        extras.get(CMConstant.FCM_EXTRA_CONFIRMATION_KEY);
                return confirmationValue.equals(CMConstant.FCM_EXTRA_CONFIRMATION_VALUE);
            }
        } catch (Exception e) {
            Log.e(TAG, "CMPushNotificationManager: isFromMoEngagePlatform ", e);
        }
        return false;
    }

    /**
     * Handle the remote message and show push notification
     *
     * @param remoteMessage
     */
    public void handlePushPayload(RemoteMessage remoteMessage) {
        if (mContext == null) {
            throw new IllegalArgumentException("Kindly invoke init before calling notification library");
        }
        try {
            if (isFromCMNotificationPlatform(remoteMessage.getData())) {
                Bundle bundle = convertMapToBundle(remoteMessage.getData());
                CMJobIntentService.enqueueWork(mContext, bundle);
            }
        } catch (Exception e) {
            Log.e(TAG, "CMPushNotificationManager: handlePushPayload ", e);
        }
    }

    private Bundle convertMapToBundle(Map<String, String> map) {
        Bundle bundle = new Bundle(map != null ? map.size() : 0);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
        }
        return bundle;
    }

}

/* private void notifyGeneral(BaseNotificationModel baseNotificationModel,
                               int notificationId, NotificationManagerCompat notificationManagerCompat) {
        Notification generalNotif = new GeneralNotificationFactory(mContext)
                .createNotification(baseNotificationModel, notificationId);

        notificationManagerCompat.notify(notificationId, generalNotif);

    }*/

/*
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
            List<ActionButton> actionButtonList = new Gson().fromJson(actions, listType);
            return actionButtonList;
        } catch (Exception e) {
            Log.e("getActions", e.getMessage());
        }
        return null;
    }

*/

/* private BaseNotificationModel convertToBaseModel(Bundle data) {
        BaseNotificationModel model = new BaseNotificationModel();
        model.setApplink(data.getString("applinks", ApplinkConst.HOME));
        model.setTitle(data.getString("title", ""));
        model.setDesc(data.getString("desc", ""));
        model.setMessage(data.getString("message", ""));
        model.setType(data.getString("type", ""));
        model.setCustomValues(getCustomValues(data));
        model.setActionButton(getActionButtons(data));
        return model;
    }*/


    /*private void generateNotification(Bundle bundle, NotificationManagerCompat notificationManagerCompat) {
        String notificationType = getNotificationType(bundle);
        BaseNotificationModel model = convertToBaseModel(bundle);

        if (CMConstant.NotificationType.GENERAL.equals(notificationType)) {
            notifyGeneral(model, CMConstant.NotificationId.GENERAL, notificationManagerCompat);
        } else if (CMConstant.NotificationType.BIG_IMAGE.equals(notificationType)) {

        }
    }*/

    /* private String getNotificationType(Bundle extras) {
        try {
            if (null == extras) {
                Log.e(LOG, "CMPushNotificationManager: No Intent extra available");
            } else if (extras.containsKey(CMConstant.EXTRA_NOTIFICATION_TYPE)) {
                return extras.getString(CMConstant.EXTRA_NOTIFICATION_TYPE);
            }
        } catch (Exception e) {
            Log.e(LOG, "CMPushNotificationManager: ", e);
        }
        return CMConstant.EXTRA_NOTIFICATION_TYPE;
    }
*/