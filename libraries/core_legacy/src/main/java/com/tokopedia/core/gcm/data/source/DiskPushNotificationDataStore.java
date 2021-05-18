package com.tokopedia.core.gcm.data.source;

import android.content.Context;

import com.tokopedia.core.database.CoreLegacyDbFlowDatabase;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.data.PushNotificationDataStore;
import com.tokopedia.core.gcm.data.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.database.PushNotificationDao;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;
import com.tokopedia.core.util.PasswordGenerator;

import rx.Observable;

import static com.tokopedia.core.gcm.Constants.REGISTRATION_MESSAGE_ERROR;
import static com.tokopedia.core.gcm.Constants.REGISTRATION_STATUS_ERROR;

/**
 * @author by alvarisi on 1/5/17.
 */

public class DiskPushNotificationDataStore implements PushNotificationDataStore {
    private final Context mContext;
    private final PushNotificationDao pushNotificationDao;

    public DiskPushNotificationDataStore(Context context) {
        mContext = context;
        pushNotificationDao = CoreLegacyDbFlowDatabase.getInstance(context).pushNotificationDao();
    }

    @Override
    public Observable<FCMTokenUpdateEntity> updateTokenServer(FCMTokenUpdate data) {
        return null;
    }

    public Observable<DeviceRegistrationDataResponse> deviceRegistration() {
        return Observable.just(true).map(aBoolean -> {
            DeviceRegistrationDataResponse response = new DeviceRegistrationDataResponse();
            response.setStatusCode(REGISTRATION_STATUS_ERROR);
            response.setDeviceRegistration(PasswordGenerator.getAppId(mContext));
            response.setStatusMessage(REGISTRATION_MESSAGE_ERROR);
            return response;
        });
    }

    public Observable<Boolean> saveRegistrationDevice(String registrationDevice) {
        return Observable.just(registrationDevice).map(req -> {
            FCMCacheManager.storeRegId(req, mContext);
            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteSavedPushNotification() {
        return Observable.just(true).map(aBoolean -> {
            pushNotificationDao.drop();
            return true;
        }).onErrorReturn(throwable -> false);
    }
}
