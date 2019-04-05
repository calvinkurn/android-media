package com.tokopedia.tokocash.pendingcashback.domain;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.pendingcashback.data.PendingCashbackEntity;
import com.tokopedia.tokocash.pendingcashback.data.PendingCashbackMapper;
import com.tokopedia.tokocash.pendingcashback.data.ResponsePendingCashback;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/7/18.
 */

public class GetPendingCasbackUseCase extends UseCase<PendingCashback> {

    private static final String USERID = "userID";
    private static final String MSISDN = "msisdn";

    private GraphqlUseCase graphqlUseCase;
    private Context context;
    private PendingCashbackMapper pendingCashbackMapper;
    private UserSession userSession;

    @Inject
    public GetPendingCasbackUseCase(@ApplicationContext Context context,
                                    GraphqlUseCase graphqlUseCase,
                                    PendingCashbackMapper pendingCashbackMapper,
                                    UserSession userSession) {
        this.graphqlUseCase = graphqlUseCase;
        this.context = context;
        this.pendingCashbackMapper = pendingCashbackMapper;
        this.userSession = userSession;
    }

    @Override
    public Observable<PendingCashback> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<GraphqlResponse>>() {
                    @Override
                    public Observable<GraphqlResponse> call(RequestParams requestParams) {
                        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.wallet_pending_cashback_query);
                        Map<String, Object> variables = new HashMap<>();
                        variables.put(USERID, Integer.valueOf(userSession.getUserId()));
                        variables.put(MSISDN, userSession.getPhoneNumber());

                        if (!TextUtils.isEmpty(query)) {
                            GraphqlRequest request = new GraphqlRequest(query, ResponsePendingCashback.class, variables, false);
                            graphqlUseCase.clearRequest();
                            graphqlUseCase.addRequest(request);
                            return graphqlUseCase.createObservable(null);
                        }
                        return Observable.error(new Exception("Query and/or variable are empty."));
                    }
                })
                .map(new Func1<GraphqlResponse, ResponsePendingCashback>() {
                    @Override
                    public ResponsePendingCashback call(GraphqlResponse graphqlResponse) {
                        return graphqlResponse.getData(ResponsePendingCashback.class);
                    }
                })
                .map(new Func1<ResponsePendingCashback, PendingCashbackEntity>() {
                    @Override
                    public PendingCashbackEntity call(ResponsePendingCashback responsePendingCashback) {
                        return responsePendingCashback.getPendingCashbackEntity();
                    }
                })
                .map(pendingCashbackMapper);
    }

    public void unsubscribe() {
        if (graphqlUseCase != null)
            graphqlUseCase.unsubscribe();
    }
}
