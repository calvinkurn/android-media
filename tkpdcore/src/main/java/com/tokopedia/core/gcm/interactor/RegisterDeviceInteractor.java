package com.tokopedia.core.gcm.interactor;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author  by alvarisi on 1/5/17.
 */

public class RegisterDeviceInteractor {
    private final PushNotificationRepository mPushNotificationRepository;

    public RegisterDeviceInteractor() {
        this.mPushNotificationRepository = new PushNotificationDataRepository();
    }

    public void registerDevice(Subscriber<String> subscriber){
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
