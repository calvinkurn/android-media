package com.tokopedia.product.detail.view.util

import com.tokopedia.kotlin.extensions.view.joinToStringWithLast
import com.tokopedia.product.detail.common.data.model.product.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.ProductInfoP2General
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

class DynamicProductDetailHashMap(private val mapOfData: Map<String, DynamicPDPDataModel>) {

    companion object {
        private const val SOCIAL_PROOF = "social_proof"
        private const val PRODUCT_SNAPSHOT = "product_snapshot"
        private const val SHOP_INFO = "shop_info"
        private const val PRODUCT_INFO = "product_info"
        private const val DISCUSSION = "discussion"
        private const val IMAGE_REVIEW = "image_review"
        private const val MOST_HELPFUL_REVIEW = "most_helpful_review"
        private const val TRADE_IN = "trade_in"
        private const val SHOP_VOUCHER = "shop_voucher"
        private const val PRODUCT_LIST = "product_list"
        private const val TOP_ADS_LAIN = "top_ads_lain"
        private const val TOP_ADS_SUKA = "top_ads_suka"
        private const val TOP_ADS_BERSAMA = "top_ads_bersama"
        private const val TOP_ADS_PENCARIAN = "top_ads_pencarian"
        private const val PRODUCT_LAST_SEEN = "product_last_seen"
        private const val PRODUCT_VARIANT_INFO = "variant"
    }

    val socialProofMap: ProductSocialProofDataModel?
        get() = mapOfData[SOCIAL_PROOF] as ProductSocialProofDataModel

    val snapShotMap: ProductSnapshotDataModel
        get() = mapOfData[PRODUCT_SNAPSHOT] as ProductSnapshotDataModel

    val shopInfoMap: ProductShopInfoDataModel?
        get() = mapOfData[SHOP_INFO] as ProductShopInfoDataModel

    val productInfoMap: ProductInfoDataModel?
        get() = mapOfData[PRODUCT_INFO] as ProductInfoDataModel

    val productDiscussionMap: ProductDiscussionDataModel?
        get() = mapOfData[DISCUSSION] as ProductDiscussionDataModel

    val productImageReviewMap: ProductImageReviewDataModel?
        get() = mapOfData[IMAGE_REVIEW] as ProductImageReviewDataModel

    val productMostHelpfulMap: ProductMostHelpfulReviewDataModel?
        get() = mapOfData[MOST_HELPFUL_REVIEW] as ProductMostHelpfulReviewDataModel

    val productTradeinMap: ProductTradeinDataModel?
        get() = mapOfData[TRADE_IN] as ProductTradeinDataModel

    val productMerchantVoucherMap: ProductMerchantVoucherDataModel?
        get() = mapOfData[SHOP_VOUCHER] as ProductMerchantVoucherDataModel

    val productLastSeenMap: ProductLastSeenDataModel?
        get() = mapOfData[PRODUCT_LAST_SEEN] as ProductLastSeenDataModel

    val productVariantInfoMap: ProductGeneralInfoDataModel?
        get() = mapOfData[PRODUCT_VARIANT_INFO] as ProductGeneralInfoDataModel

    val listProductRecomMap: List<ProductRecommendationDataModel>? = mapOfData.filterKeys {
        it == TOP_ADS_LAIN || it == TOP_ADS_BERSAMA || it == TOP_ADS_PENCARIAN || it == TOP_ADS_SUKA
    }.map {
        it.value as ProductRecommendationDataModel
    }

    val getShopInfo: ProductShopInfoDataModel
        get() = shopInfoMap ?: ProductShopInfoDataModel()

    fun updateDataP1(data: ProductInfoP1?) {
        data?.let {
            snapShotMap.productInfoP1 = it.productInfo
            snapShotMap.media = it.productInfo.media

            socialProofMap?.run {
                productInfo = it.productInfo
            }

            productDiscussionMap?.run {
                shopId = it.productInfo.basic.shopID.toString()
                talkCount = it.productInfo.stats.countTalk
            }

            productInfoMap?.run {
                productInfo = it.productInfo
            }

            productLastSeenMap?.run {
                lastSeen = it.productInfo.basic.lastUpdatePrice
            }

        }
    }

    fun updateDataP2Shop(data: ProductInfoP2ShopData?) {
        data?.let {
            shopInfoMap?.run {
                shopInfo = it.shopInfo
            }

            snapShotMap.shopInfo = it.shopInfo ?: ShopInfo()
            snapShotMap.nearestWarehouse = it.nearestWarehouse

            productInfoMap?.run {
                shopInfo = it.shopInfo
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
                productInfoP2 = it
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

    fun updateDataP2Login(data: ProductInfoP2Login?) {
        data?.let {
            snapShotMap.isWishlisted = it.isWishlisted
        }
    }

    fun updateRecomData(data: List<RecommendationWidget>) {
        listProductRecomMap?.run {
            forEach {
                when (it.name) {
                    TOP_ADS_SUKA -> {
                        data.getOrNull(0)?.let { recom ->
                            it.recomWidgetData = recom
                        }
                    }
                    TOP_ADS_PENCARIAN -> {
                        data.getOrNull(1)?.let { recom ->
                            it.recomWidgetData = recom
                        }
                    }
                    TOP_ADS_BERSAMA -> {
                        data.getOrNull(2)?.let { recom ->
                            it.recomWidgetData = recom
                        }
                    }
                    TOP_ADS_LAIN -> {
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