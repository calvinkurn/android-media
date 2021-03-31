package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.hotel.HotelCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.HotelCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.HotelCheckVoucher
import rx.Subscriber

class PromoCheckoutListHotelPresenter(private val checkVoucherUseCase: HotelCheckVoucherUseCase,
                                      val checkVoucherMapper: HotelCheckVoucherMapper) : BaseDaggerPresenter<PromoCheckoutListContract.View>(), PromoCheckoutListFlightContract.Presenter {

    override fun checkPromoCode(cartID: String, promoCode: String, hexColor: String) {
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
}
