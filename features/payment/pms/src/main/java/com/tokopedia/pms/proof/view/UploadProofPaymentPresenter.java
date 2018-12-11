package com.tokopedia.pms.proof.view;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.pms.R;
import com.tokopedia.pms.common.Constant;
import com.tokopedia.pms.payment.data.model.DataPaymentList;
import com.tokopedia.pms.proof.domain.UploadProofPaymentUseCase;
import com.tokopedia.pms.proof.model.UploadProof;
import com.tokopedia.pms.proof.model.getproof.DataResponseGetProof;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 7/6/18.
 */

public class UploadProofPaymentPresenter extends BaseDaggerPresenter<UploadProofPaymentContract.View> implements UploadProofPaymentContract.Presenter {

    private UploadProofPaymentUseCase uploadProofPaymentUseCase;
    private GraphqlUseCase getProofUseCase;

    public UploadProofPaymentPresenter(UploadProofPaymentUseCase uploadProofPaymentUseCase, GraphqlUseCase getProofUseCase) {
        this.uploadProofPaymentUseCase = uploadProofPaymentUseCase;
        this.getProofUseCase = getProofUseCase;
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
        getProofUseCase.unsubscribe();
    }

    public void getProofPayment(String transactionId, String merchantCode, Resources resources) {
        getView().showLoadingMain();
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put(Constant.TRANSACTION_ID, transactionId);
        variables.put(Constant.MERCHANT_CODE, merchantCode);
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources, R.raw.get_proof_payment),DataResponseGetProof.class, variables);
        getProofUseCase.clearRequest();
        getProofUseCase.addRequest(graphqlRequest);
        getProofUseCase.execute(RequestParams.create(), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().hideLoadingMain();
                    getView().onErrorGetImageProof(e);
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                getView().hideLoadingMain();
                DataResponseGetProof dataResponseGetProof = graphqlResponse.getData(DataResponseGetProof.class);
                if(dataResponseGetProof.getGetProof().isSuccess()) {
                    getView().onSuccessGetImageProof(dataResponseGetProof.getGetProof().getImageUrl());
                }else{
                    getView().onErrorGetImageProof(new MessageErrorException(dataResponseGetProof.getGetProof().getMessage()));
                }
            }
        });
    }
}
