package com.tokopedia.settingbank.choosebank.data.db;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.ModelContainer;

/**
 * @author by nisie on 7/24/18.
 */
@Database(name = BankDatabase.NAME, version = BankDatabase.VERSION,
        foreignKeysSupported = true)
@ModelContainer
public class BankDatabase {

    public static final String NAME = "tkpdbankdb";

    public static final int VERSION = 1;

}
