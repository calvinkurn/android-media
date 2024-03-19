package com.tokopedia.play.broadcaster.analytic.report

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by Jonathan Darwin on 15 March 2024
 */

/**
 * Mynakama (Row 94 - 103)
 * MA : https://mynakama.tokopedia.com/datatracker/requestdetail/view/2649
 * SA : https://mynakama.tokopedia.com/datatracker/requestdetail/view/2921
 */
class PlayBroadcastReportAnalytic @AssistedInject constructor(
    @Assisted private val getAccount: () -> ContentAccountUiModel,
    @Assisted(ASSISTED_CHANNEL_ID) private val getChannelId: () -> String,
    @Assisted(ASSISTED_CHANNEL_TITLE) private val getChannelTitle: () -> String,
    analyticManagerFactory: ContentAnalyticManager.Factory
) {

    @AssistedFactory
    interface Factory {
        fun create(
            getAccount: () -> ContentAccountUiModel,
            @Assisted(ASSISTED_CHANNEL_ID) getChannelId: () -> String,
            @Assisted(ASSISTED_CHANNEL_TITLE) getChannelTitle: () -> String,
        ): PlayBroadcastReportAnalytic
    }

    private val analyticManager = analyticManagerFactory.create(
        businessUnit = BusinessUnit.content,
        eventCategory = EventCategory.playBroadcast,
    )

    fun impressStatisticIconCoachMark() {
        analyticManager.sendViewContent(
            eventAction = "view - estimasi pendapatan coachmark",
            eventLabel = getReportEventLabel(),
            mainAppTrackerId = "50351",
            sellerAppTrackerId = "50374",
        )
    }

    fun clickLiveStatsArea() {
        analyticManager.sendClickContent(
            eventAction = "click - top area",
            eventLabel = getReportEventLabel(),
            mainAppTrackerId = "50352",
            sellerAppTrackerId = "50375",
        )
    }

    fun impressLiveReportBottomSheet() {
        analyticManager.sendViewContent(
            eventAction = "view - information bottomsheet",
            eventLabel = getReportEventLabel(),
            mainAppTrackerId = "50353",
            sellerAppTrackerId = "50376",
        )
    }

    fun clickEstimatedIncomeCardOnLiveReport() {
        analyticManager.sendClickContent(
            eventAction = "click - estimasi pendapatan card bottomsheet",
            eventLabel = getReportEventLabel(),
            mainAppTrackerId = "50354",
            sellerAppTrackerId = "50377",
        )
    }

    fun clickStatisticIcon() {
        analyticManager.sendClickContent(
            eventAction = "click - estimasi pendapatan icon",
            eventLabel = getReportEventLabel(),
            mainAppTrackerId = "50355",
            sellerAppTrackerId = "50378",
        )
    }

    fun impressProductReportBottomSheet() {
        analyticManager.sendViewContent(
            eventAction = "view - estimasi pendapatan bottomsheet",
            eventLabel = getReportEventLabel(),
            mainAppTrackerId = "50356",
            sellerAppTrackerId = "50379",
        )
    }

    fun clickEstimatedIncomeInfoIcon() {
        analyticManager.sendClickContent(
            eventAction = "click - estimasi pendapatan info icon bottomsheet",
            eventLabel = getReportEventLabel(),
            mainAppTrackerId = "50357",
            sellerAppTrackerId = "50380",
        )
    }

    fun impressEstimatedIncomeInfoBottomSheet() {
        analyticManager.sendViewContent(
            eventAction = "view - estimasi pendapatan explanation bottomsheet",
            eventLabel = getReportEventLabel(),
            mainAppTrackerId = "50358",
            sellerAppTrackerId = "50381",
        )
    }

    fun impressProductReportBottomSheetError() {
        analyticManager.sendViewContent(
            eventAction = "view - estimasi pendapatan error bottomsheet",
            eventLabel = getReportEventLabel(),
            mainAppTrackerId = "50359",
            sellerAppTrackerId = "50382",
        )
    }

    fun clickEstimatedIncomeCardOnSummaryPage() {
        analyticManager.sendClickContent(
            eventAction = "click - estimasi pendapatan icon card",
            eventLabel = getReportEventLabel(),
            mainAppTrackerId = "50360",
            sellerAppTrackerId = "50383",
        )
    }

    private fun getReportEventLabel(): String {
        val account = getAccount().toAnalyticModel()

        return analyticManager.concatLabels(
            account.id,
            getChannelId(),
            getChannelTitle(),
            account.type,
        )
    }

    companion object {
        private const val ASSISTED_CHANNEL_ID = "get_channel_id"
        private const val ASSISTED_CHANNEL_TITLE = "get_channel_title"
    }
}
