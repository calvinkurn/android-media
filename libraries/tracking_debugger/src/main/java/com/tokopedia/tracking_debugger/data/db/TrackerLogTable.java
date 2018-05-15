package com.tokopedia.tracking_debugger.data.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * @author okasurya on 5/14/18.
 */

@Table(name = "tracker_log", database = TrackingDebuggerDatabase.class)
public class TrackerLogTable {
    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String data;

    @Column
    String name;

    @Column
    String category;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}