package com.tokopedia.useridentification.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.useridentification.R;
import com.tokopedia.useridentification.domain.pojo.RegisterIdentificationPojo;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by alvinatin on 23/11/18.
 */

public class RegisterIdentificationUseCase {

    private static final String PROJECT_ID = "projectID";

    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    public RegisterIdentificationUseCase(@ApplicationContext Context context,
                                         GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;

    }

    public Observable<GraphqlResponse> createObservable(RequestParams params) {
        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw
                .mutation_register_kyc);

        GraphqlRequest graphqlRequest = new GraphqlRequest(query,
                RegisterIdentificationPojo.class, params.getParameters(), false);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(params);
    }

    public static RequestParams getRequestParam(int projectid) {
        RequestParams param = RequestParams.create();
        if (projectid != -1)
            param.putInt(PROJECT_ID, projectid);
        else
            param.putInt(PROJECT_ID, KYCConstant.KYC_PROJECT_ID);
        return param;
    }

    public void unsubscribe() {
        graphqlUseCase.unsubscribe();
    }
}
