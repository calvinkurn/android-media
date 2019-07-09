package com.tokopedia.core.database.manager;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.tokopedia.core.database.DbMetadata;
import com.tokopedia.core.database.dao.ResCenterAttachmentDao;
import com.tokopedia.core.database.model.ResCenterAttachment;

@Database(entities = {ResCenterAttachment.class}, version = DbMetadata.resCenterDbVersion, exportSchema= false)
public abstract class ResCenterAttachmentDatabase extends RoomDatabase {

    public abstract ResCenterAttachmentDao resCenterAttachmentDao();

    private ResCenterAttachmentDatabase() {}

    private static class InstanceHolder {
        private static ResCenterAttachmentDatabase INSTANCE;

        static ResCenterAttachmentDatabase getInstance(Context context) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        ResCenterAttachmentDatabase.class,
                        DbMetadata.resCenterDatabaseName
                ).build();
            }

            return INSTANCE;
        }
    }

    public static ResCenterAttachmentDatabase getDatabase(final Context context) {
        return InstanceHolder.getInstance(context);
    }
}
