package com.tokopedia.play_common.ui.chat.viewholder

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play_common.R
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 09/06/20
 */
class PlayChatViewHolder(
        itemView: View,
        typographyType: Int
) : BaseViewHolder(itemView) {

    private val tvChat = itemView.findViewById<Typography>(R.id.tv_chat).apply {
        setType(typographyType)
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        itemView.setOnClickListener {  }
    }

    fun bind(chat: PlayChatUiModel) {
        val userName = SpannableString(chat.name)
        userName.setSpan(
                ForegroundColorSpan(
                        MethodChecker.getColor(itemView.context, R.color.Neutral_N150)
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

    companion object {

        val LAYOUT = R.layout.item_play_chat
    }
}