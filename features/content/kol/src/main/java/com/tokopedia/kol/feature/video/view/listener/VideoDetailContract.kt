package com.tokopedia.kol.feature.video.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.user.session.UserSession

/**
 * @author by yfsx on 25/03/19.
 */
interface VideoDetailContract {
    interface View : CustomerView {
        fun showLoading()

        fun hideLoading()

        fun onSuccessFollowKol()

        fun onErrorFollowKol(error: String)

        fun getContext() : Context

        fun getUserSession() :UserSession

        fun onErrorGetVideoDetail(error: String)

        fun onSuccessGetVideoDetail(visitables: List<Visitable<*>>)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getFeedDetail(detailId: String)

        fun followKol(id : Int)

        fun unFollowKol(id: Int)

        fun likeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like)

        fun unlikeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like)
    }
}