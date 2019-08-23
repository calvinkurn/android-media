package com.tokopedia.core.gcm.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by alvarisi on 2/22/17.
 */
@Entity
public class DbPushNotification {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "response")
    private String response;

    @ColumnInfo(name = "customIndex")
    private String customIndex;

    @ColumnInfo(name = "serverId")
    private String serverId;

    public DbPushNotification() {
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return this.response;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

    public String getCustomIndex() {
        return customIndex;
    }

    public void setCustomIndex(String customIndex) {
        this.customIndex = customIndex;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
