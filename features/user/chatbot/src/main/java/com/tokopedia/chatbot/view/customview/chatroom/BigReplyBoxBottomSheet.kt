package com.tokopedia.chatbot.view.customview.chatroom

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.listener.ChatbotSendButtonListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify2

class BigReplyBoxBottomSheet : BottomSheetUnify(), ChatbotSendButtonListener {

    private lateinit var context: FragmentActivity
    private var sendButton: ImageView? = null
    private var messageText: TextFieldUnify2? = null
    private var isSendButtonActivated: Boolean = false

    init {
        isFullpage = false
        isCancelable = false
        showKnob = true
        showCloseIcon = false
        clearContentPadding = true
    }

    companion object {
        @JvmStatic
        fun newInstance(context: FragmentActivity): BigReplyBoxBottomSheet {
            return BigReplyBoxBottomSheet().apply {
                this.context = context
            }
        }

        val LAYOUT = R.layout.bottom_sheet_big_reply_box
        const val MINIMUM_NUMBER_OF_WORDS = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = View.inflate(context, LAYOUT, null)
        setChild(contentView)
        contentView.run {
            sendButton = findViewById(R.id.send_but)
            messageText = findViewById(R.id.chat_text)
        }
        setUpTextWatcher()
        disableSendButton()
        bindClickListeners()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpTextWatcher() {
        val textWatcher = getTextWatcherForMessage()
        messageText?.editText?.addTextChangedListener(textWatcher)
    }

    private fun bindClickListeners() {
        sendButton?.setOnClickListener {

        }
    }

    private fun getWordCount(): Int {
        val words = messageText?.editText?.text?.toString()?.trim() ?: ""
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
        sendButton?.setImageResource(R.drawable.ic_chatbot_send_deactivated)
        isSendButtonActivated = false
    }

    override fun enableSendButton() {
        sendButton?.setImageResource(R.drawable.ic_chatbot_send)
        isSendButtonActivated = true
    }

}
