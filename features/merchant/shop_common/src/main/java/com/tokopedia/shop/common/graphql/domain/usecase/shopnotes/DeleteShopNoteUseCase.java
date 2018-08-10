package com.tokopedia.shop.common.graphql.domain.usecase.shopnotes;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.shopnote.gql.DeleteShopNoteMutation;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class DeleteShopNoteUseCase extends UseCase<String> {
    public static final String ID = "id";

    private SingleGraphQLUseCase<DeleteShopNoteMutation> graphQLUseCase;

    @Inject
    public DeleteShopNoteUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<DeleteShopNoteMutation>(context, DeleteShopNoteMutation.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_mutation_delete_shop_note;
            }

            @Override
            protected HashMap<String, Object> createGraphQLVariable(RequestParams requestParams) {
                HashMap<String, Object> variables = new HashMap<>();
                String name = requestParams.getString(ID, "");
                variables.put(ID, name);
                return variables;
            }
        };
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(new GraphQLSuccessMapper())
                //TODO remove below, just for test.
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends String>>() {
                    @Override
                    public Observable<? extends String> call(Throwable throwable) {
                        String jsonString = "{\"deleteShopNote\":{\"success\":true,\"message\":\"Success\"}}";
                        DeleteShopNoteMutation response = new Gson().fromJson(jsonString, DeleteShopNoteMutation.class);
                        return Observable.just(response).flatMap(new GraphQLSuccessMapper());
                    }
                });

    }

    public static RequestParams createRequestParams(String noteId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ID, noteId);
        return requestParams;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
