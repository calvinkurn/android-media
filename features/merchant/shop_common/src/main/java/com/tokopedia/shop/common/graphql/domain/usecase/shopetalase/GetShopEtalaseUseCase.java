package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseMapper;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseViewModel;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.shop.common.graphql.data.shopetalase.gql.ShopEtalaseQuery;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetShopEtalaseUseCase extends UseCase<List<ShopEtalaseViewModel>> {
    private SingleGraphQLUseCase<ShopEtalaseQuery> graphQLUseCase;

    @Inject
    public GetShopEtalaseUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<ShopEtalaseQuery>(context, ShopEtalaseQuery.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_query_shop_etalase;
            }
        };
    }

    @Override
    public Observable<List<ShopEtalaseViewModel>> createObservable(RequestParams requestParams) {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(new GraphQLResultMapper<>())
                //TODO remove below, just for test.
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ArrayList<ShopEtalaseModel>>>() {
                    @Override
                    public Observable<? extends ArrayList<ShopEtalaseModel>> call(Throwable throwable) {
                        String jsonString = "{\"shopShowcases\":{\"result\":[{\"id\":\"123\",\"name\":\"Atasan\",\"count\":100},{\"id\":\"456\",\"name\":\"Bawahan\",\"count\":50}],\"error\":{\"message\":\"error message\"}}}";
                        ShopEtalaseQuery response = new Gson().fromJson(jsonString, ShopEtalaseQuery.class);
                        return Observable.just(response).flatMap(new GraphQLResultMapper<>());
                    }
                }).flatMap(new Func1<ArrayList<ShopEtalaseModel>, Observable<ShopEtalaseModel>>() {
                    @Override
                    public Observable<ShopEtalaseModel> call(ArrayList<ShopEtalaseModel> shopEtalaseModels) {
                        return Observable.from(shopEtalaseModels);
                    }
                })
                .map(new ShopEtalaseMapper())
                .toList();

    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
