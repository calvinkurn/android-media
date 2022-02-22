package com.tokopedia.home_component.viewholders

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.visitable.QuestWidgetModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.quest_widget.constants.QuestWidgetLocations
import com.tokopedia.quest_widget.listeners.QuestWidgetCallbacks
import com.tokopedia.quest_widget.tracker.QuestSource
import com.tokopedia.quest_widget.view.QuestWidgetView
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

class QuestWidgetViewHolder(
    itemView: View,
    var questWidgetCallbacks: QuestWidgetCallbacks
): AbstractViewHolder<QuestWidgetModel>(itemView){

    private var questWidget: QuestWidgetView?=null
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_quest_widget
    }

    init {
        questWidget = itemView.findViewById(R.id.questWidget)
    }

    override fun bind(element: QuestWidgetModel?) {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(questWidget?.context)
        if(config.getBoolean(RemoteConfigKey.ENABLE_QUEST_WIDGET, true)) {
            questWidget?.show()
            questWidget?.setupListeners(questWidgetCallbacks)
            if (element?.questData == null) {
                questWidget?.getQuestList(
                    page = QuestWidgetLocations.HOME_PAGE,
                    source = QuestSource.HOME,
                    position = adapterPosition
                )
            }
        }
        else{
            val params: ViewGroup.LayoutParams = this.itemView.layoutParams
            params.height = 0
            params.width = 0
            this.itemView.layoutParams = params
            questWidget?.hide()
        }
    }

}