package com.tokopedia.sellerapp.fcm;

import android.app.Application;
import android.os.Bundle;

import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.gcm.base.BaseAppNotificationReceiverUIBackground;
import com.tokopedia.core.gcm.utils.GCMUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

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
        Map<Integer, Class> dedicatedNotification = getCommonDedicatedNotification();

        Class<?> clazz = dedicatedNotification.get(GCMUtils.getCode(data));
        if (clazz != null) {
            executeNotification(data, clazz);
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
        Map<Integer, Class> dedicatedNotification = getCommonPromoNotification();
        Class<?> clazz = dedicatedNotification.get(GCMUtils.getCode(data));
        if (clazz != null) {
            executeNotification(data, clazz);
        }
    }
}
