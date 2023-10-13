package com.tokopedia.stories.creation.data

import com.tokopedia.content.product.picker.seller.domain.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.model.sort.SortUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
class StoriesCreationProductRepositoryImpl @Inject constructor(

) : ContentProductPickerSellerRepository {

    override suspend fun getCampaignList(): List<CampaignUiModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getEtalaseList(): List<EtalaseUiModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsInEtalase(
        etalaseId: String,
        cursor: String,
        keyword: String,
        sort: SortUiModel
    ): PagedDataUiModel<ProductUiModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsInCampaign(
        campaignId: String,
        page: Int
    ): PagedDataUiModel<ProductUiModel> {
        TODO("Not yet implemented")
    }

    override suspend fun setProductTags(channelId: String, productIds: List<String>) {
        TODO("Not yet implemented")
    }

    override suspend fun getProductTagSummarySection(
        channelID: String,
        fetchCommission: Boolean
    ): List<ProductTagSectionUiModel> {
        TODO("Not yet implemented")
    }

    override suspend fun setPinProduct(channelId: String, product: ProductUiModel): Boolean {
        TODO("Not yet implemented")
    }
}
