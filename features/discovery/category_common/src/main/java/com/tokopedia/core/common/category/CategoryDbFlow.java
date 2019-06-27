package com.tokopedia.core.common.category;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdCategoryGeneratedDatabaseHolder;

public class CategoryDbFlow {

    public static void initDatabase(Context applicationContext) {
        // should only initDatabase once per application, if the config is null, initialize it.
        try {
            FlowManager.getConfig();
        } catch (IllegalStateException e) {
            FlowManager.init(new FlowConfig.Builder(applicationContext).build());
        }

        FlowManager.initModule(TkpdCategoryGeneratedDatabaseHolder.class);
    }
}
