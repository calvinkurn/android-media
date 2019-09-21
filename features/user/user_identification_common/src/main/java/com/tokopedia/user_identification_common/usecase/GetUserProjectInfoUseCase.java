package com.tokopedia.user_identification_common.usecase;

import android.content.res.Resources;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.network.exception.MessageErrorException;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.user_identification_common.R;
import com.tokopedia.user_identification_common.pojo.KycUserProjectInfoPojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Observable;

public class GetUserProjectInfoUseCase {

    private static final String PROJECT_ID = "projectId";

    private final GraphqlUseCase graphqlUseCase;
    private final Resources resources;

    @Inject
    public GetUserProjectInfoUseCase(Resources resources, GraphqlUseCase graphqlUseCase) {
        this.resources = resources;
        this.graphqlUseCase = graphqlUseCase;
    }

    public void execute(Map<String, Object> requestParams, Subscriber<GraphqlResponse> subscriber) {
        String query = GraphqlHelper.loadRawString(resources, R.raw.query_get_kyc_project_info);

        GraphqlRequest graphqlRequest = new GraphqlRequest(query, KycUserProjectInfoPojo.class, requestParams, false);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public Observable<KycUserProjectInfoPojo> execute(Map<String, Object> requestParams) {
        String query = GraphqlHelper.loadRawString(resources, R.raw.query_get_kyc_project_info);

        GraphqlRequest graphqlRequest = new GraphqlRequest(query,
                KycUserProjectInfoPojo.class, requestParams, false);

        RequestParams params = RequestParams.create();
        params.putAll(requestParams);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(params).map(graphqlResponse -> {
            KycUserProjectInfoPojo data = graphqlResponse.getData(KycUserProjectInfoPojo.class);
            List<GraphqlError> graphqlErrorList = graphqlResponse.getError(KycUserProjectInfoPojo.class);
            if (data == null) {
                throw new RuntimeException();
            } else if (graphqlErrorList != null && !graphqlErrorList.isEmpty()) {
                Observable.error(new MessageErrorException(graphqlErrorList.get(0).getMessage()));
            }
            return data;
        });
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
