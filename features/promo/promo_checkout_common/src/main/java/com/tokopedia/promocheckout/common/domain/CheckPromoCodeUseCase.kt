package com.tokopedia.promocheckout.common.domain

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.DataResponseCheckPromoCode
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.common.domain.model.ResponseCheckPromoCode
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*

class CheckPromoCodeUseCase(val resources: Resources, val graphqlUseCase: GraphqlUseCase) : UseCase<DataVoucher>() {

    val PROMO_CODE = "promoCode"
    val SKIP_APPLY = "skipApply"
    val PARAM_PROMO_SUGGESTED = "suggested"
    val ONE_CLICK_SHIPMENT = "oneClickShipment"

    override fun createObservable(requestParams: RequestParams?): Observable<DataVoucher> {
        graphqlUseCase.clearRequest()
        val variables = HashMap<String, Any>()
        variables[PROMO_CODE] = requestParams?.getString(PROMO_CODE, "") ?: ""
        variables[SKIP_APPLY] = requestParams?.getBoolean(SKIP_APPLY, false) ?: false
        variables[PARAM_PROMO_SUGGESTED] = requestParams?.getBoolean(PARAM_PROMO_SUGGESTED, false)?:false
        variables[ONE_CLICK_SHIPMENT] = requestParams?.getBoolean(ONE_CLICK_SHIPMENT, false)?:false

        val graphqlRequest = GraphqlRequest(PromoQuery.promoCheckPromoCode(), DataResponseCheckPromoCode::class.java, variables, false)
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .flatMap {
                    val checkPromoCode = it.getData<DataResponseCheckPromoCode>(DataResponseCheckPromoCode::class.java)
                    if(checkPromoCode?.checkPromoCartV2?.status.equals("error", true)){
                        throw CheckPromoCodeException(checkPromoCode?.checkPromoCartV2?.errorMessage?.joinToString()?:"")
                    }
                    Observable.just(checkPromoCode?.checkPromoCartV2?.data?.dataVoucher)
                }
    }

    fun createRequestParams(promoCode: String, skipApply: Boolean = false, suggestedPromo:Boolean = false, oneClickShipment : Boolean = false): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PROMO_CODE, promoCode)
        requestParams.putBoolean(SKIP_APPLY, skipApply)
        requestParams.putBoolean(PARAM_PROMO_SUGGESTED, suggestedPromo)
        requestParams.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment)
        return requestParams
    }

}