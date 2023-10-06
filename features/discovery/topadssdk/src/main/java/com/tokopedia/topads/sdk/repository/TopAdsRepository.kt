package com.tokopedia.topads.sdk.repository

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_SINGLE
import com.tokopedia.topads.sdk.domain.interactor.DIMEN_ID
import com.tokopedia.topads.sdk.domain.model.TopAdsBannerResponse
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

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
      auto_scroll {
        enable
        timer
      }
    }
  }
}"""

@GqlQuery("TopAdsBannerQuery", TOP_ADS_BANNER_QUERY)
open class TopAdsRepository() {

    protected open val graphqlRepository: GraphqlRepository by lazy { GraphqlInteractor.getInstance().graphqlRepository }

    suspend fun getImageData(
        queryParams: MutableMap<String, Any>
    ): ArrayList<TopAdsImageViewModel> {
        val response = this.getGQLData(
            TopAdsBannerResponse::class.java,
            queryParams
        )

        return mapToListOfTopAdsImageViewModel(response, queryParams)
    }

    private suspend fun <T : Any> getGQLData(
        gqlResponseType: Class<T>,
        gqlParams: Map<String, Any>,
        cacheType: CacheType = CacheType.CLOUD_THEN_CACHE
    ): T {
        try {
            val gqlUseCase = GraphqlUseCase<T>(graphqlRepository)
            gqlUseCase.setTypeClass(gqlResponseType)
            gqlUseCase.setGraphqlQuery(GqlQuery)
            gqlUseCase.setRequestParams(mapOf(DISPLAY_BANNER_PARAMS to getQueryMapAsString(gqlParams)))

            gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(cacheType).build())
            return gqlUseCase.executeOnBackground()
        } catch (t: Throwable) {
            throw t
        }
    }

    private fun getQueryMapAsString(queryMap: Map<String, Any>): String {
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
                isAutoScrollEnabled =
                    responseBanner.topadsDisplayBannerAdsV3.header?.autoScroll?.enable ?: false
                scrollDuration =
                    responseBanner.topadsDisplayBannerAdsV3.header?.autoScroll?.timer?.times(1000)
                        ?: 0
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

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "topadsDisplayBannerAdsV3"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}displayBannnerParams: String) {
              $OPERATION_NAME(displayBannnerParams:${'$'}displayBannnerParams){
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
                  auto_scroll {
                    enable
                    timer
                  }
                }
              }
            }
        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

        override fun getQuery(): String = QUERY

        override fun getTopOperationName(): String = OPERATION_NAME
    }
}
