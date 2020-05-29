package com.tokopedia.reviewseller.feature.reviewdetail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant
import com.tokopedia.reviewseller.common.util.ReviewSellerUtil
import com.tokopedia.reviewseller.common.util.getKeyByValue
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductFeedbackFilterData
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.reviewseller.feature.reviewdetail.domain.GetProductFeedbackDetailListUseCase
import com.tokopedia.reviewseller.feature.reviewdetail.domain.GetProductReviewInitialUseCase
import com.tokopedia.reviewseller.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.BaseSellerReviewDetail
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductReviewDetailViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val userSession: UserSessionInterface,
        private val getProductReviewInitialUseCase: GetProductReviewInitialUseCase,
        private val getProductFeedbackDetailListUseCase: GetProductFeedbackDetailListUseCase
) : BaseViewModel(dispatcherProvider.main()) {

    var filterPeriod: String = ""
    var sortBy: String = ""

    private var prefixTime = "time="
    private var prefixTopic = "topic="
    private var prefixRating = "rating="

    private var chipsFilterText = "30 Hari Terakhir"
    private var productId = 0
    private var filterByList: MutableList<String> = mutableListOf(chipsFilterText)
    var filterRatingData: List<RatingBarUiModel> = listOf()
    var filterTopicData: List<SortFilterItemWrapper> = listOf()

    var sortTopicData: List<SortItemUiModel> = listOf()

    var sortAndFilter: Pair<List<SortFilterItemWrapper>, String>? = null

    val ratingFilterData = MutableLiveData<List<RatingBarUiModel>>()
    val topicFilterData = MutableLiveData<List<SortFilterItemWrapper>>()

    val topicAndSortFilterData = MutableLiveData<Pair<List<SortFilterItemWrapper>, String>>()

    private val _reviewInitialData = MediatorLiveData<Result<Triple<List<BaseSellerReviewDetail>, String, Boolean>>>()
    val reviewInitialData: LiveData<Result<Triple<List<BaseSellerReviewDetail>, String, Boolean>>>
        get() = _reviewInitialData

    private val _productFeedbackDetail = MediatorLiveData<Result<ProductFeedbackDetailUiModel>>()
    val productFeedbackDetail: LiveData<Result<ProductFeedbackDetailUiModel>>
        get() = _productFeedbackDetail

    init {
        //Add Default Filter Date Value
        filterByList = mutableListOf(ReviewSellerConstant.mapFilterReviewDetail().getKeyByValue(chipsFilterText))

        setupFeedBackDetail()
        setSortDetailBottomSheet()
    }

    fun setChipFilterDateText(chipFilterText: String) {
        chipsFilterText = chipFilterText
        filterPeriod = ReviewSellerConstant.mapFilterReviewDetail().getKeyByValue(chipFilterText)
        removeFilterElement(prefixTime)
        filterByList.add(ReviewSellerUtil.setFilterJoinValueFormat(filterPeriod))
    }

    private fun setupFeedBackDetail() {
        _productFeedbackDetail.addSource(topicFilterData) { data ->
            val topicFilterText = data.filter { it.isSelected }
            val topicFilterTextGenerated = if (topicFilterText.isEmpty()) "" else topicFilterText.joinToString(prefix = prefixTopic, separator = ",") {
                it.titleUnformated
            }
            removeFilterElement(prefixTopic)

            if (topicFilterTextGenerated.isNotBlank()) {
                filterByList.add(topicFilterTextGenerated)
            }
            getFeedbackDetailListNext(productId, sortBy, 1)
        }

        _productFeedbackDetail.addSource(ratingFilterData) { data ->
            val ratingFilterText = data?.filter { it.ratingIsChecked }.orEmpty()
            val ratingFilterGenerated = if (ratingFilterText.isEmpty()) "" else ratingFilterText.joinToString(prefix = prefixRating, separator = ",") {
                it.ratingLabel.toString()
            }

            removeFilterElement(prefixRating)

            if (ratingFilterGenerated.isNotBlank()) {
                filterByList.add(ratingFilterGenerated)
            }

            getFeedbackDetailListNext(productId, sortBy, 1)
        }

        _productFeedbackDetail.addSource(topicAndSortFilterData) { data ->
            val topicFilterText = data.first.filter { it.isSelected }
            val topicFilterTextGenerated = if (topicFilterText.isEmpty()) "" else topicFilterText.joinToString(prefix = prefixTopic, separator = ",") {
                it.titleUnformated
            }
            removeFilterElement(prefixTopic)

            updateSortAndFilterTopicData(data)

            if (topicFilterTextGenerated.isNotBlank()) {
                filterByList.add(topicFilterTextGenerated)
            }

            sortBy = data.second

            getFeedbackDetailListNext(productId, sortBy, 1)
        }
    }

    private fun removeFilterElement(regex: String) {
        filterByList.removeAll {
            it.contains(regex)
        }
    }

    fun updateRatingFilterData(data: List<RatingBarUiModel>) {
        filterRatingData = data
    }

    fun updateTopicsFilterData(data: List<SortFilterItemWrapper>) {
        filterTopicData = data
    }

    private fun updateSortAndFilterTopicData(data: Pair<List<SortFilterItemWrapper>, String>) {
        this.sortAndFilter = data
    }

    private fun setSortDetailBottomSheet() {
        val data = SellerReviewProductDetailMapper.mapToItemSortTopic()
        sortTopicData = data
        val sortValue = data.firstOrNull { it.isSelected }?.title.orEmpty()
        sortBy = ReviewSellerConstant.mapSortReviewDetail().getKeyByValue(sortValue)
    }

    fun setSortAndFilterTopicData(data: Pair<List<SortFilterItemWrapper>, String>) {
        val updatedData = data.first.map {
            if (it.count == 0) {
                it.isSelected = false
            }
            it
        }
        topicAndSortFilterData.value = updatedData to data.second
    }

    fun setFilterTopicDataText(data: List<SortFilterItemWrapper>?) {
        val updatedData = data?.map {
            if (it.count == 0) {
                it.isSelected = false
            }
            it
        }
        topicFilterData.value = updatedData
    }

    fun setFilterRatingDataText(data: List<RatingBarUiModel>) {
        val updatedData = data.map {
            if (it.ratingCount == 0) {
                it.ratingIsChecked = false
            }
            it
        }
        ratingFilterData.value = updatedData
    }

    fun getProductRatingDetail(productID: Int, sortBy: String) {
        launchCatchError(block = {
            productId = productID
            getProductReviewInitialUseCase.requestParams = GetProductReviewInitialUseCase.createParams(productID, getGeneratedFilterByText(), sortBy, 1, getGeneratedTimeFilterByText())
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
                    uiData.addAll(SellerReviewProductDetailMapper.mapToFeedbackUiModel(productRatingDetailData.productFeedBackResponse!!.productrevFeedbackDataPerProduct, userSession))
                }
                _reviewInitialData.postValue(Success(Triple(uiData, productName, hasNext)))
            }
        })
        {
            _reviewInitialData.postValue(Fail(it))
        }
    }


    private fun getGeneratedFilterByText(): String {
        return if (filterByList.size == 1) {
            filterByList.firstOrNull() ?: ""
        } else {
            filterByList.joinToString(separator = ";")
        }
    }

    private fun getGeneratedTimeFilterByText(): String = filterByList.find { it.contains(prefixTime) }
            ?: ""

    fun getFeedbackDetailListNext(productID: Int, sortBy: String, page: Int) {
        launchCatchError(block = {
            val feedbackDetailList = withContext(dispatcherProvider.io()) {
                getProductFeedbackDetailList(productID, sortBy, getGeneratedFilterByText(), page)
            }
            _productFeedbackDetail.postValue(Success(feedbackDetailList))
        }, onError = {
            _productFeedbackDetail.postValue(Fail(it))
        })
    }

    private suspend fun getProductFeedbackDetailList(productID: Int, sortBy: String, filterBy: String, page: Int = 1): ProductFeedbackDetailUiModel {
        getProductFeedbackDetailListUseCase.params = GetProductFeedbackDetailListUseCase.createParams(
                productID,
                sortBy,
                filterBy,
                ReviewSellerConstant.DEFAULT_PER_PAGE,
                page
        )

        val productFeedbackDetailResponse = getProductFeedbackDetailListUseCase.executeOnBackground()
        return SellerReviewProductDetailMapper.mapToProductFeedbackDetailUiModel(productFeedbackDetailResponse, userSession)
    }
}