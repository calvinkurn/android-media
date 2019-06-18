package com.tokopedia.gamification.taptap.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.DataConverter;

@Database(entities = {CrackResultEntity.class}, version = 1,  exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class GamificationRoomDb extends RoomDatabase {

    public abstract GamificationDao gamificationDao();


    private static volatile GamificationRoomDb gamificationRoomDb;

    public static GamificationRoomDb getDatabase(final Context context) {
        if (gamificationRoomDb == null) {
            synchronized (GamificationRoomDb.class) {
                if (gamificationRoomDb == null) {
                    gamificationRoomDb = Room.databaseBuilder(context.getApplicationContext(),
                            GamificationRoomDb.class, "GamificationRoomDb.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return gamificationRoomDb;
    }
}
