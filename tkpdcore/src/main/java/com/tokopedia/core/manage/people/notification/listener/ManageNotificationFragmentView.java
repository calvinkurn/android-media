package com.tokopedia.core.manage.people.notification.listener;

import android.app.Activity;

import com.tokopedia.core.manage.people.notification.model.SettingNotification;

/**
 * Created by Nisie on 6/22/16.
 */
public interface ManageNotificationFragmentView {

    Activity getActivity();

    void finishLoading();

    void setToUI(SettingNotification setting);

    void showLoading();

    int getFlagTalkProduct();

    int getFlagReview();

    int getFlagNewsletter();

    int getFlagMessage();

    int getFlagAdminMessage();

    void showSnackbar(String status);

    void showSnackbar();
}
