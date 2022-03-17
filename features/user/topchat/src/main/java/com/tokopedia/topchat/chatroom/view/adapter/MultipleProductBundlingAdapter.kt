package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.ProductBundlingCardViewHolder
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel

class MultipleProductBundlingAdapter(
    private val listener: ProductBundlingListener,
    private val adapterListener: AdapterListener
) : RecyclerView.Adapter<ProductBundlingCardViewHolder>() {

    var carousel: MultipleProductBundlingUiModel? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductBundlingCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ProductBundlingCardViewHolder(view, listener, adapterListener)
    }

    override fun onBindViewHolder(holder: ProductBundlingCardViewHolder, position: Int) {
        carousel?.listBundling?.get(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = carousel?.listBundling?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return when(carousel?.listBundling?.size?: 0) {
            1 -> ProductBundlingCardViewHolder.LAYOUT_SINGLE
            else -> ProductBundlingCardViewHolder.LAYOUT_CAROUSEL
        }
    }
}