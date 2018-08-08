package com.tokopedia.core.database.model;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;

/**
 * Created by stevenfredian on 2/23/16.
 */

@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class ProductReviewModelDB extends BaseModel {

    @PrimaryKey
    @Column
    public String productID;

    @Column
    public String jsonResult;

    @Column
    public long expiredTime;
}