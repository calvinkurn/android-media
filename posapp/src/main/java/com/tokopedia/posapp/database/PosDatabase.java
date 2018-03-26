package com.tokopedia.posapp.database;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.posapp.database.model.CartDb;

/**
 * Created by okasurya on 9/8/17.
 */

@Database(name = PosDatabase.NAME, version = PosDatabase.VERSION, foreignKeysSupported = true)
@ModelContainer
public class PosDatabase {
        public static final String NAME = "tokopedia_pos";

        public static final int VERSION = 2;
}
