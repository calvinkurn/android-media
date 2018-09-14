package com.tokopedia.navigation.presentation.view;

import com.tokopedia.navigation_common.model.NotifcenterUnread;
import com.tokopedia.navigation_common.model.NotificationsModel;

/**
 * Created by meta on 26/07/18.
 */
public interface NotificationView extends LoadDataView {
    void renderNotification(NotificationsModel data, NotifcenterUnread unread,
                            boolean isHasShop);
}
