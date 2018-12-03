package com.tokopedia.core.gcm.data;

import android.content.Context;

import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author  by alvarisi on 1/5/17.
 */

public class RegisterDeviceInteractor {
    private final PushNotificationRepository mPushNotificationRepository;

    @Inject
    public RegisterDeviceInteractor(Context context) {
        this.mPushNotificationRepository = new PushNotificationDataRepository(context);
    }

    public void registerDevice(Subscriber<DeviceRegistrationDataResponse> subscriber){
        mPushNotificationRepository.deviceRegistration()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    public void storeRegistrationDevice(String registrationDevice){
        mPushNotificationRepository.saveRegistrationDevice(registrationDevice);
    }

}
