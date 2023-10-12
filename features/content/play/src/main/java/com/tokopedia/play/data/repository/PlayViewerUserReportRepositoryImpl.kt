package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.content.common.usecase.GetUserReportListUseCase
import com.tokopedia.content.common.usecase.PostUserReportUseCase
import com.tokopedia.play.domain.repository.PlayViewerUserReportRepository
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 13/12/21
 */
class PlayViewerUserReportRepositoryImpl @Inject constructor(
    private val getUserReportListUseCase: GetUserReportListUseCase,
    private val postUserReportUseCase: PostUserReportUseCase,
    private val playUiModelMapper: PlayUiModelMapper,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : PlayViewerUserReportRepository {

    override suspend fun getReasoningList(): List<PlayUserReportReasoningUiModel> =
        withContext(dispatchers.io) {
            val response = getUserReportListUseCase.executeOnBackground()
            return@withContext playUiModelMapper.mapUserReport(response.data)
        }

    override suspend fun submitReport(
        channelId: Long,
        mediaUrl: String,
        partnerId: Long,
        reasonId: Int,
        timestamp: Long,
        reportDesc: String,
        partnerType: PartnerType,
    ): Boolean = withContext(dispatchers.io)
    {
        val request = postUserReportUseCase
            .createParam(
                reporterId = userSession.userId.toLongOrZero(),
                channelId = channelId,
                mediaUrl = mediaUrl,
                partnerId = partnerId,
                reasonId = reasonId,
                timestamp = timestamp,
                reportDesc = reportDesc,
                partnerType = PostUserReportUseCase.PartnerType.getTypeFromPlay(partnerType.value),
            )
        postUserReportUseCase.setRequestParams(request.parameters)
        val response = postUserReportUseCase.executeOnBackground()
        return@withContext playUiModelMapper.mapUserReportSubmission(response.submissionReport)
    }
}
