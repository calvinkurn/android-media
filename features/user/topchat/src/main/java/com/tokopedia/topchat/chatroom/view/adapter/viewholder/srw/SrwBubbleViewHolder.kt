package com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.SrwBubbleUiModel
import com.tokopedia.topchat.chatroom.view.custom.SrwLinearLayout

class SrwBubbleViewHolder(
        itemView: View?
) : AbstractViewHolder<SrwBubbleUiModel>(itemView) {

    private val srwLayout: SrwLinearLayout? = itemView?.findViewById(R.id.chat_srw_bubble)

    override fun bind(element: SrwBubbleUiModel) {
        setupSrw(element)
    }

    private fun setupSrw(element: SrwBubbleUiModel) {
        srwLayout?.initialize(element.srwHangingState)
    }

    companion object {
        val LAYOUT = R.layout.item_chat_srw_bubble
    }
}