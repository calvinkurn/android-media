package com.tokopedia.discovery.catalog.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class CatalogDetailDBModel {
    @PrimaryKey
    @ColumnInfo(name = "detail_catalog_id")
    @NonNull
    private String detailCatalogId;

    @ColumnInfo(name = "detail_catalog_data")
    private String detailCatalogData;

    @ColumnInfo(name = "expired_time")
    private long expiredTime;

    public CatalogDetailDBModel(@NonNull String detailCatalogId, String detailCatalogData, long expiredTime) {
        this.detailCatalogId = detailCatalogId;
        this.detailCatalogData = detailCatalogData;
        this.expiredTime = expiredTime;
    }

    @NonNull
    public String getDetailCatalogId() {
        return detailCatalogId;
    }

    public String getDetailCatalogData() {
        return detailCatalogData;
    }

    public long getExpiredTime() {
        return expiredTime;
    }
}
