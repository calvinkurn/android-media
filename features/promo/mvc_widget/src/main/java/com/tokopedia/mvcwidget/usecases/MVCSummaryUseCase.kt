package com.tokopedia.mvcwidget.usecases

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.mvcwidget.GqlUseCaseWrapper
import com.tokopedia.mvcwidget.TOKOPOINTS_CATALOG_MVC_SUMMARY
import com.tokopedia.mvcwidget.TOKOPOINTS_CATALOG_MVC_SUMMARY_QUERY
import com.tokopedia.mvcwidget.TokopointsCatalogMVCListResponse
import javax.inject.Inject
import javax.inject.Named

@GqlQuery("TpMvcSummaryQuery", TOKOPOINTS_CATALOG_MVC_SUMMARY_QUERY)

class MVCSummaryUseCase @Inject constructor(val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): TokopointsCatalogMVCListResponse {
        return gqlWrapper.getResponse(TokopointsCatalogMVCListResponse::class.java, TpMvcSummaryQuery.GQL_QUERY, map)
    }

    fun getQueryParams(shopId: Int): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[TokopointsCatalogMVCParams.SHOP_ID] = shopId
        return variables
    }
}