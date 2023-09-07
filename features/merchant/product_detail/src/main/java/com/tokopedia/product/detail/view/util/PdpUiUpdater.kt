package com.tokopedia.product.detail.view.util

import android.content.Context
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.pdp.fintech.view.FintechPriceDataModel
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.ar.ProductArInfo
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirImage
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductMediaRecomBasicInfo
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimateData
import com.tokopedia.product.detail.common.data.model.rates.ShipmentPlus
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.datamodel.ArButtonDataModel
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicOneLinerDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaContainerType
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel
import com.tokopedia.product.detail.data.model.datamodel.OngoingCampaignDataModel
import com.tokopedia.product.detail.data.model.datamodel.PdpComparisonWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.PdpRecommendationWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoTitleDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDiscussionMostHelpfulDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMerchantVoucherSummaryDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniShopWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofStockDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMostHelpfulReviewDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecomWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationVerticalDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationVerticalPlaceholderDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopAdditionalDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTickerInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopAdsImageDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopadsHeadlineUiModel
import com.tokopedia.product.detail.data.model.datamodel.UpcomingNplDataModel
import com.tokopedia.product.detail.data.model.datamodel.ViewToViewWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.asMediaContainerType
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductShopReviewDataModel
import com.tokopedia.product.detail.data.model.purchaseprotection.PPItemDetailPage
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpful
import com.tokopedia.product.detail.data.model.ticker.TickerDataResponse
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.model.upcoming.ProductUpcomingData
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PDP_7
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PDP_9_TOKONOW
import com.tokopedia.product.detail.data.util.ProductDetailConstant.RECOM_VERTICAL
import com.tokopedia.product.detail.data.util.ProductDetailConstant.VIEW_TO_VIEW
import com.tokopedia.product.detail.view.viewholder.a_plus_content.APlusImageUiModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModels
import com.tokopedia.recommendation_widget_common.extension.toViewToViewItemModels
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlin.math.roundToLong

/**
 * This class hold all of the ViewHolder data. They have same instance.
 * If you changes one of this variable , data inside ViewHolder also updated (don't forget to notify)
 */
class PdpUiUpdater(var mapOfData: MutableMap<String, DynamicPdpDataModel>) {

    private val miniSocialProofMap: ProductMiniSocialProofDataModel?
        get() = mapOfData[ProductDetailConstant.MINI_SOCIAL_PROOF] as? ProductMiniSocialProofDataModel

    private val miniSocialProofStockMap: ProductMiniSocialProofStockDataModel?
        get() = mapOfData[ProductDetailConstant.MINI_SOCIAL_PROOF_STOCK] as? ProductMiniSocialProofStockDataModel

