package com.tokopedia.profile.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.profile.domain.usecase.GetProfileFirstPage
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.subscriber.GetProfileFirstPageSubscriber
import javax.inject.Inject

/**
 * @author by milhamj on 9/21/18.
 */
class ProfilePresenter @Inject constructor(val getProfileFirstPage: GetProfileFirstPage)
    : BaseDaggerPresenter<ProfileContract.View>(), ProfileContract.Presenter {

    override var cursor: String = ""

    override fun detachView() {
        super.detachView()
        getProfileFirstPage.unsubscribe()
    }

    override fun getProfileFirstPage(userId: String) {
        getProfileFirstPage.execute(GetProfileFirstPageSubscriber(view))
    }

    override fun getProfilePost(userId: String) {

    }
}