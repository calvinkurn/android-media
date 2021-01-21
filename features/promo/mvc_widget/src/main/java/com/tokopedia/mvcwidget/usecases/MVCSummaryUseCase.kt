package com.tokopedia.mvcwidget.usecases

import com.google.gson.Gson
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.mvcwidget.*
import javax.inject.Inject

@GqlQuery("TpMvcSummaryQuery", TOKOPOINTS_CATALOG_MVC_SUMMARY_QUERY)

class MVCSummaryUseCase @Inject constructor(val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): TokopointsCatalogMVCSummaryResponse {
//        return gqlWrapper.getResponse(TokopointsCatalogMVCSummaryResponse::class.java, TpMvcSummaryQuery.GQL_QUERY, map)
        return getFakeResponse()
    }

    fun mapTokopointsCatalogMVCSummaryToMvcData(data: TokopointsCatalogMVCSummary): MvcData {
        var title = ""
        var subtitle = data.subTitle ?: ""
        if (!data.titles.isNullOrEmpty()) {
            title = data.titles[0]?.text ?: ""
        }
        return MvcData(title, subtitle, data.imageURL ?: "")
    }

    fun getQueryParams(shopId: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[TokopointsCatalogMVCParams.SHOP_ID] = shopId
        variables[TokopointsCatalogMVCParams.LIMIT] = 1
        return variables
    }

    fun getFakeResponse(): TokopointsCatalogMVCSummaryResponse {
        return Gson().fromJson(FakeResponse.FakeTokopointsCatalogMVCSummaryResponse, TokopointsCatalogMVCSummaryResponse::class.java)
    }
}