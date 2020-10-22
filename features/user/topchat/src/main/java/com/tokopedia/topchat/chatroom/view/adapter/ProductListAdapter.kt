package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ProductCarouselAttachmentViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel

class ProductListAdapter constructor(
        private val searchListener: SearchListener,
        private val listener: ProductAttachmentListener,
        private val deferredAttachment: DeferredViewHolderAttachment,
        private val commonListener: CommonViewHolderListener,
        private val adapterListener: AdapterListener,
        private val isUnifyBroadcast: Boolean = false
) : RecyclerView.Adapter<TopchatProductAttachmentViewHolder>() {

    var carousel: ProductCarouselUiModel? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = carousel?.products?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopchatProductAttachmentViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(ProductCarouselAttachmentViewHolder.LAYOUT, parent, false)
        return ProductCarouselAttachmentViewHolder(
                view, listener, deferredAttachment, searchListener, commonListener, adapterListener
        )
    }

    override fun onBindViewHolder(holder: TopchatProductAttachmentViewHolder, position: Int) {
        carousel?.products?.get(position)?.let {
            holder.bind(it as ProductAttachmentViewModel, isUnifyBroadcast)
        }
    }

}