package com.tokopedia.promocheckout.list.view.presenter

import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.hotel.HotelCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.HotelCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.HotelCheckVoucher
import rx.Subscriber

class PromoCheckoutListHotelPresenter(private val checkVoucherUseCase: HotelCheckVoucherUseCase,
                                      val checkVoucherMapper: HotelCheckVoucherMapper) : BaseDaggerPresenter<PromoCheckoutListContract.View>(), PromoCheckoutListFlightContract.Presenter {

    override fun checkPromoCode(cartID: String, promoCode: String) {
        view.showProgressLoading()

        checkVoucherUseCase.execute(checkVoucherUseCase.createRequestParams(promoCode, cartID), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()
                val checkVoucherData = objects.getData<HotelCheckVoucher.Response>(HotelCheckVoucher.Response::class.java).response
                try {
                    if(view.context != null) checkVoucherData.messageColor = "#" + Integer.toHexString( ContextCompat.getColor(view.context!!, com.tokopedia.unifyprinciples.R.color.Unify_G200) and HEX_CODE_TRANSPARENCY)
                }catch (e: Throwable){
                    e.printStackTrace()
                }
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
    companion object{
        private const val HEX_CODE_TRANSPARENCY: Int = 0x00ffffff
    }
}
