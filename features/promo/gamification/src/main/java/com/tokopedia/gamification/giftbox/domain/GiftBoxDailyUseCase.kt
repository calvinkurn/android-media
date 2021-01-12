package com.tokopedia.gamification.giftbox.domain

import com.google.gson.Gson
import com.tokopedia.gamification.FAKE_GAMI_LUCKY_HOME
import com.tokopedia.gamification.giftbox.data.di.GIFT_BOX_DAILY
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxEntity
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named

class GiftBoxDailyUseCase @Inject constructor(@Named(GIFT_BOX_DAILY) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): GiftBoxEntity {
        return getFakeResponse(map)
//        return gqlWrapper.getResponse(GiftBoxEntity::class.java, queryString, map)
    }

    suspend fun getFakeResponse(map: HashMap<String, Any>): GiftBoxEntity {
        return  Gson().fromJson<GiftBoxEntity>(FAKE_GAMI_LUCKY_HOME, GiftBoxEntity::class.java)
    }

    fun getRequestParams(pageName: String): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[Params.PAGE] = pageName
        map[Params.CAMPAIGN_SLUG] = ""
        return map
    }

    object Params {
        const val CAMPAIGN_SLUG = "campaignSlug"
        const val PAGE = "page"
    }

}