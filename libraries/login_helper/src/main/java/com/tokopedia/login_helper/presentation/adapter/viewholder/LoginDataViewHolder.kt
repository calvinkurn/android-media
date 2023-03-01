package com.tokopedia.login_helper.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.login_helper.databinding.ItemLoginDataBinding
import com.tokopedia.login_helper.databinding.ItemLoginHeaderBinding
import com.tokopedia.login_helper.domain.uiModel.LoginDataUiModel
import com.tokopedia.login_helper.domain.uiModel.UserDataUiModel
import com.tokopedia.utils.view.binding.viewBinding

class LoginDataViewHolder(itemView: View?, private val listener: LoginHelperClickListener) :
    AbstractViewHolder<UserDataUiModel>(itemView) {

    private var binding: ItemLoginDataBinding? by viewBinding()
    override fun bind(element: UserDataUiModel?) {
        binding?.apply {
            if (element?.email != null) {
                userEmail.text = element.email
            }
            if (element?.tribe != null && element.tribe.isNotEmpty()){
                tribeChip.chipText = element.tribe
            }
            root.setOnClickListener {
                listener.onClickUserData(element)
            }
        }
    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = com.tokopedia.login_helper.R.layout.item_login_data
    }

}

interface LoginHelperClickListener {
    fun onClickUserData(data: UserDataUiModel?)
}
