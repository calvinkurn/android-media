package com.tokopedia.travel.passenger.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * @author by furqan on 02/01/2020
 */
@Database(entities = [TravelPassengerTable::class], version = 2)
abstract class TravelPassengerDb : RoomDatabase() {

    abstract fun travelPassengerDao(): TravelPassengerDao

    companion object {

        @Volatile
        private lateinit var travelPassengerRoomDb: TravelPassengerDb

        fun getDatabase(context: Context): TravelPassengerDb {
            if (!::travelPassengerRoomDb.isInitialized) {
                synchronized(TravelPassengerDb::class.java) {
                    travelPassengerRoomDb = Room.databaseBuilder<TravelPassengerDb>(context.applicationContext,
                            TravelPassengerDb::class.java, "TravelPassenger.db")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return travelPassengerRoomDb
        }
    }

}