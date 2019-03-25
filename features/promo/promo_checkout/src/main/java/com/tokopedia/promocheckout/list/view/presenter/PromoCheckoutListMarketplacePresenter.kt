package com.tokopedia.promocheckout.list.view.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoFirstStepParam
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class PromoCheckoutListMarketplacePresenter(private val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase, val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper) : BaseDaggerPresenter<PromoCheckoutListMarketplaceContract.View>(), PromoCheckoutListMarketplaceContract.Presenter {

    override fun checkPromoStackingCode(promoCode: String, oneClickShipment: Boolean, checkPromoFirstStepParam: CheckPromoFirstStepParam?) {
        if (checkPromoFirstStepParam == null) return

        if (TextUtils.isEmpty(promoCode)) {
            view.onErrorEmptyPromoCode()
            return
        } else {
            // Clear all merchant promo
            checkPromoFirstStepParam.orders?.forEach { order ->
                order.codes = ArrayList()
            }
            // Set promo global
            val codes = ArrayList<String>()
            codes.add(promoCode)
            checkPromoFirstStepParam.codes = codes
        }
        view.showProgressLoading()

        checkPromoStackingCodeUseCase.setParams(checkPromoFirstStepParam)
        checkPromoStackingCodeUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onNext(t: GraphqlResponse?) {
                view.hideProgressLoading()

                val responseGetPromoStack = checkPromoStackingCodeMapper.call(t)
                if (responseGetPromoStack.data.message.state.mapToStatePromoStackingCheckout() == TickerPromoStackingCheckoutView.State.FAILED) {
                    view.onErrorCheckPromoCode(MessageErrorException(responseGetPromoStack.data.message.text))
                } else {
                    view.onSuccessCheckPromoStackingCode(responseGetPromoStack.data)
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorCheckPromoCode(e)
                }
            }

        })

        /*checkPromoCodeUseCase.execute(checkPromoCodeUseCase.createRequestParams(promoCode, oneClickShipment = oneClickShipment), object : Subscriber<DataVoucher>() {
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
        })*/
    }

    override fun detachView() {
        // checkPromoCodeUseCase.unsubscribe()
        checkPromoStackingCodeUseCase.unsubscribe()
        super.detachView()
    }
}
