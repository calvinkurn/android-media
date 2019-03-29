package com.tokopedia.common.travel.database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by nabillasabbaha on 15/08/18.
 */
@Database(entities = {TravelPassengerTable.class}, version = 1)
public abstract class TravelPassengerRoomDb extends RoomDatabase {

    public abstract TravelPassengerDao travelPassengerDao();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    private static volatile TravelPassengerRoomDb travelPassengerRoomDb;

    public static TravelPassengerRoomDb getDatabase(final Context context) {
        if (travelPassengerRoomDb == null) {
            synchronized (TravelPassengerRoomDb.class) {
                if (travelPassengerRoomDb == null) {
                    travelPassengerRoomDb = Room.databaseBuilder(context.getApplicationContext(),
                            TravelPassengerRoomDb.class, "TravelPassenger.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return travelPassengerRoomDb;
    }
}
