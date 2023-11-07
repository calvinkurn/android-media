package com.tokopedia.catalogcommon.viewholder.columnedinfo

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ColumnedInfoAdapter(
    private val items: List<Pair<String, String>> = listOf()
) : RecyclerView.Adapter<ColumnedInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnedInfoViewHolder {
        val rootView = ColumnedInfoViewHolder.createRootView(parent)
        return ColumnedInfoViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ColumnedInfoViewHolder, position: Int) {
        holder.bind(items[position], position < itemCount.dec())
    }

    override fun getItemCount() = items.size
}
