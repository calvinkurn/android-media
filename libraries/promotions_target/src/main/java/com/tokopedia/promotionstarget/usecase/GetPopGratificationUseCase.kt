package com.tokopedia.promotionstarget.usecase


import com.tokopedia.promotionstarget.CouponGratificationParams
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.di.GET_POP_GRATIFICATION
import com.tokopedia.promotionstarget.gql.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named


class GetPopGratificationUseCase @Inject constructor(@Named(GET_POP_GRATIFICATION) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {
    val PARAMS = CouponGratificationParams

    suspend fun getResponse(map: HashMap<String, Any>): GetPopGratificationResponse {
        return gqlWrapper.getResponse(GetPopGratificationResponse::class.java, queryString, map)
    }

    fun getQueryParams(campaignSlug: String, page: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[PARAMS.CAMPAIGN_SLUG] = campaignSlug
        variables[PARAMS.PAGE] = page
        return variables
    }

}