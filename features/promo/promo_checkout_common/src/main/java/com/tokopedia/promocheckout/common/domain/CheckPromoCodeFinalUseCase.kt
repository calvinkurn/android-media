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
import java.util.HashMap

class CheckPromoCodeFinalUseCase(val resources: Resources, val graphqlUseCase: GraphqlUseCase) : UseCase<DataVoucher>(){

    override fun createObservable(requestParams: RequestParams?): Observable<DataVoucher> {
        graphqlUseCase.clearRequest()
        val variables = HashMap<String, Any>()
        variables[CARTS] = requestParams?.getString(CARTS, "") ?: false

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources, R.raw.check_promo_code_final), ResponseCheckPromoCode::class.java, variables)
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .flatMap {
                    val checkPromoCode = it.getData<ResponseCheckPromoCode>(ResponseCheckPromoCode::class.java)
                    Observable.just(checkPromoCode.data?.dataVoucher)
                }
    }

    companion object {
        val CARTS = "carts"
        fun createRequestParams(carts: String = ""): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(CARTS, carts)
            return requestParams
        }
    }

}
