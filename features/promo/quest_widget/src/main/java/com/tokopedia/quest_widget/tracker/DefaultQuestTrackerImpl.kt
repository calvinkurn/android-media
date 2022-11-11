package com.tokopedia.quest_widget.tracker

open class DefaultQuestTrackerImpl : QuestTrackerImpl {

    override fun clickLihatButton(@QuestSource source: Int) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = Tracker.Event.EVENT_CLICK
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.CATEGORY_QUEST_WIDGET
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_LIHAT_SEMUA
        when(source){
            QuestSource.HOME ->{
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.HOME_PAGE}"
            }
            QuestSource.REWARDS ->{
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.REWARDS_PAGE}"
            }
            QuestSource.DISCO ->{
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.DISCO_PAGE}"
            }
        }
        Tracker.fillCommonItems(map, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun viewQuestWidget(@QuestSource source: Int, id: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = Tracker.Event.EVENT_VIEW
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.CATEGORY_QUEST_WIDGET
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.VIEW_QUEST_WIDGET
        when(source){
            QuestSource.HOME ->{
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.HOME_PAGE}_$id"
            }
            QuestSource.REWARDS ->{
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.REWARDS_PAGE}_$id"
            }
            QuestSource.DISCO ->{
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.DISCO_PAGE}_$id"
            }
        }

        Tracker.fillCommonItems(map, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun clickQuestCard(@QuestSource source: Int, id: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = Tracker.Event.EVENT_CLICK
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.CATEGORY_QUEST_WIDGET
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.CLICK_QUEST_WIDGET
        when(source){
            QuestSource.HOME ->{
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.HOME_PAGE}_$id"
            }
            QuestSource.REWARDS ->{
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.REWARDS_PAGE}_$id"
            }
            QuestSource.DISCO ->{
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.DISCO_PAGE}_$id"
            }
        }

        Tracker.fillCommonItems(map, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)
    }

    override fun slideQuestCard(source: Int, direction: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = Tracker.Event.EVENT_CLICK
        map[Tracker.Constants.EVENT_CATEGORY] = Tracker.Category.CATEGORY_QUEST_WIDGET
        map[Tracker.Constants.EVENT_ACTION] = Tracker.Action.SLIDE_QUEST_WIDGET
        when(source){
            QuestSource.HOME ->{
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.HOME_PAGE}_$direction"
            }
            QuestSource.REWARDS ->{
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.REWARDS_PAGE}_$direction"
            }
            QuestSource.DISCO ->{
                map[Tracker.Constants.EVENT_LABEL] = "${Tracker.Label.DISCO_PAGE}_$direction"
            }
        }

        Tracker.fillCommonItems(map, Tracker.Constants.PHYSICALGOODS_BUSINESSUNIT)
        Tracker.getTracker().sendGeneralEvent(map)

    }
}