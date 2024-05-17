package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.SSOAuthorizationUiModel
import com.tokopedia.unifycomponents.UnifyButton

class SSOAuthorizationViewHolder(
    itemView: View,
    private val listener: LoginSSOListener
) : AbstractViewHolder<SSOAuthorizationUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_lark_sso
    }

    override fun bind(element: SSOAuthorizationUiModel?) {
        val iconLark = ContextCompat.getDrawable(itemView.context, R.drawable.ic_lark)
        val btn = itemView.findViewById<UnifyButton>(R.id.btnLoginSSO)
        btn.setDrawable(iconLark, UnifyButton.DrawablePosition.LEFT)
        btn.setOnClickListener {
            itemView.context.apply {
                listener.onClickLogin()
            }
        }
    }

    interface LoginSSOListener {
        fun onClickLogin()
    }
}
