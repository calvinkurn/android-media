package com.tokopedia.core.drawer2.domain.datamanager;

import android.content.Context;

import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTopPoints;
import com.tokopedia.core.util.SessionHandler;

import rx.Observable;

/**
 * Created by nisie on 1/23/17.
 */

public interface DrawerDataManager {

   void getProfile();

    void getDeposit();

    void getTopPoints();

    void getTokoCash();

    void getNotification();

    void unsubscribe();

    void getProfileCompletion();

    void getUserAttributes(SessionHandler sessionHandler);
}
