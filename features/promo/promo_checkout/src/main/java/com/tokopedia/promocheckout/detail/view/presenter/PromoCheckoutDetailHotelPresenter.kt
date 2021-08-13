package com.tokopedia.promocheckout.detail.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase
import com.tokopedia.promocheckout.common.domain.hotel.HotelCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.HotelCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.promocheckout.common.domain.model.HotelCheckVoucher
import com.tokopedia.promocheckout.detail.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import rx.Subscriber

class PromoCheckoutDetailHotelPresenter(private val getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                        private val checkVoucherUseCase: HotelCheckVoucherUseCase,
                                        val checkVoucherMapper: HotelCheckVoucherMapper,
                                        private val cancelVoucherUseCase: FlightCancelVoucherUseCase) :
        BaseDaggerPresenter<PromoCheckoutDetailContract.View>(), PromoCheckoutDetailHotelContract.Presenter {

    override fun checkVoucher(promoCode: String, cartID: String, hexColor: String) {
        view.showProgressLoading()
        checkVoucherUseCase.execute(checkVoucherUseCase.createRequestParams(promoCode, cartID), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()
                val checkVoucherData = objects.getData<HotelCheckVoucher.Response>(HotelCheckVoucher.Response::class.java).response
                checkVoucherData.messageColor = hexColor
                if (checkVoucherData.isSuccess) {
                    view.onSuccessCheckPromo(checkVoucherMapper.mapData(checkVoucherData))
                } else {
                    view.onErrorCheckPromo(MessageErrorException(checkVoucherData.errorMessage))
                }
            }

            override fun onCompleted() {}

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
                        view.onSuccessGetDetailPromo(dataDetailCheckoutPromo?.promoCheckoutDetailModel)
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
}