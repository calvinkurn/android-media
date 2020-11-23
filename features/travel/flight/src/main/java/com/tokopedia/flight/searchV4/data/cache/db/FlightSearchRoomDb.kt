package com.tokopedia.flight.searchV4.data.cache.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tokopedia.flight.searchV4.data.FlightRouteDao
import com.tokopedia.flight.searchV4.data.cache.db.dao.FlightComboDao
import com.tokopedia.flight.searchV4.data.cache.db.dao.FlightJourneyDao

/**
 * @author by furqan on 05/11/2020
 */
@Database(
        entities = [
            FlightComboTable::class,
            FlightJourneyTable::class,
            FlightRouteTable::class],
        version = 15
)
@TypeConverters(FlightTypeConverters::class)
abstract class FlightSearchRoomDb : RoomDatabase() {

    abstract fun flightJourneyDao(): FlightJourneyDao
    abstract fun flightRouteDao(): FlightRouteDao
    abstract fun flightComboDao(): FlightComboDao

    companion object {
        private const val DB_NAME = "FlightSearchRoom.db"

        @Volatile
        private var INSTANCE: FlightSearchRoomDb? = null

        fun getInstance(context: Context): FlightSearchRoomDb {
            if (INSTANCE == null) {
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                            context.applicationContext,
                            FlightSearchRoomDb::class.java,
                            DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
            }
            return INSTANCE!!
        }
    }

}