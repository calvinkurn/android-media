package com.tokopedia.profile.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.profile.view.listener.ProfileContract
import javax.inject.Inject

/**
 * @author by milhamj on 9/21/18.
 */
class ProfilePresenter @Inject constructor()
    : BaseDaggerPresenter<ProfileContract.View>(), ProfileContract.Presenter {
    override fun detachView() {
        super.detachView()
    }

}