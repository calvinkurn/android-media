package com.tokopedia.user_identification_common.subscriber;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.user_identification_common.pojo.GetApprovalStatusPojo;

import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 15/11/18.
 */
public class GetApprovalStatusSubscriber extends Subscriber<GraphqlResponse> {

    private final GetApprovalStatusListener listener;

    public interface GetApprovalStatusListener {
        void onErrorGetShopVerificationStatus(Throwable errorMessage);

        void onSuccessGetShopVerificationStatus(int status);

        void onErrorGetShopVerificationStatusWithErrorCode(String errorCode);
    }

    public GetApprovalStatusSubscriber(GetApprovalStatusListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (listener != null) {
            listener.onErrorGetShopVerificationStatus(e);
        }
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        GetApprovalStatusPojo pojo = graphqlResponse.getData(GetApprovalStatusPojo.class);
        List<GraphqlError> graphqlErrorList = graphqlResponse.getError(GetApprovalStatusPojo.class);
        if (listener != null
                && pojo != null
                && (graphqlErrorList == null || graphqlErrorList.isEmpty())) {
            routingOnNext(pojo);
        } else if (graphqlErrorList != null
                && !graphqlErrorList.isEmpty()
                && listener != null
                && graphqlErrorList.get(0) != null
                && graphqlErrorList.get(0).getMessage() != null) {
            listener.onErrorGetShopVerificationStatus(new MessageErrorException(graphqlErrorList.get(0).getMessage()));
        } else if (listener != null) {
            listener.onErrorGetShopVerificationStatusWithErrorCode(KYCConstant.UNHANDLED_RESPONSE);
        }

    }

    private void routingOnNext(GetApprovalStatusPojo pojo) {
        if (pojo.getKycStatus() != null
                && pojo.getKycStatus().getKycStatusDetailPojo() != null
                && pojo.getKycStatus().getKycStatusDetailPojo().getIsSuccess() == 1) {
            listener.onSuccessGetShopVerificationStatus(pojo.getKycStatus().getKycStatusDetailPojo().getStatus());
        } else if (pojo.getKycStatus() != null
                && pojo.getKycStatus().getMessage() != null
                && !pojo.getKycStatus().getMessage().isEmpty()) {
            listener.onErrorGetShopVerificationStatus(new MessageErrorException(pojo.getKycStatus().getMessage().get(0)));
        } else {
            listener.onErrorGetShopVerificationStatusWithErrorCode(KYCConstant.ERROR_MESSAGE_EMPTY);
        }
    }
}
