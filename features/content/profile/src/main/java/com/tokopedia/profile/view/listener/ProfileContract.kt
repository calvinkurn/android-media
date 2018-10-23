package com.tokopedia.profile.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.profile.view.viewmodel.ProfileFirstPageViewModel

/**
 * @author by milhamj on 9/17/18.
 */
interface ProfileContract {
    interface View : BaseListViewListener<Visitable<*>> {
        fun onSuccessGetProfileFirstPage(firstPageViewModel: ProfileFirstPageViewModel)

        fun onSuccessGetProfilePost(visitables: List<Visitable<*>>, lastCursor: String)

        fun goToFollowing()

        fun followUnfollowUser(userId: Int, follow: Boolean)

        fun goToProduct(productId: Int)

        fun addImages(productId: Int)

        fun updateCursor(cursor: String)

        fun onSuccessFollowKol()

        fun onErrorFollowKol(errorMessage: String)
    }
    interface Presenter : CustomerPresenter<View> {
        var cursor: String

        fun getProfileFirstPage(userId: Int)

        fun getProfilePost(userId: Int)

        fun followKol(id: Int)

        fun unfollowKol(id: Int)

        fun likeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like)

        fun unlikeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like)

        fun deletePost(id: Int, rowNumber: Int)
    }
}