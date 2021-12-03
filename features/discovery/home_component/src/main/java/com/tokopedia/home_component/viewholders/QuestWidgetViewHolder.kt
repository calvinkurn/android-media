package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.visitable.QuestWidgetModel
import com.tokopedia.quest_widget.constants.QuestWidgetLocations
import com.tokopedia.quest_widget.listeners.QuestWidgetCallbacks
import com.tokopedia.quest_widget.tracker.QuestSource
import com.tokopedia.quest_widget.view.QuestWidgetView

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
        questWidget?.setupListeners(questWidgetCallbacks)
        if(element?.questData == null){
            questWidget?.getQuestList(page = QuestWidgetLocations.HOME_PAGE, source = QuestSource.HOME)
        }
    }

}