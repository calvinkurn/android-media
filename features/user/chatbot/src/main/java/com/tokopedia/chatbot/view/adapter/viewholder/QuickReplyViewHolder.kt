package com.tokopedia.chatbot.view.adapter.viewholder

import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.view.customview.ReadMoreBottomSheet

/**
 * @author by nisie on 5/8/18.
 */
class QuickReplyViewHolder(itemView: View,
                           private val chatLinkHandlerListener: ChatLinkHandlerListener)
    : BaseChatViewHolder<QuickReplyListViewModel>(itemView) {

    private val message: TextView
    private val mesageBottom: TextView
    private val mesageLayout: RelativeLayout

    init {
        message = itemView.findViewById<View>(R.id.message) as TextView
        mesageBottom = itemView.findViewById<View>(R.id.bottom_view) as TextView
        mesageLayout = itemView.findViewById<View>(R.id.message_layout) as RelativeLayout
    }

    override fun bind(element: QuickReplyListViewModel) {
        super.bind(element)
        setMessage(element)
        setClickableUrl()
    }

    private fun setMessage(element: QuickReplyListViewModel) {
        if (!element.message.isEmpty()) {
            message.text = MethodChecker.fromHtml(element.message)
            if (message.text.toString().length > 170) {

                mesageLayout.setBackgroundDrawable(ContextCompat.getDrawable(itemView.context, com.tokopedia.chatbot.R.drawable.left_bubble_with_stroke))
                mesageBottom.visibility = View.VISIBLE
                mesageBottom.setOnClickListener {
                    ReadMoreBottomSheet.createInstance(message.text.toString()).show((itemView.context as FragmentActivity).supportFragmentManager,"read_more_bottom_sheet")

                }

            } else {
                mesageBottom.visibility = View.GONE
                mesageLayout.setBackgroundDrawable(ContextCompat.getDrawable(itemView.context, com.tokopedia.chat_common.R.drawable.left_bubble))
            }
        }
    }

    private fun setClickableUrl() {
        message.movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
    }

    companion object {

        val LAYOUT = R.layout.quick_reply_chat_layout
    }
}
