package com.tokopedia.tkpd.tkpdreputation.review.product.view.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCaseV2
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCaseV2
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetListUseCaseV2
import com.tokopedia.tkpd.tkpdreputation.review.product.usecase.ReviewProductGetHelpfulUseCaseV2
import com.tokopedia.tkpd.tkpdreputation.review.product.usecase.ReviewProductGetRatingUseCaseV2
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductListMapper
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModel
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class ReviewProductViewModel @Inject constructor(
        private val userSession: UserSession,
        private val reviewProductListMapper: ReviewProductListMapper,
        private val reviewProductGetHelpfulUseCaseV2: ReviewProductGetHelpfulUseCaseV2,
        private val reviewProductGetListUseCaseV2: ReviewProductGetListUseCaseV2,
        private val deleteReviewResponseUseCaseV2: DeleteReviewResponseUseCaseV2,
        private val likeDislikeReviewUseCaseV2: LikeDislikeReviewUseCaseV2,
        private val reviewProductGetRatingUseCaseV2: ReviewProductGetRatingUseCaseV2
) : BaseViewModel(CoroutineDispatchersProvider.main) {

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
            reviewProductGetRatingUseCaseV2.params = ReviewProductGetRatingUseCaseV2.Params(
                    productId = productId
            )
            val data = reviewProductGetRatingUseCaseV2.executeOnBackground()
            ratingReview.postValue(data)
        }, onError = {})
    }

    fun getHelpfulReview(productId: String) {
        launchCatchError(block = {
            reviewProductGetHelpfulUseCaseV2.params = ReviewProductGetHelpfulUseCaseV2.Params(
                    productId = productId,
                    userId = userSession.userId
            )
            val data = reviewProductGetHelpfulUseCaseV2.executeOnBackground()
            val reviewProductList = reviewProductListMapper.map(data, userSession.userId, productId)
            helpfulReviewList.postValue(reviewProductList)
        }, onError = {})
    }

    fun getProductReview(productId: String, page: Int, rating: String, isWithImage: Boolean) {
        launchCatchError(block = {
            reviewProductGetListUseCaseV2.params = ReviewProductGetListUseCaseV2.Params(
                    productId = productId,
                    page = page.toString(),
                    rating = rating,
                    userId = userSession.userId,
                    withAttachment = isWithImage
            )
            val data = reviewProductGetListUseCaseV2.executeOnBackground()
            val reviewProductList = reviewProductListMapper.map(data, userSession.userId)
            val isHasNextPage = !TextUtils.isEmpty(data?.paging?.uriNext)
            this@ReviewProductViewModel.reviewProductList.postValue(reviewProductList to isHasNextPage)
        }, onError = {
            reviewProductListError.postValue(it)
        })
    }

    fun deleteReview(reviewId: String?, reputationId: String?, productId: String) {
        launchCatchError(block = {
            isShowProgressDialog.postValue(true)

            deleteReviewResponseUseCaseV2.params = DeleteReviewResponseUseCaseV2.Params(
                    reviewId = reviewId ?: "",
                    productId = productId,
                    shopId = userSession.shopId,
                    reputationId = reputationId ?: ""
            )
            val data = deleteReviewResponseUseCaseV2.executeOnBackground()
            isShowProgressDialog.postValue(false)
            if (data.isSuccess) {
                deleteReview.postValue(reviewId)
            } else deleteReviewError.postValue(RuntimeException())

        }, onError = {
            isShowProgressDialog.postValue(false)
            deleteReviewError.postValue(it)
        })
    }

    fun postLikeDislikeReview(reviewId: String, likeStatus: Int, productId: String?) {
        launchCatchError(block = {
            isShowProgressDialog.postValue(true)
            val shopId = userSession.shopId.let {
                if (TextUtils.isEmpty(it)) "0"
                else it
            }
            likeDislikeReviewUseCaseV2.params = LikeDislikeReviewUseCaseV2.Params(
                    reviewId = reviewId,
                    likeStatus = likeStatus,
                    productId = productId ?: "",
                    shopId = shopId
            )
            val data = likeDislikeReviewUseCaseV2.executeOnBackground()
            isShowProgressDialog.postValue(false)
            postLikeDislike.postValue(data to reviewId)
        }, onError = {
            isShowProgressDialog.postValue(false)
            errorPostLikeDislike.postValue(Triple(it, reviewId, likeStatus))
        })
    }
}