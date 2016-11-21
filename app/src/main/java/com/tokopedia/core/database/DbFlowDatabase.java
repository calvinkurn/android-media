package com.tokopedia.core.database;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.core.database.model.RechargeOperatorModelDBAttrs_Table;

/**
 * Created by Nathan on 9/30/2016.
 */
@Database(name = DbFlowDatabase.NAME, version = DbFlowDatabase.VERSION, foreignKeysSupported = true)
public class DbFlowDatabase {

    public static final String NAME = "tokopedia";

    public static final int VERSION = 5;

    @Migration(version = DbFlowDatabase.VERSION, database = DbFlowDatabase.class)
    public static class Migration2 extends BaseMigration {

        @Override
        public void migrate(DatabaseWrapper database) {
            //database.execSQL("DROP TABLE IF EXISTS RechargeOperatorModelDB_Table");
            //database.execSQL("ALTER TABLE RechargeOperatorModelDBAttrs ADD COLUMN maximumLength INTEGER DEFAULT 0");
            //database.execSQL("DROP TABLE IF EXISTS " + RechargeOperatorModelDBAttrs.class);
            /*SQLite.update(RechargeOperatorModelDBAttrs.class)
                    .set(Employee_Table.status.eq("Invalid"))
                    .where(Employee_Table.job.eq("Laid Off"))
                    .execute(database); // required inside a migration to pass the wrapper*/
        }
    }
}
