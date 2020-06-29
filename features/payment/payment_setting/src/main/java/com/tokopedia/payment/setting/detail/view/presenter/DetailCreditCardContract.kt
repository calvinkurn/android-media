package com.tokopedia.payment.setting.detail.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface DetailCreditCardContract {
    interface View : CustomerView {
        fun onDeleteCCResult(success: Boolean?, message: String?, tokenId: String?)
        fun onErrorDeleteCC(e: Throwable, tokenId: String?)
        fun showProgressDialog()
        fun hideProgressDialog()

    }

    interface Presenter : CustomerPresenter<View> {
        fun deleteCreditCard(tokenId: String, resources: Resources?)
    }
}