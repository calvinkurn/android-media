package com.tokopedia.common.travel.database;

import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;

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
