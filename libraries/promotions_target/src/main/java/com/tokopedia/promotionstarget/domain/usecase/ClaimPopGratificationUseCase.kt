package com.tokopedia.promotionstarget.domain.usecase


import com.tokopedia.promotionstarget.data.CouponGratificationParams
import com.tokopedia.promotionstarget.data.claim.ClaimPayload
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.data.di.CLAIM_POP_GRATIFICATION
import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named


class ClaimPopGratificationUseCase @Inject constructor(@Named(CLAIM_POP_GRATIFICATION) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {
    val PARAMS = CouponGratificationParams

    suspend fun getResponse(map: HashMap<String, Any>): ClaimPopGratificationResponse {
        return gqlWrapper.getResponse(ClaimPopGratificationResponse::class.java, queryString, map)
    }

    fun getQueryParams(claimPayload: ClaimPayload): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[PARAMS.CAMPAIGN_SLUG] = claimPayload.campaignSlug
        variables[PARAMS.PAGE] = claimPayload.page
        return variables
    }

}