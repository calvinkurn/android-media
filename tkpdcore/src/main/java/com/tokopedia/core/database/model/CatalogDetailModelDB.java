package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;

/*
 * @author anggaprasetiyo on 10/18/16.
 */
@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class CatalogDetailModelDB extends BaseModel {

    @PrimaryKey
    @Column
    private String detailCatalogId;
    @Column
    private String detailCatalogData;
    @Column
    private long expiredTime;

    public CatalogDetailModelDB() {
    }

    private CatalogDetailModelDB(Builder builder) {
        setDetailCatalogId(builder.detailCatalogId);
        setDetailCatalogData(builder.detailCatalogData);
        setExpiredTime(builder.expiredTime);
    }

    public String getDetailCatalogId() {
        return detailCatalogId;
    }

    public void setDetailCatalogId(String detailCatalogId) {
        this.detailCatalogId = detailCatalogId;
    }

    public String getDetailCatalogData() {
        return detailCatalogData;
    }

    public void setDetailCatalogData(String detailCatalogData) {
        this.detailCatalogData = detailCatalogData;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }


    public static final class Builder {
        private String detailCatalogId;
        private String detailCatalogData;
        private long expiredTime;

        public Builder() {
        }

        public Builder detailCatalogId(String val) {
            detailCatalogId = val;
            return this;
        }

        public Builder detailCatalogData(String val) {
            detailCatalogData = val;
            return this;
        }

        public CatalogDetailModelDB build() {
            this.expiredTime = System.currentTimeMillis() + 1000 * 60 * 60;
            return new CatalogDetailModelDB(this);
        }
    }
}
