package com.tokopedia.login_helper.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.login_helper.domain.uiModel.LoginDataUiModel

class LoginDataViewHolder(itemView: View?): AbstractViewHolder<LoginDataUiModel>(itemView) {

    override fun bind(element: LoginDataUiModel?) {

    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = com.tokopedia.login_helper.R.layout.item_login_data
    }

}
