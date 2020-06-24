package com.tokopedia.topads.sdk.repository

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.topads.sdk.domain.interactor.DIMEN_ID
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.model.TopAdsmageViewResponse

import javax.inject.Inject

private const val BASE_URL = "https://ta-staging.tokopedia.com/v1.3/display"

class TopAdsRepository @Inject constructor() : BaseRepository() {

    suspend fun getImageData(queryParams: MutableMap<String, Any>): ArrayList<TopAdsImageViewModel> {

        val response = this.getRestData<TopAdsmageViewResponse>(BASE_URL,
                object : TypeToken<TopAdsmageViewResponse>() {}.type,
                queryMap = queryParams)

        return mapToListOfTopAdsImageViewModel(response, queryParams)

    }

    private fun mapToListOfTopAdsImageViewModel(response: TopAdsmageViewResponse, queryParams: MutableMap<String, Any>): ArrayList<TopAdsImageViewModel> {
        val list = ArrayList<TopAdsImageViewModel>()
        response.data?.forEach{ data ->
            val model = TopAdsImageViewModel()
            val image = getImageById(data?.banner?.images, queryParams[DIMEN_ID] as? Int)
            with(model) {
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