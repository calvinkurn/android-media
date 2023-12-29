package com.tokopedia.loginHelper.presentation.searchAccount.adapter.viewholder

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.loginHelper.R
import com.tokopedia.loginHelper.databinding.ItemLoginSearchBinding
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel
import com.tokopedia.loginHelper.presentation.searchAccount.adapter.listener.LoginHelperSearchListener

class LoginHelperSearchViewHolder(
    private val binding: ItemLoginSearchBinding,
    private val listener: LoginHelperSearchListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserDataUiModel) {
        binding.apply {
            userEmail.text = user.email
            editAccount.setOnClickListener {
                listener.onEditAccount(user)
            }
            deleteAccount.setOnClickListener {
                listener.onDeleteAccount(user)
            }
        }
    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_login_search
    }
}
