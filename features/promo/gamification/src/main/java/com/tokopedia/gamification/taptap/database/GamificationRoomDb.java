package com.tokopedia.gamification.taptap.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
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
