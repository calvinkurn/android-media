package com.tokopedia.play.broadcaster.setup.product.model

import com.tokopedia.play.broadcaster.setup.product.view.model.ProductListPaging
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkState

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
data class PlayBroProductChooserUiState(
    val campaignAndEtalase: CampaignAndEtalaseUiModel,
    val focusedProductList: ProductListPaging,
    val selectedProductSectionList: List<ProductTagSectionUiModel>,
    val loadParam: ProductListPaging.Param,
    val shopName: String,
    val saveState: ProductSaveStateUiModel,
) {

    companion object {
        val Empty: PlayBroProductChooserUiState
            get() = PlayBroProductChooserUiState(
                campaignAndEtalase = CampaignAndEtalaseUiModel.Empty,
                focusedProductList = ProductListPaging.Empty,
                selectedProductSectionList = emptyList(),
                loadParam = ProductListPaging.Param.Empty,
                shopName = "",
                saveState = ProductSaveStateUiModel.Empty,
            )
    }
}

data class CampaignAndEtalaseUiModel(
    val campaignList: List<CampaignUiModel>,
    val etalaseList: List<EtalaseUiModel>,
    val state: NetworkState,
) {
    companion object {
        val Empty: CampaignAndEtalaseUiModel
            get() = CampaignAndEtalaseUiModel(
                campaignList = emptyList(),
                etalaseList = emptyList(),
                state = NetworkState.Loading,
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