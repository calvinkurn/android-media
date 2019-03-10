package com.tokopedia.kyc.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kyc.model.ConfirmRequestDataContainer;
import com.tokopedia.kyc.model.ConfirmSubmitResponse;
import com.tokopedia.kyc.model.GqlDocModel;
import com.tokopedia.kyc.view.KycUtil;
import com.tokopedia.kyc.view.interfaces.GenericOperationsView;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;

public class TnCConfirmationPresenter extends BaseDaggerPresenter<GenericOperationsView> implements ITncConfirmation{

    @Inject
    public TnCConfirmationPresenter(){

    }

    @Override
    public void submitKycTnCConfirmForm(ConfirmRequestDataContainer confirmRequestDataContainer) {
        HashMap<String, Object> gqlDataHashMap = new HashMap<>();
        gqlDataHashMap.put("kyc_request_id", confirmRequestDataContainer.getKycReqId());
        gqlDataHashMap.put("identity_document_type",
                confirmRequestDataContainer.getDocumentType());
        gqlDataHashMap.put("identity_document_number",
                confirmRequestDataContainer.getDocumentNumber());
        gqlDataHashMap.put("mother_maiden_name",
                confirmRequestDataContainer.getMothersMaidenName());
        GqlDocModel ob1 = new GqlDocModel();
        GqlDocModel ob2 = new GqlDocModel();
        ArrayList<GqlDocModel> list = new ArrayList<>();
        ob1.setKey(confirmRequestDataContainer.getCardIdDocumentId());
        ob2.setKey(confirmRequestDataContainer.getSelfieIdDocumentId());
        list.add(ob1);
        list.add(ob2);
        gqlDataHashMap.put("documents", list);

        KycUtil.executeKycConfirmation(getView().getActivity(), getTnCAcceptGQLSubscriber(), gqlDataHashMap);
    }

    private Subscriber getTnCAcceptGQLSubscriber() {
        return new Subscriber<GraphqlResponse>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getView().showHideProgressBar(false);
                getView().failure(null);
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                getView().showHideProgressBar(false);
                ConfirmSubmitResponse kycDocumentUploadResponse = graphqlResponse.getData(ConfirmSubmitResponse.class);
                if (kycDocumentUploadResponse != null && kycDocumentUploadResponse.getConfirmSubmitResponse().getKycRequestId() > 0) {
                    getView().success(kycDocumentUploadResponse);
                }
                else {
                    getView().failure(null);
                }
            }
        };
    }

}
