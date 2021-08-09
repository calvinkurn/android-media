package com.tokopedia.pms.proof.view;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.pms.analytics.PmsIdlingResource;
import com.tokopedia.pms.proof.domain.UploadPaymentProofUseCase;
import com.tokopedia.pms.proof.model.PaymentProofResponse;

import java.lang.reflect.Type;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 7/6/18.
 */

public class UploadProofPaymentPresenter extends BaseDaggerPresenter<UploadProofPaymentContract.View>
        implements UploadProofPaymentContract.Presenter {

    private UploadPaymentProofUseCase uploadPaymentProofUseCase;

    public UploadProofPaymentPresenter(UploadPaymentProofUseCase uploadPaymentProofUseCase) {
        this.uploadPaymentProofUseCase = uploadPaymentProofUseCase;
    }

    @Override
    public void uploadProofPayment(String transactionId, String merchantCode, String imageUrl) {
        PmsIdlingResource.INSTANCE.increment();
        getView().showLoadingDialog();
        uploadPaymentProofUseCase.setRequestParams(transactionId, merchantCode, imageUrl);
        uploadPaymentProofUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                PmsIdlingResource.INSTANCE.decrement();
                if(isViewAttached()) {
                    getView().hideLoadingDialog();
                    getView().onErrorUploadProof(e);
                }
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                PmsIdlingResource.INSTANCE.decrement();
                Type token = new TypeToken<PaymentProofResponse>() {}.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                PaymentProofResponse paymentProofResponse = restResponse.getData();
                getView().onResultUploadProof(paymentProofResponse);
                getView().hideLoadingDialog();
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        uploadPaymentProofUseCase.unsubscribe();
    }
}
