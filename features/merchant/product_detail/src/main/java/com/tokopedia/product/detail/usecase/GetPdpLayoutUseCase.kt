package com.tokopedia.product.detail.usecase

import android.content.SharedPreferences
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.CacheState
import com.tokopedia.product.detail.common.data.model.pdplayout.PdpGetLayout
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.rates.TokoNowParam
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailMapper
import com.tokopedia.product.detail.data.util.TobacoErrorException
import com.tokopedia.product.detail.di.ComponentFilter
import com.tokopedia.product.detail.di.RawQueryKeyConstant.NAME_LAYOUT_ID_DAGGER
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.product.detail.view.util.componentDevFilter
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

open class GetPdpLayoutUseCase @Inject constructor(
    private val gqlUseCase: MultiRequestGraphqlUseCase,
    @Named(NAME_LAYOUT_ID_DAGGER) private val layoutIdTest: String,
    private val remoteConfig: RemoteConfig,
    private val dispatcher: CoroutineDispatchers,
    @ComponentFilter private val componentFilterSharedPref: SharedPreferences
) {

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
                  weightWording
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
                      liveIndicator {
                        isLive
                        channelID
                        applink
                        mediaURL
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
                        campaignLogo
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
                        productID
                        campaignLogo
                        applink
                        superGraphicURL
                      }
                    }
            		... on pdpDataProductContent {
                      name
                      parentName
                      labelIcons {
                        label
                        iconURL
                        type
                      }
                      isCOD
                      isShowPrice
                      price {
                        value
                        priceFmt
                        slashPriceFmt
                        discPercentage
                        isPriceMasked
                      }
                      campaign {
                        campaignID
                        campaignType
                        campaignTypeName
                        campaignLogo
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
                        productID
                        campaignLogo
                        applink
                        superGraphicURL
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
                    ... on pdpDataComponentPromoPrice {
                      promo {
                        iconURL
                        promoPriceFmt
                        subtitle
                        applink
                        color
                        background
                        superGraphicURL
                        priceAdditionalFmt
                        separatorColor
                        bottomsheetParam
                        promoCodes{
                          promoID
                          promoCode
                          promoCodeType
                        }
                      }
                      componentPriceType
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
                        slashPriceFmt
                        discPercentage
                        isPriceMasked
                        sku
                        optionID
                        optionName
                        productName
                        labelIcons {
                          label
                          iconURL
                          type
                        }
                        productURL
                        isCOD
                        isWishlist
                        promo {
                          iconURL
                          promoPriceFmt
                          subtitle
                          applink
                          color
                          background
                          superGraphicURL
                          priceAdditionalFmt
                          separatorColor
                          bottomsheetParam
                          promoCodes {
                              promoID
                              promoCode
                              promoCodeType
                          }
                        }
                        componentPriceType
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
                          campaignLogo
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
                            productID
                            campaignLogo
                            applink
                            superGraphicURL
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
                      padding {
                        t
                        b
                      }
                      imageSize{
                        w
                        h
                      }
                    }
                    ... on pdpDataComponentDynamicOneLinerVariant {
                      name
                      text
                      applink
                      separator
                      icon
                      status
                      chevronPos
                      padding {
                        t
                        b
                      }
                      imageSize{
                        w
                        h
                      }
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
                    ... on pdpDataComponentSocialProofV2 {
                        socialProofContent {
                            socialProofType
                            socialProofID
                            title
                            subtitle
                            icon
                            applink {
                                appLink
                            }
                        }
                    }
                    ... on pdpDataComponentSDUIDivKit {
                        template 
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
                putObject(ProductDetailCommonConstant.PARAM_FORCE_REFRESH, refreshPage)
            }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    private val shouldCacheable
        get() = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PDP_P1_CACHEABLE)

    private val cacheAge
        get() = remoteConfig.getLong(
            RemoteConfigKey.ENABLE_PDP_P1_CACHE_AGE,
            CacheStrategyUtil.EXPIRY_TIME_MULTIPLIER
        )

    private val layoutId
        get() = requestParams.getString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, "")

    private val forceRefresh
        get() = requestParams.getBoolean(ProductDetailCommonConstant.PARAM_FORCE_REFRESH, false)

    private val excludeComponentTypeInCache by lazy {
        listOf(
            ProductDetailConstant.PRODUCT_LIST,
            ProductDetailConstant.VIEW_TO_VIEW,
            ProductDetailConstant.PRODUCT_LIST_VERTICAL,
            ProductDetailConstant.TOP_ADS,
            ProductDetailConstant.FINTECH_WIDGET_TYPE,
            ProductDetailConstant.FINTECH_WIDGET_V2_TYPE,
            ProductDetailConstant.CONTENT_WIDGET,
            ProductDetailConstant.GLOBAL_BUNDLING,
            ProductDetailConstant.NOTIFY_ME
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
    suspend fun executeOnBackground(): Flow<Result<ProductDetailDataModel>> = flow {
        prepareRequest()

        val cache = CacheState(remoteCacheableActive = shouldCacheable)
        emit(cache)
    }.flatMapLatest {
        if (shouldCacheable && !forceRefresh) {
            processRequestCacheable(cacheState = it)
        } else {
            flowOf(processRequestAlwaysCloud(cacheState = it))
        }
    }.catch {
        emit(Result.failure(it))
    }.flowOn(dispatcher.io)

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

    private fun processRequestCacheable(cacheState: CacheState) = flow {
        val pdpLayoutStateFromCache = processRequestCacheOnly(cacheState = cacheState)
        // if from cache is null cause throwable, so that get data from cloud
        var pdpLayoutCache =
            pdpLayoutStateFromCache.getOrNull()?.layoutData?.cacheState ?: cacheState

        if (pdpLayoutCache.isFromCache) {
            // is from cache, emit for the first
            emit(pdpLayoutStateFromCache)

            pdpLayoutCache = pdpLayoutCache.copy(cacheFirstThenCloud = true)
        }

        val pdpLayoutCloudState = processRequestAlwaysCloud(cacheState = pdpLayoutCache)
        emit(pdpLayoutCloudState)
    }

    private suspend fun processRequestCacheOnly(cacheState: CacheState) = runCatching {
        val response = GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY).build().let {
            gqlUseCase.setCacheStrategy(it)
            gqlUseCase.executeOnBackground()
        }
        val error = response.getPdpLayoutError()
        val data = response.getPdpLayout()
        val hasCacheAvailable = error.isNullOrEmpty() && data != null
        val isCampaign = isProductCampaign(layout = data)

        // if cache data available and product non campaign, so emit to VM
        if (hasCacheAvailable && !isCampaign) {
            // expected cache state is fromCache is true and cacheFirstThenCloud is false
            processResponse(
                pdpLayout = data,
                error = error,
                cacheState = cacheState.copy(isFromCache = true),
                isCampaign = false // always false in here
            )
        } else {
            // expected cache state is fromCache is false and cacheFirstThenCloud is false
            ProductDetailDataModel(layoutData = ProductInfoP1(cacheState = cacheState))
        }
    }.onSuccess {
        Result.success(it)
    }.onFailure {
        Result.failure<Throwable>(it)
    }

    private suspend fun processRequestAlwaysCloud(cacheState: CacheState) = runCatching {
        val response = CacheStrategyUtil.getCacheStrategy(forceRefresh = true, cacheAge = cacheAge)
            .let {
                gqlUseCase.setCacheStrategy(it)
                gqlUseCase.executeOnBackground()
            }
        val data = response.getPdpLayout()
        val error = response.getPdpLayoutError()
        val isCampaign = isProductCampaign(layout = data)

        processResponse(
            pdpLayout = data,
            error = error,
            cacheState = cacheState.copy(isFromCache = false),
            isCampaign = isCampaign
        )
    }.onSuccess {
        Result.success(it)
    }.onFailure {
        Result.failure<Throwable>(it)
    }

    private fun isProductCampaign(layout: PdpGetLayout?): Boolean {
        val pdpLayout = layout ?: return false

        return pdpLayout.components.any { component ->
            component.componentData.any { it.campaign.isActive }
        }
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
        val getDynamicProductInfoP1 = ProductDetailMapper
            .mapToDynamicProductDetailP1(this)
            .copy(cacheState = cacheState, isCampaign = isCampaign)
        val infiniteRecommendationComponent =
            components.firstOrNull { it.type == ProductDetailConstant.PRODUCT_LIST_VERTICAL }
        val hasInfiniteRecommendation = infiniteRecommendationComponent != null
        val initialLayoutData =
            ProductDetailMapper.mapIntoVisitable(components, getDynamicProductInfoP1)
                .filterNot {
                    if (cacheState.isFromCache) {
                        getIgnoreComponentTypeInCache().contains(it.type())
                    } else {
                        false
                    }
                }.componentDevFilter(componentFilterSharedPref).toMutableList()
        val p1VariantData = ProductDetailMapper
            .mapVariantIntoOldDataClass(this)
        return ProductDetailDataModel(
            layoutData = getDynamicProductInfoP1.copy(
                hasInfiniteRecommendation = hasInfiniteRecommendation,
                infiniteRecommendationPageName = infiniteRecommendationComponent?.componentName
                    ?: "",
                infiniteRecommendationQueryParam = infiniteRecommendationComponent?.componentData?.firstOrNull()?.queryParam.orEmpty()
            ),
            listOfLayout = initialLayoutData,
            variantData = p1VariantData,
        )
    }

    // for hansel-able
    fun getIgnoreComponentTypeInCache() = excludeComponentTypeInCache
}
