package com.tokopedia.play.broadcaster.setup.product.model

import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
sealed class ProductSetupAction {

    data class SetSort(val sort: SortUiModel) : ProductSetupAction()
    data class SelectCampaign(val campaign: CampaignUiModel) : ProductSetupAction()
    data class SelectEtalase(val etalase: EtalaseUiModel) : ProductSetupAction()
    data class SelectProduct(val product: ProductUiModel) : ProductSetupAction()
    data class LoadProductList(
        val keyword: String,
    ) : ProductSetupAction()
    data class SearchProduct(val keyword: String) : ProductSetupAction()
    object SaveProducts : ProductSetupAction()
    object RetryFetchProducts : ProductSetupAction()

    data class DeleteSelectedProduct(val product: ProductUiModel) : ProductSetupAction()
}