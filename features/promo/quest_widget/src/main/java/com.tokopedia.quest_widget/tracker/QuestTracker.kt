package com.tokopedia.quest_widget.tracker

class QuestTracker {

    private var trackerImpl: QuestTrackerImpl = DefaultQuestTrackerImpl()

    fun clickLihatButton(@QuestSource source: Int){
        trackerImpl.clickLihatButton(source)
    }

    fun clickQuestCard(@QuestSource source: Int, id: String){
        trackerImpl.clickQuestCard(source, id)
    }

}