package com.tokopedia.chatbot.chatbot2.view.customview.chatroom

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.customview.chatroom.listener.ReplyBoxClickListener
import com.tokopedia.chatbot.chatbot2.view.listener.ChatbotSendButtonListener
import com.tokopedia.chatbot.databinding.BottomsheetChatbotBigReplyBoxBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
class BigReplyBoxBottomSheet : BottomSheetUnify(), ChatbotSendButtonListener {
    private var isSendButtonActivated: Boolean = false
    private var labelText = ""
    private var hintText = ""
    private var shouldShowAddAttachmentButton: Boolean = false
    private var messageText: String = ""
    private var isSendButtonClicked: Boolean = false
    private var isError: Boolean = false

    private var _viewBinding: BottomsheetChatbotBigReplyBoxBinding? = null
    private fun getBindingView() = _viewBinding!!

    init {
        isFullpage = false
        isCancelable = false
        showKnob = true
        showCloseIcon = false
        clearContentPadding = true
        showHeader = false
        isKeyboardOverlap = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = BottomsheetChatbotBigReplyBoxBinding.inflate(LayoutInflater.from(context))
        setChild(getBindingView().root)
        setUpTextWatcher()
        disableSendButton()
        bindClickListeners()
        setUpEditText()
        getBindingView().chatText.icon1.showWithCondition(shouldShowAddAttachmentButton)
        changeSendButtonIcon(isEnabled = true)
        if (isError) {
            setWordLengthError()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpEditText() {
        val chatText = getBindingView().chatText
        with(chatText) {
            minLine = MINIMUM_NUMBER_OF_LINES
            maxLine = MAXIMUM_NUMBER_OF_LINES
            labelText.text = this@BigReplyBoxBottomSheet.labelText
            context?.resources?.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                ?.let { labelText.setTextColor(it) }
            setPlaceholder(hintText)
            if (messageText.isNotEmpty()) {
                editText.setText(messageText)
            }
            editText.setHintTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
            val message = String.format(
                context?.resources?.getString(R.string.chatbot_remaining_words)
                    .toBlankOrString(),
                MINIMUM_NUMBER_OF_WORDS
            )
            setMessage(message)
            requestFocus()
        }
    }

    private fun setUpTextWatcher() {
        val textWatcher = getTextWatcherForMessage()
        getBindingView().chatText.editText.addTextChangedListener(textWatcher)
    }

    private fun bindClickListeners() {
        getBindingView().sendButton.setOnClickListener {
            if (isSendButtonActivated) {
                isSendButtonClicked = true
                replyBoxClickListener?.getMessageContentFromBottomSheet(
                    getBindingView().chatText.editText.text?.toString() ?: ""
                )
                dismissAllowingStateLoss()
            } else {
                setWordLengthError()
            }
        }
        getBindingView().chatText.icon1.setOnClickListener {
            hideKeyboard()
            dismissAllowingStateLoss()
            replyBoxClickListener?.onAttachmentMenuClicked()
        }

        setOnDismissListener {
            val text = getBindingView().chatText.editText.text?.toString() ?: ""
            hideKeyboard()
            if (isSendButtonClicked) {
                return@setOnDismissListener
            }
            replyBoxClickListener?.dismissBigReplyBoxBottomSheet(
                text,
                getWordCount()
            )
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(_viewBinding?.parent?.windowToken, 0)
    }

    fun hideAddAttachmentButton(state: Boolean) {
        getBindingView().chatText.icon1.showWithCondition(state)
    }

    private fun getWordCount(): Int {
        val words = getBindingView().chatText.editText.text?.toString()?.trim() ?: ""
        return words.split("\\s+".toRegex()).size
    }

    private fun getTextWatcherForMessage(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (getWordCount() >= MINIMUM_NUMBER_OF_WORDS) {
                    enableSendButton()
                }
                context?.resources?.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                    ?.let { _viewBinding?.chatText?.labelText?.setTextColor(it) }
            }

            override fun afterTextChanged(s: Editable?) {
                val wordCount = getWordCount()
                if (wordCount < MINIMUM_NUMBER_OF_WORDS) {
                    disableSendButton()
                }
                setWordLengthWarning(wordCount)
            }
        }
    }

    override fun disableSendButton() {
        isSendButtonActivated = false
    }

    override fun enableSendButton() {
        isSendButtonActivated = true
    }

    private fun changeSendButtonIcon(isEnabled: Boolean) {
        if (isEnabled) {
            getBindingView().sendButton.setImageResource(R.drawable.ic_chatbot_send)
        } else {
            getBindingView().sendButton.setImageResource(R.drawable.ic_chatbot_send_deactivated)
        }
    }

    private fun setWordLengthWarning(wordCount: Int) {
        getBindingView().chatText.run {
            if (isError) {
                setWordLengthError()
                isError = false
                return@run
            }
            if (this.editText.text.isEmpty()) {
                val message = String.format(
                    context?.resources?.getString(R.string.chatbot_remaining_words)
                        .toBlankOrString(),
                    MINIMUM_NUMBER_OF_WORDS
                )
                isInputError = false
                setMessage(message)
                return
            }
            if (wordCount < MINIMUM_NUMBER_OF_WORDS) {
                val message = String.format(
                    context?.resources?.getString(R.string.chatbot_remaining_words_while_typing)
                        .toBlankOrString(),
                    MINIMUM_NUMBER_OF_WORDS - wordCount
                )
                isInputError = false
                setMessage(message)
            } else {
                setMessage("")
            }
        }
    }

    private fun setWordLengthError() {
        getBindingView().chatText.run {
            val message = String.format(
                context?.resources?.getString(R.string.chatbot_big_reply_word_error)
                    .toBlankOrString(),
                MINIMUM_NUMBER_OF_WORDS
            )
            setMessage(message)
            isInputError = true
            context?.resources?.getColor(com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                ?.let { labelText.setTextColor(it) }
        }
    }

    fun clearMessageText() {
        getBindingView().chatText.editText.setText("")
        messageText = ""
    }

    fun setErrorStatus(errorStatus: Boolean) {
        this.isError = errorStatus
    }

    companion object {
        @JvmStatic
        fun newInstance(
            replyBoxBottomSheetPlaceHolder: String,
            replyBoxBottomSheetTitle: String,
            shouldShowAddAttachmentButton: Boolean,
            msgText: String = ""
        ): BigReplyBoxBottomSheet {
            return BigReplyBoxBottomSheet().apply {
                this.labelText = replyBoxBottomSheetTitle
                this.hintText = replyBoxBottomSheetPlaceHolder
                this.shouldShowAddAttachmentButton = shouldShowAddAttachmentButton
                this.messageText = msgText
            }
        }

        var replyBoxClickListener: ReplyBoxClickListener? = null
        const val MINIMUM_NUMBER_OF_WORDS = 5
        const val MINIMUM_NUMBER_OF_LINES = 3
        const val MAXIMUM_NUMBER_OF_LINES = 10
    }
}
