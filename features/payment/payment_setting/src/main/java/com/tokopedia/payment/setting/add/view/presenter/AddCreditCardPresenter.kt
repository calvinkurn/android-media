package com.tokopedia.payment.setting.add.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.payment.setting.add.domain.AddCreditCardUseCase
import com.tokopedia.payment.setting.add.model.ResponseGetIFrameCreditCard
import rx.Subscriber
import java.lang.reflect.Type

class AddCreditCardPresenter(val addCreditCardUseCase: AddCreditCardUseCase) : BaseDaggerPresenter<AddCreditCardContract.View>(), AddCreditCardContract.Presenter {

    override fun getIframeAddCC() {
        view.showProgressDialog()
        addCreditCardUseCase.execute(object : Subscriber<Map<Type, RestResponse>>() {

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if(isViewAttached) {
                    view.hideProgressDialog()
                    view.onErrorGetIframeData(e)
                }
            }

            override fun onNext(restResponse: Map<Type, RestResponse>) {
                //Success scenario e.g. HTTP 200 OK
                view.hideProgressDialog()
                val responseIframge = restResponse.get(ResponseGetIFrameCreditCard::class.java)
                val dataResponseGetIFrameCreditCard = responseIframge?.getData<ResponseGetIFrameCreditCard>()
                if(dataResponseGetIFrameCreditCard?.isSuccess?:false){
                    view.onSuccessGetIFrameData(dataResponseGetIFrameCreditCard?.data)
                }else{
                    view.onErrorGetIframeData(dataResponseGetIFrameCreditCard?.message)
                }
            }
        })
    }

}
