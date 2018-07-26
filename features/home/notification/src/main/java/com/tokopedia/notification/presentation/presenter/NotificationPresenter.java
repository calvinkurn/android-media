package com.tokopedia.notification.presentation.presenter;

import com.tokopedia.notification.presentation.view.NotificationView;

/**
 * Created by meta on 26/07/18.
 */
public class NotificationPresenter {

    private NotificationView notificationView;



    public void setView(NotificationView notificationView) {
        this.notificationView = notificationView;
    }

    public void onResume() {

    }

    public void onDestroy() {

    }
}
