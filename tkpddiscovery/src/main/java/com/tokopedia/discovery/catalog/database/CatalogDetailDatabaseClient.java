package com.tokopedia.discovery.catalog.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class CatalogDetailDatabaseClient {
    private static CatalogDetailDatabaseClient mInstance;

    private CatalogDetailDatabase catalogDetailDatabase;

    private CatalogDetailDatabaseClient(Context context) {
        catalogDetailDatabase = Room.databaseBuilder(context.getApplicationContext(),
                CatalogDetailDatabase.class, "CatalogDetail.db").fallbackToDestructiveMigration().build();
    }

    public static synchronized CatalogDetailDatabaseClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CatalogDetailDatabaseClient(context);
        }
        return mInstance;
    }

    public CatalogDetailDatabase getCatalogDetailDatabase() {
        return catalogDetailDatabase;
    }
}
