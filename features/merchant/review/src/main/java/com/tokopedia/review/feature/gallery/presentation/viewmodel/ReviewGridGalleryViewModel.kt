package com.tokopedia.review.feature.gallery.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.gallery.data.ProductrevGetReviewImage
import com.tokopedia.review.feature.gallery.domain.usecase.GetReviewImagesUseCase
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.domain.usecase.GetProductRatingAndTopicsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReviewGridGalleryViewModel @Inject constructor(
    private val getProductRatingAndTopicsUseCase: GetProductRatingAndTopicsUseCase,
    private val getReviewImagesUseCase: GetReviewImagesUseCase,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(coroutineDispatchers.io) {

    private var productId: MutableLiveData<String> = MutableLiveData()
    private val currentPage = MutableLiveData<Int>()

    private val _rating = MediatorLiveData<Result<ProductrevGetProductRatingAndTopic>>()
    val rating: LiveData<Result<ProductrevGetProductRatingAndTopic>>
        get() = _rating

    private val _reviewImages = MediatorLiveData<Result<ProductrevGetReviewImage>>()
    val reviewImages: LiveData<Result<ProductrevGetReviewImage>>
        get() = _reviewImages

    init {
        _rating.addSource(productId) {
            getRatingAndTopics(it)
        }
        _reviewImages.addSource(currentPage) {
            getReviewImages(it)
        }
    }

    fun setPage(page: Int) {
        currentPage.value = page
    }

    private fun getRatingAndTopics(productId: String) {
        launchCatchError(block = {
            getProductRatingAndTopicsUseCase.setParams(productId)
            val data = getProductRatingAndTopicsUseCase.executeOnBackground()
            _rating.postValue(Success(data.productrevGetProductRatingAndTopics))
        }) {
            _rating.postValue(Fail(it))
        }
    }

    private fun getReviewImages(page: Int) {
        launchCatchError(block = {
            getReviewImagesUseCase.setParams(
                productId.value ?: "",
                page
            )
            val data = getReviewImagesUseCase.executeOnBackground()
            _reviewImages.postValue(Success(data.productrevGetReviewImage))
        }) {
            _reviewImages.postValue(Fail(it))
        }
    }
}