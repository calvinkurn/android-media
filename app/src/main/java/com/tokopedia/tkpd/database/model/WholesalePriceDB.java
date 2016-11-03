package com.tokopedia.tkpd.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.tkpd.database.container.ProductDbContainer;
import com.tokopedia.tkpd.database.DatabaseConstant;
import com.tokopedia.tkpd.database.DbFlowDatabase;

import static com.tokopedia.tkpd.database.model.PictureDB.createProductDbContainer;

/**
 * Created by m.normansyah on 12/27/15.
 * modified by m.normansyah on 6/10/2016
 */
@ModelContainer
@Table(database = DbFlowDatabase.class)
public class WholesalePriceDB extends BaseModel implements DatabaseConstant{

    public static final String HARGA_GROSIR_PRODUCT = "harga_grosir_product";
    private ProductDB productDB;

    public WholesalePriceDB(int min, int max, double priceWholesale) {
        super();
        this.min = min;
        this.max = max;
        this.priceWholesale = priceWholesale;
    }

    public WholesalePriceDB(double min, double max, double priceWholesale) {
        super();
        this.min = (int)min;
        this.max = (int)max;
        this.priceWholesale = priceWholesale;
    }

    public WholesalePriceDB(){
        super();
    }

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Column
    @ForeignKey(onDelete = ForeignKeyAction.CASCADE, onUpdate = ForeignKeyAction.CASCADE)
    ProductDbContainer productDbContainer;

    public void linkToProduct(ProductDB productDB){
        productDbContainer = createProductDbContainer(productDB);
        productDbContainer.toModelForce();
    }

    @Column
    public int min;
    @Column
    public int max;
    @Column
    public double priceWholesale;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public double getPriceWholesale() {
        return priceWholesale;
    }

    public void setPriceWholesale(double priceWholesale) {
        this.priceWholesale = priceWholesale;
    }

    public ProductDB getProduk() {
        if(productDbContainer != null)
            return productDbContainer.toModel();
        return productDB;
    }

    public void setProduk(ProductDB produk) {
        linkToProduct(produk);
        productDB = produk;
    }

    @Override
    public long getId() {
        return Id;
    }
}
