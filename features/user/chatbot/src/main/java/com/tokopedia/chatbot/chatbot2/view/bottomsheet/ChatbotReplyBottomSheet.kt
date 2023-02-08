package com.tokopedia.chatbot.chatbot2.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.adapter.ChatbotReplyBottomSheetAdapter
import com.tokopedia.chatbot.databinding.BottomsheetChatbotReplyBinding
import com.tokopedia.chatbot.view.uimodel.ChatbotReplyOptionsUiModel
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ChatbotReplyBottomSheet(
    private val messageUiModel: MessageUiModel,
    private val listener: ChatbotReplyBottomSheetAdapter.ReplyBubbleBottomSheetListener,
    private val isReplyBubbleEnabled: Boolean
): BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomsheetChatbotReplyBinding>()
    private var replyAdapter: ChatbotReplyBottomSheetAdapter? = null

    init {
        isFullpage = false
        clearContentPadding = false
        showCloseIcon = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetChatbotReplyBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)

        setTitle(
            context?.resources?.getString(R.string.chatbot_reply_bubble_bottomsheet_title)
                .toBlankOrString()
        )

        binding?.rvReplyBubble?.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val list = getList(isReplyBubbleEnabled)
            replyAdapter?.setList(list)
            adapter = replyAdapter
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun getList(isReplyBubbleEnabled: Boolean): List<ChatbotReplyOptionsUiModel> {
        if (isReplyBubbleEnabled) {
            return listOf(
                ChatbotReplyOptionsUiModel.Reply(
                    context?.resources?.getString(R.string.chatbot_bottomsheet_reply)
                        .toBlankOrString()
                ),
                ChatbotReplyOptionsUiModel.CopyToClipboard(
                    context?.resources?.getString(R.string.chatbot_bottomsheet_copy)
                        .toBlankOrString()
                )
            )
        } else {
            return listOf(
                ChatbotReplyOptionsUiModel.CopyToClipboard(
                    context?.resources?.getString(R.string.chatbot_bottomsheet_copy)
                        .toBlankOrString()
                )
            )
        }
    }

    fun setOnMenuClickListener(callback: (ChatbotReplyOptionsUiModel) -> Unit) {
        this.replyAdapter = ChatbotReplyBottomSheetAdapter(callback)
    }
}

