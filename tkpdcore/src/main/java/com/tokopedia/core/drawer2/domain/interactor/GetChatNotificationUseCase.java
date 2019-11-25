package com.tokopedia.core.drawer2.domain.interactor;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.data.pojo.TopchatNotificationPojo.ChatNotificationResponse;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.TopChatNotificationModel;
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
    private LocalCacheHandler drawerCache;

    @Inject
    public GetChatNotificationUseCase(
            @Named(GET_CHAT_NOTIFICATION_QUERY) String query,
            GraphqlUseCase graphqlUseCase,
            LocalCacheHandler drawerCache
    ) {
        this.query = query;
        this.graphqlUseCase = graphqlUseCase;
        this.drawerCache = drawerCache;
    }

    @Override
    public Observable<TopChatNotificationModel> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(query, ChatNotificationResponse.class);
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
        }).doOnNext(saveToCache());
    }

    private Action1<TopChatNotificationModel> saveToCache() {
        return topChatNotificationModel -> {
            int notifUnreadsSeller = topChatNotificationModel.getNotifUnreadsSeller();
            drawerCache.putInt(DrawerNotification.CACHE_INBOX_MESSAGE, notifUnreadsSeller);
            drawerCache.putInt(
                    DrawerNotification.CACHE_TOTAL_NOTIF,
                    drawerCache.getInt(DrawerNotification.CACHE_TOTAL_NOTIF, 0) + notifUnreadsSeller
            );
            drawerCache.applyEditor();
        };
    }

}
