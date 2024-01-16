package com.tokopedia.play.broadcaster.shorts.analytic.interspersing

import com.tokopedia.content.analytic.Value.off
import com.tokopedia.content.analytic.Value.on
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.shorts.analytic.helper.PlayShortsAnalyticHelper
import com.tokopedia.play.broadcaster.shorts.analytic.sender.PlayShortsAnalyticSender
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on January 15, 2024
 */

/**
 * MyNakama
 * MA (Row 83 - 88) : https://mynakama.tokopedia.com/datatracker/requestdetail/view/3511
 * SA (Row 63 - 68) : https://mynakama.tokopedia.com/datatracker/requestdetail/view/3512
 */
class PlayShortsInterspersingAnalyticImpl @Inject constructor(
    private val analyticSender: PlayShortsAnalyticSender,
) : PlayShortsInterspersingAnalytic {

    override fun clickInterspersingToggle(
        accountUiModel: ContentAccountUiModel,
        creationId: String,
        isActive: Boolean
    ) {
        analyticSender.sendGeneralClickEventContent(
            eventAction = "click - show video on pdp",
            eventLabel = getEventLabel(
                accountUiModel,
                creationId,
                listOf(getToggleInterspersingLabel(isActive))
            ),
            trackerId = PlayShortsAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "49475",
                sellerAppTrackerId = "49464",
            )
        )
    }

    override fun impressInterspersingError(
        accountUiModel: ContentAccountUiModel,
        creationId: String
    ) {
        analyticSender.sendGeneralViewEventContent(
            eventAction = "view - error toaster show video PDP",
            eventLabel = getEventLabel(
                accountUiModel,
                creationId,
            ),
            trackerId = PlayShortsAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "49476",
                sellerAppTrackerId = "49465",
            )
        )
    }

    override fun clickCloseInterspersingConfirmation(
        accountUiModel: ContentAccountUiModel,
        creationId: String
    ) {
        analyticSender.sendGeneralClickEventContent(
            eventAction = "click - x icon video product option for pdp",
            eventLabel = getEventLabel(
                accountUiModel,
                creationId,
            ),
            trackerId = PlayShortsAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "49477",
                sellerAppTrackerId = "49466",
            )
        )
    }

    override fun clickVideoPdpCard(accountUiModel: ContentAccountUiModel, creationId: String) {
        analyticSender.sendGeneralClickEventContent(
            eventAction = "click - video card option for pdp",
            eventLabel = getEventLabel(
                accountUiModel,
                creationId,
            ),
            trackerId = PlayShortsAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "49478",
                sellerAppTrackerId = "49467",
            )
        )
    }

    override fun clickNextInterspersingConfirmation(
        accountUiModel: ContentAccountUiModel,
        creationId: String
    ) {
        analyticSender.sendGeneralClickEventContent(
            eventAction = "click - lanjut show video on pdp",
            eventLabel = getEventLabel(
                accountUiModel,
                creationId,
            ),
            trackerId = PlayShortsAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "49479",
                sellerAppTrackerId = "49468",
            )
        )
    }

    override fun clickBackInterspersingConfirmation(
        accountUiModel: ContentAccountUiModel,
        creationId: String
    ) {
        analyticSender.sendGeneralClickEventContent(
            eventAction = "click - kembali show video on pdp",
            eventLabel = getEventLabel(
                accountUiModel,
                creationId,
            ),
            trackerId = PlayShortsAnalyticHelper.getTrackerIdBySite(
                mainAppTrackerId = "49480",
                sellerAppTrackerId = "49474",
            )
        )
    }

    private fun getToggleInterspersingLabel(isActive: Boolean): String {
        return if (isActive) on else off
    }

    private fun getEventLabel(
        accountUiModel: ContentAccountUiModel,
        creationId: String,
        additionalData: List<String> = emptyList()
    ): String {
        return buildString {
            append(PlayShortsAnalyticHelper.getEventLabelByAccount(accountUiModel))
            append(" - ")
            append(creationId)
            additionalData.forEachIndexed { idx, data ->
                if (idx == additionalData.size-1) {
                    append(" - ")
                }

                append(data)
            }
        }
    }
}
