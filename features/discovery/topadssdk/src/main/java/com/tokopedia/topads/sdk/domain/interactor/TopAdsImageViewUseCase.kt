package com.tokopedia.topads.sdk.domain.interactor

import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.MutableMap
import kotlin.collections.arrayListOf
import kotlin.collections.set

private const val ADS_TYPE = "ep"
private const val DEVICE_TYPE = "device"
private const val SOURCE = "src"
private const val PAGE_TOKEN = "page_token"
private const val ADS_COUNT = "item"
private const val DIMEN_ID = "dimen_id"
private const val USER_ID = "user_id"

class TopAdsImageViewUseCase @Inject constructor(private val userSession: UserSessionInterface) {

    suspend fun getImageData(query: String, queryParmas: Map<String, Any>): TopAdsImageViewResponse {
        //will use to fetch data using BaseRepository
        val response = TopAdsImageViewResponse()
        val images = ArrayList<TopAdsImageViewResponse.Data.Banner.Image>()
        images.add(TopAdsImageViewResponse.Data.Banner.Image("https://ecs7.tokopedia.net/img/cache/100-square/shops-1/2020/2/9/7732831/7732831_e649a20b-611f-4d5f-ab0c-68ec6fbb1b2f.png"))
        images.add(TopAdsImageViewResponse.Data.Banner.Image("https://ecs7.tokopedia.net/img/cache/100-square/shops-1/2020/2/9/7732831/7732831_e649a20b-611f-4d5f-ab0c-68ec6fbb1b2f.png"))
        response.data?.get(0)?.banner?.images = images
        response.data = arrayListOf<TopAdsImageViewResponse.Data>(TopAdsImageViewResponse.Data(banner = TopAdsImageViewResponse.Data.Banner(images = images)))
        return response
    }

    fun getQueryMap(source: String, pageToken: String, adsCount: Int, dimens: String): MutableMap<String, Any> {
        val queryMap = HashMap<String, Any>()
        queryMap[USER_ID] = userSession.userId
        queryMap[ADS_TYPE] = "banner"
        queryMap[DEVICE_TYPE] = "android"
        queryMap[SOURCE] = source
        queryMap[PAGE_TOKEN] = pageToken
        queryMap[ADS_COUNT] = adsCount
        queryMap[DIMEN_ID] = dimens

        return queryMap
    }
}
