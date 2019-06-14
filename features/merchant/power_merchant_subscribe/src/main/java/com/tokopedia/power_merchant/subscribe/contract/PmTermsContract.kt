package com.tokopedia.power_merchant.subscribe.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by milhamj on 14/06/19.
 */
interface PmTermsContract {
    interface View : CustomerView {
        fun showLoading()
        fun hideLoading()
        fun onSuccessActivate(success: Boolean)
        fun onErrorActivate(throwable: Throwable)
    }

    interface Presenter : CustomerPresenter<View> {
        fun activatePowerMerchant()
    }
}