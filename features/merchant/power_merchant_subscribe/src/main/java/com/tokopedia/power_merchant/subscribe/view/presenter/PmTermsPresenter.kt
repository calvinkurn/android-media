package com.tokopedia.power_merchant.subscribe.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.gm.common.domain.interactor.ActivatePowerMerchantUseCase
import com.tokopedia.power_merchant.subscribe.view.contract.PmTermsContract
import rx.Subscriber
import timber.log.Timber
import javax.inject.Inject

/**
 * @author by milhamj on 14/06/19.
 */
class PmTermsPresenter @Inject constructor(
        private val activatePowerMerchantUseCase: ActivatePowerMerchantUseCase)
    : BaseDaggerPresenter<PmTermsContract.View>(), PmTermsContract.Presenter {

    override fun activatePowerMerchant() {
        view.showLoading()
        activatePowerMerchantUseCase.execute(object : Subscriber<Boolean>() {
            override fun onNext(isSuccess: Boolean) {
                if (isSuccess) {
                    view.hideLoading()
                    view.onSuccessActivate()
                } else {
                    onError(RuntimeException())
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                Timber.d(e)
                if (isViewNotAttached) {
                    return
                }
                view.hideLoading()
                view.onError(e)
            }
        })
    }

    override fun detachView() {
        super.detachView()
        activatePowerMerchantUseCase.unsubscribe()
    }
}