package com.tokopedia.play.ui.chatlist.viewholder

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.PlayChatUiModel

/**
 * Created by jegul on 04/12/19
 */
class ChatViewHolder(
        itemView: View
) : BaseViewHolder(itemView) {

    private val tvChat = itemView.findViewById<TextView>(R.id.tv_chat)

    override fun onViewRecycled() {
        super.onViewRecycled()
        itemView.setOnClickListener {  }
    }

    fun bind(chat: PlayChatUiModel) {
        val userName = SpannableString(chat.name)
        userName.setSpan(
                ForegroundColorSpan(
                        MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Neutral_N150)
                ),
                0,
                chat.name.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvChat.text = ""
        tvChat.append(userName)
        tvChat.append(" ")
        tvChat.append(chat.message)
    }

    fun bind(quickReplyString: String, onQuickReplyClicked: (String) -> Unit) {
        tvChat.text = quickReplyString
        if (!itemView.hasOnClickListeners()) itemView.setOnClickListener { onQuickReplyClicked(quickReplyString) }
    }
}