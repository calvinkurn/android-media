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
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
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

    private val reviewProductList = MutableLiveData<Result<Pair<List<ReviewProductModel>, Boolean>>>()
    fun getReviewProductList(): LiveData<Result<Pair<List<ReviewProductModel>, Boolean>>> = reviewProductList

    private val ratingReview = MutableLiveData<DataResponseReviewStarCount>()
    fun getRatingReview(): LiveData<DataResponseReviewStarCount> = ratingReview

    private val helpfulReviewList = MutableLiveData<List<ReviewProductModel>>()
    fun getHelpfulReviewList(): LiveData<List<ReviewProductModel>> = helpfulReviewList

    private val isShowProgressDialog = MutableLiveData<Boolean>()
    fun getShowProgressDialog(): LiveData<Boolean> = isShowProgressDialog

    private val deleteReview = MutableLiveData<Result<String>>()
    fun getDeleteReview(): LiveData<Result<String>> = deleteReview

    private val postLikeDislike = MutableLiveData<Pair<LikeDislikeDomain, String>>()
    fun getPostLikeDislike(): LiveData<Pair<LikeDislikeDomain, String>> = postLikeDislike

    private val errorPostLikeDislike = MutableLiveData<Triple<Throwable, String, Int>>()
    fun getErrorPostLikeDislike(): LiveData<Triple<Throwable, String, Int>> = errorPostLikeDislike

    fun getRatingReview(productId: String) {
        launchCatchError(block = {
            reviewProductGetRatingUseCase.setParams(productId)
            val data = reviewProductGetRatingUseCase.executeOnBackground()
            ratingReview.postValue(data)
        }, onError = {})
    }

    fun getHelpfulReview(productId: String) {
        launchCatchError(block = {
            reviewProductGetHelpfulUseCase.setParams(
                    productId = productId,
                    userId = userSession.userId
            )
            val data = reviewProductGetHelpfulUseCase.executeOnBackground()
            val reviewProductList = reviewProductListMapper.map(data, userSession.userId, productId)
            helpfulReviewList.postValue(reviewProductList)
        }, onError = {})
    }

    fun getProductReview(productId: String, page: Int, rating: String, isWithImage: Boolean) {
        launchCatchError(block = {
            reviewProductGetListUseCase.setParams(
                    productId = productId,
                    page = page.toString(),
                    rating = rating,
                    userId = userSession.userId,
                    withAttachment = isWithImage
            )
            val data = reviewProductGetListUseCase.executeOnBackground()
            val reviewProductList = reviewProductListMapper.map(data, userSession.userId)
            val isHasNextPage = !TextUtils.isEmpty(data.paging?.uriNext)
            this@ReviewProductViewModel.reviewProductList.postValue(Success(reviewProductList to isHasNextPage))
        }, onError = {
            this@ReviewProductViewModel.reviewProductList.postValue(Fail(it))
        })
    }

    fun deleteReview(reviewId: String, reputationId: String, productId: String) {
        launchCatchError(block = {
            isShowProgressDialog.postValue(true)
            deleteReviewResponseUseCase.setParams(
                    reviewId = reviewId,
                    productId = productId,
                    shopId = userSession.shopId,
                    reputationId = reputationId
            )
            val data = deleteReviewResponseUseCase.executeOnBackground()
            isShowProgressDialog.postValue(false)
            if (data.isSuccess) {
                deleteReview.postValue(Success(reviewId))
            } else deleteReview.postValue(Fail(RuntimeException()))
        }, onError = {
            isShowProgressDialog.postValue(false)
            deleteReview.postValue(Fail(it))
        })
    }

    fun postLikeDislikeReview(reviewId: String, likeStatus: Int, productId: String?) {
        launchCatchError(block = {
            isShowProgressDialog.postValue(true)
            val shopId = userSession.shopId.let {
                if (TextUtils.isEmpty(it)) "0"
                else it
            }
            likeDislikeReviewUseCase.setParams(
                    reviewId = reviewId,
                    likeStatus = likeStatus,
                    productId = productId ?: "",
                    shopId = shopId
            )
            val data = likeDislikeReviewUseCase.executeOnBackground()
            isShowProgressDialog.postValue(false)
            postLikeDislike.postValue(data to reviewId)
        }, onError = {
            isShowProgressDialog.postValue(false)
            errorPostLikeDislike.postValue(Triple(it, reviewId, likeStatus))
        })
    }
}