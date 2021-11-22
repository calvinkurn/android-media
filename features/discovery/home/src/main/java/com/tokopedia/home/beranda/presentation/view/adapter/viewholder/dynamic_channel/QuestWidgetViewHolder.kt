package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.QuestWidgetModel
import com.tokopedia.quest_widget.constants.QuestWidgetLocations
import com.tokopedia.quest_widget.view.QuestWidgetView

const val HOMEPAGE_PARAM = "homepag1"
class QuestWidgetViewHolder(
    itemView: View
): AbstractViewHolder<QuestWidgetModel>(itemView){

    private var questWidget: QuestWidgetView?=null
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_quest_widget
    }

    init {
        questWidget = itemView.findViewById(R.id.questWidget)
    }

    override fun bind(element: QuestWidgetModel) {
//        questWidget?.setQuestData(element.questData)
        questWidget?.getQuestList(page = QuestWidgetLocations.MY_REWARD)
    }
}