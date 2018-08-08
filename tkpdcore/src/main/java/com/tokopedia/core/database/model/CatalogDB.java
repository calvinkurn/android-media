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
 * Created by Toped18 on 8/3/2016.
 * modified by m.normansyah on 6/10/2016
 */
@ModelContainer
@Table(database = DbFlowDatabase.class)
public class CatalogDB extends BaseModel implements DatabaseConstant {
    public static final String CATALOG_ITEM = "catalog_item";
    public static final String CATALOG_SELECTOR = "catalog_selector";

    @ContainerKey(ID)
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @Override
    public long getId() {
        return Id;
    }

    @Column
    public String productName;

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

    List<CatalogItemDB> catalogItemDBs;

    public CatalogDB() {
        super();
    }

    public CatalogDB(String productName, CategoryDB categoryDB){
        this.productName = productName;
        this.categoryDB = categoryDB;
    }

    public CategoryDB getCategoryDB() {
        return categoryDB;
    }

    public void setCategoryDB(CategoryDB categoryDB) {
        this.categoryDB = categoryDB;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "catalogItemDBs")
    public List<CatalogItemDB> getAllCatalogItemDBs() {
        if (catalogItemDBs == null || catalogItemDBs.isEmpty()) {
            catalogItemDBs = SQLite.select()
                    .from(CatalogItemDB.class)
                    .where(CatalogItemDB_Table.catalogDBForeignKeyContainer_Id.eq(Id))
                    .queryList();
        }
        return catalogItemDBs;
    }
}
