package com.tokopedia.talk.feature.reading.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import com.tokopedia.talk.feature.reading.data.model.discussionaggregate.DiscussionAggregateResponse
import com.tokopedia.talk.feature.reading.data.model.discussiondata.DiscussionDataResponseWrapper
import com.tokopedia.talk.feature.reading.data.model.SortOption
import com.tokopedia.talk.feature.reading.data.model.TalkLastAction
import com.tokopedia.talk.feature.reading.domain.usecase.GetDiscussionAggregateUseCase
import com.tokopedia.talk.feature.reading.domain.usecase.GetDiscussionDataUseCase
import com.tokopedia.talk.feature.reading.data.model.TalkReadingCategory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TalkReadingViewModel @Inject constructor(
        private val getDiscussionAggregateUseCase: GetDiscussionAggregateUseCase,
        private val getDiscussionDataUseCase: GetDiscussionDataUseCase,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _discussionAggregate = MutableLiveData<Result<DiscussionAggregateResponse>>()
    val discussionAggregate: LiveData<Result<DiscussionAggregateResponse>>
    get() = _discussionAggregate

    private val _sortOptions = MutableLiveData<List<SortOption>>()
    val sortOptions: LiveData<List<SortOption>>
    get() = _sortOptions

    private val _discussionData = MutableLiveData<Result<DiscussionDataResponseWrapper>>()
    val discussionData: LiveData<Result<DiscussionDataResponseWrapper>>
    get() = _discussionData

    private val _filterCategories = MutableLiveData<List<TalkReadingCategory>>()
    val filterCategories: LiveData<List<TalkReadingCategory>>
    get() = _filterCategories

    private val _talkLastAction = MutableLiveData<TalkLastAction>()
    val talkLastAction: LiveData<TalkLastAction>
    get() = _talkLastAction

    fun getDiscussionAggregate(productId: String, shopId: String) {
        launchCatchError(block = {
            val response = withContext(dispatcher.io) {
                getDiscussionAggregateUseCase.setParams(productId, shopId)
                getDiscussionAggregateUseCase.executeOnBackground()
            }
            _discussionAggregate.postValue(Success(response))
        }) {
            _discussionAggregate.postValue(Fail(it))
        }
    }

    fun getDiscussionData(productId: String, shopId: String, page: Int, limit: Int, sortBy: String, category: String) {
        launchCatchError(block = {
            val response = withContext(dispatcher.io) {
                getDiscussionDataUseCase.setParams(productId, shopId, page, limit, sortBy, category)
                getDiscussionDataUseCase.executeOnBackground()
            }
            _discussionData.postValue(Success(response))
        }) {
            _discussionData.postValue(Fail(it))
        }
    }

    fun updateCategories(categories: List<TalkReadingCategory>) {
        _filterCategories.value = categories
    }

    fun updateSelectedCategory(selectedCategory: String, isSelected: Boolean) {
        val filterCategories = _filterCategories.value?.toMutableList()
        val categoryToModify = filterCategories?.filter { selectedCategory.contains(it.displayName) } ?: emptyList()
        if(categoryToModify.isNotEmpty()) {
            val modifiedCategory = categoryToModify.first().copy(isSelected = isSelected)
            val index = filterCategories?.indexOfFirst { selectedCategory.contains(it.displayName) }
            index?.let {
                filterCategories[index] = modifiedCategory
                _filterCategories.value = filterCategories
            }
        }
    }

    fun unselectAllCategories() {
        val filterCategories = _filterCategories.value?.toMutableList()
        val updatedCategories = mutableListOf<TalkReadingCategory>()
        filterCategories?.forEachIndexed { index, talkReadingCategory ->
            updatedCategories.add(index, talkReadingCategory.copy(isSelected = false))
        }
        _filterCategories.value = updatedCategories
    }

    fun updateSortOptions(sortOptions: List<SortOption>) {
        _sortOptions.value = sortOptions
    }

    fun updateSelectedSort(sortOption: SortOption) {
        val sortOptions = _sortOptions.value?.toMutableList()
        sortOptions?.forEach {
            it.isSelected = it.id == sortOption.id
        }
        _sortOptions.value = sortOptions
    }

    fun resetSortOptions() {
        val sortOptions = _sortOptions.value?.toMutableList()
        sortOptions?.forEach {
            it.isSelected = it.id == SortOption.SortId.INFORMATIVENESS
        }
        _sortOptions.value = sortOptions
    }

    fun updateLastAction(lastAction: TalkLastAction) {
        _talkLastAction.postValue(lastAction)
    }

}