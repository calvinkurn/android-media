package com.tokopedia.discovery.categoryrevamp.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.view.interfaces.QuickFilterListener
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.unifyprinciples.Typography

class QuickFilterAdapter(private var quickFilterList: ArrayList<Filter>,
                         val quickFilterListener: QuickFilterListener) : RecyclerView.Adapter<QuickFilterAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_nav_quick_filter, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return quickFilterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option: Option = quickFilterList[position].options[0]
        bindFilterNewIcon(holder, option)

        holder.quickFilterText.text = option.name

        setBackgroundResource(holder, option)

        holder.quickFilterText.setOnClickListener {
            quickFilterListener.onQuickFilterSelected(option)
        }
        holder.filterNewIcon.setOnClickListener {
            quickFilterListener.onQuickFilterSelected(option)
        }
    }

    private fun bindFilterNewIcon(holder: ViewHolder, option: Option) {
        if (option.isNew) {
            holder.filterNewIcon.setVisibility(View.VISIBLE)
        } else {
            holder.filterNewIcon.setVisibility(View.GONE)
        }
    }


    private fun setBackgroundResource(holder: ViewHolder, option: Option) {
        if (quickFilterListener != null && quickFilterListener.isQuickFilterSelected(option)) {
            holder.itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_selected)
        } else {
            holder.itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_neutral)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quickFilterText = itemView.findViewById<Typography>(R.id.quick_filter_text)
        val itemContainer = itemView.findViewById<View>(R.id.filter_item_container)
        val filterNewIcon = itemView.findViewById<View>(R.id.filter_new_icon)
    }

    fun clearData() {
        val itemSizeBeforeCleared = itemCount
        quickFilterList.clear()
        notifyItemRangeRemoved(0, itemSizeBeforeCleared)
    }
}





