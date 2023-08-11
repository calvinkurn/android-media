package com.tokopedia.oldcatalog.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.oldcatalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.oldcatalog.model.raw.gql.GQL_CATALOG_COMPARISON_PRODUCT_QUERY
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type
import javax.inject.Inject

class CatalogComparisonProductRepository @Inject constructor() : BaseRepository() {

    private val KEY_CATALOG_ID = "catalog_id"
    private val KEY_BRAND = "brand"
    private val KEY_CATEGORY_ID = "category_id"
    private val KEY_LIMIT = "limit"
    private val KEY_PAGE = "page"
    private val KEY_NAME = "name"

    suspend fun getComparisonProducts(catalogId: String, brand : String, categoryId : String,
                              limit: String, page : String, name : String): GraphqlResponse? {
        val type: MutableList<Type> = ArrayList()
        type.add(CatalogComparisonProductsResponse::class.java)
        return getGQLData(getQueries(), type, getRequests(catalogId,brand, categoryId,limit,page,name))
    }

    private fun getQueries(): MutableList<String> {
        val queries: MutableList<String> = ArrayList()
        queries.add(GQL_CATALOG_COMPARISON_PRODUCT_QUERY)
        return queries
    }

    private fun getRequests(catalogId: String, brand : String, categoryId : String,
                            limit: String, page : String, name : String): MutableList<HashMap<String, Any>> {
        val request: MutableList<HashMap<String, Any>> = ArrayList()
        request.add(createRequestParams(catalogId,brand, categoryId,limit,page,name).parameters)
        return request
    }

    private fun createRequestParams(catalogId: String, brand : String, categoryId : String,
                                    limit: String, page : String, name : String) : RequestParams {
        return RequestParams().apply {
            putString(KEY_CATALOG_ID, catalogId)
            putString(KEY_BRAND, brand)
            putString(KEY_CATEGORY_ID, categoryId)
            putString(KEY_LIMIT, limit)
            putString(KEY_PAGE, page)
            putString(KEY_NAME, name)
        }
    }
}
