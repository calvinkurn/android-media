package com.tokopedia.core.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.tokopedia.core.database.dao.ResCenterAttachmentDao;
import com.tokopedia.core.database.model.ResCenterAttachment;

@Database(entities = {ResCenterAttachment.class}, version = DbMetadata.appDatabaseVersion)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DbMetadata.appDatabaseName
                    ).build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract ResCenterAttachmentDao resCenterAttachmentDao();
}
