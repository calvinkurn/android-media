package com.tokopedia.review.feature.reviewdetail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.util.*
import com.tokopedia.review.feature.inboxreview.domain.mapper.InboxReviewMapper
import com.tokopedia.review.feature.reviewdetail.data.ProductFeedbackFilterData
import com.tokopedia.review.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.review.feature.reviewdetail.domain.GetProductFeedbackDetailListUseCase
import com.tokopedia.review.feature.reviewdetail.domain.GetProductReviewInitialUseCase
import com.tokopedia.review.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.review.feature.reviewdetail.view.adapter.BaseSellerReviewDetail
import com.tokopedia.review.feature.reviewdetail.view.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductReviewDetailViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val getProductReviewInitialUseCase: GetProductReviewInitialUseCase,
    private val getProductFeedbackDetailListUseCase: GetProductFeedbackDetailListUseCase
) : BaseViewModel(dispatcherProvider.main) {

    var filterPeriod: String = ""
    var sortBy: String = ""

    private var prefixTime = "time="
    private var prefixTopic = "topic="
    private var prefixRating = "rating="

    private var chipsFilterText = "30 Hari Terakhir"
    private var productId = 0
    private var filterByList: MutableList<String> = mutableListOf(chipsFilterText)

    private var filterRatingData: List<RatingBarUiModel> = listOf()

    private var filterTopicData: List<SortFilterItemWrapper> = listOf()

    private var sortTopicData: List<SortItemUiModel> = listOf()

    private var sortAndFilter: Pair<List<SortFilterItemWrapper>, String> = Pair(listOf(), "")

    private val ratingFilterData = MutableLiveData<List<RatingBarUiModel>>()
    private val topicFilterData = MutableLiveData<List<SortFilterItemWrapper>>()

    private val topicAndSortFilterData = MutableLiveData<Pair<List<SortFilterItemWrapper>, String>>()

    private val _reviewInitialData = MediatorLiveData<Result<Triple<List<BaseSellerReviewDetail>, String, Boolean>>>()
    val reviewInitialData: LiveData<Result<Triple<List<BaseSellerReviewDetail>, String, Boolean>>>
        get() = _reviewInitialData

    val productFeedbackDetailMediator = MediatorLiveData<Result<ProductFeedbackDetailUiModel>>()
    val productFeedbackDetail: LiveData<Result<ProductFeedbackDetailUiModel>>
        get() = productFeedbackDetailMediator

    init {
        //Add Default Filter Date Value
        filterByList = mutableListOf(ReviewConstants.mapFilterReviewDetail().getKeyByValue(chipsFilterText))

        setupFeedBackDetail()
        //Initialize Sort Topic Filter
        val data = SellerReviewProductDetailMapper.mapToItemSortTopic()
        sortTopicData = data
        sortBy = ReviewConstants.mapSortReviewDetail().getKeyByValue(data.getSortBy)
    }

    fun setChipFilterDateText(chipFilterText: String) {
        chipsFilterText = chipFilterText
        filterPeriod = ReviewConstants.mapFilterReviewDetail().getKeyByValue(chipFilterText)
        filterByList.removeFilterElement(prefixTime)
        filterByList.add(ReviewUtil.setFilterJoinValueFormat(filterPeriod))
    }

    private fun setupFeedBackDetail() {
        productFeedbackDetailMediator.addSource(topicFilterData) { data ->
            val topicFilterText = data.filter { it.isSelected }
            val topicFilterTextGenerated = if (topicFilterText.isEmpty()) "" else topicFilterText.joinToString(prefix = prefixTopic, separator = ",") {
                it.titleUnformated
            }
            filterByList.removeFilterElement(prefixTopic)

            if (topicFilterTextGenerated.isNotBlank()) {
                filterByList.add(topicFilterTextGenerated)
            }
            getFeedbackDetailListNext(productId, sortBy, 1)
        }

        productFeedbackDetailMediator.addSource(ratingFilterData) { data ->
            val ratingFilterText = data?.filter { it.ratingIsChecked }.orEmpty()
            val ratingFilterGenerated = if (ratingFilterText.isEmpty()) "" else ratingFilterText.joinToString(prefix = prefixRating, separator = ",") {
                it.ratingLabel.toString()
            }

            filterByList.removeFilterElement(prefixRating)

            if (ratingFilterGenerated.isNotBlank()) {
                filterByList.add(ratingFilterGenerated)
            }

            getFeedbackDetailListNext(productId, sortBy, 1)
        }

        productFeedbackDetailMediator.addSource(topicAndSortFilterData) { data ->
            val topicFilterText = data.first.filter { it.isSelected }
            val topicFilterTextGenerated = if (topicFilterText.isEmpty()) "" else topicFilterText.joinToString(prefix = prefixTopic, separator = ",") {
                it.titleUnformated
            }

            filterByList.removeFilterElement(prefixTopic)

            if (topicFilterTextGenerated.isNotBlank()) {
                filterByList.add(topicFilterTextGenerated)
            }

            sortBy = data.second

            getFeedbackDetailListNext(productId, sortBy, 1)
        }
    }

    fun updateRatingFilterData(data: List<RatingBarUiModel>) {
        filterRatingData = data
    }

    fun updateTopicsFilterData(data: List<SortFilterItemWrapper>) {
        filterTopicData = data
    }

    fun updateSortAndFilterTopicData(data: Pair<List<SortFilterItemWrapper>, String>) {
        this.sortAndFilter = data
    }

    fun setSortTopicData(data: List<SortItemUiModel>) {
        this.sortTopicData = data
    }

    fun getSortAndFilter(): Pair<List<SortFilterItemWrapper>, String> {
        return sortAndFilter
    }

    fun getFilterTopicData(): List<SortFilterItemWrapper> {
        return filterTopicData
    }

    fun getFilterRatingData(): List<RatingBarUiModel> {
        return filterRatingData
    }

    fun getSortTopicData(): List<SortItemUiModel> {
        return sortTopicData
    }

    fun setSortAndFilterTopicData(data: Pair<List<SortFilterItemWrapper>, String>) {
        val updatedData = InboxReviewMapper.mapToUnSelectedSortAndFilter(data)
        topicAndSortFilterData.value = updatedData.first to data.second
    }

    fun setFilterTopicDataText(data: List<SortFilterItemWrapper>) {
        topicFilterData.value = InboxReviewMapper.mapToUnSelectedFilterTopic(data)
    }

    fun setFilterRatingDataText(data: List<RatingBarUiModel>) {
        val updatedData = InboxReviewMapper.mapToUnSelectedFilterRating(data)
        ratingFilterData.value = updatedData
    }

    fun getProductRatingDetail(productID: Int, sortBy: String) {
        launchCatchError(block = {
            productId = productID
            getProductReviewInitialUseCase.requestParams = GetProductReviewInitialUseCase.createParams(productID, filterByList.getGeneratedFilterByText, sortBy, 1,
                    filterByList.getGeneratedTimeFilterByText(prefixTime))
            val productRatingDetailData = getProductReviewInitialUseCase.executeOnBackground()

            val productName = productRatingDetailData.productReviewDetailOverallResponse?.productGetReviewAggregateByProduct?.productName
                    ?: ""
            val hasNext = productRatingDetailData.productFeedBackResponse?.productrevFeedbackDataPerProduct?.hasNext
                    ?: false
            val productFilterData = productRatingDetailData.productReviewFilterResponse?.productrevFeedbackDataPerProduct
                    ?: ProductFeedbackFilterData()
            val uiData: MutableList<BaseSellerReviewDetail> = mutableListOf()

            uiData.add(SellerReviewProductDetailMapper.mapToRatingDetailOverallUiModel(productRatingDetailData.productReviewDetailOverallResponse?.productGetReviewAggregateByProduct
                    ?: ProductReviewDetailOverallResponse.ProductGetReviewAggregateByProduct(), chipsFilterText))
            uiData.add(SellerReviewProductDetailMapper.mapToRatingBarUiModel(productFilterData, filterRatingData))
            uiData.add(SellerReviewProductDetailMapper.mapToTopicUiModel(productFilterData, filterTopicData))

            if (productRatingDetailData.productFeedBackResponse == null) {
                _reviewInitialData.postValue(Fail(Throwable()))
            } else {
                if (productRatingDetailData.productFeedBackResponse?.productrevFeedbackDataPerProduct?.list?.isEmpty() == true || productRatingDetailData.productFeedBackResponse?.productrevFeedbackDataPerProduct?.list == null) {
                    uiData.add(ProductFeedbackErrorUiModel(false))
                } else {
                    uiData.addAll(SellerReviewProductDetailMapper.mapToFeedbackUiModel(productRatingDetailData.productFeedBackResponse?.productrevFeedbackDataPerProduct, userSession))
                }
                _reviewInitialData.postValue(Success(Triple(uiData, productName, hasNext)))
            }
        })
        {
            _reviewInitialData.postValue(Fail(it))
        }
    }

    fun getFeedbackDetailListNext(productID: Int, sortBy: String, page: Int) {
        launchCatchError(block = {
            val feedbackDetailList = withContext(dispatcherProvider.io) {
                getProductFeedbackDetailListUseCase.params = GetProductFeedbackDetailListUseCase.createParams(
                        productID,
                        sortBy,
                        filterByList.getGeneratedFilterByText,
                        ReviewConstants.DEFAULT_PER_PAGE,
                        page
                )
                val productFeedbackDetailResponse = getProductFeedbackDetailListUseCase.executeOnBackground()
                SellerReviewProductDetailMapper.mapToProductFeedbackDetailUiModel(productFeedbackDetailResponse, userSession)
            }
            productFeedbackDetailMediator.postValue(Success(feedbackDetailList))
        }, onError = {
            productFeedbackDetailMediator.postValue(Fail(it))
        })
    }
}