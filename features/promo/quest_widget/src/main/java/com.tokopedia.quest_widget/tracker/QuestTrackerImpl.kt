package com.tokopedia.quest_widget.tracker

interface QuestTrackerImpl {

    fun clickLihatButton(@QuestSource source: Int)

    fun clickQuestCard(@QuestSource source: Int, id: String)

}