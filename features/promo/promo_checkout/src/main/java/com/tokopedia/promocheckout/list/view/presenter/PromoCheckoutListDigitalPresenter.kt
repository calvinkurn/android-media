package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.DigitalCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.CheckVoucherDigital
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import rx.Subscriber

class PromoCheckoutListDigitalPresenter(private val checkVoucherUseCase: DigitalCheckVoucherUseCase,
                                        val checkVoucherMapper: DigitalCheckVoucherMapper) : BaseDaggerPresenter<PromoCheckoutListContract.View>(), PromoCheckoutListDigitalContract.Presenter {

    override fun checkPromoCode(promoCode: String, promoDigitalModel: PromoDigitalModel) {
        view.showProgressLoading()

        checkVoucherUseCase.execute(checkVoucherUseCase.createRequestParams(promoCode, promoDigitalModel), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()

                val checkVoucherData = objects.getSuccessData<CheckVoucherDigital.Response>().response
                if (checkVoucherData.voucherData.success) {
                    view.onSuccessCheckPromo(checkVoucherMapper.mapData(checkVoucherData.voucherData))
                } else {
                    view.onErrorCheckPromo(MessageErrorException(checkVoucherData.voucherData.message.text))
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
