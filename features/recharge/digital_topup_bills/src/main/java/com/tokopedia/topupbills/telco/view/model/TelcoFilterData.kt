package com.tokopedia.topupbills.telco.view.model

import com.tokopedia.topupbills.telco.data.TelcoFilterTagComponent

class TelcoFilterData {
    private val filterTags = mutableListOf<TelcoFilterTagComponent>()
    private val filterData = ArrayList<HashMap<String, Any>>()

    fun addFilter(paramName: String, valuesFilter: List<String>) {
        val valueItem = HashMap<String, Any>()
        valueItem[FILTER_PARAM_NAME] = paramName
        valueItem[FILTER_VALUE] = valuesFilter
        filterData.add(valueItem)
    }

    fun setFilterTags(filterTags: List<TelcoFilterTagComponent>) {
        this.filterTags.clear()
        this.filterTags.addAll(filterTags)
    }

    fun getFilterTags(): List<TelcoFilterTagComponent> {
        return filterTags
    }

    fun getAllFilter(): ArrayList<HashMap<String, Any>> {
        return filterData
    }

    fun removeFilter(paramName: String) {
        val iterator = filterData.iterator()
        while(iterator.hasNext()){
            val item = iterator.next()
            if (item.containsValue(paramName)) {
                iterator.remove()
            }
        }
    }

    fun isFilterSelected(): Boolean {
        return filterData.size > 0
    }

    companion object {
        const val FILTER_PARAM_NAME = "param_name"
        const val FILTER_VALUE = "value"
    }
}