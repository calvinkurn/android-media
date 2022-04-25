package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.ProductBundlingCardViewHolder
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel

class MultipleProductBundlingAdapter(
    private val listener: ProductBundlingListener,
    private val adapterListener: AdapterListener,
    private val searchListener: SearchListener,
    private val commonListener: CommonViewHolderListener,
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
        return ProductBundlingCardViewHolder(view, listener, adapterListener,
            searchListener, commonListener)
    }

    override fun onBindViewHolder(holder: ProductBundlingCardViewHolder, position: Int) {
        carousel?.listBundling?.get(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = carousel?.listBundling?.size ?: EMPTY_PRODUCT_BUNDLING

    override fun getItemViewType(position: Int): Int {
        return when(carousel?.listBundling?.size?: EMPTY_PRODUCT_BUNDLING) {
            EMPTY_PRODUCT_BUNDLING -> throw IllegalStateException("No such type")
            MAX_SINGLE_SIZE -> ProductBundlingCardViewHolder.LAYOUT_SINGLE
            else -> ProductBundlingCardViewHolder.LAYOUT_CAROUSEL
        }
    }

    companion object {
        private const val EMPTY_PRODUCT_BUNDLING = 0
        private const val MAX_SINGLE_SIZE = 1
    }
}