package com.tokopedia.play.domain.repository

import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel

/**
 * @author by astidhiyaa on 13/12/21
 */
interface PlayViewerUserReportRepository {

    suspend fun getReasoningList() : List<PlayUserReportReasoningUiModel>

    suspend fun submitReport(
        channelId: Long,
        mediaUrl: String,
        partnerId: Long,
        reasonId: Int,
        timestamp: Long,
        reportDesc: String,
        partnerType: PartnerType,
        userId: Long,
    ): Boolean
}
