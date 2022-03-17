package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.BundleItem
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.MultipleProductBundlingListViewHolder

class MultipleProductBundlingAdapter: RecyclerView.Adapter<MultipleProductBundlingListViewHolder>() {

    var bundlingList: List<BundleItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MultipleProductBundlingListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(MultipleProductBundlingListViewHolder.LAYOUT, parent, false)
        return MultipleProductBundlingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MultipleProductBundlingListViewHolder, position: Int) {
        holder.bind(bundlingList[position])
    }

    override fun getItemCount(): Int = bundlingList.size
}