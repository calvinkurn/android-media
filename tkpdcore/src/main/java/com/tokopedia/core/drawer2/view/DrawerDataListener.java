package com.tokopedia.core.drawer2.view;

import com.tokopedia.core.drawer2.data.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
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

    void onGetTopPoints(DrawerTopPoints drawerTopPoints);
}
