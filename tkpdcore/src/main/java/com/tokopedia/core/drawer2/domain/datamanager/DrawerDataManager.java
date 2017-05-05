package com.tokopedia.core.drawer2.domain.datamanager;

import android.content.Context;

import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTopPoints;

import rx.Observable;

/**
 * Created by nisie on 1/23/17.
 */

public interface DrawerDataManager {

    Observable<DrawerProfile> getDrawerProfile(Context context);

    void getDeposit();

    void getTopPoints();

    void getTokoCash();

    void getNotification();

    void unsubscribe();
}
