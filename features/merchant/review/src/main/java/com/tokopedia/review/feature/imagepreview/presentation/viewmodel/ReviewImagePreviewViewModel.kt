package com.tokopedia.review.feature.imagepreview.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.data.ProductrevLikeReview
import com.tokopedia.review.common.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.feature.reading.utils.ReadReviewUtils
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.usecase.GetDetailedReviewMediaUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ReviewImagePreviewViewModel @Inject constructor(
    private val toggleLikeReviewUseCase: ToggleLikeReviewUseCase,
    private val getDetailedReviewMediaUseCase: GetDetailedReviewMediaUseCase,
    private val userSession: UserSessionInterface,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(coroutineDispatchers.io) {

    private val _toggleLikeReview = MutableLiveData<Result<ProductrevLikeReview>>()
    val toggleLikeReviewReview: LiveData<Result<ProductrevLikeReview>>
        get() = _toggleLikeReview

    private val _reviewImages = MediatorLiveData<Result<ProductrevGetReviewMedia>>()
    val reviewImages: LiveData<Result<ProductrevGetReviewMedia>>
        get() = _reviewImages

    private val page = MutableLiveData<Int>()
    private var productId = ""

    init {
        _reviewImages.addSource(page) {
            getReviewImages(productId, it)
        }
    }

    fun setProductId(productId: String) {
        this.productId = productId
    }

    fun setPage(page: Int) {
        this.page.value = page
    }

    fun getUserId(): String {
        return userSession.userId
    }

    fun getProductId(): String {
        return productId
    }

    fun toggleLikeReview(reviewId: String, likeStatus: Int) {
        launchCatchError(block = {
            toggleLikeReviewUseCase.setParams(
                reviewId, ReadReviewUtils.invertLikeStatus(likeStatus)
            )
            val data = toggleLikeReviewUseCase.executeOnBackground()
            _toggleLikeReview.postValue(Success(data.productrevLikeReview))
        }) {
            _toggleLikeReview.postValue(Fail(it))
        }
    }

    private fun getReviewImages(productId: String, page: Int) {
        launchCatchError(block = {
            getDetailedReviewMediaUseCase.setParams(
                productId,
                page
            )
            val data = getDetailedReviewMediaUseCase.executeOnBackground()
            _reviewImages.postValue(Success(data.productrevGetReviewMedia))
        }) {
            _reviewImages.postValue(Fail(it))
        }
    }
}