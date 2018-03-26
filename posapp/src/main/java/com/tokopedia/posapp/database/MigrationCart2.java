package com.tokopedia.posapp.database;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.tokopedia.posapp.database.model.CartDb;

@Migration(version = 2, database = PosDatabase.class)
public class MigrationCart2 extends AlterTableMigration<CartDb> {
    public MigrationCart2(Class<CartDb> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
        addColumn(SQLiteType.TEXT, "name");
        addColumn(SQLiteType.TEXT, "imageUrl");
        addColumn(SQLiteType.REAL, "priceUnformatted");
        addColumn(SQLiteType.TEXT, "price");
    }
}