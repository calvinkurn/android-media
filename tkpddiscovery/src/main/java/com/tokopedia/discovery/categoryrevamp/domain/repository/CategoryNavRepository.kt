package com.tokopedia.discovery.categoryrevamp.domain.repository

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery.R
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.bannedCategory.BannedCategoryResponse
import com.tokopedia.discovery.categoryrevamp.data.bannedCategory.Data
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class CategoryNavRepository @Inject constructor() {

    @Inject
    lateinit var resources: Resources

    @Inject
    lateinit var baseRepository: BaseRepository


    suspend fun getCategoryDetail(departmentId: String): Data? {
        val params: RequestParams = getSubCategoryParam(departmentId)
        val query = GraphqlHelper.loadRawString(resources, R.raw.gql_nav_category_detail_v3)
        return (baseRepository.getGQLData(query, BannedCategoryResponse::class.java, params.parameters)
                as BannedCategoryResponse).categoryDetailQuery?.data as Data
    }

    private fun getSubCategoryParam(departmentId: String): RequestParams {
        val subCategoryMap = RequestParams()
        subCategoryMap.putString(com.tokopedia.common_category.constants.CategoryNavConstants.IDENTIFIER, departmentId)
        subCategoryMap.putBoolean(com.tokopedia.common_category.constants.CategoryNavConstants.INTERMEDIARY, false)
        subCategoryMap.putBoolean(com.tokopedia.common_category.constants.CategoryNavConstants.SAFESEARCH, false)
        return subCategoryMap
    }

}