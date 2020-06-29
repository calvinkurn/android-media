package com.tokopedia.common_category.usecase.repository

import android.content.res.Resources
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.data.raw.GQL_NAV_CATEGORY_DETAIL_V3
import com.tokopedia.common_category.model.bannedCategory.BannedCategoryResponse
import com.tokopedia.common_category.model.bannedCategory.Data
import com.tokopedia.usecase.RequestParams
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

    private fun getSubCategoryParam(departmentId: String): RequestParams {
        val subCategoryMap = RequestParams()
        subCategoryMap.putString(CategoryNavConstants.IDENTIFIER, departmentId)
        subCategoryMap.putBoolean(CategoryNavConstants.INTERMEDIARY, false)
        subCategoryMap.putBoolean(CategoryNavConstants.SAFESEARCH, false)
        return subCategoryMap
    }

}