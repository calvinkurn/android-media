package com.tokopedia.quest_widget.listeners

import com.tokopedia.quest_widget.util.LiveDataResult

interface QuestWidgetStatus {
    fun questWidgetStatus(status: LiveDataResult.STATUS)
}