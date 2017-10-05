package com.tokopedia.core.database.model;

import com.google.gson.annotations.Expose;
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
public class RechargeOperatorModel extends BaseModel {

    @PrimaryKey
    @Column
    @Expose
    public String prefix;

    @Column
    @Expose
    public String name;

    @Column
    @Expose
    public int operatorId;

    @Column
    @Expose
    public String image;

    @Column
    @Expose
    public int status;

    @Column
    @Expose
    public int minimumLength;

    @Column
    @Expose
    public int maximumLength;

    @Column
    @Expose
    public String nominalText;

    @Column
    @Expose
    public boolean showPrice;

    @Column
    @Expose
    public boolean showProduct;

    @Column
    @Expose
    public int weight;

    @Column
    @Expose
    public int defaultProductId;

    @Column
    @Expose
    public boolean allowAlphanumeric;

    @Override
    public String toString() {
        return name;
    }
}