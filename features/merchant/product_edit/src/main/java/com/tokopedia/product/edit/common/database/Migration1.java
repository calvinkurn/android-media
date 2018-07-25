package com.tokopedia.product.edit.common.database;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.tokopedia.product.edit.common.data.source.database.ProductDraftDataBase;

/**
 * Created by User on 6/22/2017.
 */

@Migration(version = 3, database = TkpdSellerDatabase.class)
public class Migration1 extends AlterTableMigration<ProductDraftDataBase> {

    public Migration1(Class<ProductDraftDataBase> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
        addColumn(SQLiteType.INTEGER, ProductDraftDataBase.COLUMN_IS_UPLOADING);
    }
}
