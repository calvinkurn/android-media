package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DatabaseConstant;
import com.tokopedia.core.database.DbFlowDatabase;

/**
 * Created by Toped18 on 8/2/2016.
 * modified by m.normansyah on 6/10/2016
 */
@ModelContainer
@Table(database = DbFlowDatabase.class)
public class StockStatusDB extends BaseModel implements DatabaseConstant {

    public static final String STOCK_DETAIL = "stock_detail";

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public Long Id;

    @Override
    public long getId() {
        return Id;
    }

    public StockStatusDB(){}

    public StockStatusDB(String stockDetail) {
        this.stockDetail = stockDetail;
    }

    @Unique
    @Column
    public String stockDetail;

    public String getStockDetail() {
        return stockDetail;
    }

    public void setStockDetail(String stockDetail) {
        this.stockDetail = stockDetail;
    }

    @Override
    public String toString() {
        return "Stok Info {" +
                "stok detail= \'" + stockDetail + '\'' +
                '}';
    }
}
