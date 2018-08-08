package com.tokopedia.shop.common.graphql.domain.usecase;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper;
import com.tokopedia.shop.common.graphql.model.shoplocation.ShopLocation;
import com.tokopedia.shop.common.graphql.model.shoplocation.ShopLocationResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

public class GetShopLocationUseCase extends UseCase<ArrayList<ShopLocation>> {
    private SingleGraphQLUseCase<ShopLocationResponse> graphQLUseCase;
    public GetShopLocationUseCase(Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<ShopLocationResponse>(context, ShopLocationResponse.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_query_shop_location;
            }
        };
    }

    @Override
    public Observable<ArrayList<ShopLocation>> createObservable(RequestParams requestParams) {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(new GraphQLResultMapper<>())
                //TODO remove below, just for test.
        .onErrorResumeNext(new Func1<Throwable, Observable<? extends ArrayList<ShopLocation>>>() {
            @Override
            public Observable<? extends ArrayList<ShopLocation>> call(Throwable throwable) {
                String jsonString = "{\"shopLocations\":{\"result\":[{\"id\":\"123\",\"name\":\"Toko 1\",\"address\":\"Jalan Kehidupan\",\"districtId\":54,\"districtName\":\"Setiabudi\",\"cityId\":65,\"cityName\":\"Jakarta Selatan\",\"stateId\":34,\"stateName\":\"DKI Jakarta\",\"postalCode\":41156,\"email\":\"gmail@tokopedia.com\",\"phone\":\"089989764583\",\"fax\":\"78765\"},{\"id\":\"456\",\"name\":\"Toko 2\",\"address\":\"Jalan Yang\",\"districtId\":54,\"districtName\":\"Palmerah\",\"cityId\":65,\"cityName\":\"Jakarta Barat\",\"stateId\":34,\"stateName\":\"DKI Jakarta\",\"postalCode\":41156,\"email\":\"gmail@tokopedia.com\",\"phone\":\"089689764583\",\"fax\":\"78765\"}],\"error\":{\"message\":\"error message\"}}}";
                ShopLocationResponse response = new Gson().fromJson(jsonString, ShopLocationResponse.class);
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
