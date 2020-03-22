package com.tokopedia.gamification.giftbox.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.gamification.giftbox.data.di.GAMI_REMIND_ME
import com.tokopedia.gamification.giftbox.data.di.GAMI_REMIND_ME_CHECK
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxEntity
import com.tokopedia.gamification.giftbox.data.entities.RemindMeCheckEntity
import com.tokopedia.gamification.giftbox.data.entities.RemindMeEntity
import com.tokopedia.gamification.giftbox.presentation.helpers.FakeResponses
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named

class RemindMeUseCase @Inject constructor(
        @Named(GAMI_REMIND_ME) val mutationRemindMe: String,
        @Named(GAMI_REMIND_ME_CHECK) val queryRemindMeCheck: String,
        val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getRemindMeResponse(map: HashMap<String, Any>): RemindMeEntity {
        return gqlWrapper.getResponse(RemindMeEntity::class.java, mutationRemindMe, map)
    }

    suspend fun getRemindMeCheckResponse(map: HashMap<String, Any>): RemindMeCheckEntity {
        return gqlWrapper.getResponse(RemindMeCheckEntity::class.java, queryRemindMeCheck, map)
    }

    suspend fun getRemindMeCheckResponseFake(): RemindMeCheckEntity {
        return getFakeEntity(FakeResponses.RemindMeCheckResponse.SUCCESS)
    }

    fun getRequestParams(about: String): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[Params.ABOUT] = about
        return map
    }

    object Params {
        const val ABOUT = "about"
    }


    private fun getFakeEntity(str: String): RemindMeCheckEntity {
        val type = object : TypeToken<RemindMeCheckEntity>() {}.type
        val entity = Gson().fromJson<RemindMeCheckEntity>(str, type)
        return entity
    }

}