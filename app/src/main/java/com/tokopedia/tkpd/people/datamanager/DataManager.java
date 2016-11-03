package com.tokopedia.tkpd.people.datamanager;

import android.content.Context;

/**
 * Created on 5/31/16.
 */
public interface DataManager {

    void requestPeopleInfo(Context context, String userID);

    void unSubscribe();
}
