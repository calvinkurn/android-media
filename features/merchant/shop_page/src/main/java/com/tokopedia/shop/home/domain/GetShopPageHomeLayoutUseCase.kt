package com.tokopedia.shop.home.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.data.model.ShopLayoutWidgetParamsModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopPageHomeLayoutUseCase @Inject constructor(
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ShopLayoutWidget>() {

    companion object {
        private const val KEY_SHOP_ID = "shopId"
        private const val KEY_STATUS = "status"
        private const val KEY_LAYOUT_ID = "layoutId"
        private const val KEY_DISTRICT_ID = "districtId"
        private const val KEY_CITY_ID = "cityId"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"


        @JvmStatic
        fun createParams(
                paramsModel: ShopLayoutWidgetParamsModel
        ) = mapOf<String, Any>(
                KEY_SHOP_ID to paramsModel.shopId,
                KEY_STATUS to paramsModel.status,
                KEY_LAYOUT_ID to paramsModel.layoutId,
                KEY_DISTRICT_ID to paramsModel.districtId,
                KEY_CITY_ID to paramsModel.cityId,
                KEY_LATITUDE to paramsModel.latitude,
                KEY_LONGITUDE to paramsModel.longitude
        )
    }

    private val query = """
            query get_shop_page_home_layout(${'$'}shopId: String!,${'$'}status:String,${'$'}layoutId:String,${'$'}districtId:String,${'$'}cityId:String,${'$'}latitude:String,${'$'}longitude:String){
              shopPageGetLayout (shopID:${'$'}shopId,status:${'$'}status,layoutID:${'$'}layoutId,districtID:${'$'}districtId,cityID:${'$'}cityId,latitude:${'$'}latitude,longitude:${'$'}longitude){
                layoutID
                masterLayoutID
                merchantTierID
                status
                maxWidgets
                publishDate
                widgets {
                  widgetID
                  layoutOrder
                  name
                  type
                  header {
                    title
                    ctaText
                    ctaLink
                    cover
                    ratio
                    isATC
                  }
                  data {
                    ... on DisplayWidget {
                      imageUrl
                      videoUrl
                      appLink
                      webLink
                    }
                    ... on ProductWidget {
                      productID
                      name
                      imageUrl
                      productUrl
                      displayPrice
                      originalPrice
                      discountPercentage
                      isShowFreeOngkir
                      freeOngkirPromoIcon
                      isSoldOut
                      rating
                      totalReview
                      isPO
                      cashback
                      recommendationType
                      labelGroups {
                          position
                          type
                          title
                          url
                      }
                    }
                    ... on CampaignWidget {
                      campaignID
                      name
                      description
                      startDate
                      endDate
                      statusCampaign
                      timeDescription
                      timeCounter
                      totalNotify
                      totalNotifyWording
                      dynamicRule {
                        descriptionHeader
                        dynamicRoleData{
                         ruleID
                        }
                      }
                      banners {
                        imageID
                        imageURL
                        bannerType
                      }
                      products {
                        id
                        name
                        url
                        urlApps
                        urlMobile
                        imageURL
                        price
                        countSold
                        stock
                        status
                        discountedPrice
                        discountPercentage
                        position
                        stockWording {
                          title
                        }
                        hideGimmick
                        stockSoldPercentage
                        labelGroups {
                          position
                          type
                          title
                          url
                        }
                      }
                    }
                  }
                }
              }
            }
        """.trimIndent()

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ShopLayoutWidget {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.CLOUD_THEN_CACHE).build())

        val gqlRequest = GraphqlRequest(query, ShopLayoutWidget.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ShopLayoutWidget.Response>(ShopLayoutWidget.Response::class.java)
                    .shopLayoutWidget
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun clearCache() {
        gqlUseCase.clearCache()
    }

}