package com.tokopedia.promocheckout.detail.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoFirstStepParam
import com.tokopedia.promocheckout.common.domain.CancelPromoUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.model.cancelpromo.ResponseCancelPromo
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.detail.domain.DetailCouponMarkeplaceModel
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.lang.reflect.Type

class PromoCheckoutDetailPresenter(private val getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                   private val checkPromoStackingUseCase: CheckPromoStackingCodeUseCase,
                                   val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper,
                                   private val cancelPromoUseCase: CancelPromoUseCase) :
        BaseDaggerPresenter<PromoCheckoutDetailContract.View>(), PromoCheckoutDetailContract.Presenter {

    override fun cancelPromo() {
        view.showProgressLoading()
        cancelPromoUseCase.execute(RequestParams.EMPTY, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorCancelPromo(e)
                }
            }

            override fun onNext(restResponse: Map<Type, RestResponse>) {
                view.hideProgressLoading()
                val responseCancel = restResponse.get(ResponseCancelPromo::class.java)
                val responseCancelPromo = responseCancel?.getData<ResponseCancelPromo>()
                var resultSuccess = responseCancelPromo?.data?.isSuccess ?: false

                if (resultSuccess) {
                    // view.onSuccessCancelPromo();
                    view.onSuccessCancelPromoStacking()
                } else {
                    view.onErrorCancelPromo(RuntimeException())
                }
            }
        })

    }

    override fun validatePromoStackingUse(checkPromoFirstStepParam: CheckPromoFirstStepParam?) {
        if (checkPromoFirstStepParam == null) return

        view.showProgressLoading()
        checkPromoStackingUseCase.setParams(checkPromoFirstStepParam)
        checkPromoStackingUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onNext(t: GraphqlResponse?) {
                view.hideProgressLoading()

                val responseGetPromoStack = checkPromoStackingCodeMapper.call(t)
                if (responseGetPromoStack.data.message?.state?.mapToStatePromoCheckout() == TickerCheckoutView.State.FAILED) {
                    view.onErrorValidatePromo(MessageErrorException(responseGetPromoStack.data.message?.text))
                } else {
                    view.onSuccessValidatePromoStacking(responseGetPromoStack.data)
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

    /*override fun validatePromoUse(codeCoupon: String, oneClickShipment: Boolean, resources: Resources) {
        checkPromoUseCase.execute(checkPromoUseCase.createRequestParams(codeCoupon, oneClickShipment = oneClickShipment), object : Subscriber<DataVoucher>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorValidatePromo(e)
                }
            }

            override fun onNext(dataVoucher: DataVoucher) {
                view.hideProgressLoading()
                if (dataVoucher.message?.state?.mapToStatePromoCheckout() == TickerCheckoutView.State.FAILED) {
                    view.onErrorValidatePromo(MessageErrorException(dataVoucher.message?.text))
                } else {
                    view.onSuccessValidatePromo(dataVoucher)
                }
            }
        })
    }*/

    override fun getDetailPromo(codeCoupon: String, oneClickShipment: Boolean) {
        view.showLoading()
        getDetailCouponMarketplaceUseCase.execute(getDetailCouponMarketplaceUseCase.createRequestParams(codeCoupon, oneClickShipment = oneClickShipment),
                object : Subscriber<DetailCouponMarkeplaceModel>() {

                    override fun onError(e: Throwable) {
                        if (isViewAttached) {
                            view.hideLoading()
                            view.onErroGetDetail(e)
                        }
                    }

                    override fun onCompleted() {

                    }

                    override fun onNext(t: DetailCouponMarkeplaceModel?) {
                        view.hideLoading()
                        view.onSuccessGetDetailPromo(t?.dataPromoCheckoutDetail?.promoCheckoutDetailModel
                                ?: throw RuntimeException())
                        if (t.dataVoucher?.message?.state?.mapToStatePromoCheckout() == TickerCheckoutView.State.FAILED) {
                            view.onErroGetDetail(CheckPromoCodeDetailException(t.dataVoucher?.message?.text
                                    ?: ""))
                        }
                    }
                })
    }

    override fun detachView() {
        getDetailCouponMarketplaceUseCase.unsubscribe()
        // checkPromoUseCase.unsubscribe()
        checkPromoStackingUseCase.unsubscribe()
        super.detachView()
    }
}