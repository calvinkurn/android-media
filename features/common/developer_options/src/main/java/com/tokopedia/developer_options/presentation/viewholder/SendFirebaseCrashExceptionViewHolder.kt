package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.SendFirebaseCrashExceptionUiModel
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

class SendFirebaseCrashExceptionViewHolder(
    itemView: View,
    private val listener: SendFirebaseCrashListener
): AbstractViewHolder<SendFirebaseCrashExceptionUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_send_firebase_exception
    }

    override fun bind(element: SendFirebaseCrashExceptionUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.send_firebase_crash_btn)
        val tf = itemView.findViewById<TextFieldUnify>(R.id.firebase_crash_tf)
        btn.text = element.text
        btn.setOnClickListener {
            val crashMessage = tf.textFieldInput.text.toString()
            listener.onClickSendFirebaseCrashBtn(crashMessage)
        }
    }

    interface SendFirebaseCrashListener {
        fun onClickSendFirebaseCrashBtn(message: String)
    }
}