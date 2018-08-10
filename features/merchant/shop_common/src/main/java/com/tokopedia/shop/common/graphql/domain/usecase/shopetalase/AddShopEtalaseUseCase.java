package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.shopetalase.gql.AddShopEtalaseMutation;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class AddShopEtalaseUseCase extends UseCase<String> {
    public static final String NAME = "name";

    private SingleGraphQLUseCase<AddShopEtalaseMutation> graphQLUseCase;

    @Inject
    public AddShopEtalaseUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<AddShopEtalaseMutation>(context, AddShopEtalaseMutation.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_mutation_add_shop_etalase;
            }

            @Override
            protected HashMap<String, Object> createGraphQLVariable(RequestParams requestParams) {
                HashMap<String, Object> variables = new HashMap<>();
                String name = requestParams.getString(NAME, "");
                variables.put(NAME, name);
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
                        String jsonString = "{\"addShopShowcase\":{\"success\":true,\"message\":\"Success\"}}";
                        AddShopEtalaseMutation response = new Gson().fromJson(jsonString, AddShopEtalaseMutation.class);
                        return Observable.just(response).flatMap(new GraphQLSuccessMapper());
                    }
                });

    }

    public static RequestParams createRequestParams(String etalaseName) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(NAME, etalaseName);
        return requestParams;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
