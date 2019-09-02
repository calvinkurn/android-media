package com.tokopedia.train.common.database;

import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.train.search.data.database.TrainScheduleDao;
import com.tokopedia.train.search.data.database.TrainScheduleTable;
import com.tokopedia.train.station.data.database.TrainStationDao;
import com.tokopedia.train.station.data.database.TrainStationTable;

/**
 * Created by nabillasabbaha on 31/01/19.
 */
@Database(entities = {TrainStationTable.class, TrainScheduleTable.class}, version = 1)
public abstract class TrainRoomDb extends RoomDatabase {

    public abstract TrainStationDao trainStationDao();

    public abstract TrainScheduleDao trainScheduleDao();

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

    private static volatile TrainRoomDb trainRoomDb;

    public static TrainRoomDb getDatabase(final Context context) {
        if (trainRoomDb == null) {
            synchronized (TrainRoomDb.class) {
                if (trainRoomDb == null) {
                    trainRoomDb = Room.databaseBuilder(context.getApplicationContext(),
                            TrainRoomDb.class, "TkpdTrain.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return trainRoomDb;
    }
}
