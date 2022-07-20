package com.tokopedia.quest_widget.listeners

import com.tokopedia.quest_widget.util.LiveDataResult

interface QuestWidgetStatusCallback {
    fun getQuestWidgetStatus(status: LiveDataResult.STATUS)
}