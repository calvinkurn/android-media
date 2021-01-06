package com.tokopedia.product.detail.view.util

import android.content.Context
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.financing.PDPInstallmentRecommendationData
import com.tokopedia.product.detail.data.model.purchaseprotection.PPItemDetailPage
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpful
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.model.upcoming.ProductUpcomingData
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.variant_common.model.VariantCategory
import kotlin.math.roundToLong

/**
 * This class hold all of the ViewHolder data. They have same instance.
 * If you changes one of this variable , data inside ViewHolder also updated (don't forget to notify)
 */
class PdpUiUpdaterDiffutil(var mapOfData: MutableMap<String, DynamicPdpDataModel>) {

    private val miniSocialProofMap: ProductMiniSocialProofDataModel?
        get() = mapOfData[ProductDetailConstant.MINI_SOCIAL_PROOF] as? ProductMiniSocialProofDataModel

    val basicContentMap: ProductContentDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_CONTENT] as? ProductContentDataModel

    val shopInfoMap: ProductShopInfoDataModel?
        get() = mapOfData[ProductDetailConstant.SHOP_INFO] as? ProductShopInfoDataModel

    val productInfoMap: ProductInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_INFO] as? ProductInfoDataModel

    val productDiscussionMostHelpfulMap: ProductDiscussionMostHelpfulDataModel?
        get() = mapOfData[ProductDetailConstant.DISCUSSION_FAQ] as? ProductDiscussionMostHelpfulDataModel

    val productReviewOldMap: ProductMostHelpfulReviewDataModel?
        get() = mapOfData[ProductDetailConstant.MOST_HELPFUL_REVIEW] as? ProductMostHelpfulReviewDataModel

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

    val productShipingInfoMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_SHIPPING_INFO] as? ProductGeneralInfoDataModel

    val orderPriorityMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.ORDER_PRIORITY] as? ProductGeneralInfoDataModel

    val productProtectionMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_PROTECTION] as? ProductGeneralInfoDataModel

    val productFullfilmentMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_FULLFILMENT] as? ProductGeneralInfoDataModel

    val productNewVariantDataModel: VariantDataModel?
        get() = mapOfData[ProductDetailConstant.VARIANT_OPTIONS] as? VariantDataModel

    val notifyMeMap: ProductNotifyMeDataModel?
        get() = mapOfData[ProductDetailConstant.UPCOMING_DEALS] as? ProductNotifyMeDataModel

    val miniShopInfo: ProductMiniShopInfoDataModel?
        get() = mapOfData[ProductDetailConstant.MINI_SHOP_INFO] as? ProductMiniShopInfoDataModel

    val mediaMap: ProductMediaDataModel?
        get() = mapOfData[ProductDetailConstant.MEDIA] as? ProductMediaDataModel

    val tickerInfoMap: ProductTickerInfoDataModel?
        get() = mapOfData[ProductDetailConstant.TICKER_INFO] as? ProductTickerInfoDataModel

    val shopCredibility: ProductShopCredibilityDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY] as? ProductShopCredibilityDataModel

    val getShopInfo: ProductShopInfoDataModel
        get() = shopInfoMap ?: ProductShopInfoDataModel()

    val productByMeMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.KEY_BYME] as? ProductGeneralInfoDataModel

    val topAdsImageData: TopAdsImageDataModel?
        get() = mapOfData[ProductDetailConstant.KEY_TOP_ADS] as? TopAdsImageDataModel

    fun updateDataP1(context: Context?, dataP1: DynamicProductInfoP1?, enableVideo: Boolean, loadInitialData: Boolean = false) {
        dataP1?.let {

            updateData(ProductDetailConstant.PRODUCT_CONTENT, loadInitialData) {
                basicContentMap?.run {
                    data = ProductContentMainData(
                            campaign = it.data.campaign,
                            freeOngkir = it.data.isFreeOngkir,
                            cashbackPercentage = it.data.isCashback.percentage,
                            price = it.data.price,
                            stockWording = it.data.stock.stockWording,
                            isVariant = it.data.variant.isVariant,
                            productName = it.data.name,
                            isProductActive = it.basic.isActive()
                    )
                }
            }

            updateData(ProductDetailConstant.VARIANT_OPTIONS, loadInitialData) {
                productNewVariantDataModel?.run {
                    isRefreshing = false
                }
            }

            updateData(ProductDetailConstant.MEDIA, loadInitialData) {
                mediaMap?.run {
                    shouldRenderImageVariant = true
                    listOfMedia = if (enableVideo) {
                        DynamicProductDetailMapper.convertMediaToDataModel(it.data.media.toMutableList())
                    } else {
                        DynamicProductDetailMapper.convertMediaToDataModel(it.data.media.filter { it.type != ProductMediaDataModel.VIDEO_TYPE }.toMutableList())
                    }
                }
            }

            updateData(ProductDetailConstant.MINI_SHOP_INFO, loadInitialData) {
                miniShopInfo?.run {
                    shopName = it.basic.shopName
                    isOS = it.data.isOS
                    isGoldMerchant = it.data.isPowerMerchant
                }
            }

            updateData(ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY, loadInitialData) {
                shopCredibility?.run {
                    isOs = it.data.isOS
                    isPm = it.data.isPowerMerchant
                }
            }

            updateData(ProductDetailConstant.SHOP_INFO, loadInitialData) {
                shopInfoMap?.run {
                    shopName = it.basic.shopName
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

            updateData(ProductDetailConstant.PRODUCT_INFO, loadInitialData) {
                productInfoMap?.run {
                    youtubeVideos = it.data.youtubeVideos
                }
            }

            updateData(ProductDetailConstant.PRODUCT_WHOLESALE_INFO, loadInitialData) {
                productWholesaleInfoMap?.run {
                    val minPrice = it.data.wholesale?.minBy { it.price.value }?.price?.value
                            ?: return@run
                    data.first().subtitle = context?.getString(R.string.label_format_wholesale, minPrice.getCurrencyFormatted())
                            ?: ""
                }
            }

            updateData(ProductDetailConstant.REVIEW, loadInitialData) {
                productReviewMap?.run {
                    totalRating = it.basic.stats.countReview.toIntOrZero()
                    ratingScore = it.basic.stats.rating
                }
            }

            updateData(ProductDetailConstant.MOST_HELPFUL_REVIEW, loadInitialData) {
                productReviewOldMap?.run {
                    totalRating = it.basic.stats.countReview.toIntOrZero()
                    ratingScore = it.basic.stats.rating
                }
            }

        }
    }

    private fun updateDataTradein(context: Context?, tradeinResponse: ValidateTradeIn) {
        updateData(ProductDetailConstant.TRADE_IN) {
            productTradeinMap?.run {
                updateData(ProductDetailConstant.PRODUCT_CONTENT) {
                    basicContentMap?.shouldShowTradein = tradeinResponse.isEligible
                }

                data.first().subtitle = if (tradeinResponse.usedPrice.toIntOrZero() > 0) {
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
                data.first().subtitle = String.format(context?.getString(R.string.new_installment_template)
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
                data.first().subtitle = fullFillmentText
            }
        }
    }

    fun updateByMeData(context: Context?) {
        updateData(ProductDetailConstant.KEY_BYME) {
            productByMeMap?.run {
                data.firstOrNull()?.subtitle = context?.getString(R.string.product_detail_by_me_subtitle)
                        ?: ""
            }
        }
    }

    fun updateDataP2(context: Context?, p2Data: ProductInfoP2UiData, productId: String, isProductWarehouse: Boolean, isProductInCampaign: Boolean, isOutOfStock: Boolean) {
        p2Data.let {
            updateData(ProductDetailConstant.SHOP_INFO) {
                shopInfoMap?.run {
                    shopLocation = it.shopInfo.location
                    shopLastActive = it.shopInfo.shopLastActive
                    shopAvatar = it.shopInfo.shopAssets.avatar
                    isAllowManage = it.shopInfo.isAllowManage
                    isGoAPotik = it.isGoApotik
                    shopBadge = it.shopBadge
                    shouldRenderShopInfo = true
                }
            }

            updateData(ProductDetailConstant.TICKER_INFO) {
                tickerInfoMap?.run {
                    statusInfo = if (it.shopInfo.isShopInfoNotEmpty()) it.shopInfo.statusInfo else null
                    closedInfo = if (it.shopInfo.isShopInfoNotEmpty()) it.shopInfo.closedInfo else null
                    this.isProductWarehouse = isProductWarehouse
                    this.isProductInCampaign = isProductInCampaign
                    this.isOutOfStock = isOutOfStock
                }
            }

            updateData(ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY) {
                shopCredibility?.run {
                    shopLastActive = it.shopInfo.shopLastActive
                    shopName = it.shopInfo.shopCore.name
                    shopAva = it.shopInfo.shopAssets.avatar
                    shopLocation = it.shopInfo.location
                    shopActiveProduct = it.shopInfo.activeProduct.toIntOrZero()
                    shopCreated = it.shopInfo.createdInfo.shopCreated
                    isGoApotik = it.isGoApotik
                    shopSpeed = it.shopSpeed
                    shopChatSpeed = it.shopChatSpeed.toIntOrZero()
                    shopRating = it.shopRating
                }
            }

            updateData(ProductDetailConstant.ORDER_PRIORITY) {
                orderPriorityMap?.run {
                    data.first().subtitle = it.shopCommitment.staticMessages.pdpMessage
                }
            }

            updateData(ProductDetailConstant.MINI_SOCIAL_PROOF) {
                miniSocialProofMap?.run {
                    wishlistCount = it.wishlistCount.toIntOrZero()
                    viewCount = it.productView.toIntOrZero()
                    shouldRenderSocialProof = true
                }
            }

            updateData(ProductDetailConstant.SHOP_VOUCHER) {
                productMerchantVoucherMap?.run {
                    voucherData = ArrayList(it.vouchers)
                }
            }

            updatePurchaseProtectionData(it.productPurchaseProtectionInfo.ppItemDetailPage)
            updateDataTradein(context, it.validateTradeIn)
            updateNotifyMeUpcoming(productId, it.upcomingCampaigns)
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
                    title = ppItemData.title ?: ""
                } else if (ppItemData.titlePDP?.isNotEmpty() == true) {
                    title = ppItemData.titlePDP ?: ""
                }
                val contentList = ArrayList<Content>()
                // Subtitle
                contentList.add(Content(subtitle = ppItemData.subTitlePDP
                        ?: ""))
                // Partner Details
                contentList.add(Content(
                        icon = ppItemData.partnerLogo ?: "",
                        subtitle = ppItemData.partnerText ?: "",
                        applink = ppItemData.linkURL ?: ""
                ))
                data = contentList
                isApplink = ppItemData.isAppLink ?: false
            }
        }

    }

    fun updateNotifyMeButton(notifyMe:Boolean) {
        updateData(ProductDetailConstant.UPCOMING_DEALS) {
            notifyMeMap?.notifyMe = !notifyMe
        }
    }

    fun updateNotifyMeUpcoming(productId: String, upcomingData: Map<String, ProductUpcomingData>?) {
        updateData(ProductDetailConstant.PRODUCT_CONTENT) {
            basicContentMap?.run {
                val selectedUpcoming = upcomingData?.get(productId)
                upcomingNplData = UpcomingNplDataModel(selectedUpcoming?.upcomingType
                        ?: "", selectedUpcoming?.ribbonCopy ?: "",
                        selectedUpcoming?.startDate ?: "")
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
                        ?: "", selectedUpcoming?.ribbonCopy ?: "",
                        selectedUpcoming?.startDate ?: "")
            }
        }
    }

    fun updateDataP2General(data: ProductInfoP2Other?) {
        data?.let {
            updateData(ProductDetailConstant.REVIEW) {
                productReviewMap?.run {
                    listOfReviews = it.helpfulReviews
                    imageReviews = it.imageReviews
                }
            }

            updateData(ProductDetailConstant.MOST_HELPFUL_REVIEW) {
                productReviewOldMap?.run {
                    listOfReviews = it.helpfulReviews
                    imageReviews = it.imageReviews
                }
            }

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

    fun updateDataP3(context: Context?, it: ProductInfoP3) {
        updateData(ProductDetailConstant.PRODUCT_SHIPPING_INFO) {
            productShipingInfoMap?.run {
                data.first().subtitle = context?.getString(R.string.ongkir_pattern_string_dynamic_pdp, it.rateEstSummarizeText?.minPrice, "${it.rateEstSummarizeText?.destination}")
                        ?: ""
            }
        }

        updateData(ProductDetailConstant.TICKER_INFO) {
            tickerInfoMap?.run {
                generalTickerInfo = it.tickerInfo
            }
        }
    }

    fun updateRecommendationData(data: RecommendationWidget) {
        updateData(data.pageName) {
            (mapOfData[data.pageName] as? ProductRecommendationDataModel)?.run {
                recomWidgetData = data
                cardModel = mapToCardModel(data)
                filterData = mapToAnnotateChip(data)
            }
        }
    }

    fun updateFilterRecommendationData(data: ProductRecommendationDataModel){
        updateData(data.name) {
            (mapOfData[data.name] as? ProductRecommendationDataModel)?.run {
                filterData = data.filterData
                recomWidgetData = data.recomWidgetData
                cardModel = mapToCardModel(data.recomWidgetData)
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
        updateData(ProductDetailConstant.SHOP_INFO) {
            shopInfoMap?.isFavorite = !isFavorite
        }

        updateData(ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY) {
            shopCredibility?.isFavorite = !isFavorite
        }
    }

    fun updateShopFollow(isFollow: Int) {
        updateData(ProductDetailConstant.SHOP_INFO) {
            shopInfoMap?.isFavorite = isFollow == ProductDetailConstant.ALREADY_FAVORITE_SHOP
        }
        updateData(ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY) {
            shopCredibility?.isFavorite = isFollow == ProductDetailConstant.ALREADY_FAVORITE_SHOP
        }
    }

    fun failUpdateShopFollow() {
        updateData(ProductDetailConstant.SHOP_INFO) {
            shopInfoMap?.enableButtonFavorite = true
        }
        updateData(ProductDetailConstant.PRODUCT_SHOP_CREDIBILITY) {
            shopCredibility?.enableButtonFavorite = true
        }
    }

    fun updateTickerData(isProductWarehouse: Boolean, isProductInCampaign: Boolean, isOutOfStock: Boolean) {
        updateData(ProductDetailConstant.TICKER_INFO) {
            tickerInfoMap?.run {
                this.isProductWarehouse = isProductWarehouse
                this.isProductInCampaign = isProductInCampaign
                this.isOutOfStock = isOutOfStock
            }
        }
    }

    fun updateTopAdsImageData(data: ArrayList<TopAdsImageViewModel>) {
        updateData(ProductDetailConstant.KEY_TOP_ADS) {
            topAdsImageData?.data = data
        }
    }

    fun removeComponent(key: String) {
        if (key.isNotEmpty()) {
            mapOfData.remove(key)
        }
    }

    private fun mapToCardModel(data: RecommendationWidget?): List<ProductCardModel> {
        if (data == null) return listOf()
        return data.recommendationItemList.map {
            ProductCardModel(
                    slashedPrice = it.slashedPrice,
                    productName = it.name,
                    formattedPrice = it.price,
                    productImageUrl = it.imageUrl,
                    isTopAds = it.isTopAds,
                    discountPercentage = it.discountPercentage,
                    reviewCount = it.countReview,
                    ratingCount = it.rating,
                    countSoldRating = it.ratingAverage,
                    shopLocation = it.location,
                    isWishlistVisible = false,
                    isWishlisted = it.isWishlist,
                    shopBadgeList = it.badgesUrl.map {
                        ProductCardModel.ShopBadge(imageUrl = it
                                ?: "")
                    },
                    freeOngkir = ProductCardModel.FreeOngkir(
                            isActive = it.isFreeOngkirActive,
                            imageUrl = it.freeOngkirImageUrl
                    ),
                    labelGroupList = it.labelGroupList.map { recommendationLabel ->
                        ProductCardModel.LabelGroup(
                                position = recommendationLabel.position,
                                title = recommendationLabel.title,
                                type = recommendationLabel.type
                        )
                    }
            )
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
                mapOfData[key] = it.newInstance()
            }
        }
        updateAction.invoke()
    }

}