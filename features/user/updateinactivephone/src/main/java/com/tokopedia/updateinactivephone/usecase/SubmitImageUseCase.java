package com.tokopedia.updateinactivephone.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.graphql.data.ObservableFactory;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.model.response.GqlUpdatePhoneStatusResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.EMAIL;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.FILE_UPLOADED;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.PHONE;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.USER_ID;

public class SubmitImageUseCase {
    public static final String PARAM_FILE_UPLOADED = "file_uploaded";


    private GraphqlUseCase graphqlUseCase;
    private Context context;

    public SubmitImageUseCase(Context context) {
        graphqlUseCase = new GraphqlUseCase();
        this.context = context;
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public Observable<GraphqlResponse> getObservable(RequestParams requestParams) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(PHONE, requestParams.getString(PHONE, ""));
        variables.put(EMAIL, requestParams.getString(EMAIL, ""));
        variables.put(USER_ID, requestParams.getInt(USER_ID, 0));
        variables.put(FILE_UPLOADED, requestParams.getObject(PARAM_FILE_UPLOADED));

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_update_phone_email),
                GqlUpdatePhoneStatusResponse.class,
                variables, false);

        List<GraphqlRequest> graphqlRequestList = new ArrayList<>();
        graphqlRequestList.add(graphqlRequest);

        GraphqlCacheStrategy graphqlCacheStrategy =
                new GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build();

        return ObservableFactory.create(graphqlRequestList,
                graphqlCacheStrategy);
    }
}
