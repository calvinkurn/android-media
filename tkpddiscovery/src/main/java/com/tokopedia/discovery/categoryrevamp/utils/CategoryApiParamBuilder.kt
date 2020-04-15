package com.tokopedia.discovery.categoryrevamp.utils

import com.tokopedia.authentication.AuthHelper
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.common_category.util.ParamMapToUrl
import com.tokopedia.common_category.model.filter.DAFilterQueryType
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession

class CategoryApiParamBuilder {

    companion object {
        val categoryApiParamBuilder: CategoryApiParamBuilder by lazy { CategoryApiParamBuilder() }
    }

    fun generateQuickFilterParam(mDepartmentId: String): RequestParams {
        val quickFilterParam = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = mDepartmentId
        quickFilterParam.putObject(com.tokopedia.common_category.constants.CategoryNavConstants.FILTER, daFilterQueryType)
        quickFilterParam.putString(com.tokopedia.common_category.constants.CategoryNavConstants.SOURCE, "quick_filter")
        return quickFilterParam
    }

    fun generateSubCategoryParam(mDepartmentId: String): RequestParams {
        val subCategoryMap = RequestParams()
        subCategoryMap.putString(com.tokopedia.common_category.constants.CategoryNavConstants.IDENTIFIER, mDepartmentId)
        subCategoryMap.putBoolean(com.tokopedia.common_category.constants.CategoryNavConstants.INTERMEDIARY, false)
        subCategoryMap.putBoolean(com.tokopedia.common_category.constants.CategoryNavConstants.SAFESEARCH, false)
        return subCategoryMap
    }

    fun generateProductListParam(start: Int,
                                 mDepartmentId: String,
                                 userSession: UserSession,
                                 gcmHandler: GCMHandler,
                                 defaultSelectedFilter: HashMap<String, String>,
                                 selectedFilterList: HashMap<String, String>,
                                 selectedSortList: HashMap<String, String>): RequestParams {
        val param = RequestParams.create()


        val searchProductRequestParams = RequestParams.create()
        searchProductRequestParams.putString(com.tokopedia.common_category.constants.CategoryNavConstants.START, (start * 10).toString())
        searchProductRequestParams.putString(com.tokopedia.common_category.constants.CategoryNavConstants.SC, mDepartmentId)
        searchProductRequestParams.putString(com.tokopedia.common_category.constants.CategoryNavConstants.DEVICE, "android")
        searchProductRequestParams.putString(com.tokopedia.common_category.constants.CategoryNavConstants.UNIQUE_ID, getUniqueId(userSession, gcmHandler))
        searchProductRequestParams.putString(com.tokopedia.common_category.constants.CategoryNavConstants.KEY_SAFE_SEARCH, "false")
        searchProductRequestParams.putString(com.tokopedia.common_category.constants.CategoryNavConstants.ROWS, "10")
        searchProductRequestParams.putString(com.tokopedia.common_category.constants.CategoryNavConstants.SOURCE, "search_product")
        if (defaultSelectedFilter.isNotEmpty()) {
            searchProductRequestParams.putAllString(defaultSelectedFilter)
            defaultSelectedFilter.clear()
        } else {
            searchProductRequestParams.putAllString(selectedFilterList)
        }
        searchProductRequestParams.putAllString(selectedSortList)
        param.putString("product_params", createParametersForQuery(searchProductRequestParams.parameters))


        val topAdsRequestParam = RequestParams.create()
        topAdsRequestParam.putString(com.tokopedia.common_category.constants.CategoryNavConstants.KEY_SAFE_SEARCH, "false")
        topAdsRequestParam.putString(com.tokopedia.common_category.constants.CategoryNavConstants.DEVICE, "android")
        topAdsRequestParam.putString(com.tokopedia.common_category.constants.CategoryNavConstants.KEY_SRC, "directory")
        topAdsRequestParam.putString(com.tokopedia.common_category.constants.CategoryNavConstants.KEY_PAGE, start.toString())
        topAdsRequestParam.putString(com.tokopedia.common_category.constants.CategoryNavConstants.KEY_EP, "product")
        topAdsRequestParam.putString(com.tokopedia.common_category.constants.CategoryNavConstants.KEY_ITEM, "2")
        topAdsRequestParam.putString(com.tokopedia.common_category.constants.CategoryNavConstants.KEY_F_SHOP, "1")
        topAdsRequestParam.putString(com.tokopedia.common_category.constants.CategoryNavConstants.KEY_DEPT_ID, mDepartmentId)

        topAdsRequestParam.putAllString(selectedSortList)
        if (defaultSelectedFilter.isNotEmpty()) {
            topAdsRequestParam.putAllString(defaultSelectedFilter)
            defaultSelectedFilter.clear()
        } else {
            topAdsRequestParam.putAllString(selectedFilterList)
        }

        param.putString("top_params", createParametersForQuery(topAdsRequestParam.parameters))
        return param
    }

    fun generateFilterParams(mDepartmentId: String): RequestParams {
        val paramMap = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = mDepartmentId
        paramMap.putString(com.tokopedia.common_category.constants.CategoryNavConstants.SOURCE, "search_product")
        paramMap.putObject(com.tokopedia.common_category.constants.CategoryNavConstants.FILTER, daFilterQueryType)
        paramMap.putString(com.tokopedia.common_category.constants.CategoryNavConstants.Q, "")
        paramMap.putString(com.tokopedia.common_category.constants.CategoryNavConstants.SOURCE, "directory")
        return paramMap

    }

    fun generateGTMDimensionData(selectedFilterList: HashMap<String, String>,
                                 selectedSortList: HashMap<String, String>): String {
        var param = ""
        val filterParam = createParametersForQuery(selectedFilterList)
        val sortParam = createParametersForQuery(selectedSortList)

        if (filterParam.isNotEmpty()) {
            param = filterParam
        }

        if (sortParam.isNotEmpty()) {
            param = if (param.isNotEmpty()) {
                "$param&$sortParam"
            } else {
                sortParam
            }
        }
        return param

    }

     fun getProductItemPath(path: String, id: String): String {
        if (path.isNotEmpty()) {
            return "category/$path - $id"
        }
        return ""
    }

    private fun getUniqueId(userSession: UserSession,
                            gcmHandler: GCMHandler): String {
        return if (userSession.isLoggedIn)
            AuthHelper.getMD5Hash(userSession.userId)
        else
            AuthHelper.getMD5Hash(gcmHandler.registrationId)
    }


    private fun createParametersForQuery(parameters: Map<String, Any>): String {
        return ParamMapToUrl.generateUrlParamString(parameters)
    }
}