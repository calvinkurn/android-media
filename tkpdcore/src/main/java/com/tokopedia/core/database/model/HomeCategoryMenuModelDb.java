package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;

/**
 * @author by mady on 9/23/16.
 */
@ModelContainer
@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE)
public class HomeCategoryMenuModelDb extends BaseModel {

    @Column
    @PrimaryKey
    int id;

    @Column
    public String contentHomeCategory;

    @Column
    private long lastUpdated;

    public HomeCategoryMenuModelDb() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentHomeCategory() {
        return contentHomeCategory;
    }

    public void setContentHomeCategory(String contentHomeCategory) {
        this.contentHomeCategory = contentHomeCategory;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}