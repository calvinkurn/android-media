package com.tokopedia.category.navbottomsheet.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.category.navbottomsheet.model.CategoryAllList
import com.tokopedia.category.navbottomsheet.model.CategoryAllListResponse
import com.tokopedia.category.navbottomsheet.model.CategoryDetailResponse
import com.tokopedia.category.navbottomsheet.model.raw.GQL_CATEGORY_LIST
import com.tokopedia.category.navbottomsheet.model.raw.GQL_CATGEOY_DETAIL
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type
import javax.inject.Inject

class CategoryRepository @Inject constructor() : BaseRepository() {

    suspend fun getCategoryListWithCategoryDetail(categoryID: String): GraphqlResponse? {
        val type: MutableList<Type> = ArrayList()
        type.add(CategoryAllListResponse::class.java)
        type.add(CategoryDetailResponse::class.java)
        return getGQLData(getQueries(), type, getRequests(categoryID))
    }

    private fun getQueries(): MutableList<String> {
        val queries: MutableList<String> = ArrayList()
        queries.add(GQL_CATEGORY_LIST)
        queries.add(GQL_CATGEOY_DETAIL)
        return queries
    }

    private fun getRequests(categoryID: String): MutableList<HashMap<String, Any>> {
        val request: MutableList<HashMap<String, Any>> = ArrayList()
        request.add(getListParams().parameters)
        request.add(getDetailParams(categoryID).parameters)
        return request
    }


    private fun getListParams(): RequestParams {
        return RequestParams().apply {
            putBoolean("catnav", false)
        }
    }

    private fun getDetailParams(categoryID: String): RequestParams {
        return RequestParams().apply {
            putString("identifier", categoryID)
            putBoolean("intermediary", false)
            putBoolean("safeSearch", false)
        }
    }
}