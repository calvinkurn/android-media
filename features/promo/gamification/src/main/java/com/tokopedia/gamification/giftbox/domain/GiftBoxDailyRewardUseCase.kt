package com.tokopedia.gamification.giftbox.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.gamification.giftbox.data.di.GIFT_BOX_DAILY_REWARD
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxEntity
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.presentation.helpers.FakeResponses
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

    fun getRandomResponse():GiftBoxRewardEntity{
        val responseList = arrayListOf<GiftBoxRewardEntity>(
                getFakeEntity(FakeResponses.GamiCrackResponse.COUPONS_WITH_POINTS),
                getFakeEntity(FakeResponses.GamiCrackResponse.COUPONS_ONLY),
                getFakeEntity(FakeResponses.GamiCrackResponse.POINTS_ONLY)
        )
        val rand = (0..2).random()
        return responseList[rand]
    }


    fun getCouponsWithOvoPoints():GiftBoxRewardEntity{
        return getFakeEntity(FakeResponses.GamiCrackResponse.COUPONS_WITH_POINTS)
    }

    fun getCoupons():GiftBoxRewardEntity{
        return getFakeEntity(FakeResponses.GamiCrackResponse.COUPONS_ONLY)
    }

    fun getPointsOnly():GiftBoxRewardEntity{
        return getFakeEntity(FakeResponses.GamiCrackResponse.POINTS_ONLY)
    }

    fun getError():GiftBoxRewardEntity{
        return getFakeEntity(FakeResponses.GamiCrackResponse.ERROR)
    }

    private fun getFakeEntity(str:String):GiftBoxRewardEntity{
        val type = object : TypeToken<GiftBoxRewardEntity>() {}.type
        val entity = Gson().fromJson<GiftBoxRewardEntity>(str, type)
        return entity
    }
}