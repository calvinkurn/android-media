package com.tokopedia.product.detail.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.PdpGetLayout
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.common.data.model.rates.TokoNowParam
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.data.model.datamodel.CacheState
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.TobacoErrorException
import com.tokopedia.product.detail.di.RawQueryKeyConstant.NAME_LAYOUT_ID_DAGGER
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

open class GetPdpLayoutUseCase @Inject constructor(
    private val gqlUseCase: MultiRequestGraphqlUseCase,
    @Named(NAME_LAYOUT_ID_DAGGER) private val layoutIdTest: String,
    private val remoteConfig: RemoteConfig
) : UseCase<Unit>() {

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
            tokonow: TokoNowParam,
            refreshPage: Boolean
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
                putObject(ProductDetailCommonConstant.PARAM_REFRESH_PAGE, refreshPage)
            }
    }

    val shouldCacheable
        get() = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PDP_P1_CACHEABLE)

    private val cacheAge
        get() = remoteConfig.getLong(
            RemoteConfigKey.ENABLE_PDP_P1_CACHE_AGE,
            CacheStrategyUtil.EXPIRY_TIME_MULTIPLIER.toLong()
        ).toInt()

    var requestParams = RequestParams.EMPTY

    var onSuccess: (suspend (ProductDetailDataModel) -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null

    @GqlQuery("PdpGetLayoutQuery", QUERY)
    override suspend fun executeOnBackground() {
        gqlUseCase.clearRequest()
        val layoutId = requestParams.getString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, "")
        val refreshPage = requestParams.getBoolean(ProductDetailCommonConstant.PARAM_REFRESH_PAGE, false)
        if (layoutId.isEmpty() && layoutIdTest.isNotBlank()) {
            requestParams.putString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, layoutIdTest)
        }
        gqlUseCase.addRequest(GraphqlRequest(PdpGetLayoutQuery(), ProductDetailLayout::class.java, requestParams.parameters))

        if (shouldCacheable && !refreshPage) {
            processRequestCacheable()
        } else {
            gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())

            val gqlResponse = gqlUseCase.executeOnBackground()
            processResponse(gqlResponse = gqlResponse, isCache = false, cacheFirstThenCloud = false)
        }
    }

    private suspend fun processRequestCacheable() {
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY).build())
        var gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ProductDetailLayout::class.java)
        val data = runCatching {
            gqlResponse.getData<ProductDetailLayout>(ProductDetailLayout::class.java).data
        }.getOrNull()
        val hasCacheAvailable = error.isNullOrEmpty() && data != null
        var cacheFirstThenCloud = false // initial value is false, refer to CacheState
        val isCampaign = isProductCampaign(layout = data)

        // if cache data available and product non campaign, so emit to VM
        if (hasCacheAvailable && !isCampaign) {
            processResponse(gqlResponse = gqlResponse, isCache = true, cacheFirstThenCloud = false)
            cacheFirstThenCloud = true
        }

        // hit cloud to update into cache and UI
        val cacheStrategy = CacheStrategyUtil.getCacheStrategy(forceRefresh = true, cacheAge = cacheAge)
        gqlUseCase.setCacheStrategy(cacheStrategy)
        gqlResponse = gqlUseCase.executeOnBackground()
        processResponse(gqlResponse = gqlResponse, isCache = false, cacheFirstThenCloud = cacheFirstThenCloud)
    }

    private fun isProductCampaign(layout: PdpGetLayout?): Boolean {
        var hasCampaign = false

        for (component in layout?.components.orEmpty()) {
            if (hasCampaign) return true

            when (component.type) {
                ProductDetailConstant.ONGOING_CAMPAIGN -> {
                    hasCampaign = component.componentData.firstOrNull()
                        ?.campaign
                        ?.isActive
                        .orFalse()
                }
                ProductDetailConstant.PRODUCT_CONTENT -> {
                    hasCampaign = component.componentData.firstOrNull()
                        ?.campaign
                        ?.isActive
                        .orFalse()
                }
            }
        }

        return hasCampaign
    }

    private suspend fun processResponse(gqlResponse: GraphqlResponse, isCache: Boolean, cacheFirstThenCloud: Boolean) {
        val productId = requestParams.getString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, "")
        val error: List<GraphqlError>? = gqlResponse.getError(ProductDetailLayout::class.java)
        val data: PdpGetLayout = gqlResponse.getData<ProductDetailLayout>(ProductDetailLayout::class.java).data
            ?: PdpGetLayout()

        if (gqlResponse.isCached) {
            ServerLogger.log(Priority.P2, "PDP_CACHE", mapOf("type" to "true", "productId" to productId))
        } else {
            ServerLogger.log(Priority.P2, "PDP_CACHE", mapOf("type" to "false", "productId" to productId))
        }
        val blacklistMessage = data.basicInfo.blacklistMessage

        if (!error.isNullOrEmpty()) {
            val error = MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "), error.firstOrNull()?.extensions?.code.toString())
            onError?.invoke(error)
            return
        } else if (data.basicInfo.isBlacklisted) {
            gqlUseCase.clearCache()
            val error = TobacoErrorException(blacklistMessage.description, blacklistMessage.title, blacklistMessage.button, blacklistMessage.url)
            onError?.invoke(error)
            return
        }

        val dataModel = mapIntoModel(data, isCache, cacheFirstThenCloud)
        onSuccess?.invoke(dataModel)
    }

    private fun mapIntoModel(data: PdpGetLayout, isCache: Boolean, cacheFirstThenCloud: Boolean): ProductDetailDataModel {
        val initialLayoutData = DynamicProductDetailMapper.mapIntoVisitable(data.components)
            .filterNot {
                if (isCache) {
                    ignoreComponentInCache.contains(it.type())
                } else {
                    false
                }
            }.toMutableList()
        val getDynamicProductInfoP1 = DynamicProductDetailMapper.mapToDynamicProductDetailP1(data)
        val p1VariantData = DynamicProductDetailMapper.mapVariantIntoOldDataClass(data)
        val cacheState = CacheState(isFromCache = isCache, cacheFirstThenCloud = cacheFirstThenCloud)
        return ProductDetailDataModel(
            layoutData = getDynamicProductInfoP1,
            listOfLayout = initialLayoutData,
            variantData = p1VariantData,
            cacheState = cacheState
        )
    }

    private val ignoreComponentInCache by lazy {
        listOf(
            ProductDetailConstant.PRODUCT_LIST,
            ProductDetailConstant.VIEW_TO_VIEW,
            ProductDetailConstant.PRODUCT_LIST_VERTICAL,
            ProductDetailConstant.TOP_ADS
        )
    }
}
