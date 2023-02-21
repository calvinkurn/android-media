package com.tokopedia.login_helper.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.login_helper.domain.uiModel.HeaderUiModel

class LoginHeaderViewHolder(itemView: View?): AbstractViewHolder<HeaderUiModel>(itemView) {
    override fun bind(element: HeaderUiModel?) {

    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = com.tokopedia.login_helper.R.layout.item_login_header
    }
}
