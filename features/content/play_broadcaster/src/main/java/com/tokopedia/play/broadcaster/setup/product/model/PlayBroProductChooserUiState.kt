package com.tokopedia.play.broadcaster.setup.product.model

import com.tokopedia.play.broadcaster.setup.product.view.model.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortFilterUiModel

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
data class PlayBroProductChooserUiState(
    val campaignAndEtalase: CampaignAndEtalaseUiModel,
    val focusedProductList: List<ProductUiModel>,
    val selectedProductList: List<ProductUiModel>,
    val sortFilter: SortFilterUiModel,
) {

    companion object {
        val Empty: PlayBroProductChooserUiState
            get() = PlayBroProductChooserUiState(
                campaignAndEtalase = CampaignAndEtalaseUiModel.Empty,
                focusedProductList = emptyList(),
                selectedProductList = emptyList(),
                sortFilter = SortFilterUiModel.Empty,
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