package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.shopetalase.gql.ReorderShopEtalaseMutation;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class ReorderShopEtalaseUseCase extends UseCase<String> {
    public static final String IDS = "ids";

    private SingleGraphQLUseCase<ReorderShopEtalaseMutation> graphQLUseCase;

    @Inject
    public ReorderShopEtalaseUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<ReorderShopEtalaseMutation>(context, ReorderShopEtalaseMutation.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_mutation_reorder_shop_etalase;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected HashMap<String, Object> createGraphQLVariable(RequestParams requestParams) {
                HashMap<String, Object> variables = new HashMap<>();
                ArrayList<String> ids = (ArrayList<String>) requestParams.getObject(IDS);
                variables.put(IDS, ids);
                return variables;
            }
        };
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(new GraphQLSuccessMapper())
                //TODO remove below, just for test.
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends String>>() {
                    @Override
                    public Observable<? extends String> call(Throwable throwable) {
                        String jsonString = "{\"reorderShopShowcase\":{\"success\":true,\"message\":\"Success\"}}";
                        ReorderShopEtalaseMutation response = new Gson().fromJson(jsonString, ReorderShopEtalaseMutation.class);
                        return Observable.just(response).flatMap(new GraphQLSuccessMapper());
                    }
                });

    }

    public static RequestParams createRequestParams(ArrayList<String> etalaseIdList) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(IDS, etalaseIdList);
        return requestParams;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
