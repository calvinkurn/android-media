package com.tokopedia.posapp.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DatabaseConstant;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.posapp.database.PosDatabase;

/**
 * Created by okasurya on 8/22/17.
 */

@ModelContainer
@Table(database = PosDatabase.class)
public class CartDB extends BaseModel {
    @ContainerKey(DatabaseConstant.ID)
    @Column
    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String productId;

    @Column
    private int quantity;

    @Column
    private String outletId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }
}
