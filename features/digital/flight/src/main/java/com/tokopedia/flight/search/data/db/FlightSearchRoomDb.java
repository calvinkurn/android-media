package com.tokopedia.flight.search.data.db;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Rizky on 01/10/18.
 */
@Database(
        entities = {
                FlightComboTable.class,
                FlightJourneyTable.class,
                FlightRouteTable.class
        },
        version = 10)
@TypeConverters({FlightTypeConverters.class})
public abstract class FlightSearchRoomDb extends RoomDatabase {

    public abstract FlightComboDao flightComboDao();
    public abstract FlightJourneyDao flightJourneyDao();
    public abstract FlightRouteDao flightRouteDao();

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

    private static FlightSearchRoomDb INSTANCE = null;

    public static FlightSearchRoomDb getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized(FlightSearchRoomDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            FlightSearchRoomDb.class, "FlightSearchRoom.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
