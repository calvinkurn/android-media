package com.tokopedia.common.network.data.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = DbRestMetadata.NAME)
public class RestDatabaseModel {
    @PrimaryKey
    @NonNull
    public String key = "";
    public String value;
    public long expiredTime = 0;

    public RestDatabaseModel() {
    }

    @NonNull
    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }
}
