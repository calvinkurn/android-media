package com.tokopedia.gamification.giftbox.domain

import com.tokopedia.gamification.giftbox.data.di.AUTO_APPLY
import com.tokopedia.gamification.giftbox.data.entities.AutoApplyResponse
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named


class AutoApplyUseCase @Inject constructor(@Named(AUTO_APPLY) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {
    private val PARAMS = AutoApplyParams

    suspend fun getResponse(map: HashMap<String, Any>): AutoApplyResponse {
        return gqlWrapper.getResponse(AutoApplyResponse::class.java, queryString, map)
    }

    fun getQueryParams(code: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[PARAMS.CODE] = code
        return variables
    }

    object AutoApplyParams {
        const val CODE = "code"
    }
}