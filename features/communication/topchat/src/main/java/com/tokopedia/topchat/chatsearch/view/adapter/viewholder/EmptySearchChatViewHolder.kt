package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadIcon
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
        ivIcon?.loadIcon(iconUrl)
    }

    private fun bindCtaClick(element: EmptyModel) {
        tvRetryButton?.setOnClickListener {
            listener?.onClickChangeKeyword()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_empty_chat_search
        private const val iconUrl = TokopediaImageUrl.SEARCH_ICON_URL
    }
}