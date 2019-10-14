package com.tokopedia.promotionstarget.usecase


import com.tokopedia.promotionstarget.CouponGratificationParams
import com.tokopedia.promotionstarget.gql.GqlUseCaseWrapper
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.di.CLAIM_POP_GRATIFICATION
import javax.inject.Inject
import javax.inject.Named


class ClaimPopGratificationUseCase @Inject constructor(@Named(CLAIM_POP_GRATIFICATION) val queryString: String) {
    val PARAMS = CouponGratificationParams

    private val gqlWrapper = GqlUseCaseWrapper()

    suspend fun getResponse(map: HashMap<String, Any>): ClaimPopGratificationResponse {
        return gqlWrapper.getResponse(ClaimPopGratificationResponse::class.java, queryString, map)
    }

    fun getQueryParams(campaignSlug: String, page: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[PARAMS.CAMPAIGN_SLUG] = campaignSlug
        variables[PARAMS.PAGE] = page
        return variables
    }

}