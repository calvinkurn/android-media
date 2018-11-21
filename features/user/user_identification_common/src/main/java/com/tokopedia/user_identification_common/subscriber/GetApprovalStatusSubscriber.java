package com.tokopedia.user_identification_common.subscriber;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.user_identification_common.pojo.GetApprovalStatusPojo;
import com.tokopedia.user_identification_common.R;
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
        if (listener != null
                && graphqlResponse.getData(GetApprovalStatusPojo.class) != null) {

            GetApprovalStatusPojo pojo = graphqlResponse.getData(GetApprovalStatusPojo
                    .class);
            if (pojo.getKycStatus() != null
                    && pojo.getKycStatus().getKycStatusDetailPojo() != null
                    && pojo.getKycStatus().getKycStatusDetailPojo().getIsSuccess() == 1) {
                listener.onSuccessGetShopVerificationStatus(pojo.getKycStatus().getKycStatusDetailPojo().getStatus());
            } else if (pojo.getKycStatus() != null
                    && pojo.getKycStatus().getMessage() != null
                    && !pojo.getKycStatus().getMessage().isEmpty()) {
                listener.onErrorGetShopVerificationStatus(pojo.getKycStatus().getMessage().get(0));
            } else {
                listener.onErrorGetShopVerificationStatus(String.format("%s (%s)", context.getString(R.string
                        .default_request_error_unknown), KYCConstant.ERROR_MESSAGE_EMPTY));
            }
        }
    }
}
