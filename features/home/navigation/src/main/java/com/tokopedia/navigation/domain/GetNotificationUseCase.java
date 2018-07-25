package com.tokopedia.navigation.domain;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.data.GlobalNavConstant;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.data.mapper.NotificationMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by meta on 25/07/18.
 */
public class GetNotificationUseCase extends UseCase<NotificationEntity> {

    private final GraphqlUseCase graphqlUseCase;
    private final NotificationMapper mapper;

    @Inject
    public GetNotificationUseCase(GraphqlUseCase graphqlUseCase, NotificationMapper mapper) {
        this.graphqlUseCase = graphqlUseCase;
        this.mapper = mapper;
    }

    @Override
    public Observable<NotificationEntity> createObservable(RequestParams requestParams) {
        return Observable
                .just(true)
                .flatMap((Func1<Boolean, Observable<GraphqlResponse>>) aBoolean -> {
            GraphqlRequest graphqlRequest = new GraphqlRequest(
                    requestParams.getString(GlobalNavConstant.QUERY, ""),
                    NotificationEntity.class);
            graphqlUseCase.addRequest(graphqlRequest);
            return graphqlUseCase.createObservable(null);
        }).map(mapper);
    }
}