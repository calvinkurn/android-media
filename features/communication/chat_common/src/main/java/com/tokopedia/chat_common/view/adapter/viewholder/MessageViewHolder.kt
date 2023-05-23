package com.tokopedia.chat_common.view.adapter.viewholder

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import java.util.Locale

/**
 * @author by nisie on 5/16/18.
 */
open class MessageViewHolder(
    itemView: View, listener: ChatLinkHandlerListener?
) : BaseChatViewHolder<MessageUiModel>(itemView) {

    private val context: Context = itemView.context

    @JvmField
    protected var message: TextView
    private val chatStatus: ImageView? by lazy {
        itemView.findViewById(chatStatusId)
    }
    private val chatBalloon: View? by lazy {
        itemView.findViewById(mainId)
    }
    private val name: TextView? by lazy {
        itemView.findViewById(nameId)
    }
    private val label: TextView? by lazy {
        itemView.findViewById(labelId)
    }
    private val dot: TextView? by lazy {
        itemView.findViewById(dotId)
    }

    override val hourId: Int
        get() = R.id.hour
    override val dateId: Int
        get() = R.id.date
    override val chatStatusId: Int
        get() = R.id.chat_status
    protected open val messageId: Int
        get() = R.id.message
    protected open val nameId: Int
        get() = R.id.name
    protected open val labelId: Int
        get() = R.id.label
    protected open val dotId: Int
        get() = R.id.dot
    protected open val mainId: Int
        get() = R.id.main

    override fun bind(uiModel: MessageUiModel) {
        super.bind(uiModel)
        uiModel.let {
            message.text = MethodChecker.fromHtml(it.message)
            chatBalloon?.let { view ->
                setupChatBubbleAlignment(view, it)
            }

            setRole(it)
        }
    }

    private fun setupChatBubbleAlignment(chatBalloon: View, element: MessageUiModel) {
        if (element.isSender) {
            setChatRight(chatBalloon)
            setReadStatus(element)
        } else {
            setChatLeft(chatBalloon)
        }
    }

    protected open fun setChatLeft(chatBalloon: View) {
        chatBalloon.background = context.resources.getDrawable(R.drawable.left_bubble)
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, chatBalloon)
        hour?.let {
            setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, it)
        }
        message.setTextColor(
            MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
            )
        )
        message.setLinkTextColor(
            MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
            )
        )
        chatStatus?.visibility = View.GONE
    }

    private fun setAlignParent(alignment: Int, view: View) {
        val params = view.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
        params.addRule(alignment)
        view.layoutParams = params
    }

    protected open fun setChatRight(chatBalloon: View) {
        chatBalloon.background =
            context.resources.getDrawable(R.drawable.right_bubble)
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, chatBalloon)
        hour?.let {
            setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, it)
        }

        message.setTextColor(
            MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN0
            )
        )
        message.setLinkTextColor(
            MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN0
            )
        )
        chatStatus?.visibility = View.VISIBLE
    }

    private fun setRole(element: MessageUiModel) {
        if (!TextUtils.isEmpty(element.fromRole)
            && element.fromRole.lowercase(Locale.getDefault()) != ROLE_USER.lowercase(Locale.getDefault())
            && element.isSender
            && !element.isDummy
            && element.isShowRole
        ) {
            name?.text = element.from
            label?.text = element.fromRole
            name?.visibility = View.VISIBLE
            dot?.visibility = View.VISIBLE
            label?.visibility = View.VISIBLE
        } else {
            name?.visibility = View.GONE
            label?.visibility = View.GONE
            dot?.visibility = View.GONE
        }
    }

    private fun setReadStatus(element: MessageUiModel) {
        var imageResource: Int
        if (element.isShowTime) {
            chatStatus?.visibility = View.VISIBLE
            imageResource = if (element.isRead) {
                R.drawable.ic_chat_read
            } else {
                R.drawable.ic_chat_unread
            }
            if (element.isDummy) {
                imageResource = R.drawable.ic_chat_pending
            }
            chatStatus?.let {
                it.setImageDrawable(
                    MethodChecker.getDrawable(
                        it.context,
                        imageResource
                    )
                )
            }

        } else {
            chatStatus?.visibility = View.GONE
        }
    }

    companion object {
        private const val ROLE_USER = "User"

        @LayoutRes
        val LAYOUT = R.layout.layout_message_chat
    }

    init {
        message = itemView.findViewById(messageId)
        message.movementMethod = ChatLinkHandlerMovementMethod(listener)
    }
}