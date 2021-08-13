package com.tokopedia.product.manage.feature.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import javax.inject.Inject

class ProductManageFilterExpandChecklistViewModel @Inject constructor(): ViewModel() {

    private val _dataSize = MutableLiveData<Int>()
    val dataSize: LiveData<Int>
    get() = _dataSize

    private val _checklistData = MutableLiveData<MutableList<ChecklistUiModel>>()
    val checklistData: LiveData<MutableList<ChecklistUiModel>>
        get() = _checklistData

    fun initData(data: List<ChecklistUiModel>) {
        _checklistData.value = data.toMutableList()
        var numSelected = 0
        data.forEach {
            if(it.isSelected) numSelected++
        }
        _dataSize.value = numSelected
    }

    fun updateSelectedItem(element: ChecklistUiModel) {
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
    }

    fun clearAllChecklist() {
        val currentData = _checklistData.value
        currentData?.forEach {
            it.isSelected = false
            it.value = it.isSelected.toString()
        }
        _checklistData.value = currentData
        _dataSize.value = 0
    }

    private fun updateDataSize(counter: Int) {
        val dataSize = dataSize.value.orZero()
        _dataSize.value = dataSize.plus(counter)
    }

}