package com.tokopedia.expresscheckout.view.variant

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface CheckoutVariantContract {

    interface View : CustomerView {

        fun showData()

        fun showToasterError(message: String)

        fun showNetworkError()

    }

    interface Presenter : CustomerPresenter<View> {

        fun loadData()

    }

}