package com.tokopedia.chatbot.view.adapter.viewholder

import android.text.TextUtils
import android.text.format.DateUtils
import android.view.Gravity
import android.view.View
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.domain.pojo.SenderInfoData
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.view.customview.CustomChatbotChatLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import java.text.SimpleDateFormat
import java.util.*

open class BaseChatBotViewHolder<T : Visitable<*>>(itemView: View,
                                                   private val chatbotAdapterListener: ChatbotAdapterListener? = null) : BaseChatViewHolder<T>(itemView) {

    protected val customChatLayout: CustomChatbotChatLayout? = itemView.findViewById(getCustomChatLayoutId())
    private val senderAvatar = itemView.findViewById<ImageUnify>(getSenderAvatarId())
    private val senderName = itemView.findViewById<Typography>(getSenderNameId())

    open protected fun getCustomChatLayoutId(): Int = com.tokopedia.chatbot.R.id.customChatLayout
    open protected fun getSenderAvatarId(): Int = R.id.senderAvatar
    open protected fun getSenderNameId(): Int = R.id.senderName

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
        if (viewModel is BaseChatViewModel) {
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

    private fun hideSenderInfo() {
        senderAvatar?.hide()
        senderName?.hide()
    }

    private fun convertToSenderInfo(source: String): SenderInfoData? {
        if (source.isNotEmpty() && source.startsWith("chatbot")) {
            val s = source.substring(8, source.length)
            return GsonBuilder().create()
                    .fromJson<SenderInfoData>(s,
                            SenderInfoData::class.java)
        } else return null
    }

    private fun bindSenderInfo(senderInfoData: SenderInfoData) {
        senderAvatar?.show()
        senderName?.show()
        ImageHandler.loadImageCircle2(itemView.context, senderAvatar, senderInfoData.iconUrl)
        senderName?.text = senderInfoData.name
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

    override fun setHeaderDate(element: BaseChatViewModel?) {
        if (date == null) return
        var time: String? = ""

        try {
            var myTime = element?.replyTime?.toLong()
            if (myTime != null) {
                myTime = myTime / MILISECONDS
                val date = Date(myTime)
                time = if (DateUtils.isToday(myTime)) {
                    itemView.context.getString(com.tokopedia.chat_common.R.string.chat_today_date)
                } else if (DateUtils.isToday(myTime + DateUtils.DAY_IN_MILLIS)) {
                    itemView.context.getString(com.tokopedia.chat_common.R.string.chat_yesterday_date)
                } else {
                    val formatter = SimpleDateFormat("d MMM")
                    formatter.format(date)
                }
            }

        } catch (e: NumberFormatException) {
            time = element?.replyTime
        }

        if (date != null && element?.isShowDate == true
                && !TextUtils.isEmpty(time)) {
            date.show()
            date.text = time
        } else if (date != null) {
            date.hide()
        }
    }

    override fun getDateId(): Int {
        return R.id.date
    }
}
