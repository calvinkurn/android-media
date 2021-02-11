package com.tokopedia.talk.feature.reading.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.talk.feature.reading.data.model.SortOption
import com.tokopedia.talk.feature.reading.data.model.TalkLastAction
import com.tokopedia.talk.feature.reading.data.model.TalkReadingCategory
import com.tokopedia.talk.feature.reading.data.model.ViewState
import com.tokopedia.talk.feature.reading.data.model.discussionaggregate.DiscussionAggregateResponse
import com.tokopedia.talk.feature.reading.data.model.discussiondata.DiscussionDataResponseWrapper
import com.tokopedia.talk.feature.reading.domain.usecase.GetDiscussionAggregateUseCase
import com.tokopedia.talk.feature.reading.domain.usecase.GetDiscussionDataUseCase
import com.tokopedia.talk.feature.reading.presentation.fragment.TalkReadingFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import javax.inject.Inject

class TalkReadingViewModel @Inject constructor(
        private val getDiscussionAggregateUseCase: GetDiscussionAggregateUseCase,
        private val getDiscussionDataUseCase: GetDiscussionDataUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    companion object {
        private const val REQUEST_DELAY = 1000L
    }

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

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState>
        get() = _viewState

    var talkLastAction: TalkLastAction? = null

    fun getDiscussionAggregate(productId: String, shopId: String) {
        setLoading(isRefresh = true)
        launchCatchError(block = {
            val response = async {
                getDiscussionAggregateUseCase.setParams(productId, shopId)
                getDiscussionAggregateUseCase.executeOnBackground()
            }
            val discussionList = async {
                getDiscussionDataUseCase.setParams(productId, shopId, TalkReadingFragment.DEFAULT_INITIAL_PAGE, TalkReadingFragment.DEFAULT_DISCUSSION_DATA_LIMIT, "", "")
                getDiscussionDataUseCase.executeOnBackground()
            }
            _discussionAggregate.postValue(Success(response.await()))
            _discussionData.postValue(Success(discussionList.await()))
            setSuccessFromBackground(discussionList.await().discussionData.totalQuestion == 0, TalkReadingFragment.DEFAULT_INITIAL_PAGE)
        }) {
            _discussionAggregate.postValue(Fail(it))
            setError(0)
        }
    }

    fun getDiscussionData(productId: String, shopId: String, page: Int, limit: Int, sortBy: String, category: String, withDelay: Boolean = false, isRefresh: Boolean = false) {
        setLoading(isRefresh)
        launchCatchError(block = {
            if(withDelay) { delay(REQUEST_DELAY) }
            getDiscussionDataUseCase.setParams(productId, shopId, page, limit, sortBy, category)
            val response = getDiscussionDataUseCase.executeOnBackground()
            _discussionData.postValue(Success(response))
            setSuccessFromBackground(response.discussionData.totalQuestion == 0, page)
        }) {
            _discussionData.postValue(Fail(it))
            setError(page)
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
        filterCategories?.let { list ->
            val updatedCategories = mutableListOf<TalkReadingCategory>()
            list.forEachIndexed { index, talkReadingCategory ->
                updatedCategories.add(index, talkReadingCategory.copy(isSelected = false))
            }
            _filterCategories.value = updatedCategories
        }
    }

    fun updateSortOptions(sortOptions: List<SortOption>) {
        _sortOptions.value = sortOptions
    }

    fun updateSelectedSort(sortOption: SortOption) {
        val sortOptions = _sortOptions.value?.toMutableList()
        sortOptions?.let { list ->
            list.forEach {
                it.isSelected = it.id == sortOption.id
            }
            _sortOptions.value = list
        }
    }

    fun resetSortOptions() {
        val sortOptions = _sortOptions.value?.toMutableList()
        sortOptions?.let { list ->
            list.forEach {
                it.isSelected = it.id == SortOption.SortId.TIME
            }
            _sortOptions.value = sortOptions
        }
    }

    fun updateLastAction(lastAction: TalkLastAction) {
        talkLastAction = lastAction
    }

    private fun setLoading(isRefresh: Boolean) {
        _viewState.value = ViewState.Loading(isRefresh)
    }

    private fun setError(page: Int) {
        _viewState.postValue(ViewState.Error(page))
    }

    fun isUserLoggedIn(): Boolean {
        return userSession.isLoggedIn
    }

    fun setSuccess(isEmpty: Boolean, page: Int) {
        _viewState.value = ViewState.Success(isEmpty, page)
    }

    fun getUserId(): String {
        return userSession.userId
    }

    private fun setSuccessFromBackground(isEmpty: Boolean, page: Int) {
        _viewState.postValue(ViewState.Success(isEmpty, page))
    }

}