package com.tokopedia.quest_widget.tracker

import androidx.annotation.IntDef
import com.tokopedia.quest_widget.tracker.QuestSource.Companion.DEFAULT
import com.tokopedia.quest_widget.tracker.QuestSource.Companion.DISCO
import com.tokopedia.quest_widget.tracker.QuestSource.Companion.HOME
import com.tokopedia.quest_widget.tracker.QuestSource.Companion.REWARDS
import com.tokopedia.quest_widget.tracker.QuestSource.Companion.SHOP
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

object Tracker {

    object Constants {
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val PHYSICALGOODS_BUSINESSUNIT = "bgp - engagement"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace "
    }

    object Event {
        const val EVENT_VIEW = "viewQuestIris"
        const val EVENT_CLICK = "clickQuest"
    }

    object Category {
        const val CATEGORY_QUEST_WIDGET = "quest widget"
    }

    object Action {
        const val VIEW_QUEST_WIDGET = "view quest widget"
        const val CLICK_LIHAT_SEMUA = "click lihat semua"
        const val CLICK_QUEST_WIDGET = "click quest widget"
        const val SLIDE_QUEST_WIDGET = "slide widget"
    }

    object Label {
        const val HOME_PAGE = "homepage"
        const val REWARDS_PAGE = "myreward"
        const val DISCO_PAGE = "disco"
    }

    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun fillCommonItems(map: MutableMap<String, Any>, businessUnit: String) {
        map[Constants.BUSINESS_UNIT] = businessUnit
        map[Constants.CURRENT_SITE] = Constants.TOKOPEDIA_MARKETPLACE
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(DEFAULT, HOME, REWARDS , SHOP , DISCO)
annotation class QuestSource {

    companion object {
        const val DEFAULT = 0
        const val HOME = 1
        const val REWARDS = 2
        const val SHOP = 3
        const val DISCO = 4

    }
}