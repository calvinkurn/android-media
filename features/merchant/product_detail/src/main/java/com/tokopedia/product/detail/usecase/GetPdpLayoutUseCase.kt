package com.tokopedia.product.detail.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.PdpGetLayout
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.TobacoErrorException
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
import javax.inject.Inject

open class GetPdpLayoutUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<ProductDetailDataModel>() {

    companion object {
        val QUERY = """
            query pdpGetLayout(${'$'}productID : String, ${'$'}shopDomain :String, ${'$'}productKey :String) {
              pdpGetLayout(productID:${'$'}productID, shopDomain:${'$'}shopDomain,productKey:${'$'}productKey, apiVersion: 1) {
                name
                pdpSession
                basicInfo {
                  alias
                  shopName
                  productID
                  shopID
                  minOrder
                  maxOrder
                  weight
                  weightUnit
                  condition
                  status
                  url
                  sku
                  gtin
                  isMustInsurance
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
                        videoURLIOS
                        isAutoplay
                      }
                      pictures {
                        picID
                        description
                        filePath
                        fileName
                        width
                        height
                        isFromIG
                        urlOriginal
                        urlThumbnail
                        url300
                        Status
                      }
                      videos {
                        source
                        url
                      }
            		}
            		... on pdpDataProductContent {
                      name
                      price {
                        value
                        currency
                        lastUpdateUnix
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
                          lastUpdateUnix
                          currency
                        }
                      }
                      isCashback {
                        percentage
                      }
                      isFreeOngkir {
                        isActive
                        imageURL
                      }
                      preorder {
                        duration
                        timeUnit
                        isActive
                      }
                      isTradeIn
                      isOS
                      isPowerMerchant
                      isWishlist
                      isCOD
            		}
                    ... on pdpDataProductInfo {
                      row
                      content {
                        title
                        subtitle
                        applink
                      }
                    }
                    ... on pdpDataSocialProof {
                      row
                      content {
                        icon
                        title
                        subtitle
                        applink
                        type
                        rating
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
                            url200
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
                        isCOD
                        isWishlist
                        campaignInfo {
                          campaignID
                          campaignType
                          campaignTypeName
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
                      }
                    }
                  }
                }
              }
            }
        """.trimIndent()

        fun createParams(productId: String, shopDomain: String, productKey: String): RequestParams =
                RequestParams.create().apply {
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                    putString(ProductDetailCommonConstant.PARAM_SHOP_DOMAIN, shopDomain)
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_KEY, productKey)
                }
    }

    var requestParams = RequestParams.EMPTY
    var forceRefresh = false
    var enableCaching = false

    override suspend fun executeOnBackground(): ProductDetailDataModel {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(GraphqlRequest(QUERY, ProductDetailLayout::class.java, requestParams.parameters))
        if (enableCaching) {
            gqlUseCase.setCacheStrategy(CacheStrategyUtil.getCacheStrategy(forceRefresh))
        } else {
            gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                    .Builder(CacheType.ALWAYS_CLOUD).build())
        }

        val productId = requestParams.getString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, "")

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error: List<GraphqlError>? = gqlResponse.getError(ProductDetailLayout::class.java)
        val data: PdpGetLayout = gqlResponse.getData<ProductDetailLayout>(ProductDetailLayout::class.java).data
                ?: PdpGetLayout()

        if (gqlResponse.isCached) {
            Timber.w("P2#PDP_CACHE#true;productId=$productId")
        } else {
            Timber.w("P2#PDP_CACHE#false;productId=$productId")
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