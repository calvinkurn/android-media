package com.tokopedia.filter.newdynamicfilter.helper;

import android.content.Context;

import com.tokopedia.filter.newdynamicfilter.database.FilterDBModel;
import com.tokopedia.filter.newdynamicfilter.database.FilterDatabaseClient;

/**
 * Created by nakama on 11/23/17.
 */

public class DynamicFilterDbManager {

    public static void store(Context context, String filterID, String filterData) {
        FilterDatabaseClient
                .getInstance(context)
                .getFilterDatabase()
                .filterDao()
                .insert(new FilterDBModel(filterID, filterData));
    }

    public static String getFilterData(Context context, String filterId) {
        return FilterDatabaseClient
                .getInstance(context)
                .getFilterDatabase()
                .filterDao()
                .getFilterDataById(filterId).getFilterData();
    }
}
