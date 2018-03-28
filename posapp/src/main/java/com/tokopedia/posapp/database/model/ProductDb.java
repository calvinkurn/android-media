package com.tokopedia.posapp.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.posapp.database.PosDatabase;
import com.tokopedia.posapp.database.model.container.BankDbContainer;

import java.util.List;

/**
 * Created by okasurya on 8/30/17.
 */

@ModelContainer
@Table(database = PosDatabase.class, insertConflict = ConflictAction.REPLACE)
public class ProductDb extends BaseModel {
    @Column
    @PrimaryKey
    private long productId;

    @Column
    private String productName;

    @Column
    private String productPrice;

    @Column
    private double productPriceUnformatted;

    @Column
    private String productUrl;

    @Column
    private String productDescription;

    @Column
    private String productImage;

    @Column
    private String productImage300;

    @Column
    private String productImageFull;

    @Column
    private String etalaseId;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductPriceUnformatted() {
        return productPriceUnformatted;
    }

    public void setProductPriceUnformatted(double productPriceUnformatted) {
        this.productPriceUnformatted = productPriceUnformatted;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductImage300() {
        return productImage300;
    }

    public void setProductImage300(String productImage300) {
        this.productImage300 = productImage300;
    }

    public String getProductImageFull() {
        return productImageFull;
    }

    public void setProductImageFull(String productImageFull) {
        this.productImageFull = productImageFull;
    }

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }
}
