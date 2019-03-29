package com.tokopedia.tokocash.ovoactivation.domain;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.ovoactivation.view.CheckPhoneOvoModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 24/09/18.
 */
public class CheckNumberOvoUseCase extends UseCase<CheckPhoneOvoModel> {

    private GraphqlUseCase graphqlUseCase;
    private Context context;
    private CheckPhoneOvoMapper mapper;

    @Inject
    public CheckNumberOvoUseCase(@ApplicationContext Context context,
                                 CheckPhoneOvoMapper mapper, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.mapper = mapper;
    }

    @Override
    public Observable<CheckPhoneOvoModel> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<GraphqlResponse>>() {
                    @Override
                    public Observable<GraphqlResponse> call(RequestParams requestParams) {
                        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.wallet_check_phone_query);

                        if (!TextUtils.isEmpty(query)) {
                            GraphqlRequest request = new GraphqlRequest(query, ResponseCheckPhoneEntity.class, false);
                            graphqlUseCase.clearRequest();
                            graphqlUseCase.addRequest(request);
                            return graphqlUseCase.createObservable(null);
                        }
                        return Observable.error(new Exception("Query and/or variable are empty."));
                    }
                })
                .map(new Func1<GraphqlResponse, ResponseCheckPhoneEntity>() {
                    @Override
                    public ResponseCheckPhoneEntity call(GraphqlResponse graphqlResponse) {
                        return graphqlResponse.getData(ResponseCheckPhoneEntity.class);
                    }
                })
                .map(new Func1<ResponseCheckPhoneEntity, CheckPhoneOvoEntity>() {
                    @Override
                    public CheckPhoneOvoEntity call(ResponseCheckPhoneEntity responseCheckPhoneEntity) {
                        return responseCheckPhoneEntity.getCheckPhoneOvoEntity();
                    }
                })
                .map(mapper);
    }

    public void unsubscribe() {
        if (graphqlUseCase != null)
            graphqlUseCase.unsubscribe();
    }

}
