package com.tokopedia.topads.sdk.domain.interactor

import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.set

private const val ADS_TYPE = "ep"
private const val DEVICE_TYPE = "device"
private const val SOURCE = "src"
private const val PAGE_TOKEN = "page_token"
private const val ADS_COUNT = "item"
private const val DIMEN_ID = "dimen_id"
private const val USER_ID = "user_id"
private const val DEP_ID = "dep_id"

class TopAdsImageViewUseCase @Inject constructor(private val userSession: UserSessionInterface) {

    suspend fun getImageData(query: String, queryParmas: Map<String, Any>): ArrayList<TopAdsImageViewModel> {
        //will use to fetch data using BaseRepository
        val response = TopAdsImageViewResponse()
        val images = ArrayList<TopAdsImageViewResponse.Data.Banner.Image>()
        images.add(TopAdsImageViewResponse.Data.Banner.Image("https://ecs7.tokopedia.net/img/cache/480/attachment/2020/6/8/27760349/27760349_3916d53a-3472-40b0-b0cc-2662421aee84.jpg"))
        images.add(TopAdsImageViewResponse.Data.Banner.Image("https://ecs7.tokopedia.net/img/cache/480/attachment/2020/6/8/27760349/27760349_3916d53a-3472-40b0-b0cc-2662421aee84.jpg"))
        response.data?.get(0)?.banner?.images = images
        response.data = arrayListOf<TopAdsImageViewResponse.Data>(TopAdsImageViewResponse.Data(banner = TopAdsImageViewResponse.Data.Banner(images = images)),TopAdsImageViewResponse.Data(banner = TopAdsImageViewResponse.Data.Banner(images = images)))


        val response1 = ArrayList<TopAdsImageViewModel>()
        response.data?.forEachIndexed { index, data ->
            val model = TopAdsImageViewModel()
            with(model) {
                adClickUrl = data?.adClickUrl ?: "adsclick"
                adViewUrl = data?.adViewUrl ?: "asview"
                applink = data?.applinks ?: "applink"
                imageUrl = "https://ecs7.tokopedia.net/img/cache/480/attachment/2020/6/8/27760349/27760349_3916d53a-3472-40b0-b0cc-2662421aee84.jpg"
                imageWidth = 400
                imageHeight = 200
            }
            response1.add(model)
        }


        return response1
    }

    fun  getQueryMap(source: String, pageToken: String, adsCount: Int, dimens: String, depId: String): MutableMap<String, Any> {
        val queryMap = HashMap<String, Any>()
        queryMap[USER_ID] = userSession.userId
        queryMap[ADS_TYPE] = "banner"
        queryMap[DEVICE_TYPE] = "android"
        queryMap[SOURCE] = source
        queryMap[PAGE_TOKEN] = pageToken
        queryMap[ADS_COUNT] = adsCount
        queryMap[DIMEN_ID] = dimens
        queryMap[DEP_ID] = depId

        return queryMap
    }
}
