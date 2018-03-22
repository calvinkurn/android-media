package com.tokopedia.cacheapi.data.source.db.migration;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.tokopedia.cacheapi.data.source.db.DbFlowDatabase;
import com.tokopedia.cacheapi.data.source.db.model.CacheApiWhitelist;

/**
 * Created by hendry on 6/22/2017.
 */

/**
 * Add column dynamic link
 */
@Migration(version = 2, database = DbFlowDatabase.class)
public class CacheApiWhiteListMigrationV2 extends AlterTableMigration<CacheApiWhitelist> {

    public CacheApiWhiteListMigrationV2(Class<CacheApiWhitelist> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
        addColumn(SQLiteType.INTEGER, CacheApiWhitelist.COLUMN_ID);
        addColumn(SQLiteType.INTEGER, CacheApiWhitelist.COLUMN_DYNAMIC_LINK);
    }
}
