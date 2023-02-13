package com.tokopedia.topads.sdk.repository

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_SINGLE
import com.tokopedia.topads.sdk.UrlTopAdsSdk.getTopAdsImageViewUrl
import com.tokopedia.topads.sdk.domain.interactor.DIMEN_ID
import com.tokopedia.topads.sdk.domain.model.TopAdsBannerResponse
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type

private const val KEY_IRIS_SESSION_ID = "iris_session_id"
private const val DISPLAY_BANNER_PARAMS = "displayBannnerParams"
private const val TOP_ADS_BANNER_QUERY =
    """query topadsDisplayBannerAdsV3(${'$'}displayBannnerParams: String) {
  topadsDisplayBannerAdsV3(displayBannnerParams:${'$'}displayBannnerParams){
    data {
      id
      ad_ref_key
      ad_view_url
      ad_click_url
      applinks
      banner {
        Name
        Position
        LayoutType
        Images{
          Dimension {
            Id
            Height
            Width
          }
          Url
        }
        CategoryIDs
        Shop
        {
          ShopID
          ShopName
          ShopImage {
            cover
            cover_ecs
            s_ecs
            s_url
            xs_ecs
            xs_url
          }
          ShopDomain
          ShopTagline
          ShopCityName
          ShopIsOfficial
          IsPowerMerchant
          IsPowerMerchantBadge
        }
      } 
    }
    header {
      total_data
      pagination {
        kind
        next_page_token
        current_page
      }
    }
  }
}"""

@GqlQuery("TopAdsBannerQuery", TOP_ADS_BANNER_QUERY)
open class TopAdsRepository {

    protected open val restRepository: RestRepository by lazy { RestRequestInteractor.getInstance().restRepository }

    suspend fun getImageData(
        queryParams: MutableMap<String, Any>,
        sessionId: String
    ): ArrayList<TopAdsImageViewModel> {

        val response = this.postData<TopAdsBannerResponse>(
            getTopAdsImageViewUrl(),
            object : TypeToken<DataResponse<TopAdsBannerResponse>>() {}.type,
            queryMap = queryParams,
            sessionId = sessionId
        )

        return mapToListOfTopAdsImageViewModel(response, queryParams)

    }

    private suspend fun <T : Any> postData(
        url: String,
        typeOf: Type,
        requestType: RequestType = RequestType.POST,
        queryMap: MutableMap<String, Any> = RequestParams.EMPTY.parameters,
        sessionId: String,
        cacheType: com.tokopedia.common.network.data.model.CacheType = com.tokopedia.common.network.data.model.CacheType.ALWAYS_CLOUD
    ): T {
        try {
            val restRequest = RestRequest.Builder(url, typeOf)
                .setRequestType(requestType)
                .setBody(
                    GraphqlRequest(
                        TopAdsBannerQuery.GQL_QUERY,
                        TopAdsBannerResponse::class.java,
                        mapOf(DISPLAY_BANNER_PARAMS to getQueryMapAsString(queryMap))
                    )
                )

                .setHeaders(mapOf(KEY_IRIS_SESSION_ID to sessionId))
                .setCacheStrategy(RestCacheStrategy.Builder(cacheType).build())
                .build()
            val r = restRepository.getResponse(restRequest)
            return r.getData<DataResponse<TopAdsBannerResponse>>().data as T
        } catch (t: Throwable) {
            throw t
        }
    }

    private fun getQueryMapAsString(queryMap: MutableMap<String, Any>): String {
        return queryMap.entries.joinToString("&")
    }

    private fun mapToListOfTopAdsImageViewModel(
        responseBanner: TopAdsBannerResponse,
        queryParams: MutableMap<String, Any>
    ): ArrayList<TopAdsImageViewModel> {
        val list = ArrayList<TopAdsImageViewModel>()
        responseBanner.topadsDisplayBannerAdsV3.bannerListData?.forEach { data ->
            val model = TopAdsImageViewModel()
            val image = getImageById(data.banner?.images, queryParams[DIMEN_ID].toString())
            with(model) {
                bannerId = data.id
                bannerName = data.banner?.name ?: ""
                position = data.banner?.position ?: 0
                ImpressHolder = data.banner?.shop?.shopImage
                layoutType = data.banner?.layoutType ?: TYPE_SINGLE
                adClickUrl = data.adClickUrl ?: ""
                adViewUrl = data.adViewUrl ?: ""
                applink = data.applinks
                imageUrl = image.first
                imageWidth = image.second
                imageHeight = image.third
                nextPageToken =
                    responseBanner.topadsDisplayBannerAdsV3.header?.pagination?.nextPageToken
                shopId = data.banner?.shop?.shopID?.toString() ?: ""
                currentPage =
                    responseBanner.topadsDisplayBannerAdsV3.header?.pagination?.currentPage ?: ""
                kind = responseBanner.topadsDisplayBannerAdsV3.header?.pagination?.kind ?: ""
            }
            list.add(model)
        }

        return list
    }

    private fun getImageById(
        images: List<TopAdsBannerResponse.TopadsDisplayBannerAdsV3.BannerListData.Banner.Image>?,
        dimenId: String?
    ): Triple<String, Int, Int> {
        var imageUrl = ""
        var imageWidth = 0
        var imageHeight = 0

        images?.let {
            for (image in it) {
                if (image.dimension?.id == dimenId) {
                    imageUrl = image.url ?: ""
                    imageWidth = image.dimension?.width ?: 0
                    imageHeight = image.dimension?.height ?: 0
                    break
                }
            }
        }
        return Triple(imageUrl, imageWidth, imageHeight)
    }
}
