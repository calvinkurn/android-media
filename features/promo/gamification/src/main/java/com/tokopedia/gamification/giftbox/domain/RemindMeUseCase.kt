package com.tokopedia.gamification.giftbox.domain

import com.tokopedia.gamification.giftbox.data.di.GAMI_REMIND_ME
import com.tokopedia.gamification.giftbox.data.di.GAMI_REMIND_ME_CHECK
import com.tokopedia.gamification.giftbox.data.entities.RemindMeCheckEntity
import com.tokopedia.gamification.giftbox.data.entities.RemindMeEntity
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject
import javax.inject.Named

@GqlQuery("GamiUnsetReminderQuery", GAMI_UNSET_REMINDER)
class RemindMeUseCase @Inject constructor(
        @Named(GAMI_REMIND_ME) val mutationRemindMe: String,
        @Named(GAMI_REMIND_ME_CHECK) val queryRemindMeCheck: String,
        val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getRemindMeResponse(map: HashMap<String, Any>): RemindMeEntity {
        return gqlWrapper.getResponse(RemindMeEntity::class.java, mutationRemindMe, map)
    }

    suspend fun getUnSetRemindMeResponse(map: HashMap<String, Any>): RemindMeEntity {
        return gqlWrapper.getResponse(RemindMeEntity::class.java, GamiUnsetReminderQuery.GQL_QUERY, map)
    }

    suspend fun getRemindMeCheckResponse(map: HashMap<String, Any>): RemindMeCheckEntity {
        return gqlWrapper.getResponse(RemindMeCheckEntity::class.java, queryRemindMeCheck, map)
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