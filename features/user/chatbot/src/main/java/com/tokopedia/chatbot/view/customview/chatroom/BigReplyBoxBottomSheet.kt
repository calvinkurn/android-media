package com.tokopedia.chatbot.view.customview.chatroom

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.databinding.BottomSheetBigReplyBoxBinding
import com.tokopedia.chatbot.view.customview.chatroom.listener.ReplyBoxClickListener
import com.tokopedia.chatbot.view.listener.ChatbotSendButtonListener
import com.tokopedia.unifycomponents.BottomSheetUnify

class BigReplyBoxBottomSheet : BottomSheetUnify(), ChatbotSendButtonListener {

    private lateinit var context: FragmentActivity
    private var isSendButtonActivated: Boolean = false
    private var labelText = ""
    private var hintText = ""

    private var _viewBinding: BottomSheetBigReplyBoxBinding? = null
    private fun getBindingView() = _viewBinding!!

    init {
        isFullpage = false
        isCancelable = false
        showKnob = false
        showCloseIcon = false
        clearContentPadding = true
        showHeader = false
        clearContentPadding = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = BottomSheetBigReplyBoxBinding.inflate(LayoutInflater.from(context))
        setChild(getBindingView().root)
        setUpTextWatcher()
        disableSendButton()
        bindClickListeners()
        getBindingView().chatText.minLine = 3
        getBindingView().chatText.labelText.text = labelText
        getBindingView().chatText.textInputLayout.hint= hintText
     //   getBindingView().chatText.editText.unify_text_field_message = ""
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpTextWatcher() {
        val textWatcher = getTextWatcherForMessage()
        getBindingView().chatText.editText.addTextChangedListener(textWatcher)
    }

    private fun bindClickListeners() {
        getBindingView().sendButton.setOnClickListener {
            replyBoxClickListener?.getMessageContentFromBottomSheet(
                getBindingView().chatText.editText.text?.toString() ?: ""
            )
            dismissAllowingStateLoss()
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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

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
        val LAYOUT = R.layout.bottom_sheet_big_reply_box
        const val MINIMUM_NUMBER_OF_WORDS = 2
    }

}
