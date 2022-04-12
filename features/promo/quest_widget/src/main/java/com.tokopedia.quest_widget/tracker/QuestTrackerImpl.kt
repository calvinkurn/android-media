package com.tokopedia.quest_widget.tracker

interface QuestTrackerImpl {

    fun viewQuestWidget(@QuestSource source: Int, id: String)
    fun clickLihatButton(@QuestSource source: Int)
    fun clickQuestCard(@QuestSource source: Int, id: String)
    fun slideQuestCard(@QuestSource source: Int, direction: String)

}