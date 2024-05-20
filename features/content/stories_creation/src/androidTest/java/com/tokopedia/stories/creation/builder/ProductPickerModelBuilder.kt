package com.tokopedia.stories.creation.builder

import com.tokopedia.content.product.picker.seller.model.OriginalPrice
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignStatus
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignStatusUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.pinnedproduct.PinProductUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel

/**
 * Created By : Jonathan Darwin on October 20, 2023
 */
class ProductPickerModelBuilder {

    fun buildEtalaseList(
        size: Int = 1
    ): List<EtalaseUiModel> {
        return List(size) {
            EtalaseUiModel(
                id = it.toString(),
                imageUrl = "",
                title = "Etalase $it",
                totalProduct = it
            )
        }
    }

    fun buildCampaignList(
        size: Int = 1
    ): List<CampaignUiModel> {
        return List(size) {
            CampaignUiModel(
                id = it.toString(),
                imageUrl = "",
                title = "Campaign $it",
                totalProduct = it,
                startDateFmt = "",
                endDateFmt = "",
                status = CampaignStatusUiModel(
                    status = CampaignStatus.Ongoing,
                    text = "Berlangsung"
                )
            )
        }
    }

    fun buildProductTagSectionList(
        size: Int = 2
    ): List<ProductTagSectionUiModel> {
        return List(1) {
            ProductTagSectionUiModel(
                "", CampaignStatus.Ongoing,
                List(size) { productCounter ->
                    ProductUiModel(productCounter.toString(), "Product $it", false, "", 0, false, "", 1, OriginalPrice("Rp1000.00", 1000.0), PinProductUiModel.Empty, "", "", "", "", "")
                }
            )
        }
    }

    fun buildEtalaseProducts(
        size: Int = 2,
        hasNextPage: Boolean = false
    ): PagedDataUiModel<ProductUiModel> {
        return PagedDataUiModel(
            dataList = buildProductTagSectionList(size).flatMap { it.products },
            hasNextPage = hasNextPage
        )
    }
}
