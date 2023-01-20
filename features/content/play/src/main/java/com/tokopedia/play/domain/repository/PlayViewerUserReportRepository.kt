package com.tokopedia.play.domain.repository

import com.tokopedia.play.domain.PostUserReportUseCase
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel

/**
 * @author by astidhiyaa on 13/12/21
 */
interface PlayViewerUserReportRepository {

    suspend fun getReasoningList() : List<PlayUserReportReasoningUiModel>

    suspend fun submitReport(
        params: PostUserReportUseCase.ChannelReportParams
    ): Boolean
}
