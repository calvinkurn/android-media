package com.tokopedia.oldcatalog.repository.catalogdetail

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.oldcatalog.model.raw.CatalogResponseData
import com.tokopedia.oldcatalog.model.raw.gql.GQL_CATALOG_QUERY
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.oldcatalog.model.raw.gql.GQL_CATALOG_REIMAGINE_QUERY
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type
import javax.inject.Inject

class CatalogDetailRepository @Inject constructor() : BaseRepository() {

    private val KEY_CATALOG_ID = "catalog_id"
    private val KEY_COMPARISION_ID = "comparison_id"
    private val KEY_DEVICE = "device"
    private val KEY_USER_ID = "user_id"
    private val KEY_PREFER_VERSION = "preferVersion"
    private val VALUE_PREFER_VERSION = "4"

    suspend fun getCatalogDetail(
        categoryID: String,
        comparedCatalogId: String,
        userId: String,
        device: String,
        isReimagine: Boolean = false
    ): GraphqlResponse? {
        val type: MutableList<Type> = ArrayList()
        type.add(CatalogResponseData::class.java)
        return getGQLData(getQueries(isReimagine), type, getRequests(categoryID,comparedCatalogId,userId,device))
    }

    private fun getQueries(isReimagine: Boolean): MutableList<String> {
        val queries: MutableList<String> = ArrayList()
        queries.add(if (isReimagine) GQL_CATALOG_REIMAGINE_QUERY else GQL_CATALOG_QUERY)
        return queries
    }

    private fun getRequests(catalogId: String,comparedCatalogId: String,userId : String, device : String): MutableList<HashMap<String, Any>> {
        val request: MutableList<HashMap<String, Any>> = ArrayList()
        request.add(createRequestParams(catalogId,comparedCatalogId,userId, device).parameters)
        return request
    }

    private fun createRequestParams(catalogId: String, comparedCatalogId: String , userId : String, device : String) : RequestParams {
        return RequestParams().apply {
            putString(KEY_CATALOG_ID, catalogId)
            putString(KEY_COMPARISION_ID, comparedCatalogId)
            putString(KEY_USER_ID, userId)
            putString(KEY_DEVICE, device)
            putString(KEY_PREFER_VERSION, VALUE_PREFER_VERSION)
        }
    }
}
