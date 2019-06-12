package com.tokopedia.discovery.catalog.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {CatalogDetailDBModel.class}, version = 1)
public abstract class CatalogDetailDatabase extends RoomDatabase {
    public abstract CatalogDetailDao catalogDetailDao();
}
