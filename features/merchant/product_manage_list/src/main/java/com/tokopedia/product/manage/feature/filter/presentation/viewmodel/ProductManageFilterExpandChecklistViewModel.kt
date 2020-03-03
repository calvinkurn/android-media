package com.tokopedia.product.manage.feature.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel
import javax.inject.Inject

class ProductManageFilterExpandChecklistViewModel @Inject constructor(): ViewModel() {

    private val _dataSize = MutableLiveData<Int>()
    val dataSize: LiveData<Int>
    get() = _dataSize

    private val _checklistData = MutableLiveData<MutableList<ChecklistViewModel>>()
    val checklistData: LiveData<MutableList<ChecklistViewModel>>
        get() = _checklistData

    fun initData(data: List<ChecklistViewModel>) {
        _checklistData.postValue(data.toMutableList())
        var numSelected = 0
        data.forEach {
            if(it.isSelected) numSelected++
        }
        _dataSize.postValue(numSelected)
    }

    fun updateSelectedItem(element: ChecklistViewModel) {
        _checklistData.value?.remove(element)
        if(element.isSelected) {
            element.isSelected = false
            updateDataSize(-1)
        } else {
            element.isSelected = true
            updateDataSize(1)
        }
        element.value = element.isSelected.toString()
        _checklistData.value?.add(0, element)
    }

    fun clearAllChecklist() {
        _checklistData.value?.forEach {
            it.isSelected = false
        }
        _dataSize.postValue(0)
    }

    private fun updateDataSize(counter: Int) {
        _dataSize.postValue(dataSize.value?.plus(counter))
    }

}