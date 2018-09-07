package com.tokopedia.interestpick.view

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by milhamj on 07/09/18.
 */
interface InterestPickContract {
    interface View : CustomerView {

    }

    interface Presenter : CustomerPresenter<View> {

    }
}