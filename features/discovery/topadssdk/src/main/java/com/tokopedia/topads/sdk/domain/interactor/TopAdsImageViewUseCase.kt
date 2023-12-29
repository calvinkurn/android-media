package com.tokopedia.topads.sdk.domain.interactor

import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import kotlin.collections.set

private const val ADS_TYPE = "ep"
private const val DEVICE_TYPE = "device"
private const val SOURCE = "inventory_id"
private const val PAGE_TOKEN = "page_token"
private const val ADS_COUNT = "item"
const val DIMEN_ID = "dimen_id"
private const val USER_ID = "user_id"
private const val DEP_ID = "dep_id"
private const val QUERY = "q"
private const val PRODUCT_ID = "product_id"
private const val PAGE = "page"

class TopAdsImageViewUseCase constructor(
    private val userId: String,
    private val repository: TopAdsRepository,
    private val irisSessionId: String
) {

    suspend fun getImageData(queryParams: MutableMap<String, Any>): ArrayList<TopAdsImageViewModel> {
        return repository.getImageData(queryParams)
    }

    fun getQueryMap(query: String, source: String, pageToken: String, adsCount: Int, dimenId: Int, depId: String, productID: String = "", page: String = ""): MutableMap<String, Any> {
        val queryMap = HashMap<String, Any>()
        queryMap[USER_ID] = userId
        queryMap[ADS_TYPE] = "banner"
        queryMap[DEVICE_TYPE] = "android"
        queryMap[SOURCE] = source
        queryMap[ADS_COUNT] = adsCount
        queryMap[DIMEN_ID] = dimenId
        if (page.isNotEmpty()) queryMap[PAGE] = page
        if (query.isNotEmpty()) queryMap[QUERY] = query
        if (depId.isNotEmpty()) queryMap[DEP_ID] = depId
        if (productID.isNotEmpty()) queryMap[PRODUCT_ID] = productID
        if (pageToken.isNotEmpty()) queryMap[PAGE_TOKEN] = pageToken

        return queryMap
    }
}
