
package com.tokopedia.core.database;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.ModelContainer;

/**
 * Created by Nathan on 9/30/2016.
 */
@Database(name = CoreLegacyDbFlowDatabase.NAME, version = CoreLegacyDbFlowDatabase.VERSION, foreignKeysSupported = true)
@ModelContainer
public class CoreLegacyDbFlowDatabase {

    public static final String NAME = "core";

    public static final int VERSION = 1;

}
