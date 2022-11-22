package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel.LabelGroup
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationViewUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper

object ProductRecommendationMapper {
    private fun mapRecommendationItemToProductCard(
        item: RecommendationItem,
        miniCartData: MiniCartSimplifiedData?
    ): TokoNowProductCardViewUiModel = TokoNowProductCardViewUiModel(
        productId = item.productId.toString(),
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
        needToShowQuantityEditor = item.minOrder <= item.maxOrder && item.maxOrder != VariantConstant.DEFAULT_MAX_ORDER,
        labelGroupList = item.labelGroupList.map {
            LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.imageUrl
            )
        }
    )

    fun mapResponseToProductRecommendation(
        recommendationWidget: RecommendationWidget,
        miniCartData: MiniCartSimplifiedData?
    ): TokoNowProductRecommendationViewUiModel {
        val productModels = recommendationWidget.recommendationItemList.map { item ->
            TokoNowProductCardCarouselItemUiModel(
                productCardModel = mapRecommendationItemToProductCard(
                    item = item,
                    miniCartData = miniCartData
                ),
                recommendationType = item.recommendationType,
                shopId = item.shopId.toString(),
                shopType = item.shopType,
                shopName = item.shopName,
                appLink = item.appUrl
            )
        }
        val seeMoreModel = TokoNowSeeMoreCardCarouselUiModel(
            headerName = recommendationWidget.title,
            appLink = recommendationWidget.seeMoreAppLink
        )
        val headerModel = TokoNowDynamicHeaderUiModel(
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

    fun mapProductItemToRecommendationItem(product: TokoNowProductCardCarouselItemUiModel): RecommendationItem {
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
            recommendationType = product.recommendationType
        )
    }
}
