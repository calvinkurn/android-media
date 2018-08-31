package com.tokopedia.payment.setting.list

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface SettingListPaymentContract {
    interface View : BaseListViewListener<SettingListPaymentModel>{

    }

    interface Presenter : CustomerPresenter<View>{
        fun getCreditCardList()
    }
}