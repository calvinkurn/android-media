package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder

import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChatViewModel

/**
 * @author : Steven 18/02/19
 */
class PlayChatViewHolder(itemView: View) : AbstractViewHolder<ChatViewModel>(itemView) {


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.play_chat_view_holder
        const val MAX_LENGTH = 10
        const val extension= ".."
    }

    private val message: TextView = itemView.findViewById(R.id.text)

    override fun bind(element: ChatViewModel) {
        message.text = ""
        val userName = getUserName(element.senderName, element.isAdministrator)
        message.append(userName)
        message.append(" ")
        message.append(getColoredString(element.message, ContextCompat.getColor(itemView.context, R.color.white)))
    }

    private fun getUserName(senderName: String?, administrator: Boolean): Spannable {
        return when {
            administrator -> getColoredString(senderName, ContextCompat.getColor(itemView.context, R.color.play_admin))
            else -> getColoredString(getTrimmedName(senderName), ContextCompat.getColor(itemView.context, R.color.play_user))
        }
    }

    private fun getTrimmedName(senderName: String?): String? {
        senderName?.length?.let {
            return when {
                it <= MAX_LENGTH -> senderName
                else -> ("${senderName.take(MAX_LENGTH - extension.length)}$extension")
            }
        }
        return senderName

    }

    private fun getColoredString(mString: String?, colorId:Int): Spannable {
        val spannable = SpannableString(MethodChecker.fromHtml(mString))
        spannable.setSpan(ForegroundColorSpan(colorId), 0, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }
}