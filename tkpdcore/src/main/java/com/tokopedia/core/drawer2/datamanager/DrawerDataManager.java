package com.tokopedia.core.drawer2.datamanager;

import android.content.Context;

import com.tokopedia.core.drawer2.viewmodel.DrawerData;
import com.tokopedia.core.drawer2.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.viewmodel.DrawerTopPoints;

import rx.Observable;

/**
 * Created by nisie on 1/23/17.
 */

public interface DrawerDataManager {


    Observable<DrawerProfile> getDrawerProfile(Context context);

    Observable<DrawerDeposit> getDeposit(Context context);

    Observable<DrawerTopPoints> getTopPoints(Context context);

    Observable<DrawerTokoCash> getTokoCash(String accessToken);

    Observable<DrawerNotification> getNotification(Context context);

    void unsubscribe();
}
