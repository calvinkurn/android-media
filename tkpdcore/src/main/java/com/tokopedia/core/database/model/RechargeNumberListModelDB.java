package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;

/**
 * @author rizkyfadillah on 9/28/2017.
 */
@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class RechargeNumberListModelDB extends BaseModel {

    @PrimaryKey
    @Column
    public String clientNumber;

    @Column
    public String name;

    @Column
    public String lastProduct;

    @Column
    public String lastUpdated;

    @Column
    public int categoryId;

}
