package com.tokopedia.review.feature.inboxreview.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.review.common.util.ReviewConstants.UNANSWERED_VALUE
import com.tokopedia.review.common.util.ReviewConstants.prefixRating
import com.tokopedia.review.common.util.ReviewConstants.prefixStatus
import com.tokopedia.review.common.util.getGeneratedFilterByText
import com.tokopedia.review.common.util.removeFilterElement
import com.tokopedia.review.feature.inboxreview.domain.mapper.InboxReviewMapper
import com.tokopedia.review.feature.inboxreview.domain.usecase.GetInboxReviewUseCase
import com.tokopedia.review.feature.inboxreview.domain.usecase.GetInboxReviewCounterUseCase
import com.tokopedia.review.feature.inboxreview.presentation.model.InboxReviewUiModel
import com.tokopedia.review.feature.inboxreview.presentation.model.ListItemRatingWrapper
import com.tokopedia.review.feature.inboxreview.presentation.model.SortFilterInboxItemWrapper
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderCounter
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderCounterUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class InboxReviewViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatchers,
        private val getInboxReviewUseCase: GetInboxReviewUseCase,
        private val getInboxReviewCounterUseCase: GetInboxReviewCounterUseCase,
        private val productrevGetReminderCounterUseCase: ProductrevGetReminderCounterUseCase,
        val userSession: UserSessionInterface
) : BaseViewModel(dispatcherProvider.main) {

    private val _inboxReview = MutableLiveData<Result<InboxReviewUiModel>>()
    val inboxReview: LiveData<Result<InboxReviewUiModel>>
        get() = _inboxReview

    private var filterRatingList: ArrayList<ListItemRatingWrapper> = arrayListOf()

    private var allFilterList: List<SortFilterInboxItemWrapper> = listOf()

    private var filterByList: MutableList<String> = mutableListOf()

    private val _ratingFilterData = MutableLiveData<List<ListItemRatingWrapper>>()
    private val _allFilterInboxReviewData = MutableLiveData<List<SortFilterInboxItemWrapper>>()

    val feedbackInboxReviewMediator = MediatorLiveData<Result<InboxReviewUiModel>>()
    val feedbackInboxReview: LiveData<Result<InboxReviewUiModel>>
        get() = feedbackInboxReviewMediator

    private val _inboxReviewCounterText = MutableLiveData<Result<Int>>()
    val inboxReviewCounterText: LiveData<Result<Int>>
        get() = _inboxReviewCounterText

    private val estimation = MutableLiveData<ProductrevGetReminderCounter>()
    fun getEstimation(): LiveData<ProductrevGetReminderCounter> = estimation

    init {
        setupFeedBackInboxReview()
    }

    fun updateRatingFilterData(data: ArrayList<ListItemRatingWrapper>) {
        filterRatingList = data
    }

    fun updateStatusFilterData(data: List<SortFilterInboxItemWrapper>) {
        allFilterList = data
    }

    fun setFilterRatingDataText(data: List<ListItemRatingWrapper>) {
        _ratingFilterData.value = data
    }

    fun setFilterStatusDataText(data: List<SortFilterInboxItemWrapper>) {
        _allFilterInboxReviewData.value = data
    }

    fun getStatusFilterListUpdated(): List<SortFilterInboxItemWrapper> {
        return allFilterList
    }

    fun getRatingFilterListUpdated(): ArrayList<ListItemRatingWrapper> {
        return filterRatingList
    }

    fun resetAllFilter() {
        allFilterList = InboxReviewMapper.mapToUnSelectedStatusList(allFilterList)
        filterRatingList = InboxReviewMapper.mapToUnSelectedRatingList(filterRatingList)
        filterByList.clear()
        getInitInboxReview()
    }

    fun getInboxReviewCounter() {
        launchCatchError(block = {
            val counterResult = withContext(dispatcherProvider.io) {
                getInboxReviewCounterUseCase.executeOnBackground().productrevReviewTabCounter.list.firstOrNull()?.count.orZero()
            }
            _inboxReviewCounterText.postValue(Success(counterResult))
        }, onError = {
            _inboxReviewCounterText.postValue(Fail(it))
        })
    }

    fun getInboxReview(page: Int = 1) {
        launchCatchError(block = {
            val inboxReviewResult = withContext(dispatcherProvider.io) {
                getInboxReviewUseCase.params = GetInboxReviewUseCase.createParams(
                        filterByList.getGeneratedFilterByText,
                        page
                )

                val inboxReviewResultResponse = getInboxReviewUseCase.executeOnBackground()
                InboxReviewMapper.mapToInboxReviewUiModel(inboxReviewResultResponse, userSession = userSession)
            }
            _inboxReview.postValue(Success(inboxReviewResult))
        }, onError = {
            _inboxReview.postValue(Fail(it))
        })
    }

    fun getInitInboxReview(page: Int = 1, statusFilter: String = UNANSWERED_VALUE) {
        launchCatchError(block = {
            val inboxReviewResult = withContext(dispatcherProvider.io) {

                val statusFilterTextGenerated = "$prefixStatus$statusFilter"
                filterByList.removeFilterElement(prefixStatus)

                if (statusFilter.isNotBlank()) {
                    filterByList.add(statusFilterTextGenerated)
                }
                getInboxReviewUseCase.params = GetInboxReviewUseCase.createParams(
                        filterByList.getGeneratedFilterByText,
                        page
                )

                val inboxReviewResultResponse = getInboxReviewUseCase.executeOnBackground()
                InboxReviewMapper.mapToInboxReviewUiModel(inboxReviewResultResponse, userSession = userSession)
            }
            _inboxReview.postValue(Success(inboxReviewResult))
        }, onError = {
            _inboxReview.postValue(Fail(it))
        })
    }

    fun getInitFeedbackInboxReviewListNext(page: Int, statusFilter: String) {
        launchCatchError(block = {
            val statusFilterTextGenerated = "$prefixStatus$statusFilter"
            filterByList.removeFilterElement(prefixStatus)

            if (statusFilter.isNotBlank()) {
                filterByList.add(statusFilterTextGenerated)
            }

            val feedbackInboxReviewList = withContext(dispatcherProvider.io) {
                getInboxReviewUseCase.params = GetInboxReviewUseCase.createParams(
                        filterByList.getGeneratedFilterByText,
                        page
                )

                val productFeedbackInboxReviewResponse = getInboxReviewUseCase.executeOnBackground()
                InboxReviewMapper.mapToInboxReviewUiModel(productFeedbackInboxReviewResponse, userSession = userSession)
            }
            feedbackInboxReviewMediator.postValue(Success(feedbackInboxReviewList))
        }, onError = {
            feedbackInboxReviewMediator.postValue(Fail(it))
        })
    }


    private fun setupFeedBackInboxReview() {
        feedbackInboxReviewMediator.addSource(_ratingFilterData) { data ->
            val ratingFilterText = data.filter { it.isSelected }
            val ratingFilterTextGenerated = if (ratingFilterText.isEmpty()) "" else ratingFilterText.joinToString(prefix = prefixRating, separator = ",") {
                it.sortValue
            }
            filterByList.removeFilterElement(prefixRating)

            if (ratingFilterTextGenerated.isNotBlank()) {
                filterByList.add(ratingFilterTextGenerated)
            }

            val countStatusIsZero = InboxReviewMapper.mapToStatusFilterList(allFilterList).filter { it.isSelected }.count().isZero()
            if(countStatusIsZero) {
                getInitInboxReview()
            } else {
                getInboxReview()
            }
        }

        feedbackInboxReviewMediator.addSource(_allFilterInboxReviewData) { data ->
            val statusFilterText = InboxReviewMapper.mapToStatusFilterList(data).filter { it.isSelected }

            val statusFilterTextGenerated = if (statusFilterText.isEmpty()) "" else statusFilterText.joinToString(prefix = prefixStatus, separator = ",") {
                it.sortValue
            }

            filterByList.removeFilterElement(prefixStatus)

            if (statusFilterTextGenerated.isNotBlank()) {
                filterByList.add(statusFilterTextGenerated)
            }

            val countStatusIsZero = InboxReviewMapper.mapToStatusFilterList(allFilterList).filter { it.isSelected }.count().isZero()
            if(countStatusIsZero) {
                getInitInboxReview()
            } else {
                getInboxReview()
            }
        }
    }

    fun getFeedbackInboxReviewListNext(page: Int) {
        launchCatchError(block = {
            val feedbackInboxReviewList = withContext(dispatcherProvider.io) {
                getInboxReviewUseCase.params = GetInboxReviewUseCase.createParams(
                        filterByList.getGeneratedFilterByText,
                        page
                )

                val productFeedbackInboxReviewResponse = getInboxReviewUseCase.executeOnBackground()
                InboxReviewMapper.mapToInboxReviewUiModel(productFeedbackInboxReviewResponse, userSession = userSession)
            }
            feedbackInboxReviewMediator.postValue(Success(feedbackInboxReviewList))
        }, onError = {
            feedbackInboxReviewMediator.postValue(Fail(it))
        })
    }

    fun fetchReminderCounter() {
        launchCatchError(block = {
            val responseWrapper = productrevGetReminderCounterUseCase.executeOnBackground()
            estimation.postValue(responseWrapper.productrevGetReminderCounter)
        }, onError = {})
    }

}