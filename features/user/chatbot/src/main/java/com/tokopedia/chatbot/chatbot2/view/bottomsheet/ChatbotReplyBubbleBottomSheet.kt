package com.tokopedia.chatbot.chatbot2.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.adapter.ReplyBubbleBottomSheetAdapter
import com.tokopedia.chatbot.databinding.BottomsheetChatbotReplyBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ChatbotReplyBubbleBottomSheet(
    private val messageUiModel: MessageUiModel,
    private val listener: ReplyBubbleBottomSheetAdapter.ReplyBubbleBottomSheetListener
) : BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomsheetChatbotReplyBinding>()
    private var replyAdapter: ReplyBubbleBottomSheetAdapter? = null

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
        replyAdapter = ReplyBubbleBottomSheetAdapter(messageUiModel)
        replyAdapter?.setListener(listener)

        binding?.rvReplyBubble?.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val list = ArrayList<Pair<String, Int>>()
            list.add(Pair("Balas", IconUnify.REPLY))
            replyAdapter?.setList(list)
            adapter = replyAdapter
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
