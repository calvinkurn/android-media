package com.tokopedia.developer_options.presentation.viewholder

import android.content.Intent
import android.text.Editable
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity
import com.tokopedia.developer_options.presentation.model.RemoteConfigEditorUiModel
import com.tokopedia.developer_options.remote_config.RemoteConfigFragmentActivity
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

class RemoteConfigEditorViewHolder(
    itemView: View
): AbstractViewHolder<RemoteConfigEditorUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_remote_config_editor
    }

    override fun bind(element: RemoteConfigEditorUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.remote_config_editor_btn)
        val tf = itemView.findViewById<TextFieldUnify>(R.id.remote_config_editor_tf)
        btn.setOnClickListener {
            val prefix: Editable = tf.textFieldInput.editableText
            startRemoteConfigEditor(prefix.toString())
        }
    }

    private fun startRemoteConfigEditor(prefix: String) {
        itemView.context.apply {
            val intent = Intent(this, RemoteConfigFragmentActivity::class.java)
            intent.putExtra(DeveloperOptionActivity.REMOTE_CONFIG_PREFIX, prefix.trim { it <= ' ' })
            startActivity(intent)
        }
    }
}