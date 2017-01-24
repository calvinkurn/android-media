package com.tokopedia.core.drawer2.datamanager;

import android.content.Context;

import com.tokopedia.core.drawer2.viewmodel.DrawerData;

import rx.Observable;

/**
 * Created by nisie on 1/23/17.
 */

public interface DrawerDataManager {

    Observable<DrawerData> getDrawerData(Context context);

    void unsubscribe();
}
