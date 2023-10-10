package com.tokopedia.product.detail.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

open class GetPdpLayoutUseCase @Inject constructor(
    private val gqlUseCase: MultiRequestGraphqlUseCase,
    @Named(NAME_LAYOUT_ID_DAGGER) private val layoutIdTest: String,
    private val remoteConfig: RemoteConfig,
    private val dispatcher: CoroutineDispatchers
) {

    companion object {
        private const val CACHE_EXPIRED = 30L // 30 minutes

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
                    ... on pdpDataProductListComponent {
                      queryParam
                      thematicID
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

    var requestParams: RequestParams = RequestParams.EMPTY

    val shouldCacheable
        get() = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PDP_P1_CACHEABLE)

    private val cacheAge
        get() = remoteConfig.getLong(RemoteConfigKey.ENABLE_PDP_P1_CACHE_AGE, CACHE_EXPIRED).toInt()

    private val layoutId
        get() = requestParams.getString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, "")

    private val refreshPage
        get() = requestParams.getBoolean(ProductDetailCommonConstant.PARAM_REFRESH_PAGE, false)

    // public for unit test only
    val ignoreComponentInCache by lazy {
        listOf(
            ProductDetailConstant.PRODUCT_LIST,
            ProductDetailConstant.VIEW_TO_VIEW,
            ProductDetailConstant.PRODUCT_LIST_VERTICAL,
            ProductDetailConstant.TOP_ADS
        )
    }

    private fun RequestParams.putOrNotLayoutIdTest() {
        if (layoutId.isEmpty() && layoutIdTest.isNotBlank()) {
            putString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, layoutIdTest)
        }
    }

    private fun GraphqlResponse.getPdpLayout() = runCatching {
        getData<ProductDetailLayout>(ProductDetailLayout::class.java).data
    }.getOrNull()

    private fun GraphqlResponse.getPdpLayoutError() = runCatching {
        getError(ProductDetailLayout::class.java)
    }.getOrNull()

    @GqlQuery("PdpGetLayoutQuery", QUERY)
    suspend fun executeOnBackground(): Flow<ProductDetailDataModel> {
        prepareRequest()

        return if (shouldCacheable && !refreshPage) {
            processRequestCacheable()
        } else {
            processRequestAlwaysCloud(
                cacheState = CacheState(cacheFirstThenCloud = false)
            ).let {
                flowOf(it)
            }
        }
    }

    private fun prepareRequest() {
        gqlUseCase.clearRequest()
        requestParams.putOrNotLayoutIdTest()

        gqlUseCase.addRequest(
            GraphqlRequest(
                gqlQueryInterface = PdpGetLayoutQuery(),
                typeOfT = ProductDetailLayout::class.java,
                variables = requestParams.parameters
            )
        )
    }

    private fun processRequestCacheable() = flow {
        val pdpLayoutCache = processRequestCacheOnly()
        var cacheState = pdpLayoutCache.cacheState

        if (cacheState.isFromCache) {
            // is from cache, emit for the first
            emit(pdpLayoutCache)

            cacheState = cacheState.copy(cacheFirstThenCloud = true)
        }

        val pdpLayoutCloud = processRequestAlwaysCloud(cacheState = cacheState)
        emit(pdpLayoutCloud)
    }.flowOn(dispatcher.io)

    private suspend fun processRequestCacheOnly(): ProductDetailDataModel {
        val response = GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY).build().let {
            gqlUseCase.setCacheStrategy(it)
            gqlUseCase.executeOnBackground()
        }
        val error = response.getPdpLayoutError()
        val data = response.getPdpLayout()
        val hasCacheAvailable = error.isNullOrEmpty() && data != null
        val isCampaign = isProductCampaign(layout = data)
        val cacheState = CacheState(isFromCache = false, cacheFirstThenCloud = false)

        // if cache data available and product non campaign, so emit to VM
        return if (hasCacheAvailable && !isCampaign) {
            // expected cache state is fromCache is true and cacheFirstThenCloud is false
            processResponse(
                pdpLayout = data,
                error = error,
                cacheState = cacheState.copy(isFromCache = true),
                isCampaign = false // always false in here
            )
        } else {
            // expected cache state is fromCache is false and cacheFirstThenCloud is false
            ProductDetailDataModel(cacheState = cacheState)
        }
    }

    private suspend fun processRequestAlwaysCloud(cacheState: CacheState): ProductDetailDataModel {
        val response = CacheStrategyUtil.getCacheStrategy(forceRefresh = true, cacheAge = cacheAge)
            .let {
                gqlUseCase.setCacheStrategy(it)
                gqlUseCase.executeOnBackground()
            }
        val data = response.getPdpLayout()
        val error = response.getPdpLayoutError()
        val isCampaign = isProductCampaign(layout = data)

        return processResponse(
            pdpLayout = data,
            error = error,
            cacheState = cacheState.copy(isFromCache = false),
            isCampaign = isCampaign
        )
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

    private fun processResponse(
        pdpLayout: PdpGetLayout?,
        error: List<GraphqlError>?,
        cacheState: CacheState,
        isCampaign: Boolean
    ): ProductDetailDataModel {
        val data = pdpLayout ?: PdpGetLayout()

        if (!error.isNullOrEmpty()) {
            val errorMsg = MessageErrorException(
                error.mapNotNull { it.message }.joinToString(separator = ", "),
                error.firstOrNull()?.extensions?.code.toString()
            )
            throw errorMsg
        } else if (data.basicInfo.isBlacklisted) {
            gqlUseCase.clearCache()
            val tobaccoError = data.basicInfo.blacklistMessage.let {
                TobacoErrorException(
                    messages = it.description,
                    title = it.title,
                    button = it.button,
                    url = it.url
                )
            }
            throw tobaccoError
        }

        return data.mapIntoModel(cacheState = cacheState, isCampaign = isCampaign)
    }

    private fun PdpGetLayout.mapIntoModel(
        cacheState: CacheState,
        isCampaign: Boolean
    ): ProductDetailDataModel {
        val initialLayoutData = DynamicProductDetailMapper.mapIntoVisitable(components)
            .filterNot {
                if (cacheState.isFromCache) {
                    ignoreComponentInCache.contains(it.type())
                } else {
                    false
                }
            }.toMutableList()
        val getDynamicProductInfoP1 = DynamicProductDetailMapper.mapToDynamicProductDetailP1(this)
        val p1VariantData = DynamicProductDetailMapper.mapVariantIntoOldDataClass(this)
        return ProductDetailDataModel(
            layoutData = getDynamicProductInfoP1,
            listOfLayout = initialLayoutData,
            variantData = p1VariantData,
            cacheState = cacheState,
            isCampaign = isCampaign
        )
    }
}
