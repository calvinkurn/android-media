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
        val currentData = _checklistData.value
        val index = currentData?.indexOf(element)
        index?.let {
            currentData[it].let { data ->
                if(data.isSelected) {
                    updateDataSize(-1)
                } else {
                    updateDataSize(1)
                }
                data.isSelected = !data.isSelected
                data.value = data.isSelected.toString()
            }
            currentData[it]
        }
        _checklistData.postValue(currentData)
    }

    fun clearAllChecklist() {
        val currentData = _checklistData.value
        currentData?.forEach {
            it.isSelected = false
            it.value = it.isSelected.toString()
        }
        _checklistData.postValue(currentData)
        _dataSize.postValue(0)
    }

    private fun updateDataSize(counter: Int) {
        _dataSize.postValue(dataSize.value?.plus(counter))
    }

}