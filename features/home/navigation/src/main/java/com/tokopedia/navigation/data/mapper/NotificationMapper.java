package com.tokopedia.navigation.data.mapper;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.domain.model.Notification;
import com.tokopedia.navigation_common.model.NotificationsModel;

import rx.functions.Func1;

/**
 * Created by meta on 25/07/18.
 */
public class NotificationMapper implements Func1<GraphqlResponse, NotificationEntity> {

    @Override
    public NotificationEntity call(GraphqlResponse graphqlResponse) {
        return graphqlResponse.getData(NotificationEntity.class);
    }

    public static boolean isHasShop(NotificationEntity entity) {
        return entity != null
                && entity.getShopInfo() != null
                && entity.getShopInfo().getInfo() != null
                && !entity.getShopInfo().getInfo().getShopId().equalsIgnoreCase("-1");
    }

    public static Notification notificationMapper(NotificationsModel entity) {
        Notification data = new Notification();
        try {
            data.setTotalCart(Integer.parseInt(entity.getTotalCart()));
            data.setTotalInbox(totalInbox(entity));
            data.setTotalNotif(totalNotif(entity));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static Integer totalNotif(NotificationsModel entity) {
        Integer total = 0;
        try {
            total += entity.getSellerInfo().getNotification();
            total += entity.getBuyerOrder().getConfirmed();
            total += entity.getBuyerOrder().getProcessed();
            total += entity.getBuyerOrder().getShipped();
            total += entity.getBuyerOrder().getArriveAtDestination();
            total += entity.getSellerOrder().getNewOrder();
            total += entity.getSellerOrder().getShipped();
            total += entity.getSellerOrder().getReadyToShip();
            total += entity.getSellerOrder().getArriveAtDestination();
            total += entity.getResolution().getBuyer();
            total += entity.getResolution().getSeller();
        } catch (NumberFormatException e) { /*ignore*/ }
        return total;
    }

    private static Integer totalInbox(NotificationsModel entity) {
        Integer total = 0;
        try {
            total += Integer.parseInt(entity.getChat().getUnreads());
            total += Integer.parseInt(entity.getInbox().getTalk());
            total += Integer.parseInt(entity.getInbox().getReview());
            total += Integer.parseInt(entity.getInbox().getTicket());
        } catch (NumberFormatException e) { /*ignore*/ }
        return total;
    }
}
