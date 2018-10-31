package com.tokopedia.flight_dbflow;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Rizky on 25/10/18.
 */
@Database(name = TkpdFlightDatabase.NAME, version = TkpdFlightDatabase.VERSION, foreignKeysSupported = true)
public class TkpdFlightDatabase {

    public final static String NAME = "tkpd_flight";

    public final static int VERSION = 8;

}
