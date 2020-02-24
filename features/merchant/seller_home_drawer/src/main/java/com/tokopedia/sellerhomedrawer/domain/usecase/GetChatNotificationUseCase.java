package com.tokopedia.sellerhomedrawer.domain.usecase;

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeParamConstant;
import com.tokopedia.sellerhomedrawer.data.header.TopChatNotificationModel;
import com.tokopedia.sellerhomedrawer.data.userdata.TopchatNotificationPojo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

public class GetChatNotificationUseCase extends UseCase<TopChatNotificationModel> {

    private String query;
    private GraphqlUseCase graphqlUseCase;
    private boolean isRefresh = false;

    @Inject
    public GetChatNotificationUseCase(
            @Named(SellerHomeParamConstant.GET_CHAT_NOTIFICATION_QUERY) String query,
            GraphqlUseCase graphqlUseCase
    ) {
        this.query = query;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<TopChatNotificationModel> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(query, TopchatNotificationPojo.ChatNotificationResponse.class);
        graphqlUseCase.clearRequest();
        CacheType cacheType = isRefresh?CacheType.ALWAYS_CLOUD:CacheType.CACHE_FIRST;
        graphqlUseCase.setCacheStrategy(new GraphqlCacheStrategy.Builder(cacheType)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.val() / 2)
                .setSessionIncluded(true)
                .build()
        );
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(requestParams).map(graphqlResponse -> {
            TopchatNotificationPojo.ChatNotificationResponse response = graphqlResponse.getData(TopchatNotificationPojo.ChatNotificationResponse.class);
            List<GraphqlError> graphqlErrors = graphqlResponse.getError(TopchatNotificationPojo.ChatNotificationResponse.class);
            if (graphqlErrors != null) {
                if (graphqlErrors.size() > 0 && graphqlErrors.get(0) != null) {
                    try {
                        throw new ResponseErrorException(graphqlErrors.get(0).getMessage());
                    } catch (ResponseErrorException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        throw new ResponseErrorException();
                    } catch (ResponseErrorException e) {
                        e.printStackTrace();
                    }
                }
            }
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
