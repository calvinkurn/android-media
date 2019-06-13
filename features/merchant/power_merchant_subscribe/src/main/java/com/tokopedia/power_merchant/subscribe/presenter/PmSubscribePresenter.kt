package com.tokopedia.power_merchant.subscribe.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.power_merchant.subscribe.contract.PmSubscribeContract
import javax.inject.Inject

class PmSubscribePresenter @Inject constructor(): BaseDaggerPresenter<PmSubscribeContract.View>(),PmSubscribeContract.View {


    override fun detachView() {
    }

}