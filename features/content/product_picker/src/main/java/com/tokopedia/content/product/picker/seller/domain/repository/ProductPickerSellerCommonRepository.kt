package com.tokopedia.content.product.picker.seller.domain.repository

import com.tokopedia.content.product.picker.seller.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.seller.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel

/**
 * Created By : Jonathan Darwin on November 14, 2023
 */
interface ProductPickerSellerCommonRepository {

    suspend fun getCampaignList(): List<CampaignUiModel>

    suspend fun getEtalaseList(): List<EtalaseUiModel>

    suspend fun getProductsInCampaign(
        campaignId: String,
        page: Int,
    ): PagedDataUiModel<ProductUiModel>
}
