package com.tokopedia.payment.setting.list.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel

interface SettingListPaymentContract {
    interface View : BaseListViewListener<SettingListPaymentModel>{
        fun showLoadingDialog()
        fun hideLoadingDialog()
        fun onSuccessVerifPhone()
        fun onNeedVerifPhone()

    }

    interface Presenter : CustomerPresenter<View>{
        fun getCreditCardList(resources : Resources)
        fun checkVerificationPhone()
    }
}