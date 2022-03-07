package com.tokopedia.promocheckout.detail.view.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.data.entity.request.CurrentApplyCode
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.promocheckout.detail.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import rx.subscriptions.CompositeSubscription

class PromoCheckoutDetailPresenter(private val getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                   private val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase) :
        BaseDaggerPresenter<PromoCheckoutDetailContract.View>(), PromoCheckoutDetailContract.Presenter {

    private val paramGlobal = "global"
    private val statusOK = "OK"

    val compositeSubscription = CompositeSubscription()

    @Deprecated("Marketplace promo detail page cannot cancel promo")
    override fun cancelPromo(codeCoupon: String) {
        /* no-op */
    }

    @Deprecated("Marketplace promo detail page cannot apply promo")
    override fun validatePromoStackingUse(promoCode: String, promo: Promo?, isFromLoadDetail: Boolean) {
        if (promo == null) return

        if (TextUtils.isEmpty(promoCode)) return

        // Clear all merchant promo
        promo.orders?.forEach { order ->
            order.codes = ArrayList()
        }
        // Set promo global
        promo.codes = arrayListOf(promoCode)

        var currentApplyCode: CurrentApplyCode? = null
        if (promoCode.isNotEmpty()) {
            currentApplyCode = CurrentApplyCode(
                    promoCode,
                    paramGlobal
            )
        }
        promo.currentApplyCode = currentApplyCode

        if (isFromLoadDetail) {
            promo.skipApply = 1
        } else {
            promo.skipApply = 0
        }

        view.showProgressLoading()
        checkPromoStackingCodeUseCase.setParams(promo)
        compositeSubscription.add(
                checkPromoStackingCodeUseCase.createObservable(RequestParams.create())
                        .subscribe(object : Subscriber<ResponseGetPromoStackUiModel>() {
                            override fun onNext(responseGetPromoStack: ResponseGetPromoStackUiModel) {
                                if (isViewAttached) {
                                    view.hideProgressLoading()
                                    if (responseGetPromoStack.status.equals(statusOK, true) && responseGetPromoStack.data.success) {
                                        if (!isFromLoadDetail) {
                                            if (promo.skipApply == 0 && responseGetPromoStack.data.clashings.isClashedPromos) {
                                                view.onClashCheckPromo(responseGetPromoStack.data.clashings)
                                            } else {
                                                responseGetPromoStack.data.codes.forEach {
                                                    if (it.equals(promoCode, true)) {
                                                        if (responseGetPromoStack.data.message.state.mapToStatePromoStackingCheckout() == TickerPromoStackingCheckoutView.State.FAILED) {
                                                            view?.hideProgressLoading()
                                                            view.onErrorCheckPromoStacking(MessageErrorException(responseGetPromoStack.data.message.text))
                                                        } else {
                                                            view.onSuccessCheckPromo(responseGetPromoStack.data)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        val message = responseGetPromoStack.data.message.text
                                        view.onErrorCheckPromoStacking(MessageErrorException(message))
                                    }
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
        )
    }

    override fun getDetailPromo(codeCoupon: String, oneClickShipment: Boolean) {
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
        compositeSubscription.unsubscribe()
        super.detachView()
    }
}