package com.tokopedia.profile.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.profile.view.viewmodel.ProfileFirstPageViewModel

/**
 * @author by milhamj on 9/17/18.
 */
interface ProfileContract {
    interface View : BaseListViewListener<Visitable<*>> {
        fun onSuccessGetProfileFirstPage(profileFirstPageViewModel: ProfileFirstPageViewModel,
                                         cursor: String)

        fun goToFollowing()

        fun followUnfollowUser(userId: Int, follow: Boolean)

        fun goToProduct(productId: Int)

        fun addImages(productId: Int)

        fun updateCursor(cursor: String)
    }
    interface Presenter : CustomerPresenter<View> {
        var cursor: String

        fun getProfileFirstPage(userId: Int)

        fun getProfilePost(userId: Int)
    }
}