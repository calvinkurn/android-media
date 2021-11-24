package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.AccessTokenUiModel
import com.tokopedia.unifycomponents.UnifyButton

class AccessTokenViewHolder(
    itemView: View,
    private val listener: AccessTokenListener
): AbstractViewHolder<AccessTokenUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_access_token
    }

    override fun bind(element: AccessTokenUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.access_token_btn)
        btn.text = String.format("Access Token: ${listener.getAccessToken()}")
        btn.setOnClickListener {
            listener.onClickAccessTokenBtn()
        }
    }

    interface AccessTokenListener {
        fun onClickAccessTokenBtn()
        fun getAccessToken() : String
    }
}