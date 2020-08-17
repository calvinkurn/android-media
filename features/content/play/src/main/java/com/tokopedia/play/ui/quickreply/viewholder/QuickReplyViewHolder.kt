package com.tokopedia.play.ui.quickreply.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.R

/**
 * Created by jegul on 06/07/20
 */
class QuickReplyViewHolder(
        itemView: View
) : BaseViewHolder(itemView) {

    private val tvChat = itemView.findViewById<TextView>(R.id.tv_quick_reply)

    fun bind(quickReplyString: String, onQuickReplyClicked: (String) -> Unit) {
        tvChat.text = quickReplyString
        itemView.setOnClickListener { onQuickReplyClicked(quickReplyString) }
    }

    companion object {
        val LAYOUT = R.layout.item_play_quick_reply
    }
}