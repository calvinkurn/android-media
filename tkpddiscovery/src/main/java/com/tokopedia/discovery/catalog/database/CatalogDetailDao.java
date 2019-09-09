package com.tokopedia.discovery.catalog.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CatalogDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CatalogDetailDBModel catalogDetailDBModel);

    @Query("SELECT * FROM CatalogDetailDBModel WHERE detail_catalog_id LIKE :query")
    CatalogDetailDBModel getCatalogDetailDataById(String query);
}
