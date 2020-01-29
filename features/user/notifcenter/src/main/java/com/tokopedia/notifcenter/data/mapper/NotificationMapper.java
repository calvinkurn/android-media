package com.tokopedia.notifcenter.data.mapper;

import com.tokopedia.notifcenter.data.entity.NotificationEntity;

public class NotificationMapper {

    public static boolean isHasShop(NotificationEntity entity) {
        return entity != null
                && entity.getShopInfo() != null
                && entity.getShopInfo().getInfo() != null
                && !entity.getShopInfo().getInfo().getShopId().equalsIgnoreCase("-1");
    }

}
