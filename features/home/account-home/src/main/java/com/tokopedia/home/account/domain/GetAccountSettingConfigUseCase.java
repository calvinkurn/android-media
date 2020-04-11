package com.tokopedia.home.account.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.data.model.AccountSettingConfig;
import com.tokopedia.home.account.R;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 13/11/18.
 */
public class GetAccountSettingConfigUseCase  {

    private static final int TYPE_CUSTOMER_APP = 11;
    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    public GetAccountSettingConfigUseCase(@ApplicationContext Context context,
                                    GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    public void execute(Map<String, Object> requestParams, Subscriber<GraphqlResponse> subscriber){
        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw
                .query_account_setting_config);

        GraphqlRequest graphqlRequest = new GraphqlRequest(query,
                AccountSettingConfig.class, requestParams);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public static Map<String,Object> getRequestParam(){
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("type", TYPE_CUSTOMER_APP);
        return requestParams;
    }

    public void unsubscribe() {
        graphqlUseCase.unsubscribe();
    }
}
