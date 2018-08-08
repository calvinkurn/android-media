package com.tokopedia.shop.common.graphql.domain.usecase;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper;
import com.tokopedia.shop.common.graphql.model.shopetalase.ShopEtalase;
import com.tokopedia.shop.common.graphql.model.shopetalase.ShopEtalaseResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

public class GetShopEtalaseUseCase extends UseCase<ArrayList<ShopEtalase>> {
    private SingleGraphQLUseCase<ShopEtalaseResponse> graphQLUseCase;
    public GetShopEtalaseUseCase(Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<ShopEtalaseResponse>(context, ShopEtalaseResponse.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_query_shop_etalase;
            }
        };
    }

    @Override
    public Observable<ArrayList<ShopEtalase>> createObservable(RequestParams requestParams) {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(new GraphQLResultMapper<>())
                //TODO remove below, just for test.
        .onErrorResumeNext(new Func1<Throwable, Observable<? extends ArrayList<ShopEtalase>>>() {
            @Override
            public Observable<? extends ArrayList<ShopEtalase>> call(Throwable throwable) {
                String jsonString = "{\"shopShowcases\":{\"result\":[{\"id\":\"123\",\"name\":\"Atasan\",\"count\":100},{\"id\":\"456\",\"name\":\"Bawahan\",\"count\":50}],\"error\":{\"message\":\"error message\"}}}";
                ShopEtalaseResponse response = new Gson().fromJson(jsonString, ShopEtalaseResponse.class);
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
