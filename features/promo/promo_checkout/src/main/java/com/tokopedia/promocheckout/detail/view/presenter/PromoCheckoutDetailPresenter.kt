package com.tokopedia.promocheckout.detail.view.presenter

import android.content.res.Resources
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.CancelPromoUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import com.tokopedia.usecase.RequestParams
import org.json.JSONException
import org.json.JSONObject
import rx.Subscriber
import java.lang.reflect.Type
import java.util.*

class PromoCheckoutDetailPresenter(val getDetailCouponUseCase: GraphqlUseCase,
                                   val checkPromoUseCase: CheckPromoCodeUseCase,
                                   val cancelPromoUseCase: CancelPromoUseCase) :
        BaseDaggerPresenter<PromoCheckoutDetailContract.View>(), PromoCheckoutDetailContract.Presenter {

    override fun cancelPromo() {
        view.showLoading()
        cancelPromoUseCase.execute(RequestParams.EMPTY, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideLoading()
                    view.onErrorCancelPromo(e)
                }
            }

            override fun onNext(restResponse: Map<Type, RestResponse>) {
                view.hideLoading()
                val responseCancel = restResponse.get(String::class.java)
                val responseCancelPromo = responseCancel?.getData<String>()
                var resultSuccess = false
                try {
                    val jsonObject = JSONObject(responseCancelPromo)
                    resultSuccess = jsonObject.getJSONObject("data")
                            .getBoolean("success")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                if(resultSuccess){
                    view.onSuccessCancelPromo();
                }else{
                    view.onErrorCancelPromo(RuntimeException())
                }
            }
        })
    }

    override fun validatePromoUse(codeCoupon: String, oneClickShipment:Boolean, resources: Resources) {
        view.showLoading()
        checkPromoUseCase.execute(checkPromoUseCase.createRequestParams(codeCoupon, oneClickShipment = oneClickShipment), object : Subscriber<DataVoucher>() {
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
                if(dataVoucher.message?.state?.mapToStatePromoCheckout() == TickerCheckoutView.State.FAILED){
                    view.onErrorValidatePromo(MessageErrorException(dataVoucher.message?.text))
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