    val basicContentMap: ProductContentDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_CONTENT] as? ProductContentDataModel

    val productDiscussionMostHelpfulMap: ProductDiscussionMostHelpfulDataModel?
        get() = mapOfData[ProductDetailConstant.DISCUSSION_FAQ] as? ProductDiscussionMostHelpfulDataModel

    val productReviewMap: ProductMostHelpfulReviewDataModel?
        get() = mapOfData[ProductDetailConstant.REVIEW] as? ProductMostHelpfulReviewDataModel

    val fintechWidgetMap: FintechWidgetDataModel?
        get() = mapOfData[ProductDetailConstant.FINTECH_WIDGET_NAME] as? FintechWidgetDataModel

    val productTradeinMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.TRADE_IN] as? ProductGeneralInfoDataModel

    val productInstallmentInfoMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_INSTALLMENT_INFO] as? ProductGeneralInfoDataModel

    val productProtectionMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_PROTECTION] as? ProductGeneralInfoDataModel

    val infoObatKerasMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.INFO_OBAT_KERAS] as? ProductGeneralInfoDataModel

    val productSingleVariant: ProductSingleVariantDataModel?
        get() = mapOfData[ProductDetailConstant.MINI_VARIANT_OPTIONS] as? ProductSingleVariantDataModel

    val notifyMeMap: ProductNotifyMeDataModel?
        get() = mapOfData[ProductDetailConstant.UPCOMING_DEALS] as? ProductNotifyMeDataModel

    val mediaMap: ProductMediaDataModel?
        get() = mapOfData[ProductDetailConstant.MEDIA] as? ProductMediaDataModel

    val tickerInfoMap: ProductTickerInfoDataModel?
        get() = mapOfData[ProductDetailConstant.TICKER_INFO] as? ProductTickerInfoDataModel

    val shopCredibility: ProductShopCredibilityDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY] as? ProductShopCredibilityDataModel

    val miniShopWidgetMap: ProductMiniShopWidgetDataModel?
        get() = mapOfData[ProductDetailConstant.MINI_SHOP_WIDGET] as? ProductMiniShopWidgetDataModel

    val topAdsImageData: TopAdsImageDataModel?
        get() = mapOfData[ProductDetailConstant.KEY_TOP_ADS] as? TopAdsImageDataModel

    val shipmentData: ProductShipmentDataModel?
        get() = mapOfData[ProductDetailConstant.SHIPMENT] as? ProductShipmentDataModel

    val shipmentV2Data: ProductShipmentDataModel?
        get() = mapOfData[ProductDetailConstant.SHIPMENT_V2] as? ProductShipmentDataModel

    val mvcSummaryData: ProductMerchantVoucherSummaryDataModel?
        get() = mapOfData[ProductDetailConstant.MVC] as? ProductMerchantVoucherSummaryDataModel

    val bestSellerData: OneLinersDataModel?
        get() = mapOfData[ProductDetailConstant.BEST_SELLER] as? OneLinersDataModel

    val stockAssuranceData: OneLinersDataModel?
        get() = mapOfData[ProductDetailConstant.STOCK_ASSURANCE] as? OneLinersDataModel

    val productDetailInfoData: ProductDetailInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_DETAIL] as? ProductDetailInfoDataModel

    val topAdsProductBundlingData: TopadsHeadlineUiModel?
        get() = mapOfData[ProductDetailConstant.SHOPADS_CAROUSEL] as? TopadsHeadlineUiModel

    val contentWidgetData: ContentWidgetDataModel?
        get() = mapOfData[ProductDetailConstant.PLAY_CAROUSEL] as? ContentWidgetDataModel

    val dilayaniTokopedia: ProductShopAdditionalDataModel?
        get() = mapOfData[ProductDetailConstant.DILAYANI_TOKOPEDIA] as? ProductShopAdditionalDataModel

    val productArData: ArButtonDataModel?
        get() = mapOfData[ProductDetailConstant.AR_BUTTON] as? ArButtonDataModel

    private val verticalRecommendationItems =
        mutableListOf<ProductRecommendationVerticalDataModel>()

    val otherOffers: ProductCustomInfoTitleDataModel?
        get() = mapOfData[ProductDetailConstant.OTHER_OFFERS] as? ProductCustomInfoTitleDataModel

    val shopReview: ProductShopReviewDataModel?
        get() = mapOfData[ProductDetailConstant.SHOP_REVIEW] as? ProductShopReviewDataModel

    val ongoingCampaignData: OngoingCampaignDataModel?
        get() = mapOfData[ProductDetailConstant.ONGOING_CAMPAIGN] as? OngoingCampaignDataModel

    fun updateDataP1(
        dataP1: DynamicProductInfoP1?,
        loadInitialData: Boolean = false
    ) {
        dataP1?.let {
            updateData(ProductDetailConstant.PRODUCT_CONTENT, loadInitialData) {
                basicContentMap?.run {
                    data = ProductContentMainData(
                        campaign = it.data.campaign,
                        thematicCampaign = it.data.thematicCampaign,
                        cashbackPercentage = it.data.isCashback.percentage,
                        price = it.data.price,
                        stockWording = it.data.stock.stockWording,
                        isVariant = it.data.variant.isVariant,
                        productName = it.data.name,
                        isProductActive = it.basic.isActive()
                    )
                    data?.campaign?.originalPriceFmt =
                        it.data.campaign.originalPrice.getCurrencyFormatted()
                    data?.campaign?.discountedPriceFmt =
                        it.data.campaign.discountedPrice.getCurrencyFormatted()
                    data?.price?.priceFmt = it.data.price.value.getCurrencyFormatted()

                    shouldShowCampaign = ongoingCampaignData == null
                    isWishlisted = it.data.isWishlist
                }
            }

            updateData(ProductDetailConstant.ONGOING_CAMPAIGN, loadInitialData) {
                ongoingCampaignData?.apply {
                    data = ProductContentMainData(
                        campaign = it.data.campaign,
                        thematicCampaign = it.data.thematicCampaign,
                        cashbackPercentage = it.data.isCashback.percentage,
                        price = it.data.price,
                        stockWording = it.data.stock.stockWording,
                        isVariant = it.data.variant.isVariant,
                        productName = it.data.name,
                        isProductActive = it.basic.isActive()
                    ).apply {
                        campaign.originalPriceFmt = campaign.originalPrice.getCurrencyFormatted()
                        campaign.discountedPriceFmt = campaign.discountedPrice.getCurrencyFormatted()
                    }
                }
            }

            updateData(ProductDetailConstant.MINI_VARIANT_OPTIONS, loadInitialData) {
                productSingleVariant?.run {
                    isRefreshing = false
                }
            }

            updateData(ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY, loadInitialData) {
                shopCredibility?.run {
                    isOs = it.data.isOS
                    isPm = it.data.isPowerMerchant
                    shopLocation = it.basic.productMultilocation.cityName
                }
            }

            updateData(ProductDetailConstant.MINI_SOCIAL_PROOF_STOCK, loadInitialData) {
                miniSocialProofStockMap?.run {
                    rating = it.basic.stats.rating
                    ratingCount = it.basic.stats.countReview.toIntOrZero()
                    stock = it.basic.totalStockFmt
                    itemSoldFmt = it.basic.txStats.itemSoldFmt
                }
            }

            updateData(ProductDetailConstant.SHIPMENT, loadInitialData) {
                shipmentData?.isTokoNow = it.basic.isTokoNow
            }

            updateData(ProductDetailConstant.SHIPMENT_V2, loadInitialData) {
                shipmentV2Data?.isTokoNow = it.basic.isTokoNow
            }

            val productId = it.basic.productID

            dataP1.bestSellerContent?.let { bestSellerInfoContent ->
                if (bestSellerInfoContent.contains(productId)) {
                    updateBestSellerData(dataP1 = dataP1, productId = productId)
                } else {
                    updateBestSellerData()
                }
            }

            updateData(ProductDetailConstant.STOCK_ASSURANCE) {
                dataP1.stockAssuranceContent?.get(productId)?.let { content ->
                    stockAssuranceData?.oneLinersContent = content
                }
            }

            updateData(ProductDetailConstant.SHOPADS_CAROUSEL, loadInitialData) {
                topAdsProductBundlingData?.run {
                    this.productId = dataP1.basic.productID
                }
            }

            if (loadInitialData) {
                updateVerticalRecommendationWidget(productId)
                verticalRecommendationItems.clear()
            }
        }
    }

    fun updateInitialMedia(
        media: List<Media>,
        containerType: String,
        productMediaRecomBasicInfo: ProductMediaRecomBasicInfo
    ) {
        updateData(ProductDetailConstant.MEDIA, true) {
            mediaMap?.let {
                it.initialScrollPosition = if (mediaMap?.initialScrollPosition == -1) -1 else 0
                it.listOfMedia = DynamicProductDetailMapper.convertMediaToDataModel(
                    media.toMutableList()
                )
                it.containerType = if (GlobalConfig.isSellerApp()) {
                    MediaContainerType.Square
                } else {
                    containerType.asMediaContainerType()
                }
                it.recommendation = DynamicProductDetailMapper.convertRecomToDataModel(
                    productMediaRecomBasicInfo
                )
            }
        }
    }

    fun updateMediaScrollPosition(selectedOptionId: String?) {
        if (selectedOptionId == null) return
        updateData(ProductDetailConstant.MEDIA) {
            mediaMap?.apply {
                if (this.variantOptionIdScrollAnchor != selectedOptionId) {
                    this.variantOptionIdScrollAnchor = selectedOptionId
                    this.shouldUpdateImage = true
                }
            }
        }
    }

    private fun updateBestSellerData(
        dataP1: DynamicProductInfoP1? = null,
        productId: String? = null
    ) {
        updateData(ProductDetailConstant.BEST_SELLER) {
            bestSellerData?.run {
                if (dataP1 == null) {
                    this.oneLinersContent = null
                } else {
                    dataP1.bestSellerContent?.let {
                        this.oneLinersContent = it[productId]
                    }
                }
            }
        }
    }

    private fun updateDataTradein(context: Context?, tradeinResponse: ValidateTradeIn) {
        updateData(ProductDetailConstant.TRADE_IN) {
            productTradeinMap?.run {
                subtitle = if (tradeinResponse.usedPrice.toIntOrZero() > 0) {
                    context?.getString(
                        com.tokopedia.common_tradein.R.string.text_price_holder,
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            tradeinResponse.usedPrice.toIntOrZero(),
                            true
                        )
                    ).orEmpty()
                    context?.getString(
                        com.tokopedia.common_tradein.R.string.text_price_holder,
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            tradeinResponse.usedPrice.toIntOrZero(),
                            true
                        )
                    ).orEmpty()
                } else if (tradeinResponse.widgetString.isNotBlank()) {
                    tradeinResponse.widgetString
                } else {
                    context?.getString(com.tokopedia.common_tradein.R.string.trade_in_exchange)
                        .orEmpty()
                }
            }
        }
    }

    fun updateFintechData(
        selectedProductId: String,
        variantData: ProductVariant?,
        productInfo: DynamicProductInfoP1?,
        loggedIn: Boolean
    ) {
        productInfo?.let { productDetail ->
            val productIdToPriceURLMap = HashMap<String, FintechPriceDataModel>()
            val productCategoryId: String = productDetail.basic.category.id
            if (variantData == null) {
                productIdToPriceURLMap[productDetail.basic.productID] =
                    FintechPriceDataModel(productDetail.data.price.value.toString())
            } else {
                for (i in variantData.children.indices) {
                    productIdToPriceURLMap[variantData.children[i].productId] =
                        FintechPriceDataModel(
                            variantData.children[i].price.toString()
                        )
                }
            }

            updateData(ProductDetailConstant.FINTECH_WIDGET_NAME) {
                fintechWidgetMap?.run {
                    productId = selectedProductId
                    categoryId = productCategoryId
                    idToPriceUrlMap = productIdToPriceURLMap
                    isLoggedIn = loggedIn
                    shopId = productDetail.basic.shopID
                    parentId = variantData?.parentId ?: productId
                }
            }
        }
    }

    fun updateFintechDataWithProductId(selectedProductId: String, loggedIn: Boolean) {
        updateData(ProductDetailConstant.FINTECH_WIDGET_NAME) {
            fintechWidgetMap?.run {
                productId = selectedProductId
                isLoggedIn = loggedIn
            }
        }
    }

    fun updateWishlistData(isWishlisted: Boolean) {
        updateData(ProductDetailConstant.PRODUCT_CONTENT) {
            basicContentMap?.isWishlisted = isWishlisted
        }
    }

    fun updateDataP2(
        context: Context?,
        p2Data: ProductInfoP2UiData,
        productId: String,
        boeImageUrl: String
    ) {
        p2Data.let {
            updateData(ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY) {
                shopCredibility?.run {
                    shopLastActive =
                        if (context == null) {
                            ""
                        } else {
                            it.shopInfo.shopLastActive.getRelativeDate(context)
                        }
                    shopName = it.shopInfo.shopCore.name
                    shopAva = it.shopInfo.shopAssets.avatar
                    partnerLabel = it.shopInfo.partnerLabel
                    shopWarehouseCount = it.shopInfo.shopMultilocation.warehouseCount
                    shopWarehouseApplink = it.shopInfo.shopMultilocation.eduLink.applink
                    shopTierBadgeUrl = it.shopInfo.shopTierBadgeUrl
                    infoShopData = if (context == null) {
                        listOf()
                    } else {
                        getTwoShopInfoHieararchy(
                            context,
                            it.shopSpeed,
                            it.shopChatSpeed.toLongOrZero(),
                            it.shopInfo.activeProduct.toLongOrZero(),
                            it.shopInfo.createdInfo.shopCreated,
                            it.shopRating,
                            it.shopFinishRate
                        )
                    }
                    tickerDataResponse = it.shopInfo.tickerData
                }
            }

            updateData(ProductDetailConstant.MINI_SHOP_WIDGET) {
                miniShopWidgetMap?.run {
                    val shopInfo = it.shopInfo
                    shopName = shopInfo.shopCore.name
                    shopLocation = shopInfo.location
                    shopAva = shopInfo.shopAssets.avatar
                    shopBadge = shopInfo.shopTierBadgeUrl
                }
            }

            updateData(ProductDetailConstant.MINI_SOCIAL_PROOF) {
                updateMiniSocialProof(it)
            }

            updateData(ProductDetailConstant.MINI_SOCIAL_PROOF_STOCK) {
                miniSocialProofStockMap?.run {
                    wishlistCount = it.wishlistCount.toIntOrZero()
                    viewCount = it.productView.toIntOrZero()
                    shouldRenderSocialProof = true
                    buyerPhotosCount = it.imageReview.buyerMediaCount
                    buyerPhotoStaticText = it.imageReview.staticSocialProofText
                    setSocialProofData()
                }
            }

            updateData(ProductDetailConstant.MVC) {
                mvcSummaryData?.run {
                    uiModel = uiModel.copy(
                        animatedInfo = it.merchantVoucherSummary.animatedInfos,
                        isShown = it.merchantVoucherSummary.isShown,
                        shopId = it.shopInfo.shopCore.shopID,
                        productIdMVC = productId,
                        additionalData = it.merchantVoucherSummary.additionalData
                    )
                }
            }

            // old installment component
            if (it.productFinancingRecommendationData.data.partnerCode.isNotBlank()) {
                updateData(ProductDetailConstant.PRODUCT_INSTALLMENT_INFO) {
                    productInstallmentInfoMap?.run {
                        subtitle = String.format(
                            context?.getString(R.string.new_installment_template).orEmpty(),
                            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                it.productFinancingRecommendationData.data.monthlyPrice.roundToLong(),
                                false
                            )
                        )
                    }
                }
            } else {
                removeComponent(ProductDetailConstant.PRODUCT_INSTALLMENT_INFO)
            }

            updatePurchaseProtectionData(it.productPurchaseProtectionInfo.ppItemDetailPage)
            updateNotifyMeAndContent(
                productId,
                it.upcomingCampaigns,
                it.validateTradeIn.isEligible,
                boeImageUrl
            )
            updateDataTradein(context, it.validateTradeIn)
            updateData(ProductDetailConstant.REVIEW) {
                productReviewMap?.run {
                    review = it.helpfulReviews?.firstOrNull()
                    mediaThumbnails = it.imageReview.reviewMediaThumbnails
                    formattedRating = it.rating.ratingScore
                    totalRatingCount = it.rating.totalRating
                    totalReviewCount = it.rating.totalReviewTextAndImage
                }
            }

            if (it.ticker.tickerInfo.isEmpty()) {
                removeComponent(ProductDetailConstant.TICKER_INFO)
            } else {
                updateTicker(it.getTickerByProductId(productId))
            }

            updateData(ProductDetailConstant.DILAYANI_TOKOPEDIA) {
                dilayaniTokopedia?.apply {
                    title = it.shopAdditional.title
                    description = it.shopAdditional.subTitle
                    icon = it.shopAdditional.icon
                    labels = it.shopAdditional.label
                    appLink = it.shopAdditional.appLink
                    linkText = it.shopAdditional.linkText
                }
            }

            updateArData(productId, it.arInfo)

            updateData(ProductDetailConstant.INFO_OBAT_KERAS) {
                infoObatKerasMap?.apply {
                    val newAppLink = it.obatKeras.applink
                    isApplink = newAppLink.isNotEmpty()
                    applink = newAppLink
                    subtitle = it.obatKeras.subtitle
                    isPlaceholder = false
                }
            }

            updateData(ProductDetailConstant.OTHER_OFFERS) {
                updateCustomInfoTitleP2(p2 = it)
            }

            updateData(ProductDetailConstant.SHOP_REVIEW) {
                updateReviewList(it)
            }
            updateDynamicOneLiner(it)
        }
    }

    private fun updateMiniSocialProof(p2Data: ProductInfoP2UiData) {
        if (p2Data.socialProof.isEmpty()) {
            removeComponent(ProductDetailConstant.MINI_SOCIAL_PROOF)
        } else {
            miniSocialProofMap?.shouldRender = true
            val previousData = miniSocialProofMap?.items.orEmpty()
            miniSocialProofMap?.items = p2Data.socialProof.map { uiModel ->
                uiModel.copy( // retain impress-holder
                    impressHolder = previousData.find {
                        it.identifier == uiModel.identifier
                    }?.impressHolder ?: ImpressHolder()
                )
            }
        }
    }

    private fun updateReviewList(p2Data: ProductInfoP2UiData) {
        if (p2Data.shopReview.reviews.isEmpty()) {
            removeComponent(ProductDetailConstant.SHOP_REVIEW)
        } else {
            shopReview?.shouldRender = true
            shopReview?.data = p2Data.shopReview
        }
    }

    fun updateArData(selectedProductId: String, arInfo: ProductArInfo) {
        updateData(ProductDetailConstant.AR_BUTTON) {
            if (arInfo.isProductIdContainsAr(selectedProductId)) {
                productArData?.applink = arInfo.applink
                productArData?.message = arInfo.message
                productArData?.imageUrl = arInfo.imageUrl
            } else {
                productArData?.message = ""
            }
        }
    }

    fun updateTicker(data: List<TickerDataResponse>?) {
        updateData(ProductDetailConstant.TICKER_INFO) {
            tickerInfoMap?.run {
                tickerDataResponse = if (data != null && data.isNotEmpty()) {
                    data
                } else {
                    listOf()
                }
            }
        }
    }

    /**
     * @param ppItemData : Holds Purchase Protection data from getPdpData GQL
     * title             : either ppItemData.title | ppItemData.titlePDP
     * contentList       : first element = subtitle, second element: insurance partner details
     * rendered in a vertical recycler view
     */
    private fun updatePurchaseProtectionData(ppItemData: PPItemDetailPage) {
        updateData(ProductDetailConstant.PRODUCT_PROTECTION) {
            productProtectionMap?.run {
                applink = ppItemData.linkURL.ifNullOrBlank { applink }
                title = ppItemData.titlePDP.ifNullOrBlank { ppItemData.title.orEmpty() }
                subtitle = ppItemData.subTitlePDP.ifNullOrBlank { ppItemData.subTitle.orEmpty() }
                additionalIcon = ppItemData.partnerLogo ?: ""
                additionalDesc = ppItemData.partnerText ?: ""
                isApplink = ppItemData.isAppLink ?: false
            }
        }
    }

    fun updateNotifyMeButton(notifyMe: Boolean) {
        updateData(ProductDetailConstant.UPCOMING_DEALS) {
            notifyMeMap?.notifyMe = !notifyMe
        }
    }

    private fun updateNotifyMeAndContent(
        productId: String,
        upcomingData: Map<String, ProductUpcomingData>?,
        eligibleTradein: Boolean,
        freeOngkirImgUrl: String
    ) {
        updateData(ProductDetailConstant.PRODUCT_CONTENT) {
            basicContentMap?.run {
                val selectedUpcoming = upcomingData?.get(productId)
                upcomingNplData = UpcomingNplDataModel(
                    selectedUpcoming?.upcomingType.orEmpty(),
                    selectedUpcoming?.campaignTypeName.orEmpty(),
                    selectedUpcoming?.startDate.orEmpty()
                )
                shouldShowTradein = if (productTradeinMap == null) false else eligibleTradein
                this.freeOngkirImgUrl = freeOngkirImgUrl
            }
        }

        updateUpcoming(productId, upcomingData)

        updateData(ProductDetailConstant.ONGOING_CAMPAIGN) {
            ongoingCampaignData?.run {
                val selectedUpcoming = upcomingData?.get(productId)
                upcomingNplData = UpcomingNplDataModel(
                    selectedUpcoming?.upcomingType.orEmpty(),
                    selectedUpcoming?.campaignTypeName.orEmpty(),
                    selectedUpcoming?.startDate.orEmpty()
                )
            }
        }
    }

    private fun updateUpcoming(
        productId: String,
        upcomingData: Map<String, ProductUpcomingData>?
    ) {
        updateData(ProductDetailConstant.UPCOMING_DEALS) {
            notifyMeMap?.run {
                val selectedUpcoming = upcomingData?.get(productId)
                campaignID = selectedUpcoming?.campaignId ?: ""
                campaignType = selectedUpcoming?.campaignType ?: ""
                campaignTypeName = selectedUpcoming?.campaignTypeName ?: ""
                startDate = selectedUpcoming?.startDate ?: ""
                notifyMe = selectedUpcoming?.notifyMe ?: false
                bgColorUpcoming = selectedUpcoming?.bgColorUpcoming ?: ""
                upcomingNplData = UpcomingNplDataModel(
                    upcomingType = selectedUpcoming?.upcomingType.orEmpty(),
                    ribbonCopy = selectedUpcoming?.campaignTypeName.orEmpty(),
                    startDate = selectedUpcoming?.startDate.orEmpty()
                )
            }
        }
    }

    /**
     * Use this only when update variant, because no need to update tradein when variant changed
     */
    fun updateNotifyMeAndContent(
        productId: String,
        upcomingData: Map<String, ProductUpcomingData>?,
        freeOngkirImgUrl: String
    ) {
        updateData(ProductDetailConstant.PRODUCT_CONTENT) {
            basicContentMap?.run {
                val selectedUpcoming = upcomingData?.get(productId)
                upcomingNplData = UpcomingNplDataModel(
                    selectedUpcoming?.upcomingType.orEmpty(),
                    selectedUpcoming?.campaignTypeName.orEmpty(),
                    selectedUpcoming?.startDate.orEmpty()
                )
                this.freeOngkirImgUrl = freeOngkirImgUrl
            }
        }

        updateUpcoming(productId, upcomingData)

        updateData(ProductDetailConstant.ONGOING_CAMPAIGN) {
            ongoingCampaignData?.run {
                val selectedUpcoming = upcomingData?.get(productId)
                upcomingNplData = UpcomingNplDataModel(
                    selectedUpcoming?.upcomingType.orEmpty(),
                    selectedUpcoming?.campaignTypeName.orEmpty(),
                    selectedUpcoming?.startDate.orEmpty()
                )
            }
        }
    }

    fun updateDataP2General(data: ProductInfoP2Other?) {
        data?.let {
            updateData(ProductDetailConstant.DISCUSSION_FAQ) {
                productDiscussionMostHelpfulMap?.run {
                    if (it.discussionMostHelpful == null) {
                        isShimmering = true
                    } else {
                        it.discussionMostHelpful?.let {
                            questions = it.questions
                            totalQuestion = it.totalQuestion
                            isShimmering = false
                        }
                    }
                }
            }
        }
    }

    fun updateRecommendationData(data: RecommendationWidget) {
        updateData(data.pageName) {
            when (data.pageName) {
                PDP_7, PDP_9_TOKONOW -> {
                    (mapOfData[data.pageName] as? ProductRecomWidgetDataModel)?.run {
                        recomWidgetData = data
                        cardModel =
                            data.recommendationItemList.toProductCardModels()
                        filterData = mapToAnnotateChip(data)
                    }
                }
                else -> {
                    (mapOfData[data.pageName] as? ProductRecommendationDataModel)?.run {
                        recomWidgetData = data
                        cardModel = data.recommendationItemList.toProductCardModels()
                        filterData = mapToAnnotateChip(data)
                    }
                }
            }
        }
    }

    fun updateViewToViewData(
        data: RecommendationWidget?,
        state: Int = RecommendationCarouselData.STATE_READY
    ) {
        val pageName = data?.pageName ?: return
        updateData(pageName) {
            if (pageName == VIEW_TO_VIEW) {
                (mapOfData[pageName] as? ViewToViewWidgetDataModel)?.run {
                    recomWidgetData = data
                    cardModel = data?.recommendationItemList?.toViewToViewItemModels()
                    this.state = state
                }
            }
        }
    }

    fun updateVerticalRecommendationData(data: RecommendationWidget) {
        updateData(data.pageName) {
            (mapOfData[data.pageName] as? ProductRecommendationVerticalPlaceholderDataModel)?.apply {
                if (recomWidgetData?.currentPage != data.currentPage) {
                    verticalRecommendationItems.addAll(getItemDataModels())
                }
                recomWidgetData = data
            }
        }
    }

    fun getVerticalRecommendationPlaceholder(pageName: String) =
        mapOfData[pageName] as? ProductRecommendationVerticalPlaceholderDataModel

    fun getVerticalRecommendationNextPage(pageName: String): Int? {
        val dataModel = (mapOfData[pageName] as? ProductRecommendationVerticalPlaceholderDataModel)
        return dataModel?.recomWidgetData?.nextPage
    }

    fun removeComponentP2Data(it: ProductInfoP2UiData, countReview: String) {
        if (it.ratesEstimate.isEmpty()) {
            removeComponent(ProductDetailConstant.SHIPMENT_V2)
            removeComponent(ProductDetailConstant.SHIPMENT)
        }

        if (!it.validateTradeIn.isEligible) {
            removeComponent(ProductDetailConstant.TRADE_IN)
        }

        if (!it.merchantVoucherSummary.isShown) {
            removeComponent(ProductDetailConstant.MVC)
        }

        if (!it.productPurchaseProtectionInfo.ppItemDetailPage.isProtectionAvailable) {
            removeComponent(ProductDetailConstant.PRODUCT_PROTECTION)
        }

        if (it.upcomingCampaigns.values.isEmpty()) {
            removeComponent(ProductDetailConstant.NOTIFY_ME)
        }

        if (!it.shopCommitment.isNowActive) {
            removeComponent(ProductDetailConstant.ORDER_PRIORITY)
        }

        if (it.helpfulReviews?.isEmpty() == true && countReview.toIntOrZero() == 0) {
            removeComponent(ProductDetailConstant.REVIEW)
        }

        if (it.shopAdditional.isEmpty()) {
            removeComponent(ProductDetailConstant.DILAYANI_TOKOPEDIA)
        }
    }

    fun removeEmptyRecommendation(data: RecommendationWidget) {
        removeComponent(data.pageName)
    }

    fun updateComparisonDataModel(data: RecommendationWidget) {
        val recomModel = mapOfData[data.pageName] as? PdpComparisonWidgetDataModel
        if (recomModel != null) {
            updateData(data.pageName) {
                (mapOfData[data.pageName] as? PdpComparisonWidgetDataModel)?.run {
                    recommendationWidget = data
                }
            }
        } else {
            updateData(data.pageName) {
                mapOfData[data.pageName] = PdpComparisonWidgetDataModel(
                    "",
                    data.pageName,
                    data
                )
            }
        }
    }

    fun updateComparisonBpcDataModel(data: RecommendationWidget) {
        updateData(data.pageName) {
            mapOfData[data.pageName] = PdpRecommendationWidgetDataModel(
                recommendationWidgetModel = RecommendationWidgetModel(
                    widget = data,
                    metadata = RecommendationWidgetMetadata(
                        pageSource = RecommendationWidgetSource.PDP.xSourceValue,
                        pageName = data.pageName
                    ),
                    trackingModel = RecommendationWidgetTrackingModel(
                        androidPageName = RecommendationWidgetSource.PDP.trackingValue
                    )
                )
            )
        }
    }

    fun updateFilterRecommendationData(data: ProductRecommendationDataModel) {
        updateData(data.name) {
            (mapOfData[data.name] as? ProductRecommendationDataModel)?.run {
                filterData = data.filterData
                recomWidgetData = data.recomWidgetData
                cardModel = data.recomWidgetData?.recommendationItemList
                    ?.toProductCardModels().orEmpty()
            }
        }
    }

    /**
     * this function should call when first render variant and update from vbs only
     */
    fun updateVariantData(title: String, processedVariant: List<VariantCategory>?) {
        updateData(ProductDetailConstant.MINI_VARIANT_OPTIONS) {
            val variantLvlOne = processedVariant?.firstOrNull()
            productSingleVariant?.title = title
            productSingleVariant?.variantLevelOne = doRetainImpressOfVariantOptions(variantLvlOne)
        }
    }

    fun updateVariantSelected(mapOfSelectedIds: MutableMap<String, String>?) {
        if (mapOfSelectedIds == null) return
        updateData(ProductDetailConstant.MINI_VARIANT_OPTIONS) {
            productSingleVariant?.let {
                it.mapOfSelectedVariant = mapOfSelectedIds
            }
        }
    }

    fun updateSingleVariant(singleVariant: ProductSingleVariantDataModel?) {
        if (singleVariant == null) {
            removeComponent(ProductDetailConstant.MINI_VARIANT_OPTIONS)
        } else {
            updateData(ProductDetailConstant.MINI_VARIANT_OPTIONS) {
                mapOfData[ProductDetailConstant.MINI_VARIANT_OPTIONS] =
                    retainImpressOfSingleVariantOptions(newSingleVariant = singleVariant)
            }
        }
    }

    /**
     * get single variant of variant options`s impress holder object from previous data if available
     */
    private fun retainImpressOfSingleVariantOptions(
        newSingleVariant: ProductSingleVariantDataModel
    ): ProductSingleVariantDataModel {
        val variantLevelOne = newSingleVariant.variantLevelOne
        val variantLevelOneOfImpressRetained = doRetainImpressOfVariantOptions(variantLevelOne)

        return newSingleVariant.copy(variantLevelOne = variantLevelOneOfImpressRetained)
    }

    /**
     * do retain for variant options`s impress holder object from previous data if available
     */
    private fun doRetainImpressOfVariantOptions(newVariantOptions: VariantCategory?): VariantCategory? {
        val previousVariantOptions = productSingleVariant?.variantLevelOne?.variantOptions.orEmpty()
        val variantOptions = newVariantOptions?.variantOptions.orEmpty().map { new ->
            new.copy(
                impressHolder = previousVariantOptions
                    .find { it.variantId == new.variantId }
                    ?.impressHolder.apply {
                        this?.invoke()
                    } ?: ImpressHolder()
            )
        }

        return newVariantOptions?.copy(
            variantOptions = variantOptions
        )
    }

    fun updateDiscussionData(discussionMostHelpful: DiscussionMostHelpful) {
        updateData(ProductDetailConstant.DISCUSSION_FAQ) {
            productDiscussionMostHelpfulMap?.run {
                questions = discussionMostHelpful.questions
                totalQuestion = discussionMostHelpful.totalQuestion
                isShimmering = false
            }
        }
    }

    fun successUpdateShopFollow(isFavorite: Boolean) {
        updateData(ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY) {
            shopCredibility?.isFavorite = !isFavorite
        }
    }

    fun updateShopFollow(isFollow: Int) {
        updateData(ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY) {
            shopCredibility?.isFavorite = isFollow == ProductDetailConstant.ALREADY_FAVORITE_SHOP
        }
    }

    fun failUpdateShopFollow() {
        updateData(ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY) {
            shopCredibility?.enableButtonFavorite = true
        }
    }

    fun updateTopAdsImageData(data: ArrayList<TopAdsImageViewModel>) {
        updateData(ProductDetailConstant.KEY_TOP_ADS) {
            topAdsImageData?.data = data
        }
    }

    fun updateRecomTokonowQuantityData(miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>?) {
        mapOfData.filterValues { it is ProductRecommendationDataModel }.keys.forEach { key ->
            val productRecom = (mapOfData[key] as ProductRecommendationDataModel).copy()
            productRecom.recomWidgetData?.let { recomData ->
                if (recomData.hasQuantityEditor()) {
                    updateRecomWidgetQtyDataFromMiniCart(recomData.copy(), miniCart, key)
                }
            }
        }
    }

    private fun updateRecomWidgetQtyDataFromMiniCart(
        recomWidget: RecommendationWidget,
        miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>?,
        key: String
    ) {
        val dataList = recomWidget.copyRecomItemList()
        dataList.forEach { recomItem ->
            // update data based on tokonow cart
            if (recomItem.addToCartType == RecommendationItem.AddToCartType.QuantityEditor) {
                recomItem.setDefaultCurrentStock()
                miniCart?.let { cartData ->
                    recomItem.updateItemCurrentStock(
                        when {
                            recomItem.isProductHasParentID() -> {
                                getTotalQuantityVariantBasedOnParentID(recomItem, miniCart)
                            }
                            cartData.containsKey(recomItem.productId.toString()) -> {
                                cartData[recomItem.productId.toString()]?.quantity ?: 0
                            }
                            else -> 0
                        }
                    )
                }
            }
        }
        updateData(key) {
            updateRecomDataByKey(key, dataList)
        }
    }

    private fun getTotalQuantityVariantBasedOnParentID(
        recomItem: RecommendationItem,
        miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>
    ): Int {
        var variantTotalItems = 0
        miniCart.values.forEach { miniCartItem ->
            if (miniCartItem.productParentId == recomItem.parentID.toString()) {
                variantTotalItems += miniCartItem.quantity
            }
        }
        return variantTotalItems
    }

    fun updateCurrentQuantityRecomItem(recommendationItem: RecommendationItem) {
        val key = recommendationItem.pageName
        (mapOfData[key] as ProductRecommendationDataModel).recomWidgetData?.let { recomData ->
            recomData.recommendationItemList.forEach loop@{ recomItem ->
                if (recomItem.productId == recommendationItem.productId) {
                    recomItem.currentQuantity = recommendationItem.currentQuantity
                    return@loop
                }
            }
        }
    }

    fun resetFailedRecomTokonowCard(recommendationItem: RecommendationItem) {
        val key = recommendationItem.pageName
        val productRecom = (mapOfData[key] as ProductRecommendationDataModel).copy()
        productRecom.recomWidgetData?.let { recomData ->
            val recomWidget = recomData.copy()
            val dataList = recomWidget.copyRecomItemList()
            var dataUpdated = false
            dataList.forEach loop@{ recomItem ->
                if (recomItem.productId == recommendationItem.productId) {
                    recomItem.onFailedUpdateCart()
                    dataUpdated = true
                    return@loop
                }
            }
            if (dataUpdated) {
                updateRecomDataByKey(key, dataList)
            }
        }
    }

    private fun updateRecomDataByKey(key: String, dataList: List<RecommendationItem>) {
        val newData = copyPDPRecomByKey(key)
        newData.cardModel = dataList.toProductCardModels(false)
        newData.recomWidgetData?.recommendationItemList = dataList
        mapOfData[key] = newData
    }

    private fun copyPDPRecomByKey(key: String): ProductRecommendationDataModel {
        val data = mapOfData[key] as ProductRecommendationDataModel
        return ProductRecommendationDataModel().cloneData(data)
    }

    fun updateShipmentData(
        data: P2RatesEstimateData?,
        isFullfillment: Boolean,
        isCod: Boolean,
        freeOngkirData: BebasOngkirImage,
        userLocationLocalData: LocalCacheModel,
        shipmentPlus: ShipmentPlus?
    ) {
        // pair.first boType, pair.second boImage
        updateData(ProductDetailConstant.SHIPMENT) {
            shipmentData?.rates = data ?: P2RatesEstimateData()
            shipmentData?.isFullfillment = isFullfillment
            shipmentData?.isCod = isCod
            shipmentData?.shouldShowShipmentError = data == null
            shipmentData?.freeOngkirType = freeOngkirData.boType
            shipmentData?.freeOngkirUrl = freeOngkirData.imageURL
            shipmentData?.tokoCabangIconUrl = freeOngkirData.tokoCabangImageURL
            shipmentData?.shipmentPlusData = DynamicProductDetailMapper.mapToShipmentPlusData(
                shipmentPlus,
                freeOngkirData.boType
            )
        }

        updateData(ProductDetailConstant.SHIPMENT_V2) {
            shipmentV2Data?.apply {
                this.rates = data ?: P2RatesEstimateData()
                this.isFullfillment = isFullfillment
                this.isCod = isCod
                this.shouldShowShipmentError = data == null
                this.freeOngkirType = freeOngkirData.boType
                this.freeOngkirUrl = freeOngkirData.imageURL
                this.tokoCabangIconUrl = freeOngkirData.tokoCabangImageURL
                this.shipmentPlusData = DynamicProductDetailMapper.mapToShipmentPlusData(
                    shipmentPlus,
                    freeOngkirData.boType
                )
            }
        }
    }

    fun updatePlayWidget(playWidgetState: PlayWidgetState) {
        updateData(ProductDetailConstant.PLAY_CAROUSEL) {
            contentWidgetData?.playWidgetState = playWidgetState
        }
    }

    fun removeComponent(key: String) {
        if (key.isNotEmpty()) {
            mapOfData.remove(key)
        }
    }

    private fun mapToAnnotateChip(data: RecommendationWidget): List<AnnotationChip> {
        return data.recommendationFilterChips.map {
            AnnotationChip(it)
        }
    }

    private fun updateData(
        key: String,
        loadInitialData: Boolean = false,
        updateAction: () -> Unit
    ) {
        if (!loadInitialData) {
            val data = mapOfData[key]
            data?.let {
                val newInstance = it.newInstance()
                if (it.impressHolder.isInvoke) {
                    newInstance.impressHolder.invoke()
                }
                mapOfData[key] = newInstance
            }
        }
        updateAction.invoke()
    }

    /**
     * Return a new [List] of [DynamicPdpDataModel] filled with components that will be showed on PDP.
     * Only call this to supply a list of components for [com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment.submitInitialList].
     *
     * @param aPlusContentExpanded A [Boolean] indicating whether A+ content section is expanded or not.
     */
    fun getInitialItems(aPlusContentExpanded: Boolean): List<DynamicPdpDataModel> {
        return mapOfData
            .values
            .toMutableList()
            .adjustAPlusMedia(aPlusContentExpanded)
            .toList()
    }

    /**
     * Return a new [List] of [DynamicPdpDataModel] filled with components that will be showed on PDP.
     * Only call this to supply a list of components for [com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment.submitList].
     *
     * @param aPlusContentExpanded A [Boolean] indicating whether A+ content section is expanded or not.
     */
    fun getCurrentDataModels(aPlusContentExpanded: Boolean): List<DynamicPdpDataModel> {
        return mapOfData
            .values
            .toMutableList()
            .adjustAPlusMedia(aPlusContentExpanded)
            .addVerticalRecommendation()
            .toList()
    }

    private fun MutableList<DynamicPdpDataModel>.addVerticalRecommendation(): MutableList<DynamicPdpDataModel> {
        val indexVerticalRecommendation = indexOfLast {
            it is ProductRecommendationVerticalPlaceholderDataModel
        }

        if (indexVerticalRecommendation != -1) {
            verticalRecommendationItems.forEachIndexed { index, item ->
                item.position = index + 1
            }
            addAll(indexVerticalRecommendation + 1, verticalRecommendationItems)
        }
        return this
    }

    private fun MutableList<DynamicPdpDataModel>.adjustAPlusMedia(
        expanded: Boolean
    ): MutableList<DynamicPdpDataModel> {
        if (!expanded) removeAll { it is APlusImageUiModel && !it.showOnCollapsed }
        return this
    }

    private fun updateCustomInfoTitleP2(p2: ProductInfoP2UiData) {
        otherOffers?.apply {
            p2.customInfoTitle.forEach {
                when (it.componentName) {
                    ProductDetailConstant.OTHER_OFFERS -> {
                        if (status == ProductCustomInfoTitleDataModel.Status.PLACEHOLDER) {
                            title = it.title
                            status = ProductCustomInfoTitleDataModel.Status.fromString(it.status)
                        }
                    }
                }
            }
        }
    }

    private fun updateVerticalRecommendationWidget(productId: String) {
        mapOfData.forEach { (key, data) ->
            if (key.startsWith(RECOM_VERTICAL)) {
                updateData(key, true) {
                    mapOfData[key] = (data as? PdpRecommendationWidgetDataModel)?.run {
                        updateVerticalRecommendationWidgetProductId(productId)
                    } ?: data
                }
            }
        }
    }

    private fun PdpRecommendationWidgetDataModel.updateVerticalRecommendationWidgetProductId(
        productId: String
    ): PdpRecommendationWidgetDataModel {
        return copy(
            recommendationWidgetModel = recommendationWidgetModel.copy(
                metadata = recommendationWidgetModel.metadata.copy(
                    productIds = if (productId.isBlank()) listOf() else listOf(productId)
                )
            )
        )
    }

    private fun updateDynamicOneLiner(p2: ProductInfoP2UiData) {
        val dynamicOneLiner = p2.dynamicOneLiner
        dynamicOneLiner.forEach { item ->
            val dataModel = mapOfData[item.name] as? DynamicOneLinerDataModel ?: return@forEach
            mapOfData[item.name] = (dataModel.newInstance() as DynamicOneLinerDataModel).apply {
                data = DynamicOneLinerDataModel.Data(
                    text = item.text,
                    applink = item.applink,
                    separator = item.separator,
                    icon = item.icon,
                    status = item.status,
                    chevronPos = item.chevronPos
                )
            }
        }
    }

    fun updateInitialAPlusContent(
        productInfo: DynamicProductInfoP1,
        userID: String,
        aPlusContentExpanded: Boolean
    ) {
        val lastShowedAPlusMediaName = findLastShowedAPlusMediaName(aPlusContentExpanded)
        val mediaCount = getAPlusMediaCount()
        var mediaPosition = 0
        mapOfData.forEach { (key, _) ->
            updateData(key, true) {
                (mapOfData[key] as? APlusImageUiModel)?.apply {
                    mediaPosition++

                    expanded = aPlusContentExpanded
                    haveBottomPadding = name() == lastShowedAPlusMediaName
                    trackerData = APlusImageUiModel.TrackerData(
                        mediaCount = mediaCount,
                        mediaPosition = mediaPosition,
                        mediaUrl = url,
                        expanded = aPlusContentExpanded,
                        userID = userID,
                        shopID = productInfo.basic.shopID,
                        productID = productInfo.basic.productID,
                        layoutName = productInfo.layoutName,
                        categoryId = productInfo.basic.category.id,
                        categoryName = productInfo.basic.category.name
                    )
                }
            }
        }
    }

    fun updateAPlusContentMediaOnExpandedStateChange(aPlusContentExpanded: Boolean) {
        val lastShowedAPlusMediaName = findLastShowedAPlusMediaName(aPlusContentExpanded)
        mapOfData.forEach { (key, _) ->
            updateData(key, false) {
                (mapOfData[key] as? APlusImageUiModel)?.apply {
                    expanded = aPlusContentExpanded
                    haveBottomPadding = name() == lastShowedAPlusMediaName
                    trackerData.expanded = aPlusContentExpanded
                }
            }
        }
    }

    private fun findLastShowedAPlusMediaName(
        aPlusContentExpanded: Boolean
    ) = mapOfData.values.findLast {
        if (aPlusContentExpanded) {
            it is APlusImageUiModel
        } else {
            it is APlusImageUiModel && it.showOnCollapsed
        }
    }?.name()

    private fun getAPlusMediaCount() = mapOfData.values.count { it is APlusImageUiModel }
}
