package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.LoginHelperUiModel
import com.tokopedia.unifycomponents.UnifyButton

class LoginHelperViewHolder(
    itemView: View,
    private val loginHelperListener: LoginHelperListener
) : AbstractViewHolder<LoginHelperUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_login_helper
    }

    override fun bind(element: LoginHelperUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.login_helper_btn)
        btn.setOnClickListener {
            loginHelperListener.routeToLoginHelperActivity()
        }
    }
}

interface LoginHelperListener {
    fun routeToLoginHelperActivity()
}
