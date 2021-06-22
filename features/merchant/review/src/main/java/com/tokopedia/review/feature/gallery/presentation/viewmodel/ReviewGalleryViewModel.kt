package com.tokopedia.review.feature.gallery.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.data.ToggleProductReviewLike
import com.tokopedia.review.common.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.feature.reading.utils.ReadReviewUtils
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReviewGalleryViewModel @Inject constructor(
        private val toggleLikeReviewUseCase: ToggleLikeReviewUseCase,
        coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(coroutineDispatchers.io) {

    private val _toggleLikeReview = MutableLiveData<Result<ToggleProductReviewLike>>()
    val toggleLikeReview: LiveData<Result<ToggleProductReviewLike>>
        get() = _toggleLikeReview

    fun toggleLikeReview(reviewId: String, shopId: String, productId: String, likeStatus: Int) {
        launchCatchError(block = {
            toggleLikeReviewUseCase.setParams(reviewId, shopId, productId
                    ?: "", ReadReviewUtils.invertLikeStatus(likeStatus))
            val data = toggleLikeReviewUseCase.executeOnBackground()
            _toggleLikeReview.postValue(Success(data.toggleProductReviewLike))
        }) {
            _toggleLikeReview.postValue(Fail(it))
        }
    }
}