package com.tokopedia.sellerapp.fcm;

import android.app.Application;
import android.os.Bundle;

import com.tokopedia.core.gcm.base.BaseAppNotificationReceiverUIBackground;
import com.tokopedia.core.gcm.utils.GCMUtils;

import java.util.Map;

/**
 * Created by alvarisi on 1/18/17.
 */

public class AppNotificationReceiverUIBackground extends BaseAppNotificationReceiverUIBackground {
    public AppNotificationReceiverUIBackground(Application application) {
        super(application);
    }

    @Override
    public void handleDedicatedNotification(Bundle data) {
        Map<Integer, Class> dedicatedNotification = getCommonDedicatedNotification();

        Class<?> clazz = dedicatedNotification.get(GCMUtils.getCode(data));
        if (clazz != null) {
            suchADangerousReflectionFunction(data, clazz);
        }
    }

    @Override
    public void notifyReceiverBackgroundMessage(Bundle data) {
        if (isDedicatedNotification(data)) {
            handleDedicatedNotification(data);
        } else {
            handlePromotionNotification(data);
        }
    }

    private void handlePromotionNotification(Bundle data) {
        Map<Integer, Class> dedicatedNotification = getCommonPromoNotification();
        Class<?> clazz = dedicatedNotification.get(GCMUtils.getCode(data));
        if (clazz != null) {
            suchADangerousReflectionFunction(data, clazz);
        }
    }
}
