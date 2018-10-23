package com.tokopedia.profile.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.profile.view.viewmodel.ProfileFirstPageViewModel
import com.tokopedia.user.session.UserSession

/**
 * @author by milhamj on 9/17/18.
 */
interface ProfileContract {
    interface View : BaseListViewListener<Visitable<*>> {
        fun getUserSession(): UserSession

        fun onSuccessGetProfileFirstPage(firstPageViewModel: ProfileFirstPageViewModel)

        fun onSuccessGetProfilePost(visitables: List<Visitable<*>>, lastCursor: String)

        fun goToFollowing()

        fun followUnfollowUser(userId: Int, follow: Boolean)

        fun addImages(productId: Int)

        fun updateCursor(cursor: String)

        fun onSuccessFollowKol()

        fun onErrorFollowKol(errorMessage: String)

        fun onSuccessDeletePost(rowNumber: Int)

        fun onErrorDeletePost(errorMessage: String, id: Int, rowNumber: Int)

        fun onChangeAvatarClicked()

        fun onSuccessTrackPostClick(redirectLink: String)

        fun onErrorTrackPostClick(errorMessage: String, uniqueTrackingId: String,
                                  redirectLink: String)

        fun showLoadingLayout()

        fun hideLoadingLayout()
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

        fun trackPostClick(uniqueTrackingId: String, redirectLink: String)
    }
}