/*
 * Created By Kulomady on 10/12/16 10:58 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 10/12/16 10:58 PM
 */

package com.tokopedia.tkpd.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.tokopedia.tkpd.database.DbFlowDatabase;

/**
 * @author by mady on 9/23/16.
 */
@ModelContainer
@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE)
public class CategoryItemModelDb extends BaseModel {

    @PrimaryKey
    @Column
    public int id;
    @Column
    public String name;
    @Column
    public String imageUrl;
    @Column
    public String description;
    @Column
    public String redirectValue;
    @Column
    public int type;
    @Column

    @ForeignKey(saveForeignKeyModel = false)
    ForeignKeyContainer<CategoryMenuModelDb> categoryMenuForeignKeyContainer;
    private CategoryMenuModelDb categoryMenuModelDb;

    public void associateCategoryMenu(CategoryMenuModelDb categoryMenuModelDb) {
        categoryMenuForeignKeyContainer =
                new ForeignKeyContainer<>(FlowManager.getContainerAdapter(CategoryMenuModelDb.class)
                        .toForeignKeyContainer(categoryMenuModelDb));
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CategoryMenuModelDb getCategoryMenuModelDb() {
        return categoryMenuModelDb;
    }

    public void setCategoryMenuModelDb(CategoryMenuModelDb categoryMenuModelDb) {
        this.categoryMenuModelDb = categoryMenuModelDb;
        associateCategoryMenu(categoryMenuModelDb);
    }

    public String getRedirectValue() {
        return redirectValue;
    }

    public void setRedirectValue(String redirectValue) {
        this.redirectValue = redirectValue;
    }

    public void setId(int id) {
        this.id = id;
    }
}
