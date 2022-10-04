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
import javax.inject.Inject

class TokoNowRecipeFilterViewModel @Inject constructor(
    private val getSortFilterUseCase: GetSortFilterUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val PARAM_SOURCE = "source"
        private const val FILTER_SOURCE = "filter_recipe"
    }

    val visitableItems: LiveData<List<Visitable<*>>>
        get() = _visitableItems
    private val _visitableItems = MutableLiveData<List<Visitable<*>>>()

    fun getSortFilterOptions(selectedFilterIds: List<String>) {
        launchCatchError(block = {
            val params = mapOf(PARAM_SOURCE to FILTER_SOURCE)
            val response = getSortFilterUseCase.execute(params)

            val visitableItems = mutableListOf<Visitable<*>>()
            visitableItems.addSortSection(response, selectedFilterIds)
            visitableItems.addFilterSection(response, selectedFilterIds)

            _visitableItems.postValue(visitableItems)
        }) {

        }
    }
}