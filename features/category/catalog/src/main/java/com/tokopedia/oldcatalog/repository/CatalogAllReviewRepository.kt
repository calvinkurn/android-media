package com.tokopedia.oldcatalog.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.oldcatalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.oldcatalog.model.raw.gql.GQL_CATALOG_REVIEW_QUERY
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type
import javax.inject.Inject

class CatalogAllReviewRepository @Inject constructor() : BaseRepository() {

    private val KEY_CATALOG_ID = "catalog_id"
    private val KEY_KEY = "key"
    private val KEY_VALUE = "value"

    suspend fun getAllReviews(categoryID: String, key : String, value : String): GraphqlResponse? {
        val type: MutableList<Type> = ArrayList()
        type.add(CatalogProductReviewResponse::class.java)
        return getGQLData(getQueries(), type, getRequests(categoryID,key,value))
    }

    private fun getQueries(): MutableList<String> {
        val queries: MutableList<String> = ArrayList()
        queries.add(GQL_CATALOG_REVIEW_QUERY)
        return queries
    }

    private fun getRequests(catalogId: String,key : String, value : String): MutableList<HashMap<String, Any>> {
        val request: MutableList<HashMap<String, Any>> = ArrayList()
        request.add(createRequestParams(catalogId,key, value).parameters)
        return request
    }

    private fun createRequestParams(catalogId: String, key : String, value : String) : RequestParams {
        return RequestParams().apply {
            putString(KEY_CATALOG_ID, catalogId)
            putString(KEY_KEY, key)
            putString(KEY_VALUE, value)
        }
    }
}
