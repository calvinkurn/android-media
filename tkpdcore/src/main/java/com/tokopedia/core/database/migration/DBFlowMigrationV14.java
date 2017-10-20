package com.tokopedia.core.database.migration;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.database.DbFlowDatabase;

/**
 * Created by nathan on 9/22/17.
 */
@Migration(version = 14, database = DbFlowDatabase.class)
public class DBFlowMigrationV14 extends BaseMigration {

    @Override
    public void migrate(DatabaseWrapper database) {
        // Drop and re-create table CacheApiData
        ModelAdapter modelAdapter = FlowManager.getModelAdapter(CacheApiData.class);
        CommonUtils.dumper("Drop: " + modelAdapter.getTableName());
        database.execSQL("DROP TABLE IF EXISTS " + modelAdapter.getTableName());
        CommonUtils.dumper("Exec: " + modelAdapter.getCreationQuery());
        database.execSQL(modelAdapter.getCreationQuery());
    }
}
