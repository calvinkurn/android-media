package com.tokopedia.common_category.usecase.repository

import android.content.res.Resources
import com.google.gson.annotations.SerializedName
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.data.catalogModel.CatalogListResponse
import com.tokopedia.common_category.data.raw.GQL_NAV_CATEGORY_DETAIL_V3
import com.tokopedia.common_category.data.raw.GQL_NAV_SEARCH_CATALOG_COUNT
import com.tokopedia.common_category.model.bannedCategory.BannedCategoryResponse
import com.tokopedia.common_category.model.bannedCategory.Data
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type
import javax.inject.Inject

class CategoryNavRepository @Inject constructor() {

    @Inject
    lateinit var resources: Resources

    @Inject
    lateinit var baseRepository: BaseRepository


    suspend fun getCategoryDetail(departmentId: String): Data? {
        val params: RequestParams = getSubCategoryParam(departmentId)
        val query = GQL_NAV_CATEGORY_DETAIL_V3
        return baseRepository.getGQLData(query, BannedCategoryResponse::class.java, params.parameters).categoryDetailQuery?.data as Data
    }

    suspend fun getCategoryDetailWithCatalogCount(departmentId: String): GraphqlResponse? {
        val type: MutableList<Type> = ArrayList()
        type.add(BannedCategoryResponse::class.java)
        type.add(CatalogListResponse::class.java)
        return baseRepository.getGQLData(getQueries(), type, getRequests(departmentId))
    }

    private fun getQueries(): MutableList<String> {
        val queries: MutableList<String> = ArrayList()
        queries.add(GQL_NAV_CATEGORY_DETAIL_V3)
        queries.add(GQL_NAV_SEARCH_CATALOG_COUNT)
        return queries
    }

    private fun getRequests(departmentId: String) : MutableList<HashMap<String, Any>> {
        val request: MutableList<HashMap<String, Any>> = ArrayList()
        request.add(getSubCategoryParam(departmentId).parameters)
        request.add(getCatalogParam(departmentId).parameters)
        return request
    }

    private fun getCatalogParam(departmentId: String): RequestParams {
        val catalogMap = RequestParams()
        catalogMap.putInt(CategoryNavConstants.START, 0)
        catalogMap.putString(CategoryNavConstants.QUERY, "")
        catalogMap.putString(CategoryNavConstants.SOURCE, "directory")
        catalogMap.putString(CategoryNavConstants.ST, "catalog")
        catalogMap.putInt(CategoryNavConstants.ROWS, 10)
        catalogMap.putInt(CategoryNavConstants.OB, 23)
        val hashmap = HashMap<String, String>()
        var pmin = ""
        var pmax = ""
        if (hashmap.containsKey(CategoryNavConstants.PMIN)) {
            pmin = hashmap[CategoryNavConstants.PMIN] ?: ""
        }
        if (hashmap.containsKey(CategoryNavConstants.PMAX)) {
            pmax = hashmap[CategoryNavConstants.PMAX] ?: ""
        }
        catalogMap.putObject(CategoryNavConstants.FILTER, AceFilterInput(pmin, pmax, departmentId))
        return catalogMap
    }

    data class AceFilterInput(
            @SerializedName(CategoryNavConstants.PMIN)
            var pmin: String,
            @SerializedName(CategoryNavConstants.PMAX)
            var pmax: String,
            @SerializedName(CategoryNavConstants.SC)
            var sc: String)

    private fun getSubCategoryParam(departmentId: String): RequestParams {
        val subCategoryMap = RequestParams()
        subCategoryMap.putString(CategoryNavConstants.IDENTIFIER, departmentId)
        subCategoryMap.putBoolean(CategoryNavConstants.INTERMEDIARY, false)
        subCategoryMap.putBoolean(CategoryNavConstants.SAFESEARCH, false)
        return subCategoryMap
    }

}