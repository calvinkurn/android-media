package com.tokopedia.analytics.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.tokopedia.analytics.debugger.data.source.GtmLogDao;

/**
 * @author okasurya on 5/14/18.
 */
@Database(entities = {GtmLogDB.class}, version = 1)
public abstract class TkpdAnalyticsDatabase extends RoomDatabase {

    public abstract GtmLogDao gtmLogDao();

    private static String DATABASE_NAME = "tkpd_gtm_log_analytics"; // previously was tkpd_analytics

    private static volatile TkpdAnalyticsDatabase instance;
    private static final Object lock = new Object();

    public static TkpdAnalyticsDatabase getInstance(Context context) {
        TkpdAnalyticsDatabase r = instance;
        if (r == null) {
            synchronized (lock) {
                r = instance;
                if (r == null) {
                    r = Room.databaseBuilder(context,
                            TkpdAnalyticsDatabase.class, DATABASE_NAME).build();
                    instance = r;
                }
            }
        }
        return r;
    }
}
