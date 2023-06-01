package com.tokopedia.chatbot.chatbot2.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.chatbot.chatbot2.data.reject_reasons.DynamicAttachmentRejectReasons
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.adapter.ChatbotRejectReasonsAdapter
import com.tokopedia.chatbot.databinding.BottomSheetChatbotReasonsBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ChatbotRejectReasonsBottomSheet : BottomSheetUnify() {

    private var rejectReasonFeedbackForm: DynamicAttachmentRejectReasons.RejectReasonFeedbackForm? = null
    private var binding by autoClearedNullable<BottomSheetChatbotReasonsBinding>()
    private var reasonsAdapter: ChatbotRejectReasonsAdapter? = null
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
        binding = BottomSheetChatbotReasonsBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)

        setUpViews()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpViews() {
        rejectReasonFeedbackForm?.let { data ->
            binding?.apply {
                setTitle(data.title)
                senderIcon.urlSrc = data.iconTanya
                reasonTitleText.text = data.reasonTitle
                reasonText.setPlaceholder(data.textBoxPlaceHolder)
                reasonsAdapter = ChatbotRejectReasonsAdapter()
                reasonsList.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    reasonsAdapter?.setList(data.reasonChipList)
                    adapter = reasonsAdapter
                }
            }


        }
    }

    companion object {

        fun newInstance(rejectReasonFeedbackForm: DynamicAttachmentRejectReasons.RejectReasonFeedbackForm): ChatbotRejectReasonsBottomSheet {
            return ChatbotRejectReasonsBottomSheet().apply {
                this.rejectReasonFeedbackForm = rejectReasonFeedbackForm
            }
        }
    }
}
