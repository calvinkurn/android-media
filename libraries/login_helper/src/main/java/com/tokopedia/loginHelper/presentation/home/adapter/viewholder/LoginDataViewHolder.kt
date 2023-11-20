package com.tokopedia.loginHelper.presentation.home.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.loginHelper.databinding.ItemLoginDataBinding
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel
import com.tokopedia.utils.view.binding.viewBinding

class LoginDataViewHolder(itemView: View?, private val listener: LoginHelperClickListener) :
    AbstractViewHolder<UserDataUiModel>(itemView) {

    private var binding: ItemLoginDataBinding? by viewBinding()
    override fun bind(element: UserDataUiModel?) {
        binding?.apply {
            if (element?.email != null) {
                userEmail.text = element.email
            }
            if (element?.tribe != null && element.tribe.isNotEmpty()) {
                tribeChip.chipText = element.tribe
            } else {
                tribeChip.hide()
            }
            root.setOnClickListener {
                listener.onClickUserData(element)
            }
        }
    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = com.tokopedia.loginHelper.R.layout.item_login_data
    }
}

interface LoginHelperClickListener {
    fun onClickUserData(data: UserDataUiModel?)
}
