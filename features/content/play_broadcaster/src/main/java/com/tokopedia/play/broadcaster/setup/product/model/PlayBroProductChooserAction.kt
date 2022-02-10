package com.tokopedia.play.broadcaster.setup.product.model

import com.tokopedia.play.broadcaster.setup.product.view.model.ProductListPaging
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
sealed class PlayBroProductChooserAction {

    data class SetSort(val sort: SortUiModel) : PlayBroProductChooserAction()
    data class SelectCampaign(val campaign: CampaignUiModel) : PlayBroProductChooserAction()
    data class SelectEtalase(val etalase: EtalaseUiModel) : PlayBroProductChooserAction()
    data class SelectProduct(val product: ProductUiModel) : PlayBroProductChooserAction()
    data class LoadProductList(
        val keyword: String,
    ) : PlayBroProductChooserAction()
    data class SearchProduct(val keyword: String) : PlayBroProductChooserAction()
    object SaveProducts : PlayBroProductChooserAction()
    object AddProduct : PlayBroProductChooserAction()
}