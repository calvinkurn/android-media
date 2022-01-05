package com.tokopedia.play.domain.repository

import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VariantUiModel

interface PlayViewerTagItemRepository {

    suspend fun getTagItem(channelId: String): TagItemUiModel

    suspend fun getVariant(product: PlayProductUiModel.Product): VariantUiModel

    suspend fun addProductToCart(
        id: String,
        name: String,
        shopId: String,
        minQty: Int,
        price: Double,
    ): String
}