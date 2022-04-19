package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightAdsTypeAdapter.Companion.SelectAdsTypeViewHolder
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class TopAdsInsightAdsTypeAdapter(
    private val list: List<InsightAdObj>,
    private val adSelected: (Int,InsightAdObj) -> Unit
) : RecyclerView.Adapter<SelectAdsTypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectAdsTypeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(selectAdsTypeLayout, parent, false)
        return SelectAdsTypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectAdsTypeViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        holder.adName.text = item.adName
        holder.radioButton.isChecked = item.isSelected
        holder.itemView.setOnClickListener {
            adSelected.invoke(holder.adapterPosition,item)
            item.isSelected = true
            uncheckAll(holder.adapterPosition)
        }
    }

    private fun uncheckAll(position: Int) {
        list.forEachIndexed { index, it ->
            if (position != index) {
                it.isSelected = false
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    companion object {
        class SelectAdsTypeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val adName: Typography = view.findViewById(R.id.group_title)
            val radioButton: RadioButtonUnify = view.findViewById(R.id.radio_button)

            init {
                view.findViewById<Typography>(R.id.desc_group).hide()
            }
        }

        private val selectAdsTypeLayout = R.layout.topads_dash_item_moveto_group_recom
    }
}

data class InsightAdObj(
    val adName: String,
    var isSelected: Boolean
)