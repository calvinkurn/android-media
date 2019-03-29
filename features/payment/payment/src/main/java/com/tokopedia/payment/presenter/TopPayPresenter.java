package com.tokopedia.payment.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint;
import com.tokopedia.payment.fingerprint.domain.GetPostDataOtpUseCase;
import com.tokopedia.payment.fingerprint.domain.PaymentFingerprintUseCase;
import com.tokopedia.payment.fingerprint.domain.SaveFingerPrintUseCase;
import com.tokopedia.payment.fingerprint.domain.SavePublicKeyUseCase;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.payment.utils.ErrorNetMessage;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by kris on 3/14/17. Tokopedia
 */

public class TopPayPresenter extends BaseDaggerPresenter<TopPayContract.View> implements TopPayContract.Presenter {

    private SaveFingerPrintUseCase saveFingerPrintUseCase;
    private PaymentFingerprintUseCase paymentFingerprintUseCase;
    private GetPostDataOtpUseCase getPostDataOtpUseCase;
    private UserSessionInterface userSession;

    public TopPayPresenter(SaveFingerPrintUseCase saveFingerPrintUseCase,
                           SavePublicKeyUseCase savePublicKeyUseCase,
                           PaymentFingerprintUseCase paymentFingerprintUseCase,
                           GetPostDataOtpUseCase getPostDataOtpUseCase, UserSessionInterface userSession) {
        this.saveFingerPrintUseCase = saveFingerPrintUseCase;
        this.paymentFingerprintUseCase = paymentFingerprintUseCase;
        this.getPostDataOtpUseCase = getPostDataOtpUseCase;
        this.userSession = userSession;
    }

    @Override
    public void proccessUriPayment() {
        PaymentPassData paymentPassData = getView().getPaymentPassData();
        try {
            byte[] postData = paymentPassData.getQueryString().getBytes(CHARSET_UTF_8);
            getView().renderWebViewPostUrl(paymentPassData.getRedirectUrl(), postData);
        } catch (Exception e) {
            e.printStackTrace();
            getView().showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

    public void registerFingerPrint(String transactionId, String publicKey, String date, String accountSignature, String userId) {
        getView().showProgressDialog();
        saveFingerPrintUseCase.execute(saveFingerPrintUseCase.createRequestParams(transactionId, publicKey, date, accountSignature, userId),
                getSubscriberRegisterFingerPrint());
    }

    public void paymentFingerPrint(String transactionId, String publicKey, String date, String accountSignature, String userId) {
        getView().showProgressDialog();
        paymentFingerprintUseCase.execute(paymentFingerprintUseCase.createRequestParams(transactionId, publicKey, date, accountSignature, userId),
                getSubscriberPaymentFingerPrint());
    }

    @Override
    public void getPostDataOtp(String transactionId, String urlOtp) {
        getView().showProgressDialog();
        getPostDataOtpUseCase.execute(getPostDataOtpUseCase.createRequestParams(transactionId, urlOtp),
                getSubscriberPostDataOTP(urlOtp));
    }

    @Override
    public String getUserId() {
        return userSession.getUserId();
    }

    private Subscriber<HashMap<String, String>> getSubscriberPostDataOTP(final String urlOtp) {
        return new Subscriber<HashMap<String, String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressBarDialog();
                getView().onErrorGetPostDataOtp(e);
            }

            @Override
            public void onNext(HashMap<String, String> stringStringHashMap) {
                getView().hideProgressBarDialog();
                if (stringStringHashMap != null) {
                    getView().onSuccessGetPostDataOTP(urlEncodeUTF8(stringStringHashMap), urlOtp);
                } else {
                    getView().onErrorGetPostDataOtp(new RuntimeException());
                }
            }
        };
    }

    private String urlEncodeUTF8(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }

    private String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public Subscriber<Boolean> getSubscriberRegisterFingerPrint() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressBarDialog();
                getView().onErrorRegisterFingerPrint(e);
            }

            @Override
            public void onNext(Boolean isSuccess) {
                getView().hideProgressBarDialog();
                if (isSuccess) {
                    getView().onSuccessRegisterFingerPrint();
                } else {
                    getView().onErrorRegisterFingerPrint(new RuntimeException());
                }
            }
        };
    }

    public Subscriber<ResponsePaymentFingerprint> getSubscriberPaymentFingerPrint() {
        return new Subscriber<ResponsePaymentFingerprint>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressBarDialog();
                getView().onErrorPaymentFingerPrint(e);
            }

            @Override
            public void onNext(ResponsePaymentFingerprint responsePaymentFingerprint) {
                getView().hideProgressBarDialog();
                if (responsePaymentFingerprint.isSuccess()) {
                    getView().onSuccessPaymentFingerprint(responsePaymentFingerprint.getUrl(), responsePaymentFingerprint.getParamEncode());
                } else {
                    getView().onErrorPaymentFingerPrint(new RuntimeException());
                }
            }
        };
    }

    @Override
    public void detachView() {
        paymentFingerprintUseCase.unsubscribe();
        saveFingerPrintUseCase.unsubscribe();
        super.detachView();
    }
}
