package com.tokopedia.tokopoints.view.tokopointhome.topquest

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.quest_widget.listeners.QuestWidgetCallbacks
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder


class SectionTopQuestViewBinder(
    var questWidgetCallbacks: QuestWidgetCallbacks,
    )
    : SectionItemViewBinder<Any, SectionTopQuestVH>(
        Any::class.java) {

    override fun createViewHolder(parent: ViewGroup): SectionTopQuestVH {
        return SectionTopQuestVH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false ), questWidgetCallbacks)
    }

    override fun bindViewHolder(model:Any, viewHolder: SectionTopQuestVH) {
        viewHolder.bind()
    }

    override fun getSectionItemType() = R.layout.tp_topquest_container

}
