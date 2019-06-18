package com.tokopedia.tokocash.pendingcashback.domain;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.pendingcashback.data.PendingCashbackMapper;
import com.tokopedia.tokocash.pendingcashback.data.ResponsePendingCashback;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/7/18.
 */

public class GetPendingCasbackUseCase extends UseCase<PendingCashback> {

    private GraphqlUseCase graphqlUseCase;
    private Context context;
    private PendingCashbackMapper pendingCashbackMapper;

    @Inject
    public GetPendingCasbackUseCase(@ApplicationContext Context context,
                                    GraphqlUseCase graphqlUseCase,
                                    PendingCashbackMapper pendingCashbackMapper) {
        this.graphqlUseCase = graphqlUseCase;
        this.context = context;
        this.pendingCashbackMapper = pendingCashbackMapper;
    }

    @Override
    public Observable<PendingCashback> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap((Func1<RequestParams, Observable<GraphqlResponse>>) requestParams1 -> {
                    String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.wallet_pending_cashback_query);
                    Map<String, Object> variables = new HashMap<>();

                    if (!TextUtils.isEmpty(query)) {
                        GraphqlRequest request = new GraphqlRequest(query, ResponsePendingCashback.class, variables, false);
                        graphqlUseCase.clearRequest();
                        graphqlUseCase.addRequest(request);
                        return graphqlUseCase.createObservable(null);
                    }
                    return Observable.error(new Exception("Query and/or variable are empty."));
                })
                .map((Func1<GraphqlResponse, ResponsePendingCashback>) graphqlResponse ->
                        graphqlResponse.getData(ResponsePendingCashback.class))
                .map(ResponsePendingCashback::getPendingCashbackEntity)
                .map(pendingCashbackMapper);
    }

    public void unsubscribe() {
        if (graphqlUseCase != null)
            graphqlUseCase.unsubscribe();
    }
}
