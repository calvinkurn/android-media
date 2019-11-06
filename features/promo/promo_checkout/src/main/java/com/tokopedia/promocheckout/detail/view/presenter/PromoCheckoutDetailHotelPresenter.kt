package com.tokopedia.promocheckout.detail.view.presenter

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.hotel.HotelCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.HotelCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.HotelCheckVoucher
import com.tokopedia.promocheckout.detail.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import rx.Subscriber

class PromoCheckoutDetailHotelPresenter(private val getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                        private val checkVoucherUseCase: HotelCheckVoucherUseCase,
                                        val checkVoucherMapper: HotelCheckVoucherMapper) :
        BaseDaggerPresenter<PromoCheckoutDetailContract.View>(), PromoCheckoutDetailHotelContract.Presenter {

    override fun checkVoucher(promoCode: String, cartID: String) {
        view.showProgressLoading()
        checkVoucherUseCase.execute(checkVoucherUseCase.createRequestParams(promoCode, cartID), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()
                val errors = objects.getError(HotelCheckVoucher.Response::class.java)
                if (!errors.isNullOrEmpty()) {
                    val rawErrorMessage = errors[0].message
                    val errorMessage = Gson().fromJson(rawErrorMessage.substring(1, rawErrorMessage.length - 1), HotelCheckVoucherError::class.java)
                    throw MessageErrorException(errorMessage.title)
                } else {
                    val checkVoucherData = objects.getData<HotelCheckVoucher.Response>(HotelCheckVoucher.Response::class.java).response
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
        // TODO try to ask is there cancel voucher promo for hotel?
//        cancelVoucherUseCase.execute(object : Subscriber<GraphqlResponse>() {
//
//            override fun onNext(response: GraphqlResponse) {
//                view.hideLoading()
//                val cancelPromoResponse = response.getData<HotelCancelVoucher.Response>(HotelCancelVoucher.Response::class.java).response
//                if (cancelPromoResponse.attributes.success) {
//                    view.onSuccessCancelPromo()
//                } else {
//                    view.onErrorCancelPromo(Throwable("Promo tidak berhasil dilepas"))
//                }
//            }
//
//            override fun onCompleted() {
//
//            }
//
//            override fun onError(e: Throwable) {
//                if (isViewAttached) {
//                    view.hideLoading()
//                    view.onErrorCancelPromo(e)
//                }
//            }
//        })
    }

    override fun detachView() {
        getDetailCouponMarketplaceUseCase.unsubscribe()
        super.detachView()
    }

    companion object {
        data class HotelCheckVoucherError(val id: String, val status: String, val title: String)
    }
}