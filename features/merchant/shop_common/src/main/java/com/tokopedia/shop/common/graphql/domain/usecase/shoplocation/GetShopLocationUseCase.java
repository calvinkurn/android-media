package com.tokopedia.shop.common.graphql.domain.usecase.shoplocation;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.shop.common.graphql.data.shoplocation.gql.ShopLocationQuery;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

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
                //TODO remove below, just for test.
//        .onErrorResumeNext((Throwable throwable) -> {
//                String jsonString = "{\"shopLocations\":{\"result\":[{\"id\":\"123\",\"name\":\"Toko 1\",\"address\":\"Jalan Kehidupan\",\"districtId\":54,\"districtName\":\"Setiabudi\",\"cityId\":65,\"cityName\":\"Jakarta Selatan\",\"stateId\":34,\"stateName\":\"DKI Jakarta\",\"postalCode\":41156,\"email\":\"gmail@tokopedia.com\",\"phone\":\"089989764583\",\"fax\":\"78765\"},{\"id\":\"456\",\"name\":\"Toko 2\",\"address\":\"Jalan Yang\",\"districtId\":54,\"districtName\":\"Palmerah\",\"cityId\":65,\"cityName\":\"Jakarta Barat\",\"stateId\":34,\"stateName\":\"DKI Jakarta\",\"postalCode\":41156,\"email\":\"gmail@tokopedia.com\",\"phone\":\"089689764583\",\"fax\":\"78765\"}],\"error\":{\"message\":\"error message\"}}}";
//                ShopLocationQuery response = new Gson().fromJson(jsonString, ShopLocationQuery.class);
//                return Observable.just(response).flatMap(new GraphQLResultMapper<>());
//        });

    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
