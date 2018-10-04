package com.tokopedia.train.common.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * @author by alvarisi on 3/7/18.
 */

@Database(name = TkpdTrainDatabase.NAME, version = TkpdTrainDatabase.VERSION, foreignKeysSupported = true)
public class TkpdTrainDatabase {
    public static final String NAME = "tkpd_train_database";

    public static final int VERSION = 1;
}
