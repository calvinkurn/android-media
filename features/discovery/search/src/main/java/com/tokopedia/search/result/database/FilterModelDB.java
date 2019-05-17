package com.tokopedia.search.result.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.cacheapi.data.source.db.DbFlowDatabase;

@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FilterModelDB extends BaseModel {

    @PrimaryKey
    @Column
    public String filterId;

    @Column
    public String filterData;
}