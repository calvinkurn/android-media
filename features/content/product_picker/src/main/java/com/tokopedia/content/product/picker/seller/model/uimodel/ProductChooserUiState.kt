package com.tokopedia.content.product.picker.seller.model.uimodel

import com.tokopedia.content.product.picker.seller.model.ProductListPaging
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.seller.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.model.result.ContentProductPickerNetworkResult

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
    val state: ContentProductPickerNetworkResult,
) {
    companion object {
        val Empty: CampaignAndEtalaseUiModel
            get() = CampaignAndEtalaseUiModel(
                campaignList = emptyList(),
                etalaseList = emptyList(),
                state = ContentProductPickerNetworkResult.Loading,
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
