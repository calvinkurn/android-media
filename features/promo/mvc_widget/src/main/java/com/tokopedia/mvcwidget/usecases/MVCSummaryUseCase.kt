package com.tokopedia.mvcwidget.usecases

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.mvcwidget.GqlUseCaseWrapper
import com.tokopedia.mvcwidget.TOKOPOINTS_CATALOG_MVC_SUMMARY_QUERY
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import javax.inject.Inject

@GqlQuery("TpMvcSummaryQuery", TOKOPOINTS_CATALOG_MVC_SUMMARY_QUERY)

class MVCSummaryUseCase @Inject constructor(val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): TokopointsCatalogMVCSummaryResponse {
        return gqlWrapper.getResponse(TokopointsCatalogMVCSummaryResponse::class.java, TpMvcSummaryQuery.GQL_QUERY, map)
    }

    fun getQueryParams(shopId: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[TokopointsCatalogMVCParams.SHOP_ID] = shopId
        variables[TokopointsCatalogMVCParams.API_VERSION] = "2.0.0"
        return variables
    }
}