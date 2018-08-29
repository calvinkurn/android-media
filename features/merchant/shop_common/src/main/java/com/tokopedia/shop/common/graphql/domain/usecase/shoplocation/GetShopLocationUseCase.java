package com.tokopedia.shop.common.graphql.domain.usecase.shoplocation;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.GraphQLDataError;
import com.tokopedia.shop.common.graphql.data.GraphQLResult;
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.shop.common.graphql.data.shoplocation.gql.ShopLocationQuery;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetShopLocationUseCase extends UseCase<List<ShopLocationModel>> {
    private SingleGraphQLUseCase<ShopLocationQuery> graphQLUseCase;

    @Inject
    public GetShopLocationUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<ShopLocationQuery>(context, ShopLocationQuery.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_query_shop_location;
            }
        };
    }

    @Override
    public Observable<List<ShopLocationModel>> createObservable(RequestParams requestParams) {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(new GraphQLResultMapper<>());

    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
