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
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class GetAccessTokenUseCase extends UseCase<TokenInstantDebitBca> {

    private Context context;
    private GraphqlUseCase graphqlUseCase;

    @Inject
    public GetAccessTokenUseCase(@ApplicationContext Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<TokenInstantDebitBca> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<GraphqlResponse>>() {
                    @Override
                    public Observable<GraphqlResponse> call(RequestParams requestParams) {
                        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.mutation_access_token_instant_debit_bca);
                        if (!TextUtils.isEmpty(query)) {
                            graphqlUseCase.clearRequest();
                            graphqlUseCase.addRequest(new GraphqlRequest(query, ResponseAccessTokenBca.class));
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
}
