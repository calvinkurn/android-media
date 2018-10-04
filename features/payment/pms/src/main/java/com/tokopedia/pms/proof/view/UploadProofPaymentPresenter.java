package com.tokopedia.pms.proof.view;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.pms.proof.domain.UploadProofPaymentUseCase;
import com.tokopedia.pms.proof.model.UploadProof;

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
        getView().showLoadingDialog();
        uploadProofPaymentUseCase.execute(uploadProofPaymentUseCase.createRequestParams(transactionId, merchantCode, imageUrl), new Subscriber<UploadProof>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().hideLoadingDialog();
                    getView().onErrorUploadProof(e);
                }
            }

            @Override
            public void onNext(UploadProof uploadProof) {
                getView().hideLoadingDialog();
                getView().onResultUploadProof(uploadProof);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        uploadProofPaymentUseCase.unsubscribe();
    }
}
