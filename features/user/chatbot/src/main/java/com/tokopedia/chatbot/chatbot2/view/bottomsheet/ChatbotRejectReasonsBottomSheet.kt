package com.tokopedia.chatbot.chatbot2.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.chatbot2.data.rejectreasons.DynamicAttachmentRejectReasons
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.adapter.ChatbotRejectReasonsAdapter
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.listener.ChatbotRejectReasonsChipListener
import com.tokopedia.chatbot.databinding.BottomSheetChatbotReasonsBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ChatbotRejectReasonsBottomSheet : BottomSheetUnify() {

    private var rejectReasonFeedbackForm: DynamicAttachmentRejectReasons? = null
    private var binding by autoClearedNullable<BottomSheetChatbotReasonsBinding>()
    private var reasonsAdapter: ChatbotRejectReasonsAdapter? = null
    private var listener: ChatbotRejectReasonsListener? = null
    private var chipSelectedListener: ChatbotRejectReasonsChipListener? = null
    private var isChipSelected: Boolean = false
    private var isEnabledFromText: Boolean = false
    private var charCount: Long = 0
    private var selectedReasonList = mutableListOf<DynamicAttachmentRejectReasons.RejectReasonFeedbackForm.RejectReasonReasonChip>()
    private var isSendButtonClicked: Boolean = false
    private var reasonTextString: String = ""

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
        rejectReasonFeedbackForm?.feedbackForm?.let { data ->
            binding?.apply {
                setUpText(data)
                renderReasonText(data)
                initializeAdapter(data)
                setUpClickListener()
                setUpTextWatcher()
            }
            handleButtonState()
        }
    }

    private fun BottomSheetChatbotReasonsBinding.setUpText(data: DynamicAttachmentRejectReasons.RejectReasonFeedbackForm) {
        setTitle(data.title)
        senderIcon.urlSrc = data.iconTanya
        reasonTitleText.text = data.reasonTitle
        charCount = data.reasonMinimumCharacter.toLongOrZero()
    }
    private fun BottomSheetChatbotReasonsBinding.renderReasonText(data: DynamicAttachmentRejectReasons.RejectReasonFeedbackForm) {
        reasonText.apply {
            setPlaceholder(data.textBoxPlaceHolder)
            minLine = MINIMUM_LINES
            labelText.hide()
            editText.setHintTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
            context?.resources?.apply {
                setMessage(
                    getString(com.tokopedia.chatbot.R.string.chatbot_reject_reasons_min) +
                        data.reasonMinimumCharacter +
                        getString(
                            com.tokopedia.chatbot.R.string.chatbot_reject_reasons_character
                        )
                )
            }
        }
    }

    private fun BottomSheetChatbotReasonsBinding.initializeAdapter(data: DynamicAttachmentRejectReasons.RejectReasonFeedbackForm) {
        reasonsAdapter = ChatbotRejectReasonsAdapter(chipSelectedListener)
        reasonsList.apply {
            layoutManager = getMyLayoutManager()
            reasonsAdapter?.setList(data.reasonChipList, selectedReasonList)
            adapter = reasonsAdapter
        }
    }

    fun clearChipList() {
        reasonsAdapter?.selectedList?.clear()
    }

    private fun BottomSheetChatbotReasonsBinding.setUpClickListener() {
        btnSubmit.setOnClickListener {
            isSendButtonClicked = true
            listener?.submitRejectReasonsViaSocket(
                reasonsAdapter?.selectedList ?: emptyList(),
                reasonText.editText.text?.toString() ?: "",
                rejectReasonFeedbackForm?.helpfulQuestion
            )
            reasonText.editText.setText("")

            dismiss()
        }

        setOnDismissListener {
            if (!isSendButtonClicked) {
                listener?.isDismissClicked(
                    true,
                    binding?.reasonText?.editText?.text?.toString()?.trim() ?: ""
                )
            }
        }
    }

    fun setText(text: String) {
        reasonTextString = text
    }

    private fun getMyLayoutManager(): RecyclerView.LayoutManager {
        val gridLayoutManager = GridLayoutManager(context, GRID_SPAN_2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position < GRID_SPAN_2) GRID_SPAN_1 else GRID_SPAN_2
            }
        }
        return gridLayoutManager
    }

    fun checkChipCounter(count: Int) {
        isChipSelected = count > ZERO
        handleButtonState()
    }

    fun setUpListener(listener: ChatbotRejectReasonsListener) {
        this.listener = listener
    }

    fun setUpChipClickListener(listener: ChatbotRejectReasonsChipListener) {
        this.chipSelectedListener = listener
    }

    fun updateSendButtonStatus(sendButtonStatus: Boolean) {
        this.isSendButtonClicked = sendButtonStatus
    }

    private fun handleButtonState() {
        binding?.btnSubmit?.isEnabled = (isEnabledFromText && isChipSelected)
    }

    private fun setUpTextWatcher() {
        val textWatcher = getTextWatcherForMessage()
        binding?.reasonText?.editText?.addTextChangedListener(textWatcher)
    }

    private fun getTextWatcherForMessage(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (getCharCount() >= MINIMUM_CHAR) {
                    isEnabledFromText = true
                    handleButtonState()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val wordCount = getCharCount()
                if (wordCount < MINIMUM_CHAR) {
                    isEnabledFromText = false
                    handleButtonState()
                }
            }
        }
    }

    private fun getCharCount(): Int {
        val words = binding?.reasonText?.editText?.text?.toString()?.trim() ?: ""
        return words.count { it != ' ' }
    }

    override fun onResume() {
        super.onResume()
        binding?.reasonText?.editText?.setText(reasonTextString)
    }

    companion object {

        fun newInstance(
            rejectReasonFeedbackForm: DynamicAttachmentRejectReasons? = null,
            reasonList: MutableList<DynamicAttachmentRejectReasons.RejectReasonFeedbackForm.RejectReasonReasonChip>,
            text: String
        ): ChatbotRejectReasonsBottomSheet {
            return ChatbotRejectReasonsBottomSheet().apply {
                this.rejectReasonFeedbackForm = rejectReasonFeedbackForm
                this.selectedReasonList = reasonList
                this.reasonTextString = text
            }
        }

        const val MINIMUM_LINES = 5
        const val MINIMUM_CHAR = 30
        const val ZERO = 0
        const val GRID_SPAN_1 = 1
        const val GRID_SPAN_2 = 2
    }

    interface ChatbotRejectReasonsListener {
        fun submitRejectReasonsViaSocket(
            selectedReasons: List<DynamicAttachmentRejectReasons.RejectReasonFeedbackForm.RejectReasonReasonChip>,
            reasonText: String,
            helpfulQuestion: DynamicAttachmentRejectReasons.RejectReasonHelpfulQuestion?
        )

        fun isDismissClicked(isDismissClickOnRejectReasons: Boolean, text: String)
    }
}
