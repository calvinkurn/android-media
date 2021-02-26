package com.tokopedia.tkpd.tkpdreputation.review.product.view.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetListUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.usecase.ReviewProductGetHelpfulUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.usecase.ReviewProductGetRatingUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductListMapper
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModel
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewProductViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatchers,
        private val userSession: UserSession,
        private val reviewProductListMapper: ReviewProductListMapper,
        private val reviewProductGetHelpfulUseCase: ReviewProductGetHelpfulUseCase,
        private val reviewProductGetListUseCase: ReviewProductGetListUseCase,
        private val deleteReviewResponseUseCase: DeleteReviewResponseUseCase,
        private val likeDislikeReviewUseCase: LikeDislikeReviewUseCase,
        private val reviewProductGetRatingUseCase: ReviewProductGetRatingUseCase
) : BaseViewModel(dispatcherProvider.main) {

    private val reviewProductList = MutableLiveData<Pair<List<ReviewProductModel>, Boolean>>()
    fun getReviewProductList(): LiveData<Pair<List<ReviewProductModel>, Boolean>> = reviewProductList

    private val reviewProductListError = MutableLiveData<Throwable>()
    fun getReviewProductListError(): LiveData<Throwable> = reviewProductListError

    private val ratingReview = MutableLiveData<DataResponseReviewStarCount>()
    fun getRatingReview(): LiveData<DataResponseReviewStarCount> = ratingReview

    private val helpfulReviewList = MutableLiveData<List<ReviewProductModel>>()
    fun getHelpfulReviewList(): LiveData<List<ReviewProductModel>> = helpfulReviewList

    private val isShowProgressDialog = MutableLiveData<Boolean>()
    fun getShowProgressDialog(): LiveData<Boolean> = isShowProgressDialog

    private val deleteReview = MutableLiveData<String>()
    fun getDeleteReview(): LiveData<String> = deleteReview

    private val deleteReviewError = MutableLiveData<Throwable>()
    fun getDeleteReviewError(): LiveData<Throwable> = deleteReviewError

    private val postLikeDislike = MutableLiveData<Pair<LikeDislikeDomain, String>>()
    fun getPostLikeDislike(): LiveData<Pair<LikeDislikeDomain, String>> = postLikeDislike

    private val errorPostLikeDislike = MutableLiveData<Triple<Throwable, String, Int>>()
    fun getErrorPostLikeDislike(): LiveData<Triple<Throwable, String, Int>> = errorPostLikeDislike

    fun getRatingReview(productId: String) {
        launchCatchError(block = {
            reviewProductGetRatingUseCase.params = ReviewProductGetRatingUseCase.Params(
                    productId = productId
            )
            val data = reviewProductGetRatingUseCase.executeOnBackground()
            withContext(dispatcherProvider.main) { ratingReview.value = data }
        }, onError = {})
    }

    fun getHelpfulReview(productId: String) {
        launchCatchError(block = {
            reviewProductGetHelpfulUseCase.params = ReviewProductGetHelpfulUseCase.Params(
                    productId = productId,
                    userId = userSession.userId
            )
            val data = reviewProductGetHelpfulUseCase.executeOnBackground()
            val reviewProductList = reviewProductListMapper.map(data, userSession.userId, productId)
            withContext(dispatcherProvider.main) { helpfulReviewList.value = reviewProductList }
        }, onError = {})
    }

    fun getProductReview(productId: String, page: Int, rating: String, isWithImage: Boolean) {
        launchCatchError(block = {
            reviewProductGetListUseCase.params = ReviewProductGetListUseCase.Params(
                    productId = productId,
                    page = page.toString(),
                    rating = rating,
                    userId = userSession.userId,
                    withAttachment = isWithImage
            )
            val data = reviewProductGetListUseCase.executeOnBackground()
            val reviewProductList = reviewProductListMapper.map(data, userSession.userId)
            val isHasNextPage = !TextUtils.isEmpty(data?.paging?.uriNext)
            withContext(dispatcherProvider.main) {
                this@ReviewProductViewModel.reviewProductList.value = reviewProductList to isHasNextPage
            }
        }, onError = {
            withContext(dispatcherProvider.main) { reviewProductListError.value = it }
        })
    }

    fun deleteReview(reviewId: String?, reputationId: String?, productId: String) {
        launchCatchError(block = {
            withContext(dispatcherProvider.main) { isShowProgressDialog.value = true }

            deleteReviewResponseUseCase.params = DeleteReviewResponseUseCase.Params(
                    reviewId = reviewId ?: "",
                    productId = productId,
                    shopId = userSession.shopId,
                    reputationId = reputationId ?: ""
            )
            val data = deleteReviewResponseUseCase.executeOnBackground()
            withContext(dispatcherProvider.main) {
                isShowProgressDialog.value = false
                if (data.isSuccess) {
                    deleteReview.value = reviewId
                } else deleteReviewError.value = RuntimeException()
            }

        }, onError = {
            withContext(dispatcherProvider.main) {
                isShowProgressDialog.value = false
                deleteReviewError.value = it
            }
        })
    }

    fun postLikeDislikeReview(reviewId: String, likeStatus: Int, productId: String?) {
        launchCatchError(block = {
            withContext(dispatcherProvider.main) { isShowProgressDialog.value = true }
            val shopId = userSession.shopId.let {
                if (TextUtils.isEmpty(it)) "0"
                else it
            }
            likeDislikeReviewUseCase.params = LikeDislikeReviewUseCase.Params(
                    reviewId = reviewId,
                    likeStatus = likeStatus,
                    productId = productId ?: "",
                    shopId = shopId
            )
            val data = likeDislikeReviewUseCase.executeOnBackground()
            withContext(dispatcherProvider.main) {
                isShowProgressDialog.value = false
                postLikeDislike.value = data to reviewId
            }
        }, onError = {
            withContext(dispatcherProvider.main) {
                isShowProgressDialog.value = false
                errorPostLikeDislike.value = Triple(it, reviewId, likeStatus)
            }
        })
    }
}