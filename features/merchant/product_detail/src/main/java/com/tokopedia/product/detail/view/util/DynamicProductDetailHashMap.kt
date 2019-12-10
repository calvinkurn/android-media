package com.tokopedia.product.detail.view.util

import android.content.Context
import com.tokopedia.abstraction.common.utils.KMNumbers
import com.tokopedia.kotlin.extensions.view.joinToStringWithLast
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.ProductInfoP2General
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

class DynamicProductDetailHashMap(private val context: Context, private val mapOfData: Map<String, DynamicPDPDataModel>) {

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

    val productImageReviewMap: ProductImageReviewDataModel?
        get() = mapOfData[ProductDetailConstant.IMAGE_REVIEW] as? ProductImageReviewDataModel

    val productMostHelpfulMap: ProductMostHelpfulReviewDataModel?
        get() = mapOfData[ProductDetailConstant.MOST_HELPFUL_REVIEW] as? ProductMostHelpfulReviewDataModel

    val productTradeinMap: ProductTradeinDataModel?
        get() = mapOfData[ProductDetailConstant.TRADE_IN] as? ProductTradeinDataModel

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

    val listProductRecomMap: List<ProductRecommendationDataModel>? = mapOfData.filterKeys {
        it == ProductDetailConstant.PDP_1 || it == ProductDetailConstant.PDP_2
                || it == ProductDetailConstant.PDP_3 || it == ProductDetailConstant.PDP_4
    }.map {
        it.value as ProductRecommendationDataModel
    }

    val getShopInfo: ProductShopInfoDataModel
        get() = shopInfoMap ?: ProductShopInfoDataModel()

    fun updateDataP1(data: DynamicProductInfoP1?) {
        data?.let {
            snapShotMap.run {
                dynamicProductInfoP1 = it
                media = it.data.media.map { media ->
                    ProductMediaDataModel(media.type, media.uRL300, media.uRLOriginal, media.uRLThumbnail, media.description, media.videoURLAndroid, media.isAutoplay)
                }
                isWishlisted = it.data.isWishlist
            }

            productDiscussionMap?.run {
                shopId = it.basic.shopID
                // Should be in p2
//                talkCount = it.productInfo.stats.countTalk
            }

            productInfoMap?.run {
                dynamicProductInfoP1 = it
            }

            productWholesaleInfoMap?.run {
                val minPrice = it.data.wholesale?.minBy { it.price.value }?.price?.value ?: return
                description = context.getString(R.string.label_format_wholesale, minPrice.getCurrencyFormatted())
            }

            productLastSeenMap?.run {
                lastSeen = KMNumbers.formatRupiahString(it.data.price.value.toLong())
            }

        }
    }

    fun updateDataP2Shop(data: ProductInfoP2ShopData?) {
        data?.let {
            shopInfoMap?.run {
                shopInfo = it.shopInfo
            }

            snapShotMap.run {
                isAllowManage = it.shopInfo?.isAllowManage ?: 0
                nearestWarehouse = it.nearestWarehouse
            }

            productInfoMap?.run {
                shopName = it.shopInfo?.shopCore?.name ?: ""
            }
        }
    }

    fun updateDataP2General(data: ProductInfoP2General?) {
        data?.let {
            shopInfoMap?.run {
                shopFeature = it.shopFeature
                shopBadge = it.shopBadge
            }
            productInfoMap?.run {
                productSpecification = it.productSpecificationResponse
            }

            socialProofMap?.run {
                wishListCount = it.wishlistCount.count
                rating = it.rating
            }

            productDiscussionMap?.run {
                latestTalk = it.latestTalk
            }

            productImageReviewMap?.run {
                productInfoP2General = it
            }

            productMostHelpfulMap?.run {
                listOfReviews = it.helpfulReviews
            }

            productMerchantVoucherMap?.run {
                voucherData = ArrayList(it.vouchers)
            }

        }
    }

    fun updateRecomData(data: List<RecommendationWidget>) {
        listProductRecomMap?.run {
            forEach {
                when (it.name) {
                    ProductDetailConstant.PDP_1 -> {
                        data.getOrNull(0)?.let { recom ->
                            it.recomWidgetData = recom
                        }
                    }
                    ProductDetailConstant.PDP_2 -> {
                        data.getOrNull(1)?.let { recom ->
                            it.recomWidgetData = recom
                        }
                    }
                    ProductDetailConstant.PDP_3 -> {
                        data.getOrNull(2)?.let { recom ->
                            it.recomWidgetData = recom
                        }
                    }
                    ProductDetailConstant.PDP_4 -> {
                        data.getOrNull(3)?.let { recom ->
                            it.recomWidgetData = recom
                        }
                    }
                }
            }
        }
    }

    fun updateVariantInfo(productVariant: ProductVariant, selectedOptionString: String) {
        productVariantInfoMap?.run {
            description =
                    if (selectedOptionString.isEmpty()) {
                        "Pilih " +
                                productVariant.variant.map { it.name }.joinToStringWithLast(separator = ", ",
                                        lastSeparator = " dan ")
                    } else {
                        selectedOptionString
                    }
        }
    }

}