package com.tokopedia.affiliate.feature.onboarding.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername.RegisterUsernameData;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by milhamj on 10/7/18.
 */
public class RegisterUsernameUseCase extends GraphqlUseCase {
    private static final String AFFILIATE_NAME = "affiliateName";

    private final Context context;

    @Inject
    RegisterUsernameUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public Observable<GraphqlResponse> createObservable(RequestParams params) {
        String query = GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.mutation_af_register_username
        );

        this.clearRequest();
        this.addRequest(new GraphqlRequest(
                query,
                RegisterUsernameData.class,
                params.getParameters(), false)
        );
        return super.createObservable(params);
    }

    public static RequestParams createRequestParams(String username) {
        RequestParams param = RequestParams.create();
        param.putString(AFFILIATE_NAME, username);
        return param;
    }
}
