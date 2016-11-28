package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;

/**
 * @author ricoharisin on 7/14/16.
 */
@Table(database = DbFlowDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class RechargeOperatorModelDBAttrs extends BaseModel {

    @PrimaryKey
    @Column
    public String prefix;

    @Column
    public String name;

    @Column
    public int operatorId;

    @Column
    public String image;

    @Column
    public int status;

    @Column
    public int minimumLength;

    @Column
    public int maximumLength;

    @Column
    public String nominalText;

    @Column
    public Boolean showPrice;

    @Column
    public Boolean showProduct;

    @Column
    public Integer weight;

    @Override
    public String toString() {
        return name;
    }
}