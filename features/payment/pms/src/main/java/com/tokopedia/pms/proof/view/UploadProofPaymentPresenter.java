package com.tokopedia.pms.proof.view;

import android.content.res.Resources;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.pms.R;
import com.tokopedia.pms.common.Constant;
import com.tokopedia.pms.proof.domain.UploadPaymentProofUseCase;
import com.tokopedia.pms.proof.model.PaymentProofResponse;
import com.tokopedia.pms.proof.model.getproof.DataResponseGetProof;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 7/6/18.
 */

public class UploadProofPaymentPresenter extends BaseDaggerPresenter<UploadProofPaymentContract.View> implements UploadProofPaymentContract.Presenter {

    private UploadPaymentProofUseCase uploadPaymentProofUseCase;
    private GraphqlUseCase getProofUseCase;

    public UploadProofPaymentPresenter(GraphqlUseCase getProofUseCase,
                                       UploadPaymentProofUseCase uploadPaymentProofUseCase) {
        this.getProofUseCase = getProofUseCase;
        this.uploadPaymentProofUseCase = uploadPaymentProofUseCase;
    }

    @Override
    public void uploadProofPayment(String transactionId, String merchantCode, String imageUrl) {
        getView().showLoadingDialog();
        uploadPaymentProofUseCase.setRequestParams(transactionId, merchantCode, imageUrl);
        uploadPaymentProofUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
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
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
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
        getProofUseCase.unsubscribe();
    }

    public void getProofPayment(String transactionId, String merchantCode, Resources resources) {
        getView().showLoadingMain();
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put(Constant.TRANSACTION_ID, transactionId);
        variables.put(Constant.MERCHANT_CODE, merchantCode);
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources, R.raw.get_proof_payment), DataResponseGetProof.class, variables, false);
        getProofUseCase.clearRequest();
        getProofUseCase.addRequest(graphqlRequest);
        getProofUseCase.execute(RequestParams.create(), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideLoadingMain();
                    getView().onErrorGetImageProof(e);
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                getView().hideLoadingMain();
                DataResponseGetProof dataResponseGetProof = graphqlResponse.getData(DataResponseGetProof.class);
                if (dataResponseGetProof.getGetProof().isSuccess()) {
                    getView().onSuccessGetImageProof(dataResponseGetProof.getGetProof().getImageUrl());
                } else {
                    getView().onErrorGetImageProof(new MessageErrorException(dataResponseGetProof.getGetProof().getMessage()));
                }
            }
        });
    }
}
