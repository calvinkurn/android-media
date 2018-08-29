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
