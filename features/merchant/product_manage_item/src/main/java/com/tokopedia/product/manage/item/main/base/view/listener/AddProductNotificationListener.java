package com.tokopedia.product.manage.item.main.base.view.listener;

/**
 * @author sebastianuskh on 4/20/17.
 */

public interface AddProductNotificationListener {
    void createNotification(long draftProductId, String productName);

    void notificationUpdate(long draftProductId);
}
