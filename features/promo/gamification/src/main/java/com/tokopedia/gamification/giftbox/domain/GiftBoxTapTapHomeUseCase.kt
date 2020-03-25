package com.tokopedia.gamification.giftbox.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.gamification.giftbox.data.di.GAMI_TAP_EGG_HOME
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxEntity
import com.tokopedia.gamification.giftbox.presentation.helpers.FakeResponses
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import com.tokopedia.gamification.taptap.data.entiity.TapTapBaseEntity
import javax.inject.Inject
import javax.inject.Named

class GiftBoxTapTapHomeUseCase @Inject constructor(@Named(GAMI_TAP_EGG_HOME) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): TapTapBaseEntity {
        return gqlWrapper.getResponse(TapTapBaseEntity::class.java, queryString, map)
    }

    fun getFakeResponseActive(): TapTapBaseEntity {
        return getFakeEntity(FakeResponses.TapTapHome.RESPONSE)
    }

    private fun getFakeEntity(str: String): TapTapBaseEntity {
        val type = object : TypeToken<TapTapBaseEntity>() {}.type
        val entity = Gson().fromJson<TapTapBaseEntity>(str, type)
        return entity
    }
}