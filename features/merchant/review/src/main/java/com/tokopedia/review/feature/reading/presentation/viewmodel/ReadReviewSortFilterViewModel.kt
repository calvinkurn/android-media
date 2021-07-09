package com.tokopedia.review.feature.reading.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import javax.inject.Inject

class ReadReviewSortFilterViewModel @Inject constructor() : ViewModel() {

    private var originalFilter: Set<String> = setOf()
    private var originalSort: String = ""
    private val selectedFilters = MutableLiveData<Set<String>>(setOf())
    private val selectedSort = MutableLiveData<ListItemUnify>(ListItemUnify())
    private var filterData: ArrayList<ListItemUnify> = arrayListOf()

    private val _buttonState = MediatorLiveData<Boolean>()
    val buttonState: LiveData<Boolean>
        get() = _buttonState


    init {
        _buttonState.addSource(selectedFilters) {
            _buttonState.value = it != originalFilter
        }
        _buttonState.addSource(selectedSort) {
            _buttonState.value = it.listTitleText != originalSort
        }
    }

    fun setInitialValues(originalFilter: Set<String>, originalSort: String, filterData: ArrayList<ListItemUnify>) {
        this.originalFilter = originalFilter
        this.originalSort = originalSort
        this.filterData = filterData
    }

    fun getFilterData(): ArrayList<ListItemUnify> {
        return filterData
    }

    fun getOriginalFilters(): Set<String>{
        return originalFilter
    }

    fun getOriginalSort(): String {
        return originalSort
    }

    fun onFilterCheckChange(isChecked: Boolean, itemUnify: ListItemUnify) {
        selectedFilters.value = if (isChecked) {
            selectedFilters.value?.plus(itemUnify.listTitleText)
        } else {
            selectedFilters.value?.minus(itemUnify.listTitleText)
        }
    }

    fun updateSelectedFilter(listItemUnify: ListItemUnify) {
        selectedFilters.value = selectedFilters.value?.plus(listItemUnify.listTitleText)
    }

    fun onSortCheckChange(isChecked: Boolean, selectedSortOption: ListItemUnify) {
        if (isChecked) {
            selectedSort.value = selectedSortOption
        }
    }

    fun clearAllFilters() {
        selectedFilters.value = setOf()
    }

    fun getSelectedSort(): ListItemUnify {
        return selectedSort.value ?: ListItemUnify()
    }

    fun getSelectedFilters(): Set<ListItemUnify> {
        return selectedFilters.value?.map { ListItemUnify(it, "") }?.toSet() ?: setOf()
    }

}