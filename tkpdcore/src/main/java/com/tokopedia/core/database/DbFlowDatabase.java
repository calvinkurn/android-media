package com.tokopedia.core.database;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.ModelContainer;

/**
 * Created by Nathan on 9/30/2016.
 */
@Database(name = DbFlowDatabase.NAME, version = DbFlowDatabase.VERSION, foreignKeysSupported = true)
@ModelContainer
public class DbFlowDatabase {

    public static final String NAME = "tokopedia";

    public static final int VERSION = 15;

}
