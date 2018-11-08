package com.tokopedia.promocheckout.common.domain

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.common.domain.model.ResponseCheckPromoCode
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*

class CheckPromoCodeFinalUseCase(val resources: Resources, val graphqlUseCase: GraphqlUseCase) : UseCase<DataVoucher>() {
    val CARTS = "carts"
    val ONE_CLICK_SHIPMENT = "oneClickShipment"

    override fun createObservable(requestParams: RequestParams?): Observable<DataVoucher> {
        graphqlUseCase.clearRequest()
        val variables = HashMap<String, Any>()
        variables[CARTS] = requestParams?.getString(CARTS, "") ?: false
        variables[ONE_CLICK_SHIPMENT] = requestParams?.getBoolean(ONE_CLICK_SHIPMENT, false) ?: false

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources, R.raw.check_promo_code_final), ResponseCheckPromoCode::class.java, variables)
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .flatMap {
                    val checkPromoCode = it.getData<ResponseCheckPromoCode>(ResponseCheckPromoCode::class.java)
                    if(checkPromoCode.status.equals("error", true)){
                        throw CheckPromoCodeException(checkPromoCode.errorMessage?.joinToString()?:"")
                    }
                    Observable.just(checkPromoCode.data?.dataVoucher)
                }
    }

    fun createRequestParams(carts: String = "", oneClickShipment: Boolean = false): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(CARTS, carts)
        requestParams.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment)
        return requestParams
    }

}
