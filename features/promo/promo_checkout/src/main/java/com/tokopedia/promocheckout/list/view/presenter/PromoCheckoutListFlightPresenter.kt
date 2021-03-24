package com.tokopedia.promocheckout.list.view.presenter

import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.FlightCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.promocheckout.common.domain.model.FlightCheckVoucher
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailFlightPresenter
import rx.Subscriber

class PromoCheckoutListFlightPresenter(private val checkVoucherUseCase: FlightCheckVoucherUseCase,
                                       val checkVoucherMapper: FlightCheckVoucherMapper) : BaseDaggerPresenter<PromoCheckoutListContract.View>(), PromoCheckoutListFlightContract.Presenter {

    override fun attachView(view: PromoCheckoutListContract.View) {
        super.attachView(view)
        view.context
    }

    override fun checkPromoCode(cartID: String, promoCode: String) {
        view.showProgressLoading()

        checkVoucherUseCase.execute(checkVoucherUseCase.createRequestParams(promoCode, cartID), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()
                val errors = objects.getError(FlightCheckVoucher.Response::class.java)
                if (!errors.isNullOrEmpty()) {
                    val rawErrorMessage = errors[0].message
                    val errorMessage = Gson().fromJson(rawErrorMessage.substring(1, rawErrorMessage.length - 1), PromoCheckoutDetailFlightPresenter.Companion.FlightCheckVoucherError::class.java)
                    throw MessageErrorException(errorMessage.title)
                } else {
                    val checkVoucherData = objects.getData<FlightCheckVoucher.Response>(FlightCheckVoucher.Response::class.java).response
                    try {
                        if(view.context != null) checkVoucherData.messageColor = "#" + Integer.toHexString( ContextCompat.getColor(view.context!!, com.tokopedia.unifyprinciples.R.color.Unify_G200) and HEX_CODE_TRANSPARENCY)
                    }catch (e: Throwable){
                        e.printStackTrace()
                    }
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
    companion object{
        private const val HEX_CODE_TRANSPARENCY: Int = 0x00ffffff
    }
}
