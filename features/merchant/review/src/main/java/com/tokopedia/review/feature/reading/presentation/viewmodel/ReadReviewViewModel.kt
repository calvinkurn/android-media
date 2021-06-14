package com.tokopedia.review.feature.reading.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetProductReviewList
import com.tokopedia.review.feature.reading.domain.usecase.GetProductRatingAndTopicsUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetProductReviewListUseCase
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
        private const val INITIAL_PAGE = 0
    }

    private val _ratingAndTopics = MediatorLiveData<Result<ProductrevGetProductRatingAndTopic>>()
    val ratingAndTopic: LiveData<Result<ProductrevGetProductRatingAndTopic>>
        get() = _ratingAndTopics

    private val _productReviews = MediatorLiveData<ProductrevGetProductReviewList>()
    val productReviews: LiveData<ProductrevGetProductReviewList>
        get() = _productReviews

    private val currentPage = MutableLiveData(INITIAL_PAGE)
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

    init {
        _ratingAndTopics.addSource(productId) {
            getRatingAndTopics(it)
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
}