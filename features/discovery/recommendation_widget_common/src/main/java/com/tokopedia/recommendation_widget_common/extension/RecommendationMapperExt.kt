package com.tokopedia.recommendation_widget_common.extension

import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.ProductListType
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationBanner
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationSpecificationLabels
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationSpecificationLabelsBullet
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.viewtoview.ViewToViewItemData
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by Lukas on 2/15/21.
 */

fun List<RecommendationEntity.RecommendationData>.mappingToRecommendationModel(): List<RecommendationWidget> {
    return map { recommendationData ->
        recommendationData.toRecommendationWidget()
    }
}

const val SPEC_TYPE_TEXT = "text"
const val SPEC_TYPE_BULLET = "bullet"

fun RecommendationEntity.RecommendationData.toRecommendationWidget(): RecommendationWidget {
    return RecommendationWidget(
        recommendationItemList = recommendation.mapIndexed { index, recommendation ->
            RecommendationItem(
                productId = recommendation.id,
                name = recommendation.name,
                categoryBreadcrumbs = recommendation.categoryBreadcrumbs,
                url = recommendation.url,
                appUrl = recommendation.appUrl,
                clickUrl = recommendation.clickUrl,
                wishlistUrl = recommendation.wishlistUrl,
                trackerImageUrl = recommendation.trackerImageUrl,
                imageUrl = recommendation.imageUrl,
                price = recommendation.price,
                priceInt = recommendation.priceInt,
                departmentId = recommendation.departmentId,
                rating = recommendation.rating,
                ratingAverage = recommendation.ratingAverage,
                countReview = recommendation.countReview,
                stock = if (isTokonow() && recommendation.maxOrder != 0) recommendation.maxOrder else recommendation.stock,
                recommendationType = recommendation.recommendationType,
                isTopAds = recommendation.isIsTopads,
                isWishlist = recommendation.isWishlist,
                slashedPrice = recommendation.slashedPrice,
                slashedPriceInt = recommendation.slashedPriceInt,
                discountPercentage = if (recommendation.discountPercentage > 0) "${recommendation.discountPercentage}%" else "",
                discountPercentageInt = recommendation.discountPercentage,
                position = index,
                shopId = recommendation.shop.id,
                shopName = recommendation.shop.name,
                warehouseId = recommendation.warehouseId,
                quantity = getItemQuantityBasedOnLayoutType(),
                header = title,
                pageName = pageName,
                minOrder = recommendation.minOrder,
                maxOrder = recommendation.maxOrder,
                location = if (isTokonow()) "" else recommendation.shop.city,
                badgesUrl = if (isTokonow()) listOf<String>() else recommendation.badges.map { it.imageUrl },
                type = layoutType,
                isFreeOngkirActive = if (isTokonow()) false else recommendation.freeOngkirInformation.isActive,
                freeOngkirImageUrl = if (isTokonow()) "" else recommendation.freeOngkirInformation.imageUrl,
                labelGroupList = recommendation.labelGroups.map {
                    RecommendationLabel(title = it.title, type = it.type, position = it.position, imageUrl = it.imageUrl)
                },
                isGold = recommendation.shop.isGold,
                isOfficial = recommendation.shop.isOfficial,
                specs = recommendation.specificationsLabels.map {
                    RecommendationSpecificationLabels(
                        specTitle = it.key,
                        specSummary = if (it.type == SPEC_TYPE_TEXT) it.value else "",
                        recommendationSpecificationLabelsBullet = if (it.type == SPEC_TYPE_BULLET) {
                            it.content.map { content ->
                                RecommendationSpecificationLabelsBullet(
                                    specsSummary = content.description,
                                    icon = content.iconUrl
                                )
                            }
                        } else {
                            listOf()
                        }
                    )
                },
                parentID = recommendation.parentID,
                addToCartType = getAtcType(),
                anchorProductId = this.recommendation.firstOrNull()?.id.toString()
            )
        },
        title = title,
        foreignTitle = foreignTitle,
        subtitle = subtitle,
        source = source,
        tid = tid,
        widgetUrl = widgetUrl,
        layoutType = layoutType,
        seeMoreAppLink = seeMoreAppLink,
        currentPage = pagination.currentPage,
        nextPage = pagination.nextPage,
        prevPage = pagination.prevPage,
        hasNext = pagination.hasNext,
        pageName = pageName,
        recommendationBanner = campaign.mapToBannerData(),
        isTokonow = isTokonow(),
        endDate = campaign.endDate,
    )
}

fun List<RecommendationItem>.toProductCardModels(
    hasThreeDots: Boolean = false
): List<ProductCardModel> {
    return map {
        it.toProductCardModel(hasThreeDots = hasThreeDots)
    }
}

