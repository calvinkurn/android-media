package com.tokopedia.power_merchant.subscribe.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel

class PmSubscribeContract {

    interface View : CustomerView {
        fun onSuccessGetPmInfo(powerMerchantStatus: PowerMerchantStatus)
        fun onErrorGetPmInfo(throwable: Throwable)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getPmStatusInfo(shopId: String)
        fun getPmInfo(shopId:String)
        fun getScoreInfo()
        fun activatePowerMerchant()
    }
}