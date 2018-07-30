package com.tokopedia.navigation.domain;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.data.GlobalNavConstant;
import com.tokopedia.navigation.data.entity.DrawerNotificationEntity;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.data.mapper.DrawerNotificationMapper;
import com.tokopedia.navigation.data.mapper.NotificationMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by meta on 25/07/18.
 */
public class GetDrawerNotificationUseCase extends UseCase<DrawerNotificationEntity> {

    private final GraphqlUseCase graphqlUseCase;
    private final DrawerNotificationMapper mapper;

    @Inject
    public GetDrawerNotificationUseCase(GraphqlUseCase graphqlUseCase, DrawerNotificationMapper mapper) {
        this.graphqlUseCase = graphqlUseCase;
        this.mapper = mapper;
    }

    @Override
    public Observable<DrawerNotificationEntity> createObservable(RequestParams requestParams) {
        return Observable
                .just(true)
                .flatMap((Func1<Boolean, Observable<GraphqlResponse>>) aBoolean -> {
            GraphqlRequest graphqlRequest = new GraphqlRequest(
                    requestParams.getString(GlobalNavConstant.QUERY, ""),
                    DrawerNotificationEntity.class);
            graphqlUseCase.addRequest(graphqlRequest);
            return graphqlUseCase.createObservable(null);
        }).map(mapper);
    }
}