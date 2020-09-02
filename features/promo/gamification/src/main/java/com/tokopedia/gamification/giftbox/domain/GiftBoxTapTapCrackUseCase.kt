package com.tokopedia.gamification.giftbox.domain

import com.tokopedia.gamification.GamificationConstants
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity
import com.tokopedia.gamification.giftbox.data.di.GAMI_TAP_CRACK_EGG
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named

class GiftBoxTapTapCrackUseCase @Inject constructor(@Named(GAMI_TAP_CRACK_EGG) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {
    suspend fun getResponse(map: HashMap<String, Any>): ResponseCrackResultEntity {
        return gqlWrapper.getResponse(ResponseCrackResultEntity::class.java, queryString, map)
    }

    fun getQueryParams(tokenId: String, campaignId: Long): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[GamificationConstants.GraphQlVariableKeys.TOKEN_ID] = tokenId
        variables[GamificationConstants.GraphQlVariableKeys.CAMPAIGN_ID] = campaignId
        return variables
    }

}