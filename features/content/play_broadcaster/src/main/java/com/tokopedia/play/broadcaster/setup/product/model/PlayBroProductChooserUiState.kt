package com.tokopedia.play.broadcaster.setup.product.model

import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseProductListMap
import com.tokopedia.play.broadcaster.setup.product.view.model.ProductListPaging
import com.tokopedia.play.broadcaster.ui.model.etalase.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
data class PlayBroProductChooserUiState(
    val campaignAndEtalase: CampaignAndEtalaseUiModel,
    val focusedProductList: ProductListPaging,
    val selectedProductList: EtalaseProductListMap,
    val loadParam: ProductListPaging.Param,
    val shopName: String,
    val saveState: ProductSaveStateUiModel,
) {

    companion object {
        val Empty: PlayBroProductChooserUiState
            get() = PlayBroProductChooserUiState(
                campaignAndEtalase = CampaignAndEtalaseUiModel.Empty,
                focusedProductList = ProductListPaging.Empty,
                selectedProductList = emptyMap(),
                loadParam = ProductListPaging.Param.Empty,
                shopName = "",
                saveState = ProductSaveStateUiModel.Empty,
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

data class ProductSaveStateUiModel(
    val isLoading: Boolean,
    val canSave: Boolean,
) {

    companion object {
        val Empty: ProductSaveStateUiModel
            get() = ProductSaveStateUiModel(
                isLoading = false,
                canSave = false,
            )
    }
}