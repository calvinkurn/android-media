package com.tokopedia.networklib.data.model;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.ModelContainer;

@Database(name = DbMetadata.NAME, version = DbMetadata.VERSION, foreignKeysSupported = true)
@ModelContainer
public class DbMetadata {

    public static final String NAME = "tokopedia_rest";

    public static final int VERSION = 1;

}
