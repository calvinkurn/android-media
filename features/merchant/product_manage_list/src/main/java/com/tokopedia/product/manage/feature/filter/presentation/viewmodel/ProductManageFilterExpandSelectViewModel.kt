package com.tokopedia.product.manage.feature.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChipsAdapter.Companion.MAXIMUM_CHIPS
import javax.inject.Inject

class ProductManageFilterExpandSelectViewModel @Inject constructor(): ViewModel() {

    private val _selectData = MutableLiveData<MutableList<SelectUiModel>>()
    private var selectedElement: SelectUiModel? = null

    val selectData: LiveData<MutableList<SelectUiModel>>
        get() = _selectData

    fun updateData(data: List<SelectUiModel>) {
        _selectData.value = data.toMutableList()
        selectedElement = findSelectedData(data)
    }

    fun updateSelectedItem(element: SelectUiModel): Boolean {
        var needSort = false
        val currentData = _selectData.value.orEmpty()
        if(currentData.isNotEmpty()) {
            selectedElement?.let { selectedElement ->
                val selectIndex = currentData.indexOf(selectedElement)
                val selected = selectIndex.let { currentData[it] }
                if(selected == element) {
                    return needSort
                }
                selected.isSelected = false
            }

            currentData.indexOf(element).let {
                currentData[it].isSelected = true
                selectedElement = currentData[it]
                _selectData.value = currentData.toMutableList()
                needSort = it > (MAXIMUM_CHIPS - 1)
            }
        }
        return needSort
    }

    private fun findSelectedData(selectUiModels: List<SelectUiModel>): SelectUiModel? {
        val selectedData = selectUiModels.filter {
            it.isSelected
        }
        return if(selectedData.isNotEmpty()) {
            selectedData.first()
        } else {
            null
        }
    }

}