package com.tokopedia.shop.review.shop.view.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.review.product.data.model.reviewlist.DataResponseReviewShop
import com.tokopedia.shop.review.product.view.ReviewProductListMapper
import com.tokopedia.shop.review.shop.domain.DeleteReviewResponseUseCase
import com.tokopedia.shop.review.shop.domain.LikeDislikeReviewUseCase
import com.tokopedia.shop.review.shop.domain.ReviewShopUseCase
import com.tokopedia.shop.review.shop.domain.model.DeleteReviewResponseDomain
import com.tokopedia.shop.review.shop.domain.model.LikeDislikeDomain
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 1/19/18.
 */
class ReviewShopPresenter @Inject constructor(private val shopReviewUseCase: ReviewShopUseCase,
                                              private val likeDislikeReviewUseCase: LikeDislikeReviewUseCase,
                                              private val deleteReviewResponseUseCase: DeleteReviewResponseUseCase,
                                              private val productReviewListMapper: ReviewProductListMapper,
                                              private val userSession: UserSessionInterface) : BaseDaggerPresenter<ReviewShopContract.View?>(), ReviewShopContract.Presenter {

    val isLogin: Boolean
        get() = userSession.isLoggedIn

    override fun onDestroy() {
        shopReviewUseCase.unsubscribe()
        likeDislikeReviewUseCase.unsubscribe()
        deleteReviewResponseUseCase.unsubscribe()
    }

    fun deleteReview(reviewId: String?, reputationId: String?, productId: String?) {
        view!!.showProgressLoading()
        deleteReviewResponseUseCase.execute(DeleteReviewResponseUseCase.Companion.getParam(reviewId, productId, userSession.shopId, reputationId),
                getSubscriberDeleteReview(reviewId))
    }

    private fun getSubscriberDeleteReview(reviewId: String?): Subscriber<DeleteReviewResponseDomain?> {
        return object : Subscriber<DeleteReviewResponseDomain?>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view!!.hideProgressLoading()
                    view!!.onErrorDeleteReview(e)
                }
            }

            override fun onNext(deleteReviewResponseDomain: DeleteReviewResponseDomain?) {
                view!!.hideProgressLoading()
                if (deleteReviewResponseDomain?.isSuccess == true) {
                    view!!.onSuccessDeleteReview(deleteReviewResponseDomain, reviewId)
                } else {
                    view!!.onErrorDeleteReview(RuntimeException())
                }
            }
        }
    }

    fun postLikeDislikeReview(reviewId: String?, likeStatus: Int, productId: String?) {
        view!!.showProgressLoading()
        likeDislikeReviewUseCase.execute(LikeDislikeReviewUseCase.Companion.getParam(reviewId, likeStatus, productId, userSession.shopId),
                getSubscriberPostLikeDislike(reviewId, likeStatus))
    }

    private fun getSubscriberPostLikeDislike(reviewId: String?, likeStatus: Int): Subscriber<LikeDislikeDomain?> {
        return object : Subscriber<LikeDislikeDomain?>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view!!.hideProgressLoading()
                    view!!.onErrorPostLikeDislike(e, reviewId, likeStatus)
                }
            }

            override fun onNext(likeDislikeDomain: LikeDislikeDomain?) {
                view!!.hideProgressLoading()
                view!!.onSuccessPostLikeDislike(likeDislikeDomain, reviewId)
            }
        }
    }

    fun getShopReview(shopDomain: String?, shopId: String?, page: Int) {
        shopReviewUseCase.execute(
                shopReviewUseCase.createRequestParams(shopDomain, shopId, page.toString(), userSession.userId),
                subscriberGetShopReview
        )
    }

    protected val subscriberGetShopReview: Subscriber<DataResponseReviewShop?>
        protected get() = object : Subscriber<DataResponseReviewShop?>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view!!.showGetListError(e)
                }
            }

            override fun onNext(dataResponseReviewShop: DataResponseReviewShop?) {
                dataResponseReviewShop?.let{
                    view!!.renderList(productReviewListMapper.map(dataResponseReviewShop, userSession.userId),
                            !TextUtils.isEmpty(dataResponseReviewShop.paging!!.uriNext))
                }
            }
        }

    fun isMyShop(shopId: String?): Boolean {
        return userSession.shopId == shopId
    }

}