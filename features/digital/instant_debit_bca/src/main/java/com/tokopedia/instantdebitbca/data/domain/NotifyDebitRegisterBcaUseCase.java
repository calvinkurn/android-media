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
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class NotifyDebitRegisterBcaUseCase extends UseCase<NotifyDebitRegisterBca> {

    public static final String XCOID = "xcoID";
    public static final String CREDENTIAL_TYPE = "credentialType";
    public static final String CREDENTIAL_NO = "credentialNo";
    public static final String MAX_LIMIT = "maxLimit";
    public static final String USER_AGENT = "user_agent";
    public static final String IP_ADDRESS = "ip_address";
    public static final String ACTION = "action";
    public static final String UPDATE = "update";

    public static final String MERCHANT_CODE = "merchantCode";
    public static final String DEVICE_ID = "deviceID";
    public static final String BANK_CODE = "bankCode";
    public static final String CALLBACK_URL = "callbackUrl";
    public static final String SIGNATURE = "signature";
    public static final String DEBIT_DATA = "debitData";
    public static final String MERCHANT_DATA = AuthUtil.KEY.INSTANT_DEBIT_BCA_MERCHANT_CODE;
    public static final String BANK_DATA = AuthUtil.KEY.INSTANT_DEBIT_BCA_BANK_CODE;
    public static final String CALLBACK_DATA = "https://tokopedia.com";


    private Context context;
    private GraphqlUseCase graphqlUseCase;

    @Inject
    public NotifyDebitRegisterBcaUseCase(@ApplicationContext Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<NotifyDebitRegisterBca> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<GraphqlResponse>>() {
                    @Override
                    public Observable<GraphqlResponse> call(RequestParams requestParams) {
                        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.idb_notify_debit_register_mutation);
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
        requestParams.putString(DEVICE_ID, deviceId);
        requestParams.putString(BANK_CODE, BANK_DATA);
        requestParams.putString(CALLBACK_URL, CALLBACK_DATA);
        requestParams.putString(SIGNATURE, "");
        requestParams.putString(NotifyDebitRegisterBcaUseCase.ACTION, NotifyDebitRegisterBcaUseCase.UPDATE);
        requestParams.putObject(DEBIT_DATA, debitDataString);
        return requestParams;
    }
}
