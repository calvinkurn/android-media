package com.tokopedia.quest_widget.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.quest_widget.R
import com.tokopedia.quest_widget.data.Config
import com.tokopedia.quest_widget.data.QuestWidgetListItem
import com.tokopedia.quest_widget.listeners.QuestWidgetCallbacks
import com.tokopedia.quest_widget.tracker.QuestTracker

class QuestWidgetAdapter(
    val data: List<QuestWidgetListItem>,
    val configList: ArrayList<Config>?,
    val questTracker: QuestTracker,
    val source: Int,
    var questWidgetCallbacks: QuestWidgetCallbacks,
    var questWidgetPosition: Int
) : RecyclerView.Adapter<QuestWidgetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestWidgetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quest_widget_card, parent, false)
        return QuestWidgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestWidgetViewHolder, position: Int) {
        configList?.get(position)?.let { holder.questWidgetItemView.setData(data[position], it, questTracker, source, position, questWidgetCallbacks, questWidgetPosition) }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class QuestWidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val questWidgetItemView: QuestWidgetItemView = itemView.findViewById(R.id.quest_widget_root)
}