package com.tokopedia.tkpd.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.tkpd.database.DbFlowDatabase;

/**
 * Created by hangnadi on 2/19/16.
 */
@Table(database = DbFlowDatabase.class, primaryKeyConflict = ConflictAction.REPLACE)
public class StateResCenterDetailDB extends BaseModel {

    @PrimaryKey
    @Column
    public String resolutionID;

    @Column
    public int byCustomer;

    @Column
    public int  bySeller;

    @Column
    public int lastFlagReceived;

    @Column
    public int lastSolutionType;

    @Column
    public String lastSolutionString;

    @Column
    public int lastTroubleType;

    @Column
    public String lastTroubleString;

    @Column
    public String orderPriceFmt;

    @Column
    public int orderPriceRaw;

    @Column
    public String shippingPriceFmt;

    @Column
    public int shippingPriceRaw;
}