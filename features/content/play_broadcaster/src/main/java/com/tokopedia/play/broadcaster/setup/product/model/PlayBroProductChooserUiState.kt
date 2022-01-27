package com.tokopedia.play.broadcaster.setup.product.model

import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
data class PlayBroProductChooserUiState(
    val campaignList: List<CampaignUiModel>,
) {

    companion object {
        val Empty: PlayBroProductChooserUiState
            get() = PlayBroProductChooserUiState(
                campaignList = emptyList(),
            )
    }
}