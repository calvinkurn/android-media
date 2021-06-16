package com.tokopedia.review.feature.reading.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetProductReviewList
import com.tokopedia.review.feature.reading.domain.usecase.GetProductRatingAndTopicsUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetProductReviewListUseCase
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReadReviewViewModel @Inject constructor(
        private val getProductRatingAndTopicsUseCase: GetProductRatingAndTopicsUseCase,
        private val getProductReviewListUseCase: GetProductReviewListUseCase,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        const val INITIAL_PAGE = 0
    }

    private val _ratingAndTopics = MediatorLiveData<Result<ProductrevGetProductRatingAndTopic>>()
    val ratingAndTopic: LiveData<Result<ProductrevGetProductRatingAndTopic>>
        get() = _ratingAndTopics

    private val _productReviews = MediatorLiveData<Result<ProductrevGetProductReviewList>>()
    val productReviews: LiveData<Result<ProductrevGetProductReviewList>>
        get() = _productReviews

    private val currentPage = MutableLiveData<Int>()
    private var productId: MutableLiveData<String> = MutableLiveData()

    fun setProductId(productId: String) {
        this.productId.value = productId
    }

    fun getReviewStatistics(): List<ProductReviewDetail> {
        return (ratingAndTopic.value as? Success)?.data?.rating?.detail ?: listOf()
    }

    fun getReviewSatisfactionRate(): String {
        return (ratingAndTopic.value as? Success)?.data?.rating?.satisfactionRate ?: ""
    }

    fun setPage(page: Int) {
        currentPage.value = page
    }

    fun mapProductReviewToReadReviewUiModel(productReviews: List<ProductReview>, shopId: String): List<ReadReviewUiModel> {
        return productReviews.map {
            ReadReviewUiModel(it, false, shopId)
        }
    }

    init {
        _ratingAndTopics.addSource(productId) {
            getRatingAndTopics(it)
        }
        _productReviews.addSource(currentPage) {
            getProductReviews(it)
        }
    }

    private fun getProductReviews(page: Int) {
        launchCatchError(block = {
            getProductReviewListUseCase.setParams(productId.value ?: "", page)
            val data = getProductReviewListUseCase.executeOnBackground()
            _productReviews.postValue(Success(data.productrevGetProductReviewList))
        }) {
            _productReviews.postValue(Fail(it))
        }
    }

    private fun getRatingAndTopics(productId: String) {
        launchCatchError(block = {
            getProductRatingAndTopicsUseCase.setParams(productId)
            val data = getProductRatingAndTopicsUseCase.executeOnBackground()
            _ratingAndTopics.postValue(Success(data.productrevGetProductRatingAndTopics))
        }) {
            _ratingAndTopics.postValue(Fail(it))
        }
    }

    private fun resetPage() {
        setPage(INITIAL_PAGE)
    }
}