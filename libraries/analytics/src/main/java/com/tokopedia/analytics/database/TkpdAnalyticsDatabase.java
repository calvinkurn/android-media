package com.tokopedia.analytics.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * @author okasurya on 5/14/18.
 */

@Database(name = TkpdAnalyticsDatabase.NAME, version = TkpdAnalyticsDatabase.VERSION)
public class TkpdAnalyticsDatabase {
    public static final int VERSION = 1;
    public static final String NAME = "tkpd_analytics";
}
