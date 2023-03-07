package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.ProductBundlingCardViewHolder
import com.tokopedia.topchat.chatroom.view.custom.product_bundling.ProductBundlingCardAttachmentContainer
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel

class MultipleProductBundlingAdapter(
    private val listener: ProductBundlingListener,
    private val adapterListener: AdapterListener,
    private val searchListener: SearchListener,
    private val commonListener: CommonViewHolderListener,
    private val deferredAttachment: DeferredViewHolderAttachment
) : RecyclerView.Adapter<ProductBundlingCardViewHolder>() {

    var carousel: MultipleProductBundlingUiModel? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var source: ProductBundlingCardAttachmentContainer.BundlingSource? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductBundlingCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ProductBundlingCardViewHolder(view, listener, adapterListener,
            searchListener, commonListener, deferredAttachment, source)
    }

    override fun onBindViewHolder(holder: ProductBundlingCardViewHolder, position: Int) {
        carousel?.listBundling?.get(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = carousel?.listBundling?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return ProductBundlingCardViewHolder.LAYOUT_CAROUSEL
    }
}