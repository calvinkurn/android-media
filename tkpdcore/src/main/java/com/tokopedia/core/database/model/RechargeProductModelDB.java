package com.tokopedia.core.database.model;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;

/**
 * @author  by ricoharisin on 7/14/16.
 */
@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class RechargeProductModelDB extends BaseModel {

    @PrimaryKey
    @Column
    public int id;

    @Column
    public int categoryId;

    @Column
    public int OperatorId;

    @Column
    public String product;

    @Column
    public int status;
}