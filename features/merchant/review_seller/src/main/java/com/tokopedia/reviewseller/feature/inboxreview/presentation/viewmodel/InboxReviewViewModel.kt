package com.tokopedia.reviewseller.feature.inboxreview.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.UNANSWERED_VALUE
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.prefixRating
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.prefixStatus
import com.tokopedia.reviewseller.common.util.getGeneratedFilterByText
import com.tokopedia.reviewseller.common.util.removeFilterElement
import com.tokopedia.reviewseller.feature.inboxreview.domain.mapper.InboxReviewMapper
import com.tokopedia.reviewseller.feature.inboxreview.domain.usecase.GetInboxReviewUseCase
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.InboxReviewUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.ListItemRatingWrapper
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.SortFilterInboxItemWrapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class InboxReviewViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val getInboxReviewUseCase: GetInboxReviewUseCase,
        private val userSession: UserSessionInterface
) : BaseViewModel(dispatcherProvider.main()) {

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

    fun getInboxReview(page: Int = 1) {
        launchCatchError(block = {
            val inboxReviewResult = withContext(dispatcherProvider.io()) {
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
            val inboxReviewResult = withContext(dispatcherProvider.io()) {

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

            val feedbackInboxReviewList = withContext(dispatcherProvider.io()) {
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
            val feedbackInboxReviewList = withContext(dispatcherProvider.io()) {
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

}