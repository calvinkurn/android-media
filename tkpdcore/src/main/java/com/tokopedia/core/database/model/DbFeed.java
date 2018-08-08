package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;

/**
 * @author Kulomady on 1/3/17.
 */

@ModelContainer
@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE)
public class DbFeed extends BaseModel {

    @Column
    @PrimaryKey
    int id;

    @Column
    public String contentFeed;

    @Column
    private long lastUpdated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentFeed() {
        return contentFeed;
    }

    public void setContentFeed(String contentFeed) {
        this.contentFeed = contentFeed;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
