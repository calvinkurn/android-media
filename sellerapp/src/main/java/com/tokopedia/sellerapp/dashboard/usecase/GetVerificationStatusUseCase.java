package com.tokopedia.sellerapp.dashboard.usecase;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.sellerapp.dashboard.model.GetApprovalStatusPojo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.sellerapp.R;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.internal.observers.AssertableSubscriberObservable;

/**
 * @author by nisie on 15/11/18.
 */
public class GetVerificationStatusUseCase {

    private static final String PROJECT_ID = "project_id";
    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    public GetVerificationStatusUseCase(@ApplicationContext Context context,
                                    GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;

    }

    public void execute(Map<String, Object> requestParams, Subscriber<GraphqlResponse> subscriber){
        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw
                .query_get_kyc_seller_dashboard_status);

        GraphqlRequest graphqlRequest = new GraphqlRequest(query,
                GetApprovalStatusPojo.class, requestParams);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public static Map<String,Object> getRequestParam(String projectId){
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put(PROJECT_ID, projectId);
        return requestParams;
    }

    public void unsubscribe() {
        graphqlUseCase.unsubscribe();
    }
}
