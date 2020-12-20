package com.tokopedia.topads.sdk.repository

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.topads.sdk.UrlTopAdsSdk.getTopAdsImageViewUrl
import com.tokopedia.topads.sdk.domain.interactor.DIMEN_ID
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.model.TopAdsmageViewResponse
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type

class TopAdsRepository {

    private val restRepository: RestRepository by lazy { RestRequestInteractor.getInstance().restRepository }

    suspend fun getImageData(queryParams: MutableMap<String, Any>): ArrayList<TopAdsImageViewModel> {

        val response = this.getRestData<TopAdsmageViewResponse>(getTopAdsImageViewUrl(),
                object : TypeToken<TopAdsmageViewResponse>() {}.type,
                queryMap = queryParams)

        return mapToListOfTopAdsImageViewModel(response, queryParams)

    }

    private suspend fun <T : Any> getRestData(url: String,
                                              typeOf: Type,
                                              requestType: RequestType = RequestType.GET,
                                              queryMap: MutableMap<String, Any> = RequestParams.EMPTY.parameters,
                                              cacheType: com.tokopedia.common.network.data.model.CacheType = com.tokopedia.common.network.data.model.CacheType.ALWAYS_CLOUD): T {
        try {
            val restRequest = RestRequest.Builder(url, typeOf)
                    .setRequestType(requestType)
                    .setQueryParams(queryMap)
                    .setCacheStrategy(RestCacheStrategy.Builder(cacheType).build())
                    .build()
            return restRepository.getResponse(restRequest).getData()
        } catch (t: Throwable) {
            throw t
        }
    }

    private fun mapToListOfTopAdsImageViewModel(response: TopAdsmageViewResponse, queryParams: MutableMap<String, Any>): ArrayList<TopAdsImageViewModel> {
        val list = ArrayList<TopAdsImageViewModel>()
        response.data?.forEach{ data ->
            val model = TopAdsImageViewModel()
            val image = getImageById(data?.banner?.images, queryParams[DIMEN_ID] as? Int)
            with(model) {
                bannerId = data?.id.toString()
                bannerName = data?.banner?.name ?: ""
                position = data?.banner?.position ?: 0
                adClickUrl = data?.adClickUrl ?: ""
                adViewUrl = data?.adViewUrl ?: ""
                applink = data?.applinks ?: ""
                imageUrl = image.first
                imageWidth = image.second
                imageHeight = image.third
                nextPageToken = response.header?.pagination?.nextPageToken
            }
            list.add(model)
        }

        return list
    }

    private fun getImageById(images: List<TopAdsmageViewResponse.Data.Banner.Image?>?, dimenId: Int?): Triple<String, Int, Int> {
        var imageUrl = ""
        var imageWidth = 0
        var imageHeight = 0

        images?.let {
            for (image in it) {
                if (image?.imageDimension?.dimenId == dimenId) {
                    imageUrl = image?.fullEcs ?: ""
                    imageWidth = image?.imageDimension?.width ?: 0
                    imageHeight = image?.imageDimension?.height ?: 0
                    break
                }
            }
        }
        return Triple(imageUrl, imageWidth, imageHeight)
    }
}