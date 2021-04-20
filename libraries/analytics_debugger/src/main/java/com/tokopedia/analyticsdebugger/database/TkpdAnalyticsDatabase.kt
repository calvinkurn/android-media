package com.tokopedia.analyticsdebugger.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tokopedia.analyticsdebugger.debugger.data.source.*

/**
 * @author okasurya on 5/14/18.
 */
@Database(entities = [GtmLogDB::class, FpmLogDB::class, ApplinkLogDB::class, TopAdsLogDB::class, IrisSaveLogDB::class, IrisSendLogDB::class], version = 8)
abstract class TkpdAnalyticsDatabase : RoomDatabase() {

    abstract fun gtmLogDao(): GtmLogDao
    abstract fun fpmLogDao(): FpmLogDao
    abstract fun applinkLogDao(): ApplinkLogDao
    abstract fun topAdsLogDao(): TopAdsLogDao
    abstract fun irisLogSaveDao(): IrisLogSaveDao
    abstract fun irisLogSendDao(): IrisLogSendDao

    companion object {

        private val DATABASE_NAME = "tkpd_gtm_log_analytics" // previously was tkpd_analytics

        @Volatile
        private var instance: TkpdAnalyticsDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): TkpdAnalyticsDatabase {
            var r = instance
            if (r == null) {
                synchronized(lock) {
                    r = instance
                    if (r == null) {
                        r = Room.databaseBuilder(context,
                            TkpdAnalyticsDatabase::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration().build()
                        instance = r
                    }
                }
            }
            return r!!
        }
    }
}
