package com.tokopedia.useridentification.subscriber;

import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.network.exception.MessageErrorException;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo;

import java.util.List;

import rx.Subscriber;

public class GetUserProjectInfoSubcriber extends Subscriber<GraphqlResponse> {

    public interface GetUserProjectInfoListener {
        void onUserBlacklist();
        void onSuccessGetUserProjectInfo(int status);
        void onErrorGetUserProjectInfo(Throwable throwable);
        void onErrorGetUserProjectInfoWithErrorCode(String errorCode);
    }

    private final GetUserProjectInfoListener listener;

    public GetUserProjectInfoSubcriber(GetUserProjectInfoListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (listener != null) {
            listener.onErrorGetUserProjectInfo(e);
        }
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        KycUserProjectInfoPojo pojo = graphqlResponse.getData(KycUserProjectInfoPojo.class);
        List<GraphqlError> graphqlErrorList = graphqlResponse.getError(KycUserProjectInfoPojo.class);

        if (listener != null && pojo != null && (graphqlErrorList == null || graphqlErrorList.isEmpty())) {
            routingOnNext(pojo);
        } else if (graphqlErrorList != null && !graphqlErrorList.isEmpty() && listener != null && graphqlErrorList.get(0) != null && graphqlErrorList.get(0).getMessage() != null) {
            listener.onErrorGetUserProjectInfo(new MessageErrorException(graphqlErrorList.get(0).getMessage()));
        } else if (listener != null) {
            listener.onErrorGetUserProjectInfoWithErrorCode(KYCConstant.UNHANDLED_RESPONSE);
        }
    }

    private void routingOnNext(KycUserProjectInfoPojo pojo) {
        if (pojo.getKycProjectInfo() != null) {
            if (pojo.getKycProjectInfo().getStatus() == KYCConstant.STATUS_BLACKLISTED) {
                listener.onUserBlacklist();
            } else {
                listener.onSuccessGetUserProjectInfo(pojo.getKycProjectInfo().getStatus());
            }
        } else {
            listener.onErrorGetUserProjectInfoWithErrorCode(KYCConstant.ERROR_MESSAGE_EMPTY);
        }
    }

}
