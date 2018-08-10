package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.constant.ShopScheduleActionDef;
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper;
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.CloseShopScheduleMutation;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class UpdateShopScheduleUseCase extends UseCase<String> {
    public static final String ACTION = "action";
    public static final String CLOSE_START = "closeStart";
    public static final String CLOSE_END = "closeEnd";
    public static final String CLOSE_NOW = "closeNow";
    public static final String CLOSE_NOTE = "closeNote";

    private SingleGraphQLUseCase<CloseShopScheduleMutation> graphQLUseCase;

    @Inject
    public UpdateShopScheduleUseCase(@ApplicationContext Context context) {
        graphQLUseCase = new SingleGraphQLUseCase<CloseShopScheduleMutation>(context, CloseShopScheduleMutation.class) {
            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_mutation_close_shop_schedule;
            }

            @Override
            protected HashMap<String, Object> createGraphQLVariable(RequestParams requestParams) {
                HashMap<String, Object> variables = new HashMap<>();
                int action = requestParams.getInt(ACTION, 0);
                variables.put(ACTION, action);

                String closeStart = requestParams.getString(CLOSE_START, "");
                if (!TextUtils.isEmpty(closeStart)) {
                    variables.put(CLOSE_START, closeStart);
                }
                String closeEnd = requestParams.getString(CLOSE_END, "");
                if (!TextUtils.isEmpty(closeEnd)) {
                    variables.put(CLOSE_END, closeEnd);
                }
                if (action == ShopScheduleActionDef.CLOSED) {
                    if (!TextUtils.isEmpty(closeStart) &&
                            !TextUtils.isEmpty(closeEnd)) {
                        variables.put(CLOSE_NOW, false);
                    } else {
                        variables.put(CLOSE_NOW, true);
                    }
                }
                String closeNote = requestParams.getString(CLOSE_NOTE, "");
                if (!TextUtils.isEmpty(closeNote)) {
                    variables.put(CLOSE_NOTE, closeNote);
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
                        String jsonString = "{\"closeShopSchedule\":{\"success\":true,\"message\":\"Success\"}}";
                        CloseShopScheduleMutation response = new Gson().fromJson(jsonString, CloseShopScheduleMutation.class);
                        return Observable.just(response).flatMap(new GraphQLSuccessMapper());
                    }
                });

    }


    public static RequestParams createRequestParams(@ShopScheduleActionDef int action,
                                                    String closeStart,
                                                    String closeEnd,
                                                    String closeNote) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(ACTION, action);
        requestParams.putString(CLOSE_START, closeStart);
        requestParams.putString(CLOSE_END, closeEnd);
        requestParams.putString(CLOSE_NOTE, closeNote);
        return requestParams;
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        graphQLUseCase.unsubscribe();
    }
}
