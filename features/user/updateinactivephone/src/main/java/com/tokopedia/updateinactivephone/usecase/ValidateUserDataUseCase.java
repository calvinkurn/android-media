package com.tokopedia.updateinactivephone.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.model.response.GqlValidateUserDataResponse;
import com.tokopedia.updateinactivephone.subscriber.ValidateUserDataSubscriber;

import java.util.HashMap;
import java.util.Map;

import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.OPERATION_NAME.VALIDATE_USER_DATA;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.EMAIL;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.PHONE;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.USER_ID;

public class ValidateUserDataUseCase {

    private GraphqlUseCase graphqlUseCase;
    private Context context;

    public ValidateUserDataUseCase(Context context) {
        graphqlUseCase = new GraphqlUseCase();
        this.context = context;
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public void execute(String phone, String email, String userId, ValidateUserDataSubscriber validateUserDataSubscriber) {
        graphqlUseCase.clearRequest();
        Map<String, Object> variables = new HashMap<>();

        variables.put(PHONE, phone);
        variables.put(EMAIL, email);
        variables.put(USER_ID, Integer.parseInt(userId));

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_validate_user_data),
                GqlValidateUserDataResponse.class,
                variables, VALIDATE_USER_DATA, false);

        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(validateUserDataSubscriber);
    }
}
