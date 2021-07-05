package com.tokopedia.sellerapp.fcm;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.gcm.Visitable;
import com.tokopedia.core.gcm.base.BaseAppNotificationReceiverUIBackground;
import com.tokopedia.core.gcm.notification.applink.ApplinkPushNotificationBuildAndShow;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.fcm.notification.TopAdsBelow20kNotification;
import com.tokopedia.sellerapp.fcm.notification.TopAdsTopupSuccessNotification;
import com.tokopedia.topchat.chatlist.view.ChatNotifInterface;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * Created by alvarisi on 1/18/17.
 */

public class AppNotificationReceiverUIBackground extends BaseAppNotificationReceiverUIBackground {
    public static final String DEFAULT_NOTIF_CODE_VALUE = "0";
    private static final int DEFAULT_CART_VALUE = 0;
    private RemoteConfig remoteConfig;
    private AppNotificationReceiver receiver;

    public AppNotificationReceiverUIBackground(Application application) {
        super(application);
        remoteConfig = new FirebaseRemoteConfigImpl(application);
        //Hack reflection error don't remove it if reflection is already delete
        receiver = new AppNotificationReceiver();
    }

    public void prepareAndExecuteDedicatedNotification(Bundle data) {
        if (!isRefreshCart(data)) {
            Map<Integer, Visitable> dedicatedNotification = getCommonDedicatedNotification();
            dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TOPADS_BELOW_20K, new TopAdsBelow20kNotification(mContext));
            dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TOPADS_TOPUP_SUCCESS, new TopAdsTopupSuccessNotification(mContext));
            Visitable visitable = dedicatedNotification.get(getCode(data));
            if (visitable != null) {
                visitable.proccessReceivedNotification(data);
            }
        }
    }

    @Override
    public void notifyReceiverBackgroundMessage(Bundle data) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "AppNotificationReceiverUIBackground_Seller");
        messageMap.put("isSupported", String.valueOf(isSupportedApplinkNotification(data)));
        messageMap.put("isDedicated", String.valueOf(isDedicatedNotification(data)));
        ServerLogger.log(Priority.P2, "PUSH_NOTIF_UNUSED", messageMap);
        handlingNotification(data);
    }

    private void handlingNotification(Bundle data) {
        if(revertOldHandling()) {
            if (isSupportedApplinkNotification(data)) {
                handleApplinkNotification(data);
            } else if (isDedicatedNotification(data)) {
                handleDedicatedNotification(data);
            } else {
                handlePromotionNotification(data);
            }
        }
    }

    private boolean revertOldHandling() {
        return false;
    }

    private void handleApplinkNotification(Bundle data) {
        if (data.getString(Constants.ARG_NOTIFICATION_APPLINK_LOGIN_REQUIRED, "false").equals("true")) {
            UserSessionInterface userSession = new UserSession(mContext);
            if (userSession.isLoggedIn()
                    && userSession.getUserId().equals(data.getString(Constants.ARG_NOTIFICATION_TARGET_USER_ID))) {

                resetNotificationStatus(data);
                prepareAndExecuteApplinkNotification(data);
                refreshUI(data);
                mFCMCacheManager.resetCache(data);
            }
        } else {
            prepareAndExecuteApplinkNotification(data);
        }
    }

    private boolean isSupportedApplinkNotification(Bundle bundle) {
        String applink = bundle.getString(Constants.ARG_NOTIFICATION_APPLINK, "");
        DeepLinkDelegate deepLinkDelegate = DeepLinkHandlerActivity.getDelegateInstance();
        return deepLinkDelegate.supportsUri(applink);
    }


    private void prepareAndExecuteApplinkNotification(Bundle data) {
        if (!isRefreshCart(data)) {
            String applinks = data.getString(Constants.ARG_NOTIFICATION_APPLINK);
            String category = Uri.parse(applinks).getHost();
            switch (category) {
                case Constants.ARG_NOTIFICATION_APPLINK_TOPCHAT:
                    if (mActivitiesLifecycleCallbacks.getLiveActivityOrNull() != null
                            && mActivitiesLifecycleCallbacks.getLiveActivityOrNull() instanceof ChatNotifInterface) {
                        ((ChatNotifInterface) mActivitiesLifecycleCallbacks.getLiveActivityOrNull()).onGetNotif(data);
                    } else {
                        String applink = data.getString(Constants.ARG_NOTIFICATION_APPLINK);
                        String fullname = data.getString("full_name");
                        applink = String.format("%s?fullname=%s", applink, fullname);
                        data.putString(Constants.ARG_NOTIFICATION_APPLINK, applink);
                        buildNotifByData(data);
                    }
                    break;
                case Constants.ARG_NOTIFICATION_APPLINK_SELLER_INFO:
                    UserSessionInterface userSession = new UserSession(mContext);
                    if (userSession.hasShop()) {
                        buildNotifByData(data);
                    }
                    break;
                default:
                    buildNotifByData(data);
                    break;
            }
        }
    }

    private void buildNotifByData(Bundle data) {
        ApplinkPushNotificationBuildAndShow buildAndShow = new ApplinkPushNotificationBuildAndShow(data);
        Intent intent = new Intent(mContext, DeepLinkHandlerActivity.class);
        buildAndShow.process(mContext, intent);
    }

    public void handleDedicatedNotification(Bundle data) {
        UserSessionInterface userSession = new UserSession(mContext);
        if (userSession.isLoggedIn()
                && userSession.getUserId().equals(data.getString("to_user_id"))) {

            resetNotificationStatus(data);
            Timber.d("resetNotificationStatus");
            prepareAndExecuteDedicatedNotification(data);
            refreshUI(data);
            mFCMCacheManager.resetCache(data);
        }
    }

    private void refreshUI(Bundle data) {
        if (!mActivitiesLifecycleCallbacks.isAppOnBackground()) {
            Activity currentActivity = mActivitiesLifecycleCallbacks.getLiveActivityOrNull();
            if (currentActivity != null && currentActivity instanceof NotificationReceivedListener) {
                NotificationReceivedListener listener = (NotificationReceivedListener) currentActivity;
                listener.onGetNotif();
                if (isRefreshCart(data)) {
                    listener.onRefreshCart(data.getInt(Constants.ARG_NOTIFICATION_CART_EXISTS, DEFAULT_CART_VALUE));
                }
            }
        }
    }

    private boolean isRefreshCart(Bundle data) {
        return Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE, DEFAULT_NOTIF_CODE_VALUE))
                == TkpdState.GCMServiceState.GCM_CART_UPDATE;
    }

    @Override
    public void handlePromotionNotification(Bundle data) {
        Map<Integer, Visitable> promoNotifications = getCommonPromoNotification();
        Visitable visitable = promoNotifications.get(getCode(data));
        if (visitable != null) {
            visitable.proccessReceivedNotification(data);
        }
    }
}
