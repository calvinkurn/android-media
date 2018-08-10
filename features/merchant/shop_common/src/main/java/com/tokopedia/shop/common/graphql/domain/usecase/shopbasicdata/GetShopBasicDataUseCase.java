package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataMapper;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataViewModel;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataQuery;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetShopBasicDataUseCase extends UseCase<ShopBasicDataViewModel> {
    private SingleGraphQLUseCase<ShopBasicDataQuery> graphQLUseCase;

    @Inject
    public GetShopBasicDataUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<ShopBasicDataQuery>(context, ShopBasicDataQuery.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_query_shop_basic_data;
            }
        };
    }

    @Override
    public Observable<ShopBasicDataViewModel> createObservable(RequestParams requestParams) {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(new GraphQLResultMapper<>())
                //TODO remove below, just for test.
        .onErrorResumeNext(new Func1<Throwable, Observable<? extends ShopBasicDataModel>>() {
            @Override
            public Observable<? extends ShopBasicDataModel> call(Throwable throwable) {
                String jsonString = "{\"shopBasicData\":{\"result\":{\"domain\":\"tokoku\",\"name\":\"Toko Ku\",\"status\":1,\"closeSchedule\":\"1530403200\",\"openSchedule\":\"1530403300\",\"tagline\":\"awesome\",\"description\":\"Toko Awesome\",\"logo\":\"https://cdn-tokopedia.com/logo.png\",\"level\":2,\"expired\":\"1530403200\"}}}";
                ShopBasicDataQuery response = new Gson().fromJson(jsonString, ShopBasicDataQuery.class);
                return Observable.just(response).flatMap(new GraphQLResultMapper<>());
            }
        })
        .map(new ShopBasicDataMapper());

    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
