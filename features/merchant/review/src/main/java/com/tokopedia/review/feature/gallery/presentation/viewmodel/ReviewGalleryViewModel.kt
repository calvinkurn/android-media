package com.tokopedia.review.feature.gallery.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.review.feature.gallery.domain.usecase.GetProductRatingUseCase
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.usecase.GetDetailedReviewMediaUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReviewGalleryViewModel @Inject constructor(
    private val getProductRatingUseCase: GetProductRatingUseCase,
    private val getDetailedReviewMediaUseCase: GetDetailedReviewMediaUseCase,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(coroutineDispatchers.io) {

    private var productId: MutableLiveData<String> = MutableLiveData()
    private val currentPage = MutableLiveData<Int>()

    private val _rating = MediatorLiveData<Result<ProductRating>>()
    val rating: LiveData<Result<ProductRating>>
        get() = _rating

    private val _reviewImages = MediatorLiveData<Result<ProductrevGetReviewMedia>>()
    val reviewMedia: LiveData<Result<ProductrevGetReviewMedia>>
        get() = _reviewImages

    private val _concatenatedReviewImages = MutableLiveData<ProductrevGetReviewMedia>()
    val concatenatedReviewImages: LiveData<ProductrevGetReviewMedia>
        get() = _concatenatedReviewImages

    private val allReviewDetail = mutableListOf<Detail>()
    private var shopId = ""

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

    fun getShopId(): String {
        return shopId
    }

    fun setPage(page: Int) {
        currentPage.value = page
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
            productId.value?.let {
                getDetailedReviewMediaUseCase.setParams(it, page)
                val data = getDetailedReviewMediaUseCase.executeOnBackground()
                _reviewImages.postValue(Success(data.productrevGetReviewMedia))
                if (page == 1) {
                    _concatenatedReviewImages.postValue(data.productrevGetReviewMedia)
                } else {
                    _concatenatedReviewImages.postValue(mergeDetailedReviewMediaUseCaseResult(_concatenatedReviewImages.value, data.productrevGetReviewMedia, page))
                }
                allReviewDetail.add(data.productrevGetReviewMedia.detail)
                shopId = data.productrevGetReviewMedia.detail.reviewDetail.firstOrNull()?.shopId ?: ""
            }
        }) {
            _reviewImages.postValue(Fail(it))
        }
    }

    private fun mergeDetailedReviewMediaUseCaseResult(
        oldResponse: ProductrevGetReviewMedia?,
        newResponse: ProductrevGetReviewMedia,
        pageToLoad: Int
    ): ProductrevGetReviewMedia {
        return if (oldResponse == null) {
            newResponse
        } else {
            val mergedReviewImages = if (pageToLoad == getPrevPage()) {
                newResponse.reviewMedia.plus(oldResponse.reviewMedia)
            } else {
                oldResponse.reviewMedia.plus(newResponse.reviewMedia)
            }
            val mergedReviewDetail = if (pageToLoad == getPrevPage()) {
                newResponse.detail.reviewDetail.plus(oldResponse.detail.reviewDetail)
            } else {
                oldResponse.detail.reviewDetail.plus(newResponse.detail.reviewDetail)
            }
            val mergedReviewGalleryImages = if (pageToLoad == getPrevPage()) {
                newResponse.detail.reviewGalleryImages.plus(oldResponse.detail.reviewGalleryImages)
            } else {
                oldResponse.detail.reviewGalleryImages.plus(newResponse.detail.reviewGalleryImages)
            }
            oldResponse.copy(
                reviewMedia = mergedReviewImages,
                detail = oldResponse.detail.copy(
                    reviewDetail = mergedReviewDetail,
                    reviewGalleryImages = mergedReviewGalleryImages,
                    mediaCountFmt = newResponse.detail.mediaCountFmt,
                    mediaCount = newResponse.detail.mediaCount
                ),
                hasNext = if (pageToLoad == getPrevPage()) oldResponse.hasNext else newResponse.hasNext,
                hasPrev = if (pageToLoad == getPrevPage()) newResponse.hasPrev else oldResponse.hasPrev
            )
        }
    }

    private fun getPrevPage(): Int {
        val firstLoaded = _concatenatedReviewImages.value?.reviewMedia?.firstOrNull()?.mediaNumber.orZero()
        return (firstLoaded / GetDetailedReviewMediaUseCase.DEFAULT_LIMIT)
    }
}