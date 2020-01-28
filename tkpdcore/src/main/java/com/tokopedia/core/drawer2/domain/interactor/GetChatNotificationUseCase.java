package com.tokopedia.core.drawer2.domain.interactor;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.data.pojo.TopchatNotificationPojo.ChatNotificationResponse;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.TopChatNotificationModel;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Action1;

import javax.inject.Inject;
import javax.inject.Named;

import static com.tokopedia.core.drawer2.domain.GqlQueryConstant.GET_CHAT_NOTIFICATION_QUERY;

public class GetChatNotificationUseCase extends UseCase<TopChatNotificationModel> {

    private String query;
    private GraphqlUseCase graphqlUseCase;
    private boolean isRefresh = false;

    @Inject
    public GetChatNotificationUseCase(
            @Named(GET_CHAT_NOTIFICATION_QUERY) String query,
            GraphqlUseCase graphqlUseCase
    ) {
        this.query = query;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<TopChatNotificationModel> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(query, ChatNotificationResponse.class);
        graphqlUseCase.clearRequest();
        CacheType cacheType = isRefresh?CacheType.ALWAYS_CLOUD:CacheType.CACHE_FIRST;
        graphqlUseCase.setCacheStrategy(new GraphqlCacheStrategy.Builder(cacheType)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.val() / 2)
                .setSessionIncluded(true)
                .build()
        );
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(requestParams).map(graphqlResponse -> {
            ChatNotificationResponse response = graphqlResponse.getData(ChatNotificationResponse.class);
            if (null != response) {
                TopChatNotificationModel model = new TopChatNotificationModel(
                        response.getNotifications().getTopchatNotificationPojo().getNotifUnreads()
                );
                model.setNotifUnreadsBuyer(
                        response.getNotifications().getTopchatNotificationPojo().getNotifUnreadsBuyer()
                );
                model.setNotifUnreadsSeller(
                        response.getNotifications().getTopchatNotificationPojo().getNotifUnreadsSeller()
                );
                return model;
            } else {
                throw new RuntimeException();
            }
        });
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
