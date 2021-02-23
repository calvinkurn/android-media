package com.tokopedia.tkpd.tkpdreputation.review.product.view.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetHelpfulUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetListUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetRatingUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductListMapper
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModel
import com.tokopedia.user.session.UserSession
import rx.Subscriber
import javax.inject.Inject

class ReviewProductViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatchers,
        private val userSession: UserSession,
        private val reviewProductListMapper: ReviewProductListMapper,
        private val reviewProductGetRatingUseCase: ReviewProductGetRatingUseCase,
        private val reviewProductGetHelpfulUseCase: ReviewProductGetHelpfulUseCase,
        private val reviewProductGetListUseCase: ReviewProductGetListUseCase,
        private val deleteReviewResponseUseCase: DeleteReviewResponseUseCase,
        private val likeDislikeReviewUseCase: LikeDislikeReviewUseCase
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
        val requestParams = reviewProductGetRatingUseCase.createRequestParams(productId)
        reviewProductGetRatingUseCase.execute(requestParams, getSubscriberGetRating())
    }

    fun getHelpfulReview(productId: String) {
        val requestParams = reviewProductGetHelpfulUseCase.createRequestParams(productId, userSession.userId)
        reviewProductGetHelpfulUseCase.execute(requestParams, getSubscriberGetHelpfulReview((productId)))
    }

    fun getProductReview(productId: String, page: Int, rating: String, isWithImage: Boolean) {
        val requestParams = reviewProductGetListUseCase.createRequestParams(productId, page.toString(), rating, userSession.userId).let {
            if (isWithImage) reviewProductGetListUseCase.withPhotoParams(it)
            else it
        }
        reviewProductGetListUseCase.execute(requestParams, getSubscriberGetReviewProduct())

    }

    fun deleteReview(reviewId: String?, reputationId: String?, productId: String) {
        isShowProgressDialog.postValue(true)

        val requestParams = DeleteReviewResponseUseCase.getParam(reviewId, productId, userSession.shopId, reputationId)
        deleteReviewResponseUseCase.execute(requestParams, getSubscriberDeleteReview(reviewId))
    }

    fun postLikeDislikeReview(reviewId: String, likeStatus: Int, productId: String?) {
        isShowProgressDialog.postValue(true)
        val shopId = userSession.shopId.let {
            if (TextUtils.isEmpty(it)) "0"
            else it
        }
        likeDislikeReviewUseCase.execute(LikeDislikeReviewUseCase.getParam(reviewId, likeStatus, productId, shopId), getSubscriberPostLikeDislike(reviewId, likeStatus))
    }

    private fun getSubscriberGetRating(): Subscriber<DataResponseReviewStarCount> {
        return object : Subscriber<DataResponseReviewStarCount>() {

            override fun onNext(t: DataResponseReviewStarCount?) {
                ratingReview.postValue(t)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {}
        }
    }

    private fun getSubscriberGetHelpfulReview(productId: String): Subscriber<DataResponseReviewHelpful> {
        return object : Subscriber<DataResponseReviewHelpful>() {
            override fun onNext(t: DataResponseReviewHelpful?) {
                val reviewProductList = reviewProductListMapper.map(t, userSession.userId, productId)
                helpfulReviewList.postValue(reviewProductList)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {}
        }
    }

    private fun getSubscriberGetReviewProduct(): Subscriber<DataResponseReviewProduct> {
        return object : Subscriber<DataResponseReviewProduct>() {
            override fun onNext(t: DataResponseReviewProduct?) {
                val reviewProductList = reviewProductListMapper.map(t, userSession.userId)
                val isHasNextPage = !TextUtils.isEmpty(t?.paging?.uriNext)
                this@ReviewProductViewModel.reviewProductList.postValue(reviewProductList to isHasNextPage)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                reviewProductListError.postValue(e)
            }

        }
    }

    private fun getSubscriberDeleteReview(reviewId: String?): Subscriber<DeleteReviewResponseDomain> {
        return object : Subscriber<DeleteReviewResponseDomain>() {
            override fun onNext(t: DeleteReviewResponseDomain?) {
                isShowProgressDialog.postValue(false)
                if (t?.isSuccess == true) {
                    deleteReview.postValue(reviewId)
                } else deleteReviewError.postValue(RuntimeException())
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                isShowProgressDialog.postValue(false)
                deleteReviewError.postValue(e)
            }
        }
    }

    private fun getSubscriberPostLikeDislike(reviewId: String, likeStatus: Int): Subscriber<LikeDislikeDomain> {
        return object : Subscriber<LikeDislikeDomain>() {
            override fun onNext(t: LikeDislikeDomain) {
                isShowProgressDialog.postValue(false)
                postLikeDislike.postValue(t to reviewId)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                isShowProgressDialog.postValue(false)
                errorPostLikeDislike.postValue(Triple(e, reviewId, likeStatus))
            }

        }
    }
}