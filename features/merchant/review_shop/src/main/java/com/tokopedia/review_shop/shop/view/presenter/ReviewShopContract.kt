package com.tokopedia.review_shop.shop.view.presenter

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.review_shop.shop.domain.model.DeleteReviewResponseDomain
import com.tokopedia.review_shop.shop.domain.model.LikeDislikeDomain
import com.tokopedia.review_shop.shop.view.adapter.ReviewShopModelContent

/**
 * Created by zulfikarrahman on 1/19/18.
 */
interface ReviewShopContract {
    interface Presenter : CustomerPresenter<View?> {
        fun onDestroy()
    }

    interface View : BaseListViewListener<ReviewShopModelContent?> {
        fun onErrorDeleteReview(e: Throwable?)
        fun onSuccessDeleteReview(deleteReviewResponseDomain: DeleteReviewResponseDomain?, reviewId: String?)
        fun onErrorPostLikeDislike(e: Throwable?, reviewId: String?, likeStatus: Int)
        fun onSuccessPostLikeDislike(likeDislikeDomain: LikeDislikeDomain?, reviewId: String?)
        fun hideProgressLoading()
        fun showProgressLoading()
    }
}