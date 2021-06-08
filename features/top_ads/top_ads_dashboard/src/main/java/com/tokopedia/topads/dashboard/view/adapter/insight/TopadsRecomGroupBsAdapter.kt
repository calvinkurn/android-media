package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import kotlinx.android.synthetic.main.topads_dash_item_moveto_group_recom.view.*

private const val VIEW_SHIMMER = 0
private const val VIEW_GROUP = 1
private const val DEFAULT_SHIMMER_COUNT = 5

class TopadsRecomGroupBsAdapter(private val onGroupSelect: (() -> Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: MutableList<GroupListDataItem> = mutableListOf()
    private var list: List<CountDataItem> = listOf()
    private var showShimmer = true
    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_GROUP -> {
                val v = LayoutInflater.from(parent.context).inflate(GroupViewHolder.Layout, parent, false)
                GroupViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(ShimmerViewHolder.Layout, parent, false)
                return ShimmerViewHolder(v)
            }
        }
    }

    class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            val Layout = R.layout.topads_dash_item_moveto_group_recom
        }

        val name: TextView = view.findViewById(R.id.group_title)
        val radio: RadioButtonUnify = view.findViewById(R.id.radio_button)
        val groupDesc: TextView = view.findViewById(R.id.desc_group)

    }

    class ShimmerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            val Layout = R.layout.topads_dash_item_choose_group_recom_bs_shimmer
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (showShimmer)
            VIEW_SHIMMER else
            VIEW_GROUP
    }

    override fun getItemCount(): Int {
        return if (showShimmer) {
            DEFAULT_SHIMMER_COUNT
        } else
            return items.size
    }

    fun setShimmer() {
        items.clear()
        showShimmer = true
        notifyDataSetChanged()
    }

    fun getCheckedPosition(): String {
        return items[selectedPosition].groupId
    }

    fun isChecked():Int{
        return selectedPosition
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? GroupViewHolder)?.let {
            it.name.text = items[holder.adapterPosition].groupName
            it.radio.isChecked = items[holder.adapterPosition].isSelected
            it.groupDesc.text = String.format(it.itemView.desc_group.context.getString(R.string.topads_dash_grp_bs_item_desc), list[holder.adapterPosition].totalAds, list[holder.adapterPosition].totalKeywords)
            it.itemView.setOnClickListener { v ->
                onGroupSelect.invoke()
                items[holder.adapterPosition].isSelected = true
                setOtherFalse(holder.adapterPosition)
                v.radio_button.isChecked = true
                selectedPosition = position
            }
        }
    }

    private fun setOtherFalse(adapterPosition: Int) {
        items.forEachIndexed { index, it ->
            if (adapterPosition != index) {
                it.isSelected = false
            }
        }
        notifyDataSetChanged()
    }

    fun setItems(groupList: List<GroupListDataItem>) {
        this.items = groupList as MutableList<GroupListDataItem>
    }

    fun setKeyProductCount(list: List<CountDataItem>) {
        showShimmer = false
        this.list = list
        notifyDataSetChanged()
    }
}