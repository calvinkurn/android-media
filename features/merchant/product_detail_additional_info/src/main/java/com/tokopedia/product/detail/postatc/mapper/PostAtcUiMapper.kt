package com.tokopedia.product.detail.postatc.mapper

import com.tokopedia.product.detail.postatc.base.PostAtcUiModel
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel.Type.ADDONS
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel.Type.PRODUCT_INFO
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel.Type.RECOMMENDATION
import com.tokopedia.product.detail.postatc.data.model.PostAtcInfo
import com.tokopedia.product.detail.postatc.data.model.PostAtcLayout
import com.tokopedia.product.detail.postatc.view.component.addons.AddonsUiModel
import com.tokopedia.product.detail.postatc.view.component.productinfo.ProductInfoUiModel
import com.tokopedia.product.detail.postatc.view.component.recommendation.RecommendationUiModel

internal fun List<PostAtcLayout.Component>.mapToUiModel(
    postAtcInfo: PostAtcInfo
): List<PostAtcUiModel> = mapNotNull {
    when (it.type) {
        PRODUCT_INFO -> toProductInfoUiModel(it)
        RECOMMENDATION -> toRecommendationUiModel(it)
        ADDONS -> toAddonsUiModel(it, postAtcInfo)
        else -> null
    }
}

private fun toProductInfoUiModel(component: PostAtcLayout.Component): ProductInfoUiModel? {
    val data = component.data.firstOrNull() ?: return null
    return ProductInfoUiModel(
        title = data.title,
        subtitle = data.subtitle,
        imageLink = data.image,
        buttonText = data.button.text,
        cartId = data.button.cartId,
        name = component.name,
        type = component.type
    )
}

private fun toRecommendationUiModel(component: PostAtcLayout.Component): RecommendationUiModel {
    return RecommendationUiModel(
        name = component.name,
        type = component.type
    )
}

private fun toAddonsUiModel(
    component: PostAtcLayout.Component,
    postAtcInfo: PostAtcInfo
): AddonsUiModel? {
    val data = component.data.firstOrNull() ?: return null
    val addons = postAtcInfo.addons ?: return null
    return AddonsUiModel(
        name = component.name,
        type = component.type,
        data = AddonsUiModel.Data(
            cartId = postAtcInfo.cartId,
            title = data.title,
            productId = postAtcInfo.productId,
            warehouseId = addons.warehouseId,
            isFulfillment = addons.isFulfillment,
            selectedAddonsIds = addons.selectedAddonsIds,
            deselectedAddonsIds = addons.deselectedAddonsIds,
            categoryId = postAtcInfo.categoryId,
            shopId = postAtcInfo.shopId,
            quantity = addons.quantity.toLong(),
            price = postAtcInfo.originalPrice,
            discountedPrice = postAtcInfo.price,
            condition = postAtcInfo.condition
        )
    )
}
