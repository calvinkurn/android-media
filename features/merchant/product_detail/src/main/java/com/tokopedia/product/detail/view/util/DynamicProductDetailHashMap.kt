package com.tokopedia.product.detail.view.util

import android.content.Context
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.joinToStringWithLast
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.*
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.financing.PDPInstallmentRecommendationResponse
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToLong

class DynamicProductDetailHashMap(private val context: Context, private val mapOfData: Map<String, DynamicPdpDataModel>) {

    val socialProofMap: ProductSocialProofDataModel?
        get() = mapOfData[ProductDetailConstant.SOCIAL_PROOF] as? ProductSocialProofDataModel

    val snapShotMap: ProductSnapshotDataModel
        get() = mapOfData[ProductDetailConstant.PRODUCT_SNAPSHOT] as ProductSnapshotDataModel

    val shopInfoMap: ProductShopInfoDataModel?
        get() = mapOfData[ProductDetailConstant.SHOP_INFO] as? ProductShopInfoDataModel

    val productInfoMap: ProductInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_INFO] as? ProductInfoDataModel

    val productDiscussionMap: ProductDiscussionDataModel?
        get() = mapOfData[ProductDetailConstant.DISCUSSION] as? ProductDiscussionDataModel

    val productMostHelpfulMap: ProductMostHelpfulReviewDataModel?
        get() = mapOfData[ProductDetailConstant.MOST_HELPFUL_REVIEW] as? ProductMostHelpfulReviewDataModel

    val productTradeinMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.TRADE_IN] as? ProductGeneralInfoDataModel

    val productMerchantVoucherMap: ProductMerchantVoucherDataModel?
        get() = mapOfData[ProductDetailConstant.SHOP_VOUCHER] as? ProductMerchantVoucherDataModel

    val productLastSeenMap: ProductLastSeenDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_LAST_SEEN] as? ProductLastSeenDataModel

    val productVariantInfoMap: ProductGeneralInfoDataModel?
        get() = mapOfData[ProductDetailConstant.PRODUCT_VARIANT_INFO] as? ProductGeneralInfoDataModel

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

    val listProductRecomMap: List<ProductRecommendationDataModel>? = mapOfData.filterKeys {
        it == ProductDetailConstant.PDP_1 || it == ProductDetailConstant.PDP_2
                || it == ProductDetailConstant.PDP_3 || it == ProductDetailConstant.PDP_4
    }.map {
        it.value as ProductRecommendationDataModel
    }

    val getShopInfo: ProductShopInfoDataModel
        get() = shopInfoMap ?: ProductShopInfoDataModel()

    fun updateDataP1(dataP1: DynamicProductInfoP1?) {
        dataP1?.let {
            snapShotMap.run {
                dynamicProductInfoP1 = it
                media = it.data.media.map { media ->
                    ProductMediaDataModel(media.type, media.uRL300, media.uRLOriginal, media.uRLThumbnail, media.description, media.videoURLAndroid, media.isAutoplay)
                }
            }

            valuePropositionDataModel?.run {
                isOfficialStore = it.data.isOS
            }

            productDiscussionMap?.run {
                shopId = it.basic.shopID
                talkCount = it.basic.stats.countTalk
            }

            socialProofMap?.run {
                txStats = it.basic.txStats
                stats = it.basic.stats
            }

            productInfoMap?.run {
                dynamicProductInfoP1 = it
            }

            productWholesaleInfoMap?.run {
                val minPrice = it.data.wholesale?.minBy { it.price.value }?.price?.value ?: return
                data.first().subtitle = context.getString(R.string.label_format_wholesale, minPrice.getCurrencyFormatted())
            }

            productLastSeenMap?.run {
                /*
                 * Sometimes this lastUpdateUnix doesn't has Long value like "123"
                 * If P1 updated by selected variant this value will be formatted dated "dd-mm-yyy , hh:mm"
                 */
                val isLongFormat = try {
                    it.data.price.lastUpdateUnix.toLong()
                    true
                } catch (e: Throwable) {
                    false
                }

                lastSeen = if (isLongFormat) {
                    val date = Date(it.data.price.lastUpdateUnix.toLong() * 1000)
                    val dateString = date.toFormattedString("dd-mm-yyyy , hh:mm")
                    "$dateString WIB"
                } else {
                    it.data.price.lastUpdateUnix
                }

            }
        }
    }

    fun updateDataTradein(tradeinResponse: ValidateTradeInPDP) {
        productTradeinMap?.run {
            snapShotMap.shouldShowTradein = true
            data.first().subtitle = if (tradeinResponse.usedPrice > 0) {
                context.getString(R.string.text_price_holder, CurrencyFormatUtil.convertPriceValueToIdrFormat(tradeinResponse.usedPrice, true))
            } else {
                context.getString(R.string.trade_in_exchange)
            }
        }
    }

    fun updateDataInstallment(financingData: PDPInstallmentRecommendationResponse, isOs: Boolean) {
        productInstallmentInfoMap?.run {
            data.first().subtitle = String.format(context.getString(R.string.new_installment_template),
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            (if (isOs) financingData.response.data.osMonthlyPrice
                            else financingData.response.data.monthlyPrice).roundToLong(), false))
        }
    }

    fun updateDataP2Shop(dataP2: ProductInfoP2ShopData?) {
        dataP2?.let {
            shopInfoMap?.run {
                shopInfo = it.shopInfo
            }

            productFullfilmentMap?.run {
                data.first().subtitle = context.getString(R.string.multiorigin_desc)
            }

            snapShotMap.run {
                isAllowManage = it.shopInfo?.isAllowManage ?: 0
                nearestWarehouse = it.nearestWarehouse
                statusTitle = it.shopInfo?.statusInfo?.statusTitle ?: ""
                statusTitle = it.shopInfo?.statusInfo?.statusMessage ?: ""
                shopStatus = it.shopInfo?.statusInfo?.shopStatus ?: 1
            }

            productInfoMap?.run {
                shopName = it.shopInfo?.shopCore?.name ?: ""
            }
        }
    }

    fun updateDataP2Login(it: ProductInfoP2Login) {
        snapShotMap.apply {
            isWishlisted = it.isWishlisted
        }
    }

    fun updateDataP2General(dataP2General: ProductInfoP2General?) {
        dataP2General?.let {
            shopInfoMap?.run {
                shopFeature = it.shopFeature
                shopBadge = it.shopBadge
            }
            productInfoMap?.run {
                productSpecification = it.productSpecificationResponse
            }

            orderPriorityMap?.run {
                data.first().subtitle = it.shopCommitment.staticMessages.pdpMessage
            }

            productProtectionMap?.run {
                data.first().subtitle = it.productPurchaseProtectionInfo.ppItemDetailPage!!.subTitlePDP
                        ?: ""
            }

            socialProofMap?.run {
                wishListCount = it.wishlistCount.count
                rating = it.rating
            }

            productDiscussionMap?.run {
                latestTalk = it.latestTalk
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

    fun updateDataP3(it: ProductInfoP3) {
        productShipingInfoMap?.run {
            data.first().subtitle = " ${context.getString(R.string.shipping_pattern_string, it.ratesModel?.services?.size
                    ?: 0)}${context.getString(R.string.ongkir_pattern_string, it.rateEstSummarizeText?.minPrice, "<b>${it.rateEstSummarizeText?.destination}</b>")}"
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

    fun updateVariantInfo(productVariant: ProductVariant, selectedOptionString: String) {
        productVariantInfoMap?.run {
            data.first().subtitle =
                    if (selectedOptionString.isEmpty()) {
                        "Pilih " +
                                productVariant.variant.map { it.name }.joinToStringWithLast(separator = ", ",
                                        lastSeparator = " dan ")
                    } else {
                        selectedOptionString
                    }
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
                        ProductCardModel.ShopBadge(imageUrl = it ?: "")
                    },
                    freeOngkir = ProductCardModel.FreeOngkir(
                            isActive = it.isFreeOngkirActive,
                            imageUrl = it.freeOngkirImageUrl
                    ),
                    labelPromo = ProductCardModel.Label(
                            title = it.labelPromo.title,
                            type = it.labelPromo.type
                    ),
                    labelCredibility = ProductCardModel.Label(
                            title = it.labelCredibility.title,
                            type = it.labelCredibility.type
                    ),
                    labelOffers = ProductCardModel.Label(
                            title = it.labelOffers.title,
                            type = it.labelOffers.type
                    )
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