package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist

import com.tkpd.atcvariant.util.roundToIntOrZero
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupProductModel
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.isReimagineProductCardInBackground
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.productcarditem.Badges
import com.tokopedia.discovery2.data.productcarditem.StylesGroup
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.productcard.ProductCardModel
import kotlin.math.roundToInt

object ProductCardColumnListMapper {
    private const val PERCENTAGE_CHAR = '%'

    fun List<ComponentsItem>.mapToCarouselPagingGroupProductModel(): CarouselPagingGroupProductModel {
        return CarouselPagingGroupProductModel(
            group = CarouselPagingGroupModel(),
            productItemList = mapToProductCardModel()
        )
    }

    private fun List<ComponentsItem>.mapToProductCardModel(): List<ProductCardModel> {
        return map {
            it.data?.firstOrNull()?.let { item ->
                val labelGroupList: ArrayList<ProductCardModel.LabelGroup> = ArrayList()
                ProductCardModel(
                    productImageUrl = item.imageUrlMobile ?: "",
                    productName = item.name.toString(),
                    formattedPrice = item.price.orEmpty(),
                    slashedPrice = item.discountedPrice.orEmpty(),
                    discountPercentage = if (item.discountPercentage?.lastOrNull() != PERCENTAGE_CHAR &&
                        item.discountPercentage?.lastOrNull() != null
                    ) {
                        "${item.discountPercentage}$PERCENTAGE_CHAR"
                    } else {
                        item.discountPercentage.orEmpty()
                    },
                    isInBackground = it.properties.isReimagineProductCardInBackground(),
                    countSoldRating = item.averageRating,
                    isTopAds = item.isTopads ?: false,
                    freeOngkir = ProductCardModel.FreeOngkir(
                        imageUrl = item.freeOngkir?.freeOngkirImageUrl
                            ?: "",
                        isActive = item.freeOngkir?.isActive ?: false
                    ),
                    pdpViewCount = getPDPViewCount(item.pdpView),
                    labelGroupList = labelGroupList.apply {
                        item.labelsGroupList?.forEach { labels ->
                            add(
                                ProductCardModel.LabelGroup(
                                    labels.position,
                                    labels.title,
                                    labels.type,
                                    labels.url,
                                    labels.styles?.mapToProductCardModelStyle().orEmpty()
                                )
                            )
                        }
                    },
                    shopLocation = getShopLocation(item),
                    shopBadgeList = getShopBadgeList(item.badges),
                    stockBarPercentage = setStockProgress(item),
                    stockBarLabel = item.stockWording?.title ?: "",
                    stockBarLabelColor = item.stockWording?.color ?: "",
                    isOutOfStock = (item.isActiveProductCard == false),
//                    hasNotifyMeButton = if (item.stockWording?.title?.isNotEmpty() == true) false else item.hasNotifyMe,
//                    hasThreeDots = item.hasThreeDots,
//                    hasButtonThreeDotsWishlist = item.hasThreeDotsWishlist,
//                    hasAddToCartWishlist = item.hasATCWishlist,
//                    hasAddToCartButton = !item.hasATCWishlist && item.atcButtonCTA == Constant.ATCButtonCTATypes.GENERAL_CART && item.isActiveProductCard == true,
//                    hasSimilarProductWishlist = item.hasSimilarProductWishlist == true,
//                    variant = variantProductCard(item),
//                    nonVariant = nonVariantProductCard(item),
//                    cardInteraction = true,
                    productListType = ProductCardModel.ProductListType.BEST_SELLER
                )
            } ?: ProductCardModel()
        }
    }

    private fun getPDPViewCount(pdpView: String): String {
        val pdpViewData = pdpView.toDoubleOrZero()
        return if (pdpViewData >= Constant.ProductCardModel.PDP_VIEW_THRESHOLD) {
            Utils.getCountView(pdpViewData)
        } else {
            ""
        }
    }

    private fun setStockProgress(dataItem: DataItem): Int {
        val stockSoldPercentage = dataItem.stockSoldPercentage
        if (stockSoldPercentage?.roundToInt() == null) {
            dataItem.stockWording?.title = ""
        } else {
            if (stockSoldPercentage.roundToIntOrZero() !in (Constant.ProductCardModel.SOLD_PERCENTAGE_LOWER_LIMIT) until Constant.ProductCardModel.SOLD_PERCENTAGE_UPPER_LIMIT) {
                dataItem.stockWording?.title = ""
            }
        }
        return stockSoldPercentage?.roundToIntOrZero() ?: 0
    }

    private fun getShopBadgeList(showBadges: List<Badges?>?): List<ProductCardModel.ShopBadge> {
        return ArrayList<ProductCardModel.ShopBadge>().apply {
            showBadges?.firstOrNull()?.let {
                add(
                    ProductCardModel.ShopBadge(
                        isShown = true,
                        imageUrl = it.image_url,
                        title = it.title
                    )
                )
            }
        }
    }

    private fun getShopLocation(dataItem: DataItem): String {
        return if (!dataItem.shopLocation.isNullOrEmpty()) {
            dataItem.shopLocation!!
        } else if (!dataItem.shopName.isNullOrEmpty()) {
            dataItem.shopName!!
        } else {
            ""
        }
    }

    private fun List<StylesGroup>.mapToProductCardModelStyle(): List<ProductCardModel.LabelGroup.Style> {
        val result = this.map { style ->
            ProductCardModel.LabelGroup.Style(style.key, style.value)
        }
        return result
    }
}
