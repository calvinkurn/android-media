package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.EllipsizeMaker
import com.tokopedia.chatbot.EllipsizeMaker.MESSAGE_LINE_COUNT
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.util.ChatBotTimeConverter
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

    override fun alwaysShowTime(): Boolean = true

    override fun getHourTime(replyTime: String): String {
        return ChatBotTimeConverter.getHourTime(replyTime)
    }

    private fun setMessage(element: QuickReplyListViewModel) {
        if (element.message.isNotEmpty()) {
            message.text = MethodChecker.fromHtml(element.message)
            message.post {
                if (message.lineCount >= MESSAGE_LINE_COUNT) {
                    message.maxLines = MESSAGE_LINE_COUNT
                    message.text = EllipsizeMaker.getTruncatedMsg(message)
                    MethodChecker.setBackground(mesageLayout, ContextCompat.getDrawable(itemView.context,R.drawable.left_bubble_with_stroke))
                    mesageBottom.visibility = View.VISIBLE
                    mesageBottom.setOnClickListener {
                        ReadMoreBottomSheet.createInstance(element.message).show((itemView.context as FragmentActivity).supportFragmentManager,"read_more_bottom_sheet")
                    }

                } else {
                    mesageBottom.visibility = View.GONE
                    MethodChecker.setBackground(mesageLayout, ContextCompat.getDrawable(itemView.context,com.tokopedia.chat_common.R.drawable.left_bubble))
                }
            }

        }
    }

    private fun setClickableUrl() {
        message.movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
    }

    companion object {
        val LAYOUT = R.layout.quick_reply_chat_layout
    }

    override fun getHourId(): Int {
        return R.id.hour
    }
}
