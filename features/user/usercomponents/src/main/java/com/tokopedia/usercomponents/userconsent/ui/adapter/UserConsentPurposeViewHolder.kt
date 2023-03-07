package com.tokopedia.usercomponents.userconsent.ui.adapter

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.databinding.UiUserConsentItemBinding
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.common.UserConsentPurposeUiModel
import com.tokopedia.utils.view.binding.viewBinding

class UserConsentPurposeViewHolder(
    itemView: View,
    private val listener: UserConsentPurposeListener
): BaseViewHolder(itemView) {

    private val itemViewBinding: UiUserConsentItemBinding? by viewBinding()

    fun onBind(userConsentPurposeUiModel: UserConsentPurposeUiModel) {
        itemViewBinding?.apply {
            checkboxPurposes.text = userConsentPurposeUiModel.purposes.attribute.uiName
            checkboxPurposes.setOnCheckedChangeListener { _, isChecked ->
                listener.onCheckedChange(isChecked, userConsentPurposeUiModel.purposes)
            }
        }
    }

    interface UserConsentPurposeListener {
        fun onCheckedChange(
            isChecked: Boolean,
            purposeDataModel: UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.ui_user_consent_item
    }
}
