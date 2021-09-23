package com.tokopedia.review.feature.reading.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import javax.inject.Inject

class ReadReviewSortFilterViewModel @Inject constructor() : ViewModel() {

    private var originalFilter: Set<String> = setOf()
    private val selectedFilters = MutableLiveData<Set<String>>(setOf())
    private var filterData: ArrayList<ListItemUnify> = arrayListOf()

    private val _buttonState = MediatorLiveData<Boolean>()
    val buttonState: LiveData<Boolean>
        get() = _buttonState

    private val _resetButtonState = MediatorLiveData<Boolean>()
    val resetButtonState: LiveData<Boolean>
        get() = _resetButtonState


    init {
        _buttonState.addSource(selectedFilters) {
            _buttonState.value = it != originalFilter
        }
        _resetButtonState.addSource(selectedFilters) {
            _resetButtonState.value = it.isNotEmpty()
        }
    }

    fun setInitialValues(originalFilter: Set<String>, filterData: ArrayList<ListItemUnify>) {
        this.originalFilter = originalFilter
        this.filterData = filterData
    }

    fun getFilterData(): ArrayList<ListItemUnify> {
        return filterData
    }

    fun getOriginalFilters(): Set<String> {
        return originalFilter
    }

    fun onFilterCheckChange(isChecked: Boolean, itemUnify: ListItemUnify) {
        selectedFilters.value = if (isChecked) {
            selectedFilters.value!!.plus(itemUnify.listTitleText)
        } else {
            selectedFilters.value!!.minus(itemUnify.listTitleText)
        }
    }

    fun updateSelectedFilter(listItemUnify: ListItemUnify) {
        selectedFilters.value = selectedFilters.value!!.plus(listItemUnify.listTitleText)
    }

    fun clearAllFilters() {
        selectedFilters.value = setOf()
    }

    fun getSelectedFilters(): Set<ListItemUnify> {
        return selectedFilters.value!!.map { ListItemUnify(it, "") }.toSet()
    }

}