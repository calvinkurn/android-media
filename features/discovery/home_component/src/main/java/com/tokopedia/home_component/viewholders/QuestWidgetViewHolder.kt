package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.visitable.QuestWidgetModel
import com.tokopedia.quest_widget.constants.QuestWidgetLocations
import com.tokopedia.quest_widget.data.QuestData
import com.tokopedia.quest_widget.listeners.QuestWidgetCallbacks
import com.tokopedia.quest_widget.tracker.QuestSource
import com.tokopedia.quest_widget.view.QuestWidgetView

const val HOMEPAGE_PARAM = "homepag1"
class QuestWidgetViewHolder(
    itemView: View,
    var questWidgetCallbacks: QuestWidgetCallbacks
): AbstractViewHolder<QuestWidgetModel>(itemView){

    private var questWidget: QuestWidgetView?=null
    private lateinit var result: QuestData
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
//        else{
//            // what to do in case element.questdata is null, it will be null in first run
//            questWidget?.setQuestData(result, QuestSource.HOME)
//        }
    }

    // if element.questdata null
    // get result from api
    // else
    //

}