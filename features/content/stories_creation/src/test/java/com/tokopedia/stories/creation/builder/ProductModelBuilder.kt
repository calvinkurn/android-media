package com.tokopedia.stories.creation.builder

import com.tokopedia.content.product.picker.seller.model.OriginalPrice
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignStatus
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.model.pinnedproduct.PinProductUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel

/**
 * Created By : Jonathan Darwin on October 18, 2023
 */
class ProductModelBuilder {

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
}
