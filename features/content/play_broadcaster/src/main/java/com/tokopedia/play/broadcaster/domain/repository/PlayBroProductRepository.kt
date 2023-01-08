package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
interface PlayBroProductRepository {

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

    suspend fun getProductTagSummarySection(channelID: String): List<ProductTagSectionUiModel>

    suspend fun setPinProduct(channelId: String, product: ProductUiModel): Boolean
}
