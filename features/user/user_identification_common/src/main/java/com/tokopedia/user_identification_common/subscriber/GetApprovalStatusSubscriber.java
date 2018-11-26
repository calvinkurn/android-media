package com.tokopedia.user_identification_common.subscriber;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.network.exception.MessageErrorException;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.user_identification_common.R;
import com.tokopedia.user_identification_common.pojo.GetApprovalStatusPojo;

import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 15/11/18.
 */
public class GetApprovalStatusSubscriber extends Subscriber<GraphqlResponse> {

    private final Context context;
    private final GetApprovalStatusListener listener;

    public interface GetApprovalStatusListener {
        void onErrorGetShopVerificationStatus(String errorMessage);

        void onSuccessGetShopVerificationStatus(int status);
    }

    public GetApprovalStatusSubscriber(Context context,
                                       GetApprovalStatusListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (listener != null) {
            listener.onErrorGetShopVerificationStatus(ErrorHandler.getErrorMessage(context, e));
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
            listener.onErrorGetShopVerificationStatus(ErrorHandler.getErrorMessage(context,
                    new MessageErrorException(graphqlErrorList.get(0).getMessage())));
        } else if (listener != null) {
            listener.onErrorGetShopVerificationStatus(String.format("%s (%s)",
                    context.getString(R.string.default_request_error_unknown),
                    KYCConstant.UNHANDLED_RESPONSE));
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
            listener.onErrorGetShopVerificationStatus(pojo.getKycStatus().getMessage().get(0));
        } else {
            listener.onErrorGetShopVerificationStatus(String.format("%s (%s)",
                    context.getString(R.string
                    .default_request_error_unknown),
                    KYCConstant.ERROR_MESSAGE_EMPTY));
        }
    }
}
