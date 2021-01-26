package com.tokopedia.gamification.pdp.domain.usecase

import com.tokopedia.gamification.pdp.data.GamingRecommendationParamResponse
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import com.tokopedia.gamification.pdp.data.di.modules.GqlQueryModule
import javax.inject.Inject
import javax.inject.Named

class GamingRecommendationParamUseCase @Inject constructor(
        @Named(GqlQueryModule.GAMING_RECOMMENDATION_PARAM_QUERY)
        val queryString: String,
        val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): GamingRecommendationParamResponse {
        return gqlWrapper.getResponse(GamingRecommendationParamResponse::class.java, queryString, map)
    }

    fun getRequestParams(pageName: String): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[Params.PAGE_NAME] = pageName
        return map
    }

    object Params {
        const val PAGE_NAME = "pageName"
    }
}
