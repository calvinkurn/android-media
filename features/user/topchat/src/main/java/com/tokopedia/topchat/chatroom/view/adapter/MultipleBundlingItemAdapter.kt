package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.BundleItem
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.MultipleBundlingItemViewHolder

class MultipleBundlingItemAdapter: RecyclerView.Adapter<MultipleBundlingItemViewHolder>() {

    var bundlingList: List<BundleItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MultipleBundlingItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(MultipleBundlingItemViewHolder.LAYOUT, parent, false)
        return MultipleBundlingItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MultipleBundlingItemViewHolder, position: Int) {
        holder.bind(bundlingList[position])
    }

    override fun getItemCount(): Int = bundlingList.size
}