package com.tokopedia.gamification.giftbox.domain

import com.tokopedia.gamification.giftbox.data.di.GAMI_REMIND_ME
import com.tokopedia.gamification.giftbox.data.di.GAMI_REMIND_ME_CHECK
import com.tokopedia.gamification.giftbox.data.entities.*
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import com.tokopedia.gql_query_annotation.GqlQuery
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Named

@GqlQuery("GamiUnsetReminderQuery", GAMI_UNSET_REMINDER)
class RemindMeUseCase @Inject constructor(
        @Named(GAMI_REMIND_ME) val mutationRemindMe: String,
        @Named(GAMI_REMIND_ME_CHECK) val queryRemindMeCheck: String,
        val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getRemindMeResponse(map: HashMap<String, Any>): RemindMeEntity {
        delay(2000L)
//        return gqlWrapper.getResponse(RemindMeEntity::class.java, mutationRemindMe, map)
         return RemindMeEntity(GameRemindMe(ResultStatus(code = "200", message = arrayListOf("OK"), reason = "r"), true))
    }

    suspend fun getUnSetRemindMeResponse(map: HashMap<String, Any>): RemindMeEntity {
        delay(2000L)
//        return gqlWrapper.getResponse(RemindMeEntity::class.java, GamiUnsetReminderQuery.GQL_QUERY, map)
        return RemindMeEntity(GameRemindMe(ResultStatus(code = "200", message = arrayListOf("OK"), reason = "r"), true))
    }

    suspend fun getRemindMeCheckResponse(map: HashMap<String, Any>): RemindMeCheckEntity {
        return getRemindMeCheckResponseFake(map)
//        return gqlWrapper.getResponse(RemindMeCheckEntity::class.java, queryRemindMeCheck, map)
    }

    suspend fun getRemindMeCheckResponseFake(map: HashMap<String, Any>): RemindMeCheckEntity {
        return RemindMeCheckEntity(GameRemindMeCheck(ResultStatus(code = "200", message = arrayListOf("OK"), reason = "r"), true))
    }

    fun getRequestParams(about: String): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[Params.ABOUT] = about
        return map
    }

    object Params {
        const val ABOUT = "about"
    }
}