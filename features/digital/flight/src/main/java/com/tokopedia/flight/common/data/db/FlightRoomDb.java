package com.tokopedia.flight.common.data.db;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.flight.airport.data.source.database.FlightAirportCountryDao;
import com.tokopedia.flight.airport.data.source.database.FlightAirportCountryTable;
import com.tokopedia.flight.passenger.data.db.FlightPassengerDao;
import com.tokopedia.flight.passenger.data.db.FlightPassengerTable;

/**
 * Created by nabillasabbaha on 12/03/19.
 */
@Database(entities = {FlightAirportCountryTable.class, FlightPassengerTable.class}, version = 1)
public abstract class FlightRoomDb extends RoomDatabase {

    public abstract FlightAirportCountryDao flightAirportCountryDao();

    public abstract FlightPassengerDao flightPassengerDao();

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