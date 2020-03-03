package com.tokopedia.discovery.categoryrevamp.utils

import com.tokopedia.authentication.AuthHelper
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.filter.DAFilterQueryType
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
        quickFilterParam.putObject(CategoryNavConstants.FILTER, daFilterQueryType)
        quickFilterParam.putString(CategoryNavConstants.SOURCE, "quick_filter")
        return quickFilterParam
    }

    fun generateSubCategoryParam(mDepartmentId: String): RequestParams {
        val subCategoryMap = RequestParams()
        subCategoryMap.putString(CategoryNavConstants.IDENTIFIER, mDepartmentId)
        subCategoryMap.putBoolean(CategoryNavConstants.INTERMEDIARY, false)
        subCategoryMap.putBoolean(CategoryNavConstants.SAFESEARCH, false)
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
        searchProductRequestParams.putString(CategoryNavConstants.START, (start * 10).toString())
        searchProductRequestParams.putString(CategoryNavConstants.SC, mDepartmentId)
        searchProductRequestParams.putString(CategoryNavConstants.DEVICE, "android")
        searchProductRequestParams.putString(CategoryNavConstants.UNIQUE_ID, getUniqueId(userSession, gcmHandler))
        searchProductRequestParams.putString(CategoryNavConstants.KEY_SAFE_SEARCH, "false")
        searchProductRequestParams.putString(CategoryNavConstants.ROWS, "10")
        searchProductRequestParams.putString(CategoryNavConstants.SOURCE, "search_product")
        if (defaultSelectedFilter.isNotEmpty()) {
            searchProductRequestParams.putAllString(defaultSelectedFilter)
            defaultSelectedFilter.clear()
        } else {
            searchProductRequestParams.putAllString(selectedFilterList)
        }
        searchProductRequestParams.putAllString(selectedSortList)
        param.putString("product_params", createParametersForQuery(searchProductRequestParams.parameters))


        val topAdsRequestParam = RequestParams.create()
        topAdsRequestParam.putString(CategoryNavConstants.KEY_SAFE_SEARCH, "false")
        topAdsRequestParam.putString(CategoryNavConstants.DEVICE, "android")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_SRC, "directory")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_PAGE, start.toString())
        topAdsRequestParam.putString(CategoryNavConstants.KEY_EP, "product")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_ITEM, "2")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_F_SHOP, "1")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_DEPT_ID, mDepartmentId)

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
        paramMap.putString(CategoryNavConstants.SOURCE, "search_product")
        paramMap.putObject(CategoryNavConstants.FILTER, daFilterQueryType)
        paramMap.putString(CategoryNavConstants.Q, "")
        paramMap.putString(CategoryNavConstants.SOURCE, "directory")
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