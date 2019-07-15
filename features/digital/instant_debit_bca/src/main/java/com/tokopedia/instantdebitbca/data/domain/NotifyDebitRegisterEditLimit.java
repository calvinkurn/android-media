package com.tokopedia.instantdebitbca.data.domain;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.instantdebitbca.R;
import com.tokopedia.instantdebitbca.data.data.ResponseDebitRegisterBca;
import com.tokopedia.instantdebitbca.data.view.model.NotifyDebitRegisterBca;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class NotifyDebitRegisterEditLimit extends NotifyDebitRegisterBcaUseCase{
    @Inject
    public NotifyDebitRegisterEditLimit(@ApplicationContext Context context, GraphqlUseCase graphqlUseCase) {
        super(context, graphqlUseCase);
    }

    @Override
    public Observable<NotifyDebitRegisterBca> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<GraphqlResponse>>() {
                    @Override
                    public Observable<GraphqlResponse> call(RequestParams requestParams) {
                        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.idb_edit_limit_mutation);
                        Map<String, Object> variable = requestParams.getParameters();
                        if (!TextUtils.isEmpty(query)) {
                            graphqlUseCase.clearRequest();
                            graphqlUseCase.addRequest(new GraphqlRequest(query, ResponseDebitRegisterBca.class, variable));
                            return graphqlUseCase.createObservable(null);
                        }
                        return Observable.error(new Exception("Query variable are empty"));
                    }
                })
                .map(new Func1<GraphqlResponse, ResponseDebitRegisterBca>() {
                    @Override
                    public ResponseDebitRegisterBca call(GraphqlResponse graphqlResponse) {
                        return graphqlResponse.getData(ResponseDebitRegisterBca.class);
                    }
                })
                .map(it -> {
                    NotifyDebitRegisterBca notifyDebitRegisterBca = new NotifyDebitRegisterBca();
                    notifyDebitRegisterBca.setCallbackUrl(it.getNotifyDebitRegister().getDebitRegister().getCallbackUrl());
                    return notifyDebitRegisterBca;
                });
    }

    public RequestParams createRequestParam(String debitDataString, String deviceId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(MERCHANT_CODE, MERCHANT_DATA);
        requestParams.putString(BANK_CODE, BANK_DATA);
        requestParams.putString(CALLBACK_URL, CALLBACK_DATA);
        requestParams.putString(SIGNATURE, "");
        requestParams.putString(NotifyDebitRegisterBcaUseCase.ACTION, NotifyDebitRegisterBcaUseCase.UPDATE);
        requestParams.putObject(DEBIT_DATA, debitDataString);
        return requestParams;
    }
}
