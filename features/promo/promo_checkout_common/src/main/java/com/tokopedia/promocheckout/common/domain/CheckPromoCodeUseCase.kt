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

class CheckPromoCodeUseCase(val resources: Resources, val graphqlUseCase: GraphqlUseCase) : UseCase<DataVoucher>() {

    val PROMO_CODE = "promoCode"
    val SKIP_APPLY = "skipApply"

    override fun createObservable(requestParams: RequestParams?): Observable<DataVoucher> {
        graphqlUseCase.clearRequest()
        val variables = HashMap<String, Any>()
        variables[PROMO_CODE] = requestParams?.getString(PROMO_CODE, "") ?: ""
        variables[SKIP_APPLY] = requestParams?.getBoolean(SKIP_APPLY, false) ?: false

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources, R.raw.check_promo_code), ResponseCheckPromoCode::class.java, variables)
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .flatMap {
                    val checkPromoCode = it.getData<ResponseCheckPromoCode>(ResponseCheckPromoCode::class.java)
                    Observable.just(checkPromoCode.data?.dataVoucher)
                }
    }

    fun createRequestParams(promoCode: String, skipApply: Boolean = false): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PROMO_CODE, promoCode)
        requestParams.putBoolean(SKIP_APPLY, skipApply)
        return requestParams
    }

}