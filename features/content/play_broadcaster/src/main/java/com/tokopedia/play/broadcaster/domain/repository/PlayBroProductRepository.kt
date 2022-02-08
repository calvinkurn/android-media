package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
interface PlayBroProductRepository {

    suspend fun getCampaignList(): List<CampaignUiModel>

    suspend fun getEtalaseList(): List<EtalaseUiModel>

    suspend fun getProductsInEtalase(
        etalaseId: String,
        page: Int,
        keyword: String,
    ): List<ProductUiModel>

    suspend fun addProductTag(channelId: String, productIds: List<String>)

    suspend fun getProductTagSummarySection(
        channelID: Long,
    ): List<ProductTagSectionUiModel>
}