package com.tokopedia.content.product.picker.model.etalase

import com.tokopedia.content.product.picker.model.campaign.CampaignUiModel

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
sealed class SelectedEtalaseModel {

    data class Campaign(val campaign: CampaignUiModel) : SelectedEtalaseModel()
    data class Etalase(val etalase: EtalaseUiModel) : SelectedEtalaseModel()
    object None : SelectedEtalaseModel()
}
