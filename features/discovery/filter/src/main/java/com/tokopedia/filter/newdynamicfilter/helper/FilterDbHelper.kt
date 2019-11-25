package com.tokopedia.filter.newdynamicfilter.helper

import android.content.Context

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option

/**
 * Created by henrypriyono on 11/27/17.
 */

object FilterDbHelper {
    fun storeLocationFilterOptions(context: Context, optionList: List<Option>) {

        val listType = object : TypeToken<List<Option>>() {}.type
        val gson = Gson()
        val optionData = gson.toJson(optionList, listType)

        DynamicFilterDbManager.store(context, Filter.TEMPLATE_NAME_LOCATION, optionData)
    }

    fun loadLocationFilterOptions(context: Context): List<Option> {
        val data = DynamicFilterDbManager.getFilterData(context, Filter.TEMPLATE_NAME_LOCATION)
        val listType = object : TypeToken<List<Option>>() {}.type
        return Gson().fromJson(data, listType)
    }
}
