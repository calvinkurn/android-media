package com.tokopedia.power_merchant.subscribe.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

class PmSubscribeContract {

    interface View : CustomerView {

    }

    interface Presenter : CustomerPresenter<View> {

    }
}