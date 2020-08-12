package com.tokopedia.flight.searchV4.data.cache.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.tokopedia.flight.searchV4.data.FlightRouteDao;
import com.tokopedia.flight.searchV4.data.cache.db.dao.FlightComboDao;
import com.tokopedia.flight.searchV4.data.cache.db.dao.FlightJourneyDao;

/**
 * Created by Rizky on 01/10/18.
 */
@Database(
        entities = {
                FlightComboTable.class,
                FlightJourneyTable.class,
                FlightRouteTable.class
        },
        version = 14)
@TypeConverters({FlightTypeConverters.class})
public abstract class FlightSearchRoomDb extends RoomDatabase {

    public abstract FlightJourneyDao flightJourneyDao();

    public abstract FlightRouteDao flightRouteDao();

    public abstract FlightComboDao flightComboDao();

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

    private volatile static FlightSearchRoomDb INSTANCE = null;

    public static FlightSearchRoomDb getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FlightSearchRoomDb.class) {
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
