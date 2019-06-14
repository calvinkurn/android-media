package com.tokopedia.power_merchant.subscribe.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusUseCase
import com.tokopedia.power_merchant.subscribe.contract.PmSubscribeContract
import com.tokopedia.power_merchant.subscribe.subscriber.GetPmStatusInfoSubscriber
import javax.inject.Inject

class PmSubscribePresenter @Inject constructor(
        private val getPowerMerchantStatusUseCase: GetPowerMerchantStatusUseCase)
    : BaseDaggerPresenter<PmSubscribeContract.View>(), PmSubscribeContract.Presenter {

    override fun getPmInfo(shopId: String) {
//        getShopStatusUseCase.execute(GetShopStatusUseCase.createRequestParams(shopId)
//                ,GetPmInfoSubscriber(view))
    }

    override fun getPmStatusInfo(shopId: String){
        getPowerMerchantStatusUseCase.execute(GetPowerMerchantStatusUseCase.createRequestParams(shopId),GetPmStatusInfoSubscriber(view))
    }


    override fun detachView() {

    }

    override fun getScoreInfo() {

    }

    override fun activatePowerMerchant() {

    }

}