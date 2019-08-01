package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.FlightCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.promocheckout.common.domain.model.FlightCheckVoucher
import rx.Subscriber

class PromoCheckoutListFlightPresenter(private val checkVoucherUseCase: FlightCheckVoucherUseCase,
                                       val checkVoucherMapper: FlightCheckVoucherMapper) : BaseDaggerPresenter<PromoCheckoutListContract.View>(), PromoCheckoutListFlightContract.Presenter {

    override fun checkPromoCode(cartID: String, promoCode: String) {
        view.showProgressLoading()

        checkVoucherUseCase.execute(checkVoucherUseCase.createRequestParams(promoCode, cartID), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()
                val checkVoucherData = objects.getData<FlightCheckVoucher.Response>(FlightCheckVoucher.Response::class.java).response
                view.onSuccessCheckPromo(checkVoucherMapper.mapData(checkVoucherData))
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
