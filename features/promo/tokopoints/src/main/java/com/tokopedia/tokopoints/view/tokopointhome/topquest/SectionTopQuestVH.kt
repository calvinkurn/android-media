package com.tokopedia.tokopoints.view.tokopointhome.topquest

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.quest_widget.constants.QuestWidgetLocations
import com.tokopedia.quest_widget.listeners.QuestWidgetCallbacks
import com.tokopedia.quest_widget.tracker.QuestSource
import com.tokopedia.quest_widget.view.QuestWidgetView
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.homeresponse.RewardsRecommendation
import com.tokopedia.tokopoints.view.tokopointhome.RewardsRecomListener

class SectionTopQuestVH(
    val view: View,
    var questWidgetCallbacks: QuestWidgetCallbacks
) : RecyclerView.ViewHolder(view) {

    fun bind() {
        val view: QuestWidgetView = view.findViewById(R.id.topquest)

        val config: RemoteConfig = FirebaseRemoteConfigImpl(view.context)
        if(config.getBoolean(RemoteConfigKey.ENABLE_QUEST_WIDGET, true)) {
            view.show()
            view.setupListeners(questWidgetCallbacks)
            view.getQuestList(0, "", QuestWidgetLocations.MY_REWARD, QuestSource.REWARDS, position = adapterPosition)
        }
        else{
            val params: ViewGroup.LayoutParams = this.itemView.layoutParams
            params.height = 0
            params.width = 0
            this.itemView.layoutParams = params
            view.hide()
        }
    }
}