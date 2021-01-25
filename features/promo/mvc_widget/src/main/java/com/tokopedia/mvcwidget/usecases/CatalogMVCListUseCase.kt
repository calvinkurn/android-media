package com.tokopedia.mvcwidget.usecases

import com.google.gson.Gson
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvcwidget.FakeResponse
import com.tokopedia.mvcwidget.GqlUseCaseWrapper
import com.tokopedia.mvcwidget.TP_CATALOG_MVC_LIST_QUERY
import com.tokopedia.mvcwidget.TokopointsCatalogMVCListResponse
import kotlinx.coroutines.delay
import javax.inject.Inject

@GqlQuery("TokopointsCatalogMvcQuery", TP_CATALOG_MVC_LIST_QUERY)

class CatalogMVCListUseCase @Inject constructor(var gqlWrapper: GqlUseCaseWrapper?) {
    private val PARAMS = TokopointsCatalogMVCParams

    suspend fun getResponse(map: HashMap<String, Any>): TokopointsCatalogMVCListResponse? {
        return getFakeResponse()
//        return gqlWrapper?.getResponse(getResponseClazz(), TokopointsCatalogMvcQuery.GQL_QUERY, map)
    }

    fun getQueryParams(shopId: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[TokopointsCatalogMVCParams.SHOP_ID] = shopId.toString()
        return variables
    }

    fun getResponseClazz(): Class<TokopointsCatalogMVCListResponse> {
        return TokopointsCatalogMVCListResponse::class.java
    }

    fun getQuery(): String {
        return TokopointsCatalogMvcQuery.GQL_QUERY
    }

    fun getGraphqlRequest(shopId: String): GraphqlRequest {
        val mvcUseCase = CatalogMVCListUseCase(null)
        val mvcParams = mvcUseCase.getQueryParams(shopId)
        val mvcQuery = mvcUseCase.getQuery()
        val mvcRequest = GraphqlRequest(mvcQuery,
                mvcUseCase.getResponseClazz(), mvcParams)
        return mvcRequest
    }

    suspend fun getFakeResponse(): TokopointsCatalogMVCListResponse {
        delay(1000L)
        return Gson().fromJson(FakeResponse.FakeTokopointsCatalogMVCListFirstFollowResponse, TokopointsCatalogMVCListResponse::class.java)
    }
}

object TokopointsCatalogMVCParams {
    const val SHOP_ID = "shopID"
    const val LIMIT = "limit"
}