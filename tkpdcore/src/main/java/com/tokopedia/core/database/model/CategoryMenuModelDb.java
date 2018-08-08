/*
 * Created By Kulomady on 10/12/16 10:49 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 10/12/16 10:49 PM
 */

package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;

import java.util.ArrayList;

/**
 * @author by mady on 9/23/16.
 */
@ModelContainer
@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE)
public class CategoryMenuModelDb extends BaseModel {

    @Column
    public String headerTitle;

    public ArrayList<CategoryItemModelDb> allItemsInSection;

    @Column
    @PrimaryKey
    long id;

    @Column
    private long lastUpdated;

    public CategoryMenuModelDb() {

    }
    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "allItemsInSection")
    public ArrayList<CategoryItemModelDb> getAllItemsInSection() {
        if (allItemsInSection == null || allItemsInSection.isEmpty()) {
            allItemsInSection = (ArrayList<CategoryItemModelDb>) SQLite.select()
                    .from(CategoryItemModelDb.class)
                    .where(CategoryItemModelDb_Table.categoryMenuForeignKeyContainer_id.eq(id))
                    .queryList();
        }

        return allItemsInSection;

    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
