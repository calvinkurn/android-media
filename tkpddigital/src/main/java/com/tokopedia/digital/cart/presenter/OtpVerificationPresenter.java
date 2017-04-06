package com.tokopedia.digital.cart.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.digital.cart.interactor.IOtpVerificationInteractor;
import com.tokopedia.digital.cart.interactor.OtpVerificationInteractor;
import com.tokopedia.digital.cart.listener.IOtpVerificationView;
import com.tokopedia.digital.cart.model.OtpData;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
    public void processFirstRequestSmsOtp() {
        view.showInitialProgressLoading();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("otp_type", "16");
        requestParams.putString("mode", "sms");
        otpVerificationInteractor.requestOtp(requestParams.getParameters(),
                getSubscriberFirstRequestSmsOtp());
    }


    @Override
    public void processReRequestSmsOtp() {
        view.showProgressLoading();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("otp_type", "16");
        requestParams.putString("mode", "sms");
        otpVerificationInteractor.requestOtp(requestParams.getParameters(),
                getSubscriberReRequestSmsOtp());
    }

    @Override
    public void processRequestCallOtp() {
        view.showProgressLoading();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("otp_type", "16");
        requestParams.putString("mode", "call");
        otpVerificationInteractor.requestOtp(requestParams.getParameters(),
                getSubscriberRequestCallOtp());
    }

    @Override
    public void processVerifyOtp() {
        view.showProgressLoading();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("user", view.getUserId());
        requestParams.putString("code", view.getOtpCode());
        otpVerificationInteractor.verifyOtp(requestParams.getParameters(),
                getSubscriberVerifyOtp());
    }


    @NonNull
    private Subscriber<OtpData> getSubscriberReRequestSmsOtp() {
        return new Subscriber<OtpData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof SocketTimeoutException) {
                    view.renderErrorTimeoutReRequestSmsOtp(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    view.renderErrorNoConnectionReRequestSmsOtp(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else {
                    view.renderErrorReRequestSmsOtp(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(OtpData otpData) {
                view.hideProgressLoading();
                Log.d(TAG, otpData.getMessage());
                if (otpData.isSuccess()) view.renderSuccessReRequestSmsOtp(otpData.getMessage());
                else view.renderErrorResponseReRequestSmsOtp(otpData.getMessage());
            }
        };
    }

    private Subscriber<OtpData> getSubscriberFirstRequestSmsOtp() {
        return new Subscriber<OtpData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.clearContentRendered();
                view.hideProgressLoading();
                if (e instanceof SocketTimeoutException) {
                    view.renderErrorTimeoutFirstRequestSmsOtp(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    view.renderErrorNoConnectionFirstRequestSmsOtp(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else {
                    view.renderErrorFirstRequestSmsOtp(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(OtpData otpData) {
                view.hideProgressLoading();
                if (otpData.isSuccess()) view.renderSuccessFirstRequestSmsOtp(otpData.getMessage());
                else view.renderErrorResponseFirstRequestSmsOtp(otpData.getMessage());
            }
        };
    }


    @NonNull
    private Subscriber<OtpData> getSubscriberRequestCallOtp() {
        return new Subscriber<OtpData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof SocketTimeoutException) {
                    view.renderErrorTimeoutRequestCallOtp(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    view.renderErrorNoConnectionRequestCallOtp(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else {
                    view.renderErrorRequestCallOtp(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(OtpData otpData) {
                view.hideProgressLoading();
                Log.d(TAG, otpData.getMessage());
                if (otpData.isSuccess()) view.renderSuccessRequestCallOtp(otpData.getMessage());
                else view.renderErrorResponseRequestCallOtp(otpData.getMessage());
            }
        };
    }

    @NonNull
    private Subscriber<OtpData> getSubscriberVerifyOtp() {
        return new Subscriber<OtpData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                e.printStackTrace();
                if (e instanceof SocketTimeoutException) {
                    view.renderErrorTimeoutVerifyOtp(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    view.renderErrorNoConnectionVerifyOtp(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else {
                    view.renderErrorVerifyOtp(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(OtpData otpData) {
                view.hideProgressLoading();
                Log.d(TAG, otpData.getMessage());
                if (otpData.isSuccess()) view.renderSuccessVerifyOtp(otpData.getMessage());
                else view.renderErrorResponseVerifyOtp(otpData.getMessage());
            }
        };
    }
}
