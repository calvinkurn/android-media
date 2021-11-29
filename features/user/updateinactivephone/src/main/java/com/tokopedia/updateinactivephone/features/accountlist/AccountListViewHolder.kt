package com.tokopedia.updateinactivephone.features.accountlist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.databinding.FragmentInactivePhoneDataUploadBinding
import com.tokopedia.updateinactivephone.databinding.ItemInactivePhoneAccountListBinding
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.binding.viewBinding

class AccountListViewHolder(
        itemView: View,
        private val listener: Listener
): BaseViewHolder(itemView) {

    private var itemViewBinding: ItemInactivePhoneAccountListBinding? by viewBinding()

    fun onBind(userDetailDataModel: AccountListDataModel.UserDetailDataModel) {
        itemView.context?.let {
            itemViewBinding?.imgAvatar?.let { avatar ->
                ImageUtils.loadImageCircle2(it,
                    avatar, userDetailDataModel.image)
            }
        }

        itemViewBinding?.txtName?.text = userDetailDataModel.fullname
        itemViewBinding?.txtEmail?.text = userDetailDataModel.email

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