package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.PdpGetLayout
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.ratesestimate.UserLocationRequest
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.TobacoErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

open class GetPdpLayoutUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<ProductDetailDataModel>() {

    companion object {
        val QUERY = """
            query pdpGetLayout(${'$'}productID : String, ${'$'}shopDomain :String, ${'$'}productKey :String, ${'$'}whID : String, ${'$'}layoutID : String, ${'$'}userLocation: pdpUserLocation) {
              pdpGetLayout(productID:${'$'}productID, shopDomain:${'$'}shopDomain,productKey:${'$'}productKey, apiVersion: 1, whID:${'$'}whID, layoutID:${'$'}layoutID, userLocation:${'$'}userLocation) {
                name
                pdpSession
                basicInfo {
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
                    itemSoldPaymentVerified
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
                      }
                      videos {
                        source
                        url
                      }
            		}
            		... on pdpDataProductContent {
                      name
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
                      content {
                        title
                        subtitle
                        applink
                        showAtFront
                        isAnnotation
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
                      content {
                        icon
                        text
                      }
                    }
                     ... on pdpDataCustomInfo {
                       icon
                       title
                       isApplink
                       applink
                       separator
                       description
                    }
                    ... on pdpDataProductVariant {
                      errorCode
                      parentID
                      defaultChild
                      sizeChart
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
                        price
                        priceFmt
                        sku
                        optionID
                        productName
                        productURL
                        isCOD
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
                    }
                  }
                }
              }
            }
        """.trimIndent()

        fun createParams(productId: String, shopDomain: String, productKey: String, whId: String, layoutId: String, userLocationRequest: UserLocationRequest): RequestParams =
                RequestParams.create().apply {
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                    putString(ProductDetailCommonConstant.PARAM_SHOP_DOMAIN, shopDomain)
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_KEY, productKey)
                    putString(ProductDetailCommonConstant.PARAM_WAREHOUSE_ID, whId)
                    putString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, layoutId)
                    putObject(ProductDetailCommonConstant.PARAM_USER_LOCATION, userLocationRequest)
                }
    }

    var requestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductDetailDataModel {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(GraphqlRequest(QUERY, ProductDetailLayout::class.java, requestParams.parameters))
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