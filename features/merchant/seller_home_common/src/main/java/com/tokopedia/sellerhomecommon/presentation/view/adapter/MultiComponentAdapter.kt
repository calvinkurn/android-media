package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.multicomponent.MultiComponentTabViewHolder


class MultiComponentAdapter(
    private val listener: WidgetListener
) : RecyclerView.Adapter<MultiComponentTabViewHolder>() {

    private val items: MutableList<MultiComponentTab> = mutableListOf()
    private var widgetType: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiComponentTabViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.shc_multi_component_view, parent, false)
        return MultiComponentTabViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: MultiComponentTabViewHolder, position: Int) {
        holder.bind(items[position], widgetType)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // For notify multi component widget loading
    fun setWidgetType(widgetType: String) {
        this.widgetType = widgetType
    }

    fun updateTabList(tabs: List<MultiComponentTab>) {
        val diffCallback = MultiComponentTabDiffer(items, tabs)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(tabs)
        diffResult.dispatchUpdatesTo(this)
    }
}
