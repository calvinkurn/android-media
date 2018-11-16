package com.tokopedia.useridentification.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.useridentification.R;
import com.tokopedia.useridentification.domain.pojo.GetApprovalStatusPojo;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 12/11/18.
 */
public class GetApprovalStatusUseCase {

    private static final String PROJECT_ID = "project_id";
    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    public GetApprovalStatusUseCase(@ApplicationContext Context context,
                                    GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;

    }

    public void execute(Map<String, Object> requestParams, Subscriber<GraphqlResponse> subscriber){
        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw
                .query_get_kyc_approval_status);

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
