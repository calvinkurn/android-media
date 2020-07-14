package com.tokopedia.review.feature.inbox.history.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.ReviewViewState
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.util.CoroutineDispatcherProvider
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.history.data.ProductrevFeedbackHistoryResponse
import com.tokopedia.review.feature.inbox.history.domain.usecase.ProductrevFeedbackHistoryUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewHistoryViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatcherProvider,
        private val productrevFeedbackHistoryUseCase: ProductrevFeedbackHistoryUseCase
) : BaseViewModel(dispatchers.io()) {

    private val _reviewList = MediatorLiveData<ReviewViewState<ProductrevFeedbackHistoryResponse>>()
    val reviewList: LiveData<ReviewViewState<ProductrevFeedbackHistoryResponse>>
        get() = _reviewList

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String>
        get() = _searchQuery

    private val _currentPage = MutableLiveData(ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE)
    val currentPage: LiveData<Int>
        get() = _currentPage

    init {
        _reviewList.addSource(currentPage) {
            getReviewData(it)
        }
    }

    fun getReviewData(page: Int) {
        if(page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE) {
            _reviewList.value = LoadingView()
        }
        launchCatchError(block = {
            val response = withContext(dispatchers.io()) {
                productrevFeedbackHistoryUseCase.setParams(searchQuery.value, page)
                productrevFeedbackHistoryUseCase.executeOnBackground()
            }
            _reviewList.postValue(Success(response.productrevFeedbackHistoryResponse, page))
        }) {
            _reviewList.postValue(Fail(it, page))
        }
    }

    fun updatePage(page: Int) {
        _currentPage.value = page
        _currentPage.notifyObserver()
    }

    fun updateKeyWord(keyword: String) {
        _searchQuery.value = keyword
        _searchQuery.notifyObserver()
        resetPage()
    }

    fun resetPage() {
        _currentPage.value = ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE
        _currentPage.notifyObserver()
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}