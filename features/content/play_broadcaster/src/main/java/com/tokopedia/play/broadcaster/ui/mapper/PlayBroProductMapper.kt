package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.play.broadcaster.domain.model.campaign.GetCampaignListResponse
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatusUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductMapper @Inject constructor() {

    private val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm 'WIB'", Locale.getDefault())

    fun mapCampaignList(response: GetCampaignListResponse): List<CampaignUiModel> {
        return response.getSellerCampaignList.campaigns.map {
            CampaignUiModel(
                id = it.campaignId,
                title = it.campaignName,
                imageUrl = it.coverImage,
                startDateFmt = sdf.format((it.startDate * 1000).toDate()),
                endDateFmt = sdf.format((it.endDate * 1000).toDate()),
                status = CampaignStatusUiModel(
                    status = CampaignStatus.getById(it.statusId),
                    text = it.statusText,
                ),
                totalProduct = it.productSummary.totalItem,
            )
        }
    }

    private fun Long.toDate(): Date {
        return Date(this)
    }
}