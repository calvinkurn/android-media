package com.tokopedia.power_merchant.subscribe.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantActivationResult
import com.tokopedia.gm.common.domain.interactor.ActivatePowerMerchantUseCase
import com.tokopedia.gm.common.domain.interactor.ToggleAutoExtendPowerMerchantUseCase
import com.tokopedia.power_merchant.subscribe.view.contract.PmTermsContract
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by milhamj on 14/06/19.
 */
class PmTermsPresenter @Inject constructor(
        private val activatePowerMerchantUseCase: ActivatePowerMerchantUseCase,
        private val toggleAutoExtendUseCase: ToggleAutoExtendPowerMerchantUseCase)
    : BaseDaggerPresenter<PmTermsContract.View>(), PmTermsContract.Presenter {

    override fun activatePowerMerchant() {
        activatePowerMerchantUseCase.execute(object : Subscriber<PowerMerchantActivationResult>() {
            override fun onNext(t: PowerMerchantActivationResult?) {
                view.onSuccessActivate()
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                if (isViewNotAttached || e == null) {
                    return
                }
                view.onError(e)
            }
        })
    }

    override fun autoExtendPowerMerchant() {
        toggleAutoExtendUseCase.execute(
                ToggleAutoExtendPowerMerchantUseCase.createRequestParams(true),
                object : Subscriber<PowerMerchantActivationResult>() {
                    override fun onNext(t: PowerMerchantActivationResult?) {
                        view.onSuccessAutoExtend()
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (isViewNotAttached || e == null) {
                            return
                        }
                        view.onError(e)
                    }
                })
    }

    override fun detachView() {
        super.detachView()
        toggleAutoExtendUseCase.unsubscribe()
        activatePowerMerchantUseCase.unsubscribe()
    }
}