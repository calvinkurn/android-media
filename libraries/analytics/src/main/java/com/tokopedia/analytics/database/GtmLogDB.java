package com.tokopedia.analytics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @author okasurya on 5/14/18.
 */

@Table(name = GtmLogDB.NAME, database = TkpdAnalyticsDatabase.class)
public class GtmLogDB extends BaseModel {
    public static final String NAME = "gtm_log";

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String data;

    @Column
    String name;

    @Column
    String category;

    @Column
    long timestamp;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}