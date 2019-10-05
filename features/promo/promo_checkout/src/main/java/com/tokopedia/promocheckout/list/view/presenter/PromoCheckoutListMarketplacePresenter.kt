package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import android.text.TextUtils
import android.util.Log
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.data.entity.request.CurrentApplyCode
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.promocheckout.list.model.listpromocatalog.ResponseExchangeCoupon
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class PromoCheckoutListMarketplacePresenter(private val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase, val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper) : BaseDaggerPresenter<PromoCheckoutListMarketplaceContract.View>(), PromoCheckoutListMarketplaceContract.Presenter {

    private val paramGlobal = "global"
    private val statusOK = "OK"

    override fun checkPromoStackingCode(promoCode: String, oneClickShipment: Boolean, promo: Promo?) {
        if (promo == null) return

        if (TextUtils.isEmpty(promoCode)) {
            view.onErrorEmptyPromo()
            return
        } else {
            // Clear all merchant promo
            promo.orders?.forEach { order ->
                order.codes = ArrayList()
            }
            // Set promo global
            val codes = ArrayList<String>()
            codes.add(promoCode)
            promo.codes = codes

            var currentApplyCode: CurrentApplyCode? = null
            if (promoCode.isNotEmpty()) {
                currentApplyCode = CurrentApplyCode(
                        promoCode,
                        paramGlobal
                )
            }
            promo.currentApplyCode = currentApplyCode
        }
        view.showProgressLoading()

        checkPromoStackingCodeUseCase.setParams(promo)
        checkPromoStackingCodeUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onNext(t: GraphqlResponse?) {
                view.hideProgressLoading()

                val responseGetPromoStack = checkPromoStackingCodeMapper.call(t)
                if (responseGetPromoStack.status.equals(statusOK, true)) {
                    if (responseGetPromoStack.data.clashings.isClashedPromos) {
                        view?.onClashCheckPromo(responseGetPromoStack.data.clashings)
                    } else {
                        responseGetPromoStack.data.codes.forEach {
                            if (it.equals(promoCode, true)) {
                                if (responseGetPromoStack.data.message.state.mapToStatePromoStackingCheckout() == TickerPromoStackingCheckoutView.State.FAILED) {
                                    view?.hideProgressLoading()
                                    view.onErrorCheckPromo(MessageErrorException(responseGetPromoStack.data.message.text))
                                } else {
                                    view.onSuccessCheckPromo(responseGetPromoStack.data)
                                }
                            }
                        }
                    }
                } else {
                    val message = responseGetPromoStack.data.message.text
                    view.onErrorCheckPromo(MessageErrorException(message))
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorCheckPromo(e)
                }
            }

        })

    }

    override fun getListExchangeCoupon(resources: Resources) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.promo_checkout_exchange_coupon), ResponseExchangeCoupon::class.java, null, false)
        val getListCouponUseCase=GraphqlUseCase()
        getListCouponUseCase.clearRequest()
        getListCouponUseCase.addRequest(graphqlRequest)
        getListCouponUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.showGetListLastSeenError(e)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                val dataExchangeCoupon = objects.getData<ResponseExchangeCoupon>(ResponseExchangeCoupon::class.java)
                view.renderListExchangeCoupon((dataExchangeCoupon.tokopointsCatalogHighlight!!))
            }
        })
    }


    override fun detachView() {
        checkPromoStackingCodeUseCase.unsubscribe()
        super.detachView()
    }
}
