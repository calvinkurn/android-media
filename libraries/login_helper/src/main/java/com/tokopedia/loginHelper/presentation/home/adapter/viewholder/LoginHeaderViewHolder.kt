package com.tokopedia.loginHelper.presentation.home.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.loginHelper.databinding.ItemLoginHeaderBinding
import com.tokopedia.loginHelper.domain.uiModel.users.HeaderUiModel
import com.tokopedia.utils.view.binding.viewBinding

class LoginHeaderViewHolder(itemView: View?) : AbstractViewHolder<HeaderUiModel>(itemView) {

    private var binding: ItemLoginHeaderBinding? by viewBinding()

    override fun bind(element: HeaderUiModel?) {
        binding?.apply {
            if (element?.userCount == 0) {
                this.root.hide()
            } else {
                val dataText = element?.title + "(" + element?.userCount?.toString() + ")"
                headerText.text = dataText
            }
        }
    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = com.tokopedia.loginHelper.R.layout.item_login_header
    }
}
