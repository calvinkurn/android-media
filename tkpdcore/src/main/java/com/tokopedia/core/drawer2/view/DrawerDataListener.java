package com.tokopedia.core.drawer2.view;

import android.app.Activity;

import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTopPoints;

/**
 * Created by nisie on 5/4/17.
 */

public interface DrawerDataListener {
    void onGetDeposit(DrawerDeposit drawerDeposit);

    void onErrorGetDeposit(String errorMessage);

    void onGetNotificationDrawer(DrawerNotification drawerNotification);

    void onErrorGetNotificationDrawer(String errorMessage);

    void onGetTokoCash(DrawerTokoCash drawerTokoCash);

    void onErrorGetTokoCash(String errorMessage);

    void onGetTopPoints(DrawerTopPoints drawerTopPoints);

    void onErrorGetTopPoints(String errorMessage);

    void onGetProfile(DrawerProfile drawerProfile);

    void onErrorGetProfile(String errorMessage);

    String getString(int resId);

    Activity getActivity();

    void onErrorGetProfileCompletion(String errorMessage);

    void onSuccessGetProfileCompletion(int completion);

    void onErrorGetNotificationTopchat(String errorMessage);

    void onSuccessGetTopChatNotification(int notifUnreads);
}
