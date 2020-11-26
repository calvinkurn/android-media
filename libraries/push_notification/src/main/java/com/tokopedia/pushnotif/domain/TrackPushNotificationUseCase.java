package com.tokopedia.pushnotif.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.pushnotif.R;
import com.tokopedia.pushnotif.data.model.TrackPushNotificationEntity;
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class TrackPushNotificationUseCase extends UseCase<TrackPushNotificationEntity> {

    private Context context;
    private GraphqlUseCase gqlUseCase;

    private final String PARAM_TRANSACTION_ID = "trans_id";
    private final String PARAM_RECIPIENT_ID = "recipient_id";
    private final String PARAM_STATUS = "status";

    public static final String STATUS_DELIVERED = "delivered";
    public static final String STATUS_CLICKED = "clicked";
    public static final String STATUS_DROPPED = "dropped";

    public TrackPushNotificationUseCase(Context context, GraphqlUseCase gqlUseCase) {
        this.context = context;
        this.gqlUseCase = gqlUseCase;
    }

    @Override
    public Observable<TrackPushNotificationEntity> createObservable(RequestParams requestParams) {
        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.mutation_webhook_push_notif);
        GraphqlRequest gqlRequest = new GraphqlRequest(query, TrackPushNotificationEntity.class, requestParams.getParameters());

        gqlUseCase.clearRequest();
        gqlUseCase.addRequest(gqlRequest);


        return gqlUseCase.createObservable(RequestParams.EMPTY)
                .map(gqlResponse -> {
                    return gqlResponse.getData(TrackPushNotificationEntity.class);
                });
    }

    public RequestParams createRequestParam(ApplinkNotificationModel model, String status) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_TRANSACTION_ID, model.getTransactionId());
        params.putString(PARAM_RECIPIENT_ID, model.getToUserId());
        params.putString(PARAM_STATUS, status);
        return params;
    }

}
