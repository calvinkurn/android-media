package com.tokopedia.play.broadcaster.setup.product.model

import com.tokopedia.play.broadcaster.setup.product.view.model.EtalaseProductListMap
import com.tokopedia.play.broadcaster.setup.product.view.model.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
data class PlayBroProductChooserUiState(
    val campaignAndEtalase: CampaignAndEtalaseUiModel,
    val focusedProductList: List<ProductUiModel>,
    val selectedProductList: EtalaseProductListMap,
    val sort: SortUiModel?,
    val productTagSummary: ProductTagSummaryUiModel,
) {

    companion object {
        val Empty: PlayBroProductChooserUiState
            get() = PlayBroProductChooserUiState(
                campaignAndEtalase = CampaignAndEtalaseUiModel.Empty,
                focusedProductList = emptyList(),
                selectedProductList = emptyMap(),
                sort = null,
                productTagSummary = ProductTagSummaryUiModel.Unknown,
            )
    }
}

data class CampaignAndEtalaseUiModel(
    val selected: SelectedEtalaseModel,
    val campaignList: List<CampaignUiModel>,
    val etalaseList: List<EtalaseUiModel>,
) {
    companion object {
        val Empty: CampaignAndEtalaseUiModel
            get() = CampaignAndEtalaseUiModel(
                selected = SelectedEtalaseModel.None,
                campaignList = emptyList(),
                etalaseList = emptyList(),
            )
    }
}

sealed class ProductTagSummaryUiModel {
    object Unknown: ProductTagSummaryUiModel()
    object Loading: ProductTagSummaryUiModel()
    data class Success(val sections: List<ProductTagSectionUiModel>, val productCount: Int): ProductTagSummaryUiModel()
    object Empty: ProductTagSummaryUiModel()
    data class Error(val throwable: Throwable): ProductTagSummaryUiModel()
}