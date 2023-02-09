package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.getAddToCartQuantity

object ProductCardMapper {
    private const val DEFAULT_PARENT_PRODUCT_ID = "0"

    fun mapRecomWidgetToProductList(
        headerName: String,
        recomWidget: RecommendationWidget,
        miniCartData: MiniCartSimplifiedData?,
        needToChangeMaxLinesName: Boolean
    ): List<ProductCardCompactCarouselItemUiModel> {
        return recomWidget.recommendationItemList.map { product ->
            val productId = product.productId.toString()
            val quantity = getAddToCartQuantity(productId, miniCartData)
            val parentId = product.parentID.toString()

            ProductCardCompactCarouselItemUiModel(
                shopId = product.shopId.toString(),
                shopName = product.shopName,
                shopType = product.shopType,
                appLink = product.appUrl,
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
                    needToShowQuantityEditor = true,
                    labelGroupList = mapLabelGroup(product),
                    usePreDraw = true,
                    needToChangeMaxLinesName = needToChangeMaxLinesName
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
