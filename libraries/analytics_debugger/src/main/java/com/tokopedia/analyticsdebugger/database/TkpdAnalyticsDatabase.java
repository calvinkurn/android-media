package com.tokopedia.analyticsdebugger.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.tokopedia.analyticsdebugger.debugger.data.source.GtmErrorLogDao;
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDao;
import com.tokopedia.analyticsdebugger.debugger.data.source.IrisLogSaveDao;
import com.tokopedia.analyticsdebugger.debugger.data.source.IrisLogSendDao;

/**
 * @author okasurya on 5/14/18.
 */
@Database(entities = {GtmLogDB.class, IrisSaveLogDB.class, IrisSendLogDB.class, GtmErrorLogDB.class}, version = 2)
public abstract class TkpdAnalyticsDatabase extends RoomDatabase {

    public abstract GtmLogDao gtmLogDao();
    public abstract IrisLogSaveDao irisLogSaveDao();
    public abstract IrisLogSendDao irisLogSendDao();
    public abstract GtmErrorLogDao gtmErrorLogDao();

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
