package com.tokopedia.discovery.catalog.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CatalogDetailDBModel.class}, version = 1)
public abstract class CatalogDetailDatabase extends RoomDatabase {
    public abstract CatalogDetailDao catalogDetailDao();
}
