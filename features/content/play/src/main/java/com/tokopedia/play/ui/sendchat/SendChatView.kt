package com.tokopedia.play.ui.sendchat

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 02/12/19
 */
class SendChatView(container: ViewGroup, listener: Listener) : UIView(container) {

    companion object {
        private const val MAX_CHARS = 140
    }

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_chat_form, container, true)
                    .findViewById(R.id.cl_chat_form)

    private val etChat: EditText = view.findViewById(R.id.et_chat)
    private val ivSend: ImageView = view.findViewById(R.id.iv_send)

    private var prevText = ""

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (s.isNotEmpty()) ivSend.visible() else ivSend.gone()

            if (s.length > MAX_CHARS) {
                if (prevText.length < MAX_CHARS) prevText = s.substring(0, MAX_CHARS)
                s.replace(0, s.length, prevText)
            }
            else prevText = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    init {
        etChat.addTextChangedListener(textWatcher)

        etChat.setOnClickListener {
            listener.onChatFormClicked(this)
        }

        ivSend.setOnClickListener {
            val message: String = etChat.text.toString()
            if (message.isNotEmpty() && message.isNotBlank()) {
                listener.onSendChatClicked(this, message)
                etChat.setText("")
            }
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    fun focusChatForm() {
        if (!etChat.isFocusable) {
            etChat.apply {
                isFocusable = true
                isFocusableInTouchMode = true
            }
        }

        if (!etChat.hasFocus()) etChat.requestFocus()

    }

    interface Listener {
        fun onChatFormClicked(view: SendChatView)
        fun onSendChatClicked(view: SendChatView, message: String)
    }
}