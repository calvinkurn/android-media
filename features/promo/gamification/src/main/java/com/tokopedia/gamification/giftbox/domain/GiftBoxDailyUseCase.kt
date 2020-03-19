package com.tokopedia.gamification.giftbox.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.gamification.giftbox.data.di.GIFT_BOX_DAILY
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxEntity
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.presentation.helpers.FakeResponses
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named

class GiftBoxDailyUseCase @Inject constructor(@Named(GIFT_BOX_DAILY) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): GiftBoxEntity {
        return gqlWrapper.getResponse(GiftBoxEntity::class.java, queryString, map)
    }

    fun getRequestParams(campaignSlug: String, pageName: String): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[Params.PAGE] = pageName
        map[Params.CAMPAIGN_SLUG] = campaignSlug
        return map
    }

    object Params {
        const val CAMPAIGN_SLUG = "campaignSlug"
        const val PAGE = "page"
    }

    fun getFakeResponseActive(): GiftBoxEntity {
        return getFakeEntity(FakeResponses.GamiLuckyHomeResponse.ACTIVE)
    }

    fun getFakeResponseEmpty(): GiftBoxEntity {
        return getFakeEntity(FakeResponses.GamiLuckyHomeResponse.EMPTY)
    }

    private fun getFakeEntity(str: String): GiftBoxEntity {
        val type = object : TypeToken<GiftBoxEntity>() {}.type
        val entity = Gson().fromJson<GiftBoxEntity>(str, type)
        return entity
    }


}