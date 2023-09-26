package com.tokopedia.content.product.picker.domain

import com.tokopedia.content.product.picker.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.model.product.ProductUiModel
import com.tokopedia.content.product.picker.model.sort.SortUiModel

/**
 * Created By : Jonathan Darwin on September 26, 2023
 */
interface ContentProductPickerSGCRepository {

    suspend fun getCampaignList(): List<CampaignUiModel>

    suspend fun getEtalaseList(): List<EtalaseUiModel>

    suspend fun getProductsInEtalase(
        etalaseId: String,
        cursor: String,
        keyword: String,
        sort: SortUiModel,
    ): PagedDataUiModel<ProductUiModel>

    suspend fun getProductsInCampaign(
        campaignId: String,
        page: Int,
    ): PagedDataUiModel<ProductUiModel>

    suspend fun setProductTags(channelId: String, productIds: List<String>)

    suspend fun getProductTagSummarySection(
        channelID: String,
        fetchCommission: Boolean = false,
    ): List<ProductTagSectionUiModel>

    suspend fun setPinProduct(channelId: String, product: ProductUiModel): Boolean

}
