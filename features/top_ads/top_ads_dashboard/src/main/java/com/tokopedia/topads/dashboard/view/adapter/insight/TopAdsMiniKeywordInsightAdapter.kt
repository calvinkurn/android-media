package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TopAdsMiniKeywordInsightAdapter(var onCheck: ((pos: Int) -> Unit?)) :
    RecyclerView.Adapter<TopAdsMiniKeywordInsightAdapter.ViewHolder>() {

    var items: MutableList<KeywordInsightDataMain> = mutableListOf()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageUnify? = view.findViewById(R.id.img)
        val keywordName: Typography? = view.findViewById(R.id.keywordName)
        val arrow: ImageUnify? = view.findViewById(R.id.arrow)
        fun viewHolder(itemView: View) {
            super.itemView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_dash_keyword_insight_mini_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.img?.setImageDrawable(holder.view.context.getResDrawable(R.drawable.topads_dashboard_folder))
        holder.arrow?.setImageDrawable(holder.view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_arrow))
        val name = items[position].name + "(" + items[position].count.toString() + ")"
        holder.keywordName?.text = name
        holder.view.setOnClickListener {
            onCheck(position)
        }
    }

}