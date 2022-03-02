package com.tokopedia.mvcwidget.usecases

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvcwidget.GqlUseCaseWrapper
import com.tokopedia.mvcwidget.TP_CATALOG_MVC_LIST_QUERY
import com.tokopedia.mvcwidget.TokopointsCatalogMVCListResponse
import com.tokopedia.mvcwidget.trackers.MvcSource
import javax.inject.Inject

@GqlQuery("TokopointsCatalogMvcQuery", TP_CATALOG_MVC_LIST_QUERY)

class CatalogMVCListUseCase @Inject constructor(var gqlWrapper: GqlUseCaseWrapper?) {

    suspend fun getResponse(map: HashMap<String, Any>): TokopointsCatalogMVCListResponse? {
        return gqlWrapper?.getResponse(getResponseClazz(), TokopointsCatalogMvcQuery.GQL_QUERY, map)
    }

    fun getQueryParams(shopId: String, productId: String = "", source: Int = MvcSource.DEFAULT): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[TokopointsCatalogMVCParams.SHOP_ID] = shopId
        if(productId.isNullOrEmpty().not()){
            variables[TokopointsCatalogMVCParams.PRODUCT_ID] = productId
        }
        when(source){
            MvcSource.PDP ->{
                variables[TokopointsCatalogMVCParams.SOURCE] = "pdp"
            }
            MvcSource.SHOP ->{
                variables[TokopointsCatalogMVCParams.SOURCE] = "shop_page"
            }
            MvcSource.DISCO ->{
                variables[TokopointsCatalogMVCParams.SOURCE] = "discovery_page"
            }
            MvcSource.REWARDS ->{
                variables[TokopointsCatalogMVCParams.SOURCE] = "rewards_homepage"
            }
        }
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
}

object TokopointsCatalogMVCParams {
    const val SHOP_ID = "shopID"
    const val PRODUCT_ID = "productID"
    const val SOURCE = "source"
    const val LIMIT = "limit"
    const val API_VERSION = "apiVersion"
}