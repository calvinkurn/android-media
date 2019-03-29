package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DatabaseConstant;
import com.tokopedia.core.database.DbFlowDatabase;

import java.util.List;

/**
 * Created by m.normansyah on 12/27/15.
 */
@ModelContainer
@Table(database = DbFlowDatabase.class)
public class ProductDB extends BaseModel implements DatabaseConstant {

    public static final String PRODUCT_IMAGE = "product_image";
    public static final String PRODUCT_KONDISI = "product_kondisi";
    public static final String PRODUCT_ASURANSI = "product_asuransi";
    public static final String PRODUCT_PENGEMBALIAN = "product_pengembalian";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_HARGA = "product_harga";
    public static final String PRODUCT_BERAT = "product_berat";
    public static final String PRODUCT_ORDER_MINIMAL = "product_order_minimal";
    public static final String PRODUCT_DESKRIPSI = "product_deskripsi";
    public static final String PRODUCT_SYNC_TO_SERVER = "product_sync_to_server";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_URL = "product_url";
    public static final String PRODUCT_BERAT_UNIT = "product_berat_unit";
    public static final String PRODUCT_KATEGORI = "product_kategori";
    public static final String PRODUCT_MATA_UANG = "product_mata_uang";
    public static final String PRODUCT_KEBIJAKAN_PENGEMBALIAN = "product_kebijakan_pengembalian";
    public static final String PRODUCT_ETALASE = "product_etalase";
    public static final String PRODUCT_PREORDER = "product_preorder";
    public static final String PRODUCT_CATALOG = "product_catalog";
    public static final String PRODUCT_STOCK_STATUS = "product_stock_status";

    public ProductDB(){super();}

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Column
    public int conditionProd;
    @Column
    public int assuranceProd;
    @Column
    public int returnableProd;

    @ForeignKey(
            saveForeignKeyModel = true,
            onDelete = ForeignKeyAction.CASCADE,
            onUpdate = ForeignKeyAction.CASCADE,
            references = {
                    @ForeignKeyReference(
                            columnType = long.class,
                            columnName = "kebijakanReturnableDB",
                            foreignKeyColumnName = ID
                    )
            }
    )
    @Column
    public ReturnableDB kebijakanReturnableDB;

    @ForeignKey(
            saveForeignKeyModel = true,
            onDelete = ForeignKeyAction.CASCADE,
            onUpdate = ForeignKeyAction.CASCADE,
            references = {
                    @ForeignKeyReference(
                            columnType = long.class,
                            columnName = "etalaseDB",
                            foreignKeyColumnName = ID
                    )
            }
    )
    @Column
    public EtalaseDB etalaseDB;

    @Column
    public String nameProd;

    @ForeignKey(
            saveForeignKeyModel = true,
            onDelete = ForeignKeyAction.CASCADE,
            onUpdate = ForeignKeyAction.CASCADE,
            references = {
                    @ForeignKeyReference(
                            columnType = long.class,
                            columnName = "categoryDB",
                            foreignKeyColumnName = ID
                    )
            }
    )
    @Column
    public CategoryDB categoryDB;

    @ForeignKey(
            saveForeignKeyModel = true,
            onDelete = ForeignKeyAction.CASCADE,
            onUpdate = ForeignKeyAction.CASCADE,
            references = {
                    @ForeignKeyReference(
                            columnType = long.class,
                            columnName = "unitCurrencyDB",
                            foreignKeyColumnName = ID
                    )
            }
    )
    @Column
    public CurrencyDB unitCurrencyDB;
    @Column
    public double priceProd;
    @Column
    public int weightProd;

    @ForeignKey(
            saveForeignKeyModel = true,
            onDelete = ForeignKeyAction.CASCADE,
            onUpdate = ForeignKeyAction.CASCADE,
            references = {
                    @ForeignKeyReference(
                            columnType = long.class,
                            columnName = "weightUnitDB",
                            foreignKeyColumnName = ID
                    )
            }
    )
    @Column
    public WeightUnitDB weightUnitDB;
    @Column
    public int minOrderProd;
    @Column
    public String descProd;
    @Column
    public int syncToServer;
    @Column
    public int productId;
    @Column
    public String productUrl;
    @Column
    public int productPreOrder;
    @Column
    public long catalogid;

    @ForeignKey(
            saveForeignKeyModel = true,
            onDelete = ForeignKeyAction.CASCADE,
            onUpdate = ForeignKeyAction.CASCADE,
            references = {
                    @ForeignKeyReference(
                            columnName = "stockStatusDB",
                            columnType = Long.class,
                            foreignKeyColumnName = ID
                    )
            }
    )
    @Column
    public StockStatusDB stockStatusDB;

