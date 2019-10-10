package com.tokopedia.promotionstarget.usecase


import com.tokopedia.promotionstarget.CouponGratificationParams
import com.tokopedia.promotionstarget.GqlUseCaseWrapper
import com.tokopedia.promotionstarget.data.GetPopGratificationlResponse
import com.tokopedia.promotionstarget.viewmodel.PromotionsTargetVM

typealias PARAMS = CouponGratificationParams

class ClaimPopGratificationUseCase(val queryString: String) {


    private val gqlWrapper = GqlUseCaseWrapper()

    suspend fun getResponse(map: HashMap<String, Any>): GetPopGratificationlResponse {
        return gqlWrapper.getResponse(GetPopGratificationlResponse::class.java, queryString, map)
    }

    fun getQueryParams(campaignSlug: String, page: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[PARAMS.CAMPAIGN_SLUG] = campaignSlug
        variables[PARAMS.PAGE] = page
        return variables
    }

}