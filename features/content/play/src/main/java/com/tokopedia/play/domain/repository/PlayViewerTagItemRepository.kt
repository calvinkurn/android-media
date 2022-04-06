package com.tokopedia.play.domain.repository

import com.tokopedia.play.view.type.PlayUpcomingBellStatus
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VariantUiModel
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute

interface PlayViewerTagItemRepository {

    suspend fun getTagItem(channelId: String): TagItemUiModel

    suspend fun getVariant(product: PlayProductUiModel.Product): VariantUiModel

    suspend fun selectVariantOption(
        variant: VariantUiModel,
        selectedOption: VariantOptionWithAttribute,
    ): VariantUiModel

    suspend fun addProductToCart(
        id: String,
        name: String,
        shopId: String,
        minQty: Int,
        price: Double,
    ): String

    suspend fun checkUpcomingCampaign(campaignId: Long): Boolean

    suspend fun subscribeUpcomingCampaign(campaignId: Long, reminderType: PlayUpcomingBellStatus): Pair<Boolean, String>
}