fun RecommendationItem.toProductCardModel(
    hasAddToCartButton: Boolean = false,
    addToCartButtonType: Int = UnifyButton.Type.TRANSACTION,
    hasThreeDots: Boolean = false,
    cardInteraction: Boolean = false,
    productCardListType: ProductListType = ProductListType.CONTROL,
    cardType: Int = CardUnify2.TYPE_SHADOW,
): ProductCardModel {
    var variant: ProductCardModel.Variant? = null
    var nonVariant: ProductCardModel.NonVariant? = null
    var hasThreeDotsFinalValue = hasThreeDots
    if (addToCartType == RecommendationItem.AddToCartType.QuantityEditor) {
        hasThreeDotsFinalValue = false
        variant = ProductCardModel.Variant(quantity = quantity)
        nonVariant = ProductCardModel.NonVariant(quantity = quantity, minQuantity = minOrder, maxQuantity = stock)
    }
    return ProductCardModel(
        slashedPrice = slashedPrice,
        productName = name,
        formattedPrice = price,
        productImageUrl = imageUrl,
        isTopAds = isTopAds,
        isWishlistVisible = true,
        hasThreeDots = hasThreeDotsFinalValue,
        isWishlisted = isWishlist,
        discountPercentage = discountPercentage,
        reviewCount = countReview,
        ratingCount = rating,
        shopLocation = location,
        countSoldRating = ratingAverage,
        shopBadgeList = badgesUrl.map {
            ProductCardModel.ShopBadge(imageUrl = it)
        },
        freeOngkir = ProductCardModel.FreeOngkir(
            isActive = isFreeOngkirActive,
            imageUrl = freeOngkirImageUrl
        ),
        labelGroupList = labelGroupList.map {
            ProductCardModel.LabelGroup(position = it.position, title = it.title, type = it.type, imageUrl = it.imageUrl)
        },
        hasAddToCartButton = if (addToCartType == RecommendationItem.AddToCartType.None) hasAddToCartButton else true,
        addToCartButtonType = addToCartButtonType,
        variant = if (isProductHasParentID()) variant else null,
        nonVariant = if (isProductHasParentID()) null else nonVariant,
        cardInteraction = cardInteraction,
        cardType = cardType,
        productListType = productCardListType,
    )
}

fun List<RecommendationItem>.toViewToViewItemModels(): List<ViewToViewItemData> {
    return map {
        it.toViewToViewItem()
    }
}

fun RecommendationItem.toViewToViewItem(): ViewToViewItemData {
    return ViewToViewItemData(
        name = name,
        price = price,
        imageUrl = imageUrl,
        departmentId = departmentId.toString(),
        url = url,
        recommendationData = this
    )
}

var LABEL_FULFILLMENT: String = "fulfillment"
val LAYOUTTYPE_HORIZONTAL_ATC: String = "horizontal-atc"
val LAYOUTTYPE_INFINITE_ATC: String = "infinite-atc"
val PAGENAME_IDENTIFIER_RECOM_ATC: String = "hatc"
val DEFAULT_QTY_0: Int = 0
val DEFAULT_QTY_1: Int = 1

// tokonow validation
private fun RecommendationEntity.RecommendationData.isTokonow(): Boolean {
    return layoutType == LAYOUTTYPE_HORIZONTAL_ATC || layoutType == LAYOUTTYPE_INFINITE_ATC
}

private fun RecommendationEntity.RecommendationData.getItemQuantityBasedOnLayoutType(): Int {
    return if (this.isTokonow()) DEFAULT_QTY_0 else DEFAULT_QTY_1
}

fun List<RecommendationLabel>.hasLabelGroupFulfillment(): Boolean {
    return this.any { it.position == LABEL_FULFILLMENT }
}

private fun RecommendationEntity.RecommendationData.getAtcType(): RecommendationItem.AddToCartType {
    return if (layoutType == LAYOUTTYPE_HORIZONTAL_ATC || layoutType == LAYOUTTYPE_INFINITE_ATC) {
        RecommendationItem.AddToCartType.QuantityEditor
    } else if (pageName.contains(PAGENAME_IDENTIFIER_RECOM_ATC)) {
        RecommendationItem.AddToCartType.DirectAtc
    } else {
        RecommendationItem.AddToCartType.None
    }
}

fun RecommendationEntity.RecommendationCampaign.mapToBannerData(): RecommendationBanner? {
    assets?.banner?.let {
        return RecommendationBanner(
            applink = appLandingPageLink,
            imageUrl = it.apps,
            thematicID = thematicID
        )
    }
    return null
}

fun mappingMiniCartDataToRecommendation(recomWidget: RecommendationWidget, miniCartMap: MutableMap<MiniCartItemKey, MiniCartItem>?) {
    val recomItemList = mutableListOf<RecommendationItem>()
    recomWidget.recommendationItemList.forEach { item ->
        miniCartMap?.let {
            if (item.isProductHasParentID()) {
                var variantTotalItems = 0
                variantTotalItems += it.getMiniCartItemParentProduct(item.parentID.toString())?.totalQuantity ?: 0
                item.updateItemCurrentStock(variantTotalItems)
            } else {
                item.updateItemCurrentStock(
                    it.getMiniCartItemProduct(item.productId.toString())?.quantity
                        ?: 0
                )
            }
        }
        recomItemList.add(item)
    }
    recomWidget.recommendationItemList = recomItemList
}
