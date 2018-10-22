package com.tokopedia.profile.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.profile.view.viewmodel.ProfileFirstPageViewModel
import com.tokopedia.user.session.UserSession

/**
 * @author by milhamj on 22/10/18.
 */

interface ProfileEmptyContract {
    interface View : BaseListViewListener<Visitable<*>> {
        fun getUserSession(): UserSession

        fun onChangeAvatarClicked()

        fun goToFollowing()

        fun followUnfollowUser(userId: Int, follow: Boolean)
    }
    interface Presenter : CustomerPresenter<View> {
        fun getProfileHeader(userId: Int)
    }
}
