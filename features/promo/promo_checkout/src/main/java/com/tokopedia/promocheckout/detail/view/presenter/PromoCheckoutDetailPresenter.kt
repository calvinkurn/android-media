package com.tokopedia.promocheckout.detail.view.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class PromoCheckoutDetailPresenter(private val getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                   private val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase,
                                   private val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper,
                                   private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase) :
        BaseDaggerPresenter<PromoCheckoutDetailContract.View>(), PromoCheckoutDetailContract.Presenter {

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

    override fun validatePromoStackingUse(promoCode: String, promo: Promo?, isFromLoadDetail: Boolean) {
        if (promo == null) return

        if (TextUtils.isEmpty(promoCode)) return

        // Clear all merchant promo
        promo.orders?.forEach { order ->
            order.codes = ArrayList()
        }
        // Set promo global
        val codes = ArrayList<String>()
        codes.add(promoCode)
        promo.codes = codes

        if (isFromLoadDetail) {
            promo.skipApply = 1
        } else {
            promo.skipApply = 0
        }

        view.showProgressLoading()
        checkPromoStackingCodeUseCase.setParams(promo)
        checkPromoStackingCodeUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onNext(t: GraphqlResponse?) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    val responseGetPromoStack = checkPromoStackingCodeMapper.call(t)
                    if (responseGetPromoStack.data.message.state.mapToStatePromoStackingCheckout() == TickerCheckoutView.State.FAILED) {
                        if (!isFromLoadDetail) {
                            view.onErrorValidatePromo(MessageErrorException(responseGetPromoStack.data.message.text))
                        }
                    } else {
                        if (!isFromLoadDetail) {
                            if (promo.skipApply == 0 && responseGetPromoStack.data.clashings.isClashedPromos) {
                                view.onClashCheckPromo(responseGetPromoStack.data.clashings)
                            } else {
                                view.onSuccessValidatePromoStacking(responseGetPromoStack.data)
                            }
                        } else {
                            view.onErroGetDetail(CheckPromoCodeDetailException(responseGetPromoStack.data.message.text))
                        }
                    }
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorValidatePromo(e)
                }
            }

        })
    }

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

                        validatePromoStackingUse(codeCoupon, promo, true)
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