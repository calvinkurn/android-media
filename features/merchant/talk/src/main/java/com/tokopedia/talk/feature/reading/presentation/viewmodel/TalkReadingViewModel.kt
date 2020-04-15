package com.tokopedia.talk.feature.reading.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregateResponse
import com.tokopedia.talk.feature.reading.data.model.DiscussionDataResponse
import com.tokopedia.talk.feature.reading.data.model.SortOption
import com.tokopedia.talk.feature.reading.domain.usecase.GetDiscussionAggregateUseCase
import com.tokopedia.talk.feature.reading.domain.usecase.GetDiscussionDataUseCase
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

    private val _discussionData = MutableLiveData<Result<DiscussionDataResponse>>()
    val discussionData: LiveData<Result<DiscussionDataResponse>>
    get() = _discussionData

    fun getDiscussionAggregate(productId: Int) {
        launchCatchError(block = {
            val response = withContext(dispatcher.io) {
                getDiscussionAggregateUseCase.setParams(productId)
                getDiscussionAggregateUseCase.executeOnBackground()
            }
            _discussionAggregate.postValue(Success(response))
        }) {
            _discussionAggregate.postValue(Fail(it))
        }
    }

    fun getDiscussionData(productId: Int, page: Int, limit: Int, sortBy: String, category: String) {
        launchCatchError(block = {
            val response = withContext(dispatcher.io) {
                getDiscussionDataUseCase.setParams(productId, page, limit, sortBy, category)
                getDiscussionDataUseCase.executeOnBackground()
            }
            _discussionData.postValue(Success(response))
        }) {
            _discussionData.postValue(Fail(it))
        }
    }

    fun updateSortOptions(sortOptions: List<SortOption>) {
        _sortOptions.value = sortOptions
    }

    fun updateSelectedSort(sortOption: SortOption) {
        _sortOptions.value?.forEach {
            it.isSelected = it.id == sortOption.id
        }
    }

}