package com.tokopedia.shop.common.graphql.domain.usecase.shopnotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.shopnote.gql.AddShopNoteMutation;
import com.tokopedia.shop.common.graphql.data.shopnote.gql.UpdateShopNoteMutation;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class UpdateShopNoteUseCase extends UseCase<String> {
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";

    private SingleGraphQLUseCase<AddShopNoteMutation> graphQLUseCase;

    @Inject
    public UpdateShopNoteUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<AddShopNoteMutation>(context, AddShopNoteMutation.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_mutation_update_shop_note;
            }

            @Override
            protected HashMap<String, Object> createGraphQLVariable(RequestParams requestParams) {
                HashMap<String, Object> variables = new HashMap<>();
                variables.put(ID, requestParams.getString(ID, ""));
                String title = requestParams.getString(TITLE, "");
                if (!TextUtils.isEmpty(title)) {
                    variables.put(TITLE, title);
                }
                String content = requestParams.getString(CONTENT, "");
                if (!TextUtils.isEmpty(content)) {
                    variables.put(CONTENT, content);
                }
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
                        String jsonString = "{\"updateShopNote\":{\"success\":true,\"message\":\"Success\"}}";
                        UpdateShopNoteMutation response = new Gson().fromJson(jsonString, UpdateShopNoteMutation.class);
                        return Observable.just(response).flatMap(new GraphQLSuccessMapper());
                    }
                });

    }

    public static RequestParams createRequestParams(@NonNull String id,
                                                    @Nullable String title,
                                                    @Nullable String content) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ID, id);
        requestParams.putString(TITLE, title);
        requestParams.putString(CONTENT, content);
        return requestParams;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
