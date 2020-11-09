package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import kotlinx.android.synthetic.main.topads_dash_keyword_insight_mini_item_layout.view.*

class TopAdsMiniKeywordInsightAdapter(var onCheck: ((pos: Int) -> Unit?)) : RecyclerView.Adapter<TopAdsMiniKeywordInsightAdapter.ViewHolder>() {

    var items: MutableList<KeywordInsightDataMain> = mutableListOf()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun viewHolder(itemView: View) {
            super.itemView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_dash_keyword_insight_mini_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.img.setImageDrawable(holder.view.context.getResDrawable(R.drawable.topads_dashboard_folder))
        holder.view.arrow.setImageDrawable(holder.view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_arrow))
        val name = items[position].name + "(" + items[position].count.toString() + ")"
        holder.view.keywordName.text = name
        holder.view.setOnClickListener {
            onCheck(position)
        }
    }

}