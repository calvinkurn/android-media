package com.tokopedia.profile.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by milhamj on 9/17/18.
 */
interface ProfileContract {
    interface View : CustomerView {

    }
    interface Presenter : CustomerPresenter<CustomerView> {

    }
}