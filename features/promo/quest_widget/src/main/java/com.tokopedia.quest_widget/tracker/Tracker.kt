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
    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
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