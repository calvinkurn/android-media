package com.tokopedia.user_identification_common.usecase;

import android.content.res.Resources;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.user_identification_common.R;
import com.tokopedia.user_identification_common.pojo.GetApprovalStatusPojo;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 12/11/18.
 */
public class GetApprovalStatusUseCase {

    private static final String PROJECT_ID = "projectId";

    private final GraphqlUseCase graphqlUseCase;
    private final Resources resources;

    @Inject
    public GetApprovalStatusUseCase(Resources resources,
                                    GraphqlUseCase graphqlUseCase) {
        this.resources = resources;
        this.graphqlUseCase = graphqlUseCase;

    }

    public void execute(Map<String, Object> requestParams, Subscriber<GraphqlResponse> subscriber) {
        String query = GraphqlHelper.loadRawString(resources, R.raw
                .query_get_kyc_approval_status);

        GraphqlRequest graphqlRequest = new GraphqlRequest(query,
                GetApprovalStatusPojo.class, requestParams);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public static Map<String, Object> getRequestParam() {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put(PROJECT_ID, KYCConstant.KYC_PROJECT_ID);
        return requestParams;
    }

    public void unsubscribe() {
        graphqlUseCase.unsubscribe();
    }
}
