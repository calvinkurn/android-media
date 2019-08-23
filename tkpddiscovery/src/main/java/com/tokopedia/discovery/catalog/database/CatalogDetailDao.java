package com.tokopedia.discovery.catalog.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface CatalogDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CatalogDetailDBModel catalogDetailDBModel);

    @Query("SELECT * FROM CatalogDetailDBModel WHERE detail_catalog_id LIKE :query")
    CatalogDetailDBModel getCatalogDetailDataById(String query);
}
