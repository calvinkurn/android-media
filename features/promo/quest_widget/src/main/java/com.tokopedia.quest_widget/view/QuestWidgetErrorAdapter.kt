package com.tokopedia.quest_widget.view

 import android.view.LayoutInflater
 import android.view.View
 import android.view.ViewGroup
 import androidx.recyclerview.widget.RecyclerView
 import com.tokopedia.quest_widget.R
 import com.tokopedia.unifycomponents.ImageUnify

class QuestWidgetErrorAdapter(var handleError: HandleError) : RecyclerView.Adapter<QuestWidgetErrorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestWidgetErrorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quest_widget_error_card, parent, false)
        return QuestWidgetErrorViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestWidgetErrorViewHolder, position: Int) {
        holder.retry.setOnClickListener {
            handleError.retry()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}

class QuestWidgetErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val retry: ImageUnify = itemView.findViewById(R.id.iv_error)
}

interface HandleError{
    fun retry()
}