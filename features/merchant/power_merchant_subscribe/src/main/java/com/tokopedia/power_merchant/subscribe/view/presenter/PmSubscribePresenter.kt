package com.tokopedia.power_merchant.subscribe.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.gm.common.domain.interactor.DeactivatePowerMerchantUseCase
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusUseCase
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import com.tokopedia.power_merchant.subscribe.view.subscriber.GetInfoToggleAutoExtendSubscriber
import com.tokopedia.power_merchant.subscribe.view.subscriber.GetPmStatusInfoSubscriber
import javax.inject.Inject

class PmSubscribePresenter @Inject constructor(
        private val getPowerMerchantStatusUseCase: GetPowerMerchantStatusUseCase,
        private val deactivationPowerMerchant: DeactivatePowerMerchantUseCase)
    : BaseDaggerPresenter<PmSubscribeContract.View>(), PmSubscribeContract.Presenter {

    override fun setAutoExtendOff() {
        deactivationPowerMerchant.execute(GetInfoToggleAutoExtendSubscriber(view))
    }

    override fun getPmStatusInfo(shopId: String){
        getPowerMerchantStatusUseCase.execute(GetPowerMerchantStatusUseCase.createRequestParams(shopId),
                GetPmStatusInfoSubscriber(view))
    }

    override fun detachView() {
        getPowerMerchantStatusUseCase.unsubscribe()
        deactivationPowerMerchant.unsubscribe()
    }
}