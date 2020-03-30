package com.tokopedia.gamification.giftbox.domain

import com.tokopedia.gamification.giftbox.data.di.GIFT_BOX_DAILY_REWARD
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named

class GiftBoxDailyRewardUseCase @Inject constructor(@Named(GIFT_BOX_DAILY_REWARD) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): GiftBoxRewardEntity {
        return gqlWrapper.getResponse(GiftBoxRewardEntity::class.java, queryString, map)
    }

    fun getRequestParams(campaignSlug: String, uniqueCode: String): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[Params.UNIQUE_CODE] = uniqueCode
        map[Params.CAMPAIGN_SLUG] = campaignSlug
        return map
    }

    object Params {
        const val CAMPAIGN_SLUG = "campaignSlug"
        const val UNIQUE_CODE = "uniqueCode"
    }

}