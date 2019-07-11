package com.tokopedia.promocheckout.detail.view.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.data.entity.request.CurrentApplyCode
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class PromoCheckoutDetailDigitalPresenter(private val getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                          private val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase,
                                          private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase) :
        BaseDaggerPresenter<PromoCheckoutDetailContract.View>(), PromoCheckoutDetailContract.Presenter {

    private val paramGlobal = "global"
    private val statusOK = "OK"

    override fun cancelPromo(codeCoupon: String) {
        view.showProgressLoading()
        val promoCodes = arrayListOf(codeCoupon)
        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, promoCodes)
        clearCacheAutoApplyStackUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorCancelPromo(e)
                }
            }

            override fun onNext(response: GraphqlResponse) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    val responseData = response.getData<ClearCacheAutoApplyStackResponse>(ClearCacheAutoApplyStackResponse::class.java)
                    if (responseData.successData.success) {
                        view.onSuccessCancelPromoStacking()
                    } else {
                        view.onErrorCancelPromo(RuntimeException())
                    }
                }
            }

        })

    }

    override fun validatePromoStackingUse(promoCode: String, promo: Promo?, isFromLoadDetail: Boolean) { }

    override fun getDetailPromo(codeCoupon: String, oneClickShipment: Boolean, promo: Promo?) {
        view.showLoading()
        getDetailCouponMarketplaceUseCase.execute(getDetailCouponMarketplaceUseCase.createRequestParams(codeCoupon, oneClickShipment = oneClickShipment),
                object : Subscriber<GraphqlResponse>() {

                    override fun onError(e: Throwable) {
                        if (isViewAttached) {
                            view.hideLoading()
                            view.onErroGetDetail(e)
                        }
                    }

                    override fun onCompleted() {

                    }

                    override fun onNext(response: GraphqlResponse?) {
                        view.hideLoading()
                        val dataDetailCheckoutPromo = response?.getData<DataPromoCheckoutDetail>(DataPromoCheckoutDetail::class.java)
                        view.onSuccessGetDetailPromo(dataDetailCheckoutPromo?.promoCheckoutDetailModel
                                ?: throw RuntimeException())
                    }
                })
    }

    override fun detachView() {
        getDetailCouponMarketplaceUseCase.unsubscribe()
        checkPromoStackingCodeUseCase.unsubscribe()
        clearCacheAutoApplyStackUseCase.unsubscribe()
        super.detachView()
    }
}