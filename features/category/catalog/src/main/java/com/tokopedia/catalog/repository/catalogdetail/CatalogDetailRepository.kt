package com.tokopedia.catalog.repository.catalogdetail

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.catalog.model.raw.CatalogResponseData
import com.tokopedia.catalog.model.raw.gql.GQL_CATALOG_QUERY
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type
import javax.inject.Inject

class CatalogDetailRepository @Inject constructor() : BaseRepository() {

    private val KEY_CATALOG_ID = "catalog_id"
    private val KEY_DEVICE = "device"
    private val KEY_USER_ID = "user_id"

    suspend fun getCatalogDetail(categoryID: String,userId : String, device : String): GraphqlResponse? {
        val type: MutableList<Type> = ArrayList()
        type.add(CatalogResponseData::class.java)
        return getGQLData(getQueries(), type, getRequests(categoryID,userId,device))
    }

    private fun getQueries(): MutableList<String> {
        val queries: MutableList<String> = ArrayList()
        queries.add(GQL_CATALOG_QUERY)
        return queries
    }

    private fun getRequests(catalogId: String,userId : String, device : String): MutableList<HashMap<String, Any>> {
        val request: MutableList<HashMap<String, Any>> = ArrayList()
        request.add(createRequestParams(catalogId,userId, device).parameters)
        return request
    }

    private fun createRequestParams(catalogId: String, userId : String, device : String) : RequestParams {
        return RequestParams().apply {
            putString(KEY_CATALOG_ID, catalogId)
            putString(KEY_USER_ID, userId)
            putString(KEY_DEVICE, device)
        }
    }
}