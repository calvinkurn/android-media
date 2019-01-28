package com.tokopedia.saldodetails.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.response.model.GqlHoldSaldoBalanceResponse;
import com.tokopedia.saldodetails.response.model.GqlSaldoBalanceResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.saldodetails.response.model.SummaryDepositParam.PARAM_IS_SELLER;

public class GetSaldoBalanceUseCase {

    private static final String GET_SALDO_BALANCE = "SaldoQuery";
    private static final String PARAM_SALDO_TYPE = "type";
    private static final String VALUE_SALDO_TYPE_USABLE = "usable";
    private static final String VALUE_SALDO_TYPE_HOLD = "hold";
    private GraphqlUseCase graphqlUseCase;
    private Context context;
    private boolean isRequesting;
    private boolean isSeller;


    @Inject
    public GetSaldoBalanceUseCase(@ApplicationContext Context context) {
        graphqlUseCase = new GraphqlUseCase();
        this.context = context;
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public void setSellerStatus(boolean status) {
        this.isSeller = status;
    }

    public void execute(Subscriber<GraphqlResponse> subscriber) {
        graphqlUseCase.clearRequest();
        setRequesting(true);

        List<GraphqlRequest> graphqlRequestList = new ArrayList<>();

        HashMap<String, Object> usableRequestMap = new HashMap<>();
        usableRequestMap.put(PARAM_SALDO_TYPE, VALUE_SALDO_TYPE_USABLE);
        usableRequestMap.put(PARAM_IS_SELLER, isSeller);
        GraphqlRequest graphqlRequestForUsable = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_saldo_balance),
                GqlSaldoBalanceResponse.class,
                usableRequestMap, GET_SALDO_BALANCE);

        graphqlRequestList.add(graphqlRequestForUsable);

        HashMap<String, Object> holdRequestMap = new HashMap<>();
        holdRequestMap.put(PARAM_SALDO_TYPE, VALUE_SALDO_TYPE_HOLD);
        holdRequestMap.put(PARAM_IS_SELLER, isSeller);
        GraphqlRequest graphqlRequestForHold = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_saldo_balance),
                GqlHoldSaldoBalanceResponse.class,
                holdRequestMap, GET_SALDO_BALANCE);

        graphqlRequestList.add(graphqlRequestForHold);

        graphqlUseCase.addRequests(graphqlRequestList);

        graphqlUseCase.execute(subscriber);
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public void setRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }
}
