package com.tokopedia.shop.common.graphql.domain.usecase.shoplocation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.shoplocation.gql.AddShopLocationMutation;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class AddShopLocationUseCase extends UseCase<String> {
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String DISTRICT_ID = "districtId";
    public static final String CITY_ID = "cityId";
    public static final String STATE_ID = "stateId";
    public static final String POSTAL_CODE = "postalCode";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String FAX = "fax";

    private SingleGraphQLUseCase<AddShopLocationMutation> graphQLUseCase;

    @Inject
    public AddShopLocationUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<AddShopLocationMutation>(context, AddShopLocationMutation.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_mutation_add_shop_location;
            }

            @Override
            protected HashMap<String, Object> createGraphQLVariable(RequestParams requestParams) {
                HashMap<String, Object> variables = new HashMap<>();
                variables.put(NAME, requestParams.getString(NAME, ""));
                variables.put(ADDRESS, requestParams.getString(ADDRESS, ""));
                variables.put(DISTRICT_ID, requestParams.getInt(DISTRICT_ID, 0));
                variables.put(CITY_ID, requestParams.getInt(CITY_ID, 0));
                variables.put(STATE_ID, requestParams.getInt(STATE_ID, 0));
                variables.put(POSTAL_CODE, requestParams.getInt(POSTAL_CODE, 0));
                String email = requestParams.getString(EMAIL, "");
                if (!TextUtils.isEmpty(email)) {
                    variables.put(EMAIL, email);
                }
                String phone = requestParams.getString(PHONE, "");
                if (!TextUtils.isEmpty(phone)) {
                    variables.put(PHONE, phone);
                }
                String fax = requestParams.getString(FAX, "");
                if (!TextUtils.isEmpty(fax)) {
                    variables.put(FAX, fax);
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
                        String jsonString = "{\"addShopLocation\":{\"success\":true,\"message\":\"Success\"}}";
                        AddShopLocationMutation response = new Gson().fromJson(jsonString, AddShopLocationMutation.class);
                        return Observable.just(response).flatMap(new GraphQLSuccessMapper());
                    }
                });

    }

    public static RequestParams createRequestParams(@NonNull String name,@NonNull String address,
                                                    int districtId, int cityId,
                                                    int stateId, int postalCode,
                                                    @Nullable String email, @Nullable String phone, @Nullable String fax) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(NAME, name);
        requestParams.putString(ADDRESS, address);
        requestParams.putInt(DISTRICT_ID, districtId);
        requestParams.putInt(CITY_ID, cityId);
        requestParams.putInt(STATE_ID, stateId);
        requestParams.putInt(POSTAL_CODE, postalCode);
        requestParams.putString(EMAIL, email);
        requestParams.putString(PHONE, phone);
        requestParams.putString(FAX, fax);
        return requestParams;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
