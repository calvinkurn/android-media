package com.tokopedia.kyc.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.kyc.domain.UploadDocumentUseCase;
import com.tokopedia.kyc.model.KYCDocumentUploadResponse;
import com.tokopedia.kyc.view.interfaces.GenericOperationsView;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class DocumentUploadPresenter extends BaseDaggerPresenter<GenericOperationsView> implements IDocumentUploadListener {

    private UploadDocumentUseCase uploadDocumentUseCase;

    @Inject
    public DocumentUploadPresenter(){

    }

    @Override
    public void makeDocumentUploadRequest(String imagePath, String docType, int kycReqId) {
        uploadDocumentUseCase = new UploadDocumentUseCase(null,
                getView().getActivity(), imagePath, docType, kycReqId);
        uploadDocumentUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showHideProgressBar(false);
                getView().failure(null);
            }
            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                getView().showHideProgressBar(false);
                KYCDocumentUploadResponse kycDocumentUploadResponse =
                        (typeRestResponseMap.get(KYCDocumentUploadResponse.class)).getData();
                if(kycDocumentUploadResponse != null &&
                        kycDocumentUploadResponse.getKycImageUploadDataClass() != null &&
                        kycDocumentUploadResponse.getKycImageUploadDataClass().getDocumentId() > 0){
                    getView().success(kycDocumentUploadResponse);
                }
                else {
                    getView().failure(kycDocumentUploadResponse);
                }
            }
        });
    }

    @Override
    public void detachView() {
        if(uploadDocumentUseCase != null) {
            uploadDocumentUseCase.unsubscribe();
        }
        super.detachView();
    }
}
