package com.tokopedia.promocheckout.list.view.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import rx.Subscriber

class PromoCheckoutListMarketplacePresenter(val checkPromoCodeUseCase: CheckPromoCodeUseCase): BaseDaggerPresenter<PromoCheckoutListMarketplaceContract.View>(), PromoCheckoutListMarketplaceContract.Presenter  {
    override fun checkPromoCode(promoCode: String, oneClickShipment: Boolean) {
        if(TextUtils.isEmpty(promoCode)){
            view.onErrorEmptyPromoCode()
            return
        }
        view.showProgressLoading()
        checkPromoCodeUseCase.execute(checkPromoCodeUseCase.createRequestParams(promoCode, oneClickShipment = oneClickShipment), object : Subscriber<DataVoucher>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorCheckPromoCode(e)
                }
            }

            override fun onNext(dataVoucher: DataVoucher) {
                view.hideProgressLoading()
                if(dataVoucher.message?.state?.mapToStatePromoCheckout() == TickerCheckoutView.State.FAILED){
                    view.onErrorCheckPromoCode(MessageErrorException(dataVoucher.message?.text))
                }else{
                    view.onSuccessCheckPromoCode(dataVoucher)
                }
            }
        })
    }

    override fun detachView() {
        checkPromoCodeUseCase.unsubscribe()
        super.detachView()
    }
}
