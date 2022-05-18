package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.tokopedianow.common.util.SharedPreferencesUtil
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeQuestSequenceWidgetViewHolder.*
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel

class QuestWidgetCallback(
    private val view: TokoNowView,
    private val viewModel: TokoNowHomeViewModel,
    private val analytics: HomeAnalytics
): HomeQuestSequenceWidgetListener {

    override fun onClickRefreshQuestWidget() {
        viewModel.refreshQuestList()
    }

    override fun onCloseQuestAllClaimedBtnClicked(id: String) {
        setQuestWidgetRemoved()
        viewModel.removeWidget(id)
        analytics.trackClickCloseQuestWidget()
    }

    override fun onQuestWidgetImpression() {
        analytics.trackImpressionQuestWidget()
    }

    override fun onClickSeeDetails() {
        analytics.trackClickSeeDetailsQuestWidget()
    }

    override fun onClickQuestWidgetTitle() {
        analytics.trackClickTitleCardQuestWidget()
    }

    override fun onClickQuestWidgetCard() {
        analytics.trackClickCardQuestWidget()
    }

    override fun onFinishedQuestImpression() {
        analytics.trackImpressionFinishedQuestWidget()
    }

    override fun onClickCheckReward() {
        analytics.trackClickRewardQuestWidget()
    }

    private fun setQuestWidgetRemoved() {
        val activity = view.getFragmentPage().activity
        SharedPreferencesUtil.setQuestAllClaimedRemoved(activity)
    }
}