package com.tokopedia.promocheckout.detail.view.presenter

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.FlightCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.promocheckout.common.domain.model.FlightCheckVoucher
import com.tokopedia.promocheckout.detail.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import rx.Subscriber

class PromoCheckoutDetailFlightPresenter(private val getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                         private val checkVoucherUseCase: FlightCheckVoucherUseCase,
                                         val checkVoucherMapper: FlightCheckVoucherMapper,
                                         private val cancelVoucherUseCase: FlightCancelVoucherUseCase) :
        BaseDaggerPresenter<PromoCheckoutDetailContract.View>(), PromoCheckoutDetailFlightContract.Presenter {

    override fun checkVoucher(promoCode: String, cartID: String, hexColor: String) {
        view.showProgressLoading()
        checkVoucherUseCase.execute(checkVoucherUseCase.createRequestParams(promoCode, cartID), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()
                val errors = objects.getError(FlightCheckVoucher.Response::class.java)
                if (!errors.isNullOrEmpty()) {
                    val rawErrorMessage = errors[0].message
                    val errorMessage = Gson().fromJson(rawErrorMessage.substring(1, rawErrorMessage.length - 1), FlightCheckVoucherError::class.java)
                    throw MessageErrorException(errorMessage.title)
                } else {
                    val checkVoucherData = objects.getData<FlightCheckVoucher.Response>(FlightCheckVoucher.Response::class.java).response
                    checkVoucherData.messageColor = hexColor
                    view.onSuccessCheckPromo(checkVoucherMapper.mapData(checkVoucherData))
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorCheckPromoStacking(e)
                }
            }

        })

    }

    override fun getDetailPromo(codeCoupon: String) {
        view.showLoading()
        getDetailCouponMarketplaceUseCase.execute(getDetailCouponMarketplaceUseCase.createRequestParams(codeCoupon),
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

    override fun cancelPromo() {
        view.showLoading()
        cancelVoucherUseCase.execute(object : Subscriber<GraphqlResponse>() {

            override fun onNext(response: GraphqlResponse) {
                view.hideLoading()
                val cancelPromoResponse = response.getData<FlightCancelVoucher.Response>(FlightCancelVoucher.Response::class.java).response
                if (cancelPromoResponse.attributes.success) {
                    view.onSuccessCancelPromo()
                } else {
                    view.onErrorCancelPromo(Throwable("Promo tidak berhasil dilepas"))
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideLoading()
                    view.onErrorCancelPromo(e)
                }
            }
        })
    }

    override fun detachView() {
        getDetailCouponMarketplaceUseCase.unsubscribe()
        super.detachView()
    }

    companion object {
        data class FlightCheckVoucherError(val id: String, val status: String, val title: String)
    }
}