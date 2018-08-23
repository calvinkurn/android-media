package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.shop.common.graphql.data.shopetalase.gql.ShopEtalaseQuery;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetShopEtalaseUseCase extends UseCase<ArrayList<ShopEtalaseModel>> {

    public static final String WITH_DEFAULT = "withDefault";
    private SingleGraphQLUseCase<ShopEtalaseQuery> graphQLUseCase;

    @Inject
    public GetShopEtalaseUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<ShopEtalaseQuery>(context, ShopEtalaseQuery.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_query_shop_etalase;
            }

            @Override
            protected HashMap<String, Object> createGraphQLVariable(RequestParams requestParams) {
                HashMap<String, Object> variables = new HashMap<>();
                Boolean withDefault = requestParams.getBoolean(WITH_DEFAULT, true);
                variables.put(WITH_DEFAULT, withDefault);
                return variables;
            }
        };
    }

    @Override
    public Observable<ArrayList<ShopEtalaseModel>> createObservable(RequestParams requestParams) {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(new GraphQLResultMapper<>());
                //TODO remove below, just for test.
//                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ArrayList<ShopEtalaseModel>>>() {
//                    @Override
//                    public Observable<? extends ArrayList<ShopEtalaseModel>> call(Throwable throwable) {
//                        String jsonString = "{\"shopShowcases\":{\"result\":[{\"id\":\"123\",\"name\":\"Atasan\",\"count\":100,\"type\":-1},{\"id\":\"456\",\"name\":\"Bawahan\",\"count\":50,\"type\":-1},{\"id\":\"123\",\"name\":\"Atasan2\",\"count\":0,\"type\":-1},{\"id\":\"456\",\"name\":\"Bawahan2\",\"count\":0,\"type\":1},{\"id\":\"123\",\"name\":\"Atasan3\",\"count\":23,\"type\":1},{\"id\":\"456\",\"name\":\"Bawahan3\",\"count\":0,\"type\":1},{\"id\":\"123\",\"name\":\"Atasan4\",\"count\":123,\"type\":1},{\"id\":\"456\",\"name\":\"Bawahan4\",\"count\":0,\"type\":1}],\"error\":{\"message\":\"error message\"}}}";
//                        ShopEtalaseQuery response = new Gson().fromJson(jsonString, ShopEtalaseQuery.class);
//                        return Observable.just(response).flatMap(new GraphQLResultMapper<>());
//                    }
//                });
    }

    public static RequestParams createRequestParams(boolean withDefault) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putBoolean(WITH_DEFAULT, withDefault);
        return requestParams;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
