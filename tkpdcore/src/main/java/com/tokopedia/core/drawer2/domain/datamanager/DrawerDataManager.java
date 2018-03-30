package com.tokopedia.core.drawer2.domain.datamanager;

import com.tokopedia.core.util.SessionHandler;

/**
 * Created by nisie on 1/23/17.
 */

public interface DrawerDataManager {

    void getTokoCash();

    void getNotification();

    void unsubscribe();

    void getUserAttributes(SessionHandler sessionHandler);

    void getSellerUserAttributes(SessionHandler sessionHandler);

}
