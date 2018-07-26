package com.tokopedia.notification.presentation.view;

/**
 * Created by meta on 26/07/18.
 */
public interface NotificationView {

    void onStartLoading();

    void onHideLoading();

    void onError(String message);
}
