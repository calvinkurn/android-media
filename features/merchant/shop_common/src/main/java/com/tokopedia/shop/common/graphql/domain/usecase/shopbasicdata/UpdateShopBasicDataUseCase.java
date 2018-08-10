package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataMutation;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class UpdateShopBasicDataUseCase extends UseCase<String> {
    public static final String TAGLINE = "tagline";
    public static final String DESCRIPTION = "description";
    public static final String LOGO_CODE = "logoCode";
    public static final String FILE_PATH = "filePath";
    public static final String FILE_NAME = "fileName";

    private SingleGraphQLUseCase<ShopBasicDataMutation> graphQLUseCase;

    @Inject
    public UpdateShopBasicDataUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<ShopBasicDataMutation>(context, ShopBasicDataMutation.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_mutation_shop_basic_data;
            }

            @Override
            protected HashMap<String, Object> createGraphQLVariable(RequestParams requestParams) {
                HashMap<String, Object> variables = new HashMap<>();
                String tagline = requestParams.getString(TAGLINE, "");
                if (!TextUtils.isEmpty(tagline)) {
                    variables.put(TAGLINE, tagline);
                }
                String description = requestParams.getString(DESCRIPTION, "");
                if (!TextUtils.isEmpty(description)) {
                    variables.put(DESCRIPTION, description);
                }
                String logoCode = requestParams.getString(LOGO_CODE, "");
                if (!TextUtils.isEmpty(logoCode)) {
                    variables.put(LOGO_CODE, logoCode);
                }
                String filePath = requestParams.getString(FILE_PATH, "");
                if (!TextUtils.isEmpty(filePath)) {
                    variables.put(FILE_PATH, filePath);
                }
                String fileName = requestParams.getString(FILE_NAME, "");
                if (!TextUtils.isEmpty(fileName)) {
                    variables.put(FILE_NAME, fileName);
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
                        String jsonString = "{\"updateShopInfo\":{\"success\":true,\"message\":\"Success\"}}";
                        ShopBasicDataMutation response = new Gson().fromJson(jsonString, ShopBasicDataMutation.class);
                        return Observable.just(response).flatMap(new GraphQLSuccessMapper());
                    }
                });

    }

    public static RequestParams createRequestParams(String tagline, String description,
                                                    //optional, either code only, or (filePath & fileName) only
                                                    String logoCode,
                                                    String filePath, String fileName) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TAGLINE, tagline);
        requestParams.putString(DESCRIPTION, description);
        requestParams.putString(LOGO_CODE, logoCode);
        requestParams.putString(FILE_PATH, filePath);
        requestParams.putString(FILE_NAME, fileName);
        return requestParams;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
