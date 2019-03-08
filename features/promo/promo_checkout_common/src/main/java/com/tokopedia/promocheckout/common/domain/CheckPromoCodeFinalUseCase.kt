package com.tokopedia.promocheckout.common.domain

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.DataResponseCheckPromoCodeFinal
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

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources, R.raw.check_promo_code_final), DataResponseCheckPromoCodeFinal::class.java, variables, false)
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .flatMap {
                    val checkPromoCode = it.getData<DataResponseCheckPromoCodeFinal>(DataResponseCheckPromoCodeFinal::class.java)
                    if(checkPromoCode.checkPromoCartV2?.status.equals("error", true)){
                        throw CheckPromoCodeException(checkPromoCode.checkPromoCartV2?.errorMessage?.joinToString()?:"")
                    }
                    Observable.just(checkPromoCode.checkPromoCartV2?.data?.dataVoucher)
                }
    }

    fun createRequestParams(carts: String = "", oneClickShipment: Boolean = false): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(CARTS, carts)
        requestParams.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment)
        return requestParams
    }

}
