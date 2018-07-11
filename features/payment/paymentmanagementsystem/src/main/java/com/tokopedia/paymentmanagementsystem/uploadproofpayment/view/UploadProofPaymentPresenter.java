package com.tokopedia.paymentmanagementsystem.uploadproofpayment.view;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.paymentmanagementsystem.uploadproofpayment.domain.UploadProofPaymentUseCase;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 7/6/18.
 */

public class UploadProofPaymentPresenter extends BaseDaggerPresenter<UploadProofPaymentContract.View> implements UploadProofPaymentContract.Presenter {

    private UploadProofPaymentUseCase uploadProofPaymentUseCase;

    public UploadProofPaymentPresenter(UploadProofPaymentUseCase uploadProofPaymentUseCase) {
        this.uploadProofPaymentUseCase = uploadProofPaymentUseCase;
    }

    @Override
    public void uploadProofPayment(String transactionId, String merchantCode, String imageUrl) {
        uploadProofPaymentUseCase.execute(uploadProofPaymentUseCase.createRequestParams(transactionId, merchantCode, imageUrl), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        });
    }
}
