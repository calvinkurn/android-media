package com.tokopedia.content.common.report_content.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.report_content.viewholder.FeedMenuViewHolder
import com.tokopedia.content.common.report_content.model.ContentMenuItem

/**
 * Created By : Shruti Agarwal on Feb 02, 2023
 */
class FeedMenuAdapter(
    private val listener: FeedMenuViewHolder.Listener,
) : RecyclerView.Adapter<FeedMenuViewHolder>() {

    private val data = mutableListOf<ContentMenuItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedMenuViewHolder {
        return FeedMenuViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: FeedMenuViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateData(itemList: List<ContentMenuItem>) {
        data.clear()
        data.addAll(itemList)
        notifyDataSetChanged()
    }
}
