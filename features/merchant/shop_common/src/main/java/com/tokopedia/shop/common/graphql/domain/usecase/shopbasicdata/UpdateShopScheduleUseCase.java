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

            public static final String MAX_CLOSE_END = "253402275599"; //Epoch for 31 Dec 999

            @Override
            protected int getGraphQLRawResId() {
                return R.raw.gql_mutation_close_shop_schedule;
            }

            @Override
            protected HashMap<String, Object> createGraphQLVariable(RequestParams requestParams) {
                HashMap<String, Object> variables = new HashMap<>();
                int action = requestParams.getInt(ACTION, 0);
                variables.put(ACTION, action);

                // close end, to shop to start open
                String closeEnd = requestParams.getString(CLOSE_END, "");
                if (!TextUtils.isEmpty(closeEnd)) {
                    variables.put(CLOSE_END, String.valueOf(Long.parseLong(closeEnd) / 1000L));
                }

                boolean closeNow = requestParams.getBoolean(CLOSE_NOW, false);
                variables.put(CLOSE_NOW, closeNow);

                if (closeNow) {
                    // closeNow true must have end date; the business currently does not allow empty end date
                    if (TextUtils.isEmpty(closeEnd)) {
                        variables.put(CLOSE_END, MAX_CLOSE_END);
                    }
                } else { // open with schedule start
                    String closeStart = requestParams.getString(CLOSE_START, "");
                    if (!TextUtils.isEmpty(closeStart)) {
                        variables.put(CLOSE_START, String.valueOf(Long.parseLong(closeStart) / 1000L));
                    }
                }

                String closeNote = requestParams.getString(CLOSE_NOTE, "");
                variables.put(CLOSE_NOTE, closeNote);
                return variables;
            }
        };
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(new GraphQLSuccessMapper());
    }

    /**
     * update shop schedule
     *
     * @param action     1:to open; 0: to close
     * @param closeNow   true means close start = today
     * @param closeStart schedule for shop to close
     * @param closeEnd   schedule for shop to open
     * @param closeNote  close note for open
     */
    public static RequestParams createRequestParams(@ShopScheduleActionDef int action,
                                                    boolean closeNow,
                                                    String closeStart,
                                                    String closeEnd,
                                                    String closeNote) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(ACTION, action);
        requestParams.putBoolean(CLOSE_NOW, closeNow);
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
