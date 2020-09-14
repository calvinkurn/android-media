package com.tokopedia.navigation.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.GlobalNavConstant;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.data.mapper.NotificationRequestMapper;
import com.tokopedia.purchase_platform.common.constant.CartConstant;
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
    private final NotificationRequestMapper mapper;
    private Context context;

    @Inject
    public GetDrawerNotificationUseCase(@ApplicationContext Context context,
                                        GraphqlUseCase graphqlUseCase,
                                        NotificationRequestMapper mapper) {
        this.graphqlUseCase = graphqlUseCase;
        this.mapper = mapper;
        this.context = context;
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
                setCartCount(Integer.parseInt(notificationEntity.getNotifications().getTotalCart()));
            } catch (NumberFormatException e) {
                setCartCount(0);
            }
        };
    }

    public void setCartCount(int count) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CartConstant.CART);
        localCacheHandler.putInt(CartConstant.CACHE_TOTAL_CART, count);
        localCacheHandler.applyEditor();
    }

    public int getCartCount(Context context) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CartConstant.CART);
        return localCacheHandler.getInt(CartConstant.CACHE_TOTAL_CART, 0);
    }

}
