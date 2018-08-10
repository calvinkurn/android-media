package com.tokopedia.shop.common.graphql.domain.usecase.shopnotes;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.shop.common.graphql.data.shopnote.gql.ShopNoteQuery;
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetShopNotesUseCase extends UseCase<ArrayList<ShopNoteModel>> {
    private SingleGraphQLUseCase<ShopNoteQuery> graphQLUseCase;

    @Inject
    public GetShopNotesUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<ShopNoteQuery>(context, ShopNoteQuery.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_query_shop_notes;
            }
        };
    }

    @Override
    public Observable<ArrayList<ShopNoteModel>> createObservable(RequestParams requestParams) {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(new GraphQLResultMapper<>())
                //TODO remove below, just for test.
        .onErrorResumeNext(new Func1<Throwable, Observable<? extends ArrayList<ShopNoteModel>>>() {
            @Override
            public Observable<? extends ArrayList<ShopNoteModel>> call(Throwable throwable) {
                String jsonString = "{\"shopNotes\":{\"result\":[{\"id\":\"123\",\"title\":\"Catatan 1\",\"content\":\"<b>Isi Catatan 1</b>\",\"isTerms\":true,\"updateTime\":\"1530403200\"},{\"id\":\"123\",\"title\":\"Catatan 2\",\"content\":\"<b>Isi Catatan 2</b>\",\"isTerms\":false,\"updateTime\":\"1530403200\"}],\"error\":{\"message\":\"error message\"}}}";
                ShopNoteQuery response = new Gson().fromJson(jsonString, ShopNoteQuery.class);
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
