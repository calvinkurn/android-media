package com.tokopedia.privacycenter.dsar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.dsar.model.ItemRangeModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DsarViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val itemRangeData = MutableLiveData<ArrayList<ItemRangeModel>>()
    val _itemRangeData: LiveData<ArrayList<ItemRangeModel>> = itemRangeData

    private val filterItems = MutableLiveData<ArrayList<String>>()
    val _filterItems: LiveData<ArrayList<String>> = filterItems

    private val startDate = MutableLiveData<String>()
    val _startDate: LiveData<String> = startDate

    private val endDate = MutableLiveData<String>()
    val _endDate: LiveData<String> = endDate

    fun setRangeItems(items: ArrayList<ItemRangeModel>) {
        itemRangeData.value = items
    }

    fun getSelectedRangeItems(): ItemRangeModel? {
        return itemRangeData.value?.first { it.selected }
    }

    fun setSelectedRangeItems(id: Int) {
        itemRangeData.value?.forEach {
            it.selected = it.id == id
        }
        itemRangeData.value = itemRangeData.value
    }

    fun populateRangeItems() {
        itemRangeData.value?.clear()
        val items = arrayListOf(
            ItemRangeModel(0, "Selama Tahun Ini"),
            ItemRangeModel(1, "3 Tahun Terakhir"),
            ItemRangeModel(2, "3 Bulan Terakhir"),
            ItemRangeModel(3, "30 Hari Terakhir"),
            ItemRangeModel(4, "7 Hari Terakhir"),
            ItemRangeModel(5, "Pilih Tanggal Sendiri")
        )
        itemRangeData.value = items
    }

    fun addFilter(filter: String) {
        if((filterItems.value?.count() ?: 0) < 3) {
            filterItems.value?.add(filter)
        }
    }

    fun removeFilter(filter: String) {
        filterItems.value?.remove(filter)
    }

    fun applyTransactionHistoryFilter() {
        if(itemRangeData.value?.isNotEmpty() == true) {
            println("selected_item ${itemRangeData.value!!.first { it.selected }}")
        } else {

        }
    }

    fun setStartDate(date: Date) {
        startDate.value = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date).toString()
    }

    fun setEndDate(date: Date) {
        endDate.value = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date).toString()
    }

    fun showSummary() {

    }
}
