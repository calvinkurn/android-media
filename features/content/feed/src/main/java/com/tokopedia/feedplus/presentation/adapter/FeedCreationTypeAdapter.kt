package com.tokopedia.feedplus.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedCreationTypeViewHolder
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem

/**
 * Created By : Shruti Agarwal on Feb 02, 2023
 */
class FeedCreationTypeAdapter(
    private val listener: FeedCreationTypeViewHolder.Listener,
) : RecyclerView.Adapter<FeedCreationTypeViewHolder>() {

    private val data = mutableListOf<ContentCreationTypeItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedCreationTypeViewHolder {
        return FeedCreationTypeViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: FeedCreationTypeViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateData(itemList: List<ContentCreationTypeItem>) {
        data.clear()
        data.addAll(itemList)
        notifyDataSetChanged()
    }
}
