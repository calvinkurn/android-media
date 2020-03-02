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
        _selectData.value?.remove(element)
        updateElementValue(element, true)
        selectedElement?.let {
            _selectData.value?.remove(it)
            updateElementValue(it, false)
            _selectData.value?.add(it)
        }
        _selectData.value?.add(0,element)
    }

    fun updateSelectedItem(element: SelectViewModel) {
        _selectData.value?.remove(element)
        updateElementValue(element, true)
        _selectData.value?.add(0,element)
    }

    private fun updateElementValue(element: SelectViewModel?, value: Boolean) {
        element?.let {
            it.isSelected = value
        }
    }

}