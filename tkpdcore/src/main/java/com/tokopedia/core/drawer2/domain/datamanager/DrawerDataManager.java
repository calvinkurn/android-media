package com.tokopedia.core.drawer2.domain.datamanager;

import com.tokopedia.core.util.SessionHandler;

/**
 * Created by nisie on 1/23/17.
 */

public interface DrawerDataManager {

    void getProfile();

    void getDeposit();

    void getTokoCash();

    void getNotification();

    void unsubscribe();

    void getProfileCompletion();

    void getUserAttributes(SessionHandler sessionHandler);

}
