package com.tokopedia.search.result.product.requestparamgenerator

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.utils.getValueString
import com.tokopedia.search.result.product.pagination.Pagination
import com.tokopedia.search.utils.getUserId
import com.tokopedia.topads.sdk.TopAdsConstants
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject

class RequestParamsGenerator @Inject constructor(
    private val userSession: UserSessionInterface,
    private val pagination: Pagination,
) {

    private val userId: String
        get() = getUserId(userSession)

    private val startFrom: Int
        get() = pagination.startFrom

    fun createGetInspirationCarouselChipProductsRequestParams(
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        searchParameter: Map<String, Any>,
        chooseAddressParams: Map<String, String>?,
    ): RequestParams {
        val requestParams = createInitializeSearchParam(
            searchParameter,
            chooseAddressParams,
        )

        requestParams.putString(SearchApiConst.IDENTIFIER, clickedInspirationCarouselOption.identifier)
        requestParams.putString(SearchApiConst.SRP_COMPONENT_ID, clickedInspirationCarouselOption.componentId)

        return requestParams
    }

    fun createRequestDynamicFilterParams(
        searchParameter: Map<String, Any>,
        chooseAddressParams: Map<String, String>?,
    ): RequestParams? {
        val requestParams = RequestParams.create()

        putRequestParamsChooseAddress(requestParams, chooseAddressParams)
        requestParams.putAll(searchParameter)
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_PRODUCT)
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE)

        return requestParams
    }


    fun createGetProductCountRequestParams(
        mapParameter: Map<String, String>,
        chooseAddressParams: Map<String, String>?,
    ): RequestParams {
        val requestParams = createInitializeSearchParam(
            mapParameter,
            chooseAddressParams,
        )

        enrichWithRelatedSearchParam(requestParams)

        requestParams.putString(SearchApiConst.ROWS, "0")

        return requestParams
    }

    fun enrichWithRelatedSearchParam(requestParams: RequestParams) {
        requestParams.putBoolean(SearchApiConst.RELATED, true)
    }

    fun createSearchProductRequestParams(
        requestParams: RequestParams,
        isLocalSearch : Boolean,
        hasFilter: Boolean,
        hasSort: Boolean,
        seenAds: String,
    ): RequestParams {
        val isSkipGlobalNavWidget = isLocalSearch || hasFilter || hasSort
        val isSkipGetLastFilterWidget = hasFilter || hasSort

        return RequestParams.create().apply {
            putObject(SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS, requestParams.parameters)

            putBoolean(SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_PRODUCT_ADS, isLocalSearch)
            putBoolean(SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_HEADLINE_ADS, isLocalSearch)
            putBoolean(SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL, isLocalSearch)
            putBoolean(SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET, isLocalSearch)
            putBoolean(SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_GLOBAL_NAV, isSkipGlobalNavWidget)
            putBoolean(SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_GET_LAST_FILTER_WIDGET, isSkipGetLastFilterWidget)
            putString(TopAdsConstants.SEEN_ADS, seenAds)
        }
    }

    fun createLocalSearchRequestParams(
        navSource : String,
        pageTitle : String,
        pageId : String,
    ): RequestParams =
        RequestParams.create().apply {
            putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
            putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE)
            putString(SearchApiConst.NAVSOURCE, navSource)
            putString(SearchApiConst.SRP_PAGE_TITLE, pageTitle)
            putString(SearchApiConst.SRP_PAGE_ID, pageId)
            putString(SearchApiConst.START, startFrom.toString())
            putString(SearchApiConst.ROWS, getSearchRows())
        }

    fun createInitializeSearchParam(
        searchParameter: Map<String, Any>,
        chooseAddressParams: Map<String, String>?,
    ): RequestParams {
        val requestParams = RequestParams.create()

        putRequestParamsOtherParameters(
            requestParams,
            searchParameter,
        )
        putRequestParamsChooseAddress(requestParams, chooseAddressParams)
        requestParams.putAll(searchParameter)

        return requestParams
    }

    fun createSameSessionRecommendationParam(
        item: ProductItemDataView,
        chooseAddressParams: Map<String, String>?,
    ) : RequestParams {
        val requestParams = RequestParams.create().apply {
            putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE)
            putString(SearchApiConst.PRODUCT_ID, item.productID)
        }
        putRequestParamsChooseAddress(requestParams, chooseAddressParams)

        return requestParams
    }

    private fun putRequestParamsChooseAddress(
        requestParams: RequestParams,
        chooseAddressParams: Map<String, String>?
    ) {
        chooseAddressParams ?: return

        requestParams.putAllString(chooseAddressParams)
    }

    private fun putRequestParamsOtherParameters(
        requestParams: RequestParams,
        searchParameter: Map<String, Any>,
    ) {
        putRequestParamsSearchParameters(requestParams, searchParameter)
        putRequestParamsTopAdsParameters(requestParams)
        putRequestParamsDepartmentIdIfNotEmpty(requestParams, searchParameter)
    }

    private fun putRequestParamsSearchParameters(
        requestParams: RequestParams,
        searchParameter: Map<String, Any>,
    ) {
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE)
        requestParams.putString(SearchApiConst.ROWS, getSearchRows())
        requestParams.putString(SearchApiConst.OB, getSearchSort(searchParameter))
        requestParams.putString(SearchApiConst.START, startFrom.toString())
        requestParams.putString(SearchApiConst.IMAGE_SIZE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE)
        requestParams.putString(SearchApiConst.IMAGE_SQUARE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE)
        requestParams.putString(SearchApiConst.Q, getSearchQuery(searchParameter).omitNewlineAndPlusSign())
        requestParams.putString(SearchApiConst.UNIQUE_ID, getUniqueId())
        requestParams.putString(SearchApiConst.USER_ID, userId)
        requestParams.putString(SearchApiConst.SHOW_ADULT, getShowAdult(searchParameter))
    }

    private fun getShowAdult(searchParameter: Map<String, Any>): String {
        val showAdult = searchParameter.getValueString(SearchApiConst.SHOW_ADULT)
        return showAdult.ifEmpty { SearchApiConst.DEFAULT_VALUE_OF_SHOW_ADULT }
    }

    private fun getSearchRows() = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS

    private fun getSearchSort(searchParameter: Map<String, Any>): String {
        val sort = searchParameter.getValueString(SearchApiConst.OB)
        return if (sort.isNotEmpty()) sort else SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
    }

    private fun getSearchQuery(searchParameter: Map<String, Any>): String {
        return searchParameter
            .getValueString(SearchApiConst.Q)
            .omitNewlineAndPlusSign()
    }

    private fun String.omitNewlineAndPlusSign() =
        replace("\n", "").replace("+", " ")

    private fun getUniqueId(): String {
        return if (userSession.isLoggedIn) AuthHelper.getMD5Hash(userSession.userId)
        else AuthHelper.getMD5Hash(userSession.deviceId)
    }

    private fun putRequestParamsTopAdsParameters(
        requestParams: RequestParams,
    ) {
        requestParams.putInt(TopAdsParams.KEY_ITEM, 2)
        requestParams.putString(TopAdsParams.KEY_EP, TopAdsParams.DEFAULT_KEY_EP)
        requestParams.putString(TopAdsParams.KEY_SRC, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
        requestParams.putBoolean(TopAdsParams.KEY_WITH_TEMPLATE, true)
        requestParams.putInt(TopAdsParams.KEY_PAGE, getTopAdsKeyPage(startFrom))
    }

    private fun getTopAdsKeyPage(startFrom: Int): Int {
        return try {
            val defaultValueStart = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt()
            startFrom / defaultValueStart + 1
        } catch (e: NumberFormatException) {
            Timber.w(e)
            0
        }
    }

    private fun putRequestParamsDepartmentIdIfNotEmpty(
        requestParams: RequestParams,
        searchParameter: Map<String, Any>,
    ) {
        val departmentId = searchParameter.getValueString(SearchApiConst.SC)

        if (departmentId.isNotEmpty()) {
            requestParams.putString(SearchApiConst.SC, departmentId)
            requestParams.putString(TopAdsParams.KEY_DEPARTEMENT_ID, departmentId)
        }
    }
}
