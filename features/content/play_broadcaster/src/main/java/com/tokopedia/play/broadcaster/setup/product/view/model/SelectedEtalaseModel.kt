package com.tokopedia.play.broadcaster.setup.product.view.model

import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
sealed class SelectedEtalaseModel {

    data class Campaign(val campaign: CampaignUiModel) : SelectedEtalaseModel()
    data class Etalase(val etalase: EtalaseUiModel) : SelectedEtalaseModel()
    object None : SelectedEtalaseModel()
}