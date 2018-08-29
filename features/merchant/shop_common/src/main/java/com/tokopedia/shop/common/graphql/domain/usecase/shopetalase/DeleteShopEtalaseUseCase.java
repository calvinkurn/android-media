package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.shopetalase.gql.DeleteShopEtalaseMutation;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class DeleteShopEtalaseUseCase extends UseCase<String> {
    public static final String ID = "id";

    private SingleGraphQLUseCase<DeleteShopEtalaseMutation> graphQLUseCase;

    @Inject
    public DeleteShopEtalaseUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<DeleteShopEtalaseMutation>(context, DeleteShopEtalaseMutation.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_mutation_delete_shop_etalase;
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
                .flatMap(new GraphQLSuccessMapper());
    }

    public static RequestParams createRequestParams(String etalaseId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ID, etalaseId);
        return requestParams;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
