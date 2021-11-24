package com.tokopedia.developer_options.presentation.viewholder

import android.content.Intent
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.RequestNewFcmTokenUiModel
import com.tokopedia.developer_options.presentation.service.DeleteFirebaseTokenService
import com.tokopedia.unifycomponents.UnifyButton

class RequestNewFcmTokenViewHolder(
    itemView: View
): AbstractViewHolder<RequestNewFcmTokenUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_request_new_fcm_token
    }

    override fun bind(element: RequestNewFcmTokenUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.request_new_fcm_btn)
        btn.setOnClickListener {
            itemView.context.apply {
                val intent = Intent(this, DeleteFirebaseTokenService::class.java)
                startService(intent)
            }
        }
    }
}