package com.tokopedia.bankdb;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.ModelContainer;

/**
 * Created by Nathan on 9/30/2016.
 */
@Database(name = BankFlowDatabase.NAME, version = BankFlowDatabase.VERSION, foreignKeysSupported = true)
@ModelContainer
public class BankFlowDatabase {

    public static final String NAME = "tkpdBank";

    public static final int VERSION = 1;

}
