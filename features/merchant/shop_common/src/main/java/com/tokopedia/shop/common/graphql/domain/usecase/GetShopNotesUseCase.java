package com.tokopedia.shop.common.graphql.domain.usecase;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper;
import com.tokopedia.shop.common.graphql.model.shopnote.ShopNoteResponse;
import com.tokopedia.shop.common.graphql.model.shopnote.ShopNotes;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

public class GetShopNotesUseCase extends UseCase<ArrayList<ShopNotes>> {
    private SingleGraphQLUseCase<ShopNoteResponse> graphQLUseCase;
    public GetShopNotesUseCase(Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<ShopNoteResponse>(context, ShopNoteResponse.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_query_shop_notes;
            }
        };
    }

    @Override
    public Observable<ArrayList<ShopNotes>> createObservable(RequestParams requestParams) {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(new GraphQLResultMapper<>())
                //TODO remove below, just for test.
        .onErrorResumeNext(new Func1<Throwable, Observable<? extends ArrayList<ShopNotes>>>() {
            @Override
            public Observable<? extends ArrayList<ShopNotes>> call(Throwable throwable) {
                String jsonString = "{\"shopNotes\":{\"result\":[{\"id\":\"123\",\"title\":\"Catatan 1\",\"content\":\"<b>Isi Catatan 1</b>\",\"isTerms\":true,\"updateTime\":\"1530403200\"},{\"id\":\"123\",\"title\":\"Catatan 2\",\"content\":\"<b>Isi Catatan 2</b>\",\"isTerms\":false,\"updateTime\":\"1530403200\"}],\"error\":{\"message\":\"error message\"}}}";
                ShopNoteResponse response = new Gson().fromJson(jsonString, ShopNoteResponse.class);
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
