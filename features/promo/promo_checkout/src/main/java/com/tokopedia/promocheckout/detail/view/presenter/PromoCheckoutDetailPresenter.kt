package com.tokopedia.promocheckout.detail.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.*

class PromoCheckoutDetailPresenter(val getDetailCouponUseCase: GraphqlUseCase, val checkPromoUseCase: CheckPromoCodeUseCase) : BaseDaggerPresenter<PromoCheckoutDetailContract.View>(), PromoCheckoutDetailContract.Presenter {

    override fun validatePromoUse(codeCoupon: String, resources: Resources) {
        view.showLoading()
        checkPromoUseCase.execute(checkPromoUseCase.createRequestParams(codeCoupon), object : Subscriber<DataVoucher>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideLoading()
                    view.onErrorValidatePromo(e)
                }
            }

            override fun onNext(dataVoucher: DataVoucher) {
                view.hideLoading()
                if(dataVoucher.message?.state?.equals("red")?:false){
                    view.onErrorValidatePromo(ResponseErrorException(dataVoucher.message?.text))
                }else{
                    view.onSuccessValidatePromo(dataVoucher)
                }
            }
        })
    }

    override fun getDetailPromo(codeCoupon: String, resources : Resources) {
        view.showLoading()
        val variables = HashMap<String, Any>()
        variables.put(INPUT_CODE, codeCoupon)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.promo_checkout_detail_marketplace), DataPromoCheckoutDetail::class.java, variables)
        getDetailCouponUseCase.clearRequest()
        getDetailCouponUseCase.addRequest(graphqlRequest)
        getDetailCouponUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideLoading()
                    view.onErroGetDetail(e)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                view.hideLoading()
                val dataDetailCheckoutPromo = objects.getData<DataPromoCheckoutDetail>(DataPromoCheckoutDetail::class.java)
                view.onSuccessGetDetailPromo(dataDetailCheckoutPromo?.promoCheckoutDetailModel)
            }
        })
    }

    override fun detachView() {
        getDetailCouponUseCase.unsubscribe()
        checkPromoUseCase.unsubscribe()
        super.detachView()
    }

    companion object {
        private val INPUT_CODE = "code"
    }
}