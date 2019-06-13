package com.tokopedia.power_merchant.subscribe.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.power_merchant.subscribe.contract.PmSubscribeContract
import com.tokopedia.power_merchant.subscribe.subscriber.GetPmInfoSubscriber
import javax.inject.Inject

class PmSubscribePresenter @Inject constructor(private val getShopStatusUseCase: GetShopStatusUseCase)
    : BaseDaggerPresenter<PmSubscribeContract.View>(), PmSubscribeContract.Presenter {

    override fun getPmInfo(shopId: String) {
        getShopStatusUseCase.execute(GetShopStatusUseCase.createRequestParams(shopId)
                ,GetPmInfoSubscriber(view))
    }


    override fun detachView() {

    }

    override fun getScoreInfo() {

    }

    override fun activatePowerMerchant() {

    }

}