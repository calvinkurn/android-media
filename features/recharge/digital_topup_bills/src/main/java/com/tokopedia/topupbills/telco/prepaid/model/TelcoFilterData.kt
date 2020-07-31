package com.tokopedia.topupbills.telco.prepaid.model

import com.tokopedia.topupbills.telco.data.TelcoFilterTagComponent

class TelcoFilterData {

    private val _filterData = ArrayList<HashMap<String, Any>>()
    private val _filterTags = mutableListOf<TelcoFilterTagComponent>()

    /**
     * example filter data param
     * "filterData": [
     *      {
     *          "value": [
     *          "621"
     *          ],
     *          "param_name": "filter_tag_tipe_kuota"
     *      },
     *          {param_name: "apply_filter_to_component_id", value: ["8"]}
     *      ]
     *
     */

    fun addFilter(componentId: Int, paramName: String, valuesFilter: List<String>) {
        var addNewObj = true
        var addApplyFilter = true
        _filterData.map {
            if (it.containsValue(paramName)) {
                it[FILTER_VALUE] = valuesFilter
                addNewObj = false
            }

            if (it.containsValue(FILTER_COMPONENT_ID)) {
                addApplyFilter = false
            }
        }

        //add "apply_filter_to_component_id"
        if (addApplyFilter) {
            val valueItem = HashMap<String, Any>()
            val list = ArrayList<String>()
            list.add(componentId.toString())
            valueItem[FILTER_PARAM_NAME] = FILTER_COMPONENT_ID
            valueItem[FILTER_VALUE] = list
            _filterData.add(valueItem)
        }

        if (addNewObj) {
            val valueItem = HashMap<String, Any>()
            valueItem[FILTER_PARAM_NAME] = paramName
            valueItem[FILTER_VALUE] = valuesFilter
            _filterData.add(valueItem)
        }

        if (getFilterSelectedByParamName(paramName).size == 0) {
            removeFilter(paramName)
        }

        if (_filterData.size == 1 && _filterData[0].containsValue(FILTER_COMPONENT_ID)) {
            removeFilter(FILTER_COMPONENT_ID)
        }
    }

    fun getAllFilter(): ArrayList<HashMap<String, Any>> {
        return _filterData
    }

    fun clearAllFilter() {
        _filterData.clear()
    }

    fun isFilterSelected(): Boolean {
        return _filterData.isNotEmpty()
    }

    fun isFilterSelectedByParamName(paramName: String): Boolean {
        return getFilterSelectedByParamName(paramName).size > 0
    }

    fun getFilterSelectedByParamName(paramName: String): ArrayList<String> {
        val arrayList = ArrayList<String>()
        _filterData.map {
            if (it.containsValue(paramName)) {
                val valuesFilter = it[FILTER_VALUE]
                arrayList.addAll(valuesFilter as ArrayList<String>)
            }
        }
        return arrayList
    }

    private fun removeFilter(paramName: String) {
        val iterator = _filterData.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.containsValue(paramName)) {
                iterator.remove()
            }
        }
    }

    fun setFilterTags(filterTags: List<TelcoFilterTagComponent>) {
        this._filterTags.clear()
        this._filterTags.addAll(filterTags)
    }

    fun getFilterTags(): List<TelcoFilterTagComponent> {
        return _filterTags
    }

    companion object {
        const val FILTER_PARAM_NAME = "param_name"
        const val FILTER_VALUE = "value"
        const val FILTER_COMPONENT_ID = "apply_filter_to_component_id"
    }
}