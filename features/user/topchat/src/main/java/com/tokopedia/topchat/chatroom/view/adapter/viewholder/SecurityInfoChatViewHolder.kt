package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.SecurityInfoViewModel
import com.tokopedia.topchat.chatroom.view.listener.SecurityInfoListener

/**
 * @author by steven on 05/09/18.
 */

class SecurityInfoChatViewHolder(itemView: View, private val viewListener: SecurityInfoListener)
    : AbstractViewHolder<SecurityInfoViewModel>(itemView) {
    private val timeMachineText: TextView = itemView.findViewById<View>(R.id.time_machine_text) as TextView


    init {

        val securityInfo = itemView.context.getString(R.string.security_info_chat)
        val securityInfoLink = itemView.context.getString(R.string.security_info_chat_link)

        val spannable = SpannableString(String.format("%s %s", securityInfo, securityInfoLink))

        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(itemView.context, R.color.medium_green)
            }
        }, securityInfo.length, spannable.length, 0)

        timeMachineText.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    override fun bind(element: SecurityInfoViewModel) {
        timeMachineText.setOnClickListener { v -> viewListener.onGoToSecurityInfo(element.url) }
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.security_info_chatroom_layout
    }
}
