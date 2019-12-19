package com.tokopedia.play.ui.sendchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 02/12/19
 */
class SendChatView(container: ViewGroup, listener: Listener) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_chat_form, container, true)
                    .findViewById(R.id.cl_chat_form)

    private val etChat: EditText = view.findViewById(R.id.et_chat)

    init {
        etChat.setOnClickListener {
            listener.onChatFormClicked(this)
        }

        view.findViewById<ImageView>(R.id.iv_send)
                .setOnClickListener {
                    val message: String = etChat.text.toString()
                    if (message.isNotEmpty() && message.isNotBlank()) {
                        listener.onSendChatClicked(this, message)
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