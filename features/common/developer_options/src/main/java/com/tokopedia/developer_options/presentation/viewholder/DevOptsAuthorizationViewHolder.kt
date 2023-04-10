package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.DevOptsAuthorizationUiModel
import com.tokopedia.encryption.security.sha256
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

class DevOptsAuthorizationViewHolder(
    itemView: View,
    private val listener: DevOptsAuthorizationListener
): AbstractViewHolder<DevOptsAuthorizationUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dev_opts_authorization
    }

    override fun bind(element: DevOptsAuthorizationUiModel?) {
        val btn = itemView.findViewById<UnifyButton>(R.id.dev_opts_auth_btn)
        val tf = itemView.findViewById<TextFieldUnify>(R.id.dev_opts_auth_tf)
        btn.setOnClickListener {
            val password = tf.textFieldInput.text.toString()
            itemView.context.apply {
                if (password.isBlank()) {
                    Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
                } else {
                    listener.onSubmitDevOptsPassword(password.sha256())
                }
            }
        }
    }

    interface DevOptsAuthorizationListener {
        fun onSubmitDevOptsPassword(sha256: String)
    }
}
