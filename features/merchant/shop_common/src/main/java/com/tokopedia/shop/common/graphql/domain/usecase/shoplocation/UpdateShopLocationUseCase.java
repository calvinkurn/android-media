package com.tokopedia.shop.common.graphql.domain.usecase.shoplocation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.graphql.data.shoplocation.gql.UpdateShopLocationMutation;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class UpdateShopLocationUseCase extends UseCase<String> {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String DISTRICT_ID = "districtId";
    private static final String CITY_ID = "cityId";
    private static final String STATE_ID = "stateId";
    private static final String POSTAL_CODE = "postalCode";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";
    private static final String FAX = "fax";

    private SingleGraphQLUseCase<UpdateShopLocationMutation> graphQLUseCase;

    @Inject
    public UpdateShopLocationUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<UpdateShopLocationMutation>(context, UpdateShopLocationMutation.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_mutation_update_shop_location;
            }

            @Override
            protected HashMap<String, Object> createGraphQLVariable(RequestParams requestParams) {
                HashMap<String, Object> variables = new HashMap<>();
                variables.put(ID, requestParams.getString(ID, ""));
                String name = requestParams.getString(NAME, "");
                if (!TextUtils.isEmpty(name)) {
                    variables.put(NAME, name);
                }
                String address = requestParams.getString(ADDRESS, "");
                if (!TextUtils.isEmpty(address)) {
                    variables.put(ADDRESS, address);
                }
                int districtId = requestParams.getInt(DISTRICT_ID, 0);
                if (districtId > 0) {
                    variables.put(DISTRICT_ID, districtId);
                }
                int cityId = requestParams.getInt(CITY_ID, 0);
                if (cityId > 0) {
                    variables.put(CITY_ID, cityId);
                }
                int stateId = requestParams.getInt(STATE_ID, 0);
                if (stateId > 0) {
                    variables.put(STATE_ID, stateId);
                }
                int postalCode = requestParams.getInt(POSTAL_CODE, 0);
                if (postalCode > 0) {
                    variables.put(POSTAL_CODE, postalCode);
                }
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
                        String jsonString = "{\"updateShopLocation\":{\"success\":true,\"message\":\"Success\"}}";
                        UpdateShopLocationMutation response = new Gson().fromJson(jsonString, UpdateShopLocationMutation.class);
                        return Observable.just(response).flatMap(new GraphQLSuccessMapper());
                    }
                });

    }

    public static RequestParams createRequestParams(@NonNull String id,
                                                    @Nullable String name,@Nullable String address,
                                                    int districtId, int cityId,
                                                    int stateId, int postalCode,
                                                    @Nullable String email, @Nullable String phone, @Nullable String fax) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ID, id);
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
