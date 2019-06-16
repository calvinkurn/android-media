package com.tokopedia.power_merchant.subscribe.view.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus

interface PmSubscribeContract {
    interface View : CustomerView {
        fun onSuccessGetPmInfo(powerMerchantStatus: PowerMerchantStatus)
        fun onErrorGetPmInfo(throwable: Throwable)
        fun showEmptyState(throwable: Throwable)
        fun refreshData()
        fun cancelMembership()
        fun onErrorCancelMembership(throwable: Throwable)

    }

    interface Presenter : CustomerPresenter<View> {
        fun getPmStatusInfo(shopId: String)
        fun setAutoExtendOff(autoExtend:Boolean)

    }
}