package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.topchat.R

class EmptySearchChatViewHolder(
        itemView: View?,
        private val listener: Listener?
) : AbstractViewHolder<EmptyModel>(itemView) {

    private val ivIcon: ImageView? = itemView?.findViewById(R.id.iv_icon)
    private val tvRetryButton: TextView? = itemView?.findViewById(R.id.button_retry)

    interface Listener {
        fun onClickChangeKeyword()
    }

    override fun bind(element: EmptyModel) {
        bindImageIcon(element)
        bindCtaClick(element)
    }

    private fun bindImageIcon(element: EmptyModel) {
        ImageHandler.LoadImage(ivIcon, iconUrl)
    }

    private fun bindCtaClick(element: EmptyModel) {
        tvRetryButton?.setOnClickListener {
            listener?.onClickChangeKeyword()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_empty_chat_search
        private const val iconUrl = "https://images.tokopedia.net/img/android/user/chat/img_chat_empty_search_result.png"
    }
}