    List<PictureDB> pictureDBs;
    List<WholesalePriceDB> wholesalePriceDBs;

    @Column
    public String priceFormatted;

    public int getConditionProd() {
        return conditionProd;
    }

    public void setConditionProd(int conditionProd) {
        this.conditionProd = conditionProd;
    }

    public int getAssuranceProd() {
        return assuranceProd;
    }

    public void setAssuranceProd(int assuranceProd) {
        this.assuranceProd = assuranceProd;
    }

    public int getReturnableProd() {
        return returnableProd;
    }

    public void setReturnableProd(int returnableProd) {
        this.returnableProd = returnableProd;
    }

    public ReturnableDB getKebijakanReturnableDB() {
        return kebijakanReturnableDB;
    }

    public void setKebijakanReturnableDB(ReturnableDB kebijakanReturnableDB) {
        this.kebijakanReturnableDB = kebijakanReturnableDB;
    }

    public EtalaseDB getEtalaseDB() {
        return etalaseDB;
    }

    public void setEtalaseDB(EtalaseDB etalaseDB) {
        this.etalaseDB = etalaseDB;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "pictureDBs")
    public List<PictureDB> getImages(){
        if(pictureDBs == null || pictureDBs.isEmpty()){
            pictureDBs = SQLite.select()
                    .from(PictureDB.class)
                    .where(PictureDB_Table.productDbContainer_Id.eq(Id))
                    .queryList();
        }
        return pictureDBs;
    }

    public List<WholesalePriceDB> getWholeSales(){
        if(wholesalePriceDBs == null || wholesalePriceDBs.isEmpty()){
            wholesalePriceDBs = SQLite.select()
                    .from(WholesalePriceDB.class)
                    .where(WholesalePriceDB_Table.productDbContainer_Id.eq(Id))
                    .queryList();
        }
        return wholesalePriceDBs;
    }

    public String getNameProd() {
        return nameProd;
    }

    public void setNameProd(String nameProd) {
        this.nameProd = nameProd;
    }

    public CategoryDB getCategoryDB() {
        return categoryDB;
    }

    public void setCategoryDB(CategoryDB categoryDB) {
        this.categoryDB = categoryDB;
    }

    public CurrencyDB getUnitCurrencyDB() {
        return unitCurrencyDB;
    }

    public void setUnitCurrencyDB(CurrencyDB unitCurrencyDB) {
        this.unitCurrencyDB = unitCurrencyDB;
    }

    public double getPriceProd() {
        return priceProd;
    }

    public void setPriceProd(double priceProd) {
        this.priceProd = priceProd;
    }

    public int getWeightProd() {
        return weightProd;
    }

    public void setWeightProd(int weightProd) {
        this.weightProd = weightProd;
    }

    public WeightUnitDB getWeightUnitDB() {
        return weightUnitDB;
    }

    public void setWeightUnitDB(WeightUnitDB weightUnitDB) {
        this.weightUnitDB = weightUnitDB;
    }

    public int getMinOrderProd() {
        return minOrderProd;
    }

    public void setMinOrderProd(int minOrderProd) {
        this.minOrderProd = minOrderProd;
    }

    public String getDescProd() {
        return descProd;
    }

    public void setDescProd(String descProd) {
        this.descProd = descProd;
    }

    public int getSyncToServer() {
        return syncToServer;
    }

    public void setSyncToServer(int syncToServer) {
        this.syncToServer = syncToServer;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public List<WholesalePriceDB> getWholesalePriceDBs() {
        return wholesalePriceDBs;
    }

    public int getProductPreOrder() {
        return productPreOrder;
    }

    public void setProductPreOrder(int productPreOrder) {
        this.productPreOrder = productPreOrder;
    }

    public void setWholesalePriceDBs(List<WholesalePriceDB> wholesalePriceDBs) {
        this.wholesalePriceDBs = wholesalePriceDBs;
    }

    public List<PictureDB> getPictureDBs() {
        return pictureDBs;
    }

    public void setPictureDBs(List<PictureDB> pictureDBs) {
        this.pictureDBs = pictureDBs;
    }

    public long getCatalogid() {
        return catalogid;
    }

    public void setCatalogid(long catalogid) {
        this.catalogid = catalogid;
    }

    public StockStatusDB getStockStatusDB() {
        return stockStatusDB;
    }

    public void setStockStatusDB(StockStatusDB stockStatusDB) {
        this.stockStatusDB = stockStatusDB;
    }

    public String getPriceFormatted() {
        return priceFormatted;
    }

    public void setPriceFormatted(String priceFormatted) {
        this.priceFormatted = priceFormatted;
    }

    @Override
    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }
}
