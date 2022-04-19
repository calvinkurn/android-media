package com.tokopedia.review.feature.gallery.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.gallery.data.Detail
import com.tokopedia.review.feature.gallery.data.ProductrevGetReviewImage
import com.tokopedia.review.feature.gallery.domain.usecase.GetProductRatingUseCase
import com.tokopedia.review.feature.gallery.domain.usecase.GetReviewImagesUseCase
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
                getReviewImagesUseCase.setParams(it, page)
                val data = getReviewImagesUseCase.executeOnBackground()
                _reviewImages.postValue(Success(data.productrevGetReviewImage))
                allReviewDetail.add(data.productrevGetReviewImage.detail)
                shopId = data.productrevGetReviewImage.detail.reviewDetail.firstOrNull()?.shopId ?: ""
            }
        }) {
            _reviewImages.postValue(Fail(it))
        }
    }
}