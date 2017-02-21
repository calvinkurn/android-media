package com.tokopedia.core.gcm.interactor.source;

import android.content.Context;

import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.core.util.PasswordGenerator;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.core.gcm.Constants.REGISTRATION_MESSAGE_ERROR;
import static com.tokopedia.core.gcm.Constants.REGISTRATION_STATUS_ERROR;

/**
 * @author by alvarisi on 1/5/17.
 */

public class DiskPushNotificationDataStore {
    private final Context mContext;

    public DiskPushNotificationDataStore(Context context) {
        mContext = context;
    }

    public Observable<DeviceRegistrationDataResponse> deviceRegistration() {
        return Observable.just(true).map(new Func1<Boolean, DeviceRegistrationDataResponse>() {
            @Override
            public DeviceRegistrationDataResponse call(Boolean aBoolean) {
                DeviceRegistrationDataResponse response = new DeviceRegistrationDataResponse();
                response.setStatusCode(REGISTRATION_STATUS_ERROR);
                response.setDeviceRegistration(PasswordGenerator.getAppId(mContext));
                response.setStatusMessage(REGISTRATION_MESSAGE_ERROR);
                return response;
            }
        });
    }

    public Observable<Boolean> saveRegistrationDevice(String registrationDevice) {
        return Observable.just(registrationDevice).map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String req) {
                FCMCacheManager.storeRegId(req, mContext);
                return true;
            }
        });
    }
}
