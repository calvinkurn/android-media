package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.BundleItem
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.MultipleBundlingItemViewHolder
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel

class MultipleBundlingItemAdapter(
    private val listener: ProductBundlingListener?
): RecyclerView.Adapter<MultipleBundlingItemViewHolder>() {

    var bundlingList: List<BundleItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var productBundling: ProductBundlingUiModel? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MultipleBundlingItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(MultipleBundlingItemViewHolder.LAYOUT, parent, false)
        return MultipleBundlingItemViewHolder(view, listener, productBundling)
    }

    override fun onBindViewHolder(holder: MultipleBundlingItemViewHolder, position: Int) {
        if (position < MAX_ITEM_THRESHOLD) {
            holder.bind(bundlingList[position])
        }
    }

    override fun getItemCount(): Int = bundlingList.size

    companion object {
        const val MAX_ITEM_THRESHOLD = 3
    }
}