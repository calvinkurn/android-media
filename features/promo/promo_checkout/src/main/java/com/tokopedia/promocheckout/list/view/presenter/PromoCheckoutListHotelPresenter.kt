package com.tokopedia.promocheckout.list.view.presenter

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.hotel.HotelCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.HotelCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.HotelCheckVoucher
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailHotelPresenter
import rx.Subscriber

class PromoCheckoutListHotelPresenter(private val checkVoucherUseCase: HotelCheckVoucherUseCase,
                                      val checkVoucherMapper: HotelCheckVoucherMapper) : BaseDaggerPresenter<PromoCheckoutListContract.View>(), PromoCheckoutListFlightContract.Presenter {

    override fun checkPromoCode(cartID: String, promoCode: String) {
        view.showProgressLoading()

        checkVoucherUseCase.execute(checkVoucherUseCase.createRequestParams(promoCode, cartID), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()
                val errors = objects.getError(HotelCheckVoucher.Response::class.java)
                if (!errors.isNullOrEmpty()) {
                    val rawErrorMessage = errors[0].message
                    val errorMessage = Gson().fromJson(rawErrorMessage.substring(1, rawErrorMessage.length - 1), PromoCheckoutDetailHotelPresenter.Companion.HotelCheckVoucherError::class.java)
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
                    view.onErrorCheckPromo(e)
                }
            }

        })

    }
}
