package com.tokopedia.updateinactivephone.features.accountlist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.databinding.ItemInactivePhoneAccountListBinding
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.utils.view.binding.viewBinding

class AccountListViewHolder(
        itemView: View,
        private val listener: Listener
): BaseViewHolder(itemView) {

    private var itemViewBinding: ItemInactivePhoneAccountListBinding? by viewBinding()

    fun onBind(userDetailDataModel: AccountListDataModel.UserDetailDataModel) {
        itemViewBinding?.apply {
            imgAvatar.loadImageCircle(userDetailDataModel.image)
            txtName.text = userDetailDataModel.fullname
            txtEmail.text = userDetailDataModel.email
        }

        itemView.setOnClickListener {
            listener.onItemClick(userDetailDataModel)
        }
    }

    interface Listener {
        fun onItemClick(userDetailDataModel: AccountListDataModel.UserDetailDataModel)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_inactive_phone_account_list
    }
}