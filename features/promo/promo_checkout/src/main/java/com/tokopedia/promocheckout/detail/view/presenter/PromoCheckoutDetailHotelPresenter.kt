package com.tokopedia.promocheckout.detail.view.presenter

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
                val checkVoucherData = objects.getData<HotelCheckVoucher.Response>(HotelCheckVoucher.Response::class.java).response
                if (checkVoucherData.isSuccess) {
                    view.onSuccessCheckPromo(checkVoucherMapper.mapData(checkVoucherData))
                } else {
                    view.onErrorCheckPromo(MessageErrorException(checkVoucherData.errorMessage))
                }
            }

            override fun onCompleted() { }

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

    override fun detachView() {
        getDetailCouponMarketplaceUseCase.unsubscribe()
        super.detachView()
    }
}