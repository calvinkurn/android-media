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

    var positionFilterPeriod = 1
    var filterPeriod: String = ""

    private var chipsFilterText = "30 Hari Terakhir"
    private var productId = 0
    private var filterByList: MutableList<String> = mutableListOf(chipsFilterText)
    private var filterRatingData: List<RatingBarUiModel> = listOf()
    private var filterTopicData: List<SortFilterItemWrapper> = listOf()

    private val _ratingFilterData = MutableLiveData<List<RatingBarUiModel>>()
    private val _topicFilterData = MutableLiveData<List<SortFilterItemWrapper>>()

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
    }

    fun setChipFilterDateText(chipFilterText: String) {
        chipsFilterText = chipFilterText
        filterPeriod = ReviewSellerConstant.mapFilterReviewDetail().getKeyByValue(chipFilterText)
        removeFilterElement("time=")
        filterByList.add(ReviewSellerUtil.setFilterJoinValueFormat(filterPeriod))
    }

    private fun setupFeedBackDetail() {
        _productFeedbackDetail.addSource(_topicFilterData) { data ->
            val topicFilterText = data.filter { it.isSelected }
            val topicFilterTextGenerated = if (topicFilterText.isEmpty()) "" else topicFilterText.joinToString(prefix = "topic=", separator = ",") {
                it.titleUnformated
            }
            removeFilterElement("topic=")

            if (topicFilterTextGenerated.isNotBlank()) {
                filterByList.add(topicFilterTextGenerated)
            }
            getFeedbackDetailListNext(productId, "", 1)
        }

        _productFeedbackDetail.addSource(_ratingFilterData) { data ->
            val ratingFilterText = data?.filter { it.ratingIsChecked }.orEmpty()
            val ratingFilterGenerated = if (ratingFilterText.isEmpty()) "" else ratingFilterText.joinToString(prefix = "rating=", separator = ",") {
                it.ratingLabel.toString()
            }

            removeFilterElement("rating=")

            if (ratingFilterGenerated.isNotBlank()) {
                filterByList.add(ratingFilterGenerated)
            }

            getFeedbackDetailListNext(productId, "", 1)
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

    fun setFilterTopicDataText(data: List<SortFilterItemWrapper>?) {
        val updatedData = data?.map {
            if (it.count == 0) {
                it.isSelected = false
            }
            it
        }
        _topicFilterData.value = updatedData
    }

    fun setFilterRatingDataText(data: List<RatingBarUiModel>) {
        val updatedData = data.map {
            if (it.ratingCount == 0) {
                it.ratingIsChecked = false
            }
            it
        }
        _ratingFilterData.value = updatedData
    }

    fun getProductRatingDetail(productID: Int, sortBy: String) {
        launchCatchError(block = {
            productId = productID
            getProductReviewInitialUseCase.requestParams = GetProductReviewInitialUseCase.createParams(productID, getGeneratedFilterByText(), sortBy, 1, getGeneratedTimeFilterByText())
            val data = getProductReviewInitialUseCase.executeOnBackground()

            val productName = data.productReviewDetailOverallResponse?.productGetReviewAggregateByProduct?.productName
                    ?: ""
            val hasNext = data.productFeedBackResponse?.productrevFeedbackDataPerProduct?.hasNext
                    ?: false
            val productFilterData = data.productReviewFilterResponse?.productrevFeedbackDataPerProduct
                    ?: ProductFeedbackFilterData()
            val uiData: MutableList<BaseSellerReviewDetail> = mutableListOf()

            uiData.add(SellerReviewProductDetailMapper.mapToRatingDetailOverallUiModel(data.productReviewDetailOverallResponse?.productGetReviewAggregateByProduct
                    ?: ProductReviewDetailOverallResponse.ProductGetReviewAggregateByProduct(), chipsFilterText))
            uiData.add(SellerReviewProductDetailMapper.mapToRatingBarUiModel(productFilterData, filterRatingData))
            uiData.add(SellerReviewProductDetailMapper.mapToTopicUiModel(productFilterData,filterTopicData))

            if (data.productFeedBackResponse == null) {
                _reviewInitialData.postValue(Fail(Throwable()))
            } else {
                if (data.productFeedBackResponse?.productrevFeedbackDataPerProduct?.list?.isEmpty() == true || data.productFeedBackResponse?.productrevFeedbackDataPerProduct?.list == null) {
                    uiData.add(ProductFeedbackErrorUiModel(false))
                } else {
                    uiData.addAll(SellerReviewProductDetailMapper.mapToFeedbackUiModel(data.productFeedBackResponse!!.productrevFeedbackDataPerProduct, userSession))
                }
                _reviewInitialData.postValue(Success(Triple(uiData, productName, hasNext)))
            }

        }) {
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

    private fun getGeneratedTimeFilterByText(): String = filterByList.find { it.contains("time=") }
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