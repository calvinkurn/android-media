package com.tokopedia.profile.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.profile.view.viewmodel.ProfileFirstPageViewModel
import com.tokopedia.user.session.UserSession

/**
 * @author by milhamj on 9/17/18.
 */
interface ProfileContract {
    interface View : CustomerView {
        val userSession: UserSession

        fun onSuccessGetProfileFirstPage(profileFirstPageViewModel: ProfileFirstPageViewModel,
                                         cursor: String)

        fun goToFollowing(userId: Int)

        fun followUnfollowUser(userId: Int, follow: Boolean)

        fun goToProduct(productId: Int)

        fun addImages(productId: Int)

        fun updateCursor(cursor: String)
    }
    interface Presenter : CustomerPresenter<View> {
        var cursor: String

        fun getProfileFirstPage(userId: String)

        fun getProfilePost(userId: String)
    }
}