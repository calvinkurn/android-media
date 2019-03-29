package com.tokopedia.core.database.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.DatabaseConstant;

/**
 * Created by Toped18 on 8/2/2016.
 * modified by m.normansyah on 6/10/2016
 */
@ModelContainer
@Table(database = DbFlowDatabase.class)
public class CatalogItemDB extends BaseModel implements DatabaseConstant {
    public static final String CATALOG_NAME = "catalog_name";
    public static final String CATALOG_DESCRIPTION = "catalog_description";
    public static final String CATALOG_IMAGE = "catalog_image";
    public static final String CATALOG_COUNT_SHOP = "catalog_count_shop";
    public static final String CATALOG_PRICE = "catalog_price";
    public static final String CATALOG_ID = "catalog_id";
    public static final String CATALOG_URI = "catalog_uri";

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Override
    public long getId() {
        return Id;
    }

    @Column
    String catalogName;

    @Column
    String catalogDescription;

    @Column
    String catalogImage;

    @Column
    String catalogCountShop;

    @Column
    String catalogPrice;

    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    @Column
    String catalogId;

    @Column
    String catalogUri;

    @Column
    @ForeignKey
    ForeignKeyContainer<CatalogDB> catalogDBForeignKeyContainer;

    private CatalogDB catalogDB;

    public void associateCatalogDB(CatalogDB catalogDB){
        catalogDBForeignKeyContainer =
                new ForeignKeyContainer<CatalogDB>(FlowManager.getContainerAdapter(CatalogDB.class)
                        .toForeignKeyContainer(catalogDB));
    }


    public CatalogItemDB(){}

    public CatalogItemDB(String catalogName, String catalogDescription,
                         String catalogImage, String catalogCountShop,
                         String catalogPrice, String catalogId,
                         String catalogUri) {
        this.catalogName = catalogName;
        this.catalogDescription = catalogDescription;
        this.catalogImage = catalogImage;
        this.catalogCountShop = catalogCountShop;
        this.catalogPrice = catalogPrice;
        this.catalogId = catalogId;
        this.catalogUri = catalogUri;
    }

    public String getCatalogCountShop() {
        return catalogCountShop;
    }

    public void setCatalogCountShop(String catalogCountShop) {
        this.catalogCountShop = catalogCountShop;
    }

    public String getCatalogDescription() {
        return catalogDescription;
    }

    public void setCatalogDescription(String catalogDescription) {
        this.catalogDescription = catalogDescription;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogImage() {
        return catalogImage;
    }

    public void setCatalogImage(String catalogImage) {
        this.catalogImage = catalogImage;
    }

    public String getCatalogPrice() {
        return catalogPrice;
    }

    public void setCatalogPrice(String catalogPrice) {
        this.catalogPrice = catalogPrice;
    }

    public String getCatalogUri() {
        return catalogUri;
    }

    public void setCatalogUri(String catalogUri) {
        this.catalogUri = catalogUri;
    }


    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public CatalogDB getCatalogDB() {
        if(catalogDBForeignKeyContainer != null){
            return catalogDBForeignKeyContainer.toModel();
        }else {
            return catalogDB;
        }
    }

    public void setCatalogDB(CatalogDB catalogDB) {
        associateCatalogDB(catalogDB);
        this.catalogDB = catalogDB;
    }
}
