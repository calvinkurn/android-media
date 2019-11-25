package com.tokopedia.feedplus.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by yoasfs on 2019-08-06
 */
interface DynamicFeedContract {
    interface View : BaseListViewListener<Visitable<*>> {
        fun onSuccessGetFeedFirstPage(element: List<Visitable<*>>, lastCursor: String)

        fun onSuccessGetFeed(visitables: List<Visitable<*>>, lastCursor: String)

        fun updateCursor(cursor: String)

        fun onSuccessDeletePost(rowNumber: Int)

        fun onEmptyFeedButtonClicked()

        fun onSuccessLike(rowNumber: Int, columnNumber: Int)

        fun onSuccessUnlike(rowNumber: Int, columnNumber: Int)

        fun onErrorLikeUnlike(err: String)
    }

    interface Presenter : CustomerPresenter<View> {
        var cursor: String

        fun getFeedFirstPage(isPullToRefresh: Boolean)

        fun getFeed()

        fun likeKol(id: Int, rowNumber: Int, columnNumber: Int)

        fun unlikeKol(id: Int, rowNumber: Int, columnNumber: Int)

        fun trackAffiliate(url: String)
    }
}