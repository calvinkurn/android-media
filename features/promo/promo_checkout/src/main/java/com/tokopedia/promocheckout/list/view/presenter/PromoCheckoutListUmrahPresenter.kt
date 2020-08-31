package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.mapper.UmrahCheckPromoMapper
import com.tokopedia.promocheckout.common.domain.model.CheckUmrahPromoCode
import com.tokopedia.promocheckout.common.domain.umroh.UmrahCheckPromoUseCase
import rx.Subscriber

class PromoCheckoutListUmrahPresenter (private val umrahCheckPromoUseCase: UmrahCheckPromoUseCase,
                                       val umrahCheckPromoMapper: UmrahCheckPromoMapper):
        BaseDaggerPresenter<PromoCheckoutListContract.View>(),
        PromoCheckoutListUmrahContract.Presenter {

    override fun checkPromo(promoCode: String, totalPrice:Int) {
        view.showProgressLoading()
        umrahCheckPromoUseCase.execute(umrahCheckPromoUseCase.createRequestParams(promoCode, totalPrice), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()
                val checkVoucherData = objects.getData<CheckUmrahPromoCode.Response>(CheckUmrahPromoCode.Response::class.java).umrahPromoCheck.umrahPromoData
                if (checkVoucherData.success) {
                    view.onSuccessCheckPromo(umrahCheckPromoMapper.mapData(checkVoucherData))
                } else {
                    view.onErrorCheckPromo(MessageErrorException(checkVoucherData.message.text))
                }
            }

            override fun onCompleted() { }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorCheckPromo(e)
                }
            }

        })
    }
}