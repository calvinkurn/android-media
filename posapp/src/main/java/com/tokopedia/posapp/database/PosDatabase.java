package com.tokopedia.posapp.database;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.ModelContainer;

/**
 * Created by okasurya on 9/8/17.
 */

@Database(name = PosDatabase.NAME, version = PosDatabase.VERSION, foreignKeysSupported = true)
@ModelContainer
public class PosDatabase {
    public static final String NAME = "tokopedia_pos";

    public static final int VERSION = 2;
}
