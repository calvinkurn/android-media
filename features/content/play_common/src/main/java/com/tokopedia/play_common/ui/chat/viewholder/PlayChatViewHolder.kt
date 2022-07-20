package com.tokopedia.play_common.ui.chat.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play_common.R
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.extension.append
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

    fun bind(chat: PlayChatUiModel) {
        val chatText = constructChatText(
            userName = chat.name,
            message = chat.message
        )

        tvChat.text = chatText
    }

    private fun constructChatText(
        userName: String,
        message: String,
    ): CharSequence {
        val spanBuilder = SpannableStringBuilder()
        spanBuilder.append(
            text = userName,
            flags = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            StyleSpan(Typeface.BOLD),
            ForegroundColorSpan(
                MethodChecker.getColor(
                    itemView.context,
                    R.color.play_dms_chat_user_color
                )
            ),
        )
        spanBuilder.append(" $message")

        return spanBuilder
    }

    companion object {

        val LAYOUT = R.layout.item_play_chat
    }
}