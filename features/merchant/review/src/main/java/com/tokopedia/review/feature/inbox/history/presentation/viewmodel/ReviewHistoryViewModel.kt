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
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewHistoryViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatcherProvider,
        private val userSession: UserSessionInterface,
        private val productrevFeedbackHistoryUseCase: ProductrevFeedbackHistoryUseCase
) : BaseViewModel(dispatchers.io()) {

    private val _reviewList = MediatorLiveData<ReviewViewState<ProductrevFeedbackHistoryResponse>>()
    val reviewList: LiveData<ReviewViewState<ProductrevFeedbackHistoryResponse>>
        get() = _reviewList

    private var searchQuery = ""

    private val currentPage = MutableLiveData(ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE)

    init {
        _reviewList.addSource(currentPage) {
            getReviewData(it)
        }
    }

    private fun getReviewData(page: Int) {
        if(page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE) {
            _reviewList.value = LoadingView()
        }
        launchCatchError(block = {
            val response = withContext(dispatchers.io()) {
                productrevFeedbackHistoryUseCase.setParams(searchQuery, page)
                productrevFeedbackHistoryUseCase.executeOnBackground()
            }
            _reviewList.postValue(Success(response.productrevFeedbackHistoryResponse, page, searchQuery))
        }) {
            _reviewList.postValue(Fail(it, page, searchQuery))
        }
    }

    fun updatePage(page: Int) {
        currentPage.value = page
    }

    fun updateKeyWord(keyword: String) {
        searchQuery = keyword
        resetPage()
    }

    fun getUserId(): String {
        return userSession.userId
    }

    private fun resetPage() {
        updatePage(ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE)
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}