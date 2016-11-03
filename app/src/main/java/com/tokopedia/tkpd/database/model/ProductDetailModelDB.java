package com.tokopedia.tkpd.database.model;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.tkpd.database.DbFlowDatabase;

/**
 * Created by ricoharisin on 12/3/15.
 */

@Table(database = DbFlowDatabase.class, primaryKeyConflict = ConflictAction.REPLACE)
public class ProductDetailModelDB extends BaseModel {

    @PrimaryKey
    @Column
    public String productID;

    @Column
    public String productData;

    @Column
    public long expiredTime;
}