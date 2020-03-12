package com.tokopedia.analyticsdebugger.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = ApplinkLogDB.NAME)
public class ApplinkLogDB {
    static final String NAME = "applink_log";

    @PrimaryKey(autoGenerate = true)
    long id;

    @ColumnInfo(name = "applink")
    String applink;

    @ColumnInfo(name = "traces")
    String traces;

    @ColumnInfo(name = "timestamp")
    long timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public String getTraces() {
        return traces;
    }

    public void setTraces(String traces) {
        this.traces = traces;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}