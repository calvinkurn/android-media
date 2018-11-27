package com.tokopedia.payment.setting.add.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.payment.setting.add.model.Data

interface AddCreditCardContract {

    interface View : CustomerView{
        fun showProgressDialog()
        fun hideProgressDialog()
        fun onSuccessGetIFrameData(data: Data?)
        fun onErrorGetIframeData(e: Throwable)
        fun onErrorGetIframeData(e: String?)
    }

    interface Presenter : CustomerPresenter<View>{
        fun getIframeAddCC()
    }

}
