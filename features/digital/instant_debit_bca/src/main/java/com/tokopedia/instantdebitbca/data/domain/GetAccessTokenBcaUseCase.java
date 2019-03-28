package com.tokopedia.instantdebitbca.data.domain;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.instantdebitbca.R;
import com.tokopedia.instantdebitbca.data.data.ResponseAccessTokenBca;
import com.tokopedia.instantdebitbca.data.view.model.TokenInstantDebitBca;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class GetAccessTokenBcaUseCase extends UseCase<TokenInstantDebitBca> {

    public static final String MERCHANT_CODE = "merchantCode";
    public static final String PROFILE_CODE = "profileCode";

    private Context context;
    private GraphqlUseCase graphqlUseCase;

    @Inject
    public GetAccessTokenBcaUseCase(@ApplicationContext Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<TokenInstantDebitBca> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<GraphqlResponse>>() {
                    @Override
                    public Observable<GraphqlResponse> call(RequestParams requestParams) {
                        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.idb_access_token_mutation);
                        Map<String, Object> variable = requestParams.getParameters();
                        if (!TextUtils.isEmpty(query)) {
                            graphqlUseCase.clearRequest();
                            graphqlUseCase.addRequest(new GraphqlRequest(query, ResponseAccessTokenBca.class, variable));
                            return graphqlUseCase.createObservable(null);
                        }
                        return Observable.error(new Exception("Query variable are empty"));
                    }
                })
                .map(new Func1<GraphqlResponse, ResponseAccessTokenBca>() {
                    @Override
                    public ResponseAccessTokenBca call(GraphqlResponse graphqlResponse) {
                        return graphqlResponse.getData(ResponseAccessTokenBca.class);
                    }
                })
                .map(it -> {
                    TokenInstantDebitBca token = new TokenInstantDebitBca();
                    token.setAccessToken(it.getMerchantAuth().getDataToken().getTokenBca().getAccessToken());
                    token.setTokenType(it.getMerchantAuth().getDataToken().getTokenBca().getTokenType());
                    return token;
                });
    }

    public RequestParams createRequestParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(MERCHANT_CODE, AuthUtil.KEY.INSTANT_DEBIT_BCA_MERCHANT_CODE);
        requestParams.putString(PROFILE_CODE, AuthUtil.KEY.INSTANT_DEBIT_BCA_PROFILE_CODE);
        return requestParams;
    }
}
