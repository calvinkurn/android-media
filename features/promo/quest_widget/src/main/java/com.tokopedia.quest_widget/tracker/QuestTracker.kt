package com.tokopedia.quest_widget.tracker

class QuestTracker {

    var trackerImpl: QuestTrackerImpl = DefaultQuestTrackerImpl()

    fun viewQuestWidget(@QuestSource source: Int, id: String){
        trackerImpl.viewQuestWidget(source, id)
    }

    fun clickLihatButton(@QuestSource source: Int){
        trackerImpl.clickLihatButton(source)
    }

    fun clickQuestCard(@QuestSource source: Int, id: String){
        trackerImpl.clickQuestCard(source, id)
    }

    fun slideQuestCard(@QuestSource source: Int, direction: String){
        trackerImpl.slideQuestCard(source, direction)
    }

}