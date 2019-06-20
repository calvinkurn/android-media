package com.tokopedia.pushnotif.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.pushnotif.R;
import com.tokopedia.pushnotif.domain.pojo.TrackPushNotificationEntity;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class TrackPushNotificationUseCase extends UseCase<TrackPushNotificationEntity> {

    private Context context;
    private GraphqlUseCase gqlUseCase;

    private final String PARAM_TRANSACTION_ID = "trans_id";
    private final String PARAM_RECIPIENT_ID = "recepient_id";
    private final String PARAM_STATUS = "status";

    private final String STATUS_DELIVERED = "delivered";
    private final String STATUS_CLICKED = "clicked";


    private TrackPushNotificationUseCase() {
    }

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

    public RequestParams createRequestParam(ApplinkNotificationModel model) {
        RequestParams params = RequestParams.create();

        params.putString(PARAM_TRANSACTION_ID, model.getGId());
        params.putString(PARAM_RECIPIENT_ID, model.getToUserId());
        params.putString(PARAM_STATUS, STATUS_DELIVERED);

        return params;
    }

}
