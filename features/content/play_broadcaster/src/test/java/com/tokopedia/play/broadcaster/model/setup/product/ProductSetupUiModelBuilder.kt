package com.tokopedia.play.broadcaster.model.setup.product

import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.type.ProductPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatusUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.pinnedproduct.PinProductUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel

/**
 * Created By : Jonathan Darwin on February 18, 2022
 */
class ProductSetupUiModelBuilder {

    fun buildCampaignModel(
        id: String = "1",
        name: String = "Campaign 1",
        imageUrl: String = "",
        startDateFmt: String = "",
        endDateFmt: String = "",
        campaignStatusModel: CampaignStatusUiModel = buildCampaignStatus(),
        totalProduct: Int = 1,
    ) = CampaignUiModel(id, name, imageUrl, startDateFmt, endDateFmt, campaignStatusModel, totalProduct)

    fun buildEtalaseModel(
        id: String = "1",
        imageUrl: String = "",
        title: String = "",
        totalProduct: Int = 1,
    ) = EtalaseUiModel(id, imageUrl, title, totalProduct)

    fun buildCampaignList(size: Int = 5) = List(size) {
        buildCampaignModel()
    }

    fun buildEtalaseList(size: Int = 5) = List(size) {
        buildEtalaseModel()
    }

    fun buildProductUiModel(
        id: String = "1",
        name: String = "Product 1",
        imageUrl: String = "",
        stock: Long = 10,
        price: ProductPrice = OriginalPrice("Rp 12.000", 12000.0),
    ) = ProductUiModel(id, name, false, "", 0, false, imageUrl, stock, price, PinProductUiModel.Empty, "")

    fun buildProductTagSectionList(
        sectionSize: Int = 5,
        productSizePerSection: Int = 3,
    ) = List(sectionSize) { sectionIdx ->
        ProductTagSectionUiModel("Test $sectionIdx", CampaignStatus.Ongoing, List(productSizePerSection) { productIdx ->
            val idx = productSizePerSection * sectionIdx + productIdx
            buildProductUiModel(id = idx.toString())
        })
    }

    fun buildCampaignStatus(
        campaignStatus: CampaignStatus = CampaignStatus.Ongoing,
        text: String = "Berlangsung"
    ) = CampaignStatusUiModel(campaignStatus, text)
}
