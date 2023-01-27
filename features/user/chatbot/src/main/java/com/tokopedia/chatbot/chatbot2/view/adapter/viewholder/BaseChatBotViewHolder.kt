package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import androidx.cardview.widget.CardView
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.data.senderinfo.SenderInfoData
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatBotTimeConverter
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.chatbot2.view.util.view.ViewUtil
import com.tokopedia.chatbot.view.customview.CustomChatbotChatLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

open class BaseChatBotViewHolder<T : Visitable<*>>(
    itemView: View,
    private val chatbotAdapterListener: ChatbotAdapterListener? = null
) : BaseChatViewHolder<T>(itemView) {

    protected val customChatLayout: CustomChatbotChatLayout? = itemView.findViewById(getCustomChatLayoutId())
    private val senderAvatar = itemView.findViewById<ImageUnify>(getSenderAvatarId())
    private val senderName = itemView.findViewById<Typography>(getSenderNameId())
    private val dateContainer: CardView? = itemView.findViewById(getDateContainerId())

    protected open fun getCustomChatLayoutId(): Int = R.id.customChatLayout
    protected open fun getSenderAvatarId(): Int = R.id.senderAvatar
    protected open fun getSenderNameId(): Int = R.id.senderName
    open fun getDateContainerId(): Int = R.id.dateContainer

    private val bg = ViewUtil.generateBackgroundWithShadow(
        customChatLayout,
        R.color.chatbot_dms_left_message_bg,
        R.dimen.dp_chatbot_0,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_20,
        com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
        R.dimen.dp_chatbot_2,
        R.dimen.dp_chatbot_1,
        Gravity.CENTER
    )

    override fun bind(viewModel: T) {
        if (viewModel is BaseChatUiModel) {
            bindBackground()
            verifyReplyTime(viewModel)
            ChatbotMessageViewHolderBinder.bindHour(viewModel.replyTime, customChatLayout)
            setHeaderDate(viewModel)
            hideSenderInfo()
            val senderInfoData = convertToSenderInfo(viewModel.source)
            if (chatbotAdapterListener?.isPreviousItemSender(adapterPosition) == true) {
                senderInfoData?.let { bindSenderInfo(it) }
            }
        }
    }

    protected fun hideSenderInfo() {
        senderAvatar?.hide()
        senderName?.hide()
    }

    protected fun convertToSenderInfo(source: String): SenderInfoData? {
        val senderInfoPrefix = itemView.context.getString(R.string.chatbot_sender_info_prefix)
        if (source.isNotEmpty() && source.startsWith(senderInfoPrefix)) {
            val s = source.substring(senderInfoPrefix.length, source.length)
            return GsonBuilder().create()
                .fromJson<SenderInfoData>(
                    s,
                    SenderInfoData::class.java
                )
        } else {
            return null
        }
    }

    protected fun bindSenderInfo(senderInfoData: SenderInfoData) {
        senderAvatar?.show()
        senderName?.show()
        ImageHandler.loadImageCircle2(itemView.context, senderAvatar, senderInfoData.iconUrl)
        senderName?.text = senderInfoData.name
    }

    private fun bindBackground() {
        customChatLayout?.background = bg
    }

    protected fun verifyReplyTime(chat: BaseChatUiModel) {
        try {
            if (chat.replyTime.toLongOrZero() / MILISECONDS < START_YEAR) {
                chat.replyTime = (chat.replyTime.toLongOrZero() * MILISECONDS).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setHeaderDate(element: BaseChatUiModel) {
        if (date == null) return
        val time = element.replyTime?.let {
            ChatBotTimeConverter.getDateIndicatorTime(
                it,
                itemView.context.getString(com.tokopedia.chat_common.R.string.chat_today_date),
                itemView.context.getString(com.tokopedia.chat_common.R.string.chat_yesterday_date)
            )
        }
        date?.text = time
        if (date != null && element.isShowDate && !TextUtils.isEmpty(time)) {
            dateContainer?.show()
        } else if (date != null) {
            dateContainer?.hide()
        }
    }

    override val dateId: Int
        get() = R.id.date
}
