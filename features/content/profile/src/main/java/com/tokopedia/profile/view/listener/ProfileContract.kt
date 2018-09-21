package com.tokopedia.profile.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.user.session.UserSession

/**
 * @author by milhamj on 9/17/18.
 */
interface ProfileContract {
    interface View : CustomerView {
        val userSession: UserSession

        fun goToFollowing(userId: Int)

        fun followUnfollowUser(userId: Int, follow: Boolean)
    }
    interface Presenter : CustomerPresenter<View> {

    }
}