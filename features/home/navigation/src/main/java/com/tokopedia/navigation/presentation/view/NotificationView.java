package com.tokopedia.navigation.presentation.view;

import com.tokopedia.navigation.NotificationsModel;

/**
 * Created by meta on 26/07/18.
 */
public interface NotificationView extends LoadDataView {
    void renderNotification(NotificationsModel data);
}
