package com.tokopedia.digital.cart.presenter;

import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.digital.cart.interactor.IOtpVerificationInteractor;
import com.tokopedia.digital.cart.interactor.OtpVerificationInteractor;
import com.tokopedia.digital.cart.listener.IOtpVerificationView;
import com.tokopedia.digital.cart.model.OtpData;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 3/20/17.
 */

public class OtpVerificationPresenter implements IOtpVerificationPresenter {

    private final IOtpVerificationInteractor otpVerificationInteractor;
    private final IOtpVerificationView view;

    public OtpVerificationPresenter(IOtpVerificationView view,
                                    OtpVerificationInteractor otpVerificationInteractor) {
        this.otpVerificationInteractor = otpVerificationInteractor;
        this.view = view;
    }

    @Override
    public void processRequestOtp() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("otp_type", "16");
        requestParams.putString("mode", "sms");
        otpVerificationInteractor.requestOtp(requestParams.getParameters(),
                new Subscriber<OtpData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(OtpData otpData) {
                        Log.d(TAG, otpData.getMessage());
                        if (otpData.isSuccess()) view.renderSuccessRequestOtp(otpData.getMessage());
                        else view.renderErrorRequestOtp(otpData.getMessage());
                    }
                });
    }

    @Override
    public void processVerifyOtp() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("user", view.getUserId());
        requestParams.putString("code", view.getOtpCode());
        otpVerificationInteractor.verifyOtp(requestParams.getParameters(),
                new Subscriber<OtpData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(OtpData otpData) {
                        Log.d(TAG, otpData.getMessage());
                    }
                });
    }
}
