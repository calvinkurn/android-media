package com.tokopedia.topupbills.telco.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topupbills.telco.data.TelcoFilterTagComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TelcoFilterViewModel @Inject constructor(private val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _filterData = ArrayList<HashMap<String, Any>>()
    private val _filterTags = mutableListOf<TelcoFilterTagComponent>()

    /**
     * example filter data param
     * "filterData": [
            {
                "value": [
                    "621"
                ],
                "param_name": "filter_tag_tipe_kuota"
            }
        ]
     */

    fun addFilter(paramName: String, valuesFilter: List<String>) {
        var addNewObj = true
        _filterData.map {
            if (it.containsValue(paramName)) {
                it[FILTER_VALUE] = valuesFilter
                addNewObj = false
            }
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
    }

    fun getAllFilter(): ArrayList<HashMap<String, Any>> {
        return _filterData
    }

    fun isFilterSelected(): Boolean {
        return _filterData.isNotEmpty()
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
    }
}