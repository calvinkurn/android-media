package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.customview.CustomChatbotChatLayout
import com.tokopedia.kotlin.extensions.view.toLongOrZero

open class BaseChatBotViewHolder<T : Visitable<*>>(itemView: View) : BaseChatViewHolder<T>(itemView) {

    protected val customChatLayout: CustomChatbotChatLayout? = itemView.findViewById(getCustomChatLayoutId())

    open protected fun getCustomChatLayoutId():Int{ return com.tokopedia.chatbot.R.id.customChatLayout}

    private val bg = ViewUtil.generateBackgroundWithShadow(
            customChatLayout,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER
    )

    override fun bind(viewModel: T) {
        if (viewModel is BaseChatViewModel){
            bindBackground()
            verifyReplyTime(viewModel)
            ChatbotMessageViewHolderBinder.bindHour(viewModel.replyTime, customChatLayout)
            setHeaderDate(viewModel)
        }

    }

    private fun bindBackground() {
        customChatLayout?.background = bg
    }

    private fun verifyReplyTime(chat: BaseChatViewModel) {
        try {
            if (chat.replyTime.toLongOrZero() / MILISECONDS < START_YEAR) {
                chat.replyTime = (chat.replyTime.toLongOrZero() * MILISECONDS).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getDateId(): Int {
        return R.id.date
    }
}