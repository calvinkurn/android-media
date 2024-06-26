package com.tokopedia.filter.newdynamicfilter.helper

import android.content.Context

import com.tokopedia.filter.newdynamicfilter.database.FilterDBModel
import com.tokopedia.filter.newdynamicfilter.database.FilterDatabaseClient

class DynamicFilterDbManager {

    companion object {
        @JvmStatic
        fun store(context: Context, filterID: String, filterData: String) {
            FilterDatabaseClient
                    .getInstance(context)
                    .filterDatabase
                    .filterDao()
                    .insert(FilterDBModel(filterID, filterData))
        }

        @JvmStatic
        fun getFilterData(context: Context, filterId: String): String {
            return FilterDatabaseClient
                    .getInstance(context)
                    .filterDatabase
                    .filterDao()
                    .getFilterDataById(filterId).filterData
        }
    }
}
