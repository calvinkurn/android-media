package com.tokopedia.kol.feature.video.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by yfsx on 25/03/19.
 */
interface VideoDetailContract {
    interface View : CustomerView {

        val androidContext: Context

        var userSession: UserSessionInterface

        fun showLoading()

        fun hideLoading()

        fun onSuccessFollowKol()

        fun onErrorFollowKol(error: String)

        fun onErrorGetVideoDetail(error: String)

        fun onSuccessGetVideoDetail(visitables: List<Visitable<*>>)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getFeedDetail(detailId: String)

        fun followKol(id : Int)

        fun unFollowKol(id: Int)

        fun likeKol(id: Long, rowNumber: Int, likeListener: KolPostLikeListener)

        fun unlikeKol(id: Long, rowNumber: Int, likeListener: KolPostLikeListener)
    }
}
