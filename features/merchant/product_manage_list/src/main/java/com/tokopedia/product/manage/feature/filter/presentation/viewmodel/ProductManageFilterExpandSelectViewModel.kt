package com.tokopedia.product.manage.feature.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel
import javax.inject.Inject

class ProductManageFilterExpandSelectViewModel @Inject constructor(): ViewModel() {

    private val _selectData = MutableLiveData<MutableList<SelectViewModel>>()
    val selectData: LiveData<MutableList<SelectViewModel>>
        get() = _selectData

    fun updateData(data: List<SelectViewModel>) {
        _selectData.postValue(data.toMutableList())
    }

    fun updateSelectedItem(selectedElement: SelectViewModel?, element: SelectViewModel) {
        val currentData = _selectData.value
        val index = currentData?.indexOf(element)
        index?.let {
            currentData[it].isSelected = true
        }
        selectedElement?.let { selected ->
            val selectedIndex = currentData?.indexOf(selected)
            selectedIndex?.let {
                currentData[it].isSelected = true
            }
        }
        _selectData.postValue(currentData)
    }

    fun updateSelectedItem(element: SelectViewModel) {
        val currentData = _selectData.value
        val index = currentData?.indexOf(element)
        index?.let {
            currentData[it].isSelected = true
        }
        _selectData.postValue(currentData)
    }

}