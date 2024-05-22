package com.tokopedia.shop.home.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.home.data.model.ShopLayoutWidgetParamsModel
import com.tokopedia.shop.home.data.model.ShopLayoutWidgetV2
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopPageHomeLayoutV2UseCase @Inject constructor(
    private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ShopLayoutWidgetV2>() {

    companion object {
        private const val KEY_WIDGET_REQUEST = "widgetRequest"
        private const val KEY_SHOP_ID = "shopId"
        private const val KEY_DISTRICT_ID = "districtId"
        private const val KEY_CITY_ID = "cityId"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"

        @JvmStatic
        fun createParams(
            paramsModel: ShopLayoutWidgetParamsModel
        ) = mapOf(
            KEY_WIDGET_REQUEST to paramsModel.listWidgetRequest,
            KEY_SHOP_ID to paramsModel.shopId,
            KEY_DISTRICT_ID to paramsModel.districtId,
            KEY_CITY_ID to paramsModel.cityId,
            KEY_LATITUDE to paramsModel.latitude,
            KEY_LONGITUDE to paramsModel.longitude
        )
    }

    private val query = """
            query get_shop_page_home_layout_v2(${'$'}widgetRequest: [ShopPageWidgetRequest!]!,${'$'}shopId: String!,${'$'}districtId: String,${'$'}cityId: String,${'$'}latitude: String,${'$'}longitude: String) {
              shopPageGetLayoutV2(widgetRequest:${'$'}widgetRequest, shopID:${'$'}shopId, districtID:${'$'}districtId, cityID:${'$'}cityId, latitude:${'$'}latitude, longitude:${'$'}longitude) {
                widgets {
                  widgetID
                  widgetMasterID
                  layoutOrder
                  name
                  type
                  tracker {
                    appLog {
                      requestID
                      sessionID
                      logID
                    }
                  }
                  header {
                    title
                    subtitle
                    ctaText
                    ctaLink
                    cover
                    ratio
                    isATC
                    isShowEtalaseName
                    widgetStyle
                  }
                  data {
                    ... on BundleWidget {
                      bundleGroupID
                      bundleName
                      bundleDetails {
                        bundleID
                        originalPrice
                        displayPrice
                        displayPriceRaw
                        discountPercentage
                        isPO
                        isProductsHaveVariant
                        preorderInfo
                        savingAmountWording
                        minOrder
                        minOrderWording
                      }
                      bundleProducts {
                        productID
                        productName
                        imageUrl
                        appLink
                      }
                    }
                    ... on DisplayWidget {
                      imageUrl
                      videoUrl
                      appLink
                      webLink
                      linkType
                      timeInfo{
                          timeDescription
                          timeCounter
                          startDate
                          endDate
                          bgColor
                          textColor
                          status
                      }
                      campaignID
                      totalNotify
                      totalNotifyWording
                      bannerID
                      dynamicRule {
                        dynamicRoleData {
                          ruleName
                          ruleID
                          isActive
                          ruleAdditionalData
                        }
                        descriptionHeader
                      }
                      text
                      mainBannerPosition
                      showcaseList {
                        showcaseID
                        name
                        imageURL
                        ctaLink
                        isMainBanner
                      }
                      productHotspot {
                        productID
                        name
                        imageUrl
                        productUrl
                        displayPrice
                        isSoldOut
                        coordinate{
                          x
                          y
                        }
                      }
                      warehouseID
                    }
                    ... on EtalaseWidget {
                      imageUrl
                      videoUrl
                      appLink
                      webLink
                      linkID
                      Name
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
                      categoryBreadcrumbs
                      recommendationPageName
                      recommendationType
                      recommendationReference
                      isTopads
                      labelGroups {
                        position
                        type
                        title
                        url
                        styles {
                           key
                           value
                        }
                      }
                      badge {
                        title
                        imageUrl
                        show
                      }
                      warehouseID
                      minimumOrder
                      maximumOrder
                      stock
                      childIDs
                      parentID
                      recParam
                    }
                    ... on PromoWidget {
                      voucherID
                      imageUrl
                      name
                      voucherType {
                        voucherType
                        identifier
                      }
                      voucherCode
                      amount {
                        amountType
                        amount
                        amountFormatted
                      }
                      minimumSpend
                      minimumSpendFormatted
                      owner {
                        ownerID
                        identifier
                      }
                      validThru
                      tnc
                      inUseExpiry
                      status {
                        status
                        identifier
                      }
                    }
                    ... on DynamicComponentWidget {
                      tabLabel
                      tabName
                      componentList {
                        componentID
                        componentName
                        componentType
                        data {
                          linkType
                          link
                          ctaText
                          ctaLink
                          ratio
                          imageID
                          imageURL
                          desktopImageURL
                          isShowProductInfo
                        }
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
                      totalProduct
                      totalProductWording
                      voucherWording
                      dynamicRule {
                        dynamicRoleData {
                          ruleName
                          ruleID
                          isActive
                          ruleAdditionalData
                        }
                        descriptionHeader
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
                          styles {
                            key
                            value
                          }
                        }
                        minimumOrder
                        maximumOrder
                        childIDs
                        parentID
                        showStockbar
                        rating
                        warehouseID
                      }
                      backgroundGradientColor {
                        firstColor
                        secondColor
                      }
                    }
                    ... on DirectPurchaseWidget{
                      ratio
                      Title
                      Banner
                      EtalaseList{
                        imageUrl
                        desktopImageUrl
                        linkType
                        link
                        Name
                      }
                    }                 
                    ... on CampaignOfferingWidget {
                      OfferID
                      OfferName
                      OfferCtaText
                      WarehouseIDs
                      Thumbnails
                      RedirectionOfferURL
                      RedirectionOfferAppLink
                      OfferDetail {
                        TermAndCondition
                        StartDate
                        EndDate
                        TierList {
                          TierID
                          Level
                          TierWording
                          Rules {
                            TypeID
                            Operation
                            Value
                          }
                          Benefit {
                            TypeID
                            Operation
                            Value
                          }
                        }
                      }
                      Products {
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
                          styles {
                            key
                            value
                          }
                        }
                        minimumOrder
                        maximumOrder
                        childIDs
                        isVbs
                        parentID
                        showStockbar
                        rating
                      }
                      TotalProduct
                    }
                  }
                }
              }
            }

    """.trimIndent()

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ShopLayoutWidgetV2 {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.CLOUD_THEN_CACHE).build()
        )

        val gqlRequest = GraphqlRequest(query, ShopLayoutWidgetV2.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ShopLayoutWidgetV2.Response>(ShopLayoutWidgetV2.Response::class.java)
                .shopLayoutWidgetV2
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun clearCache() {
        gqlUseCase.clearCache()
    }
}
