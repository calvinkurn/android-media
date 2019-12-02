package com.tokopedia.play.ui.sendchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.sendchat.interaction.SendChatInteractionEvent

/**
 * Created by jegul on 02/12/19
 */
class ChatFormView(container: ViewGroup, listener: Listener) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_chat_form, container, false)

    override val containerId: Int = view.id

    init {
        container.addView(view)

        view.findViewById<EditText>(R.id.et_chat)
                .setOnClickListener {
                    listener.onChatFormClicked(this)
                }

        view.findViewById<ImageView>(R.id.iv_send)
                .setOnClickListener {
                    listener.onSendChatClicked(this)
                }
    }

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    interface Listener {
        fun onChatFormClicked(view: ChatFormView)
        fun onSendChatClicked(view: ChatFormView)
    }
}