package com.tokopedia.product.manage.feature.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChipsAdapter.Companion.MAXIMUM_CHIPS
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

    fun updateSelectedItem(element: SelectViewModel): Boolean {
        val currentData = _selectData.value
        var needSort = false
        if(selectedElement != null) {
            val selectIndex = currentData?.indexOf(selectedElement!!)
            val selected = selectIndex?.let { currentData[it] }
            if(selected == element) {
                return needSort
            }
            selected?.let {
                it.isSelected = !it.isSelected
            }
        }
        val index = currentData?.indexOf(element)
        index?.let {
            currentData[it].isSelected = !currentData[it].isSelected
            selectedElement = currentData[it]
            _selectData.postValue(currentData)
            needSort = it > (MAXIMUM_CHIPS - 1)
        }
        return needSort
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