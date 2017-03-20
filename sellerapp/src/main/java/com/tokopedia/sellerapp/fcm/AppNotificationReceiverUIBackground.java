package com.tokopedia.sellerapp.fcm;

import android.app.Application;
import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.gcm.Visitable;
import com.tokopedia.core.gcm.base.BaseAppNotificationReceiverUIBackground;
import com.tokopedia.core.gcm.utils.GCMUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.sellerapp.fcm.notification.TopAdsBelow20kNotification;
import com.tokopedia.sellerapp.fcm.notification.TopAdsTopupSuccessNotification;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * Created by alvarisi on 1/18/17.
 */

public class AppNotificationReceiverUIBackground extends BaseAppNotificationReceiverUIBackground {
    public AppNotificationReceiverUIBackground(Application application) {
        super(application);
    }

    public void prepareAndExecuteDedicatedNotification(Bundle data) {
        Map<Integer, Visitable> dedicatedNotification = getCommonDedicatedNotification();
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TOPADS_BELOW_20K, new TopAdsBelow20kNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TOPADS_TOPUP_SUCCESS, new TopAdsTopupSuccessNotification(mContext));
        Visitable visitable = dedicatedNotification.get(GCMUtils.getCode(data));
        if (visitable != null){
            visitable.proccessReceivedNotification(data);
        }
    }

    @Override
    public void notifyReceiverBackgroundMessage(Observable<Bundle> data) {
        data.map(new Func1<Bundle, Boolean>() {
            @Override
            public Boolean call(Bundle bundle) {
                if (isDedicatedNotification(bundle)) {
                    handleDedicatedNotification(bundle);
                } else {
                    handlePromotionNotification(bundle);
                }
                return true;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Actions.empty(), new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

    public void handleDedicatedNotification(Bundle data) {
        if (SessionHandler.isV4Login(mContext)
                && SessionHandler.getLoginID(mContext).equals(data.getString("to_user_id"))) {

            resetNotificationStatus(data);
            CommonUtils.dumper("resetNotificationStatus");

            if (mActivitiesLifecycleCallbacks.isAppOnBackground()) {
                prepareAndExecuteDedicatedNotification(data);
            } else {
                NotificationReceivedListener listener =
                        (NotificationReceivedListener) mActivitiesLifecycleCallbacks.getLiveActivityOrNull();
                if (listener != null) {
                    listener.onGetNotif();
                    if (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))
                            == TkpdState.GCMServiceState.GCM_CART_UPDATE) {
                        listener.onRefreshCart(data.getInt("is_cart_exists", 0));
                    } else {
                        prepareAndExecuteDedicatedNotification(data);
                    }
                } else {
                    prepareAndExecuteDedicatedNotification(data);
                }
            }
            mFCMCacheManager.resetCache(data);
        }
    }

    @Override
    public void handlePromotionNotification(Bundle data) {
        Map<Integer, Visitable> promoNotifications = getCommonPromoNotification();
        Visitable visitable = promoNotifications.get(GCMUtils.getCode(data));
        if (visitable != null) {
            visitable.proccessReceivedNotification(data);
        }
    }
}
