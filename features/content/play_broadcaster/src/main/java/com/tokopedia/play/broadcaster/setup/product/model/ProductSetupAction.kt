package com.tokopedia.play.broadcaster.setup.product.model

import com.tokopedia.content.product.picker.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.model.product.ProductUiModel
import com.tokopedia.content.product.picker.model.sort.SortUiModel

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
sealed interface ProductSetupAction {

    data class SetSort(val sort: SortUiModel) : ProductSetupAction
    data class SelectCampaign(val campaign: CampaignUiModel) : ProductSetupAction
    data class SelectEtalase(val etalase: EtalaseUiModel) : ProductSetupAction
    data class ToggleSelectProduct(val product: ProductUiModel) : ProductSetupAction
    data class SetProducts(val products: List<ProductUiModel>) : ProductSetupAction
    data class LoadProductList(
        val keyword: String,
    ) : ProductSetupAction
    data class SearchProduct(val keyword: String) : ProductSetupAction
    object SaveProducts : ProductSetupAction
    object RetryFetchProducts : ProductSetupAction

    data class DeleteSelectedProduct(val product: ProductUiModel) : ProductSetupAction

    /** Pin Product*/
    data class ClickPinProduct(val product: ProductUiModel) : ProductSetupAction
}
