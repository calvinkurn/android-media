package com.tokopedia.shop.common.graphql.domain.usecase.shopnotes;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.shopnote.gql.AddShopNoteMutation;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class AddShopNoteUseCase extends UseCase<String> {
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String IS_TERMS = "isTerms";

    private SingleGraphQLUseCase<AddShopNoteMutation> graphQLUseCase;

    @Inject
    public AddShopNoteUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<AddShopNoteMutation>(context, AddShopNoteMutation.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_mutation_add_shop_note;
            }

            @Override
            protected HashMap<String, Object> createGraphQLVariable(RequestParams requestParams) {
                HashMap<String, Object> variables = new HashMap<>();
                variables.put(TITLE, requestParams.getString(TITLE, ""));
                variables.put(CONTENT, requestParams.getString(CONTENT, ""));
                variables.put(IS_TERMS, requestParams.getBoolean(IS_TERMS, false));
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
                        String jsonString = "{\"addShopNote\":{\"success\":true,\"message\":\"Success\"}}";
                        AddShopNoteMutation response = new Gson().fromJson(jsonString, AddShopNoteMutation.class);
                        return Observable.just(response).flatMap(new GraphQLSuccessMapper());
                    }
                });

    }

    public static RequestParams createRequestParams(@NonNull String title,@NonNull String content,
                                                    boolean isTerms) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TITLE, title);
        requestParams.putString(CONTENT, content);
        requestParams.putBoolean(IS_TERMS, isTerms);
        return requestParams;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
