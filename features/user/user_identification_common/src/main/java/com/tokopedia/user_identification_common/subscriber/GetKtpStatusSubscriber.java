package com.tokopedia.user_identification_common.subscriber;
//
// Created by Yoris Prayogo on 2019-10-29.
//

import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.user_identification_common.domain.pojo.CheckKtpStatusPojo;
import com.tokopedia.user_identification_common.domain.pojo.KtpStatusPojo;

import java.util.List;

import rx.Subscriber;

public class GetKtpStatusSubscriber extends Subscriber<GraphqlResponse>{

    private final GetKtpStatusListener listener;

    @Override
    public void onCompleted() {}

    @Override
    public void onError(Throwable e) {
        listener.onErrorGetKtpStatus(e);
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        CheckKtpStatusPojo pojo = graphqlResponse.getData(CheckKtpStatusPojo.class);
        List<GraphqlError> graphqlErrorList = graphqlResponse.getError(CheckKtpStatusPojo.class);

        if (listener != null && pojo != null && (graphqlErrorList == null || graphqlErrorList.isEmpty())) {
            routingOnNext(pojo.getKtpStatus());
        }
    }

    private void routingOnNext(KtpStatusPojo pojo) {
        if (pojo.getBypass() || pojo.getValid()) {
            listener.onKtpValid();
        } else {
            listener.onKtpInvalid(pojo.getError());
        }
    }

    public interface GetKtpStatusListener {
        void onErrorGetKtpStatus(Throwable throwable);
        void onKtpInvalid(String message);
        void onKtpValid();
    }

    public GetKtpStatusSubscriber(GetKtpStatusListener listener) {
        this.listener = listener;
    }
}
