package com.tokopedia.sellerapp.dashboard.usecase;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.sellerapp.dashboard.model.kyc.GetApprovalStatusPojo;
import com.tokopedia.sellerapp.R;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 15/11/18.
 */
public class GetVerificationStatusUseCase {

    private static final String PROJECT_ID = "project_id";
    private static final int MERCHANT_ID = 1;

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

    public static Map<String,Object> getRequestParam(){
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put(PROJECT_ID, MERCHANT_ID);
        return requestParams;
    }

    public void unsubscribe() {
        graphqlUseCase.unsubscribe();
    }
}
