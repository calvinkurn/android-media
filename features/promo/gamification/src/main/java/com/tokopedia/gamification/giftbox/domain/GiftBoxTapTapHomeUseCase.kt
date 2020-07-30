package com.tokopedia.gamification.giftbox.domain

import com.tokopedia.gamification.giftbox.data.di.GAMI_TAP_EGG_HOME
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import com.tokopedia.gamification.taptap.data.entiity.TapTapBaseEntity
import javax.inject.Inject
import javax.inject.Named

class GiftBoxTapTapHomeUseCase @Inject constructor(@Named(GAMI_TAP_EGG_HOME) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): TapTapBaseEntity {
        return gqlWrapper.getResponse(TapTapBaseEntity::class.java, queryString, map)
    }

}