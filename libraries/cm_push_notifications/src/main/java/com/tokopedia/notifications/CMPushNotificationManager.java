package com.tokopedia.notifications;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMNotificationCacheHandler;
import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.notifications.domain.UpdateFcmTokenUseCase;
import com.tokopedia.notifications.factory.GeneralNotificationFactory;
import com.tokopedia.notifications.model.ActionButton;
import com.tokopedia.notifications.model.BaseNotificationModel;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public class CMPushNotificationManager {

    private final String LOG = CMPushNotificationManager.class.getCanonicalName();
    private UpdateFcmTokenUseCase updateFcmTokenUseCase;
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

    /**
     * Send FCM token to server
     *
     * @param token
     */

    public void setFcmTokenCMNotif(String token) {
        Log.e("error", "token: " + token);

        if (mContext == null) {
            throw new IllegalArgumentException("Kindly invoke init before calling notification library");
        }
        if (TextUtils.isEmpty(token)) {
            return;
        }
        if (CMNotificationUtils.tokenUpdateRequired(mContext, token) ||
                CMNotificationUtils.mapTokenWithUserRequired(mContext, getUserId()) ||
                CMNotificationUtils.mapTokenWithGAdsIdRequired(mContext, getGoogleAdId())) {
            sendFcmTokenToServer(token);
        }
    }


    /**
     * To check weather the incoming notification belong to campaign manangment
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
                Log.e(LOG, "CMPushNotificationManager: No Intent extra available");
            } else if (extras.containsKey(CMConstant.FCM_EXTRA_CONFIRMATION_KEY)) {
                String confirmationValue =
                        extras.get(CMConstant.FCM_EXTRA_CONFIRMATION_KEY);
                return confirmationValue.equals(CMConstant.FCM_EXTRA_CONFIRMATION_VALUE);
            }
        } catch (Exception e) {
            Log.e(LOG, "CMPushNotificationManager: isFromMoEngagePlatform ", e);
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
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
                generateNotification(bundle, notificationManagerCompat);
            }
        } catch (Exception e) {
            Log.e(LOG, "CMPushNotificationManager: handlePushPayload ", e);
        }
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

    private void notifyGeneral(BaseNotificationModel baseNotificationModel,
                               int notificationId, NotificationManagerCompat notificationManagerCompat) {
        Notification generalNotif = new GeneralNotificationFactory(mContext)
                .createNotification(baseNotificationModel, notificationId);

        notificationManagerCompat.notify(notificationId, generalNotif);

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

    private void generateNotification(Bundle bundle, NotificationManagerCompat notificationManagerCompat) {
        String notificationType = getNotificationType(bundle);
        BaseNotificationModel model = convertToBaseModel(bundle);

        if (CMConstant.NotificationType.GENERAL.equals(notificationType)) {
            notifyGeneral(model, CMConstant.NotificationId.GENERAL, notificationManagerCompat);
        } else if (CMConstant.NotificationType.BIG_IMAGE.equals(notificationType)) {

        }
    }

    private String getNotificationType(Bundle extras) {
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

    private void sendFcmTokenToServer(String token) {
        String userId = getUserId();
        String accessToken = ((CMRouter) mContext).getAccessToken();
        String gAdId = getGoogleAdId();
        updateFcmTokenUseCase = new UpdateFcmTokenUseCase();
        updateFcmTokenUseCase.createRequestParams(userId, token, CMNotificationUtils.getSdkVersion(), CMNotificationUtils.getUniqueAppId(mContext),
                CMNotificationUtils.getCurrentAppVersion(mContext));

        updateFcmTokenUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                Log.e(LOG, "onCompleted: sendFcmTokenToServer ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(LOG, "CMPushNotificationManager: sendFcmTokenToServer " + e.getMessage());
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                RestResponse res1 = typeRestResponseMap.get(String.class);
                Log.e("code", "" + res1.getCode());
                Log.e("data", "" + res1.getData());
                Log.e("error", "" + res1.getErrorBody());

                if (true) {
                    CMNotificationUtils.saveToken(mContext, token);
                    CMNotificationUtils.saveUserId(mContext, userId);
                    CMNotificationUtils.saveGAdsIdId(mContext, gAdId);
                }
            }
        });
    }


    private String getGoogleAdId() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(mContext, TkpdCache.ADVERTISINGID);
        String adsId = localCacheHandler.getString(TkpdCache.Key.KEY_ADVERTISINGID);
        if (adsId != null && !TextUtils.isEmpty(adsId.trim())) {
            return adsId;
        } else {
            AdvertisingIdClient.Info adInfo;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
            } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                e.printStackTrace();
                return "";
            }

            if (adInfo != null) {
                String adID = adInfo.getId();

                if (!TextUtils.isEmpty(adID)) {
                    localCacheHandler.putString(TkpdCache.Key.KEY_ADVERTISINGID, adID);
                    localCacheHandler.applyEditor();
                }
                return adID;
            }
        }
        return "";
    }

    private String getUserId() {
        return ((CMRouter) mContext).getUserId();
    }

}
