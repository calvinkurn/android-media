package com.tokopedia.product.detail.postatc.util

import com.tokopedia.product.detail.postatc.base.PostAtcUiModel
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel.Type.PRODUCT_INFO
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel.Type.RECOMMENDATION
import com.tokopedia.product.detail.postatc.component.productinfo.ProductInfoUiModel
import com.tokopedia.product.detail.postatc.component.recommendation.RecommendationUiModel
import com.tokopedia.product.detail.postatc.model.PostAtcComponentData
import com.tokopedia.product.detail.postatc.model.PostAtcLayout

internal fun List<PostAtcLayout.Component>.mapToUiModel(): List<PostAtcUiModel> = mapNotNull {
    when (it.type) {
        PRODUCT_INFO -> toProductInfoUiModel(it.name, it.data)
        RECOMMENDATION -> toRecommendationUiModel(it.name)
        else -> null
    }
}

private fun toProductInfoUiModel(
    name: String,
    datas: List<PostAtcComponentData>
): ProductInfoUiModel? {
    val data = datas.firstOrNull() ?: return null
    return ProductInfoUiModel(
        title = data.title,
        subtitle = data.subtitle,
        imageLink = data.image,
        buttonText = data.button.text,
        cartId = data.button.cartId,
        name = name,
    )
}

private fun toRecommendationUiModel(name: String): RecommendationUiModel {
    return RecommendationUiModel(
        name = name
    )
}
