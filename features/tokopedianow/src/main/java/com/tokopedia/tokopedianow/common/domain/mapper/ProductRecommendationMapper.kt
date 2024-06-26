package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel.LabelGroup
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationViewUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper

object ProductRecommendationMapper {
    private fun mapRecommendationItemToProductCard(
        item: RecommendationItem,
        miniCartData: MiniCartSimplifiedData?,
        hasBlockedAddToCart: Boolean
    ): ProductCardCompactUiModel = ProductCardCompactUiModel(
        productId = item.productId.toString(),
        warehouseId = item.warehouseId.toString(),
        imageUrl = item.imageUrl,
        minOrder = item.minOrder,
        maxOrder = item.maxOrder,
        availableStock = item.stock,
        orderQuantity = HomeLayoutMapper.getAddToCartQuantity(item.productId.toString(), miniCartData),
        price = item.price,
        discount = item.discountPercentage,
        slashPrice = item.slashedPrice,
        name = item.name,
        rating = item.ratingAverage,
        isVariant = item.isProductHasParentID(),
        needToShowQuantityEditor = true,
        labelGroupList = item.labelGroupList.map {
            LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.imageUrl
            )
        },
        hasBlockedAddToCart = hasBlockedAddToCart,
        usePreDraw = true
    )

    fun mapResponseToProductRecommendation(
        recommendationWidget: RecommendationWidget,
        miniCartData: MiniCartSimplifiedData?,
        hasBlockedAddToCart: Boolean
    ): TokoNowProductRecommendationViewUiModel {
        val productModels = recommendationWidget.recommendationItemList.map { item ->
            ProductCardCompactCarouselItemUiModel(
                productCardModel = mapRecommendationItemToProductCard(
                    item = item,
                    miniCartData = miniCartData,
                    hasBlockedAddToCart = hasBlockedAddToCart
                ),
                recommendationType = item.recommendationType,
                shopId = item.shopId.toString(),
                shopType = item.shopType,
                shopName = item.shopName,
                appLink = item.appUrl,
                headerName = recommendationWidget.title,
                pageName = recommendationWidget.pageName
            )
        }
        val seeMoreModel = ProductCardCompactCarouselSeeMoreUiModel(
            headerName = recommendationWidget.title,
            appLink = recommendationWidget.seeMoreAppLink
        )
        val headerModel = TokoNowDynamicHeaderUiModel(
            channelId = recommendationWidget.channelId,
            title = recommendationWidget.title,
            subTitle = recommendationWidget.subtitle,
            ctaTextLink = recommendationWidget.seeMoreAppLink
        )
        return TokoNowProductRecommendationViewUiModel(
            headerModel = headerModel,
            seeMoreModel = seeMoreModel,
            productModels = productModels
        )
    }

    fun mapProductItemToRecommendationItem(product: ProductCardCompactCarouselItemUiModel): RecommendationItem {
        return RecommendationItem(
            productId = product.productCardModel.productId.toLongOrZero(),
            imageUrl = product.productCardModel.imageUrl,
            minOrder = product.productCardModel.minOrder,
            maxOrder = product.productCardModel.maxOrder,
            stock = product.productCardModel.availableStock,
            quantity = product.productCardModel.orderQuantity,
            priceInt = product.productCardModel.price.getDigits().orZero(),
            discountPercentage = product.productCardModel.discount,
            slashedPrice = product.productCardModel.slashPrice,
            name = product.productCardModel.name,
            ratingAverage = product.productCardModel.rating,
            labelGroupList = product.productCardModel.labelGroupList.map {
                RecommendationLabel(
                    position = it.position,
                    type = it.type,
                    title = it.title,
                    imageUrl = it.imageUrl
                )
            },
            shopId = product.shopId.toIntSafely(),
            shopName = product.shopName,
            shopType = product.shopType,
            recommendationType = product.recommendationType,
            header = product.headerName
        )
    }
}
