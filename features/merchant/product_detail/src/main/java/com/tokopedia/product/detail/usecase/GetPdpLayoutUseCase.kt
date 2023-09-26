package com.tokopedia.product.detail.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.PdpGetLayout
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.common.data.model.rates.TokoNowParam
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.TobacoErrorException
import com.tokopedia.product.detail.di.RawQueryKeyConstant.NAME_LAYOUT_ID_DAGGER
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

open class GetPdpLayoutUseCase @Inject constructor(
    private val gqlUseCase: MultiRequestGraphqlUseCase,
    @Named(NAME_LAYOUT_ID_DAGGER) private val layoutIdTest: String
) : UseCase<ProductDetailDataModel>() {

    companion object {
        const val QUERY = """
            query pdpGetLayout(${'$'}productID : String, ${'$'}shopDomain :String, ${'$'}productKey :String, ${'$'}whID : String, ${'$'}layoutID : String, ${'$'}userLocation: pdpUserLocation, ${'$'}extParam: String, ${'$'}tokonow: pdpTokoNow) {
              pdpGetLayout(productID:${'$'}productID, shopDomain:${'$'}shopDomain,productKey:${'$'}productKey, apiVersion: 1, whID:${'$'}whID, layoutID:${'$'}layoutID, userLocation:${'$'}userLocation, extParam:${'$'}extParam, tokonow:${'$'}tokonow) {
                requestID
                name
                pdpSession
                basicInfo {
                  shopMultilocation {
                    cityName
                  }
                  isGiftable
                  isTokoNow
                  shopName
                  productID
                  shopID
                  minOrder
                  maxOrder
                  weight
                  weightUnit
                  status
                  url
                  sku
                  needPrescription
                  catalogID
                  isLeasing
                  isBlacklisted
                  totalStockFmt
                  defaultMediaURL
                  menu {
                    id
                    name
                    url
                  }
                  category {
                    id
                    name
                    title
                    breadcrumbURL
                    isAdult
                    isKyc
                    lastUpdateCategory
                    detail {
                      id
                      name
                      breadcrumbURL
                      isAdult
                    }
                  }
                  blacklistMessage {
                    title
                    description
                    button
                    url
                  }
                  txStats {
                    transactionSuccess
                    transactionReject
                    countSold
                    paymentVerified
                    itemSoldFmt
                  }
                  stats {
                    countReview
                    countTalk
                    rating
                  }
                }
                components {
                  name
                  type
                  data {
            		... on pdpDataProductMedia {
            		  media {
                        type
                        URLOriginal
                        URLThumbnail
                        URL300
                        description
                        videoURLAndroid
                        isAutoplay
                        variantOptionID
                        URLMaxRes
                      }
                      recommendation {
                        lightIcon
                        darkIcon
                        iconText
                        bottomsheetTitle
                        recommendation
                      }
                      videos {
                        source
                        url
                      }
                      containerType
            		}
                    ... on pdpDataOnGoingCampaign {
                      campaign {
                        campaignID
                        campaignType
                        campaignTypeName
                        percentageAmount
                        originalPrice
                        discountedPrice
                        stock
                        stockSoldPercentage
                        threshold
                        startDate
                        endDate
                        endDateUnix
                        appLinks
                        isAppsOnly
                        isActive
                        hideGimmick
                        isCheckImei
                        isUsingOvo
                        background
                        campaignIdentifier
                        paymentInfoWording
                      }
                      thematicCampaign{
                        campaignName
                        icon
                        background
                        additionalInfo
                      }
                    }
            		... on pdpDataProductContent {
                      name
                      parentName
                      isCOD
                      price {
                        value
                      }
                      campaign {
                        campaignID
                        campaignType
                        campaignTypeName
                        percentageAmount
                        originalPrice
                        discountedPrice
                        stock
                        stockSoldPercentage
                        threshold
                        startDate
                        endDate
                        endDateUnix
                        appLinks
                        isAppsOnly
                        isActive
                        hideGimmick
                        isCheckImei
                        isUsingOvo
                        background
                        campaignIdentifier
                        paymentInfoWording
                      }
                      thematicCampaign{
                        campaignName
                        icon
                        background
                        additionalInfo
                      }
                      stock {
                        useStock
                        value
                        stockWording
                      }
                      variant {
                        isVariant
                        parentID
                      }
                      wholesale {
                        minQty
                        price {
                          value
                        }
                      }
                      isCashback {
                        percentage
                      }
                      preorder {
                        duration
                        isActive
                        preorderInDays
                      }
                      isTradeIn
                      isOS
                      isPowerMerchant
                      isWishlist
            		}
                    ... on pdpDataProductInfo {
                      row
                      content {
                        title
                        subtitle
                        applink
                      }
                    }
                    ... on pdpDataProductDetail {
                      title
                      content {
                        key
                        type
                        action
                        extParam
                        title
                        subtitle
                        applink
                        infoLink
                        icon
                        showAtFront
                        isAnnotation
                        showAtBottomsheet
                      }
                      catalogBottomsheet {
                        actionTitle
                        bottomSheetTitle
                        param
                      }
                      bottomsheet {
                        actionTitle
                        bottomSheetTitle
                        param
                      }
                    }
                    ... on pdpDataSocialProof {
                      row
                      content {
                        icon
                        title
                        subtitle
                        applink
                      }
                    }
                    ... on pdpDataInfo {
                      icon
                      title
                      isApplink
                      applink
                      lightIcon
                      darkIcon
                      content {
                        icon
                        text
                      }
                    }
                    ... on pdpDataCategoryCarousel {
                        titleCarousel
                        linkText
                        applink
                        list {
                          categoryID
                          icon
                          title
                          isApplink
                          applink
                        }
                    }
                     ... on pdpDataCustomInfo {
                       icon
                       title
                       isApplink
                       applink
                       separator
                       description
                       lightIcon
                       darkIcon
                       label {
                        value
                        color
                      }
                    }
                    ... on pdpDataProductVariant {
                      errorCode
                      parentID
                      defaultChild
                      sizeChart
                      maxFinalPrice
                      landingSubText
                      variants {
                        productVariantID
                        variantID
                        name
                        identifier
                        option {
                          productVariantOptionID
                          variantUnitValueID
                          value
                          hex
                          picture{
                            url
                            url100
                          }
                        }
                      }
                      children {
                        productID
                        subText
                        price
                        priceFmt
                        sku
                        optionID
                        optionName
                        productName
                        productURL
                        isCOD
                        isWishlist
                        picture {
                          url
                          url200
                        }
                        stock {
                          stock
                          isBuyable
                          stockWording
                          stockWordingHTML
                          minimumOrder
                          maximumOrder
                          stockFmt
                          stockCopy
                        }
                        isWishlist
                        campaignInfo {
                          campaignID
                          campaignType
                          campaignTypeName
                          campaignIdentifier
                          background
                          discountPercentage
                          originalPrice
                          discountPrice
                          stock
                          stockSoldPercentage
                          startDate
                          endDateUnix
                          appLinks
                          isAppsOnly
                          isActive
                          hideGimmick
                          isCheckImei
                          isUsingOvo
                          minOrder
                        }
                        thematicCampaign{
                          campaignName
                          icon
                          background
                          additionalInfo
                        }
                      }
                      componentType
                    },
                     ... on pdpDataOneLiner {
                       productID
                       oneLinerContent
                       linkText
                       color
                       applink
                       separator
                       icon
                       isVisible
                       eduLink {
                          appLink
                       }
                    },
                    ... on pdpDataBundleComponentInfo {
                       title
                       widgetType
                       productID
                       whID
                    }
                    ... on pdpDataCustomInfoTitle {
                      title
                      status
                    },
                    ... on pdpDataDynamicOneLiner {
                      name
                      text
                      applink
                      separator
                      icon
                      status
                      chevronPos
                    }
                    ... on pdpDataProductDetailMediaComponent {
                      title
                      description
                      contentMedia {
                        url
                        ratio
                      }
                      show
                      ctaText
                    }
                  }
                }
              }
            }
        """

        fun createParams(
            productId: String,
            shopDomain: String,
            productKey: String,
            whId: String,
            layoutId: String,
            userLocationRequest: UserLocationRequest,
            extParam: String,
            tokonow: TokoNowParam
        ): RequestParams =
            RequestParams.create().apply {
                putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                putString(ProductDetailCommonConstant.PARAM_SHOP_DOMAIN, shopDomain)
                putString(ProductDetailCommonConstant.PARAM_PRODUCT_KEY, productKey)
                putString(ProductDetailCommonConstant.PARAM_WAREHOUSE_ID, whId)
                putString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, layoutId)
                putString(ProductDetailCommonConstant.PARAM_EXT_PARAM, extParam.encodeToUtf8())
                putObject(ProductDetailCommonConstant.PARAM_USER_LOCATION, userLocationRequest)
                putObject(ProductDetailCommonConstant.PARAM_TOKONOW, tokonow)
            }
    }

    var requestParams = RequestParams.EMPTY

    @GqlQuery("PdpGetLayoutQuery", QUERY)
    override suspend fun executeOnBackground(): ProductDetailDataModel {
        gqlUseCase.clearRequest()
        val layoutId = requestParams.getString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, "")
        if (layoutId.isEmpty() && layoutIdTest.isNotBlank()) {
            requestParams.putString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, layoutIdTest)
        }

        gqlUseCase.addRequest(GraphqlRequest(PdpGetLayoutQuery(), ProductDetailLayout::class.java, requestParams.parameters))
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())

        val productId = requestParams.getString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, "")

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error: List<GraphqlError>? = gqlResponse.getError(ProductDetailLayout::class.java)
        val data: PdpGetLayout = gqlResponse.getData<ProductDetailLayout>(ProductDetailLayout::class.java).data
            ?: PdpGetLayout()

        if (gqlResponse.isCached) {
            ServerLogger.log(Priority.P2, "PDP_CACHE", mapOf("type" to "true", "productId" to productId))
        } else {
            ServerLogger.log(Priority.P2, "PDP_CACHE", mapOf("type" to "false", "productId" to productId))
        }
        val blacklistMessage = data.basicInfo.blacklistMessage

        if (error != null && error.isNotEmpty()) {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "), error.firstOrNull()?.extensions?.code.toString())
        } else if (data.basicInfo.isBlacklisted) {
            gqlUseCase.clearCache()
            throw TobacoErrorException(blacklistMessage.description, blacklistMessage.title, blacklistMessage.button, blacklistMessage.url)
        }
        return mapIntoModel(data)
    }

    private fun mapIntoModel(data: PdpGetLayout): ProductDetailDataModel {
        val initialLayoutData = DynamicProductDetailMapper.mapIntoVisitable(data.components)
        val getDynamicProductInfoP1 = DynamicProductDetailMapper.mapToDynamicProductDetailP1(data)
        val p1VariantData = DynamicProductDetailMapper.mapVariantIntoOldDataClass(data)
        return ProductDetailDataModel(getDynamicProductInfoP1, initialLayoutData, p1VariantData)
    }
}
