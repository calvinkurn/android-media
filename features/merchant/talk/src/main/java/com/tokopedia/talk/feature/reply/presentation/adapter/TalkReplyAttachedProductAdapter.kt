package com.tokopedia.talk.feature.reply.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.presentation.adapter.viewholder.TalkReplyAttachedProductViewHolder
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.AttachedProductCardListener
import com.tokopedia.talk.R

class TalkReplyAttachedProductAdapter(
        private val attachedProductCardListener: AttachedProductCardListener,
        private val isInViewHolder: Boolean
) : RecyclerView.Adapter<TalkReplyAttachedProductViewHolder>() {

    private var attachedProducts: List<AttachedProduct> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkReplyAttachedProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_talk_reply_attached_product_answer, parent, false)
        return TalkReplyAttachedProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return attachedProducts.size
    }

    override fun onBindViewHolder(holder: TalkReplyAttachedProductViewHolder, position: Int) {
       holder.bind(attachedProducts[position], attachedProductCardListener, isInViewHolder)
    }

    fun setData(attachedProducts: List<AttachedProduct>) {
        this.attachedProducts = attachedProducts
        notifyDataSetChanged()
    }
}