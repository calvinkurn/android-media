package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
interface PlayBroProductRepository {

    suspend fun getCampaignList(): List<CampaignUiModel>
}