package com.tokopedia.chat_common.view.adapter.viewholder

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.FallbackAttachmentUiModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener

/**
 * @author by nisie on 5/9/18.
 */
class FallbackAttachmentViewHolder(
    itemView: View, listener: ChatLinkHandlerListener
) : BaseChatViewHolder<FallbackAttachmentUiModel>(itemView) {

    private val listener: ChatLinkHandlerListener = listener
    private val context: Context = itemView.context
    private val message: TextView = itemView.findViewById(R.id.message)

    override fun bind(uiModel: FallbackAttachmentUiModel) {
        super.bind(uiModel)
        uiModel.let {
            setMessage(it)
            setClickableUrl()
            setupChatBubbleAlignment(message, it)
        }
    }

    private fun setupChatBubbleAlignment(chatBalloon: View, element: FallbackAttachmentUiModel) {
        if (element.isSender) {
            setChatRight(chatBalloon)
        } else {
            setChatLeft(chatBalloon)
        }
    }

    private fun setChatLeft(chatBalloon: View) {
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
    }

    private fun setChatRight(chatBalloon: View) {
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
    }

    private fun setAlignParent(alignment: Int, view: View) {
        val params = view.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
        params.addRule(alignment)
        view.layoutParams = params
    }

    private fun setMessage(element: FallbackAttachmentUiModel) {
        if (!element.message.isEmpty()) {
            message.text =
                MethodChecker.fromHtml(element.message)
        }
    }

    private fun setClickableUrl() {
        message.movementMethod = ChatLinkHandlerMovementMethod(listener)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_fallback_attachment
    }

}