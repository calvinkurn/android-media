package com.tokopedia.tkpd.manage.people.profile.datamanager;

import android.content.Context;

/**
 * Created on 6/27/16.
 */
public interface DataManager {
    void getProfile(Context context);

    void refreshProfile(Context context);

    void unSubscribe();
}
