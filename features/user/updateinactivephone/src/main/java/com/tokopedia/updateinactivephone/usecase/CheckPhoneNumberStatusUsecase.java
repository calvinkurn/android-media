package com.tokopedia.updateinactivephone.usecase;

import android.content.Context;

import com.tokoepdia.updateinactivephone.model.response.GqlCheckPhoneStatusResponse;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.subscriber.CheckPhoneNumberStatusSubscriber;

import java.util.HashMap;
import java.util.Map;


public class CheckPhoneNumberStatusUsecase {

    private static final String OPERATION_NAME = "validateInactivePhoneQuery";
    private static final String PHONE = "phone";

    private GraphqlUseCase graphqlUseCase;
    private Context context;

    public CheckPhoneNumberStatusUsecase(Context context) {
        graphqlUseCase = new GraphqlUseCase();
        this.context = context;
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public void execute(String phone, CheckPhoneNumberStatusSubscriber checkPhoneNumberStatusSubscriber) {
        graphqlUseCase.clearRequest();
        Map<String, Object> variables = new HashMap<>();

        variables.put(PHONE, phone);
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_check_phone_number_status),
                GqlCheckPhoneStatusResponse.class,
                variables, OPERATION_NAME);

        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(checkPhoneNumberStatusSubscriber);
    }
}
