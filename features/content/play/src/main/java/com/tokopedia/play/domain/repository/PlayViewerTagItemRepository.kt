package com.tokopedia.play.domain.repository

import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VariantUiModel
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute

interface PlayViewerTagItemRepository {

    suspend fun getTagItem(channelId: String, warehouseId: String, partnerName: String): TagItemUiModel

    suspend fun updateCampaignReminderStatus(
        productSections: List<ProductSectionUiModel.Section>,
    ): List<ProductSectionUiModel.Section>

    suspend fun getVariant(
        product: PlayProductUiModel.Product,
        isProductFeatured: Boolean,
    ): VariantUiModel

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

    suspend fun checkUpcomingCampaign(campaignId: String): Boolean

    suspend fun subscribeUpcomingCampaign(
        campaignId: String,
        shouldRemind: Boolean,
    ): CampaignReminder

    data class CampaignReminder(
        val isReminded: Boolean,
        val message: String,
    )

    suspend fun addProductToCartOcc(
        id: String,
        name: String,
        shopId: String,
        minQty: Int,
        price: Double,
    ): String
}
