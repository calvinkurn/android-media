package com.tokopedia.tokopoints.view.tokopointhome.topquest

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.quest_widget.listeners.QuestWidgetCallbacks
import com.tokopedia.quest_widget.tracker.QuestSource
import com.tokopedia.quest_widget.view.QuestWidgetView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.homeresponse.RewardsRecommendation
import com.tokopedia.tokopoints.view.tokopointhome.RewardsRecomListener

class SectionTopQuestVH(
    val view: View,
    var questWidgetCallbacks: QuestWidgetCallbacks
) : RecyclerView.ViewHolder(view) {

    fun bind(data: RewardsRecommendation) {
         val view:QuestWidgetView = view.findViewById(R.id.topquest)
         view.setupListeners(questWidgetCallbacks)
         view.getQuestList(0,"","myreward", QuestSource.REWARDS)
    }
}