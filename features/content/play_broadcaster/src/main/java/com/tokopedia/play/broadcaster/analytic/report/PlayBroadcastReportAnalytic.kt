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
    }

    fun clickLiveStatsArea() {

    }

    fun impressLiveReportBottomSheet() {

    }

    fun clickEstimatedIncomeCardOnLiveReport() {

    }

    fun clickStatisticIcon() {

    }

    fun impressProductReportBottomSheet() {

    }

    fun clickEstimatedIncomeInfoIcon() {

    }

    fun impressEstimatedIncomeInfoBottomSheet() {

    }

    fun impressProductReportBottomSheetError() {

    }

    fun clickEstimatedIncomeCardOnSummaryPage() {

    }

    companion object {
        private const val ASSISTED_CHANNEL_ID = "get_channel_id"
        private const val ASSISTED_CHANNEL_TITLE = "get_channel_title"
    }
}
