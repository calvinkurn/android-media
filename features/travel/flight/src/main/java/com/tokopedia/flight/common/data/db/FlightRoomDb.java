package com.tokopedia.flight.common.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.tokopedia.flight.country.database.FlightAirportCountryDao;
import com.tokopedia.flight.country.database.FlightAirportCountryTable;

/**
 * Created by nabillasabbaha on 12/03/19.
 */
@Database(entities = {FlightAirportCountryTable.class}, version = 1)
public abstract class FlightRoomDb extends RoomDatabase {

    public abstract FlightAirportCountryDao flightAirportCountryDao();

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

    private static volatile FlightRoomDb flightRoomDb;

    public static FlightRoomDb getDatabase(final Context context) {
        if (flightRoomDb == null) {
            synchronized (FlightRoomDb.class) {
                if (flightRoomDb == null) {
                    flightRoomDb = Room.databaseBuilder(context.getApplicationContext(),
                            FlightRoomDb.class, "FlightRoom.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return flightRoomDb;
    }
}