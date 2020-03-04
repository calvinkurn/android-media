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
        selectedElement = findSelectedData(data)
    }

    private var selectedElement: SelectViewModel? = null

    fun updateSelectedItem(element: SelectViewModel) {
        val currentData = _selectData.value
        if(selectedElement != null) {
            val selectIndex = currentData?.indexOf(selectedElement!!)
            val selected = selectIndex?.let { currentData[it] }
            selected?.let {
                it.isSelected = !it.isSelected
            }
            if(selected == element) {
                selectedElement = null
                _selectData.postValue(currentData)
                return
            }
        }
        val index = currentData?.indexOf(element)
        index?.let {
            updateSpecificData(currentData, it)
        }
        _selectData.postValue(currentData)
    }

    private fun updateSpecificData(currentData:MutableList<SelectViewModel>, index: Int) {
        index.let {
            if(it < 5) {
                currentData[it].isSelected = !currentData[it].isSelected
            } else {
                val dataToBeSelected = currentData[it]
                dataToBeSelected.isSelected = !currentData[it].isSelected
                currentData.removeAt(it)
                currentData.add(0, dataToBeSelected)
            }
            selectedElement = currentData[it]
        }
    }

    private fun findSelectedData(selectViewModels: List<SelectViewModel>): SelectViewModel? {
        val selectedData = selectViewModels.filter {
            it.isSelected
        }
        return if(selectedData.isNotEmpty()) {
            selectedData.first()
        } else {
            null
        }
    }

}