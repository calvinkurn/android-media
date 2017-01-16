package com.tokopedia.core.gcm.interactor;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.interactor.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.interactor.source.CloudPushNotificationDataSource;
import com.tokopedia.core.gcm.interactor.source.DiskPushNotificationDataStore;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.core.gcm.Constants.REGISTRATION_MESSAGE_ERROR;
import static com.tokopedia.core.gcm.Constants.REGISTRATION_MESSAGE_OK;
import static com.tokopedia.core.gcm.Constants.REGISTRATION_STATUS_ERROR;
import static com.tokopedia.core.gcm.Constants.REGISTRATION_STATUS_OK;

/**
 * @author  by alvarisi on 1/5/17.
 */

public class PushNotificationDataRepository implements PushNotificationRepository {

    private final CloudPushNotificationDataSource mCloudPushNotificationDataSource;
    private final DiskPushNotificationDataStore mDiskPushNotificationDataStore;

    public PushNotificationDataRepository() {
        mCloudPushNotificationDataSource = new CloudPushNotificationDataSource();
        mDiskPushNotificationDataStore = new DiskPushNotificationDataStore(MainApplication.getAppContext());
    }

    @Override
    public Observable<FCMTokenUpdateEntity> updateTokenServer(FCMTokenUpdate data) {
        return mCloudPushNotificationDataSource.updateTokenServer(data);
    }

    @Override
    public Observable<DeviceRegistrationDataResponse> deviceRegistration() {
        return mCloudPushNotificationDataSource.deviceRegistration()
                .flatMap(new Func1<String, Observable<DeviceRegistrationDataResponse>>() {
                    @Override
                    public Observable<DeviceRegistrationDataResponse> call(String cloudRegitrationID) {
                        DeviceRegistrationDataResponse response = new DeviceRegistrationDataResponse();
                        if (!TextUtils.isEmpty(cloudRegitrationID)){
                            response.setStatusCode(REGISTRATION_STATUS_OK);
                            response.setDeviceRegistration(cloudRegitrationID);
                            response.setStatusMessage(REGISTRATION_MESSAGE_OK);
                            return Observable.just(response);
                        }else {
                            response.setStatusCode(REGISTRATION_STATUS_ERROR);
                            response.setStatusMessage(REGISTRATION_MESSAGE_ERROR);
                            return mDiskPushNotificationDataStore.deviceRegistration();
                        }
                    }
                });
    }

    @Override
    public Observable<Boolean> saveRegistrationDevice(String registration) {
        return mDiskPushNotificationDataStore.saveRegistrationDevice(registration);
    }
}
