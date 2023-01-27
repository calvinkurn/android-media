package com.tokopedia.chatbot.chatbot2.view.customview.chatroom

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.customview.chatroom.listener.ReplyBoxClickListener
import com.tokopedia.chatbot.chatbot2.view.listener.ChatbotSendButtonListener
import com.tokopedia.chatbot.databinding.BottomsheetChatbotBigReplyBoxBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class BigReplyBoxBottomSheet : BottomSheetUnify(), ChatbotSendButtonListener {

    private var context: FragmentActivity? = null
    private var isSendButtonActivated: Boolean = false
    private var labelText = ""
    private var hintText = ""

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
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpEditText() {
        val chatText = getBindingView().chatText
        with(chatText) {
            minLine = MINIMUM_NUMBER_OF_LINES
            maxLine = MAXIMUM_NUMBER_OF_LINES
            labelText.text = this@BigReplyBoxBottomSheet.labelText
            setPlaceholder(hintText)
            editText.setHintTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        }
    }

    private fun setUpTextWatcher() {
        val textWatcher = getTextWatcherForMessage()
        getBindingView().chatText.editText.addTextChangedListener(textWatcher)
    }

    private fun bindClickListeners() {
        getBindingView().sendButton.setOnClickListener {
            if (isSendButtonActivated) {
                replyBoxClickListener?.getMessageContentFromBottomSheet(
                    getBindingView().chatText.editText.text?.toString() ?: ""
                )
                dismissAllowingStateLoss()
            }
        }
        getBindingView().ivChatMenu.setOnClickListener {
            dismissAllowingStateLoss()
            replyBoxClickListener?.onAttachmentMenuClicked()
        }
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
            }

            override fun afterTextChanged(s: Editable?) {
                if (getWordCount() < MINIMUM_NUMBER_OF_WORDS) {
                    disableSendButton()
                }
            }
        }
    }

    override fun disableSendButton() {
        getBindingView().sendButton.setImageResource(R.drawable.ic_chatbot_send_deactivated)
        isSendButtonActivated = false
    }

    override fun enableSendButton() {
        getBindingView().sendButton.setImageResource(R.drawable.ic_chatbot_send)
        isSendButtonActivated = true
    }

    companion object {
        @JvmStatic
        fun newInstance(
            context: FragmentActivity,
            replyBoxBottomSheetPlaceHolder: String,
            replyBoxBottomSheetTitle: String
        ): BigReplyBoxBottomSheet {
            return BigReplyBoxBottomSheet().apply {
                this.context = context
                this.labelText = replyBoxBottomSheetTitle
                this.hintText = replyBoxBottomSheetPlaceHolder
            }
        }

        var replyBoxClickListener: ReplyBoxClickListener? = null
        const val MINIMUM_NUMBER_OF_WORDS = 2
        const val MINIMUM_NUMBER_OF_LINES = 3
        const val MAXIMUM_NUMBER_OF_LINES = 10
    }
}
