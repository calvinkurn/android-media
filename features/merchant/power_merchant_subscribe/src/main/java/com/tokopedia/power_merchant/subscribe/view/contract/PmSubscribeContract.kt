package com.tokopedia.power_merchant.subscribe.view.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus

interface PmSubscribeContract {
    interface View : CustomerView {
        fun onSuccessGetPmInfo(powerMerchantStatus: PowerMerchantStatus)
        fun showEmptyState(throwable: Throwable)
        fun refreshData()
        fun cancelMembership()
        fun onSuccessCancelMembership()
        fun onErrorCancelMembership(throwable: Throwable)
        fun hideLoading()

    }

    interface Presenter : CustomerPresenter<View> {
        fun getPmStatusInfo(shopId: String)
        fun setAutoExtendOff(autoExtend:Boolean)

    }
}