package com.tokopedia.product.detail.view.util

import android.content.Context
import com.tokopedia.common_tradein.model.ValidateTradeInResponse
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.data.model.ProductInfoP2General
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.financing.PDPInstallmentRecommendationResponse
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpful
import com.tokopedia.product.detail.data.model.variant.VariantDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.variant_common.model.VariantCategory
import kotlin.math.roundToLong

/**
 * This class hold all of the ViewHolder data. They have same instance.
 * If you changes one of this variable , data inside ViewHolder also updated (don't forget to notify)
 */
class PdpUiUpdater(private val mapOfData: Map<String, DynamicPdpDataModel>) {

    val socialProofMap: ProductSocialProofDataModel?
        get() = mapOfData[ProductDetailConstant.SOCIAL_PROOF] as? ProductSocialProofDataModel

    val productSocialProofPvDataModel: ProductSocialProofDataModel?
        get() = mapOfData[ProductDetailConstant.SOCIAL_PROOF_PV] as? ProductSocialProofDataModel

    val miniSocialProofMap: ProductMiniSocialProofDataModel?
        get() = mapOfData[ProductDetailConstant.MINI_SOCIAL_PROOF] as? ProductMiniSocialProofDataModel

    val snapShotMap: ProductSnapshotDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_CONTENT_TITLE_TOP] as? ProductSnapshotDataModel

    val basicContentMap: ProductContentDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_CONTENT] as? ProductContentDataModel

    val shopInfoMap: ProductShopInfoDataModel?
        get() = mapOfData[ProductDetailConstant.SHOP_INFO] as? ProductShopInfoDataModel

    val productInfoMap: ProductInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_INFO] as? ProductInfoDataModel

    val productDiscussionMap: ProductDiscussionDataModel?
        get() = mapOfData[ProductDetailConstant.DISCUSSION] as? ProductDiscussionDataModel

    val productDiscussionMostHelpfulMap: ProductDiscussionMostHelpfulDataModel?
        get() = mapOfData[ProductDetailConstant.DISCUSSION_FAQ] as? ProductDiscussionMostHelpfulDataModel

    val productMostHelpfulMap: ProductMostHelpfulReviewDataModel?
        get() = mapOfData[ProductDetailConstant.MOST_HELPFUL_REVIEW] as? ProductMostHelpfulReviewDataModel

    val productTradeinMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.TRADE_IN] as? ProductGeneralInfoDataModel

    val productMerchantVoucherMap: ProductMerchantVoucherDataModel?
        get() = mapOfData[ProductDetailConstant.SHOP_VOUCHER] as? ProductMerchantVoucherDataModel

    val productLastSeenMap: ProductLastSeenDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_LAST_SEEN] as? ProductLastSeenDataModel

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

    val valuePropositionDataModel: ProductValuePropositionDataModel?
        get() = mapOfData[ProductDetailConstant.VALUE_PROP] as? ProductValuePropositionDataModel

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

    val listProductRecomMap: List<ProductRecommendationDataModel>? = mapOfData.filterKeys {
        it == ProductDetailConstant.PDP_1 || it == ProductDetailConstant.PDP_2
                || it == ProductDetailConstant.PDP_3 || it == ProductDetailConstant.PDP_4
    }.map {
        it.value as ProductRecommendationDataModel
    }

    val getShopInfo: ProductShopInfoDataModel
        get() = shopInfoMap ?: ProductShopInfoDataModel()

    val productByMeMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.KEY_BYME] as? ProductGeneralInfoDataModel

    fun updateDataP1(context: Context?, dataP1: DynamicProductInfoP1?) {
        dataP1?.let {
            basicContentMap?.run {
                data = it
            }
            snapShotMap?.run {
                data = it
            }
            mediaMap?.run {
                shouldRenderImageVariant = true
                listOfMedia = DynamicProductDetailMapper.convertMediaToDataModel(it.data.media.toMutableList())
            }

            notifyMeMap?.run {
                campaignID = it.data.campaignId
                campaignType = it.data.campaignType
                campaignTypeName = it.data.campaignTypeName
                endDate = it.data.endDate
                startDate = it.data.startDate
                notifyMe = it.data.notifyMe
            }

            miniShopInfo?.run {
                isOS = it.data.isOS
                isGoldMerchant = it.data.isPowerMerchant
            }

            valuePropositionDataModel?.run {
                isOfficialStore = it.data.isOS
            }

            productDiscussionMap?.run {
                shopId = it.basic.shopID
                talkCount = it.basic.stats.countTalk
            }

            miniSocialProofMap?.run {
                rating = it.basic.stats.rating
                ratingCount = it.basic.stats.countReview
                viewCount = it.basic.stats.countView
                talkCount = it.basic.stats.countTalk
                paymentVerifiedCount = it.basic.txStats.itemSoldPaymentVerified.toInt()
            }

            socialProofMap?.run {
                txStats = it.basic.txStats
                stats = it.basic.stats
            }

            productSocialProofPvDataModel?.run {
                txStats = it.basic.txStats
                stats = it.basic.stats
            }

            productInfoMap?.run {
                dynamicProductInfoP1 = it
            }

            productWholesaleInfoMap?.run {
                val minPrice = it.data.wholesale?.minBy { it.price.value }?.price?.value ?: return
                data.first().subtitle = context?.getString(R.string.label_format_wholesale, minPrice.getCurrencyFormatted())
                        ?: ""
            }

            productLastSeenMap?.run {
                /*
                 * Sometimes this lastUpdateUnix doesn't has Long value like "123"
                 * If P1 updated by selected variant this value will be formatted dated "dd-mm-yyy , hh:mm"
                 */
                val dateFormatted = it.data.price.lastUpdateUnix toDate "dd-MM-yyy , HH:mm"
                lastSeen = "$dateFormatted WIB"
            }
        }
    }

    fun updateDataTradein(context: Context?, tradeinResponse: ValidateTradeInResponse) {
        productTradeinMap?.run {
            basicContentMap?.shouldShowTradein = true
            snapShotMap?.shouldShowTradein = true

            data.first().subtitle = if (tradeinResponse.usedPrice > 0) {
                context?.getString(R.string.text_price_holder, CurrencyFormatUtil.convertPriceValueToIdrFormat(tradeinResponse.usedPrice, true))
                        ?: ""
            } else if (!tradeinResponse.widgetString.isNullOrEmpty()) {
                tradeinResponse.widgetString
            } else {
                context?.getString(R.string.trade_in_exchange) ?: ""
            }
        }
    }

    fun updateDataInstallment(context: Context?, financingData: PDPInstallmentRecommendationResponse, isOs: Boolean) {
        productInstallmentInfoMap?.run {
            data.first().subtitle = String.format(context?.getString(R.string.new_installment_template)
                    ?: "",
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            (if (isOs) financingData.response.data.osMonthlyPrice
                            else financingData.response.data.monthlyPrice).roundToLong(), false))
        }
    }

    fun updateDataP2Shop(dataP2: ProductInfoP2ShopData?) {
        dataP2?.let {
            val multiOriginNearestWarehouse = it.variantMultiOrigin
            shopInfoMap?.run {
                shopInfo = it.shopInfo
            }

            shopCredibility?.run {
                shopInfo = it.shopInfo
                shopSpeed = it.shopSpeed
                shopChatSpeed = it.shopChatSpeed
                shopRating = it.shopRating
            }

            miniShopInfo?.run {
                shopName = it.shopInfo?.shopCore?.name ?: ""
            }

            basicContentMap?.run {
                nearestWarehouseDataModel = ProductSnapshotDataModel.NearestWarehouseDataModel(multiOriginNearestWarehouse.warehouseInfo.id,
                        multiOriginNearestWarehouse.price, multiOriginNearestWarehouse.stockWording)
            }

            snapShotMap?.run {
                nearestWarehouseDataModel = ProductSnapshotDataModel.NearestWarehouseDataModel(multiOriginNearestWarehouse.warehouseInfo.id,
                        multiOriginNearestWarehouse.price, multiOriginNearestWarehouse.stockWording)
            }

            tickerInfoMap?.run {
                generalTickerInfo = it.tickerInfo
                statusInfo = it.shopInfo?.statusInfo
                closedInfo = it.shopInfo?.closedInfo
            }

            productInfoMap?.run {
                shopName = it.shopInfo?.shopCore?.name ?: ""
            }
        }
    }

    fun updateWishlistData(isWishlisted: Boolean) {
        basicContentMap?.isWishlisted = isWishlisted
        snapShotMap?.isWishlisted = isWishlisted
    }

    fun updateBasicContentCodData(isCod: Boolean) {
        snapShotMap?.shouldShowCod = isCod
        basicContentMap?.shouldShowCod = isCod
    }

    fun updateFulfillmentData(context: Context?, isFullfillment: Boolean) {
        val fullFillmentText = if (!isFullfillment) {
            ""
        } else {
            context?.getString(R.string.multiorigin_desc) ?: ""
        }

        productFullfilmentMap?.run {
            data.first().subtitle = fullFillmentText
        }
    }

    fun updateByMeData(context: Context?) {
        productByMeMap?.run {
            data.firstOrNull()?.subtitle = context?.getString(R.string.product_detail_by_me_subtitle) ?: ""
        }
    }

    fun updateDataP2General(dataP2General: ProductInfoP2General?) {
        dataP2General?.let {
            shopInfoMap?.run {
                shopFeature = it.shopFeature
                shopBadge = it.shopBadge
            }

            shopCredibility?.run {
                shopFeature = it.shopFeature
            }

            mediaMap?.run {
                shouldShowImageReview = it.imageReviews.isNotEmpty()
            }

            productInfoMap?.run {
                productSpecification = it.productSpecificationResponse
            }

            orderPriorityMap?.run {
                data.first().subtitle = it.shopCommitment.staticMessages.pdpMessage
            }

            productProtectionMap?.run {
                if (it.productPurchaseProtectionInfo.ppItemDetailPage?.title?.isNotEmpty() == true) {
                    title = it.productPurchaseProtectionInfo.ppItemDetailPage?.title
                            ?: ""
                }
                data.first().subtitle = it.productPurchaseProtectionInfo.ppItemDetailPage?.subTitlePDP
                        ?: ""
            }

            socialProofMap?.run {
                wishlistCount = it.wishlistCount.count
            }

            productSocialProofPvDataModel?.run {
                wishlistCount = it.wishlistCount.count
            }

            miniSocialProofMap?.run {
                wishlistCount = it.wishlistCount.count
                shouldRenderSocialProof = true
            }

            productDiscussionMap?.run {
                latestTalk = it.latestTalk
            }

            productDiscussionMostHelpfulMap?.run {
                if(it.discussionMostHelpful == null) {
                    isShimmering = true
                } else {
                    it.discussionMostHelpful?.let {
                        questions = it.questions
                        totalQuestion = it.totalQuestion
                        isShimmering = false
                    }
                }
            }

            productMostHelpfulMap?.run {
                listOfReviews = it.helpfulReviews
                imageReviews = it.imageReviews
                rating = it.rating
            }

            productMerchantVoucherMap?.run {
                voucherData = ArrayList(it.vouchers)
            }
        }
    }

    fun updateDataP3(context: Context?, it: ProductInfoP3) {
        productShipingInfoMap?.run {
            data.first().subtitle = context?.getString(R.string.ongkir_pattern_string_dynamic_pdp, it.rateEstSummarizeText?.minPrice, "${it.rateEstSummarizeText?.destination}")
                    ?: ""
        }
    }

    fun updateRecomData(data: List<RecommendationWidget>) {
        listProductRecomMap?.run {
            forEach {
                when (it.name) {
                    ProductDetailConstant.PDP_1 -> {
                        fillRecomData(it, data, 0)
                    }
                    ProductDetailConstant.PDP_2 -> {
                        fillRecomData(it, data, 1)
                    }
                    ProductDetailConstant.PDP_3 -> {
                        fillRecomData(it, data, 2)
                    }
                    ProductDetailConstant.PDP_4 -> {
                        fillRecomData(it, data, 3)
                    }
                }
            }
        }
    }

    fun updateImageAfterClickVariant(it: MutableList<Media>) {
        mediaMap?.shouldRenderImageVariant = true
        mediaMap?.listOfMedia = DynamicProductDetailMapper.convertMediaToDataModel(it)
    }

    fun updateVariantData(processedVariant: List<VariantCategory>?) {
        productNewVariantDataModel?.listOfVariantCategory = processedVariant
    }

    fun updateNearestWarehouseData(data: ProductSnapshotDataModel.NearestWarehouseDataModel) {
        basicContentMap?.nearestWarehouseDataModel = data
        snapShotMap?.nearestWarehouseDataModel = data
    }

    fun updateDiscussionData(discussionMostHelpful: DiscussionMostHelpful) {
        productDiscussionMostHelpfulMap?.run {
            questions = discussionMostHelpful.questions
            totalQuestion = discussionMostHelpful.totalQuestion
            isShimmering = false
        }
    }

    private fun mapToCardModel(data: RecommendationWidget): List<ProductCardModel> {
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

    private fun fillRecomData(dataModel: ProductRecommendationDataModel, recomWidget: List<RecommendationWidget>, position: Int) {
        recomWidget.getOrNull(position)?.let { recom ->
            dataModel.recomWidgetData = recom
            dataModel.cardModel = mapToCardModel(recom)
        }
    }
}