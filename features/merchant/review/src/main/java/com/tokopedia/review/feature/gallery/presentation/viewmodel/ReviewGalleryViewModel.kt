package com.tokopedia.review.feature.gallery.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.gallery.data.ProductrevGetReviewImage
import com.tokopedia.review.feature.gallery.domain.usecase.GetProductRatingUseCase
import com.tokopedia.review.feature.gallery.domain.usecase.GetReviewImagesUseCase
import com.tokopedia.review.feature.gallery.presentation.uimodel.SelectedReview
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReviewGalleryViewModel @Inject constructor(
    private val getProductRatingUseCase: GetProductRatingUseCase,
    private val getReviewImagesUseCase: GetReviewImagesUseCase,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(coroutineDispatchers.io) {

    private var productId: MutableLiveData<String> = MutableLiveData()
    private val currentPage = MutableLiveData<Int>()

    private val _rating = MediatorLiveData<Result<ProductRating>>()
    val rating: LiveData<Result<ProductRating>>
        get() = _rating

    private val _reviewImages = MediatorLiveData<Result<ProductrevGetReviewImage>>()
    val reviewImages: LiveData<Result<ProductrevGetReviewImage>>
        get() = _reviewImages

    init {
        _rating.addSource(productId) {
            getRating(it)
        }
        _reviewImages.addSource(currentPage) {
            getReviewImages(it)
        }
    }

    fun setProductId(productId: String) {
        this.productId.value = productId
    }

    fun getProductId(): String {
        return this.productId.value ?: ""
    }

    fun setPage(page: Int) {
        currentPage.value = page
    }

    fun getReviewDataBasedOnFeedbackId(fullImageUrl: String, feedbackId: String): SelectedReview{
        (_reviewImages.value as? Success)?.data?.let { response ->
            val reviewData = response.detail.reviewDetail.firstOrNull {
                it.feedbackId == feedbackId
            } ?: return SelectedReview()
            with(reviewData) {
                return SelectedReview(
                    fullImageUrl,
                    user.fullName,
                    rating,
                    isLiked,
                    totalLike,
                    review,
                    createTimestamp,
                    isReportable
                )
            }
        }
        return SelectedReview()
    }

    fun getImageCount(): Long {
        return (_reviewImages.value as? Success)?.data?.detail?.imageCount ?: 0L
    }

    private fun getRating(productId: String) {
        launchCatchError(block = {
            getProductRatingUseCase.setParams(productId)
            val data = getProductRatingUseCase.executeOnBackground()
            _rating.postValue(Success(data.productRating))
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