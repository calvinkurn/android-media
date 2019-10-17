package com.tokopedia.promotionstarget.usecase

import com.tokopedia.promotionstarget.AutoApplyParams
import com.tokopedia.promotionstarget.data.autoApply.AutoApplyResponse
import com.tokopedia.promotionstarget.di.AUTO_APPLY
import com.tokopedia.promotionstarget.gql.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named


class AutoApplyUseCase @Inject constructor(@Named(AUTO_APPLY) val queryString: String) {
    private val PARAMS = AutoApplyParams

    private val gqlWrapper = GqlUseCaseWrapper()

    suspend fun getResponse(map: HashMap<String, Any>): AutoApplyResponse {
        return gqlWrapper.getResponse(AutoApplyResponse::class.java, queryString, map)
    }

    fun getQueryParams(code: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[PARAMS.CODE] = code
        return variables
    }

}