package com.tokopedia.core.gcm;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.core.deprecated.LocalCacheHandler;
import com.tokopedia.user.session.UserSession;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author by Herdi_WORK on 13.12.16.
 */

public class FCMCacheManager {

    public static void storeRegId(String id, Context context) {
        new UserSession(context).setDeviceId(id);
    }

    public static void storeFcmTimestamp(Context context) {
        UserSession userSession = new UserSession(context);
        userSession.setFcmTimestamp();
    }

    public static String getRegistrationId(Context context) {
        return new UserSession(context).getDeviceId();
    }

    public static String getRegistrationIdWithTemp(Context context) {
        UserSession userSession = new UserSession(context);
        String deviceId = userSession.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            String tempID = getTempFcmId();
            userSession.setDeviceId(tempID);
            return tempID;
        }
        return deviceId;
    }

    private static String getTempFcmId() {
        return UUID.randomUUID().toString();
    }
}