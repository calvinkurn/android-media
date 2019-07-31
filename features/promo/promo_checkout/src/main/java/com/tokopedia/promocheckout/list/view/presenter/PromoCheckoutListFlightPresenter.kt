package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckVoucherDigitalMapper
import com.tokopedia.promocheckout.common.domain.model.CheckVoucherDigital
import rx.Subscriber

class PromoCheckoutListFlightPresenter(private val checkVoucherUseCase: FlightCheckVoucherUseCase,
                                       private val cancelVoucherUseCase: FlightCancelVoucherUseCase) : BaseDaggerPresenter<PromoCheckoutListContract.View>(), PromoCheckoutListFlightContract.Presenter {

    override fun checkPromoCode(cartID: String, promoCode: String) {
        view.showProgressLoading()

        checkVoucherUseCase.execute(checkVoucherUseCase.createRequestParams(promoCode, cartID), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()
//                val checkVoucherData = objects.getData<CheckVoucherDigital.Response>(CheckVoucherDigital.Response::class.java).response
//                if (checkVoucherData.voucherData.success) {
//                    view.onSuccessCheckPromoCode(checkVoucherDigitalMapper.mapData(checkVoucherData.voucherData))
//                } else {
//                    view.onErrorCheckPromoCode(MessageErrorException(checkVoucherData.voucherData.message.text))
//                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorCheckPromoCode(e)
                }
            }

        })

    }

    override fun cancelPromoCode() {
        view.showProgressLoading()

        cancelVoucherUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()
//                val checkVoucherData = objects.getData<CheckVoucherDigital.Response>(CheckVoucherDigital.Response::class.java).response
//                if (checkVoucherData.voucherData.success) {
//                    view.onSuccessCheckPromoCode(checkVoucherDigitalMapper.mapData(checkVoucherData.voucherData))
//                } else {
//                    view.onErrorCheckPromoCode(MessageErrorException(checkVoucherData.voucherData.message.text))
//                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorCheckPromoCode(e)
                }
            }

        })
    }
}
