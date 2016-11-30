package com.tokopedia.core.database;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Created by Nathan on 9/30/2016.
 */
@Database(name = DbFlowDatabase.NAME, version = DbFlowDatabase.VERSION, foreignKeysSupported = true)
public class DbFlowDatabase {

    public static final String NAME = "tokopedia";

    public static final int VERSION = 4;

    @Migration(version = DbFlowDatabase.VERSION, database = DbFlowDatabase.class)
    public static class Migration2 extends BaseMigration {

        @Override
        public void migrate(DatabaseWrapper database) {
            //database.execSQL("DROP TABLE IF EXISTS RechargeOperatorModelDB_Table");
        }
    }
}
