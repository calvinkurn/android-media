package com.tokopedia.product.detail.view.util

import android.content.Context
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.pdp.fintech.view.FintechPriceDataModel
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirImage
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimateData
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel
import com.tokopedia.product.detail.data.model.datamodel.PdpComparisonWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductBundlingDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoDataModel
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
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTickerInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopAdsImageDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopadsHeadlineUiModel
import com.tokopedia.product.detail.data.model.datamodel.UpcomingNplDataModel
import com.tokopedia.product.detail.data.model.datamodel.VariantDataModel
import com.tokopedia.product.detail.data.model.purchaseprotection.PPItemDetailPage
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpful
import com.tokopedia.product.detail.data.model.ticker.TickerDataResponse
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.model.upcoming.ProductUpcomingData
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PDP_7
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PDP_9_TOKONOW
import com.tokopedia.recommendation_widget_common.extension.LAYOUTTYPE_HORIZONTAL_ATC
import com.tokopedia.recommendation_widget_common.extension.toProductCardModels
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
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
        get()= mapOfData[ProductDetailConstant.FINTECH_WIDGET_NAME] as? FintechWidgetDataModel

    val productTradeinMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.TRADE_IN] as? ProductGeneralInfoDataModel

    val productWholesaleInfoMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_WHOLESALE_INFO] as? ProductGeneralInfoDataModel

    val productInstallmentInfoMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_INSTALLMENT_INFO] as? ProductGeneralInfoDataModel

    val productInstallmentPaylater: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_INSTALLMENT_PAYLATER_INFO] as? ProductGeneralInfoDataModel

    val orderPriorityMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.ORDER_PRIORITY] as? ProductGeneralInfoDataModel

    val productProtectionMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_PROTECTION] as? ProductGeneralInfoDataModel

    val productFullfilmentMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_FULLFILMENT] as? ProductGeneralInfoDataModel

    val productNewVariantDataModel: VariantDataModel?
        get() = mapOfData[ProductDetailConstant.VARIANT_OPTIONS] as? VariantDataModel

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

    val productBundlingData: ProductBundlingDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_BUNDLING] as? ProductBundlingDataModel

    val topAdsProductBundlingData: TopadsHeadlineUiModel?
        get() = mapOfData[ProductDetailConstant.SHOPADS_CAROUSEL] as? TopadsHeadlineUiModel

    val contentWidgetData: ContentWidgetDataModel?
        get() = mapOfData[ProductDetailConstant.PLAY_CAROUSEL] as? ContentWidgetDataModel

    fun updateDataP1(context: Context?, dataP1: DynamicProductInfoP1?, loadInitialData: Boolean = false) {
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
                    data?.campaign?.originalPriceFmt = it.data.campaign.originalPrice.getCurrencyFormatted()
                    data?.campaign?.discountedPriceFmt = it.data.campaign.discountedPrice.getCurrencyFormatted()
                    data?.price?.priceFmt = it.data.price.value.getCurrencyFormatted()
                }
            }

            updateData(ProductDetailConstant.VARIANT_OPTIONS, loadInitialData) {
                productNewVariantDataModel?.run {
                    isRefreshing = false
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
                }
            }

            updateData(ProductDetailConstant.MINI_SOCIAL_PROOF, loadInitialData) {
                miniSocialProofMap?.run {
                    rating = it.basic.stats.rating
                    ratingCount = it.basic.stats.countReview.toIntOrZero()
                    talkCount = it.basic.stats.countTalk.toIntOrZero()
                    itemSoldFmt = it.basic.txStats.itemSoldFmt
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

            updateData(ProductDetailConstant.PRODUCT_WHOLESALE_INFO, loadInitialData) {
                productWholesaleInfoMap?.run {
                    val minPrice = it.data.wholesale?.minByOrNull { it.price.value }?.price?.value
                            ?: return@run
                    subtitle = context?.getString(R.string.label_format_wholesale, minPrice.getCurrencyFormatted())
                            ?: ""
                }
            }

            updateData(ProductDetailConstant.REVIEW, loadInitialData) {
                productReviewMap?.run {
                    totalRating = it.basic.stats.countReview.toIntOrZero()
                    ratingScore = it.basic.stats.rating
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
                    this.productId = dataP1?.basic?.productID
                }
            }
        }
    }

    fun updateInitialMedia(media: List<Media>) {
        updateData(ProductDetailConstant.MEDIA, true) {
            mediaMap?.run {
                initialScrollPosition = if (initialScrollPosition == -1) -1 else 0
                listOfMedia = DynamicProductDetailMapper.convertMediaToDataModel(
                        media.toMutableList()
                )
            }
        }
    }

    fun updateMediaScrollPosition(selectedOptionId: String?) {
        if (selectedOptionId == null) return
        updateData(ProductDetailConstant.MEDIA) {
            mediaMap?.apply {
                this.variantOptionIdScrollAnchor = selectedOptionId
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
                    context?.getString(com.tokopedia.common_tradein.R.string.text_price_holder, CurrencyFormatUtil.convertPriceValueToIdrFormat(tradeinResponse.usedPrice.toIntOrZero(), true))
                            ?: ""
                } else if (!tradeinResponse.widgetString.isNullOrEmpty()) {
                    tradeinResponse.widgetString
                } else {
                    context?.getString(com.tokopedia.common_tradein.R.string.trade_in_exchange)
                        ?: ""
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

            updateData(ProductDetailConstant.FINTECH_WIDGET_NAME)
            {
                fintechWidgetMap?.run {
                    productId = selectedProductId
                    categoryId = productCategoryId
                    idToPriceUrlMap = productIdToPriceURLMap
                    isLoggedIn = loggedIn
                }
            }
        }
    }

    fun updateFintechDataWithProductId(selectedProductId: String, loggedIn: Boolean) {
        updateData(ProductDetailConstant.FINTECH_WIDGET_NAME)
        {
            fintechWidgetMap?.run {
                productId = selectedProductId
                isLoggedIn = loggedIn
            }
        }
    }

    fun updateVariantError() {
        updateData(ProductDetailConstant.VARIANT_OPTIONS) {
            productNewVariantDataModel?.run {
                isVariantError = true
            }
        }
    }

    fun updateWishlistData(isWishlisted: Boolean) {
        updateData(ProductDetailConstant.PRODUCT_CONTENT) {
            basicContentMap?.isWishlisted = isWishlisted
        }
    }

    fun updateFulfillmentData(context: Context?, isFullfillment: Boolean) {
        val fullFillmentText = if (!isFullfillment) {
            ""
        } else {
            context?.getString(R.string.multiorigin_desc) ?: ""
        }

        updateData(ProductDetailConstant.PRODUCT_FULLFILMENT) {
            productFullfilmentMap?.run {
                subtitle = fullFillmentText
            }
        }
    }

    fun updateDataP2(context: Context?,
                     p2Data: ProductInfoP2UiData,
                     productId: String,
                     isProductWarehouse: Boolean,
                     isProductInCampaign: Boolean,
                     isOutOfStock: Boolean,
                     boeImageUrl: String,
                     isProductParent: Boolean) {
        p2Data.let {
            updateData(ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY) {
                shopCredibility?.run {
                    shopLastActive = if (context == null) "" else it.shopInfo.shopLastActive.getRelativeDate(context)
                    shopName = it.shopInfo.shopCore.name
                    shopAva = it.shopInfo.shopAssets.avatar
                    shopLocation = it.shopInfo.location
                    isGoApotik = it.isGoApotik
                    shopTierBadgeUrl = it.shopInfo.shopTierBadgeUrl
                    infoShopData = if (context == null) listOf() else
                        getTwoShopInfoHieararchy(context,
                                it.shopSpeed,
                                it.shopChatSpeed.toLongOrZero(),
                                it.shopInfo.activeProduct.toLongOrZero(),
                                it.shopInfo.createdInfo.shopCreated,
                                it.shopRating)
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

            updateData(ProductDetailConstant.ORDER_PRIORITY) {
                orderPriorityMap?.run {
                    subtitle = it.shopCommitment.staticMessages.pdpMessage
                }
            }

            updateData(ProductDetailConstant.MINI_SOCIAL_PROOF) {
                miniSocialProofMap?.run {
                    wishlistCount = it.wishlistCount.toIntOrZero()
                    viewCount = it.productView.toIntOrZero()
                    shouldRenderSocialProof = true
                    buyerPhotosCount = it.buyerPhotosCount
                    setSocialProofData()
                }
            }

            updateData(ProductDetailConstant.MINI_SOCIAL_PROOF_STOCK) {
                miniSocialProofStockMap?.run {
                    wishlistCount = it.wishlistCount.toIntOrZero()
                    viewCount = it.productView.toIntOrZero()
                    shouldRenderSocialProof = true
                    buyerPhotosCount = it.buyerPhotosCount
                    setSocialProofData()
                }
            }

            updateData(ProductDetailConstant.MVC) {
                mvcSummaryData?.run {
                    animatedInfos = it.merchantVoucherSummary.animatedInfos
                    isShown = it.merchantVoucherSummary.isShown
                    shopId = it.shopInfo.shopCore.shopID
                    productIdMVC = productId
                }
            }

            //old installment component
            if (it.productFinancingRecommendationData.data.partnerCode.isNotBlank()) {
                updateData(ProductDetailConstant.PRODUCT_INSTALLMENT_INFO) {
                    productInstallmentInfoMap?.run {
                        subtitle = String.format(context?.getString(R.string.new_installment_template)
                                ?: "",
                                CurrencyFormatUtil.convertPriceValueToIdrFormat(it.productFinancingRecommendationData.data.monthlyPrice.roundToLong(), false))
                    }
                }
            } else {
                removeComponent(ProductDetailConstant.PRODUCT_INSTALLMENT_INFO)
            }

            if (it.productFinancingRecommendationData.data.subtitle.isEmpty()) {
                removeComponent(ProductDetailConstant.PRODUCT_INSTALLMENT_PAYLATER_INFO)
            } else {
                updateData(ProductDetailConstant.PRODUCT_INSTALLMENT_PAYLATER_INFO) {
                    productInstallmentPaylater?.run {
                        subtitle = it.productFinancingRecommendationData.data.subtitle
                    }
                }
            }

            updatePurchaseProtectionData(it.productPurchaseProtectionInfo.ppItemDetailPage)
            updateNotifyMeAndContent(productId, it.upcomingCampaigns, it.validateTradeIn.isEligible, boeImageUrl)
            updateDataTradein(context, it.validateTradeIn)
            updateData(ProductDetailConstant.REVIEW) {
                productReviewMap?.run {
                    listOfReviews = it.helpfulReviews
                    mediaThumbnails = it.reviewMediaThumbnails
                    detailedMediaResult = it.detailedMediaResult
                    formattedRating = it.rating.ratingScore
                    totalRatingCount = it.rating.totalRating
                    totalReviewCount = it.rating.totalReviewTextAndImage
                }
            }

            updateData(ProductDetailConstant.PRODUCT_BUNDLING) {
                productBundlingData?.bundleInfo = it.bundleInfoMap[productId]
            }

            if (it.ticker.tickerInfo.isEmpty()) {
                removeComponent(ProductDetailConstant.TICKER_INFO)
            } else {
                updateTicker(it.getTickerByProductId(productId))
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
                if (ppItemData.title?.isNotEmpty() == true) {
                    title = ppItemData.title
                } else if (ppItemData.titlePDP?.isNotEmpty() == true) {
                    title = ppItemData.titlePDP
                }
                subtitle = ppItemData.subTitlePDP ?: ""
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

    private fun updateNotifyMeAndContent(productId: String,
                                         upcomingData: Map<String, ProductUpcomingData>?, eligibleTradein: Boolean, freeOngkirImgUrl: String) {
        updateData(ProductDetailConstant.PRODUCT_CONTENT) {
            basicContentMap?.run {
                val selectedUpcoming = upcomingData?.get(productId)
                upcomingNplData = UpcomingNplDataModel(selectedUpcoming?.upcomingType
                        ?: "", selectedUpcoming?.campaignTypeName ?: "",
                        selectedUpcoming?.startDate ?: "")
                shouldShowTradein = if (productTradeinMap == null) false else eligibleTradein
                this.freeOngkirImgUrl = freeOngkirImgUrl
            }
        }

        updateUpcoming(productId, upcomingData)
    }

    private fun updateUpcoming(productId: String,
                               upcomingData: Map<String, ProductUpcomingData>?) {
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
                        upcomingType = selectedUpcoming?.upcomingType ?: "",
                        ribbonCopy = selectedUpcoming?.campaignTypeName ?: "",
                        startDate = selectedUpcoming?.startDate ?: "",
                )
            }
        }
    }

    /**
     * Use this only when update variant, because no need to update tradein when variant changed
     */
    fun updateNotifyMeAndContent(productId: String, upcomingData: Map<String, ProductUpcomingData>?, freeOngkirImgUrl: String) {
        updateData(ProductDetailConstant.PRODUCT_CONTENT) {
            basicContentMap?.run {
                val selectedUpcoming = upcomingData?.get(productId)
                upcomingNplData = UpcomingNplDataModel(selectedUpcoming?.upcomingType
                        ?: "", selectedUpcoming?.campaignTypeName ?: "",
                        selectedUpcoming?.startDate ?: "")
                this.freeOngkirImgUrl = freeOngkirImgUrl
            }
        }

        updateUpcoming(productId, upcomingData)
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
                        cardModel = data.recommendationItemList.toProductCardModels(hasThreeDots = true)
                        filterData = mapToAnnotateChip(data)
                    }
                }
                else -> {
                    (mapOfData[data.pageName] as? ProductRecommendationDataModel)?.run {
                        recomWidgetData = data
                        cardModel = data.recommendationItemList.toProductCardModels(hasThreeDots = true)
                        filterData = mapToAnnotateChip(data)
                    }
                }
            }
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

    fun updateFilterRecommendationData(data: ProductRecommendationDataModel) {
        updateData(data.name) {
            (mapOfData[data.name] as? ProductRecommendationDataModel)?.run {
                filterData = data.filterData
                recomWidgetData = data.recomWidgetData
                cardModel = data.recomWidgetData?.recommendationItemList?.toProductCardModels(hasThreeDots = true)
                        ?: listOf()
            }
        }
    }

    fun updateVariantData(processedVariant: List<VariantCategory>?) {
        updateData(ProductDetailConstant.VARIANT_OPTIONS) {
            productNewVariantDataModel?.listOfVariantCategory = processedVariant
        }

        updateData(ProductDetailConstant.MINI_VARIANT_OPTIONS) {
            productSingleVariant?.variantLevelOne = processedVariant?.firstOrNull()
        }
    }

    fun updateVariantSelected(variantId: String, variantKey: String) {
        updateData(ProductDetailConstant.VARIANT_OPTIONS) {
            productNewVariantDataModel?.let {
                val copyMap: MutableMap<String, String> = it.mapOfSelectedVariant.toMutableMap()
                copyMap[variantKey] = variantId
                it.mapOfSelectedVariant = copyMap
            }
        }

        updateData(ProductDetailConstant.MINI_VARIANT_OPTIONS) {
            productSingleVariant?.let {
                val copyMap: MutableMap<String, String> = it.mapOfSelectedVariant.toMutableMap()
                copyMap[variantKey] = variantId
                it.mapOfSelectedVariant = copyMap
            }
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

    fun updateRecomTokonowQuantityData(miniCart: MutableMap<String, MiniCartItem>?) {
        mapOfData.filterValues { it is ProductRecommendationDataModel }.keys.forEach { key ->
            val productRecom = (mapOfData[key] as ProductRecommendationDataModel).copy()
            productRecom.recomWidgetData?.let { recomData ->
                if (recomData.layoutType == LAYOUTTYPE_HORIZONTAL_ATC) {
                    updateRecomWidgetQtyDataFromMiniCart(recomData.copy(), miniCart, key)
                }
            }
        }
    }

    private fun updateRecomWidgetQtyDataFromMiniCart(recomWidget: RecommendationWidget, miniCart: MutableMap<String, MiniCartItem>?, key: String) {
        val dataList = recomWidget.copyRecomItemList()
        dataList.forEach { recomItem ->
            //update data based on tokonow cart
            if (recomItem.isRecomProductShowVariantAndCart) {
                recomItem.setDefaultCurrentStock()
                miniCart?.let { cartData ->
                    recomItem.updateItemCurrentStock(when {
                        recomItem.isProductHasParentID() -> {
                            getTotalQuantityVariantBasedOnParentID(recomItem, miniCart)
                        }
                        cartData.containsKey(recomItem.productId.toString()) -> {
                            cartData[recomItem.productId.toString()]?.quantity ?: 0
                        }
                        else -> 0
                    })
                }
            }
        }
        updateData(key) {
            updateRecomDataByKey(key, dataList)
        }
    }

    private fun getTotalQuantityVariantBasedOnParentID(recomItem: RecommendationItem, miniCart: MutableMap<String, MiniCartItem>): Int {
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

    fun updateShipmentData(data: P2RatesEstimateData?, isFullfillment: Boolean, isCod: Boolean, freeOngkirData: BebasOngkirImage, userLocationLocalData: LocalCacheModel) {
        //pair.first boType, pair.second boImage
        updateData(ProductDetailConstant.SHIPMENT) {
            shipmentData?.rates = data ?: P2RatesEstimateData()
            shipmentData?.isFullfillment = isFullfillment
            shipmentData?.isCod = isCod
            shipmentData?.shouldShowShipmentError = data == null
            shipmentData?.freeOngkirType = freeOngkirData.boType
            shipmentData?.freeOngkirUrl = freeOngkirData.imageURL
            shipmentData?.tokoCabangIconUrl = freeOngkirData.tokoCabangImageURL
            shipmentData?.localDestination = if (userLocationLocalData.address_id == "" || userLocationLocalData.address_id == "0") "" else userLocationLocalData.label
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
                this.localDestination = if (userLocationLocalData.address_id == "" || userLocationLocalData.address_id == "0") "" else userLocationLocalData.label
            }
        }

    }

    fun updateProductBundlingData(p2Data: ProductInfoP2UiData?, productId: String?) {
        if (p2Data == null || productId == null) return
        updateData(ProductDetailConstant.PRODUCT_BUNDLING) {
            productBundlingData?.bundleInfo = p2Data.bundleInfoMap[productId]
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

    private fun updateData(key: String, loadInitialData: Boolean = false, updateAction: () -> Unit) {
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

}