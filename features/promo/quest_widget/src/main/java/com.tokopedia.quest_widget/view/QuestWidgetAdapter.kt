package com.tokopedia.quest_widget.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quest_widget.R
import com.tokopedia.quest_widget.data.QuestWidgetListItem

class QuestWidgetAdapter(val data: List<QuestWidgetListItem>, val isHiddenCta: Boolean) : RecyclerView.Adapter<QuestWidgetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestWidgetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quest_widget_card, parent, false)
        return QuestWidgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestWidgetViewHolder, position: Int) {
        data[position].let { holder.questWidgetItemView.setData(it) }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class QuestWidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val questWidgetItemView: QuestWidgetItemView = itemView.findViewById(R.id.quest_widget_root)
}