package com.tokopedia.play.broadcaster.setup.product.view.model

import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel

/**
 * Created by kenny.hadisaputra on 27/01/22
 */
sealed class EtalaseListModel {

    data class Header(val text: String) : EtalaseListModel()
    data class Body(val campaignUiModel: CampaignUiModel) : EtalaseListModel()
}