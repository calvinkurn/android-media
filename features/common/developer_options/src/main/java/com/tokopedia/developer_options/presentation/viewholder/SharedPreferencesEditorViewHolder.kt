package com.tokopedia.developer_options.presentation.viewholder

import android.content.Intent
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.SharedPreferencesEditorUiModel
import com.tokopedia.developer_options.sharedpref.SharedPrefActivity
import com.tokopedia.unifycomponents.UnifyButton

class SharedPreferencesEditorViewHolder(
    itemView: View
): AbstractViewHolder<SharedPreferencesEditorUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shared_preferences_editor
    }

    override fun bind(element: SharedPreferencesEditorUiModel?) {
        val btn = itemView.findViewById<UnifyButton>(R.id.shared_preferences_editor_btn)
        btn.setOnClickListener {
            itemView.context.apply {
                startActivity(Intent(this, SharedPrefActivity::class.java))
            }
        }
    }
}