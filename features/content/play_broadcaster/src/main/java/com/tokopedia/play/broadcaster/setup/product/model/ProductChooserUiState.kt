package com.tokopedia.play.broadcaster.setup.product.model

import com.tokopedia.play.broadcaster.setup.product.view.model.ProductListPaging
import com.tokopedia.content.product.picker.sgc.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.sgc.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.sgc.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.sgc.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkState

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
data class ProductChooserUiState(
    val campaignAndEtalase: CampaignAndEtalaseUiModel,
    val focusedProductList: ProductListPaging,
    val selectedProductList: List<ProductUiModel>,
    val loadParam: ProductListPaging.Param,
    val saveState: ProductSaveStateUiModel,
    val config: ProductSetupConfig,
) {

    companion object {
        val Empty: ProductChooserUiState
            get() = ProductChooserUiState(
                campaignAndEtalase = CampaignAndEtalaseUiModel.Empty,
                focusedProductList = ProductListPaging.Empty,
                selectedProductList = emptyList(),
                loadParam = ProductListPaging.Param.Empty,
                saveState = ProductSaveStateUiModel.Empty,
                config = ProductSetupConfig.Empty,
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

data class ProductSetupConfig(
    val shopName: String,
    val maxProduct: Int,
) {
    companion object {
        val Empty: ProductSetupConfig
            get() = ProductSetupConfig(
                shopName = "",
                maxProduct = 0,
            )
    }
}
