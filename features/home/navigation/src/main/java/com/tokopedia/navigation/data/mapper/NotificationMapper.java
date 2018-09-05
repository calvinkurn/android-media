package com.tokopedia.navigation.data.mapper;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.domain.model.Notification;
import com.tokopedia.navigation.util.IntegerUtil;
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
            data.setTotalCart(IntegerUtil.tryParseInt(entity.getTotalCart()));
            data.setTotalInbox(totalInbox(entity));
            data.setTotalNotif(totalNotif(entity));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static Integer totalNotif(NotificationsModel entity) {
        Integer total = 0;
        total += entity.getSellerInfo().getNotification();
        total += IntegerUtil.tryParseInt(entity.getBuyerOrder().getPaymentStatus());
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
        return total;
    }

    private static Integer totalInbox(NotificationsModel entity) {
        Integer total = 0;
        total += IntegerUtil.tryParseInt(entity.getChat().getUnreads());
        total += IntegerUtil.tryParseInt(entity.getInbox().getTalk());
        total += IntegerUtil.tryParseInt(entity.getInbox().getReview());
        total += IntegerUtil.tryParseInt(entity.getInbox().getTicket());
        return total;
    }
}
