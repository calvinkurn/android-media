package com.tokopedia.developer_options.presentation.viewholder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.AppAuthSecretUiModel
import com.tokopedia.devicefingerprint.appauth.getDecoder
import com.tokopedia.unifycomponents.UnifyButton

class AppAuthSecretViewHolder(
    itemView: View
): AbstractViewHolder<AppAuthSecretUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_app_auth_secret
    }

    override fun bind(element: AppAuthSecretUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.app_auth_secret_btn)
        btn.setOnClickListener {
            itemView.context.apply {
                val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                val decoder = getDecoder(this)
                val clip = ClipData.newPlainText("Copied Text", decoder)
                clipboard?.setPrimaryClip(clip)
                Toast.makeText(this, decoder, Toast.LENGTH_LONG).show()
            }
        }
    }
}