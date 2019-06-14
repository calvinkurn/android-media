package com.tokopedia.power_merchant.subscribe.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusUseCase
import com.tokopedia.power_merchant.subscribe.contract.PmTermsContract
import javax.inject.Inject

/**
 * @author by milhamj on 14/06/19.
 */
class PmTermsPresenter @Inject constructor(
        private val getPowerMerchantStatusUseCase: GetPowerMerchantStatusUseCase)
    : BaseDaggerPresenter<PmTermsContract.View>(), PmTermsContract.Presenter {

    override fun activatePowerMerchant() {

    }

    override fun autoExtendPowerMerchant() {
    }
}