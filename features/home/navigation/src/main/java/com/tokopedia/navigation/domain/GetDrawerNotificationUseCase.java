package com.tokopedia.navigation.domain;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.GlobalNavConstant;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.data.mapper.NotificationMapper;
import com.tokopedia.navigation.listener.CartListener;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by meta on 25/07/18.
 */
public class GetDrawerNotificationUseCase extends UseCase<NotificationEntity> {

    private final GraphqlUseCase graphqlUseCase;
    private final NotificationMapper mapper;
    private CartListener cartListener;

    @Inject
    public GetDrawerNotificationUseCase(GraphqlUseCase graphqlUseCase,
                                        NotificationMapper mapper,
                                        CartListener cartListener) {
        this.graphqlUseCase = graphqlUseCase;
        this.mapper = mapper;
        this.cartListener = cartListener;
    }

    @Override
    public Observable<NotificationEntity> createObservable(RequestParams requestParams) {
        return Observable
                .just(true)
                .flatMap((Func1<Boolean, Observable<GraphqlResponse>>) aBoolean -> {
                    GraphqlRequest graphqlRequest = new GraphqlRequest(
                            requestParams.getString(GlobalNavConstant.QUERY, ""),
                            NotificationEntity.class,
                            requestParams.getParameters(),
                            false);
                    graphqlUseCase.clearRequest();
                    graphqlUseCase.addRequest(graphqlRequest);
                    return graphqlUseCase.createObservable(null);
                }).map(mapper).doOnNext(saveCartCount());
    }

    private Action1<NotificationEntity> saveCartCount() {
        return notificationEntity -> {
            try {
                cartListener.setCartCount(Integer.parseInt(notificationEntity.getNotifications().getTotalCart()));
            } catch (NumberFormatException e) {
                cartListener.setCartCount(0);
            }
        };
    }
}
