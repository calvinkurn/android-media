package com.tokopedia.tokopedianow.recipelist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetSortFilterUseCase
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeSortFilterMapper.addFilterSection
import com.tokopedia.tokopedianow.recipelist.presentation.mapper.RecipeSortFilterMapper.addSortSection
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import javax.inject.Inject

class TokoNowRecipeFilterViewModel @Inject constructor(
    private val getSortFilterUseCase: GetSortFilterUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val visitableItems: LiveData<List<Visitable<*>>>
        get() = _visitableItems
    private val _visitableItems = MutableLiveData<List<Visitable<*>>>()

    fun getSortFilterOptions(selectedFilters: List<SelectedFilter>) {
        launchCatchError(block = {
            val response = getSortFilterUseCase.execute()

            val visitableItems = mutableListOf<Visitable<*>>()
            visitableItems.addSortSection(response, selectedFilters)
            visitableItems.addFilterSection(response, selectedFilters)

            _visitableItems.postValue(visitableItems)
        }) {

        }
    }
}