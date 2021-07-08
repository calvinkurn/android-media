package com.tokopedia.product.detail.view.util

import android.content.Context
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.financing.PDPInstallmentRecommendationData
import com.tokopedia.product.detail.data.model.purchaseprotection.PPItemDetailPage
import com.tokopedia.product.detail.data.model.ratesestimate.P2RatesEstimateData
import com.tokopedia.product.detail.data.model.restrictioninfo.BebasOngkirImage
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpful
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.model.upcoming.ProductUpcomingData
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PDP_7
import com.tokopedia.recommendation_widget_common.extension.LAYOUTTYPE_HORIZONTAL_ATC
import com.tokopedia.recommendation_widget_common.extension.toProductCardModels
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
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

    val productInfoMap: ProductInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_INFO] as? ProductInfoDataModel

    val productDiscussionMostHelpfulMap: ProductDiscussionMostHelpfulDataModel?
        get() = mapOfData[ProductDetailConstant.DISCUSSION_FAQ] as? ProductDiscussionMostHelpfulDataModel

    val productReviewMap: ProductMostHelpfulReviewDataModel?
        get() = mapOfData[ProductDetailConstant.REVIEW] as? ProductMostHelpfulReviewDataModel

    val productTradeinMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.TRADE_IN] as? ProductGeneralInfoDataModel

    val productMerchantVoucherMap: ProductMerchantVoucherDataModel?
        get() = mapOfData[ProductDetailConstant.SHOP_VOUCHER] as? ProductMerchantVoucherDataModel

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

    val productByMeMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailCommonConstant.KEY_BYME] as? ProductGeneralInfoDataModel

    val topAdsImageData: TopAdsImageDataModel?
        get() = mapOfData[ProductDetailConstant.KEY_TOP_ADS] as? TopAdsImageDataModel

    val shipmentData: ProductShipmentDataModel?
        get() = mapOfData[ProductDetailConstant.SHIPMENT] as? ProductShipmentDataModel

    val mvcSummaryData: ProductMerchantVoucherSummaryDataModel?
        get() = mapOfData[ProductDetailConstant.MVC] as? ProductMerchantVoucherSummaryDataModel

    val bestSellerData: BestSellerInfoDataModel?
        get() = mapOfData[ProductDetailConstant.BEST_SELLER] as? BestSellerInfoDataModel

    fun updateDataP1(context: Context?, dataP1: DynamicProductInfoP1?, enableVideo: Boolean, loadInitialData: Boolean = false) {
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

            updateData(ProductDetailConstant.MEDIA, loadInitialData) {
                mediaMap?.run {
                    shouldRenderImageVariant = !loadInitialData

                    listOfMedia = if (enableVideo) {
                        DynamicProductDetailMapper.convertMediaToDataModel(it.data.media.toMutableList())
                    } else {
                        DynamicProductDetailMapper.convertMediaToDataModel(it.data.media.filter { it.type != ProductMediaDataModel.VIDEO_TYPE }.toMutableList())
                    }
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
                    paymentVerifiedCount = it.basic.txStats.itemSoldPaymentVerified.toIntOrZero()
                }
            }

            updateData(ProductDetailConstant.MINI_SOCIAL_PROOF_STOCK, loadInitialData) {
                miniSocialProofStockMap?.run {
                    rating = it.basic.stats.rating
                    ratingCount = it.basic.stats.countReview.toIntOrZero()
                    stock = it.basic.totalStockFmt
                    paymentVerifiedCount = it.basic.txStats.itemSoldPaymentVerified.toIntOrZero()
                }
            }

            updateData(ProductDetailConstant.PRODUCT_INFO, loadInitialData) {
                productInfoMap?.run {
                    youtubeVideos = it.data.youtubeVideos
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

            val productId = it.basic.productID
            dataP1.bestSellerContent?.let { bestSellerInfoContent ->
                if (bestSellerInfoContent.contains(productId)) {
                    updateBestSellerData(dataP1 = dataP1, productId = productId)
                } else {
                    updateBestSellerData()
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
                    this.bestSellerInfoContent = null
                } else {
                    dataP1.bestSellerContent?.let {
                        this.bestSellerInfoContent = it[productId]
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

    fun updateVariantError() {
        updateData(ProductDetailConstant.VARIANT_OPTIONS) {
            productNewVariantDataModel?.run {
                isVariantError = true
            }
        }
    }

    fun updateDataInstallment(context: Context?, financingData: PDPInstallmentRecommendationData, isOs: Boolean) {
        updateData(ProductDetailConstant.PRODUCT_INSTALLMENT_INFO) {
            productInstallmentInfoMap?.run {
                subtitle = String.format(context?.getString(R.string.new_installment_template)
                        ?: "",
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                (if (isOs) financingData.data.osMonthlyPrice
                                else financingData.data.monthlyPrice).roundToLong(), false))
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

    fun updateByMeData(context: Context?) {
        updateData(ProductDetailCommonConstant.KEY_BYME) {
            productByMeMap?.run {
                subtitle = context?.getString(R.string.product_detail_by_me_subtitle)
                        ?: ""
            }
        }
    }

    fun updateDataP2(context: Context?,
                     p2Data: ProductInfoP2UiData,
                     productId: String,
                     isProductWarehouse: Boolean,
                     isProductInCampaign: Boolean,
                     isOutOfStock: Boolean,
                     boeImageUrl: String) {
        p2Data.let {

            updateData(ProductDetailConstant.TICKER_INFO) {
                tickerInfoMap?.run {
                    statusInfo = if (it.shopInfo.isShopInfoNotEmpty()) it.shopInfo.statusInfo.copy() else null
                    closedInfo = if (it.shopInfo.isShopInfoNotEmpty()) it.shopInfo.closedInfo.copy() else null
                    isUpcomingType = it.upcomingCampaigns[productId]?.isUpcomingNplType() ?: false
                    this.isProductWarehouse = isProductWarehouse
                    this.isProductInCampaign = isProductInCampaign
                    this.isOutOfStock = isOutOfStock
                }
            }

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
                }
            }

            updateData(ProductDetailConstant.MINI_SHOP_WIDGET) {
                miniShopWidgetMap?.run {
                    val shopInfo = it.shopInfo
                    shopName = shopInfo.shopCore.name
                    shopLocation = shopInfo.location
                    shopAva = shopInfo.shopAssets.avatar
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
                    buyerPhotosCount = it.imageReviews?.imageCount.toIntOrZero()
                    setSocialProofData()
                }
            }

            updateData(ProductDetailConstant.MINI_SOCIAL_PROOF_STOCK) {
                miniSocialProofStockMap?.run {
                    wishlistCount = it.wishlistCount.toIntOrZero()
                    viewCount = it.productView.toIntOrZero()
                    shouldRenderSocialProof = true
                    buyerPhotosCount = it.imageReviews?.imageCount.toIntOrZero()
                    setSocialProofData()
                }
            }

            updateData(ProductDetailConstant.SHOP_VOUCHER) {
                productMerchantVoucherMap?.run {
                    voucherData = ArrayList(it.vouchers)
                }
            }

            updateData(ProductDetailConstant.MVC) {
                mvcSummaryData?.run {
                    title = it.merchantVoucherSummary.title.firstOrNull()?.text ?: ""
                    subTitle = it.merchantVoucherSummary.subTitle
                    imageURL = it.merchantVoucherSummary.imageURL
                    isShown = it.merchantVoucherSummary.isShown
                    shopId = it.shopInfo.shopCore.shopID
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
                    imageReviews = it.imageReviews?.imageReviewItems
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

        updateData(ProductDetailConstant.UPCOMING_DEALS) {
            notifyMeMap?.run {
                val selectedUpcoming = upcomingData?.get(productId)
                campaignID = selectedUpcoming?.campaignId ?: ""
                campaignType = selectedUpcoming?.campaignType ?: ""
                campaignTypeName = selectedUpcoming?.campaignTypeName ?: ""
                startDate = selectedUpcoming?.startDate ?: ""
                notifyMe = selectedUpcoming?.notifyMe ?: false
                upcomingNplData = UpcomingNplDataModel(selectedUpcoming?.upcomingType
                        ?: "", selectedUpcoming?.campaignTypeName ?: "",
                        selectedUpcoming?.startDate ?: "")
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

        updateData(ProductDetailConstant.UPCOMING_DEALS) {
            notifyMeMap?.run {
                val selectedUpcoming = upcomingData?.get(productId)
                campaignID = selectedUpcoming?.campaignId ?: ""
                campaignType = selectedUpcoming?.campaignType ?: ""
                campaignTypeName = selectedUpcoming?.campaignTypeName ?: ""
                startDate = selectedUpcoming?.startDate ?: ""
                notifyMe = selectedUpcoming?.notifyMe ?: false
                upcomingNplData = UpcomingNplDataModel(selectedUpcoming?.upcomingType
                        ?: "", selectedUpcoming?.campaignTypeName ?: "",
                        selectedUpcoming?.startDate ?: "")
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

    fun updateDataP3(it: ProductInfoP3) {
        updateData(ProductDetailConstant.TICKER_INFO) {
            tickerInfoMap?.run {
                if (it.tickerInfo.isNotEmpty()) {
                    generalTickerInfoDataModel = it.tickerInfo
                }
            }
        }
    }

    fun updateRecommendationData(data: RecommendationWidget) {
        updateData(data.pageName) {
            when (data.pageName) {
                PDP_7 -> {
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

    fun updateImageAfterClickVariant(it: MutableList<Media>, enableVideo: Boolean) {
        updateData(ProductDetailConstant.MEDIA) {
            mediaMap?.shouldRenderImageVariant = true
            mediaMap?.listOfMedia = if (enableVideo) {
                DynamicProductDetailMapper.convertMediaToDataModel(it)
            } else {
                DynamicProductDetailMapper.convertMediaToDataModel(it.filter { it.type != ProductMediaDataModel.VIDEO_TYPE }.toMutableList())
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

    fun updateTickerData(isProductWarehouse: Boolean, isProductInCampaign: Boolean, isOutOfStock: Boolean, isUpcomingType: Boolean) {
        updateData(ProductDetailConstant.TICKER_INFO) {
            tickerInfoMap?.run {
                this.isProductWarehouse = isProductWarehouse
                this.isProductInCampaign = isProductInCampaign
                this.isOutOfStock = isOutOfStock
                this.isUpcomingType = isUpcomingType
            }
        }
    }

    fun updateTopAdsImageData(data: ArrayList<TopAdsImageViewModel>) {
        updateData(ProductDetailConstant.KEY_TOP_ADS) {
            topAdsImageData?.data = data
        }
    }

    fun updateRecomTokonowQuantityData(miniCart: MutableMap<String, MiniCartItem>?) {
        miniCart?.let { cartData ->
            mapOfData.filterValues { it is ProductRecommendationDataModel }.keys.forEach { key ->
                val productRecom = mapOfData[key] as ProductRecommendationDataModel
                productRecom.recomWidgetData?.let { recomWidget ->
                    if (recomWidget.layoutType == LAYOUTTYPE_HORIZONTAL_ATC) {
                        updateData(key) {
                            var successChangeData = false
                            recomWidget.recommendationItemList.forEach { recomItem ->
                                if (cartData.containsKey(recomItem.productId.toString())
                                        && recomItem.isRecomProductShowVariantAndCart
                                        && recomItem.quantity != cartData[recomItem.productId.toString()]?.quantity) {
                                    recomItem.quantity = cartData[recomItem.productId.toString()]?.quantity
                                            ?: 0
                                    successChangeData = true
                                }
                            }
                            if (successChangeData) {
                                productRecom.cardModel = recomWidget.recommendationItemList.toProductCardModels(false)
                            }
                        }
                    }
                }
            }
        }
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