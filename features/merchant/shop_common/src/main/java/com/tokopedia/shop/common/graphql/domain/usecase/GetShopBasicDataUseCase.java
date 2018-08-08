package com.tokopedia.shop.common.graphql.domain.usecase;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper;
import com.tokopedia.shop.common.graphql.model.shopbasicdata.ShopBasicData;
import com.tokopedia.shop.common.graphql.model.shopbasicdata.ShopBasicDataResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

public class GetShopBasicDataUseCase extends UseCase<ShopBasicData> {
    private SingleGraphQLUseCase<ShopBasicDataResponse> graphQLUseCase;
    public GetShopBasicDataUseCase(Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<ShopBasicDataResponse>(context, ShopBasicDataResponse.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_query_shop_basic_data;
            }
        };
    }

    @Override
    public Observable<ShopBasicData> createObservable(RequestParams requestParams) {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(new GraphQLResultMapper<>())
                //TODO remove below, just for test.
        .onErrorResumeNext(new Func1<Throwable, Observable<? extends ShopBasicData>>() {
            @Override
            public Observable<? extends ShopBasicData> call(Throwable throwable) {
                String jsonString = "{\"shopBasicData\":{\"result\":{\"domain\":\"tokoku\",\"name\":\"Toko Ku\",\"status\":1,\"closeSchedule\":\"1530403200\",\"openSchedule\":\"1530403300\",\"tagline\":\"awesome\",\"description\":\"Toko Awesome\",\"logo\":\"https://cdn-tokopedia.com/logo.png\",\"level\":2,\"expired\":\"1530403200\"}}}";
                ShopBasicDataResponse response = new Gson().fromJson(jsonString, ShopBasicDataResponse.class);
                return Observable.just(response).flatMap(new GraphQLResultMapper<>());
            }
        });

    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
