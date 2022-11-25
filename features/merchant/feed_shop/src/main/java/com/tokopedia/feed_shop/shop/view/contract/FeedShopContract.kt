package com.tokopedia.feed_shop.shop.view.contract

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.feed_shop.shop.domain.WhitelistDomain
import com.tokopedia.feed_shop.shop.view.model.WhitelistUiModel
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by yfsx on 08/05/19.
 */
interface FeedShopContract {
    interface View : BaseListViewListener<Visitable<*>> {

        var userSession: UserSessionInterface

        fun onSuccessGetFeedFirstPage(element: List<Visitable<*>>, lastCursor: String, whitelistDomain: WhitelistDomain)

        fun onSuccessGetFeedNotLoginFirstPage(element: List<Visitable<*>>, lastCursor: String)

        fun onSuccessGetFeed(visitables: List<Visitable<*>>, lastCursor: String)

        fun updateCursor(cursor: String)

        fun onSuccessFollowKol()

        fun onErrorFollowKol(errorMessage: String)

        fun onSuccessDeletePost(rowNumber: Int)

        fun onErrorDeletePost(errorMessage: String, id: String, rowNumber: Int)

        fun onWhitelistClicked(element: WhitelistUiModel)

        fun onEmptyFeedButtonClicked()

        fun onGotoPlayStoreClicked()

        fun onGotoLearnMoreClicked(url: String): Boolean

        fun onAddToCartSuccess(productId: String)

        fun onAddToCartFailed(pdpAppLink: String)
    }

    interface Presenter : CustomerPresenter<View> {
        var cursor: String

        fun getFeedFirstPage(shopId: String, isPullToRefresh: Boolean, authorListEmpty: Boolean)

        fun getFeed(shopId: String)

        fun followKol(id: Int)

        fun unfollowKol(id: Int)

        fun likeKol(id: Long, rowNumber: Int, likeListener: KolPostLikeListener)

        fun unlikeKol(id: Long, rowNumber: Int, likeListener: KolPostLikeListener)

        fun deletePost(id: String, rowNumber: Int)

        fun trackPostClick(uniqueTrackingId: String, redirectLink: String)

        fun trackPostClickUrl(url:String)

        fun doTopAdsTracker(url: String, shopId: String, shopName: String, imageUrl: String, isClick: Boolean)

        fun addPostTagItemToCart(postTagItem: PostTagItem)

        fun clearCache()
    }
}
