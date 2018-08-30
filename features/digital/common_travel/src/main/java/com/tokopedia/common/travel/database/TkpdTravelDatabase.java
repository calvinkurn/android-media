package com.tokopedia.common.travel.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by nabillasabbaha on 15/08/18.
 */
@Database(name = TkpdTravelDatabase.NAME, version = TkpdTravelDatabase.VERSION, foreignKeysSupported = true)
public class TkpdTravelDatabase {

    public static final String NAME = "tkpd_travel_database";

    public static final int VERSION = 1;
}
