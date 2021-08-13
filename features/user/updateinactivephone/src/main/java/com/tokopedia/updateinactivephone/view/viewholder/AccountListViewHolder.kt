package com.tokopedia.updateinactivephone.view.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.utils.image.ImageUtils

class AccountListViewHolder(
        itemView: View,
        private val listener: Listener
): BaseViewHolder(itemView) {

    private val imgAvatar = itemView.findViewById<ImageUnify>(R.id.imgAvatar)
    private val txtName = itemView.findViewById<Typography>(R.id.txtName)
    private val txtEmail = itemView.findViewById<Typography>(R.id.txtEmail)

    fun onBind(userDetailDataModel: AccountListDataModel.UserDetailDataModel) {
        itemView.context?.let {
            ImageUtils.loadImageCircle2(it, imgAvatar, userDetailDataModel.image)
        }

        txtName.text = userDetailDataModel.fullname
        txtEmail.text = userDetailDataModel.email

        itemView.setOnClickListener {
            listener.onItemClick(userDetailDataModel)
        }
    }

    interface Listener {
        fun onItemClick(userDetailDataModel: AccountListDataModel.UserDetailDataModel)
    }
}