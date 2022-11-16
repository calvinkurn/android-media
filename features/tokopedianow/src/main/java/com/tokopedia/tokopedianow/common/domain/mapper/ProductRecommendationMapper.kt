package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.model.LabelGroup
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
                shopId = item.shopId.toString(),
                shopType = item.shopType,
                shopName = item.shopName
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
}
