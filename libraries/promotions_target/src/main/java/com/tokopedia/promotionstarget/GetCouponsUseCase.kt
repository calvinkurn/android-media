package com.tokopedia.promotionstarget

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable
import java.util.HashMap

class GetCouponsUseCase(val queryString: String) {

    fun getData() {
        val graphqlRequest = GraphqlRequest(queryString, GetCouponGqlResponse::class.java, getQueryParams("",""))
        val graphqlUseCase  = GraphqlUseCase()
        graphqlUseCase.addRequest(graphqlRequest)

        graphqlUseCase.createObservable(RequestParams.EMPTY).map {
//            it.getData(GetCouponGqlResponse::class.java)
        }

    }

    fun getQueryParams(campaignSlug: String, page: String): HashMap<String,Any> {
        val variables = HashMap<String, Any>()
        variables["CampaignSlug"] = campaignSlug
        variables["Page"] = page
        return variables
    }

}