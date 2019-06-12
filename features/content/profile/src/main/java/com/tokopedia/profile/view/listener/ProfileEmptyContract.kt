package com.tokopedia.profile.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.profile.ProfileModuleRouter
import com.tokopedia.user.session.UserSession

/**
 * @author by milhamj on 22/10/18.
 */

interface ProfileEmptyContract {
    interface View : BaseListViewListener<Visitable<*>> {
        var profileRouter: ProfileModuleRouter

        fun getUserSession(): UserSession

        fun onChangeAvatarClicked()

        fun goToFollowing()
    }
    interface Presenter : CustomerPresenter<View> {
        fun getProfileHeader(userId: Int)
    }
}
