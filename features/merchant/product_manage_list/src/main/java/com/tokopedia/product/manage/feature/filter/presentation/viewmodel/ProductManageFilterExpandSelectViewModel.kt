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
        val index = _selectData.value?.indexOf(element)
        _selectData.value?.remove(element)
        updateElementValue(element, true)
        selectedElement?.let { selected ->
            val selectedIndex = _selectData.value?.indexOf(selected)
            _selectData.value?.remove(selected)
            updateElementValue(selected, false)
            selectedIndex?.let {
                _selectData.value?.add(it, selected)
            }
        }
        index?.let {
            _selectData.value?.add(it,element)
        }
    }

    fun updateSelectedItem(element: SelectViewModel) {
        val index = _selectData.value?.indexOf(element)
        _selectData.value?.remove(element)
        updateElementValue(element, true)
        index?.let {
            _selectData.value?.add(it, element)
        }
    }

    private fun updateElementValue(element: SelectViewModel?, value: Boolean) {
        element?.let {
            it.isSelected = value
        }
    }

}