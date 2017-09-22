package com.tokopedia.core.database.migration;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.database.DbFlowDatabase;

/**
 * Created by nathan on 9/22/17.
 */
@Migration(version = 14, database = DbFlowDatabase.class)
public class DBFlowMigration extends BaseMigration {

    @Override
    public void migrate(DatabaseWrapper database) {
        database.execSQL("DROP TABLE IF EXISTS " + CacheApiData.class.getSimpleName());
        database.execSQL(new CacheApiData().getModelAdapter().getCreationQuery());
    }
}
