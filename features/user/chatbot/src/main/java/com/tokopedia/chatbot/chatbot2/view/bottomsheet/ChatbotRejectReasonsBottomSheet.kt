package com.tokopedia.chatbot.chatbot2.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.chatbot2.data.reject_reasons.DynamicAttachmentRejectReasons
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.adapter.ChatbotRejectReasonsAdapter
import com.tokopedia.chatbot.databinding.BottomSheetChatbotReasonsBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.android.synthetic.main.item_chatbot_quick_reply_view.view.*

class ChatbotRejectReasonsBottomSheet : BottomSheetUnify() {

    private var rejectReasonFeedbackForm: DynamicAttachmentRejectReasons.RejectReasonFeedbackForm? = null
    private var binding by autoClearedNullable<BottomSheetChatbotReasonsBinding>()
    private var reasonsAdapter: ChatbotRejectReasonsAdapter? = null
    private var listener: ChatbotRejectReasonsListener? = null
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
                reasonText.apply {
                    setPlaceholder(data.textBoxPlaceHolder)
                    minLine = MINIMUM_LINES
                }
                reasonsAdapter = ChatbotRejectReasonsAdapter()
                reasonsList.apply {
                    layoutManager = getLayoutManager(data.reasonChipList)
                    reasonsAdapter?.setList(data.reasonChipList)
                    adapter = reasonsAdapter
                }

                btnSubmit.setOnClickListener {
                    listener?.submitRejectReasonsViaSocket(
                        reasonsAdapter?.selectedList ?: emptyList(),
                    reasonText.editText.text?.toString() ?: ""
                    )
                }
            }
        }
    }

    private fun getLayoutManager(filterList: List<DynamicAttachmentRejectReasons.RejectReasonFeedbackForm.RejectReasonReasonChip>): RecyclerView.LayoutManager {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(i: Int): Int {
                return if (filterList.size % 2 == 0) {
                    1
                } else {
                    if (i == filterList.size - 1) 2 else 1
                }
            }
        }
        return gridLayoutManager
    }

    fun setUpListener(listener: ChatbotRejectReasonsListener) {
        this.listener = listener
    }

    companion object {

        fun newInstance(rejectReasonFeedbackForm: DynamicAttachmentRejectReasons.RejectReasonFeedbackForm): ChatbotRejectReasonsBottomSheet {
            return ChatbotRejectReasonsBottomSheet().apply {
                this.rejectReasonFeedbackForm = rejectReasonFeedbackForm
            }
        }

        const val MINIMUM_LINES = 3
    }

    interface ChatbotRejectReasonsListener {
        fun submitRejectReasonsViaSocket(
            selectedReasons: List<DynamicAttachmentRejectReasons.RejectReasonFeedbackForm.RejectReasonReasonChip>,
            reasonText: String
        )
    }
}
