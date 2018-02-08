package com.tokopedia.sellerapp.dashboard.presenter.listener;

import android.app.Activity;

import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTopPoints;
import com.tokopedia.core.drawer2.view.DrawerDataListener;

/**
 * Created by User on 9/11/2017.
 */

public abstract class NotificationListener implements DrawerDataListener {

    @Override
    public abstract void onGetNotificationDrawer(DrawerNotification drawerNotification);

    @Override
    public abstract void onErrorGetNotificationDrawer(String errorMessage);

    @Override
    public void onGetDeposit(DrawerDeposit drawerDeposit) {
        // no operation
    }

    @Override
    public void onErrorGetDeposit(String errorMessage) {
        // no operation
    }

    @Override
    public void onGetTokoCash(DrawerTokoCash drawerTokoCash) {
        // no operation
    }

    @Override
    public void onErrorGetTokoCash(String errorMessage) {
        // no operation
    }

    @Override
    public void onGetTopPoints(DrawerTopPoints drawerTopPoints) {
        // no operation
    }

    @Override
    public void onErrorGetTopPoints(String errorMessage) {
        // no operation
    }

    @Override
    public void onGetProfile(DrawerProfile drawerProfile) {
        // no operation
    }

    @Override
    public void onErrorGetProfile(String errorMessage) {
        // no operation
    }

    @Override
    public String getString(int resId) {
        return null;
    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public void onErrorGetProfileCompletion(String errorMessage) {
        // no operation
    }

    @Override
    public void onSuccessGetProfileCompletion(int completion) {
        // no operation
    }

    @Override
    public void onErrorGetNotificationTopchat(String errorMessage) {

    }

    @Override
    public void onSuccessGetTopChatNotification(int notifUnreads) {

    }
}
