package com.tokopedia.shop.feed.view.contract

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.shop.feed.domain.WhitelistDomain
import com.tokopedia.shop.feed.view.model.WhitelistViewModel
import com.tokopedia.user.session.UserSession

/**
 * @author by yfsx on 08/05/19.
 */
interface FeedShopContract {
    interface View : BaseListViewListener<Visitable<*>> {
        fun onSuccessGetFeedFirstPage(element: List<Visitable<*>>, lastCursor: String, whitelistDomain: WhitelistDomain)

        fun onSuccessGetFeedNotLoginFirstPage(element: List<Visitable<*>>, lastCursor: String)

        fun onSuccessGetFeed(visitables: List<Visitable<*>>, lastCursor: String)

        fun updateCursor(cursor: String)

        fun onSuccessFollowKol()

        fun onErrorFollowKol(errorMessage: String)

        fun onSuccessDeletePost(rowNumber: Int)

        fun onErrorDeletePost(errorMessage: String, id: Int, rowNumber: Int)

        fun getUserSession(): UserSession

        fun onWhitelistClicked(element: WhitelistViewModel)

        fun onEmptyFeedButtonClicked()
    }

    interface Presenter : CustomerPresenter<View> {
        var cursor: String

        fun getFeedFirstPage(shopId: String, isPullToRefresh: Boolean)

        fun getFeed(shopId: String)

        fun followKol(id: Int)

        fun unfollowKol(id: Int)

        fun likeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like)

        fun unlikeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like)

        fun deletePost(id: Int, rowNumber: Int)

        fun trackPostClick(uniqueTrackingId: String, redirectLink: String)

        fun trackPostClickUrl(url:String)
    }
}