package com.tokopedia.analyticsdebugger.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author okasurya on 5/14/18.
 */

@Entity(tableName = FpmLogDB.NAME)
public class FpmLogDB {
    static final String NAME = "fpm_log";

    @PrimaryKey(autoGenerate = true)
    long id;

    @ColumnInfo(name = "tracename")
    String traceName;

    @ColumnInfo(name = "metrics")
    String metrics;

    @ColumnInfo(name = "attributes")
    String attributes;

    @ColumnInfo(name = "duration")
    long duration;

    @ColumnInfo(name = "timestamp")
    long timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTraceName() {
        return traceName;
    }

    public void setTraceName(String traceName) {
        this.traceName = traceName;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}