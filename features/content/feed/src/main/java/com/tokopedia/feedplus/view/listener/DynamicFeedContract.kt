package com.tokopedia.feedplus.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.user.session.UserSession

/**
 * @author by yoasfs on 2019-08-06
 */
interface DynamicFeedContract {
    interface View : BaseListViewListener<Visitable<*>> {
        fun onSuccessGetFeedFirstPage(element: List<Visitable<*>>, lastCursor: String)

        fun onSuccessGetFeed(visitables: List<Visitable<*>>, lastCursor: String)

        fun updateCursor(cursor: String)

        fun onSuccessDeletePost(rowNumber: Int)

        fun getUserSession(): UserSession

        fun onEmptyFeedButtonClicked()
    }

    interface Presenter : CustomerPresenter<View> {
        var cursor: String

        fun getFeedFirstPage(isPullToRefresh: Boolean)

        fun getFeed()

        fun likeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like)

        fun unlikeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like)

        fun trackPostClick(uniqueTrackingId: String, redirectLink: String)

        fun trackPostClickUrl(url:String)
    }
}