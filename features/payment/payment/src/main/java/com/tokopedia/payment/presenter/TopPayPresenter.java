package com.tokopedia.payment.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint;
import com.tokopedia.payment.fingerprint.domain.PaymentFingerprintUseCase;
import com.tokopedia.payment.fingerprint.domain.SaveFingerPrintUseCase;
import com.tokopedia.payment.fingerprint.domain.SavePublicKeyUseCase;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.payment.utils.ErrorNetMessage;

import java.io.UnsupportedEncodingException;

import rx.Subscriber;

/**
 * Created by kris on 3/14/17. Tokopedia
 */

public class TopPayPresenter extends BaseDaggerPresenter<TopPayContract.View> implements TopPayContract.Presenter {

    private SaveFingerPrintUseCase saveFingerPrintUseCase;
    private PaymentFingerprintUseCase paymentFingerprintUseCase;
    private UserSession userSession;

    public TopPayPresenter(SaveFingerPrintUseCase saveFingerPrintUseCase,
                           SavePublicKeyUseCase savePublicKeyUseCase,
                           PaymentFingerprintUseCase paymentFingerprintUseCase,
                           UserSession userSession) {
        this.saveFingerPrintUseCase = saveFingerPrintUseCase;
        this.paymentFingerprintUseCase = paymentFingerprintUseCase;
        this.userSession = userSession;
    }

    @Override
    public void proccessUriPayment() {
        PaymentPassData paymentPassData = getView().getPaymentPassData();
        try {
            byte[] postData = paymentPassData.getQueryString().getBytes(CHARSET_UTF_8);
            getView().renderWebViewPostUrl(paymentPassData.getRedirectUrl(), postData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            getView().showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

    public void registerFingerPrint(String transactionId, String publicKey, String date, String accountSignature, String userId) {
        getView().showProgressDialog();
        saveFingerPrintUseCase.execute(saveFingerPrintUseCase.createRequestParams(transactionId, publicKey, date, accountSignature, userId),
                getSubscriberRegisterFingerPrint());
    }

    public void paymentFingerPrint(String transactionId, String partner, String publicKey, String date, String accountSignature, String userId) {
        getView().showProgressDialog();
        paymentFingerprintUseCase.execute(paymentFingerprintUseCase.createRequestParams(transactionId, partner, publicKey, date, accountSignature, userId),
                getSubscriberPaymentFingerPrint());
    }

    @Override
    public String getUserId() {
        return userSession.getUserId();
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
                if(isSuccess){
                    getView().onSuccessRegisterFingerPrint();
                }else{
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
                if(responsePaymentFingerprint.isSuccess()){
                    getView().onSuccessPaymentFingerprint(responsePaymentFingerprint.getUrl(), responsePaymentFingerprint.getParamEncode());
                }else{
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
