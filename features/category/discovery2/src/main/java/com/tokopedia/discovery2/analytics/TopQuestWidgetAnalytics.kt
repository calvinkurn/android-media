package com.tokopedia.discovery2.analytics

import com.tokopedia.quest_widget.tracker.DefaultQuestTrackerImpl

class TopQuestWidgetAnalytics(val analytics: BaseDiscoveryAnalytics): DefaultQuestTrackerImpl() {
    override fun clickLihatButton(source: Int) {
        super.clickLihatButton(source)
        analytics.clickQuestLihatButton(source)
    }

    override fun viewQuestWidget(source: Int, id: String) {
        super.viewQuestWidget(source, id)
        analytics.viewQuestWidget(source, id)
    }

    override fun clickQuestCard(source: Int, id: String) {
        super.clickQuestCard(source, id)
        analytics.clickQuestCard(source, id)
    }

    override fun slideQuestCard(source: Int, direction: String) {
        super.slideQuestCard(source, direction)
        analytics.slideQuestCard(source, direction)
    }
}