package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.getAddToCartQuantity

object ProductCardMapper {
    private const val DEFAULT_PARENT_PRODUCT_ID = "0"
    private const val DEFAULT_MAX_ORDER = 0

    fun mapRecomWidgetToProductList(
        headerName: String,
        recomWidget: RecommendationWidget,
        miniCartData: MiniCartSimplifiedData?
    ): List<TokoNowProductCardCarouselItemUiModel> {
        return recomWidget.recommendationItemList.map { product ->
            val productId = product.productId.toString()
            val quantity = getAddToCartQuantity(productId, miniCartData)
            val parentId = product.parentID.toString()

            TokoNowProductCardCarouselItemUiModel(
                shopId = product.shopId.toString(),
                shopName = product.shopName,
                shopType = product.shopType,
                parentId = parentId,
                headerName = headerName,
                categoryBreadcrumbs = product.categoryBreadcrumbs,
                productCardModel = TokoNowProductCardViewUiModel(
                    productId = productId,
                    imageUrl = product.imageUrl,
                    minOrder = product.minOrder,
                    maxOrder = product.maxOrder,
                    availableStock = product.stock,
                    orderQuantity = quantity,
                    price = product.price,
                    discount = product.discountPercentage,
                    slashPrice = product.slashedPrice,
                    name = product.name,
                    rating = product.ratingAverage,
                    isVariant = parentId != DEFAULT_PARENT_PRODUCT_ID && parentId.isNotBlank(),
                    needToShowQuantityEditor = product.minOrder <= product.maxOrder && product.maxOrder != DEFAULT_MAX_ORDER,
                    labelGroupList = mapLabelGroup(product)
                )
            )
        }
    }

    private fun mapLabelGroup(item: RecommendationItem): List<TokoNowProductCardViewUiModel.LabelGroup> {
        return item.labelGroupList.map {
            TokoNowProductCardViewUiModel.LabelGroup(it.position, it.title, it.type, it.imageUrl)
        }
    }
}
