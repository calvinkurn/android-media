package com.tokopedia.promocheckout.common.domain.deals

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.domain.model.deals.DataRequest
import com.tokopedia.promocheckout.common.domain.model.deals.DealsPromoCheckResponse
import com.tokopedia.promocheckout.common.domain.model.deals.DealsRequestParams
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class DealsCheckVoucherUseCase (
        private val graphqlUseCase: GraphqlUseCase
){
    val params = "params"

    fun createRequestParams(code: List<String>, categoryName: String, metaData: String, grandTotal: Int): DealsRequestParams {
        val dealsRequest =  DealsRequestParams(
                categoryName = categoryName,
                data = DataRequest(
                        code = code,
                        meta_data = metaData,
                        grand_total = grandTotal
                )
        )
        return dealsRequest
    }

    fun execute(createRequestParams: DealsRequestParams, subscriber: Subscriber<GraphqlResponse>?){
        val mapParams = mapOf(params to createRequestParams)
        val graphqlRequest = GraphqlRequest(
                PromoQuery.promoDealsCheck(),
                DealsPromoCheckResponse::class.java,
                mapParams
        )
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

}