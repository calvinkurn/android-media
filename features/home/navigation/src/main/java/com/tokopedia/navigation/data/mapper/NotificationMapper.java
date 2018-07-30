package com.tokopedia.navigation.data.mapper;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.domain.model.Notification;

import rx.functions.Func1;

/**
 * Created by meta on 25/07/18.
 */
public class NotificationMapper implements Func1<GraphqlResponse, NotificationEntity> {

    @Override
    public NotificationEntity call(GraphqlResponse graphqlResponse) {
        return graphqlResponse.getData(NotificationEntity.class);
    }

    public static Notification notificationMapper(NotificationEntity.Notification entity) {
        Notification data = new Notification();
        try {
            data.setTotalCart(Integer.parseInt(entity.getTotalCart()));
            data.setTotalInbox(totalInbox(entity));
            data.setTotalNotif(Integer.parseInt(entity.getTotalNotif()));
            data.setTotalWishlist(Integer.parseInt(entity.getInbox().getWishlist()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static Integer totalInbox(NotificationEntity.Notification entity) {
        Integer total = 0;
        try {
            total += Integer.parseInt(entity.getChat().getUnreads());
            total += Integer.parseInt(entity.getInbox().getTalk());
            total += Integer.parseInt(entity.getInbox().getReview());
            total += Integer.parseInt(entity.getInbox().getTicket());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return total;
    }
}
