package com.tokopedia.sellerapp.dashboard.presenter;

import android.text.TextUtils;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.gcm.data.RegisterDeviceInteractor;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.sellerapp.dashboard.view.FCMRegistrationView;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.core.gcm.Constants.REGISTRATION_STATUS_OK;

/**
 * Created by User on 9/8/2017.
 */

public class FCMRegistrationPresenter extends BaseDaggerPresenter<FCMRegistrationView> {
    private RegisterDeviceInteractor registerFCMInteractor;

    @Inject
    public FCMRegistrationPresenter(RegisterDeviceInteractor registerFCMInteractor){
        this.registerFCMInteractor = registerFCMInteractor;
    }

    public void getFCMToken(){
        String token = registerFCMInteractor.getRegistrationToken();
        if (TextUtils.isEmpty(token)) {
            registerFCMInteractor.registerDevice(getSubscriber());
        } else {
            getView().onSuccessFCMRegistration(token);
        }
    }

    private Subscriber<DeviceRegistrationDataResponse> getSubscriber() {
        return new Subscriber<DeviceRegistrationDataResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(DeviceRegistrationDataResponse response) {
                if (response.getStatusCode() != REGISTRATION_STATUS_OK) {
                    getView().onErrorFCMRegistration(response.getStatusMessage());
//                    TrackingUtils.eventError(context.getClass().toString(), response.getStatusMessage());
                }
                String registrationToken = response.getDeviceRegistration();
                registerFCMInteractor.storeRegistrationDevice(registrationToken);
                getView().onSuccessFCMRegistration(registrationToken);
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
    }

}
