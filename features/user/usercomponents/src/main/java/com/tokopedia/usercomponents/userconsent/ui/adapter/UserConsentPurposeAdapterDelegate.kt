package com.tokopedia.usercomponents.userconsent.ui.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.usercomponents.userconsent.common.UserConsentPurposeUiModel

class UserConsentPurposeAdapterDelegate(
    private val listener: UserConsentPurposeViewHolder.UserConsentPurposeListener
): TypedAdapterDelegate<UserConsentPurposeUiModel, UserConsentPurposeUiModel, UserConsentPurposeViewHolder>(
    UserConsentPurposeViewHolder.LAYOUT
) {

    override fun onBindViewHolder(item: UserConsentPurposeUiModel, holder: UserConsentPurposeViewHolder) {
        holder.onBind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): UserConsentPurposeViewHolder {
        return UserConsentPurposeViewHolder(basicView, listener)
    }
}